package ui.fileTree

import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.ThemeApp
import ui.fileTree.lazy.FileTreeItemView

@Composable
fun FileTreeView(state: FileTree) {

    val scrollState = rememberLazyListState()

    var selectedItem by remember { mutableStateOf<FileInfo?>(null) }

    Box(modifier = Modifier.padding(start = 10.dp, end = 5.dp).fillMaxWidth()){
        LazyColumn(state = scrollState) {
            items(state.listFiles.value.sortedBy { it.file.absolutePath }){
                Spacer(modifier = Modifier.height(5.dp))
                FileTreeItemView(
                    it,
                    selectedItem,
                    onClickListener = { state.toggleDirectoryExpansion(it) },
                    onSelectedItemClickListener = { selectedItem = it }
                )
            }
        }

        VerticalScrollbar(
            ScrollbarAdapter(scrollState),
            modifier = Modifier.align(Alignment.CenterEnd),
            style = ThemeApp.scrollbar.scrollbarStyle
        )
    }
}