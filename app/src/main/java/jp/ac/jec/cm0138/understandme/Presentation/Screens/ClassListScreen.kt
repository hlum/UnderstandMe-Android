package jp.ac.jec.cm0138.understandme.Presentation.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import jp.ac.jec.cm0138.understandme.Presentation.Screens.Components.ClassItemView
import jp.ac.jec.cm0138.understandme.Presentation.ViewModels.ClassListScreenViewModel
import jp.ac.jec.cm0138.understandme.R
import jp.ac.jec.cm0138.understandme.Repository.TestRepo.TestAuthRepository
import jp.ac.jec.cm0138.understandme.Repository.TestRepo.TestClassRepository
import jp.ac.jec.cm0138.understandme.UseCase.ClassUseCase
import jp.ac.jec.cm0138.understandme.customTheme.CustomTypography
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ClassListScreen(
    modifier: Modifier = Modifier,
    viewModel: ClassListScreenViewModel = hiltViewModel(),
    navController: NavController,
) {
    LaunchedEffect(Unit) {
        viewModel.loadClasses()
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Left spacer to balance the icon on the right
                Spacer(modifier = Modifier.width(48.dp))

                Text(
                    text = "科目一覧",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = CustomTypography.header
                )

                IconButton(
                    onClick = {
                        viewModel.toggleAddOptionalClassSheet(true)
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.plus_circle),
                        contentDescription = "Add Class",
                        tint = MyAppTheme.colors.accent,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        if (viewModel.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (viewModel.classes.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "登録されている科目がありません",
                    style = CustomTypography.body.copy(
                        fontSize = 16.sp,
                        color = Color.Gray
                    ),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                items(items = viewModel.classes, key = { it.id }) { classItem ->
                    ClassItemView(
                        classID = classItem.id,
                        className = classItem.name,
                        teacherName = classItem.teacherName,
                        showIcon = true,
                        navController = navController,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }

        // Bottom Sheet for adding optional class
        if (viewModel.showAddOptionalClassSheet) {
            AddOptionalClassBottomSheet(
                classCode = viewModel.classCode,
                errorMessage = viewModel.classCodeErrorMessage,
                onClassCodeChange = { viewModel.updateClassCode(it) },
                onDismiss = { viewModel.toggleAddOptionalClassSheet(false) },
                onAddClass = { viewModel.addOptionalClass() }
            )
        }

        // Error Alert Dialog
        if (viewModel.showErrorAlert) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissErrorAlert() },
                title = { Text("エラーが発生しました") },
                text = { Text(viewModel.errorMessage) },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissErrorAlert() }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOptionalClassBottomSheet(
    classCode: String,
    errorMessage: String,
    onClassCodeChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onAddClass: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "クラスコードを入力して参加",
                style = CustomTypography.header.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "選択科目のコードは担当の先生から受け取ってください。",
                style = CustomTypography.body.copy(
                    color = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = classCode,
                onValueChange = onClassCodeChange,
                label = { Text("科目コードを入力") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                singleLine = true,
                isError = errorMessage.isNotEmpty()
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(if (errorMessage.isEmpty()) 16.dp else 12.dp))

            Button(
                onClick = {
                    isLoading = true
                    onAddClass()
                    scope.launch {
                        kotlinx.coroutines.delay(500)
                        isLoading = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                enabled = classCode.isNotEmpty() && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (classCode.isEmpty() || isLoading)
                        Color.Gray.copy(alpha = 0.3f)
                    else
                        MyAppTheme.colors.accent,
                    contentColor = if (classCode.isEmpty() || isLoading)
                        Color.Gray
                    else
                        Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Gray
                    )
                } else {
                    Text(
                        text = "参加する",
                        style = CustomTypography.body.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun ClassListScreenPreview() {
    val navController = rememberNavController()
    Scaffold { innerPadding ->
        ClassListScreen(
            viewModel = ClassListScreenViewModel(authenticationRepository = TestAuthRepository(), classUseCase = ClassUseCase(
                classRepository = TestClassRepository()
            )),
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}