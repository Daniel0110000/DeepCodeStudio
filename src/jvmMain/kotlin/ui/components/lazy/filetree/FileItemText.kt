package ui.components.lazy.filetree

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import ui.ThemeApp
import ui.fileTree.FileInfo

@Composable
fun FileItemText(model: FileInfo, modifier: Modifier){
    if(model.file.isDirectory){
        if(model.isExpanded) ItemText(model.file.name, ThemeApp.colors.folderCloseTextColor, modifier)
        else ItemText(model.file.name, ThemeApp.colors.folderOpenTextColor, modifier)
    } else ItemText(model.file.name, modifier = modifier)
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