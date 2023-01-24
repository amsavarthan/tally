package com.amsavarthan.tally.presentation.ui.screens.manage_category

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsavarthan.tally.domain.entity.CategoryType
import com.amsavarthan.tally.presentation.ui.components.TallyAppBar
import com.amsavarthan.tally.presentation.ui.theme.Gray
import com.amsavarthan.tally.presentation.ui.theme.LightGray
import com.amsavarthan.tally.presentation.utils.ContentDescription
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

data class TallyManageCategoryScreenNavArgs(
    val categoryId: Long? = null,
    val categoryType: CategoryType,
)

@Destination(
    navArgsDelegate = TallyManageCategoryScreenNavArgs::class
)
@Composable
fun TallyManageCategoryScreen(
    navigator: DestinationsNavigator,
    viewModel: ManageCategoryViewModel = hiltViewModel(),
) {

    val uiState = viewModel.uiState

    val scrollState = rememberScrollState()
    val elevation by animateDpAsState(targetValue = if (scrollState.value > 5) AppBarDefaults.TopAppBarElevation else 0.dp)

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                ManageCategoryViewModel.UiEvent.NavigateBack -> navigator.navigateUp()
                is ManageCategoryViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    if (uiState.shouldShowDeleteConfirmationDialog) {
        AlertDialog(
            title = { Text(text = "Remove category") },
            text = { Text(text = "Are you sure do you want to remove this category?") },
            onDismissRequest = {
                viewModel.onEvent(TallyManageCategoryScreenEvent.ToggleDeleteConfirmationAlertDialog())
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(TallyManageCategoryScreenEvent.OnDeleteConfirmation)
                }) {
                    Text(text = "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.onEvent(TallyManageCategoryScreenEvent.ToggleDeleteConfirmationAlertDialog())
                }) {
                    Text(text = "Keep")
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .imePadding(),
        scaffoldState = scaffoldState,
        topBar = {
            TallyAppBar(
                elevation = elevation,
                navigationIcon = {
                    IconButton(onClick = navigator::navigateUp) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = ContentDescription.buttonBack
                        )
                    }
                },
                actions = {
                    if (uiState.category.id != null) {
                        IconButton(
                            modifier = Modifier.padding(start = 8.dp),
                            onClick = {
                                viewModel.onEvent(
                                    TallyManageCategoryScreenEvent.ToggleDeleteConfirmationAlertDialog(
                                        showDialog = true
                                    )
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = ContentDescription.buttonDelete,
                                tint = MaterialTheme.colors.onBackground
                            )
                        }
                    }
                    IconButton(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = {
                            viewModel.onEvent(TallyManageCategoryScreenEvent.OnDoneButtonClicked)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Done,
                            contentDescription = ContentDescription.buttonDone,
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp, top = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val focusRequester = remember {
                FocusRequester()
            }

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(LightGray),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    modifier = Modifier.width(IntrinsicSize.Min),
                    value = TextFieldValue(
                        uiState.category.emoji,
                        selection = TextRange(uiState.category.emoji.length)
                    ),
                    onValueChange = { value ->
                        viewModel.onEvent(
                            TallyManageCategoryScreenEvent.OnCategoryEmojiChange(
                                emoji = value.text
                            )
                        )
                    },
                    textStyle = TextStyle.Default.copy(fontSize = 48.sp),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        capitalization = KeyboardCapitalization.Characters
                    ),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.Center) {
                            if (uiState.category.emoji.isBlank()) {
                                Text(
                                    modifier = Modifier.alpha(0.5f),
                                    text = "🛍️",
                                    fontSize = 48.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Category Name", style = MaterialTheme.typography.subtitle2)

            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .focusRequester(focusRequester),
                value = TextFieldValue(
                    uiState.category.name,
                    selection = TextRange(uiState.category.name.length)
                ),
                textStyle = MaterialTheme.typography.h6.copy(fontSize = 26.sp),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                onValueChange = { value ->
                    viewModel.onEvent(TallyManageCategoryScreenEvent.OnCategoryNameChange(name = value.text))
                },
                decorationBox = { innerTextField ->
                    if (uiState.category.name.isBlank()) {
                        Text(
                            text = "Shopping",
                            style = MaterialTheme.typography.h6.copy(fontSize = 26.sp),
                            color = Gray
                        )
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.height(40.dp))
            TallyCategoryTypeButtonGroup(
                selected = uiState.category.type,
                onButtonClicked = { type ->
                    viewModel.onEvent(
                        TallyManageCategoryScreenEvent.OnCategoryTypeChange(type)
                    )
                },
            )
        }
    }

}

@Composable
private fun TallyCategoryTypeButtonGroup(
    modifier: Modifier = Modifier,
    selected: CategoryType,
    onButtonClicked: (CategoryType) -> Unit,
) {
    Row(modifier = modifier.padding(horizontal = 36.dp)) {
        TallyCategoryTypeButtonGroupItem(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
            isSelected = selected == CategoryType.Income,
            text = CategoryType.Income.title,
            onClicked = { onButtonClicked(CategoryType.Income) }
        )
        TallyCategoryTypeButtonGroupItem(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
            isSelected = selected == CategoryType.Expense,
            text = CategoryType.Expense.title,
            onClicked = { onButtonClicked(CategoryType.Expense) }
        )
    }
}

@Composable
private fun TallyCategoryTypeButtonGroupItem(
    modifier: Modifier = Modifier,
    shape: Shape,
    isSelected: Boolean,
    text: String,
    onClicked: () -> Unit,
) {
    val background by animateColorAsState(targetValue = if (isSelected) Color.Black else LightGray)
    val textColor by animateColorAsState(targetValue = if (isSelected) Color.White else Color.Black)

    Box(
        modifier = modifier
            .clip(shape)
            .background(background)
            .clickable(onClick = onClicked)
            .padding(vertical = 16.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            color = textColor,
            text = text
        )
    }
}
