package ui.components.lazy.filetree

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.fileTree.FileTree

@Composable
fun FileTreeItemView(model: FileTree.Item) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .clickable { model.open() }
            .padding(start = 24.dp * model.level)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FileItemIcon(model)

        Spacer(modifier = Modifier.width(5.dp))

        FileItemText(model, Modifier.align(Alignment.CenterVertically))
    }
}