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
    filePathSelected: (String) -> Unit
) {

    // Remember the scroll state for the horizontal scrollbar
    val scrollState = rememberScrollState()
    // Remember the currently selected tav
    var tabSelected by remember { mutableStateOf("") }

    // Use LaunchedEffect to perform actions when the number of tabs changes
    LaunchedEffect(tabsState.tabs.size){
        if(tabsState.tabs.isNotEmpty()){
            // Select the last opened tab by default
            filePathSelected(tabsState.tabs.last().filePath)
            tabSelected = tabsState.tabs.last().filePath
        }
    }

    Box {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .background(ThemeApp.colors.secondColor)
                .horizontalScroll(scrollState)
        ) {
            tabsState.tabs.forEach { tabsModel ->
                TabItem(
                    tabsModel,
                    tabSelected,
                    onClickListenerTabClose = { tabsState.closeTab(tabsModel) },
                    onClickListenerTabSelected = {
                        tabSelected = it
                        filePathSelected(it)
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