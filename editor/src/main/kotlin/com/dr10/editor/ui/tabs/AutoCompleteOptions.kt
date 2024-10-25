package com.dr10.editor.ui.tabs

import com.dr10.common.models.SyntaxAndSuggestionModel
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
 * @property selectedOption Callback function is called when an option is selected
 */
class AutoCompleteOptions(
    private val editorTabViewModel: EditorTabViewModel,
    private val editorTabState: FlowStateHandler.StateWrapper<EditorTabViewModel.EditorTabState>,
    private val selectedOption: (SyntaxAndSuggestionModel) -> Unit
): JPanel() {

    init { onCreate() }

    private fun onCreate() {
        editorTabViewModel.getAllConfigs()
        val autoCompleteOptionsLayout = GroupLayout(this)
        layout = autoCompleteOptionsLayout
        preferredSize = Dimension(300, Short.MAX_VALUE.toInt())
        background = ThemeApp.colors.secondColor.toAWTColor()

        val title = JLabel("Choose Option").apply {
            font = ThemeApp.text.fontInterBold()
            foreground = ThemeApp.colors.textColor.toAWTColor()
            border = EmptyBorder(10, 0, 10, 0)
        }

        val options: JList<SyntaxAndSuggestionModel> = JList<SyntaxAndSuggestionModel>().apply {
            setCellRenderer(OptionsListCellRender())
            background = ThemeApp.colors.secondColor.toAWTColor()
            setState(editorTabState, EditorTabViewModel.EditorTabState::allConfigs) { options ->
                setListData(options.toTypedArray())
            }
        }

        val mouseListener: MouseListener = object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) { selectedOption(options.selectedValue) }
            }
        }
        options.addMouseListener(mouseListener)

        val scroll = JScrollPane(options).apply {
            border = EmptyBorder(0, 5, 0, 10)
            verticalScrollBar.setUI(CustomScrollBar(ThemeApp.colors.secondColor.toAWTColor()))
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