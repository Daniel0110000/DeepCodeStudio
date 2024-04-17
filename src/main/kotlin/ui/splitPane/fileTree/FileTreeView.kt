package ui.splitPane.fileTree

import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dr10.common.ui.ThemeApp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ui.splitPane.fileTree.lazy.FileTreeItemView
import ui.viewModels.splitPane.FileTreeViewModel

@Composable
fun FileTreeView(viewModel: FileTreeViewModel) {

    val scrollState = rememberLazyListState()

    var selectedItem by remember { mutableStateOf<FileInfo?>(null) }

    val listFiles = viewModel.listFiles.observeAsState().value

    Box(modifier = Modifier.padding(start = 10.dp, end = 5.dp).fillMaxWidth()){
        LazyColumn(state = scrollState) {
            items(listFiles.sortedBy { it.file.absolutePath }){
                Spacer(modifier = Modifier.height(5.dp))
                FileTreeItemView(
                    it,
                    selectedItem,
                    onClickListener = { viewModel.toggleDirectoryExpansion(it) },
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