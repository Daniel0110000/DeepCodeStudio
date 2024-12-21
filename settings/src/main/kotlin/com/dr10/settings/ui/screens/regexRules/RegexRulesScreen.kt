package com.dr10.settings.ui.screens.regexRules

import com.dr10.common.models.NotificationData
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomScrollBar
import com.dr10.common.ui.components.CustomSplitPaneDivider
import com.dr10.common.ui.editor.setDefaultSyntaxScheme
import com.dr10.common.ui.notification.NotificationType
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.RegexUtils
import com.dr10.common.utilities.setState
import com.dr10.settings.di.Inject
import com.dr10.settings.ui.screens.colorScheme.AssemblyCodeExamples
import com.dr10.settings.ui.screens.regexRules.components.AddAndOptionsContainer
import com.dr10.settings.ui.screens.regexRules.components.HighlightPainterWithBorder
import com.dr10.settings.ui.screens.regexRules.components.SyntaxAndSuggestionsContainer
import com.dr10.settings.ui.viewModels.RegexRulesViewModel
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import javax.swing.BorderFactory
import javax.swing.GroupLayout
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import javax.swing.SwingConstants
import javax.swing.border.EmptyBorder
import javax.swing.text.Highlighter

class RegexRulesScreen(
    private val onDisplayNotification: (NotificationData) -> Unit
): JPanel(){

    private val viewModel: RegexRulesViewModel = Inject().regexRulesViewModel
    private val regexRulesState = FlowStateHandler().run {
        viewModel.state.collectAsState(RegexRulesViewModel.RegexRulesState())
    }

    private val codeExtractionLayout = GroupLayout(this)

    private val syntaxAndSuggestionsContainer = SyntaxAndSuggestionsContainer(viewModel, regexRulesState)
    private val addAndOptionsContainer = AddAndOptionsContainer(viewModel, regexRulesState) { pattern -> regexRuleTest(pattern) }

    private lateinit var splitPane: JSplitPane
    private lateinit var textFieldRegexRuleTests: RSyntaxTextArea
    private lateinit var scrollPane: JScrollPane

    private val painter: Highlighter.HighlightPainter = HighlightPainterWithBorder(
        ThemeApp.awtColors.highlighter,
        ThemeApp.awtColors.complementaryColor
    )

    init {
        layout = codeExtractionLayout
        background = ThemeApp.awtColors.primaryColor
        border = EmptyBorder(10, 10, 10, 10)

        initializeComponents()
        setComponentsStructure()
    }

    private fun initializeComponents() {
        splitPane = JSplitPane(
            SwingConstants.VERTICAL,
            syntaxAndSuggestionsContainer,
            addAndOptionsContainer
        ).apply {
            setUI(CustomSplitPaneDivider(ThemeApp.awtColors.primaryColor))
            isContinuousLayout = true
        }

        textFieldRegexRuleTests = RSyntaxTextArea().apply {
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

        scrollPane = JScrollPane(textFieldRegexRuleTests).apply {
            background = ThemeApp.awtColors.secondaryColor
            border = BorderFactory.createLineBorder(ThemeApp.awtColors.hoverColor)
            verticalScrollBar.setUI(CustomScrollBar(ThemeApp.awtColors.secondaryColor))
            horizontalScrollBar.setUI(CustomScrollBar(ThemeApp.awtColors.secondaryColor))
        }

        setState(regexRulesState, RegexRulesViewModel.RegexRulesState::notificationData) { data ->
            if(data != null) {
                onDisplayNotification(data)
                viewModel.clearNotificationData()
            }
        }
    }

    private fun setComponentsStructure() {
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

    /**
     * Test if the regex pattern is valid and if it is, highlights the matches in the
     * [textFieldRegexRuleTests], otherwise display an error notification
     *
     * @param pattern The regex pattern to be tested
     */
    private fun regexRuleTest(pattern: String) {
        val isValidPattern = RegexUtils.isValidPattern(pattern)
        if (isValidPattern.first) highlightMatches(pattern)
        else onDisplayNotification(
            NotificationData(
                message = isValidPattern.second,
                type = NotificationType.ERROR,
                isAutoDismiss = true
            )
        )
    }

    /**
     * Highlights the matches of the regex pattern in the [textFieldRegexRuleTests]
     *
     * @param pattern The regex pattern to be used for highlighting
     */
    private fun highlightMatches(pattern: String) {
        textFieldRegexRuleTests.highlighter.removeAllHighlights()
        if(pattern.isNotBlank()) {
            Regex(pattern).findAll(textFieldRegexRuleTests.text).forEach {
                val index = if(it.groups.size > 1) 1 else 0
                val range = it.groups[index]?.range ?: IntRange(0, 0)
                textFieldRegexRuleTests.highlighter.addHighlight(range.first, range.last + 1, painter)
                textFieldRegexRuleTests.repaint()
            }
        }
    }

}