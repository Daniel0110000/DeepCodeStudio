package ui.settings.screens

import App
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import domain.model.AutocompleteOptionModel
import domain.model.SyntaxHighlightConfigModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.ThemeApp
import ui.settings.lazy.AutocompleteOptionItem
import ui.settings.lazy.NewAutocompleteOptionInput
import java.io.File

/**
 * Composable function for displaying and managing autocomplete settings
 *
 * @param modifier The modifier for layout customization
 */
@Composable
fun AutocompleteSettings(
    modifier: Modifier,
    onErrorOccurred: (String) -> Unit
) {

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
                // If the JSON file does not exist in the specified path, the [onErrorOccurred] callback is called,
                // ... and the option is removed from the database
                if(!File(it.jsonPath).exists()){
                    CoroutineScope(Dispatchers.IO).launch {
                        onErrorOccurred("JSON file not found at the specified path '${it.jsonPath}'")
                        autocompleteSettingsViewModel.deleteConfig(it.uuid)
                        autocompleteSettingsViewModel.updateAutocompleteOptions()
                    }
                } else {
                    AutocompleteOptionItem(
                        it.optionName,
                        it.jsonPath,
                        onDeleteOptionClick = {
                            scope.launch {
                                autocompleteSettingsViewModel.deleteConfig(it.uuid)

                                // Update the Syntax Highlight configurations
                                syntaxHighlightViewModel.updateSyntaxHighlightConfigs()
                                // Update the autocomplete options
                                autocompleteSettingsViewModel.updateAutocompleteOptions()
                            }
                        },
                        onUpdateJsonPathClick = { newJsonPath ->
                            scope.launch {
                                if(newJsonPath.isNotBlank()){
                                    // When the callback is executed, and [newJsonPath] is not black, update the autocomplete JSON path,
                                    // ... the syntax highlight JSON path and the selected autocompleted option json path
                                    autocompleteSettingsViewModel.updateJsonPath(
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

            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                NewAutocompleteOptionInput(
                    onAddOptionClick = {
                        scope.launch {
                            if(optionName.value.isNotBlank() && jsonPath.value.isNotBlank()){
                                autocompleteSettingsViewModel.addConfig(
                                    AutocompleteOptionModel(optionName = optionName.value, jsonPath = jsonPath.value),
                                    SyntaxHighlightConfigModel(optionName = optionName.value, jsonPath = jsonPath.value)
                                )

                                // Update the Syntax Highlight configurations
                                syntaxHighlightViewModel.updateSyntaxHighlightConfigs()
                                // Update the autocomplete options
                                autocompleteSettingsViewModel.updateAutocompleteOptions()

                                // Clear the input fields for option name and JSON path
                                autocompleteSettingsViewModel.setJsonPath("")
                                autocompleteSettingsViewModel.setJsonPath("")
                            }
                        }
                    },
                    onOptionNameChange = { autocompleteSettingsViewModel.setOptionName(it) },
                    onJsonPathSelection = { autocompleteSettingsViewModel.setJsonPath(it) },
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