package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import ui.ThemeApp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VerticalBarOption(
    label: String,
    icon: Painter,
    isSelected: Boolean = false,
    onClick: () -> Unit
){
    var isHover by remember { mutableStateOf(false) }

    TooltipArea(label){
        Box(
            modifier = Modifier
                .height(30.dp)
                .width(35.dp)
                .background(
                    if(isSelected) ThemeApp.colors.buttonColor
                    else if(isHover) ThemeApp.colors.background
                    else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .onPointerEvent(PointerEventType.Enter){ isHover = true }
                .onPointerEvent(PointerEventType.Exit){ isHover = false }
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ){
            Icon(
                icon,
                contentDescription = label,
                tint = ThemeApp.colors.textColor,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}