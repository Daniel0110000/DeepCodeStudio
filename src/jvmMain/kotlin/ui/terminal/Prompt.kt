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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ui.ThemeApp
import ui.viewModels.terminal.TerminalViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun prompt(viewModel: TerminalViewModel){
    // State to hold the current command entered by the user
    var command by remember { mutableStateOf("") }

    // [FocusRequester] to request focus for the text field
    val focus = remember { FocusRequester() }

    // Observe whether a key is currently being pressed
    val isKeyBeingPressed = viewModel.isKeyBeingPressed.observeAsState().value

    // Request focus when the component is first launched
    LaunchedEffect(Unit){ focus.requestFocus() }

    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = buildAnnotatedString {
                append(AnnotatedString("[${viewModel.currentDirectory.value}]", spanStyle = SpanStyle(color = Color(0xFF3BC368))))
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
                .focusRequester(focus)
                .onPreviewKeyEvent {
                    if(it.key == Key.Enter && !isKeyBeingPressed){
                        // Execute command when Enter is pressed, clear terminal if the command is "clear"
                        if(command == "clear") viewModel.clearTerminal()
                        else{
                            viewModel.setDirectory(viewModel.currentDirectory.value)
                            viewModel.setCommandExecuted(command)
                            viewModel.setResult(ExecuteCommands.executeCommand(command, viewModel))
                        }
                        viewModel.setIsKeyBeingPressed(true)
                        true
                    } else if (it.isCtrlPressed && it.key == Key.L && !isKeyBeingPressed){
                        // Handle Ctrl + L to clear the terminal
                        viewModel.clearTerminal()
                        viewModel.setIsKeyBeingPressed(true)
                        true
                    } else if (it.type == KeyEventType.KeyUp){
                        // Release key press flag when key is released
                        viewModel.setIsKeyBeingPressed(false)
                        false
                    } else {
                        false
                    }
                }
        )
    }

}