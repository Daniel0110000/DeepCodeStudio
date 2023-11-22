package ui.terminal

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import domain.utilies.TextUtils
import ui.ThemeApp
import ui.viewModels.terminal.TerminalViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun prompt(
    viewModel: TerminalViewModel,
    onErrorOccurred: (String) -> Unit
){

    // [FocusRequester] to request focus for the text field
    val focus = remember { FocusRequester() }

    // Value observers
    val isKeyBeingPressed = viewModel.isKeyBeingPressed.observeAsState().value
    val command = viewModel.command.observeAsState().value
    val currentDirectory = viewModel.currentDirectory.observeAsState().value

    // State selected word in the text field
    val selectedWord = remember { mutableStateOf("") }

    // Request focus when the component is first launched
    LaunchedEffect(Unit){ focus.requestFocus() }

    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = buildAnnotatedString {
                append(AnnotatedString("\uF314  [${currentDirectory}]", spanStyle = SpanStyle(color = Color(0xFF3BC368))))
                append("~$")
            },
            color = ThemeApp.colors.textColor,
            fontFamily = ThemeApp.text.fontFamily,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(5.dp))

        BasicTextField(
            value = command,
            onValueChange = {
                if(it.text != command.text){
                    selectedWord.value = TextUtils.extractSurroundingWord(
                        it.selection.start,
                        it.text
                    )
                }

                if(it.text.isEmpty()){
                    // if [it.text] is empty, clear suggestions
                    viewModel.clearSuggestions()
                }

                viewModel.setCommand(it)
            },
            textStyle = TextStyle.Default.copy(
                color = ThemeApp.colors.textColor,
                fontFamily = ThemeApp.text.fontFamily,
                fontSize = 14.sp
            ),
            onTextLayout = {
                viewModel.setCursorXCoordinates(it.getHorizontalPosition(
                    command.selection.start,
                    true
                ).toInt())
            },
            singleLine = true,
            cursorBrush = SolidColor(Color(0xFFABB2BF)),
            modifier = Modifier
                .weight(1f)
                .focusRequester(focus)
                .onPreviewKeyEvent {
                    if(it.key == Key.Enter && !isKeyBeingPressed && viewModel.suggestions.value.isEmpty()){
                        // Execute command when Enter is pressed, clear terminal if the command is "clear"
                        if(command.text == "clear") viewModel.clearTerminal()
                        else{
                            val commandResult = ExecuteCommands.executeCommand(command.text, viewModel)
                            viewModel.setDirectory(viewModel.currentDirectory.value)
                            viewModel.setCommandExecuted(command.text)
                            viewModel.setResult(commandResult)
                            // If [commandResult] contains 'error,' we call the callback [onErrorOccurred] passing the [commandResult]
                            if(commandResult.contains("error")) onErrorOccurred(commandResult)
                        }

                        viewModel.setCommand(TextFieldValue(""))

                        viewModel.setIsKeyBeingPressed(true)
                        true
                    } else if (it.isCtrlPressed && it.key == Key.L && !isKeyBeingPressed){
                        // Handle Ctrl + L to clear the terminal
                        viewModel.clearTerminal()
                        viewModel.setIsKeyBeingPressed(true)
                        true
                    } else if (it.key == Key.Tab && !isKeyBeingPressed){
                        // Handle Tab key for autocomplete suggestions
                        viewModel.setSelectedItemIndex(0)
                        viewModel.getSuggestions(selectedWord.value)
                        if(viewModel.suggestions.value.size == 1){
                            // Assigns the selected command from suggestions to the input through the viewModel
                            viewModel.setSuggestionToInput(viewModel.suggestions.value.last())

                            // Clear suggestions
                            viewModel.clearSuggestions()
                        }
                        viewModel.setIsKeyBeingPressed(true)
                        true
                    } else if(it.key == Key.DirectionDown && !isKeyBeingPressed && viewModel.suggestions.value.isNotEmpty()){
                        // Handle Down arrow key for navigating suggestions
                        if(viewModel.selectedItemIndex.value < viewModel.suggestions.value.size - 1) viewModel.setSelectedItemIndex(viewModel.selectedItemIndex.value + 1)
                        viewModel.setIsKeyBeingPressed(true)
                        true
                    } else if(it.key == Key.DirectionUp && !isKeyBeingPressed && viewModel.suggestions.value.isNotEmpty()){
                        // Handle Up arrow key for navigating suggestions
                        if(viewModel.selectedItemIndex.value > 0) viewModel.setSelectedItemIndex(viewModel.selectedItemIndex.value - 1)
                        viewModel.setIsKeyBeingPressed(true)
                        true
                    } else if (it.key == Key.Enter && viewModel.suggestions.value.isNotEmpty() && !isKeyBeingPressed){
                        // Assigns the selected command from suggestions to the input through the viewModel
                        viewModel.setSuggestionToInput(viewModel.suggestions.value[viewModel.selectedItemIndex.value])

                        // Reset selected item and clear suggestions
                        viewModel.setSelectedItemIndex(0)
                        viewModel.clearSuggestions()
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