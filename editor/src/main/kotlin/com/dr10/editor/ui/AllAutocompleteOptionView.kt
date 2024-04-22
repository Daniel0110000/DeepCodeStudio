package com.dr10.editor.ui

import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.EmptyMessage
import com.dr10.common.ui.editor.EditorErrorState
import com.dr10.common.utilities.DocumentsManager
import com.dr10.editor.ui.viewModels.EditorViewModel
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import java.awt.Cursor
import java.awt.event.MouseEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AllAutocompleteOptionView(
    editorState: EditorState,
    viewModel: EditorViewModel,
    errorState: EditorErrorState,
    modifier: Modifier
){

    // Value observers
    val viewWidth = viewModel.autocompleteOptionsViewWidth.observeAsState().value
    val selectedOption = viewModel.selectedOption.observeAsState().value

    val scrollState = rememberLazyListState() // Scroll state for the view

    // If [editorState.displayAutocompleteOptions] or [editorState.displayUpdateAutocompleteOption] is true, the view with all the options is displayed
    if(editorState.displayAutocompleteOptions.value || editorState.displayUpdateAutocompleteOption.value){
        viewModel.getAllAutocompleteOptions() // Gets all autocomplete options
        ConstraintLayout(
            modifier = modifier
                .width(viewWidth.dp)
                .background(ThemeApp.colors.secondColor)
        ) {

            val (titleView, optionsList, draggableLine, verticalScrollbar) = createRefs()

            Text(
                "Choose Option",
                color = ThemeApp.colors.textColor,
                fontFamily = ThemeApp.text.fontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(titleView) {
                    top.linkTo(parent.top, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            // If [viewModel.allAutocompleteOptions] is not empty, all autocomplete options are displayed
            if(viewModel.allAutocompleteOptions.value.isNotEmpty()){
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier
                        .constrainAs(optionsList) {
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                            top.linkTo(titleView.bottom, margin = 10.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    items(viewModel.allAutocompleteOptions.value){ model ->
                        // Check if the JSON file exists to display the option
                        if(DocumentsManager.existsFile(model.jsonPath)){
                            val isHoverOption = remember { mutableStateOf(false) }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(35.dp)
                                    .padding(horizontal = 5.dp)
                                    .onPointerEvent(PointerEventType.Enter){ isHoverOption.value = true }
                                    .onPointerEvent(PointerEventType.Exit){ isHoverOption.value = false }
                                    .background(if(model == selectedOption) ThemeApp.colors.buttonColor else if(isHoverOption.value) ThemeApp.colors.hoverTab else Color.Transparent, shape = RoundedCornerShape(5.dp))
                                    .onPointerEvent(PointerEventType.Press){
                                        when(it.awtEventOrNull?.button){
                                            MouseEvent.BUTTON1 -> when(it.awtEventOrNull?.clickCount){
                                                1 -> { viewModel.setSelectedOption(model) }
                                                2 -> {
                                                    // If [editorState.displayAutocompleteOptions] is true, the selectedOption function of the viewModel is executed
                                                    if(editorState.displayAutocompleteOptions.value) viewModel.selectedOption(model, errorState)
                                                    // If [editor.displayAutocompleteOptions] is true, the updateSelectedOption function of the viewModel is executed
                                                    if(editorState.displayUpdateAutocompleteOption.value) viewModel.updateSelectedOption(model, editorState, errorState)
                                                }
                                            }
                                        }
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Spacer(modifier = Modifier.width(5.dp))

                                Icon(
                                    painterResource("images/ic_asm.svg"),
                                    contentDescription = "Asm icon",
                                    tint = ThemeApp.colors.asmIconColor,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(5.dp))

                                Text(
                                    model.optionName,
                                    color = ThemeApp.colors.textColor,
                                    fontFamily = ThemeApp.text.fontFamily,
                                    fontSize = 13.sp,
                                )
                            }
                        } else {
                            // If the JSON file does not exist, update the data in [EditorErrorState] to handle this error
                            errorState.uuid.value = model.uuid
                            errorState.displayErrorMessage.value = true
                            errorState.errorDescription.value = "JSON file not found at the specified path '${model.jsonPath}'"
                            // Gets all autocomplete options again
                            viewModel.getAllAutocompleteOptions()
                        }
                    }
                }
            } else EmptyMessage() // If [viewModel.allAutocompleteOptions] is empty, [EmptyMessage] is displayed

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .background(ThemeApp.colors.hoverTab)
                    .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)))
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { viewModel.setAutocompleteOptionsViewWidth(-it) }
                    )
                    .constrainAs(draggableLine) {
                        height = Dimension.fillToConstraints
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )

            VerticalScrollbar(
                ScrollbarAdapter(scrollState),
                style = ThemeApp.scrollbar.scrollbarStyle,
                modifier = Modifier
                    .fillMaxHeight()
                    .constrainAs(verticalScrollbar) {
                        height = Dimension.fillToConstraints
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            )

        }
    } else Spacer(modifier = modifier)

}