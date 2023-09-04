package ui.components.lazy.filetree

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ui.ThemeApp
import ui.fileTree.FileTree

/**
 * Composable function to display an icon for a file or folder in a file tree
 *
 * @param model The FileTree.Item model representing the file or folder
 */
@Composable
fun FileItemIcon(model: FileTree.Item) = when(val type = model.type){
    is FileTree.ItemType.Folder -> when{
        !type.canExpand -> Unit
        type.isExpandable -> ItemIcon("images/ic_folder.svg", "File icon")
        else -> Icon(Icons.Rounded.KeyboardArrowRight, contentDescription = "Keyboard arrow right icon", tint = ThemeApp.colors.buttonColor, modifier = Modifier.size(18.dp))
    }
    is FileTree.ItemType.File -> when(type.ext){
        "asm" -> ItemIcon("images/ic_asm.svg", "Asm icon", ThemeApp.colors.asmIconColor)
        else -> ItemIcon("images/ic_unknown.svg", "Unknown icon")
    }
}

@Composable
fun ItemIcon(
    resourcePath: String,
    description: String,
    color: Color = ThemeApp.colors.buttonColor
) = Icon(painterResource(resourcePath), contentDescription = description, tint = color, modifier = Modifier.size(18.dp))