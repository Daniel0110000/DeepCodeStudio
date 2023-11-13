package ui.fileTree

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.ThemeApp
import domain.utilies.DocumentsManager

@Composable
fun FileOptionsMenu(
    model: FileInfo,
    dropDownExpanded: Boolean,
    dismissDropDown: () -> Unit
) {

    val showDialog = remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var typeAction by remember { mutableStateOf(Actions.NONE) }

    DropdownMenu(
        expanded = dropDownExpanded,
        onDismissRequest = { dismissDropDown() },
        offset = DpOffset(70.dp, 0.dp),
        modifier = Modifier.background(ThemeApp.colors.secondColor).width(150.dp)
    ){

        if(model.file.isDirectory){
            DropdownMenuItem(
                onClick = {
                    dismissDropDown()
                    showDialog.value = true
                    dialogTitle = "New File"
                    typeAction = Actions.NEW_FILE
                },
                modifier = Modifier.height(30.dp)
            ){
                Icon(
                    painterResource("images/ic_new_file.svg"),
                    contentDescription = "New folder icon",
                    tint = ThemeApp.colors.textColor,
                    modifier = Modifier.size(17.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    "New File",
                    color = ThemeApp.colors.textColor,
                    fontFamily = ThemeApp.text.fontFamily,
                    fontSize = 12.sp
                )

            }

            DropdownMenuItem(
                onClick = {
                    dismissDropDown()
                    showDialog.value = true
                    dialogTitle = "New Folder"
                    typeAction = Actions.NEW_FOLDER
                },
                modifier = Modifier.height(30.dp)
            ){
                Icon(
                    painterResource("images/ic_new_folder.svg"),
                    contentDescription = "New folder icon",
                    tint = ThemeApp.colors.textColor,
                    modifier = Modifier.size(17.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    "New Folder",
                    color = ThemeApp.colors.textColor,
                    fontFamily = ThemeApp.text.fontFamily,
                    fontSize = 12.sp
                )

            }
        }

        DropdownMenuItem(
            onClick = {
                dismissDropDown()
                showDialog.value = true
                dialogTitle = "Rename"
                typeAction = Actions.RENAME
            },
            modifier = Modifier.height(30.dp)
        ){
            Icon(
                painterResource("images/ic_rename.svg"),
                contentDescription = "Rename icon",
                tint = ThemeApp.colors.textColor,
                modifier = Modifier.size(17.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                "Rename",
                color = ThemeApp.colors.textColor,
                fontFamily = ThemeApp.text.fontFamily,
                fontSize = 12.sp
            )

        }

        DropdownMenuItem(
            onClick = {
                DocumentsManager.deleteFileOrDirectory(model.file)
                dismissDropDown()
            },
            modifier = Modifier.height(30.dp)
        ){
            Icon(
                painterResource("images/ic_delete.svg"),
                contentDescription = "",
                tint = ThemeApp.colors.textColor,
                modifier = Modifier.size(17.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                "Delete",
                color = ThemeApp.colors.textColor,
                fontFamily = ThemeApp.text.fontFamily,
                fontSize = 12.sp
            )

        }
    }

    if(showDialog.value){
        RenameOrCreateFileOrDirectoryDialog(
            onCloseRequest = { showDialog.value = false },
            dismissDialog = { showDialog.value = false },
            dialogTitle = dialogTitle,
            file = model.file,
            type = typeAction
        )
    }

}