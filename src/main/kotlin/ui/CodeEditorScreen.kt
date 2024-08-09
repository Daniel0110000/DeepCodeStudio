package ui

import App
import com.dr10.common.utilities.UIStateManager
import com.dr10.settings.ui.SettingsWindow
import ui.components.VerticalBarOptions
import ui.viewModels.CodeEditorViewModel
import javax.swing.GroupLayout
import javax.swing.JFrame

/**
 * Main screen for the code editor
 *
 * @param window The [JFrame] instance representing the main application window
 */
class CodeEditorScreen(
    private val window: JFrame
) {

    // ViewModels initialization
    private val codeEditorViewModel: CodeEditorViewModel = App().codeEditorViewModel
    private val syntaxHighlightSettingsViewModel = App().syntaxHighlightSettingsViewModel
    private val autocompleteSettingsViewModel = App().autocompleteSettingsViewModel
    private val settingsViewModel = App().settingsViewModel

    init {
        onCreate()
    }

    private fun onCreate() {
        val windowLayout = GroupLayout(window.contentPane)
        window.contentPane.layout = windowLayout

        val verticalBarOptions = VerticalBarOptions(
            codeEditorViewModel = codeEditorViewModel,
            collapseOrExtendSplitPane = { codeEditorViewModel.setIsCollapseSplitPane(true) },
            newDirectoryPath = { it?.let {
                codeEditorViewModel.setCurrentPath(it)
            } },
            openSettings = { codeEditorViewModel.setIsOpenSettings(true)  },
            openTerminal = { codeEditorViewModel.setIsOpenTerminal(true) }
        )

        windowLayout.setHorizontalGroup(
            windowLayout.createSequentialGroup()
                .addComponent(verticalBarOptions, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        )

        windowLayout.setVerticalGroup(
            windowLayout.createParallelGroup()
                .addComponent(verticalBarOptions)
        )


        // Set up a UI state manager to listen for changes in the code editor's state
        UIStateManager(
            stateFlow = codeEditorViewModel.state,
            onStateChanged = { state: CodeEditorViewModel.CodeEditorState ->
                if(state.isOpenSettings) {
                    // Show the settings window if [state.isOpenSettings] is true
                    SettingsWindow(
                        window = window,
                        settingsViewModel = settingsViewModel,
                        syntaxHighlightSettingsViewModel = syntaxHighlightSettingsViewModel,
                        autocompleteSettingsViewModel = autocompleteSettingsViewModel
                    ) { codeEditorViewModel.setIsOpenSettings(false) }
                }
            }
        )
    }

}