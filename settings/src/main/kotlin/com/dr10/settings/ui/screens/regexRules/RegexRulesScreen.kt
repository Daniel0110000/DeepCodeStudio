package com.dr10.settings.ui.screens.regexRules

import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomScrollBar
import com.dr10.common.ui.components.CustomSplitPaneDivider
import com.dr10.common.ui.editor.setDefaultSyntaxScheme
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.settings.di.Inject
import com.dr10.settings.ui.screens.colorScheme.AssemblyCodeExamples
import com.dr10.settings.ui.screens.regexRules.components.AddAndOptionsContainer
import com.dr10.settings.ui.screens.regexRules.components.SyntaxAndSuggestionsContainer
import com.dr10.settings.ui.viewModels.CodeExtractionViewModel
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import javax.swing.BorderFactory
import javax.swing.GroupLayout
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import javax.swing.SwingConstants
import javax.swing.border.EmptyBorder

class RegexRulesScreen: JPanel(){

    private val viewModel: CodeExtractionViewModel = Inject().codeExtractionViewModel
    private val codeExtractionState = FlowStateHandler().run {
        viewModel.state.collectAsState(CodeExtractionViewModel.CodeExtractionState())
    }

    init { onCreate() }

    private fun onCreate() {
        val codeExtractionLayout = GroupLayout(this)
        layout = codeExtractionLayout
        background = ThemeApp.awtColors.primaryColor
        border = EmptyBorder(10, 10, 10, 10)

        val syntaxAndSuggestionsContainer = SyntaxAndSuggestionsContainer(codeExtractionState)

        val addAndOptionsContainer = AddAndOptionsContainer()

        val splitPane = JSplitPane(
            SwingConstants.VERTICAL,
            syntaxAndSuggestionsContainer,
            addAndOptionsContainer
        ).apply {
            setUI(CustomSplitPaneDivider(ThemeApp.awtColors.primaryColor))
            isContinuousLayout = true
        }

        val textFieldRegexTests = RSyntaxTextArea().apply {
            text = AssemblyCodeExamples.getCodeExample()
            isCodeFoldingEnabled = false
            background = ThemeApp.awtColors.secondaryColor
            foreground = ThemeApp.awtColors.textColor
            font = ThemeApp.text.fontJetBrains
            syntaxScheme.setDefaultSyntaxScheme()
            syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86
            caretPosition = 0
            border = EmptyBorder(5, 5, 5, 5)
            font = ThemeApp.text.fontJetBrains
            currentLineHighlightColor = ThemeApp.awtColors.hoverColor
            caretColor = ThemeApp.awtColors.complementaryColor
            selectionColor = ThemeApp.awtColors.complementaryColor
        }

        val scrollPane = JScrollPane(textFieldRegexTests).apply {
            background = ThemeApp.awtColors.secondaryColor
            border = BorderFactory.createLineBorder(ThemeApp.awtColors.hoverColor)
            verticalScrollBar.setUI(CustomScrollBar(ThemeApp.awtColors.secondaryColor))
            horizontalScrollBar.setUI(CustomScrollBar(ThemeApp.awtColors.secondaryColor))
        }

        codeExtractionLayout.setHorizontalGroup(
            codeExtractionLayout.createParallelGroup()
                .addComponent(splitPane, 0, 0, Short.MAX_VALUE.toInt())
                .addComponent(scrollPane, 0, 0, Short.MAX_VALUE.toInt())
        )

        codeExtractionLayout.setVerticalGroup(
            codeExtractionLayout.createSequentialGroup()
                .addComponent(splitPane, 0, 0, Short.MAX_VALUE.toInt())
                .addGap(10)
                .addComponent(scrollPane, 0, 0, Short.MAX_VALUE.toInt())
        )

    }

}