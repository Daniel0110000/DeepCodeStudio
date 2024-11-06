package com.dr10.settings.ui.screens.syntaxAndSuggestions.components

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.JsonChooser
import com.dr10.common.utilities.setState
import com.dr10.settings.ui.viewModels.SyntaxAndSuggestionsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * [JPanel] to display the location of the selected JSON file and a button to select a new JSON file
 *
 * @param syntaxAndSuggestionsViewModel The view model that handles the syntax and suggestions state
 * @param state The state wrapper that handles the state of the syntax and suggestions
 */
class LocationContainer(
    private val syntaxAndSuggestionsViewModel: SyntaxAndSuggestionsViewModel,
    private val state: FlowStateHandler.StateWrapper<SyntaxAndSuggestionsViewModel.SyntaxAndSuggestionsState>
): JPanel() {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    init { onCreate() }

    private fun onCreate() {
        val locationContainerLayout = GroupLayout(this)
        layout = locationContainerLayout
        background = ThemeApp.awtColors.secondaryColor

        val jsonPathLabel = JLabel().apply {
            font = ThemeApp.text.fontInterRegular(12f)
            foreground = ThemeApp.awtColors.textColor
            setState(state, SyntaxAndSuggestionsViewModel.SyntaxAndSuggestionsState::jsonPath) { path -> text = path }
        }
        val selectJsonButton = SelectJsonButton {
            coroutineScope.launch {
                val jsonPath = JsonChooser.chooseJson()
                syntaxAndSuggestionsViewModel.setJsonPath(jsonPath ?: "")
            }
        }

        locationContainerLayout.setHorizontalGroup(
            locationContainerLayout.createSequentialGroup()
                .addGap(8)
                .addComponent(jsonPathLabel)
                .addGap(0, 0, Short.MAX_VALUE.toInt())
                .addComponent(selectJsonButton, 0, 0, 30)
                .addGap(8)
        )

        locationContainerLayout.setVerticalGroup(
            locationContainerLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(jsonPathLabel)
                .addComponent(selectJsonButton, 0, 0, 30)
        )

    }

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = background
        graphics.fillRoundRect(0, 0, width - 1, height - 1, 10, 10)
    }

}