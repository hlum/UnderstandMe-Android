package jp.ac.jec.cm0138.understandme.Presentation.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import jp.ac.jec.cm0138.understandme.Presentation.Screens.Components.ClassItemView
import jp.ac.jec.cm0138.understandme.customTheme.CustomTypography
import jp.ac.jec.cm0138.understandme.customTheme.NotoSansJP

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ClassListScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Text(
                text = "科目一覧",
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = CustomTypography.header
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding)
        ) {
            items(10) {
                ClassItemView(
                    classID = "",
                    className = "科目mei",
                    teacherName = "先生の名前",
                    showIcon = true,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun ClassListScreenPreview() {
    val navController = rememberNavController()
    Scaffold { innerPadding ->
        ClassListScreen(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}