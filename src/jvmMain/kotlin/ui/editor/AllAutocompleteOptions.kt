package ui.editor

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.onClick
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.WindowPosition
import domain.model.AutocompleteOptionModel
import domain.repository.SettingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import ui.ThemeApp
import java.awt.event.MouseEvent

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun AllAutocompleteOptions(
    selectedOption: (AutocompleteOptionModel) -> Unit
) {

    // Inject the [SettingRepository] using Koin
    val settingsRepository: SettingRepository by KoinJavaComponent.inject(SettingRepository::class.java)

    // The selected element
    var selectedItem by remember { mutableStateOf(0) }
    // Check if the key is pressed
    var isKeyBeingPressed by remember { mutableStateOf(false) }

    // Retrieve all autocomplete options from the database
    val allOptions = settingsRepository.getAllAutocompleteOptions()

    val scrollState = rememberScrollState()
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    Dialog(
        visible = true,
        state = DialogState(position = WindowPosition(Alignment.Center), width = 500.dp, height = 300.dp),
        onCloseRequest = { },
        title = "Choose Option",
        onPreviewKeyEvent = {
            if(it.key == Key.DirectionDown && !isKeyBeingPressed){
                // Handle the 'Down' arrow key press
                if(selectedItem < allOptions.size - 1) selectedItem++
                coroutineScope.launch{ scrollState.scrollTo(scrollState.value + 30) }
                isKeyBeingPressed = true
                true
            } else if(it.key == Key.DirectionUp && !isKeyBeingPressed){
                // Handle the 'Up' arrow key press
                if(selectedItem > 0) selectedItem--
                coroutineScope.launch{ scrollState.scrollTo(scrollState.value - 30) }
                isKeyBeingPressed = true
                true
            } else if(it.key == Key.Enter && !isKeyBeingPressed){
                // Handle the 'Enter' key press
                selectedOption(allOptions[selectedItem])
                isKeyBeingPressed = true
              true
            } else if(it.type == KeyEventType.KeyUp){
                isKeyBeingPressed = false
                false
            } else {
                // Other key events not handled
                false
            }
        }
    ){
        Box(modifier = Modifier.fillMaxSize()){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ThemeApp.colors.secondColor)
                    .focusable(true)
                    .verticalScroll(scrollState)
            ) {
                allOptions.forEachIndexed { index, option ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if(selectedItem == index) ThemeApp.colors.background else Color.Transparent)
                            .onClick { selectedItem = index }
                            .onPointerEvent(PointerEventType.Press){
                                  when(it.awtEventOrNull?.button){
                                      MouseEvent.BUTTON1 -> when (it.awtEventOrNull?.clickCount){
                                          2 -> { selectedOption(allOptions[selectedItem]) }
                                      }
                                  }
                            },
                    ){
                        Text(
                            option.optionName,
                            color = ThemeApp.colors.textColor,
                            fontFamily = ThemeApp.text.fontFamily,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(5.dp)
                        )

                        Text(
                            option.jsonPath,
                            color = ThemeApp.colors.textColor,
                            fontFamily = ThemeApp.text.fontFamily,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
                        )

                    }
                }
            }

            VerticalScrollbar(
                ScrollbarAdapter(scrollState),
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd),
                style = ThemeApp.scrollbar.scrollbarStyle
            )

        }
    }
}