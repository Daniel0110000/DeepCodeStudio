package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import domain.utilies.Constants
import domain.utilies.DocumentsManager
import ui.components.verticalBarOptions
import ui.editor.EditorView
import ui.editor.tabs.TabsState
import ui.splitPane.SplitPane
import ui.splitPane.SplitPaneState
import ui.terminal.TerminalView

@Composable
fun CodeEditorScreen() {
    // Create and remember the state for managing tabs in the editor
    val tabsState = remember { TabsState() }

    var isCollapseSplitPane by remember { mutableStateOf(false) }
    var isOpenTerminal by remember { mutableStateOf(false) }

    // Create and remember the state for managing the split pane
    var splitPaneState by remember {
        mutableStateOf(SplitPaneState(
            "${DocumentsManager.getUserHome()}/${Constants.DEFAULT_PROJECTS_DIRECTORY_NAME}",
            tabsState
        ))
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
    ) {

        verticalBarOptions(
            isCollapseSplitPane,
            newDirectoryPath = { it?.let { splitPaneState = SplitPaneState(it, tabsState) } },
            collapseOrExtendSplitPane = { isCollapseSplitPane = !isCollapseSplitPane },
            onOpenTerminal = { isOpenTerminal = true }
        )

        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if(!isCollapseSplitPane) SplitPane(splitPaneState){ isCollapseSplitPane = true }

                EditorView(tabsState)
            }

            if(isOpenTerminal){
                TerminalView{ isOpenTerminal = false }
            }
        }
    }
}