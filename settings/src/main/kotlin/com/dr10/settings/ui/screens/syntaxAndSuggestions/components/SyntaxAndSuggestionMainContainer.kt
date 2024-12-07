package com.dr10.settings.ui.screens.syntaxAndSuggestions.components

import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomScrollBar
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import com.dr10.settings.ui.components.IconButton
import com.dr10.settings.ui.components.TextField
import com.dr10.settings.ui.viewModels.SyntaxAndSuggestionsViewModel
import java.awt.BorderLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.SwingConstants
import javax.swing.border.EmptyBorder

/**
 * [JPanel] that contains the main container for the syntax and suggestions panel
 *
 * @param syntaxAndSuggestionsViewModel The view model that handles the syntax and suggestions state
 * @param state The state wrapper that handles the state of the syntax and suggestions
 */
class SyntaxAndSuggestionMainContainer(
    private val syntaxAndSuggestionsViewModel: SyntaxAndSuggestionsViewModel,
    private val state: FlowStateHandler.StateWrapper<SyntaxAndSuggestionsViewModel.SyntaxAndSuggestionsState>
): JPanel() {

    init { onCreate() }

    private fun onCreate() {
        val syntaxAndSuggestionMainContainerLayout = GroupLayout(this)
        layout = syntaxAndSuggestionMainContainerLayout
        background = ThemeApp.awtColors.primaryColor

        val emptyOptions = JLabel("No options available", SwingConstants.CENTER).apply {
            font = ThemeApp.text.fontInterRegular(13f)
            foreground = ThemeApp.awtColors.textColor
        }
        val configOptions: JList<SyntaxAndSuggestionModel> = JList<SyntaxAndSuggestionModel>().apply {
            setCellRenderer(SyntaxAndSuggestionCellRender())
            background = ThemeApp.awtColors.primaryColor
            setState(state, SyntaxAndSuggestionsViewModel.SyntaxAndSuggestionsState::defaultIndexSelected) { index ->
                selectedIndex = index
            }
        }

        configOptions.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                syntaxAndSuggestionsViewModel.setSelectedOption(configOptions.selectedValue)
            }
        })

        val scrollPanel = JScrollPane(configOptions).apply {
            border = EmptyBorder(8, 5, 8, 5)
            verticalScrollBar.setUI(CustomScrollBar())
            horizontalScrollBar.setUI(CustomScrollBar())
        }
        val optionsContainer = JPanel().apply {
            layout = BorderLayout()
            background = ThemeApp.awtColors.primaryColor
            setState(state, SyntaxAndSuggestionsViewModel.SyntaxAndSuggestionsState::allConfigs){ configs ->
                if (configs.isNotEmpty()) {
                    configOptions.setListData(configs.toTypedArray())
                    configOptions.selectedIndex = if (configOptions.selectedIndex == -1) 0 else configOptions.selectedIndex
                    remove(emptyOptions)
                    add(scrollPanel, BorderLayout.CENTER)
                } else {
                    remove(scrollPanel)
                    add(emptyOptions, BorderLayout.CENTER)
                }
            }
            add(scrollPanel, BorderLayout.CENTER)
        }


        val optionNameTitle = JLabel("Option Name").apply {
            font = ThemeApp.text.fontInterRegular(13f)
            foreground = ThemeApp.awtColors.textColor
        }
        val optionNameTextField = TextField().apply {
            setState(state, SyntaxAndSuggestionsViewModel.SyntaxAndSuggestionsState::optionName) { name ->
                if (name.isNotBlank()) setText(name)
            }
        }

        val locationTitle = JLabel("Location").apply {
            font = ThemeApp.text.fontInterRegular(13f)
            foreground = ThemeApp.awtColors.textColor
        }
        val locationContainer = LocationContainer(syntaxAndSuggestionsViewModel, state)
        val addButton = IconButton(AppIcons.addIcon) {
            syntaxAndSuggestionsViewModel.setOptionName(optionNameTextField.getText())
            syntaxAndSuggestionsViewModel.addConfig()
        }

        syntaxAndSuggestionMainContainerLayout.setHorizontalGroup(
            syntaxAndSuggestionMainContainerLayout.createParallelGroup()
                .addComponent(optionsContainer, 0, 0, Short.MAX_VALUE.toInt())
                .addGroup(
                    syntaxAndSuggestionMainContainerLayout.createSequentialGroup()
                        .addGap(8)
                        .addComponent(optionNameTitle)
                        .addGap(8)
                        .addComponent(optionNameTextField, 0, 0, 300)
                )
                .addGroup(
                    syntaxAndSuggestionMainContainerLayout.createSequentialGroup()
                        .addGap(8)
                        .addComponent(locationTitle)
                        .addGap(8)
                        .addComponent(locationContainer, 0, 0, Short.MAX_VALUE.toInt())
                        .addGap(8)
                        .addComponent(addButton, 0, 0, 35)
                        .addGap(8)
                )
        )

        syntaxAndSuggestionMainContainerLayout.setVerticalGroup(
            syntaxAndSuggestionMainContainerLayout.createSequentialGroup()
                .addComponent(optionsContainer, 0, 0, Short.MAX_VALUE.toInt())
                .addGroup(
                    syntaxAndSuggestionMainContainerLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(optionNameTitle)
                        .addComponent(optionNameTextField, 0, 0, 35)
                )
                .addGap(10)
                .addGroup(
                    syntaxAndSuggestionMainContainerLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(locationTitle)
                        .addComponent(locationContainer, 0, 0, 35)
                        .addComponent(addButton, 0, 0, 35)
                )
                .addGap(10)
        )


    }

}