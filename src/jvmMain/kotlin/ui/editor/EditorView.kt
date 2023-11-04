package ui.editor

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.SelectedAutocompleteOptionModel
import domain.repository.SettingRepository
import domain.util.JsonUtils
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import ui.ThemeApp
import ui.editor.tabs.TabsState
import ui.editor.tabs.TabsView
import java.io.File

@Composable
fun EditorView(
    tabsState: TabsState,
    editorState: EditorState
) {
    // Inject the repository using Koin
    val repository: SettingRepository by KoinJavaComponent.inject(SettingRepository::class.java)
    // Coroutine scope for handling coroutines within the Composable
    val coroutineScope = rememberCoroutineScope()

    // List of composable functions to create code editor UI
    var editorComposables by remember { mutableStateOf<List<EditorComposable>>(emptyList()) }
    // List of [EditorState] for each tab
    var editorStates by remember { mutableStateOf<List<EditorState>>(emptyList()) }

    // Index of the selected tab
    var selectedTabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(tabsState.tabs.size){
        // Check if there are tabs open. If there are, set the editor's visibility to true
        editorState.isDisplayEditor.value = tabsState.tabs.size != 0
    }

    if(editorState.isDisplayEditor.value){
        Column(modifier = Modifier.fillMaxSize()) {
            // Create tabs
            TabsView(
                tabsState,
                onNewTab = {
                    // When creating a new tab, the following actions are performed ...

                    // Create new [EditorTabComposable] for the new tab
                    editorComposables = editorComposables.plus(EditorTabComposable)
                    // Create a new [EditorState] for the new [EditorTabComposable]
                    editorStates = editorStates.plus(EditorState())
                    // Selected the last [EditorTabComposable] in the list
                    selectedTabIndex = editorComposables.lastIndex
                    // Assign the current file path to the selected tab
                    editorStates[selectedTabIndex].filePath.value = it
                    // Read the content of the current tab's file and assign it to the state of the selected tab
                    editorStates[selectedTabIndex].codeText.value = TextFieldValue(File(it).readText())

                    if(!repository.existsAutocompleteOption(it)){
                        // If no option exists, set the flag to display all autocomplete options
                        editorState.displayAllAutocompleteOptions.value = true
                    } else {
                        // If an option exists, retrieve and set the autocomplete data
                        val option = repository.getSelectedAutocompleteOption(it)
                        editorStates[selectedTabIndex].keywords.value = JsonUtils.jsonToListString(option.jsonPath)
                        editorStates[selectedTabIndex].variableDirectives.value = JsonUtils.extractVariablesAndConstantsKeywordsFromJson(option.jsonPath)
                    }
                },
                onDeleteTab = { i, path ->
                    // When a tab is deleted, both the state and the interface assigned to that tab are removed
                    editorComposables = editorComposables.filterIndexed { index, _ -> index != i  }
                    editorStates = editorStates.filterIndexed { _, value -> value.filePath.value != path }

                    // Select the index of the last [EditorComposable] in the list
                    selectedTabIndex = editorComposables.lastIndex
                },
                onChangeSelectedTab = { index ->
                    // When selecting a new tab, update the [selectedTabIndex] with the new tab's index
                    selectedTabIndex = index
                }
            )

            if(editorComposables.isNotEmpty()) editorComposables[selectedTabIndex](editorStates[selectedTabIndex])
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painterResource("images/ic_app.svg"),
                contentDescription = "App icon",
                modifier = Modifier.size(300.dp)
            )

            Text(
                "DeepCode Studio",
                color = ThemeApp.colors.secondColor,
                fontFamily = ThemeApp.text.fontFamily,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold
            )

        }
    }

    if(editorState.displayAllAutocompleteOptions.value){
        AllAutocompleteOptions(
            selectedOption = {
                // Configure the autocomplete option using the selected choice
                editorStates[selectedTabIndex].keywords.value = JsonUtils.jsonToListString(it.jsonPath)
                editorStates[selectedTabIndex].variableDirectives.value = JsonUtils.extractVariablesAndConstantsKeywordsFromJson(it.jsonPath)

                // Close the window displaying all autocomplete options
                editorState.displayAllAutocompleteOptions.value = false

                coroutineScope.launch {
                    // Add the selected option to the history of selected autocomplete options
                    repository.addSelectedAutocompleteOption(
                        SelectedAutocompleteOptionModel(
                            asmFilePath = editorStates[selectedTabIndex].filePath.value,
                            optionName = it.optionName,
                            jsonPath = it.jsonPath)
                    )
                }
            }
        )
    }

}