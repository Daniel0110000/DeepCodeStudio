package com.dr10.settings.ui

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
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.ErrorMessage
import com.dr10.common.utilities.SettingsErrorState
import com.dr10.settings.ui.screens.AutocompleteSettingsScreen
import com.dr10.settings.ui.screens.SyntaxHighlightSettingsScreen
import com.dr10.settings.ui.viewModels.AutocompleteSettingsViewModel
import com.dr10.settings.ui.viewModels.SettingsViewModel
import com.dr10.settings.ui.viewModels.SyntaxHighlightSettingsViewModel
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import kotlinx.coroutines.launch

@Composable
fun Settings(
    onCloseRequest: () -> Unit,
    viewModel: SettingsViewModel,
    syntaxHighlightSettingsViewModel: SyntaxHighlightSettingsViewModel,
    autocompleteSettingsViewModel: AutocompleteSettingsViewModel
) {

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
                Screens.SYNTAX_KEYWORD_HIGHLIGHTER_SETTINGS -> SyntaxHighlightSettingsScreen(modifier, errorState, syntaxHighlightSettingsViewModel)
                Screens.AUTOCOMPLETE_SETTINGS -> AutocompleteSettingsScreen(modifier, errorState, autocompleteSettingsViewModel)
            }

        }

        // If [errorState.displayErrorMessage] is true, [ErrorMessage] is displayed
        if(errorState.displayErrorMessage.value){
            ErrorMessage(
                errorDescription = errorState.errorDescription.value,
                onCloseRequest = {
                    coroutineScope.launch {
                        // Deletes the configurations
                        viewModel.deleteConfigs(errorState.id.value)
                        errorState.displayErrorMessage.value = false
                    }
                }
            )
        }

    }
}