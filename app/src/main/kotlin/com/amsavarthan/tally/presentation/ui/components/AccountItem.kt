package com.amsavarthan.tally.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amsavarthan.tally.presentation.ui.theme.LightGray


@Composable
fun AccountItem(
    modifier: Modifier = Modifier,
    name: String,
    onClick: () -> Unit,
    filled: Boolean = false,
    trailingIcon: ImageVector? = null,
    shape: Shape = RoundedCornerShape(16.dp),
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (filled) LightGray else Color.Transparent, shape)
            .border(if (filled) 0.dp else 2.dp, LightGray, shape)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 18.dp),
            text = name,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Medium,
        )
        if (trailingIcon != null) {
            Icon(
                modifier = Modifier.padding(horizontal = 24.dp),
                imageVector = trailingIcon,
                contentDescription = null
            )
        }
    }

}