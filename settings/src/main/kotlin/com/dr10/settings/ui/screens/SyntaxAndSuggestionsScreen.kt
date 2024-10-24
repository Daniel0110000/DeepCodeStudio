package com.dr10.settings.ui.screens

import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.EmptyMessage
import com.dr10.common.utilities.DocumentsManager
import com.dr10.common.utilities.SettingsErrorState
import com.dr10.settings.di.Inject
import com.dr10.settings.ui.components.JsonAutocompleteOptionContainer
import com.dr10.settings.ui.lazy.AutocompleteOptionItem
import com.dr10.settings.ui.lazy.NewAutocompleteOptionInput

@Composable
fun SyntaxAndSuggestionsScreen(
    modifier: Modifier,
    settingsErrorState: SettingsErrorState,
) {

    // Inject the [SyntaxAndSuggestionsViewModel]
    val viewModel = Inject().syntaxAndSuggestionsViewModel

    // Collect the state from the viewModel
    val state = viewModel.state.collectAsState().value

    // Scroll state for the LazyColumn
    val scrollState = rememberLazyListState()

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
                if(state.allConfigs.isNotEmpty()){
                    LazyColumn(
                        state = scrollState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.allConfigs){
                            // If the JSON file exists, [AutocompleteOptionItem] is displayed
                            if(DocumentsManager.existsFile(it.jsonPath)){
                                AutocompleteOptionItem(it, state.selectedOption.uniqueId){ viewModel.setSelectedOption(it) }
                            } else {
                                // If the JSON file does not exist, update the data in [EditorErrorState] to handle this error
                                settingsErrorState.id.value = it.uniqueId
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
                onAddOptionClick = { viewModel.addConfig() },
                onOptionNameChange = { viewModel.setOptionName(it) },
                onJsonPathSelection = { viewModel.setJsonPath(it) },
                optionName = state.optionName,
                jsonPath = state.jsonPath,
                isLoading = state.isLoading
            )
        }

        if(state.allConfigs.isNotEmpty()){
            JsonAutocompleteOptionContainer(
                model = state.selectedOption,
                viewModel = viewModel,
                width = state.jsonAutocompleteOptionContainerWidth
            )
        }
    }
}