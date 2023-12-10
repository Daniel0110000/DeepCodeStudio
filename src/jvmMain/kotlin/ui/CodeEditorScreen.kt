package ui

import App
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ui.components.verticalBarOptions
import ui.editor.EditorView
import ui.splitPane.SplitPane
import ui.terminal.TerminalView
import ui.viewModels.splitPane.FileTreeViewModel

@Composable
fun CodeEditorScreen() {

    // Inject [SettingRepository] and [SplitPaneViewModel]
    val repository = App().settingRepository
    val splitPaneViewModel = App().splitPaneViewModel

    // Value observer for [currentPath]
    val currentPath = splitPaneViewModel.currentPath.observeAsState().value
    // Create [FileTreeViewModel]
    val fileTreeViewModel = remember { mutableStateOf(FileTreeViewModel(repository, currentPath, splitPaneViewModel.tabState.value)) }

    // Value observers for [isCollapseSplitPane] and [isOpenTerminal]
    val isCollapseSplitPane = splitPaneViewModel.isCollapseSplitPane.observeAsState().value
    val isOpenTerminal = splitPaneViewModel.isOpenTerminal.observeAsState().value

    // Creates and assign a new instance of FileTreeViewModel when the currentPath changes
    LaunchedEffect(currentPath){
        fileTreeViewModel.value = FileTreeViewModel(repository, currentPath, splitPaneViewModel.tabState.value)
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
    ) {

        verticalBarOptions(
            isCollapseSplitPane,
            newDirectoryPath = { it?.let { splitPaneViewModel.setPath(it) } },
            collapseOrExtendSplitPane = { splitPaneViewModel.setIsCollapseSplitPane(!isCollapseSplitPane) },
            onOpenTerminal = { splitPaneViewModel.setIsOpenTerminal(!isOpenTerminal) }
        )

        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if(!isCollapseSplitPane) SplitPane(
                    fileTreeViewModel.value,
                    splitPaneViewModel
                ){ splitPaneViewModel.setIsCollapseSplitPane(true) }

                EditorView(splitPaneViewModel.tabState.value)
            }

            if(isOpenTerminal) TerminalView{ splitPaneViewModel.setIsOpenTerminal(false) }
        }
    }
}