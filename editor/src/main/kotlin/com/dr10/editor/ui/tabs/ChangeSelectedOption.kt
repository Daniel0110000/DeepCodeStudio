package com.dr10.editor.ui.tabs

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import com.dr10.editor.ui.viewModels.EditorTabViewModel
import java.awt.*
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
        layout = GridBagLayout()

        this.mouseEventListener(
            onEnter = { backgroundColor = ThemeApp.colors.hoverTab.toAWTColor() },
            onExit = { backgroundColor = ThemeApp.colors.secondColor.toAWTColor() },
            onClick = { viewModel.setIsCollapseAutocompleteOptions(!state.value.isCollapseAutocompleteOptions) }
        )

        val optionName = JLabel().apply {
            setState(state, EditorTabViewModel.EditorTabState::selectedConfig) { config ->
                text = config?.optionName ?: "..."
            }
        }

        add(
            optionName,
            GridBagConstraints().apply {
                anchor = GridBagConstraints.CENTER
                insets = Insets(0, 10, 0, 10)
            }
        )

    }

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = backgroundColor
        graphics.fillRect(0, 0, width - 1, height - 1)

    }

}