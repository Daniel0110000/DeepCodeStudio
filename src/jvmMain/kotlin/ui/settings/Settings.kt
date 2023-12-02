package ui.settings

import App
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ui.ThemeApp
import ui.components.ErrorMessage
import ui.settings.screens.autocomplete.AutocompleteSettings
import ui.settings.screens.syntaxHighlight.SyntaxHighlightSettings

@Composable
fun Settings(onCloseRequest: () -> Unit) {

    val viewModel = App().settingsViewModel

    // Value observers
    val screen = viewModel.screen.observeAsState().value
    val displayErrorMessage = viewModel.displayErrorMessage.observeAsState().value

    Dialog(
        visible = true,
        state = DialogState(width = 1200.dp, height = 700.dp),
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
                Screens.SYNTAX_KEYWORD_HIGHLIGHTER_SETTINGS -> SyntaxHighlightSettings(modifier, viewModel){
                    viewModel.setErrorDescription(it)
                    viewModel.setDisplayErrorMessage(true)
                }
                Screens.AUTOCOMPLETE_SETTINGS -> AutocompleteSettings(modifier){
                    viewModel.setErrorDescription(it)
                    viewModel.setDisplayErrorMessage(true)
                }
            }

        }

        // If [displayErrorMessage] is true, [ErrorMessage] is displayed
        if(displayErrorMessage){
            ErrorMessage(
                errorDescription = viewModel.errorDescription.value,
                onCloseRequest = { viewModel.setDisplayErrorMessage(false) }
            )
        }

    }
}