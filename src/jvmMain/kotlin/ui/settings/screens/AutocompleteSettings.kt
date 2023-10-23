package ui.settings.screens

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
import domain.model.AutocompleteOptionModel
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
    // State variables for input fields and options list
    var optionName by remember { mutableStateOf("") }
    var jsonPath by remember { mutableStateOf("") }
    var options by remember { mutableStateOf<List<AutocompleteOptionModel>>(emptyList()) }

    // Scroll state for the LazyColumn
    val scrollState = rememberLazyListState()

    // Coroutine scope for asynchronous operations
    val scope = CoroutineScope(Dispatchers.IO)

    // Inject the repository using Koin
    val repository: SettingRepository by KoinJavaComponent.inject(SettingRepository::class.java)

    // Fetch options from the repository
    LaunchedEffect(Unit){ options = repository.getAllAutocompleteOptions() }

    Box(modifier = Modifier.fillMaxSize()){

        LazyColumn(
            modifier.padding(10.dp),
            state = scrollState
        ) {
            items(options){
                AutocompleteOptionItem(
                    it.optionName,
                    it.jsonPath,
                    onDeleteOptionClick = {
                        scope.launch {
                            repository.deleteAutocompleteOption(it)
                            options = repository.getAllAutocompleteOptions()
                        }
                    },
                    onUpdateJsonPathClick = { newJsonPath ->
                        scope.launch {
                            if(newJsonPath.isNotBlank()){
                                repository.updateAutocompleteOptionJsonPath(newJsonPath, it)
                                options = repository.getAllAutocompleteOptions()
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
                            if(optionName.isNotBlank() && jsonPath.isNotBlank()){
                                repository.addAutocompleteOption(AutocompleteOptionModel(optionName = optionName, jsonPath = jsonPath))
                                options = repository.getAllAutocompleteOptions()

                                optionName = ""
                                jsonPath = ""
                            }
                        }
                    },
                    onOptionNameChange = { optionName = it },
                    onJsonPathSelection = { jsonPath = it },
                    optionName = optionName,
                    jsonPath = jsonPath
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