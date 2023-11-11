package ui.settings

import App
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.onClick
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.WindowPosition
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ui.ThemeApp
import ui.settings.screens.AutocompleteSettings
import ui.settings.screens.syntaxHighlight.SyntaxHighlightSettings
import ui.viewModels.settings.SettingsViewModel

@Composable
fun Settings(onCloseRequest: () -> Unit) {

    val viewModel = App().settingsViewModel

    val screen = viewModel.screen.observeAsState().value

    Dialog(
        visible = true,
        state = DialogState(position = WindowPosition(Alignment.Center), width = 900.dp, height = 500.dp),
        onCloseRequest = { onCloseRequest() },
        title = "Settings"
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(ThemeApp.colors.background)
        ) {
            SettingsOptions(viewModel)

            val modifier = Modifier
                .weight(1f)
                .fillMaxHeight()

            when(screen){
                Screens.SYNTAX_KEYWORD_HIGHLIGHTER_SETTINGS -> SyntaxHighlightSettings(modifier)
                Screens.AUTOCOMPLETE_SETTINGS -> AutocompleteSettings(modifier)
            }

        }

    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun SettingsOptions(viewModel: SettingsViewModel) {
    val selectedItem = viewModel.selectedItem.observeAsState().value

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .width(220.dp)
            .background(ThemeApp.colors.secondColor)
    ) {

        item { Spacer(modifier = Modifier.height(8.dp)) }

        items(viewModel.options.value.size) {
            var hoverOption by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp)
                    .background(if(selectedItem == it) ThemeApp.colors.buttonColor else if(hoverOption) ThemeApp.colors.hoverTab else Color.Transparent)
                    .onPointerEvent(PointerEventType.Enter){ hoverOption = true }
                    .onPointerEvent(PointerEventType.Exit){ hoverOption = false }
                    .onClick {
                        viewModel.setSelectedItem(it)
                        viewModel.setScreen(viewModel.screens.value[it])
                    },
                contentAlignment = Alignment.Center
            ){
                Text(
                    viewModel.options.value[it],
                    color = ThemeApp.colors.textColor,
                    fontFamily = ThemeApp.text.fontFamily,
                    fontSize = 12.sp,
                )
            }
        }
    }
}

enum class Screens{
    SYNTAX_KEYWORD_HIGHLIGHTER_SETTINGS,
    AUTOCOMPLETE_SETTINGS
}