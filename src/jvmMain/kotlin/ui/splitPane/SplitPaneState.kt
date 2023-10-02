package ui.splitPane

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ui.editor.tabs.TabsState
import ui.fileTree.FileTree

class SplitPaneState(directoryPath: String, state: TabsState) {
    // The initial width of the splittable area
    var widthSplittable by mutableStateOf(300f)
    // Create a FileTree state for the directory path and tabs state
    val fileTreeState = FileTree(directoryPath, state)
}