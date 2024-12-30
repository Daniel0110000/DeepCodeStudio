package com.dr10.editor.ui.tabs.components

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import com.dr10.editor.ui.viewModels.EditorTabViewModel
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * Custom [JPanel] that represents the bottom actions row in the editor tab
 * Contains controls for read-only model and configuration option selection
 *
 * @property viewModel The view model that handles the editor ui state
 * @property state The state wrapper that maintains the editor tab's current state
 */
class BottomActionsRow(
    private val viewModel: EditorTabViewModel,
    private val state: FlowStateHandler.StateWrapper<EditorTabViewModel.EditorTabState>
): JPanel() {

    init { onCreate() }

    private fun onCreate() {
        val bottomActionsRowLayout = GroupLayout(this)
        layout = bottomActionsRowLayout
        background = ThemeApp.colors.secondColor.toAWTColor()

        val analyzingIcon = JLabel(AppIcons.analyzerIcon)
        val analyzingLabel = JLabel().apply {
            font = ThemeApp.text.fontInterRegular(13f)
            foreground = ThemeApp.awtColors.textColor
            setState(state, EditorTabViewModel.EditorTabState::isAnalyzing) { isAnalyzing ->
                text = if(isAnalyzing) "Analyzing..." else "Analyzed"
            }
        }
        val readOnlyButton = ReadOnlyButton(viewModel, state)
        val changeSelectedOption = ChangeSelectedOption(viewModel, state)

        bottomActionsRowLayout.setHorizontalGroup(
            bottomActionsRowLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE.toInt())
                .addComponent(analyzingIcon)
                .addGap(2)
                .addComponent(analyzingLabel)
                .addGap(3)
                .addComponent(changeSelectedOption)
                .addComponent(readOnlyButton, 0, 0, 25)
                .addGap(10)
        )

        bottomActionsRowLayout.setVerticalGroup(
            bottomActionsRowLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(analyzingIcon)
                .addComponent(analyzingLabel)
                .addComponent(changeSelectedOption, 0, 0, 25)
                .addComponent(readOnlyButton, 0, 0, 25)
        )


    }

}