package ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.ThemeApp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TooltipArea(
    label: String,
    content: @Composable () -> Unit
) = androidx.compose.foundation.TooltipArea(
    tooltip = {
        Surface(
            color = ThemeApp.colors.secondColor,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.shadow(4.dp)
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontFamily = ThemeApp.text.fontFamily,
                color = ThemeApp.colors.textColor,
                modifier = Modifier.padding(10.dp)
            )
        }
    },
    tooltipPlacement = TooltipPlacement.CursorPoint(offset = DpOffset(20.dp, 0.dp)),
    content = { content() }
)