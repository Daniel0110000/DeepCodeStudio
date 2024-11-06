package com.dr10.settings.ui.screens.syntaxAndSuggestions.components

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomScrollBar
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import com.dr10.settings.ui.viewModels.SyntaxAndSuggestionsViewModel
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.Cursor
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.io.StringReader
import javax.swing.BorderFactory
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * [JPanel] to display the JSON preview and the delete config button
 *
 * @param syntaxAndSuggestionsViewModel The view model that handles the syntax and suggestions state
 * @param state The state wrapper that handles the state of the syntax and suggestions
 */
class JsonPreviewContainer(
    private val syntaxAndSuggestionsViewModel: SyntaxAndSuggestionsViewModel,
    private val state: FlowStateHandler.StateWrapper<SyntaxAndSuggestionsViewModel.SyntaxAndSuggestionsState>
): JPanel() {

    init { onCreate() }

    private fun onCreate() {
        val jsonPreviewLayout = GroupLayout(this)
        layout = jsonPreviewLayout
        preferredSize = Dimension(300, Short.MAX_VALUE.toInt())
        background = ThemeApp.awtColors.secondaryColor

        val icon = JLabel(AppIcons.jsonIcon)

        val optionNameLabel = JLabel().apply {
            font = ThemeApp.text.fontInterRegular(13f)
            foreground = ThemeApp.awtColors.textColor
            setState(state, SyntaxAndSuggestionsViewModel.SyntaxAndSuggestionsState::selectedOption){ model ->
                text = model.optionName
            }
        }

        val deleteButton = JLabel(AppIcons.deleteOpIcon).apply {
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            addMouseListener(object : MouseAdapter(){
                override fun mouseClicked(e: MouseEvent?) {
                    syntaxAndSuggestionsViewModel.deleteConfig()
                }
            })
        }


        val jsonPreviewContainer = RSyntaxTextArea().apply {
            isCodeFoldingEnabled = false
            background = ThemeApp.awtColors.secondaryColor
            foreground = ThemeApp.awtColors.textColor
            syntaxScheme.toJsonDefaultSyntaxScheme()
            font = ThemeApp.text.fontInterRegular(13f)
            syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
            selectionColor = ThemeApp.awtColors.complementaryColor
            isEditable = false
            highlightCurrentLine = false
            setState(state, SyntaxAndSuggestionsViewModel.SyntaxAndSuggestionsState::selectedOption){ model ->
                if (model.jsonPath.isNotBlank()) {
                    read(StringReader(File(model.jsonPath).readText()), null)
                }
            }
        }

        val scrollPane = RTextScrollPane(jsonPreviewContainer).apply {
            border = BorderFactory.createLineBorder(ThemeApp.colors.hoverTab.toAWTColor())
            lineNumbersEnabled = false
            verticalScrollBar.setUI(CustomScrollBar(ThemeApp.awtColors.secondaryColor))
            horizontalScrollBar.setUI(CustomScrollBar(ThemeApp.awtColors.secondaryColor))
        }

        jsonPreviewLayout.setHorizontalGroup(
            jsonPreviewLayout.createParallelGroup()
                .addGroup(
                    jsonPreviewLayout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(icon)
                        .addGap(3)
                        .addComponent(optionNameLabel)
                        .addGap(0, 0, Short.MAX_VALUE.toInt())
                        .addComponent(deleteButton)
                        .addGap(5)
                )
                .addComponent(scrollPane, 0, 0, Short.MAX_VALUE.toInt())
        )

        jsonPreviewLayout.setVerticalGroup(
            jsonPreviewLayout.createSequentialGroup()
                .addGap(10)
                .addGroup(
                    jsonPreviewLayout.createParallelGroup()
                        .addComponent(icon)
                        .addComponent(optionNameLabel)
                        .addComponent(deleteButton)

                )
                .addGap(10)
                .addComponent(scrollPane, 0, 0, Short.MAX_VALUE.toInt())

        )

    }

}