package ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ui.components.verticalBarOptions
import ui.editor.EditorState
import ui.editor.EditorView
import ui.editor.tabs.TabsState
import ui.splitPane.SplitPane
import ui.splitPane.SplitPaneState
import domain.util.Constants
import domain.util.DocumentsManager

@Composable
fun CodeEditorScreen() {
    // Create and remember the state for managing tabs in the editor
    val tabsState = remember { TabsState() }
    // Create and remember the state for managing teh editor content
    val editorState = remember { EditorState() }

    var isCollapseSplitPane by remember { mutableStateOf(false) }

    //Create and remember the state for managing the split pane
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