package jp.ac.jec.cm0138.understandme.Presentation.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import jp.ac.jec.cm0138.understandme.Entity.HomeworkFilterOption
import jp.ac.jec.cm0138.understandme.Presentation.Navigation.ANSWER_QUESTIONS_ROUTE
import jp.ac.jec.cm0138.understandme.Presentation.Navigation.AnswerMode
import jp.ac.jec.cm0138.understandme.Presentation.Navigation.HOMEWORK_DETAIL_ROUTE
import jp.ac.jec.cm0138.understandme.Presentation.Screens.Components.HomeworkItemView
import jp.ac.jec.cm0138.understandme.Presentation.ViewModels.HomeworkListScreenViewModel
import jp.ac.jec.cm0138.understandme.customTheme.CustomTypography
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme

@Composable
fun HomeworkListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeworkListScreenViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.loadHomeworks()
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopSearchBarAndFilters(
                searchText = viewModel.searchText,
                selectedFilterOption = viewModel.selectedFilterOption,
                onFilterOptionSelected = { viewModel.handleFilterChange(it) },
                onSearchTextChange = { viewModel.handleSearchTextChange(it) },
                modifier = Modifier.padding(bottom = 5.dp)
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
        ) {
            items(items = viewModel.filteredHomeworks, key = { it.id }) { homework ->
                HomeworkItemView(
                    title = homework.title,
                    dueDate = homework.dueDateString,
                    homeworkState = homework.submissionState,
                    onTap = {
                        navController.navigate(HOMEWORK_DETAIL_ROUTE(homeworkID = homework.id))
                    },
                    onAnswerClicked = {
                        navController.navigate(ANSWER_QUESTIONS_ROUTE(homeworkID = homework.id, mode = AnswerMode.ANSWER))
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}


@Composable
fun TopSearchBarAndFilters(
    modifier: Modifier = Modifier,
    searchText: String,
    selectedFilterOption: HomeworkFilterOption,
    onFilterOptionSelected: (HomeworkFilterOption) -> Unit,
    onSearchTextChange: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "課題一覧",
            modifier = Modifier,
            style = CustomTypography.header
        )

        CustomSearchBar(
            value = searchText,
            onValueChange = { onSearchTextChange(it) },
            modifier = Modifier
                .padding(vertical = 8.dp)
        )


        LazyRow(

        ) {
            items(items = HomeworkFilterOption.allCases) { filterOption ->
                FilterButton(
                    title = filterOption.displayName,
                    isSelected = filterOption == selectedFilterOption,
                    onClick = { onFilterOptionSelected(filterOption) },
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
            }
        }
    }

}


@Composable
fun FilterButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = CustomTypography.label,
        color = if (isSelected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.onSurface,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected)
                    MyAppTheme.colors.accent.copy(alpha = 0.25f)
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.001f)
            )
            .clickable(onClick = onClick)
            .padding(
                vertical = 6.dp,
                horizontal = 14.dp
            )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        placeholder = {
            Text(
                text = "課題を検索",
                style = CustomTypography.label
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),

        // 🔑 SwiftUI-like colors
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,

            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun HomeworkListScreenPreview() {
    val navController = rememberNavController()

    Scaffold { innerpadding ->
        HomeworkListScreen(
            modifier = Modifier.padding(innerpadding),
            navController = navController
        )


    }
}
