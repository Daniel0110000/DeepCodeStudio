package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.ThemeApp
import ui.settings.Settings
import domain.util.DirectoryChooser
import java.awt.Cursor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun verticalBarOptions(
    isCollapseFileTree: Boolean,
    newDirectoryPath: (String?) -> Unit,
    collapseOrExtendSplitPane: () -> Unit
) {
    var hoverCollapseButton by remember { mutableStateOf(false) }
    var hoverSelectFolderButton by remember { mutableStateOf(false) }
    var hoverOpenSettings by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    var showSettings by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(45.dp)
            .background(ThemeApp.colors.secondColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .height(30.dp)
                .width(35.dp)
                .background(if(hoverCollapseButton) ThemeApp.colors.background else Color.Transparent, shape = RoundedCornerShape(8.dp))
                .clickable { collapseOrExtendSplitPane() }
                .onPointerEvent(PointerEventType.Enter){ hoverCollapseButton = true }
                .onPointerEvent(PointerEventType.Exit){ hoverCollapseButton = false },
            contentAlignment = Alignment.Center
        ){
            Icon(
                if(isCollapseFileTree) painterResource("images/ic_extend.svg") else painterResource("images/ic_collapse.svg"),
                contentDescription = "Collapse and extend icon",
                tint = ThemeApp.colors.textColor,
                modifier = Modifier.height(23.dp).width(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Box(
            modifier = Modifier
                .height(30.dp)
                .width(35.dp)
                .background(if(hoverSelectFolderButton) ThemeApp.colors.background else Color.Transparent, shape = RoundedCornerShape(8.dp))
                .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))
                .onPointerEvent(PointerEventType.Enter){ hoverSelectFolderButton = true }
                .onPointerEvent(PointerEventType.Exit){ hoverSelectFolderButton = false }
                .clickable { coroutineScope.launch { newDirectoryPath(DirectoryChooser.chooseDirectory()) }},
            contentAlignment = Alignment.Center
        ){
            Icon(
                painterResource("images/ic_folder.svg"),
                contentDescription = "Collapse icon",
                tint = ThemeApp.colors.textColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .height(30.dp)
                .width(35.dp)
                .background(if(hoverOpenSettings) ThemeApp.colors.background else Color.Transparent, shape = RoundedCornerShape(8.dp))
                .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))
                .onPointerEvent(PointerEventType.Enter){ hoverOpenSettings = true }
                .onPointerEvent(PointerEventType.Exit){ hoverOpenSettings = false }
                .clickable { showSettings = true },
            contentAlignment = Alignment.Center
        ){
            Icon(
                painterResource("images/ic_settings.svg"),
                contentDescription = "Settings icon",
                tint = ThemeApp.colors.textColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

    }

    if(showSettings) Settings{ showSettings = false }
}