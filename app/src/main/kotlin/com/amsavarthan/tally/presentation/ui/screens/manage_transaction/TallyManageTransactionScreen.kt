package com.amsavarthan.tally.presentation.ui.screens.manage_transaction

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.twotone.AccountBalanceWallet
import androidx.compose.material.icons.twotone.ArrowRightAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsavarthan.tally.domain.entity.CategoryType
import com.amsavarthan.tally.domain.entity.ChooserType
import com.amsavarthan.tally.presentation.ui.components.TallyAppBar
import com.amsavarthan.tally.presentation.ui.screens.chooser.TallyChooserResult
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyChooserScreenDestination
import com.amsavarthan.tally.presentation.ui.screens.manage_transaction.components.TallyDatePicker
import com.amsavarthan.tally.presentation.ui.screens.manage_transaction.components.TallyKeyPad
import com.amsavarthan.tally.presentation.ui.theme.Gray
import com.amsavarthan.tally.presentation.ui.theme.LightGray
import com.amsavarthan.tally.presentation.utils.ContentDescription
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest

data class TallyManageTransactionScreenNavArgs(
    val transactionId: Long? = null,
)

@OptIn(FlowPreview::class)
@Destination(
    navArgsDelegate = TallyManageTransactionScreenNavArgs::class
)
@Composable
fun TallyManageTransactionScreen(
    navigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<TallyChooserScreenDestination, TallyChooserResult>,
    viewModel: ManageTransactionScreenViewModel = hiltViewModel(),
) {

    val (transactionDetails, shouldShowDeleteConfirmationAlertDialog) = viewModel.uiState
    val context = LocalContext.current

    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> Unit
            is NavResult.Value -> {
                viewModel.updateChosenAccountAndCategory(result.value)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ManageTransactionScreenViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                ManageTransactionScreenViewModel.UiEvent.NavigateBack -> {
                    navigator.navigateUp()
                }
            }
        }
    }

    if (shouldShowDeleteConfirmationAlertDialog) {
        AlertDialog(
            title = { Text(text = "Remove transaction") },
            text = { Text(text = "Are you sure do you want to remove this transaction?") },
            onDismissRequest = {
                viewModel.onEvent(TallyManageTransactionScreenEvent.ToggleDeleteConfirmationAlertDialog())
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(TallyManageTransactionScreenEvent.OnDeleteConfirmation)
                }) {
                    Text(text = "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.onEvent(TallyManageTransactionScreenEvent.ToggleDeleteConfirmationAlertDialog())
                }) {
                    Text(text = "Keep")
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TallyAppBar(
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = navigator::navigateUp) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = ContentDescription.buttonBack
                        )
                    }
                },
                actions = {
                    if (transactionDetails.transactionId != null) {
                        IconButton(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            onClick = {
                                viewModel.onEvent(
                                    TallyManageTransactionScreenEvent.ToggleDeleteConfirmationAlertDialog(
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
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    val focusManager = LocalFocusManager.current

                    Text(
                        text = "Amount",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = focusManager::clearFocus
                        ),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "₹",
                            color = MaterialTheme.colors.onBackground,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = transactionDetails.amount.ifBlank { "0.00" },
                            color = if (transactionDetails.amount.isBlank()) Gray else MaterialTheme.colors.onBackground,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    TallyDatePicker(
                        date = transactionDetails.localDateTime.date,
                        onDatePicked = { dateTime ->
                            viewModel.onEvent(
                                TallyManageTransactionScreenEvent.OnDateChanged(
                                    dateTime
                                )
                            )
                        }
                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                        .background(LightGray)
                        .padding(vertical = 16.dp)
                        .padding(start = 8.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ChooserItem(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        icon = Icons.TwoTone.AccountBalanceWallet,
                        value = transactionDetails.account?.name ?: "Source",
                        onClick = {
                            navigator.navigate(
                                TallyChooserScreenDestination(
                                    type = ChooserType.Account,
                                    selectedId = transactionDetails.account?.id
                                )
                            ) {
                                launchSingleTop = true
                            }
                        }
                    )

                    val degree by animateFloatAsState(
                        targetValue = if (transactionDetails.category?.type == CategoryType.Income) 180f else 0f,
                        animationSpec = tween(delayMillis = 200)
                    )

                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .rotate(degree),
                        imageVector = Icons.TwoTone.ArrowRightAlt,
                        contentDescription = null
                    )
                    ChooserItem(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        emoji = transactionDetails.category?.emoji ?: "💸",
                        value = transactionDetails.category?.name ?: "Reason",
                        onClick = {
                            navigator.navigate(
                                TallyChooserScreenDestination(
                                    type = ChooserType.Category,
                                    selectedId = transactionDetails.category?.id
                                )
                            ) {
                                launchSingleTop = true
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        modifier = Modifier.heightIn(min = 48.dp),
                        onClick = {
                            viewModel.onEvent(TallyManageTransactionScreenEvent.OnSaveButtonClicked)
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            text = "Save"
                        )
                    }
                }
            }
            TallyKeyPad(
                modifier = Modifier.weight(0.4f),
                onKeyPressed = { pressedKey ->
                    viewModel.onEvent(
                        TallyManageTransactionScreenEvent.OnKeyPadKeyPressed(
                            key = pressedKey
                        )
                    )
                },
                onKeyLongPressed = { pressedKey ->
                    viewModel.onEvent(
                        TallyManageTransactionScreenEvent.OnKeyPadKeyLongPressed(
                            key = pressedKey
                        )
                    )
                }
            )
        }
    }

}

@Composable
fun ChooserItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    emoji: String? = null,
    value: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = buildAnnotatedString {
                if (emoji != null) {
                    withStyle(SpanStyle(fontSize = 22.sp)) {
                        append(emoji)
                    }
                    append(" ")
                }
                append(value)
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Medium
        )
    }
}

