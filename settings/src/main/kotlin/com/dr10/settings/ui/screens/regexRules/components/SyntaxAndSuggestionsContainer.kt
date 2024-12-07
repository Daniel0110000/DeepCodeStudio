package com.dr10.settings.ui.screens.regexRules.components

import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomScrollBar
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import com.dr10.settings.ui.viewModels.CodeExtractionViewModel
import java.awt.Dimension
import javax.swing.BorderFactory
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.border.EmptyBorder

class SyntaxAndSuggestionsContainer(
    private val state: FlowStateHandler.StateWrapper<CodeExtractionViewModel.CodeExtractionState>
): JPanel() {

    init { onCreate() }

    private fun onCreate() {
        val syntaxAndSuggestionsContainerLayout = GroupLayout(this)
        layout = syntaxAndSuggestionsContainerLayout
        preferredSize = Dimension(300, Short.MAX_VALUE.toInt())
        background = ThemeApp.awtColors.primaryColor
        border = BorderFactory.createLineBorder(ThemeApp.awtColors.hoverColor)

        val title = JLabel("Options").apply {
            font = ThemeApp.text.fontInterBold()
            foreground = ThemeApp.colors.textColor.toAWTColor()
            border = EmptyBorder(10, 0, 10, 0)
        }

        val options: JList<SyntaxAndSuggestionModel> = JList<SyntaxAndSuggestionModel>().apply {
            setCellRenderer(SAndSRegexRulesCellRender())
            background = ThemeApp.awtColors.primaryColor
            setState(state, CodeExtractionViewModel.CodeExtractionState::allConfigs) { configs ->
                setListData(configs.toTypedArray())
            }
        }

        val optionsScrollPanel = JScrollPane(options).apply {
            border = EmptyBorder(0, 5, 8, 5)
            verticalScrollBar.setUI(CustomScrollBar())
            horizontalScrollBar.setUI(CustomScrollBar())
        }

        syntaxAndSuggestionsContainerLayout.setHorizontalGroup(
            syntaxAndSuggestionsContainerLayout.createParallelGroup()
                .addGroup(
                    syntaxAndSuggestionsContainerLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE.toInt())
                        .addComponent(title)
                        .addGap(0, 0, Short.MAX_VALUE.toInt())
                )
                .addComponent(optionsScrollPanel, 0, 0, Short.MAX_VALUE.toInt())
        )

        syntaxAndSuggestionsContainerLayout.setVerticalGroup(
            syntaxAndSuggestionsContainerLayout.createSequentialGroup()
                .addComponent(title)
                .addComponent(optionsScrollPanel, 0, 0, Short.MAX_VALUE.toInt())
        )

    }

}