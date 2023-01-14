package com.amsavarthan.tally.presentation.ui.screens.dashboard.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.twotone.AccountBalance
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TallyCurrentWeekDataCard(
    modifier: Modifier = Modifier,
    spentThisWeek: Double,
) {
    Card(modifier = modifier, elevation = 2.dp) {
        Column(
            modifier = Modifier.padding(
                top = 8.dp,
                start = 24.dp,
                bottom = 16.dp,
                end = 8.dp,
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.TwoTone.AccountBalance,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colors.onSurface
                )
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Info, contentDescription = "week-spends-help-button")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Spent this week",
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
            Text(
                "₹%.2f".format(spentThisWeek),
                style = MaterialTheme.typography.h3
            )
        }
    }
}