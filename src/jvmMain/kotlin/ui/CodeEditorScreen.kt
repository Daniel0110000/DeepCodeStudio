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
import domain.repository.SettingRepository
import ui.components.verticalBarOptions
import ui.editor.EditorView
import ui.settings.Settings
import ui.splitPane.SplitPane
import ui.terminal.TerminalView
import ui.viewModels.CodeEditorViewModel
import ui.viewModels.splitPane.FileTreeViewModel
import ui.viewModels.splitPane.SplitPaneViewModel

@Composable
fun CodeEditorScreen() {

    // Inject [CodeEditorViewModel], [SettingRepository] and [SplitPaneViewModel]
    val codeEditorViewModel: CodeEditorViewModel = App().codeEditorViewModel
    val repository: SettingRepository = App().settingRepository
    val splitPaneViewModel: SplitPaneViewModel = App().splitPaneViewModel

    // Value observer for [currentPath]
    val currentPath = splitPaneViewModel.currentPath.observeAsState().value
    // Create [FileTreeViewModel]
    val fileTreeViewModel = remember { mutableStateOf(FileTreeViewModel(repository, currentPath, codeEditorViewModel.tabState.value)) }

    // Value observers for [isOpenSettings], [isCollapseSplitPane] and [isOpenTerminal]
    val isCollapseSplitPane = splitPaneViewModel.isCollapseSplitPane.observeAsState().value
    val isOpenTerminal = codeEditorViewModel.isOpenTerminal.observeAsState().value
    val isOpenSettings = codeEditorViewModel.isOpenSettings.observeAsState().value

    // Creates and assign a new instance of FileTreeViewModel when the currentPath changes
    LaunchedEffect(currentPath){
        fileTreeViewModel.value = FileTreeViewModel(repository, currentPath, codeEditorViewModel.tabState.value)
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
    ) {

        verticalBarOptions(
            isCollapseSplitPane,
            isOpenTerminal,
            isOpenSettings,
            newDirectoryPath = { it?.let { splitPaneViewModel.setPath(it) } },
            collapseOrExtendSplitPane = { splitPaneViewModel.setIsCollapseSplitPane(!isCollapseSplitPane) },
            openTerminal = { codeEditorViewModel.setIsOpenTerminal(!isOpenTerminal) },
            openSettings = { codeEditorViewModel.setIsOpenSettings(true) }
        )

        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if(!isCollapseSplitPane) SplitPane(
                    fileTreeViewModel.value,
                    splitPaneViewModel
                ){ splitPaneViewModel.setIsCollapseSplitPane(true) }

                EditorView(codeEditorViewModel.tabState.value)
            }

            if(isOpenTerminal) TerminalView{ codeEditorViewModel.setIsOpenTerminal(false) }
        }
    }

    if(isOpenSettings) Settings{ codeEditorViewModel.setIsOpenSettings(false) }

}