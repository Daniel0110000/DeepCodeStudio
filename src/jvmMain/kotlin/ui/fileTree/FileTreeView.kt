package ui.fileTree

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.components.lazy.filetree.FileTreeItemView

@Composable
fun FileTreeView(model: FileTree) {
    Box(modifier = Modifier.padding(start = 10.dp, end = 10.dp)){
        LazyColumn {
            items(model.items){
                Spacer(modifier = Modifier.height(5.dp))
                FileTreeItemView(it)
            }
        }
    }
}