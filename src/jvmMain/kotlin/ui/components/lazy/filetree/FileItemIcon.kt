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
import ui.fileTree.FileInfo


@Composable
fun FileItemIcon(model: FileInfo){
    if(model.file.isDirectory){
        if(model.isExpanded) Icon(Icons.Rounded.KeyboardArrowRight, contentDescription = "Keyboard arrow right icon", tint = ThemeApp.colors.buttonColor, modifier = Modifier.size(18.dp))
        else ItemIcon("images/ic_folder.svg", "Folder icon")
    } else {
        if(model.file.extension == "asm") ItemIcon("images/ic_asm.svg", "ASM icon", color = ThemeApp.colors.asmIconColor)
        else ItemIcon("images/ic_unknown.svg", "Unknown icon")
    }
}

@Composable
fun ItemIcon(
    resourcePath: String,
    description: String,
    color: Color = ThemeApp.colors.buttonColor
) = Icon(painterResource(resourcePath), contentDescription = description, tint = color, modifier = Modifier.size(18.dp))