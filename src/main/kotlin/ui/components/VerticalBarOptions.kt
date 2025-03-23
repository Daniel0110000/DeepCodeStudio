package ui.components

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.DirectoryChooser
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.viewModels.CodeEditorViewModel
import java.awt.Color
import java.awt.Dimension
import javax.swing.GroupLayout
import javax.swing.JPanel

/**
 * A [JPanel] that represents a vertical bar options for interacting with the code editor
 *
 * @param codeEditorViewModel The viewModel that manages the state of the code editor
 * @param newDirectoryPath A callback function to handle the selection fo a new directory
 * @param collapseOrExtendSplitPane A callback function to collapse or extend the split pane
 * @param openTerminal A callback function to open the terminal
 */
class VerticalBarOptions(
    private val codeEditorViewModel: CodeEditorViewModel,
    private val codeEditorState: FlowStateHandler.StateWrapper<CodeEditorViewModel.CodeEditorState>,
    private val newDirectoryPath: (String?) -> Unit,
    private val collapseOrExtendSplitPane: () -> Unit,
    private val openTerminal: () -> Unit
): JPanel() {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    init { onCreate() }

    private fun onCreate() {
        val layout = GroupLayout(this)
        this.layout = layout

        preferredSize = Dimension(45, Short.MAX_VALUE.toInt())
        background = ThemeApp.colors.secondColor.toAWTColor()

        val fileTreeOption = VerticalBarOption(
            10, Color(255, 255, 255, 25),
            ThemeApp.colors.secondColor.toAWTColor(), "images/ic_folder.svg",
            onClickListener = { collapseOrExtendSplitPane() },
        ).apply {
            setState(codeEditorState, CodeEditorViewModel.CodeEditorState::isCollapseSplitPane) { isCollapseSplitPane ->
                isSelected = !isCollapseSplitPane
            }
        }

        val folderOption =  VerticalBarOption(
            10, Color(255, 255, 255, 25),
            ThemeApp.colors.secondColor.toAWTColor(), "images/ic_select_directory.svg",
            onClickListener = { coroutineScope.launch { newDirectoryPath(DirectoryChooser.chooseDirectory()) } }
        )

        val terminalOption =  VerticalBarOption(
            10, Color(255, 255, 255, 25),
            ThemeApp.colors.secondColor.toAWTColor(),
            "images/ic_terminal.svg",
            onClickListener = {
                openTerminal()
                codeEditorViewModel.setIsOpenSettings(false)
            }
        ).apply {
            setState(codeEditorState, CodeEditorViewModel.CodeEditorState::isOpenTerminal) { isOpenTerminal ->
                isSelected = isOpenTerminal
            }
        }

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGap(0, Short.MAX_VALUE.toInt(), Short.MAX_VALUE.toInt())
                .addGroup(
                    layout.createParallelGroup()
                        .addComponent(fileTreeOption, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(folderOption, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(terminalOption, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                )
                .addGap(0, Short.MAX_VALUE.toInt(), Short.MAX_VALUE.toInt())
        )

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(10)
                .addComponent(fileTreeOption, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(5)
                .addComponent(folderOption, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, Short.MAX_VALUE.toInt(), Short.MAX_VALUE.toInt())
                .addComponent(terminalOption, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(10)
        )
    }

}