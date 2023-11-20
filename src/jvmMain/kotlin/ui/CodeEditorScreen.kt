package ui

import App
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
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
    val fileTreeViewModel = FileTreeViewModel(repository, currentPath, splitPaneViewModel.tabState.value)

    // Value observers for [isCollapseSplitPane] and [isOpenTerminal]
    val isCollapseSplitPane = splitPaneViewModel.isCollapseSplitPane.observeAsState().value
    val isOpenTerminal = splitPaneViewModel.isOpenTerminal.observeAsState().value

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
                    fileTreeViewModel,
                    splitPaneViewModel
                ){ splitPaneViewModel.setIsCollapseSplitPane(true) }

                EditorView(splitPaneViewModel.tabState.value)
            }

            if(isOpenTerminal){
                TerminalView(
                    directoryPath = currentPath,
                    onCloseTerminal = { splitPaneViewModel.setIsOpenTerminal(false) }
                )
            }
        }
    }
}