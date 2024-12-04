package com.dr10.editor.ui.tabs.components

import com.dr10.common.models.SelectedConfigHistoryModel
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomScrollBar
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import com.dr10.editor.ui.viewModels.EditorTabViewModel
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.border.EmptyBorder

/**
 *  Custom [JPanel] that displays a list of all autocomplete options
 *
 * @property editorTabViewModel The view model that handles the editor ui state
 * @property editorTabState The state wrapper that maintains the editor tab's current state
 */
class AutoCompleteOptions(
    private val editorTabViewModel: EditorTabViewModel,
    private val editorTabState: FlowStateHandler.StateWrapper<EditorTabViewModel.EditorTabState>,
): JPanel() {

    init { onCreate() }

    private fun onCreate() {
        val autoCompleteOptionsLayout = GroupLayout(this)
        layout = autoCompleteOptionsLayout
        preferredSize = Dimension(300, Short.MAX_VALUE.toInt())
        background = ThemeApp.colors.secondColor.toAWTColor()

        val title = JLabel("Choose Option").apply {
            font = ThemeApp.text.fontInterBold()
            foreground = ThemeApp.colors.textColor.toAWTColor()
            border = EmptyBorder(10, 0, 10, 0)
        }

        val options: JList<SelectedConfigHistoryModel> = JList<SelectedConfigHistoryModel>().apply {
            setCellRenderer(OptionsListCellRender())
            background = ThemeApp.colors.secondColor.toAWTColor()
            setState(editorTabState, EditorTabViewModel.EditorTabState::allConfigs) { options ->
                setListData(options.toTypedArray())
            }
        }

        val mouseListener: MouseListener = object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) { editorTabViewModel.insertOrUpdateSelectedConfig(options.selectedValue) }
            }
        }
        options.addMouseListener(mouseListener)

        val scroll = JScrollPane(options).apply {
            border = EmptyBorder(0, 5, 0, 10)
            verticalScrollBar.setUI(CustomScrollBar(ThemeApp.colors.secondColor.toAWTColor()))
            horizontalScrollBar.setUI(CustomScrollBar())
        }

        autoCompleteOptionsLayout.setHorizontalGroup(
            autoCompleteOptionsLayout.createParallelGroup()
                .addComponent(title)
                .addComponent(scroll)
        )

        autoCompleteOptionsLayout.setVerticalGroup(
            autoCompleteOptionsLayout.createSequentialGroup()
                .addComponent(title)
                .addComponent(scroll)
        )
    }

}