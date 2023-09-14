package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ui.editor.EditorState
import ui.editor.EditorView
import ui.editor.tabs.TabsState
import ui.splitPane.SplitPane
import ui.splitPane.SplitPaneState
import util.Constants
import util.DocumentsManager

@Composable
fun CodeEditorScreen() {
    // Create and remember the state for managing tabs in the editor
    val tabsState = remember { TabsState() }
    // Create and remember the state for managing teh editor content
    val editorState = remember { EditorState() }

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
        SplitPane(splitPaneState){ it?.let { splitPaneState = SplitPaneState(it, tabsState) } }
        EditorView(tabsState, editorState)
    }
}