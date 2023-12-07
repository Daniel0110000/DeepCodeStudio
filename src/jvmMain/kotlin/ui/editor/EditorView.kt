package ui.editor

import App
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ui.ThemeApp
import ui.editor.navigation.CustomNavigationHost
import ui.editor.navigation.rememberNavController
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
                    onNewTab = { viewModel.onNewTab(it) },
                    onDeleteTab = { path ->
                        viewModel.onDeleteTab(path)
                        navController.navigate(editorStates[editorStates.lastIndex].filePath.value)
                        },
                    onChangeSelectedTab = {
                        index -> viewModel.setSelectedTabIndex(index)
                        navController.navigate(editorStates[index].filePath.value)
                    }
                )

                CustomNavigationHost(
                    navController = navController,
                    states = editorStates,
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )

            }

            if(editorStates.isNotEmpty()) AllAutocompleteOptionView(editorStates[selectedTabIndex], viewModel)

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

}