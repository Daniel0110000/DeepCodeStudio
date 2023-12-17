package ui.editor

import App
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ui.components.ErrorMessage
import ui.editor.navigation.SetupNavHost
import ui.editor.navigation.rememberNavController
import ui.editor.tabs.TabModel
import ui.editor.tabs.TabsState
import ui.editor.tabs.TabsView
import ui.viewModels.editor.EditorViewModel
import ui.viewModels.editor.TabsViewModel

@Composable
fun EditorView(tabsState: TabsState) {

    // Creates an instance of [EditorErrorState]
    val errorState = remember { EditorErrorState() }

    // Inject [EditorViewModel and [tabsViewModel]
    val viewModel: EditorViewModel = App().editorViewModel
    val tabsViewModel: TabsViewModel = App().tabsViewModel

    // Value observers
    val isDisplayEditor = viewModel.isDisplayEditor.observeAsState()
    val selectedTabIndex = viewModel.selectedTabIndex.observeAsState().value
    val editorStates = viewModel.editorStates.observeAsState().value

    // Creates and remembers a [NavController] instance
    val navController by rememberNavController("")

    LaunchedEffect(tabsState.tabs.size){
        // Assigns all open tabs to the [EditorViewModel]
        viewModel.setTabs(tabsState.tabs)
        // Check if there are tabs open. If there are, set the editor's visibility to true
        viewModel.setDisplayEditor(tabsState.tabs.size != 0)
    }


    // LaunchedEffect block that sets the currently selected tab in response to changes in the selectedTabIndex
    LaunchedEffect(selectedTabIndex){
        // If editorStates is not empty and the [selectedTabIndex] is within bounds
        if(editorStates.isNotEmpty() && selectedTabIndex < editorStates.size) {
            val filePath = editorStates[selectedTabIndex].filePath.value
            tabsViewModel.setTabSelected(filePath)
            navController.navigate(filePath)
        }
    }

    if(isDisplayEditor.value){
        Row(modifier = Modifier.fillMaxSize()) {

            Column(modifier = Modifier.weight(1f).fillMaxHeight().verticalScroll(rememberScrollState())) {
                TabsView(
                    tabsState,
                    onNewTab = { filePath, fileName -> viewModel.onNewTab(filePath, fileName, errorState) },
                    onDeleteTab = { path ->
                        viewModel.onDeleteTab(path)
                        navController.navigate(editorStates[editorStates.lastIndex].filePath.value)
                        },
                    onChangeSelectedTab = {
                        index -> viewModel.setSelectedTabIndex(index)
                        navController.navigate(editorStates[index].filePath.value)
                    }
                )

                SetupNavHost(
                    navController = navController,
                    states = editorStates,
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )

            }

            if(editorStates.isNotEmpty()) AllAutocompleteOptionView(editorStates[selectedTabIndex], viewModel, errorState)

        }
    } else EmptyEditorView()

    // If [errorState.displayErrorMessage] is true, [ErrorMessage] is displayed
    if(errorState.displayErrorMessage.value){
        ErrorMessage(
            errorDescription = errorState.errorDescription.value
        ){
            // Checks if the error state indicated that the tab should be closed
            if(errorState.shouldCloseTab.value){
                // Closes the tab and delete associated configurations
                if(editorStates.isNotEmpty()){
                    tabsState.closeTab(TabModel(editorStates[selectedTabIndex].fileName.value, editorStates[selectedTabIndex].filePath.value)){}
                    viewModel.deleteConfigs(editorStates[selectedTabIndex].uuid.value)
                }
                errorState.shouldCloseTab.value = false
            } else {
                // Deletes configurations associated with the error state's UUID
                viewModel.deleteConfigs(errorState.uuid.value)
            }
            errorState.displayErrorMessage.value = false
        }
    }

}