package ui.splitPane

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ui.ThemeApp
import ui.fileTree.FileTreeView
import ui.viewModels.splitPane.FileTreeViewModel
import ui.viewModels.splitPane.SplitPaneViewModel
import java.awt.Cursor

@Composable
fun SplitPane(
    fileTreeViewModel: FileTreeViewModel,
    splitPaneViewModel: SplitPaneViewModel,
    collapseSplitPane: () -> Unit
) {

    val widthSplittable = splitPaneViewModel.widthSplittable.observeAsState().value

    LaunchedEffect(widthSplittable){
        if(widthSplittable < 50){
            collapseSplitPane()
            splitPaneViewModel.setWidthSplittable(80f)
        }
    }

    Box(
        modifier = Modifier
            .width(widthSplittable.dp)
            .fillMaxHeight()
            .background(ThemeApp.colors.background)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            Box(modifier = Modifier.fillMaxWidth().height(45.dp), contentAlignment = Alignment.Center){
                Text(
                    "Folders",
                    fontFamily = ThemeApp.text.fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = ThemeApp.colors.textColor,
                    modifier = Modifier.padding(10.dp)
                )
            }

            FileTreeView(fileTreeViewModel)
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
                    state = rememberDraggableState { splitPaneViewModel.setWidthSplittable(it) }
                )
        )
    }
}