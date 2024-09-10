package ui

import App
import com.dr10.common.utilities.UIStateManager
import com.dr10.editor.ui.EditorPanel
import com.dr10.editor.ui.viewModels.TabsViewModel
import com.dr10.settings.ui.SettingsWindow
import ui.components.VerticalBarOptions
import ui.fileTree.FileTreeView
import ui.viewModels.CodeEditorViewModel
import ui.viewModels.FileTreeViewModel
import java.awt.Dimension
import javax.swing.GroupLayout
import javax.swing.JFrame
import javax.swing.JSplitPane
import javax.swing.SwingConstants

/**
 * Main screen for the code editor
 *
 * @property window The [JFrame] instance representing the main application window
 */
class CodeEditorScreen(
    private val window: JFrame
) {

    // ViewModels initialization
    private val codeEditorViewModel: CodeEditorViewModel = App().codeEditorViewModel
    private val fileTreeViewModel: FileTreeViewModel = App().fileTreeViewModel
    private val tabsViewModel: TabsViewModel = App().tabsViewModel
    private val syntaxHighlightSettingsViewModel = App().syntaxHighlightSettingsViewModel
    private val autocompleteSettingsViewModel = App().autocompleteSettingsViewModel
    private val settingsViewModel = App().settingsViewModel

    // State variables for split pane behavior
    private var collapseOrExtend: Boolean = true
    private var dividerLocation = 300

    init {
        onCreate()
    }

    private fun onCreate() {
        val windowLayout = GroupLayout(window.contentPane)
        window.contentPane.layout = windowLayout

        val verticalBarOptions = VerticalBarOptions(
            codeEditorViewModel = codeEditorViewModel,
            collapseOrExtendSplitPane = { codeEditorViewModel.setIsCollapseSplitPane(collapseOrExtend) },
            newDirectoryPath = { it?.let { fileTreeViewModel.setCurrentPath(it) } },
            openSettings = { codeEditorViewModel.setIsOpenSettings(true)  },
            openTerminal = { codeEditorViewModel.setIsOpenTerminal(true) }
        )

        val fileTreeView = FileTreeView(window, fileTreeViewModel, tabsViewModel)

        val splitPane = JSplitPane(
            SwingConstants.VERTICAL,
            fileTreeView,
            EditorPanel(tabsViewModel)
        ).apply {
            isContinuousLayout = true
        }

        windowLayout.setHorizontalGroup(
            windowLayout.createSequentialGroup()
                .addComponent(verticalBarOptions, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(splitPane, 0, 0, Short.MAX_VALUE.toInt())
        )

        windowLayout.setVerticalGroup(
            windowLayout.createParallelGroup()
                .addComponent(verticalBarOptions)
                .addComponent(splitPane, 0, 0, Short.MAX_VALUE.toInt())
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
                if (state.isCollapseSplitPane) {
                    // Collapse the split pane if [state.isCollapseSplitPane] is true
                    dividerLocation = splitPane.dividerLocation
                    splitPane.setDividerLocation(0.0)
                    splitPane.dividerSize = 0
                    splitPane.leftComponent.minimumSize = Dimension()
                } else {
                    // Restore the split pane if [state.isCollapseSplitPane] is false
                    splitPane.setDividerLocation(dividerLocation)
                    splitPane.dividerSize = 5
                    splitPane.leftComponent.minimumSize = Dimension(100,  Short.MAX_VALUE.toInt())
                }
                // Toggle the collapse/extend state
                collapseOrExtend = !state.isCollapseSplitPane
            }
        )
    }

}