package ui.components.lazy.filetree

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import ui.ThemeApp
import ui.fileTree.FileTree

/**
 * Composable function to display text for a file or folder in a file tree
 *
 * @param model The FileTree.Item model representing the file or folder
 * @param modifier Modifier for customizing the text appearance
 */
@Composable
fun FileItemText(model: FileTree.Item, modifier: Modifier) = when(val type = model.type){
    is FileTree.ItemType.Folder -> when{
        !type.canExpand -> Unit
        type.isExpandable -> ItemText(model.name, ThemeApp.colors.folderCloseTextColor, modifier)
        else -> ItemText(model.name, ThemeApp.colors.folderOpenTextColor, modifier)
    }
    is FileTree.ItemType.File -> ItemText(model.name, modifier = modifier)
}

@Composable
fun ItemText(
    text: String,
    color: Color = ThemeApp.colors.textColor,
    modifier: Modifier
) = Text(
    text = text,
    color = color,
    softWrap = true,
    modifier = modifier,
    fontSize = 13.sp,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1,
    fontFamily = ThemeApp.text.fontFamily
)