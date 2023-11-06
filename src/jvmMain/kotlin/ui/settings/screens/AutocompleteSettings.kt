package ui.settings.screens

import App
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import domain.model.AutocompleteOptionModel
import domain.model.SyntaxHighlightConfigModel
import domain.repository.SettingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import ui.ThemeApp
import ui.settings.lazy.AutocompleteOptionItem
import ui.settings.lazy.NewAutocompleteOptionInput

/**
 * Composable function for displaying and managing autocomplete settings
 *
 * @param modifier The modifier for layout customization
 */
@Composable
fun AutocompleteSettings(modifier: Modifier) {

    // Scroll state for the LazyColumn
    val scrollState = rememberLazyListState()

    // Coroutine scope for asynchronous operations
    val scope = CoroutineScope(Dispatchers.IO)

    // Inject [AutocompleteSettingsViewModel] and [AutocompleteSettingsViewModel]
    val autocompleteSettingsViewModel = App().autocompleteSettingsViewModel
    val syntaxHighlightViewModel = App().syntaxHighlightSettingsViewModel

    // Observe the list of all autocomplete options through the ViewModel
    val allOptions = autocompleteSettingsViewModel.allAutocompleteOptions.observeAsState()
    // Observe the option name through the viewModel
    val optionName = autocompleteSettingsViewModel.optionName.observeAsState()
    // Observe the JSON path through viewModel
    val jsonPath = autocompleteSettingsViewModel.jsonPath.observeAsState()

    Box(modifier = Modifier.fillMaxSize()){

        LazyColumn(
            modifier.padding(10.dp),
            state = scrollState
        ) {
            items(allOptions.value){
                AutocompleteOptionItem(
                    it.optionName,
                    it.jsonPath,
                    onDeleteOptionClick = {
                        scope.launch {
                            // Delete the selected Autocomplete Option and its associated Syntax Highlight Configuration
                            autocompleteSettingsViewModel.deleteAutocompleteOptionAndroidSyntaxHighlightConfig(it)

                            // Update the Syntax Highlight configurations
                            syntaxHighlightViewModel.updateSyntaxHighlightConfigs()
                            // Update the autocomplete options
                            autocompleteSettingsViewModel.updateAutocompleteOptions()
                        }
                    },
                    onUpdateJsonPathClick = { newJsonPath ->
                        scope.launch {
                            if(newJsonPath.isNotBlank()){
                                // When the callback is executed, and [newJsonPath] is not black, update the autocomplete JSON path
                                // ... and the syntax highlight JSON path
                                autocompleteSettingsViewModel.updateAutocompleteAndSyntaxHighlightJsonPath(
                                    newJsonPath,
                                    it
                                )

                                // Update the Syntax Highlight configurations
                                syntaxHighlightViewModel.updateSyntaxHighlightConfigs()
                                // Update the autocomplete options
                                autocompleteSettingsViewModel.updateAutocompleteOptions()
                            }
                        }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                NewAutocompleteOptionInput(
                    onAddOptionClick = {
                        scope.launch {
                            if(optionName.value.isNotBlank() && jsonPath.value.isNotBlank()){
                                // Add a new Autocomplete Option and its corresponding Syntax Highlight Configuration
                                autocompleteSettingsViewModel.addAutocompleteOptionAndSyntaxHighlightConfig(
                                    AutocompleteOptionModel(optionName = optionName.value, jsonPath = jsonPath.value),
                                    SyntaxHighlightConfigModel(optionName = optionName.value, jsonPath = jsonPath.value)
                                )

                                // Update the Syntax Highlight configurations
                                syntaxHighlightViewModel.updateSyntaxHighlightConfigs()
                                // Update the autocomplete options
                                autocompleteSettingsViewModel.updateAutocompleteOptions()

                                // Clear the input fields for option name and JSON path
                                autocompleteSettingsViewModel.updateOptionName("")
                                autocompleteSettingsViewModel.updateJsonPath("")
                            }
                        }
                    },
                    onOptionNameChange = { autocompleteSettingsViewModel.updateOptionName(it) },
                    onJsonPathSelection = { autocompleteSettingsViewModel.updateJsonPath(it) },
                    optionName = optionName.value,
                    jsonPath = jsonPath.value
                )
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