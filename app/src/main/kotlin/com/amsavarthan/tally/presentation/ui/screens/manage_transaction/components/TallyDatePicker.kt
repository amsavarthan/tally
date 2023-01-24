package com.amsavarthan.tally.presentation.ui.screens.manage_transaction.components

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amsavarthan.tally.R
import com.amsavarthan.tally.domain.utils.DateFormatter
import com.amsavarthan.tally.presentation.ui.theme.LightGray
import kotlinx.datetime.*

@Composable
fun TallyDatePicker(
    modifier: Modifier = Modifier,
    date: LocalDate,
    onDatePicked: (LocalDateTime) -> Unit,
) {

    val context = LocalContext.current
    val now = remember { Clock.System.now() }
    val today = remember { now.toLocalDateTime(TimeZone.currentSystemDefault()) }

    val dialog = remember {
        DatePickerDialog(
            /* context = */ context,
            /* themeResId = */R.style.Theme_Tally_Dialog,
            /* listener = */ { _, year, month, dayOfMonth ->
                onDatePicked(LocalDateTime(LocalDate(year, month + 1, dayOfMonth), today.time))
            },
            /* year = */ today.year,
            /* monthOfYear = */ today.monthNumber - 1,
            /* dayOfMonth = */ today.dayOfMonth
        ).apply {
            datePicker.maxDate = now.toEpochMilliseconds()
        }
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(LightGray)
            .clickable {
                dialog.show()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        Icon(imageVector = Icons.TwoTone.DateRange, contentDescription = null)
        Text(
            text = DateFormatter(date),
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Medium
        )
    }

}