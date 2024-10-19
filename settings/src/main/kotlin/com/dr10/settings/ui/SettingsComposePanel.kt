package com.dr10.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.ErrorMessage
import com.dr10.common.utilities.SettingsErrorState
import com.dr10.settings.ui.screens.SyntaxAndSuggestionsScreen
import com.dr10.settings.ui.screens.SyntaxHighlightSettingsScreen
import com.dr10.settings.ui.viewModels.SettingsViewModel
import com.dr10.settings.ui.viewModels.SyntaxHighlightSettingsViewModel
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import kotlinx.coroutines.launch

@Composable
fun SettingsComposePanel(
    viewModel: SettingsViewModel,
    syntaxHighlightSettingsViewModel: SyntaxHighlightSettingsViewModel
) {

    // Create an instance of [SettingsErrorState]
    val errorState = remember { SettingsErrorState() }

    val coroutineScope = rememberCoroutineScope()

    // Value observers
    val screen = viewModel.screen.observeAsState().value

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
            Screens.AUTOCOMPLETE_SETTINGS -> SyntaxAndSuggestionsScreen(modifier, errorState)
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