package ui.editor

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.ThemeApp

@Composable
fun LinesNumberView(countLines: Int) = Column(
    modifier = Modifier
        .fillMaxHeight()
        .width(30.dp),
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