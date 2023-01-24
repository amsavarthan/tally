package com.amsavarthan.tally.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amsavarthan.tally.presentation.ui.theme.Gray
import com.amsavarthan.tally.presentation.ui.theme.fonts

@Composable
fun TallyCurrencyTextField(
    modifier: Modifier = Modifier,
    label: String = "",
    value: String,
    onValueChange: (String) -> Unit,
    onImeDone: () -> Unit = {},
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
            value = TextFieldValue(text = value, selection = TextRange(value.length)),
            onValueChange = { fieldValue ->
                onValueChange.invoke(fieldValue.text)
            },
            textStyle = TextStyle.Default.copy(fontSize = 30.sp, fontFamily = fonts),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(onDone = { onImeDone() }),
            decorationBox = { innerTextField ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹",
                        color = MaterialTheme.colors.onBackground,
                        fontFamily = fonts,
                        fontSize = 30.sp
                    )
                    Box {
                        if (value.isBlank()) {
                            Text(
                                text = "0.00",
                                color = Gray,
                                fontFamily = fonts,
                                fontSize = 30.sp
                            )
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
            textStyle = TextStyle.Default.copy(fontSize = 30.sp, fontFamily = fonts),
            singleLine = true,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            decorationBox = { innerTextField ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        if (value.isBlank()) {
                            Text(
                                text = placeholder,
                                color = Gray,
                                fontSize = 30.sp,
                                fontFamily = fonts,
                            )
                        }
                        innerTextField()
                    }
                }
            },
        )
    }
}