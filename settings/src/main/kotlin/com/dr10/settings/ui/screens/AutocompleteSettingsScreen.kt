package com.dr10.settings.ui.screens

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
import com.dr10.common.models.AutocompleteOptionModel
import com.dr10.common.models.SyntaxHighlightOptionModel
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.EmptyMessage
import com.dr10.common.utilities.DocumentsManager
import com.dr10.common.utilities.SettingsErrorState
import com.dr10.settings.ui.components.JsonAutocompleteOptionContainer
import com.dr10.settings.ui.lazy.AutocompleteOptionItem
import com.dr10.settings.ui.lazy.NewAutocompleteOptionInput
import com.dr10.settings.ui.viewModels.AutocompleteSettingsViewModel
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import kotlinx.coroutines.launch

@Composable
fun AutocompleteSettingsScreen(
    modifier: Modifier,
    settingsErrorState: SettingsErrorState,
    autocompleteSettingsViewModel: AutocompleteSettingsViewModel
) {

    // Scroll state for the LazyColumn
    val scrollState = rememberLazyListState()

    // Coroutine scope for asynchronous operations
    val scope = rememberCoroutineScope()

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
                                settingsErrorState.id.value = it.uuid
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
                                SyntaxHighlightOptionModel(optionName = optionName, jsonPath = jsonPath)
                            )

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

                            autocompleteSettingsViewModel.setSelectedOption(selectedOption.copy(jsonPath = newJsonPath))
                        }
                    }
                }
            )
        }
    }
}