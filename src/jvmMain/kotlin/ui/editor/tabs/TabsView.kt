package ui.editor.tabs

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    // Remember the currently selected tab
    var tabSelected by remember { mutableStateOf("") }

    // Variable that contains the old value of the tab count
    var previousTabCount by remember { mutableStateOf(0) }

    // Variable that contains the index of the closed tab
    var closedTabIndex by remember { mutableStateOf(0) }
    // Variable that contains the file path of the closed tab
    var closedTabFilePath by remember { mutableStateOf("") }


    // Use LaunchedEffect to perform actions when the number of tabs changes
    LaunchedEffect(tabsState.tabs.size){

        if(tabsState.tabs.size > previousTabCount){
            // A new tab was added, update the selected tab and trigger the [onNewTab] callback
            tabSelected = tabsState.tabs.last().filePath
            onNewTab(tabsState.tabs.last().filePath)
        }

        if(tabsState.tabs.size < previousTabCount){
            //  A tab was closed. Update the necessary states and trigger the "onDeleteTab" callback
            closedTabFilePath = tabsState.closedTabFilePath.value
            onDeleteTab(closedTabIndex, closedTabFilePath)

            if(tabsState.tabs.isNotEmpty()){
                // If there are remaining tabs, set the selected tab to the last one
                tabSelected = tabsState.tabs.last().filePath
            }
        }

        // Update the previous tab count for future comparisons
        previousTabCount = tabsState.tabs.size

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
                    tabSelected,
                    onClickListenerTabClose = {
                        tabsState.closeTab(tabsModel) { index ->
                            closedTabIndex = index
                            closedTabFilePath = tabsState.closedTabFilePath.value
                        }
                    },
                    onClickListenerTabSelected = {
                        tabSelected = it
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