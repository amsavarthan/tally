package com.amsavarthan.tally.presentation.ui.screens.manage_transaction.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardBackspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.amsavarthan.tally.domain.entity.TallyKeyPadItem
import com.amsavarthan.tally.presentation.ui.theme.KeypadGray
import com.amsavarthan.tally.presentation.utils.ContentDescription

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TallyKeyPad(
    modifier: Modifier = Modifier,
    onKeyPressed: (TallyKeyPadItem) -> Unit,
    onKeyLongPressed: (TallyKeyPadItem) -> Unit,
) {
    BoxWithConstraints(modifier = modifier) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize(),
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            items(TallyKeyPadItem.getAllKeys()) { key ->
                Box(
                    modifier = Modifier
                        .height(maxHeight / 4)
                        .background(KeypadGray)
                        .clip(CircleShape)
                        .combinedClickable(
                            onClick = { onKeyPressed(key) },
                            onLongClick = { onKeyLongPressed(key) },
                        )
                ) {
                    if (key == TallyKeyPadItem.KeyBackspace) {
                        Icon(
                            modifier = Modifier.align(Alignment.Center),
                            imageVector = Icons.Outlined.KeyboardBackspace,
                            contentDescription = ContentDescription.buttonKeyBackspace,
                            tint = Color.Black
                        )
                        return@Box
                    }
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = key.label,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}