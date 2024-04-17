package ui.splitPane.fileTree

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr10.common.ui.ThemeApp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DropdownMenuItem(
    label: String,
    icon: Painter,
    onClick: () -> Unit
){

    val isHoverOption = remember { mutableStateOf(Actions.NONE) }

    androidx.compose.material.DropdownMenuItem(
        onClick = { onClick() },
        modifier = Modifier
            .height(30.dp)
            .background(if (isHoverOption.value == Actions.NEW_FILE) ThemeApp.colors.hoverTab else Color.Transparent)
            .onPointerEvent(PointerEventType.Enter) { isHoverOption.value = Actions.NEW_FILE }
            .onPointerEvent(PointerEventType.Exit) { isHoverOption.value = Actions.NONE }
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = ThemeApp.colors.buttonColor,
            modifier = Modifier.size(17.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            label,
            color = ThemeApp.colors.textColor,
            fontFamily = ThemeApp.text.fontFamily,
            fontSize = 12.sp
        )

    }
}