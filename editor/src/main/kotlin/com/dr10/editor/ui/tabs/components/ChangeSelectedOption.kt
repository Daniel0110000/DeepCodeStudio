package com.dr10.editor.ui.tabs.components

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.extensions.mouseEventListener
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import com.dr10.editor.ui.viewModels.EditorTabViewModel
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * Custom [JPanel] that displays and manges the currently selected configuration option
 *
 * @property viewModel The view model that handles the editor ui state
 * @property state The state wrapper that maintains the editor tab's current state
 */
class ChangeSelectedOption(
    private val viewModel: EditorTabViewModel,
    private val state: FlowStateHandler.StateWrapper<EditorTabViewModel.EditorTabState>
): JPanel() {

    private var backgroundColor = ThemeApp.colors.secondColor.toAWTColor()

    init { onCreate() }

    private fun onCreate() {
        val changeSelectedOptionLayout = GroupLayout(this)
        layout = changeSelectedOptionLayout

        this.mouseEventListener(
            onEnter = { backgroundColor = ThemeApp.colors.hoverTab.toAWTColor() },
            onExit = { backgroundColor = ThemeApp.colors.secondColor.toAWTColor() },
            onClick = { viewModel.setIsCollapseAutocompleteOptions(!state.value.isCollapseAutocompleteOptions) }
        )

        val optionIcon = JLabel(AppIcons.asmBottomIcon)

        val optionName = JLabel().apply {
            font = ThemeApp.text.fontInterRegular(13f)
            foreground = ThemeApp.awtColors.textColor
            setState(state, EditorTabViewModel.EditorTabState::selectedConfig) { config ->
                text = config?.optionName ?: "..."
            }
        }

        changeSelectedOptionLayout.setHorizontalGroup(
            changeSelectedOptionLayout.createSequentialGroup()
                .addGap(5)
                .addComponent(optionIcon)
                .addGap(3)
                .addComponent(optionName)
                .addGap(5)
        )
        changeSelectedOptionLayout.setVerticalGroup(
            changeSelectedOptionLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE.toInt())
                .addGroup(
                    changeSelectedOptionLayout.createParallelGroup()
                        .addComponent(optionIcon)
                        .addComponent(optionName)
                )
                .addGap(0, 0, Short.MAX_VALUE.toInt())
        )
//        add(
//            optionName,
//            GridBagConstraints().apply {
//                anchor = GridBagConstraints.CENTER
//                insets = Insets(0, 10, 0, 10)
//            }
//        )

    }

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = backgroundColor
        graphics.fillRect(0, 0, width - 1, height - 1)

    }

}