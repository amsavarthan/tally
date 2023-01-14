package com.amsavarthan.tally.presentation.ui.screens.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amsavarthan.tally.domain.entity.TallyOnBoardingStep

@Composable
fun TallyScaffold(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    step: TallyOnBoardingStep,
    scrollState: ScrollState = rememberScrollState(),
    onActionClicked: () -> Unit,
    topBar: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {

    TallyScaffold(
        scaffoldState = scaffoldState,
        modifier = modifier,
        icon = step.icon,
        title = step.title,
        description = step.description,
        actionText = step.actionText,
        illustrationResource = step.illustrationResource,
        onActionClicked = onActionClicked,
        scrollState = scrollState,
        topBar = topBar,
        content = content
    )

}

@Composable
fun TallyScaffold(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    icon: ImageVector,
    title: String,
    scrollState: ScrollState = rememberScrollState(),
    description: String? = null,
    illustrationResource: Int? = null,
    actionText: String? = null,
    onActionClicked: () -> Unit = {},
    topBar: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(scaffoldState = scaffoldState,
        modifier = modifier
            .statusBarsPadding()
            .imePadding(),
        topBar = topBar ?: {},
        bottomBar = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (illustrationResource != null) {
                    Image(
                        modifier = Modifier.fillMaxWidth(),
                        painter = painterResource(id = illustrationResource),
                        contentDescription = null,
                    )
                }
                if (actionText != null) {
                    TallyButton(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 24.dp),
                        text = actionText,
                        onClick = onActionClicked,
                    )
                }
            }
        })
    { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .then(if (topBar == null) Modifier.statusBarsPadding() else Modifier)
                .padding(horizontal = 24.dp)
                .padding(bottom = 60.dp)
                .padding(
                    top = if (topBar == null) 24.dp else 8.dp,
                    bottom = 64.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Icon(
                modifier = Modifier.size(42.dp),
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colors.onBackground,
            )
            Text(
                text = title,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Medium,
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Normal,
                )
            }
            content()
        }
    }

}