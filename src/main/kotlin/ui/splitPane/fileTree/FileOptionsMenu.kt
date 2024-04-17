package ui.splitPane.fileTree

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.DocumentsManager

@Composable
fun FileOptionsMenu(
    model: FileInfo,
    dropDownExpanded: Boolean,
    dismissDropDown: () -> Unit
) {

    var typeAction by remember { mutableStateOf(Actions.NONE) }
    val displayInputDropdownMenuItem = remember { mutableStateOf(false) }

    MaterialTheme(
        colors = MaterialTheme.colors.copy(surface = ThemeApp.colors.secondColor, primary = ThemeApp.colors.buttonColor),
        shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(2.dp))
    ){
        DropdownMenu(
            expanded = dropDownExpanded,
            onDismissRequest = { dismissDropDown() },
            offset = DpOffset(70.dp, 0.dp),
            modifier = Modifier.background(ThemeApp.colors.secondColor).width(200.dp)
        ){
            if(displayInputDropdownMenuItem.value){
                InputDropdownMenuItem(
                    typeAction = typeAction,
                    model = model,
                    dismissDropDown = {
                        dismissDropDown()
                        displayInputDropdownMenuItem.value = false
                    }
                )
            }

            if(model.file.isDirectory){
                DropdownMenuItem(
                    label = "New File",
                    icon = painterResource("images/ic_new_file.svg"),
                    onClick = {
                        displayInputDropdownMenuItem.value = !displayInputDropdownMenuItem.value
                        typeAction = Actions.NEW_FILE
                    }
                )

                DropdownMenuItem(
                    label = "New Folder",
                    icon = painterResource("images/ic_new_folder.svg"),
                    onClick = {
                        displayInputDropdownMenuItem.value = !displayInputDropdownMenuItem.value
                        typeAction = Actions.NEW_FOLDER
                    }
                )
            }

            DropdownMenuItem(
                label = "Rename",
                icon = painterResource("images/ic_rename.svg"),
                onClick = {
                    displayInputDropdownMenuItem.value = !displayInputDropdownMenuItem.value
                    typeAction = Actions.RENAME
                }
            )

            DropdownMenuItem(
                label = "Delete",
                icon = painterResource("images/ic_delete.svg"),
                onClick = {
                    DocumentsManager.deleteFileOrDirectory(model.file)
                    dismissDropDown()
                }
            )

        }
    }
}