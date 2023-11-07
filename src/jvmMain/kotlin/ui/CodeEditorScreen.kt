package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import domain.util.Constants
import domain.util.DocumentsManager
import ui.components.verticalBarOptions
import ui.editor.EditorState
import ui.editor.EditorView
import ui.editor.tabs.TabsState
import ui.splitPane.SplitPane
import ui.splitPane.SplitPaneState

@Composable
fun CodeEditorScreen() {
    // Create and remember the state for managing tabs in the editor
    val tabsState = remember { TabsState() }
    // Create and remember the state for managing teh editor content
    val editorState = remember { EditorState() }

    var isCollapseSplitPane by remember { mutableStateOf(false) }

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
            collapseOrExtendSplitPane = { isCollapseSplitPane = !isCollapseSplitPane }
        )

        if(!isCollapseSplitPane) SplitPane(splitPaneState){ isCollapseSplitPane = true }

        EditorView(tabsState, editorState)
    }
}