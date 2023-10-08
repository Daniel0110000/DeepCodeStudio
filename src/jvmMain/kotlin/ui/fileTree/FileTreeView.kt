package ui.fileTree

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.ThemeApp
import ui.fileTree.lazy.FileTreeItemView

@Composable
fun FileTreeView(state: FileTree) {

    val verticalState = rememberLazyListState()
    val horizontalState = rememberScrollState()

    Box(modifier = Modifier.padding(start = 10.dp, end = 3.dp).fillMaxWidth()){
        LazyColumn(
            state = verticalState,
            modifier = Modifier.horizontalScroll(horizontalState)
        ) {
            items(state.listFiles.value.sortedBy { it.file.absolutePath }){
                Spacer(modifier = Modifier.height(5.dp))
                FileTreeItemView(it){ state.toggleDirectoryExpansion(it) }
            }
        }

        VerticalScrollbar(
            ScrollbarAdapter(verticalState),
            modifier = Modifier.align(Alignment.CenterEnd),
            style = ThemeApp.scrollbar.scrollbarStyle
        )

        HorizontalScrollbar(
            ScrollbarAdapter(horizontalState),
            modifier = Modifier.align(Alignment.BottomCenter),
            style = ThemeApp.scrollbar.scrollbarStyle
        )

    }
}