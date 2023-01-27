package com.amsavarthan.tally.presentation.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amsavarthan.tally.domain.entity.AccountType
import com.amsavarthan.tally.domain.entity.CategoryType
import com.amsavarthan.tally.domain.entity.TransactionDetail
import com.amsavarthan.tally.presentation.ui.theme.DarkGray
import com.amsavarthan.tally.presentation.ui.theme.LightGray
import com.amsavarthan.tally.presentation.ui.theme.fonts

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionItem(
    transaction: TransactionDetail,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    requireNotNull(transaction.category)
    requireNotNull(transaction.account)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onLongClick = onLongClick, onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(text = transaction.category.emoji, fontSize = 16.sp)
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = transaction.category.name,
                fontFamily = fonts,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            val creditOrDebit = remember {
                when (transaction.account.type) {
                    AccountType.Cash -> {
                        when (transaction.category.type) {
                            CategoryType.Expense -> "Paid as"
                            CategoryType.Income -> "Received as"
                        }
                    }
                    else -> {
                        when (transaction.category.type) {
                            CategoryType.Expense -> "Debited from"
                            CategoryType.Income -> "Credited to"
                        }
                    }
                }

            }
            Text(
                text = "$creditOrDebit ${transaction.account.name}",
                fontFamily = fonts,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = DarkGray
            )
        }
        Text(
            text = "₹${transaction.amount}",
            color = if (transaction.category.type == CategoryType.Income) MaterialTheme.colors.primary else Color.Black,
            fontFamily = fonts,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}