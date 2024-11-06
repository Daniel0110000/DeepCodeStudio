package com.dr10.settings.ui.screens.syntaxAndSuggestions.components

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import com.dr10.settings.ui.viewModels.SyntaxAndSuggestionsViewModel
import java.awt.BorderLayout
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SwingUtilities
import javax.swing.border.EmptyBorder

/**
 * [JPanel] to display the option name text field
 *
 * @param state The state wrapper that handles the state of the syntax and suggestions
 */
class OptionNameTextField(
    private val state: FlowStateHandler.StateWrapper<SyntaxAndSuggestionsViewModel.SyntaxAndSuggestionsState>
): JPanel() {

    private val textField = JTextField()

    init { onCreate() }

    private fun onCreate() {
        layout = BorderLayout()
        background = ThemeApp.awtColors.secondaryColor
        border = EmptyBorder(2, 5, 2, 5)

        textField.apply {
            border = EmptyBorder(0, 0, 0, 0)
            background = ThemeApp.awtColors.secondaryColor
            caretColor = ThemeApp.awtColors.complementaryColor
            selectionColor = ThemeApp.awtColors.complementaryColor
            font = ThemeApp.text.fontInterRegular(12f)
            setState(state, SyntaxAndSuggestionsViewModel.SyntaxAndSuggestionsState::optionName) { name ->
                if (name.isBlank()) text = name
            }
        }

        add(textField, BorderLayout.CENTER)

        SwingUtilities.invokeLater { textField.requestFocusInWindow() }

    }

    fun getText(): String = textField.text

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = background
        graphics.fillRoundRect(0, 0, width - 1, height - 1, 10, 10)
    }


}