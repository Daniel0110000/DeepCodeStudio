package ui.fileTree

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.WindowPosition
import ui.ThemeApp
import util.DocumentsManager
import java.io.File

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RenameOrCreateFileOrDirectoryDialog(
    onCloseRequest: () -> Unit,
    dismissDialog: () -> Unit,
    dialogTitle: String,
    file: File,
    type: Actions
) {

    val isKeyBeingPressed = remember { mutableStateOf(false) }
    var dfName by remember { mutableStateOf(if(type == Actions.RENAME) file.name else "") }

    fun performAction(){
        if(dfName.isNotBlank()){
            when(type){
                Actions.NEW_FILE -> { DocumentsManager.createFile(file.absolutePath, dfName) }
                Actions.NEW_FOLDER -> { DocumentsManager.createDirectory(file.absolutePath, dfName) }
                Actions.RENAME -> { DocumentsManager.renameFileOrDirectory(file.absolutePath, dfName) }
                Actions.NONE -> {}
            }
            dfName = ""
            dismissDialog()
        }
    }

    Dialog(
        onCloseRequest = { onCloseRequest() },
        title = dialogTitle,
        state = DialogState(position = WindowPosition(Alignment.Center), size = DpSize(300.dp, 152.dp)),
        resizable = false
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ThemeApp.colors.background)
                .padding(10.dp)
                .onPreviewKeyEvent {
                    if(it.key == Key.Enter && !isKeyBeingPressed.value){
                        performAction()
                        isKeyBeingPressed.value = true
                        true
                    } else if(it.type == KeyEventType.KeyUp){
                        isKeyBeingPressed.value = false
                        false
                    } else {
                        false
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    when (type) {
                        Actions.NEW_FOLDER -> painterResource("images/ic_new_folder.svg")
                        Actions.NEW_FILE -> painterResource("images/ic_new_file.svg")
                        Actions.RENAME -> painterResource("images/ic_rename.svg")
                        else -> painterResource("images/ic_unknown.svg")
                    },
                    contentDescription = "Icon",
                    modifier = Modifier.size(25.dp),
                    tint = ThemeApp.colors.textColor
                )

                Spacer(modifier = Modifier.width(10.dp))

                TextField(
                    value = dfName,
                    onValueChange = { dfName = it },
                    modifier = Modifier.height(47.dp).fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text(
                        "Name",
                        color = Color(0xFF5C6370),
                        fontFamily = ThemeApp.text.fontFamily,
                        fontSize = 12.sp
                    ) },
                    textStyle = TextStyle.Default.copy(
                        fontSize = 12.sp,
                        color = ThemeApp.colors.textColor,
                        fontFamily = ThemeApp.text.fontFamily,
                        textAlign = TextAlign.Justify
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = ThemeApp.colors.buttonColor,
                        backgroundColor = ThemeApp.colors.secondColor
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { performAction() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ThemeApp.colors.buttonColor
                    ),
                    modifier = Modifier.height(30.dp)
                ){
                    Text(
                        if(type == Actions.RENAME) "Rename" else "Create",
                        color = ThemeApp.colors.textColor,
                        fontFamily = ThemeApp.text.fontFamily,
                        fontSize = 12.sp
                    )
                }
            }

        }
    }
}