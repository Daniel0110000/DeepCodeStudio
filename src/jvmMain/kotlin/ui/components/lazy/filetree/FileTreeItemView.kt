package ui.components.lazy.filetree

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.fileTree.FileInfo

@Composable
fun FileTreeItemView(model: FileInfo, onClickListener: () -> Unit) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .clickable { onClickListener() }
            .padding(start = 24.dp * model.depthLevel)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FileItemIcon(model)

        Spacer(modifier = Modifier.width(5.dp))

        FileItemText(model, Modifier.align(Alignment.CenterVertically))
    }
}