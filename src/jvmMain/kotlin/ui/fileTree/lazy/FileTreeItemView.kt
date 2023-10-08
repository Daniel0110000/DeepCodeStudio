package ui.fileTree.lazy

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.unit.dp
import ui.fileTree.FileOptionsMenu
import ui.fileTree.FileInfo

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileTreeItemView(
    model: FileInfo,
    onClickListener: () -> Unit
) {

    var dropDownExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .wrapContentHeight()
            .clickable { onClickListener() }
            .padding(start = 24.dp * model.depthLevel)
            .fillMaxWidth()
            .onClick(
                matcher = PointerMatcher.mouse(PointerButton.Secondary),
                onClick = { dropDownExpanded = true }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FileItemIcon(model)

        Spacer(modifier = Modifier.width(5.dp))

        FileItemText(model, Modifier.align(Alignment.CenterVertically))

        FileOptionsMenu(
            model = model,
            dropDownExpanded = dropDownExpanded,
            dismissDropDown = { dropDownExpanded = false }
        )
    }
}