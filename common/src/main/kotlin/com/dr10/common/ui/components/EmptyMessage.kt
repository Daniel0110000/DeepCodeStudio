package com.dr10.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.dr10.common.ui.ThemeApp

@Composable
fun EmptyMessage() = Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    Text(
        "No options available",
        fontFamily = ThemeApp.text.fontFamily,
        fontSize = 13.sp,
        color = ThemeApp.colors.textColor
    )

}