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
import com.dr10.common.ui.ThemeApp
import com.dr10.editor.ui.EditorView
import com.dr10.settings.ui.Settings
import com.dr10.terminal.TerminalView
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ui.components.VerticalBarOptions
import ui.splitPane.SplitPane
import ui.viewModels.CodeEditorViewModel
import ui.viewModels.splitPane.SplitPaneViewModel

@Composable
fun CodeEditorScreen() {

    // ViewModels initialization
    val codeEditorViewModel: CodeEditorViewModel = App().codeEditorViewModel
    val splitPaneViewModel: SplitPaneViewModel = App().splitPaneViewModel
    val syntaxHighlightSettingsViewModel = App().syntaxHighlightSettingsViewModel
    val autocompleteSettingsViewModel = App().autocompleteSettingsViewModel
    val settingsViewModel = App().settingsViewModel
    val editorViewModel = App().editorViewModel
    val tabsViewModel = App().tabsViewModel

    val autocompleteSettingsRepository = App().autocompleteSettingsRepository

    // Value observers for [currentPath], [isOpenSettings], [isCollapseSplitPane] and [isOpenTerminal]
    val currentPath = codeEditorViewModel.currentPath.observeAsState().value
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
            newDirectoryPath = { it?.let {
                codeEditorViewModel.setCurrentPath(it)
                splitPaneViewModel.setPath(it)
            } },
            collapseOrExtendSplitPane = { codeEditorViewModel.setIsCollapseSplitPane(!isCollapseSplitPane) },
            openTerminal = { codeEditorViewModel.setIsOpenTerminal(!isOpenTerminal) },
            openSettings = { codeEditorViewModel.setIsOpenSettings(true) }
        )

        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if(!isCollapseSplitPane) SplitPane(codeEditorViewModel.tabState.value){
                    codeEditorViewModel.setIsCollapseSplitPane(true)
                }

                EditorView(
                    tabsState = codeEditorViewModel.tabState.value,
                    viewModel = editorViewModel,
                    tabsViewModel = tabsViewModel,
                    autocompleteSettingsRepository = autocompleteSettingsRepository
                )
            }

            if(isOpenTerminal) TerminalView(currentPath){ codeEditorViewModel.setIsOpenTerminal(false) }
        }
    }

    if(isOpenSettings) Settings(
        onCloseRequest = { codeEditorViewModel.setIsOpenSettings(false) },
        viewModel = settingsViewModel,
        syntaxHighlightSettingsViewModel = syntaxHighlightSettingsViewModel,
        autocompleteSettingsViewModel = autocompleteSettingsViewModel
    )

}