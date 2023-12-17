package ui.settings

import App
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import kotlinx.coroutines.launch
import ui.ThemeApp
import ui.components.ErrorMessage
import ui.settings.screens.autocomplete.AutocompleteSettingsScreen
import ui.settings.screens.syntaxHighlight.SyntaxHighlightSettingsScreen
import ui.viewModels.settings.AutocompleteSettingsViewModel
import ui.viewModels.settings.SettingsViewModel
import ui.viewModels.settings.SyntaxHighlightSettingsViewModel

@Composable
fun Settings(onCloseRequest: () -> Unit) {

    // Inject [SettingsViewModel], [SyntaxHighlightSettingsViewModel] and [AutocompleteSettingsViewModel]
    val viewModel: SettingsViewModel = App().settingsViewModel
    val syntaxHighlightSettingsViewModel: SyntaxHighlightSettingsViewModel = App().syntaxHighlightSettingsViewModel
    val autocompleteSettingsViewModel: AutocompleteSettingsViewModel = App().autocompleteSettingsViewModel

    // Create an instance of [SettingsErrorState]
    val errorState = remember { SettingsErrorState() }

    val coroutineScope = rememberCoroutineScope()

    // Value observers
    val screen = viewModel.screen.observeAsState().value

    DialogWindow(
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
            SettingOptions(viewModel)

            val modifier = Modifier
                .weight(1f)
                .fillMaxHeight()

            when(screen){
                Screens.SYNTAX_KEYWORD_HIGHLIGHTER_SETTINGS -> SyntaxHighlightSettingsScreen(modifier, errorState)
                Screens.AUTOCOMPLETE_SETTINGS -> AutocompleteSettingsScreen(modifier, errorState)
            }

        }

        // If [errorState.displayErrorMessage] is true, [ErrorMessage] is displayed
        if(errorState.displayErrorMessage.value){
            ErrorMessage(
                errorDescription = errorState.errorDescription.value,
                onCloseRequest = {
                    coroutineScope.launch {
                        // Deletes the configurations and updates the data
                        viewModel.deleteConfigs(errorState.uuid.value)
                        syntaxHighlightSettingsViewModel.updateSyntaxHighlightConfigs()
                        autocompleteSettingsViewModel.updateAutocompleteOptions()
                        errorState.displayErrorMessage.value = false
                    }
                }
            )
        }

    }
}