package ui.settings.screens.autocomplete

import App
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import domain.model.AutocompleteOptionModel
import domain.model.SyntaxHighlightConfigModel
import domain.utilies.DocumentsManager
import kotlinx.coroutines.launch
import ui.ThemeApp
import ui.components.EmptyMessage
import ui.settings.SettingsErrorState
import ui.settings.lazy.AutocompleteOptionItem
import ui.settings.lazy.NewAutocompleteOptionInput
import ui.viewModels.settings.AutocompleteSettingsViewModel
import ui.viewModels.settings.SyntaxHighlightSettingsViewModel

@Composable
fun AutocompleteSettingsScreen(
    modifier: Modifier,
    settingsErrorState: SettingsErrorState
) {

    // Scroll state for the LazyColumn
    val scrollState = rememberLazyListState()

    // Coroutine scope for asynchronous operations
    val scope = rememberCoroutineScope()

    // Inject [AutocompleteSettingsViewModel] and [AutocompleteSettingsViewModel]
    val autocompleteSettingsViewModel: AutocompleteSettingsViewModel = App().autocompleteSettingsViewModel
    val syntaxHighlightViewModel: SyntaxHighlightSettingsViewModel = App().syntaxHighlightSettingsViewModel

    // Observe the list of all autocomplete options through the ViewModel
    val allOptions = autocompleteSettingsViewModel.allAutocompleteOptions.observeAsState().value
    // Observe the option name through the viewModel
    val optionName = autocompleteSettingsViewModel.optionName.observeAsState().value
    // Observe the JSON path through viewModel
    val jsonPath = autocompleteSettingsViewModel.jsonPath.observeAsState().value
    // Observe the selected option through viewModel
    val selectedOption = autocompleteSettingsViewModel.selectedOption.observeAsState().value

    Row(modifier) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(10.dp)
        ){
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ){
                // If [allOptions] is not empty, the options are displayed
                if(allOptions.isNotEmpty()){
                    LazyColumn(
                        state = scrollState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(allOptions){
                            // If the JSON file exists, [AutocompleteOptionItem] is displayed
                            if(DocumentsManager.existsFile(it.jsonPath)){
                                AutocompleteOptionItem(it, selectedOption.uuid){
                                    autocompleteSettingsViewModel.setSelectedOption(it)
                                }
                            } else {
                                // If the JSON file does not exist, update the data in [EditorErrorState] to handle this error
                                settingsErrorState.uuid.value = it.uuid
                                settingsErrorState.displayErrorMessage.value = true
                                settingsErrorState.errorDescription.value = "JSON file not found at the specified path '${it.jsonPath}'"
                            }
                        }
                    }
                } else EmptyMessage() // If [allOptions] is empty, [EmptyMessage] is displayed

                VerticalScrollbar(
                    ScrollbarAdapter(scrollState),
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterEnd),
                    style = ThemeApp.scrollbar.scrollbarStyle
                )

            }

            NewAutocompleteOptionInput(
                onAddOptionClick = {
                    scope.launch {
                        if(optionName.isNotBlank() && jsonPath.isNotBlank()){
                            autocompleteSettingsViewModel.addConfig(
                                AutocompleteOptionModel(optionName = optionName, jsonPath = jsonPath),
                                SyntaxHighlightConfigModel(optionName = optionName, jsonPath = jsonPath)
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
                optionName = optionName,
                jsonPath = jsonPath
            )
        }

        if(allOptions.isNotEmpty()){
            JsonAutocompleteOptionContainer(
                selectedOption,
                autocompleteSettingsViewModel,
                onDeleteOptionClick = {
                    scope.launch {
                        autocompleteSettingsViewModel.deleteConfig(selectedOption.uuid)
                        // Update the Syntax Highlight configurations
                        syntaxHighlightViewModel.updateSyntaxHighlightConfigs()
                        // Update the autocomplete options
                        autocompleteSettingsViewModel.updateAutocompleteOptions()

                        autocompleteSettingsViewModel.setSelectedOption(allOptions.first())
                    }
                },
                onUpdateJsonPathClick = { newJsonPath ->
                    scope.launch {
                        if(newJsonPath.isNotBlank()){
                            // When the callback is executed, and [newJsonPath] is not black, update the autocomplete JSON path,
                            // ... the syntax highlight JSON path and the selected autocompleted option json path
                            autocompleteSettingsViewModel.updateJsonPath(
                                newJsonPath,
                                selectedOption
                            )

                            // Update the Syntax Highlight configurations
                            syntaxHighlightViewModel.updateSyntaxHighlightConfigs()
                            // Update the autocomplete options
                            autocompleteSettingsViewModel.updateAutocompleteOptions()

                            autocompleteSettingsViewModel.setSelectedOption(selectedOption.copy(jsonPath = newJsonPath))
                        }
                    }
                }
            )
        }
    }
}