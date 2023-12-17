package ui

import App
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ui.components.VerticalBarOptions
import ui.editor.EditorView
import ui.settings.Settings
import ui.splitPane.SplitPane
import ui.terminal.TerminalView
import ui.viewModels.CodeEditorViewModel
import ui.viewModels.splitPane.SplitPaneViewModel

@Composable
fun CodeEditorScreen() {

    // Inject [CodeEditorViewModel] and [SplitPaneViewModel]
    val codeEditorViewModel: CodeEditorViewModel = App().codeEditorViewModel
    val splitPaneViewModel: SplitPaneViewModel = App().splitPaneViewModel

    // Value observers for [isOpenSettings], [isCollapseSplitPane] and [isOpenTerminal]
    val isCollapseSplitPane = codeEditorViewModel.isCollapseSplitPane.observeAsState().value
    val isOpenTerminal = codeEditorViewModel.isOpenTerminal.observeAsState().value
    val isOpenSettings = codeEditorViewModel.isOpenSettings.observeAsState().value

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
    ) {

        VerticalBarOptions(
            isCollapseSplitPane,
            isOpenTerminal,
            isOpenSettings,
            newDirectoryPath = { it?.let { splitPaneViewModel.setPath(it) } },
            collapseOrExtendSplitPane = { codeEditorViewModel.setIsCollapseSplitPane(!isCollapseSplitPane) },
            openTerminal = { codeEditorViewModel.setIsOpenTerminal(!isOpenTerminal) },
            openSettings = { codeEditorViewModel.setIsOpenSettings(true) }
        )

        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if(!isCollapseSplitPane) SplitPane(codeEditorViewModel.tabState.value){
                    codeEditorViewModel.setIsCollapseSplitPane(true)
                }

                EditorView(codeEditorViewModel.tabState.value)
            }

            if(isOpenTerminal) TerminalView{ codeEditorViewModel.setIsOpenTerminal(false) }
        }
    }

    if(isOpenSettings) Settings{ codeEditorViewModel.setIsOpenSettings(false) }

}