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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import domain.utilies.TextUtils
import ui.ThemeApp
import ui.viewModels.terminal.TerminalViewModel

@Composable
fun prompt(
    viewModel: TerminalViewModel,
    onErrorOccurred: (String) -> Unit
){

    // [FocusRequester] to request focus for the text field
    val focus = remember { FocusRequester() }

    // Value observers
    val command = viewModel.command.observeAsState().value
    val currentDirectory = viewModel.currentDirectory.observeAsState().value
    val allCommandHistory = viewModel.allCommandHistory.observeAsState().value

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
                .onPreviewKeyEvent { terminalKeyEvents(it, viewModel, selectedWord.value, allCommandHistory){ r -> onErrorOccurred(r) } }
        )
    }

}