package ui.editor.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.ThemeApp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TabItem(
    model: TabModel,
    tabSelected: String,
    onClickListenerTabClose: () -> Unit,
    onClickListenerTabSelected: (String) -> Unit
) {

    // Track whether the close icon is hovered
    var hoverCloseIcon by remember { mutableStateOf(false) }
    // Track whether the tab itself os hovered
    var hoverTab by remember { mutableStateOf(false) }

    // Determine oif the current tab is selected
    val isSelected = tabSelected == model.filePath

    Row(
        modifier = Modifier
            .background(if(hoverTab) ThemeApp.colors.hoverTab else ThemeApp.colors.secondColor)
            .fillMaxHeight()
            .drawBehind {
                clipRect {
                    drawLine(
                        brush = SolidColor(if(isSelected) ThemeApp.colors.buttonColor else Color.Transparent),
                        cap = StrokeCap.Square,
                        strokeWidth = 4f,
                        start = Offset(x = 0f, y = size.height),
                        end = Offset(x = size.width, y = size.height)
                    )
                }
            }
            .onPointerEvent(PointerEventType.Enter){ hoverTab = true }
            .onPointerEvent(PointerEventType.Exit){ hoverTab = false }
            .clickable { onClickListenerTabSelected(model.filePath) },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            painterResource("images/ic_asm.svg"),
            contentDescription = "Asm icon",
            modifier = Modifier.size(20.dp),
            tint = ThemeApp.colors.asmIconColor
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            model.fileName,
            color = ThemeApp.colors.textColor,
            fontFamily = ThemeApp.text.fontFamily,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.width(5.dp))

        Box(
            modifier = Modifier
                .size(18.dp)
                .background(if(hoverCloseIcon) Color(0x40FFFFFF) else Color.Transparent, shape = CircleShape)
                .onPointerEvent(PointerEventType.Enter){ hoverCloseIcon = true }
                .onPointerEvent(PointerEventType.Exit){ hoverCloseIcon = false }
                .clickable { onClickListenerTabClose() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = "Close icon",
                tint = ThemeApp.colors.textColor,
                modifier = Modifier.size(13.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

    }
}