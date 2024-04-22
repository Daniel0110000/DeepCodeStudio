package com.dr10.editor.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr10.common.ui.ThemeApp

@Composable
fun LinesNumber(
    countLines: Int,
    scrollState: ScrollState,
    modifier: Modifier
) = Column(
    modifier = modifier
        .width(30.dp)
        .verticalScroll(scrollState),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    for (i in 1..countLines){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(17.dp),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = i.toString(),
                color = ThemeApp.colors.lineNumberTextColor,
                fontSize = 13.sp,
                fontFamily = ThemeApp.text.fontFamily
            )
        }
    }
}