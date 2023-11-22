package ui.editor

import App
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ui.ThemeApp
import ui.editor.tabs.TabsState
import ui.editor.tabs.TabsView
import ui.viewModels.editor.EditorViewModel
import ui.viewModels.editor.TabsViewModel

@Composable
fun EditorView(tabsState: TabsState) {

    // Inject [EditorViewModel and [tabsViewModel]
    val viewModel: EditorViewModel = App().editorViewModel
    val tabsViewModel: TabsViewModel = App().tabsViewModel

    // Value observers
    val isDisplayEditor = viewModel.isDisplayEditor.observeAsState()
    val selectedTabIndex = viewModel.selectedTabIndex.observeAsState().value
    val editorComposables = viewModel.editorComposables.observeAsState().value
    val editorStates = viewModel.editorStates.observeAsState().value
    val displayAllAutocompleteOptions = viewModel.displayAutocompleteOptions.observeAsState().value

    LaunchedEffect(tabsState.tabs.size){
        // Assigns all open tabs to the [EditorViewModel]
        viewModel.setTabs(tabsState.tabs)
        // Check if there are tabs open. If there are, set the editor's visibility to true
        viewModel.setDisplayEditor(tabsState.tabs.size != 0)
    }


    // LaunchedEffect block that sets the currently selected tab in response to changes in the selectedTabIndex
    LaunchedEffect(selectedTabIndex){
        if(editorStates.isNotEmpty()) {
            tabsViewModel.setTabSelected(editorStates[selectedTabIndex].filePath.value)
        }
    }

    if(isDisplayEditor.value){
        Column(modifier = Modifier.fillMaxSize()) {
            TabsView(
                tabsState,
                onNewTab = { viewModel.onNewTab(it) },
                onDeleteTab = { i, path -> viewModel.onDeleteTab(i, path) },
                onChangeSelectedTab = { index -> viewModel.setSelectedTabIndex(index) }
            )

            if(editorComposables.isNotEmpty() && editorStates[selectedTabIndex].syntaxHighlightConfig.value.jsonPath.isNotEmpty()) {
                editorComposables[selectedTabIndex](editorStates[selectedTabIndex])
            }
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

    if(displayAllAutocompleteOptions){
        AllAutocompleteOptions(
            selectedOption = { viewModel.selectedOption(it) }
        )
    }

}