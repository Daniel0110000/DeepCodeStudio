package ui.splitPane

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ui.editor.tabs.TabsState
import ui.fileTree.FileTree
import util.toProjectFile
import java.io.File

class SplitPaneState(directoryPath: String, state: TabsState) {
    var widthSplittable by mutableStateOf(300f)
    val fileTreeState = FileTree(File(directoryPath).toProjectFile(), state)
}