package ui.editor

import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ui.ThemeApp
import ui.components.EmptyMessage
import ui.viewModels.editor.EditorViewModel
import java.awt.Cursor
import java.awt.event.MouseEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AllAutocompleteOptionView(
    editorState: EditorState,
    viewModel: EditorViewModel
){

    // Value observers
    val width = viewModel.autocompleteOptionsViewWidth.observeAsState().value
    val selectedOption = viewModel.selectedOption.observeAsState().value

    val scrollState = rememberLazyListState() // Scroll state for the view

    // If [editorState.displayAutocompleteOptions] or [editorState.displayUpdateAutocompleteOption] is true, the view with all the options is displayed
    if(editorState.displayAutocompleteOptions.value || editorState.displayUpdateAutocompleteOption.value){
        viewModel.getAllAutocompleteOptions() // Gets all autocomplete options
        Box(
            modifier = Modifier
                .width(width.dp)
                .fillMaxHeight()
                .background(ThemeApp.colors.secondColor)
        ){
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    "Choose Option",
                    color = ThemeApp.colors.textColor,
                    fontFamily = ThemeApp.text.fontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                )

                // If [viewModel.allAutocompleteOptions] is not empty, all autocomplete options are displayed
                if(viewModel.allAutocompleteOptions.value.isNotEmpty()){
                    LazyColumn(
                        state = scrollState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(viewModel.allAutocompleteOptions.value){ model ->

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
                                                    if(editorState.displayAutocompleteOptions.value) viewModel.selectedOption(model)
                                                    // If [editor.displayAutocompleteOptions] is true, the updateSelectedOption function of the viewModel is executed
                                                    if(editorState.displayUpdateAutocompleteOption.value) viewModel.updateSelectedOption(model, editorState)
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
                        }
                    }
                } else EmptyMessage() // If [viewModel.allAutocompleteOptions] is empty, [EmptyMessage] is displayed
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(ThemeApp.colors.hoverTab)
                    .align(Alignment.CenterStart)
                    .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)))
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { viewModel.setAutocompleteOptionsViewWidth(-it) }
                    )
            )

            VerticalScrollbar(
                ScrollbarAdapter(scrollState),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                style = ThemeApp.scrollbar.scrollbarStyle
            )

        }
    }
}