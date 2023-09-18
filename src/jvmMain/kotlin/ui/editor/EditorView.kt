package ui.editor

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ui.ThemeApp
import ui.editor.codeAutoCompletion.AutoCompleteDropdown
import ui.editor.codeAutoCompletion.KeywordAutoCompleteUtil
import ui.editor.tabs.TabsState
import ui.editor.tabs.TabsView
import util.DocumentsManager
import util.TextUtils
import java.io.File

/**
 * Composable function to display the code editor view
 *
 * @param tabsState The state of editor tabs
 * @param editorState The state of the code editor
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditorView(
    tabsState: TabsState,
    editorState: EditorState
) {

    // CScroll state for the text editor
    val scrollState = rememberScrollState()
    // Coroutine scope for handling coroutines within the Composable
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {

        // Write the editor content to the associated file
        DocumentsManager.writeFile(File(editorState.filePath.value), editorState.codeText.value.text)

        TabsView(tabsState){
            editorState.codeText.value = TextFieldValue(File(it).readText())
            editorState.filePath.value = it
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(modifier = Modifier.weight(1f)) {

            LinesNumberView(editorState.linesCount.value, scrollState)

            Spacer(modifier = Modifier.width(10.dp))

            Box(modifier = Modifier.fillMaxSize()){
                BasicTextField(
                    value = editorState.codeText.value,
                    onValueChange = {
                        if (it.text != editorState.codeText.value.text){
                            val selectedWord = TextUtils.extractSurroundingWord(it.selection.start, it.text)
                            editorState.wordToSearch.value = selectedWord

                            editorState.autoCompleteSuggestions.value = KeywordAutoCompleteUtil.autoCompleteKeywords(selectedWord)
                            editorState.isAutoCompleteVisible.value = editorState.autoCompleteSuggestions.value.isNotEmpty()
                        } else {
                            editorState.autoCompleteSuggestions.value = emptyList()
                            editorState.isAutoCompleteVisible.value = false
                        }

                        editorState.codeText.value = it //TextFieldValue(it.text.replace("\t", " "), TextRange(it.selection.start, it.selection.end))

                    },
                    onTextLayout = {
                        val lineCount = it.lineCount
                        if(lineCount != editorState.linesCount.value) editorState.linesCount.value = lineCount
                        editorState.textLayoutResult.value = it
                        editorState.cursorXCoordinate.value = it.getHorizontalPosition(editorState.codeText.value.selection.start, true).toInt()
                    },
                    textStyle = TextStyle(
                        fontSize = 13.sp,
                        color = ThemeApp.colors.textColor,
                        fontFamily = ThemeApp.text.codeTextFontFamily,
                        fontWeight = FontWeight.W500
                    ),
                    cursorBrush = SolidColor(ThemeApp.colors.buttonColor),
                    visualTransformation = EditorVisualTransformation(),
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .focusRequester(editorState.textFieldFocusRequester.value)
                        .onPreviewKeyEvent {
                            if(it.key == Key.DirectionDown && editorState.isAutoCompleteVisible.value && !editorState.isKeyBeingPressed.value){
                                if(editorState.selectedItemIndex.value < editorState.autoCompleteSuggestions.value.size -1 ) editorState.selectedItemIndex.value ++
                                editorState.isKeyBeingPressed.value = true
                                true
                            } else if(it.key == Key.DirectionUp && editorState.isAutoCompleteVisible.value && !editorState.isKeyBeingPressed.value){
                                if(editorState.selectedItemIndex.value > 0) editorState.selectedItemIndex.value --
                                editorState.isKeyBeingPressed.value = true
                                true
                            } else if(it.key == Key.Enter && editorState.isAutoCompleteVisible.value && !editorState.isKeyBeingPressed.value){
                                val newText = TextUtils.insertTextAtCursorPosition(editorState.codeText.value.selection.start, editorState.codeText.value.text, editorState.autoCompleteSuggestions.value[editorState.selectedItemIndex.value])
                                editorState.codeText.value = TextFieldValue(newText, TextRange(editorState.codeText.value.selection.start + editorState.autoCompleteSuggestions.value[editorState.selectedItemIndex.value].length - editorState.wordToSearch.value.length))
                                editorState.selectedItemIndex.value = 0
                                editorState.autoCompleteSuggestions.value = emptyList()
                                editorState.isAutoCompleteVisible.value = false
                                editorState.isKeyBeingPressed.value = true
                                true
                            } else if(it.key == Key.Enter && !editorState.isKeyBeingPressed.value){
                                coroutineScope.launch{ scrollState.scrollTo(scrollState.value + 10) }

                                // Extract the current code text, cursor position, and text before the cursor
                                val codeText = editorState.codeText.value.text
                                val cursorPosition = editorState.codeText.value.selection.start
                                val textBeforeCursor = codeText.substring(0, cursorPosition)

                                // Extract the last line of text and split the text before the cursor into individual words
                                val lastLine = textBeforeCursor.lines().last()
                                val wordsInText = textBeforeCursor.split("\\s+".toRegex())

                                // Check if specific conditions are met to determine whether to add spaces and newlines
                                if(textBeforeCursor.last() == ':' ||
                                    (lastLine.contains("   ") && lastLine != "   ") ||
                                    wordsInText.last() == ".data" ||
                                    wordsInText.last() == ".bss" ||
                                    wordsInText.last() == ".text"
                                ) {
                                    editorState.codeText.value = TextUtils.insertSpacesInText(codeText, cursorPosition, "\n   ")
                                } else {
                                    editorState.codeText.value = TextUtils.insertSpacesInText(codeText, cursorPosition, "\n")
                                }

                                editorState.isKeyBeingPressed.value = true
                                true
                            } else if(it.key == Key.Tab && !editorState.isKeyBeingPressed.value){
                                editorState.codeText.value = TextUtils.insertSpacesInText(editorState.codeText.value.text, editorState.codeText.value.selection.start, "   ")
                                editorState.isKeyBeingPressed.value = true
                                true
                            } else if(it.type == KeyEventType.KeyUp){
                                editorState.isKeyBeingPressed.value = false
                                false
                            } else {
                                false
                            }
                        }
                )

                // Display teh auto-complete dropdown if suggestions are available
                if(editorState.isAutoCompleteVisible.value){
                    AutoCompleteDropdown(
                        items = editorState.autoCompleteSuggestions.value,
                        cursorX = editorState.cursorXCoordinate.value,
                        cursorY = editorState.cursorYCoordinate.value - scrollState.value,
                        selectedItemIndex = editorState.selectedItemIndex.value
                    )
                } else editorState.selectedItemIndex.value = 0

                VerticalScrollbar(
                    ScrollbarAdapter(scrollState),
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    style = defaultScrollbarStyle().copy(
                        unhoverColor = ThemeApp.colors.secondColor,
                        hoverColor = ThemeApp.colors.secondColor,
                        thickness = 5.dp
                    )
                )
            }
        }
    }
}