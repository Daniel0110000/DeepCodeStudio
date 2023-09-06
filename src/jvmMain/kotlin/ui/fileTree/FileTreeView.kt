package ui.fileTree

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.ThemeApp
import ui.components.lazy.filetree.FileTreeItemView

@Composable
fun FileTreeView(model: FileTree) {

    val verticalState = rememberLazyListState()
    val horizontalState = rememberScrollState()

    Box(modifier = Modifier.padding(start = 10.dp, end = 3.dp).fillMaxWidth()){
        LazyColumn(
            state = verticalState,
            modifier = Modifier.horizontalScroll(horizontalState)
        ) {
            items(model.items){
                Spacer(modifier = Modifier.height(5.dp))
                FileTreeItemView(it)
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