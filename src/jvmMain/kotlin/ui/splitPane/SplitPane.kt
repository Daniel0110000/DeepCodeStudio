package ui.splitPane

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.ThemeApp
import ui.fileTree.FileTreeView
import util.FileChooser
import java.awt.Cursor

@Composable
fun SplitPane(
    splitState: SplitPaneState,
    newDirectoryPath: (String?) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .width(if(splitState.widthSplittable > 50)(splitState.widthSplittable).dp else 80.dp)
            .fillMaxHeight()
            .background(ThemeApp.colors.background)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .background(ThemeApp.colors.secondColor),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(ThemeApp.colors.buttonColor, shape = RoundedCornerShape(8.dp))
                        .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))
                        .clickable { coroutineScope.launch { newDirectoryPath(FileChooser.chooseDirectory()) } },
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painterResource("images/ic_folder.svg"),
                        contentDescription = "Folder icon",
                        tint = ThemeApp.colors.textColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            FileTreeView(splitState.fileTreeState)

        }

        Box(
            modifier = Modifier
                .background(ThemeApp.colors.secondColor)
                .fillMaxHeight()
                .width(2.dp)
                .align(Alignment.CenterEnd)
                .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)))
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { splitState.widthSplittable += it }
                )
        )
    }
}