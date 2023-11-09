package ui.editor.tabs

import App
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ui.ThemeApp

@Composable
fun TabsView(
    tabsState: TabsState,
    onNewTab: (String) -> Unit,
    onDeleteTab: (Int, String) -> Unit,
    onChangeSelectedTab: (Int) -> Unit
) {

    // Remember the scroll state for the horizontal scrollbar
    val scrollState = rememberScrollState()

    // Inject [TabsViewModel]
    val viewModel = App().tabsViewModel

    // Value observers
    val tabSelected = viewModel.tabSelected.observeAsState()
    val previousTabCount = viewModel.previousTabCount.observeAsState()
    val closedTabIndex = viewModel.closedTabIndex.observeAsState()
    val closedTabFilePath = viewModel.closedTabFilePath.observeAsState()

    // Use LaunchedEffect to perform actions when the number of tabs changes
    LaunchedEffect(tabsState.tabs.size){

        if(tabsState.tabs.size > previousTabCount.value){
            // A new tab was added, update the selected tab and trigger the [onNewTab] callback
            viewModel.setTabSelected(tabsState.tabs.last().filePath)
            onNewTab(tabsState.tabs.last().filePath)
        }

        if(tabsState.tabs.size < previousTabCount.value){
            // A tab was closed. Update the necessary states and trigger the "onDeleteTab" callback
            viewModel.setClosedTabFilePath(tabsState.closedTabFilePath.value)
            onDeleteTab(closedTabIndex.value, closedTabFilePath.value)

            if(tabsState.tabs.isNotEmpty()){
                // If there are remaining tabs, set the selected tab to the last one
                viewModel.setTabSelected(tabsState.tabs.last().filePath)
            }
        }

        // Update the previous tab count for future comparisons
        viewModel.setPreviousTabCount(tabsState.tabs.size)

    }

    Box {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .background(ThemeApp.colors.secondColor)
                .horizontalScroll(scrollState)
        ) {
            tabsState.tabs.forEachIndexed { index, tabsModel ->
                TabItem(
                    tabsModel,
                    tabSelected.value,
                    onClickListenerTabClose = {
                        tabsState.closeTab(tabsModel) { index ->
                            viewModel.setClosedTabIndex(index)
                            viewModel.setClosedTabFilePath(tabsState.closedTabFilePath.value)
                        }
                    },
                    onClickListenerTabSelected = {
                        viewModel.setTabSelected(it)
                        onChangeSelectedTab(index)
                    }
                )
            }
        }

        HorizontalScrollbar(
            ScrollbarAdapter(scrollState),
            modifier = Modifier.align(Alignment.TopCenter),
            style = ThemeApp.scrollbar.tabsScrollbarStyle
        )
    }
}