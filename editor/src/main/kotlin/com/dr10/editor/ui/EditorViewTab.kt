package com.dr10.editor.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.LocalTextContextMenu
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.editor.EditorVisualTransformation
import com.dr10.common.utilities.DocumentsManager
import com.dr10.database.domain.repositories.AutocompleteSettingsRepository
import com.dr10.editor.ui.autocompleteCode.AutocompleteDropdownView
import com.dr10.editor.ui.components.BottomActionsRow
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditorViewTab(
    modifier: Modifier,
    state: EditorState,
    autocompleteSettingsRepository: AutocompleteSettingsRepository
){

    // Vertical scroll state for the editor
    val verticalScrollState = rememberScrollState()
    // Horizontal scroll state for the editor
    val horizontalScrollState = rememberScrollState()
    // Coroutine scope for handling coroutines within the Composable
    val coroutineScope = rememberCoroutineScope()

    // If the current file exists, write the editor content to the associated file
    if (File(state.filePath.value).exists()) DocumentsManager.writeFile(File(state.filePath.value), state.codeText.value.text)

    // LaunchedEffect to request focus for the TextField when the view is created
    LaunchedEffect(Unit){ state.textFieldFocusRequester.value.requestFocus() }

    ConstraintLayout(modifier = modifier) {
        val (linesNumber, editor, bottomActions, verticalScrollBar) = createRefs()

        LinesNumber(
            state.linesCount.value,
            scrollState = verticalScrollState,
            modifier = Modifier.constrainAs(linesNumber){
                height = Dimension.fillToConstraints
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(bottomActions.top)
            }
        )

        CompositionLocalProvider(LocalTextContextMenu provides EmptyContextMenu){
            BasicTextField(
                value = state.codeText.value,
                onValueChange = { state.onValueChange(it) },
                readOnly = state.readOnly.value,
                onTextLayout = { state.onTextLayout(it){ coroutineScope.launch { verticalScrollState.scrollTo(state.lineIndex.value * 10) } } },
                textStyle = TextStyle(
                    fontSize = 13.sp,
                    color = ThemeApp.colors.textColor,
                    fontFamily = ThemeApp.text.codeTextFontFamily,
                    fontWeight = FontWeight.W500
                ),
                cursorBrush = SolidColor(ThemeApp.colors.buttonColor),
                visualTransformation = EditorVisualTransformation(state.syntaxHighlightConfig.value, state.syntaxHighlightRegexModel.value),
                modifier = Modifier
                    .constrainAs(editor) {
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                        top.linkTo(parent.top)
                        bottom.linkTo(bottomActions.top)
                        start.linkTo(linesNumber.end)
                        end.linkTo(parent.end)
                    }
                    .focusRequester(state.textFieldFocusRequester.value)
                    .onPreviewKeyEvent{ editorKeyEvents(it, state) }
                    .verticalScroll(verticalScrollState)
            )
        }


        // Displays the visual indicator of the selected line
        selectedLine(
            17 * (if(state.lineIndex.value != 0) state.lineIndex.value - 1 else state.lineIndex.value) - verticalScrollState.value,
        )

        // Display teh auto-complete dropdown if suggestions are available
        if (state.isAutoCompleteVisible.value) {
            AutocompleteDropdownView(
                items = state.autoCompleteSuggestions.value,
                editorState = state,
                cursorX = state.cursorXCoordinate.value - horizontalScrollState.value,
                cursorY = state.cursorYCoordinate.value - verticalScrollState.value,
                selectedItemIndex = state.selectedItemIndex.value,
                state.variableDirectives.value
            )
        } else state.selectedItemIndex.value = 0

        VerticalScrollbar(
            ScrollbarAdapter(verticalScrollState),
            modifier = Modifier
                .constrainAs(verticalScrollBar){
                    height = Dimension.fillToConstraints
                    top.linkTo(parent.top)
                    bottom.linkTo(bottomActions.top)
                    end.linkTo(parent.end)
                },
            style = ThemeApp.scrollbar.scrollbarStyle
        )

        BottomActionsRow(
            state,
            autocompleteSettingsRepository,
            modifier = Modifier.constrainAs(bottomActions){
                width = Dimension.fillToConstraints
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        )

    }
//    Column(modifier = modifier) {
//        BoxWithConstraints(modifier = Modifier.weight(1f)) {
//
//
//
//
//
//            // Vertical scroll for the code editor
//            VerticalScrollbar(
//                ScrollbarAdapter(verticalScrollState),
//                modifier = Modifier
//                    .align(Alignment.CenterEnd)
//                    .fillMaxHeight(),
//                style = ThemeApp.scrollbar.scrollbarStyle
//            )
//
//            // Horizontal scroll for the code editor
//            HorizontalScrollbar(
//                ScrollbarAdapter(horizontalScrollState),
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .fillMaxWidth(),
//                style = ThemeApp.scrollbar.scrollbarStyle
//            )
//        }
//        // Create the bottom actions row
//
//    }
}