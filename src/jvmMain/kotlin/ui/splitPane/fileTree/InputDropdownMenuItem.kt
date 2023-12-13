package ui.splitPane.fileTree

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.ThemeApp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputDropdownMenuItem(
    typeAction: Actions,
    model: FileInfo,
    dismissDropDown: () -> Unit
) = DropdownMenuItem(
    onClick = {},
    modifier = Modifier.height(40.dp),
    enabled = false
){

    val name = remember { mutableStateOf(if(typeAction == Actions.RENAME) model.file.name else "") }
    val isKeyBeingPressed = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            when (typeAction) {
                Actions.NEW_FOLDER -> painterResource("images/ic_new_folder.svg")
                Actions.NEW_FILE -> painterResource("images/ic_new_file.svg")
                Actions.RENAME -> painterResource("images/ic_rename.svg")
                else -> painterResource("images/ic_unknown.svg")
            },
            contentDescription = "Icon",
            modifier = Modifier.size(20.dp),
            tint = ThemeApp.colors.buttonColor
        )

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .height(35.dp)
                .fillMaxWidth()
                .background(ThemeApp.colors.background, shape = RoundedCornerShape(5.dp)),
            contentAlignment = Alignment.Center
        ){
            BasicTextField(
                value = name.value,
                onValueChange = { name.value = it },
                singleLine = true,
                textStyle = TextStyle.Default.copy(
                    fontSize = 12.sp,
                    color = ThemeApp.colors.textColor,
                    fontFamily = ThemeApp.text.fontFamily,
                ),
                cursorBrush = SolidColor(ThemeApp.colors.buttonColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .onPreviewKeyEvent {
                        if(it.key == Key.Enter && !isKeyBeingPressed.value){
                            performAction(typeAction, model.file, name.value)
                            dismissDropDown()
                            name.value = ""
                            isKeyBeingPressed.value = true
                            true
                        } else if(it.type == KeyEventType.KeyUp){
                            isKeyBeingPressed.value = false
                            false
                        } else {
                            false
                        }
                    }
            )
        }

    }
}