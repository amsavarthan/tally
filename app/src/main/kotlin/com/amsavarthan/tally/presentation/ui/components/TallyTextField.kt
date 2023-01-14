package com.amsavarthan.tally.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amsavarthan.tally.domain.utils.CurrencyFilter
import com.amsavarthan.tally.presentation.ui.theme.Gray
import java.text.DecimalFormat

@Composable
fun TallyCurrencyTextField(
    modifier: Modifier = Modifier,
    label: String = "",
    value: Double,
    onValueChange: (String) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (label.isNotBlank()) {
            Text(
                text = label,
                style = MaterialTheme.typography.subtitle2
            )
        }
        BasicTextField(
            value = if (value == 0.0) "" else DecimalFormat("0.00").format(value),
            onValueChange = { rawText ->
                onValueChange.invoke(CurrencyFilter(rawText))
            },
            textStyle = TextStyle.Default.copy(fontSize = 30.sp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorationBox = { innerTextField ->
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "₹", color = MaterialTheme.colors.onBackground, fontSize = 30.sp)
                    Box {
                        if (value == 0.0) {
                            Text(text = "0.00", color = Gray, fontSize = 30.sp)
                        }
                        innerTextField()
                    }
                }
            },
        )
    }
}

@Composable
fun TallyTextField(
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (label.isNotBlank()) {
            Text(
                text = label,
                style = MaterialTheme.typography.subtitle2,
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle.Default.copy(fontSize = 30.sp),
            singleLine = true,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            decorationBox = { innerTextField ->
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box {
                        if (value.isBlank()) {
                            Text(text = placeholder, color = Gray, fontSize = 30.sp)
                        }
                        innerTextField()
                    }
                }
            },
        )
    }
}