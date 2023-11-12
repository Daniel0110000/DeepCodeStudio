package ui.terminal

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.util.DocumentsManager
import ui.ThemeApp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun prompt(
    onResult: (String) -> Unit,
    onCommand: (String) -> Unit
){
    var command by remember { mutableStateOf("") }
    var isKeyBeingPressed = false

    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = buildAnnotatedString {
                append(AnnotatedString("[${DocumentsManager.getUserHome()}]", spanStyle = SpanStyle(color = Color(0xFF3BC368))))
                append("~$")
            },
            color = ThemeApp.colors.textColor,
            fontFamily = ThemeApp.text.fontFamily,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(5.dp))

        BasicTextField(
            value = command,
            onValueChange = { command = it },
            textStyle = TextStyle.Default.copy(
                color = ThemeApp.colors.textColor,
                fontFamily = ThemeApp.text.fontFamily,
                fontSize = 14.sp
            ),
            singleLine = true,
            cursorBrush = SolidColor(Color(0xFFABB2BF)),
            modifier = Modifier
                .weight(1f)
                .onPreviewKeyEvent {
                    if(it.key == Key.Enter && !isKeyBeingPressed){
                        onCommand(command)
                        isKeyBeingPressed = true
                        true
                    } else if (it.type == KeyEventType.KeyUp){
                        isKeyBeingPressed = false
                        false
                    } else {
                        false
                    }
                }
        )
    }
}