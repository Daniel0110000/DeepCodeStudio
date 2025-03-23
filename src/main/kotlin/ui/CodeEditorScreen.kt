package ui

import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomSplitPaneDivider
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import com.dr10.editor.ui.EditorPanel
import com.dr10.editor.ui.viewModels.TabsViewModel
import com.dr10.settings.ui.SettingsWindow
import com.dr10.terminal.ui.TerminalView
import com.dr10.terminal.ui.viewModel.TerminalViewModel
import di.Inject
import ui.components.TopActionsBar
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

    private val codeEditorViewModel: CodeEditorViewModel = Inject().codeEditorViewModel
    private val codeEditorState = FlowStateHandler().run {
        codeEditorViewModel.state.collectAsState(CodeEditorViewModel.CodeEditorState())
    }

    private val terminalViewModel: TerminalViewModel = Inject().terminalViewModel
    private val fileTreeViewModel: FileTreeViewModel = Inject().fileTreeViewModel
    private val tabsViewModel: TabsViewModel = Inject().tabsViewModel

    // State variables for split pane behavior
    private var collapseOrExtend: Boolean = true
    private var currentEditorDividerLocation = 300
    private var currentCodeEditorDividerLocation = -1

    init { onCreate() }

    private fun onCreate() {
        val windowLayout = GroupLayout(window.contentPane)
        window.contentPane.layout = windowLayout

        val topActionsBar = TopActionsBar(
            codeEditorState = codeEditorState,
            openSettings = { codeEditorViewModel.setIsOpenSettings(true) }
        )

        val verticalBarOptions = VerticalBarOptions(
            codeEditorViewModel = codeEditorViewModel,
            codeEditorState = codeEditorState,
            collapseOrExtendSplitPane = { codeEditorViewModel.setIsCollapseSplitPane(collapseOrExtend) },
            newDirectoryPath = { it?.let { fileTreeViewModel.setCurrentPath(it) } },
            openTerminal = { codeEditorViewModel.setIsOpenTerminal(!codeEditorViewModel.state.value.isOpenTerminal) }
        )

        val fileTreeView = FileTreeView(window, fileTreeViewModel, tabsViewModel)

        val editorSplitPane = JSplitPane(
            SwingConstants.VERTICAL,
            fileTreeView,
            EditorPanel(tabsViewModel) { codeEditorViewModel.setCurrentPath(it) }
        ).apply {
            setUI(CustomSplitPaneDivider())
            isContinuousLayout = true
            setState(codeEditorState, CodeEditorViewModel.CodeEditorState::isCollapseSplitPane) { isCollapseSplitPane ->
                if (isCollapseSplitPane) {
                    // Collapse the split pane if [state.isCollapseSplitPane] is true
                    currentEditorDividerLocation = dividerLocation
                    setDividerLocation(0.0)
                    dividerSize = 0
                } else {
                    // Restore the split pane if [state.isCollapseSplitPane] is false
                    setDividerLocation(currentEditorDividerLocation)
                    dividerSize = 3
                }
                // Toggle the collapse/extend state
                collapseOrExtend = !isCollapseSplitPane
            }
        }

        val terminalView = TerminalView(terminalViewModel) {
            codeEditorViewModel.setIsOpenTerminal(false)
        }

        val codeEditorSplitPane = JSplitPane(
            SwingConstants.HORIZONTAL,
            editorSplitPane,
            terminalView
        ).apply {
            setUI(CustomSplitPaneDivider(ThemeApp.awtColors.primaryColor))
            isContinuousLayout = true
            leftComponent.minimumSize = Dimension(0, 100)
            rightComponent.minimumSize = Dimension(0, 100)
            setState(codeEditorState, CodeEditorViewModel.CodeEditorState::isOpenTerminal) { isOpenTerminal ->
                if (isOpenTerminal) {
                    terminalViewModel.openInitialTerminal()
                    if (currentCodeEditorDividerLocation == -1) setDividerLocation(0.6)
                    else dividerLocation = currentCodeEditorDividerLocation
                    dividerSize = 3
                } else {
                    currentCodeEditorDividerLocation = dividerLocation
                    dividerLocation = Short.MAX_VALUE.toInt()
                    dividerSize = 0
                }
            }
        }

        windowLayout.setHorizontalGroup(
            windowLayout.createParallelGroup()
                .addComponent(topActionsBar, 0, 0, Short.MAX_VALUE.toInt())
                .addGroup(
                    windowLayout.createSequentialGroup()
                        .addComponent(verticalBarOptions, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(codeEditorSplitPane, 0, 0, Short.MAX_VALUE.toInt())
                )
        )

        windowLayout.setVerticalGroup(
            windowLayout.createSequentialGroup()
                .addComponent(topActionsBar, 40, 40, 40)
                .addGroup(
                    windowLayout.createParallelGroup()
                        .addComponent(verticalBarOptions)
                        .addComponent(codeEditorSplitPane, 0, 0, Short.MAX_VALUE.toInt())
                )
        )

        setState(codeEditorState, CodeEditorViewModel.CodeEditorState::isOpenSettings) { isOpenSettings ->
            if(isOpenSettings) {
                // Show the settings window if [state.isOpenSettings] is true
                SettingsWindow(window = window) { codeEditorViewModel.setIsOpenSettings(false) }
            }
        }
    }

}