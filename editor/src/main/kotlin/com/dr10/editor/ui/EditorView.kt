package com.dr10.editor.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.dr10.common.ui.components.ErrorMessage
import com.dr10.common.ui.editor.EditorErrorState
import com.dr10.database.domain.repositories.AutocompleteSettingsRepository
import com.dr10.editor.ui.navigation.SetupNavHost
import com.dr10.editor.ui.navigation.rememberNavController
import com.dr10.editor.ui.tabs.TabModel
import com.dr10.editor.ui.tabs.TabsState
import com.dr10.editor.ui.tabs.TabsView
import com.dr10.editor.ui.viewModels.EditorViewModel
import com.dr10.editor.ui.viewModels.TabsViewModel
import dev.icerock.moko.mvvm.livedata.compose.observeAsState

@Composable
fun EditorView(
    tabsState: TabsState,
    viewModel: EditorViewModel,
    tabsViewModel: TabsViewModel,
    autocompleteSettingsRepository: AutocompleteSettingsRepository
) {

    // Creates an instance of [EditorErrorState]
    val errorState = remember { EditorErrorState() }

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
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (tabs, allAutocompleteOptions, editorNav) = createRefs()

            TabsView(
                tabsState,
                tabsViewModel,
                onNewTab = { filePath, fileName -> viewModel.onNewTab(filePath, fileName, errorState) },
                onDeleteTab = { path ->
                    viewModel.onDeleteTab(path)
                    navController.navigate(editorStates[editorStates.lastIndex].filePath.value)
                },
                onChangeSelectedTab = {
                        index -> viewModel.setSelectedTabIndex(index)
                    navController.navigate(editorStates[index].filePath.value)
                },
                modifier = Modifier.constrainAs(tabs){
                    width = Dimension.fillToConstraints
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            )

            SetupNavHost(
                navController = navController,
                states = editorStates,
                modifier = Modifier.constrainAs(editorNav){
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                    top.linkTo(tabs.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(allAutocompleteOptions.start)
                },
                autocompleteSettingsRepository
            )

            if(editorStates.isNotEmpty()){
                AllAutocompleteOptionView(
                    editorStates[selectedTabIndex],
                    viewModel,
                    errorState,
                    modifier = Modifier.constrainAs(allAutocompleteOptions){
                        height = Dimension.fillToConstraints
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                )
            }

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