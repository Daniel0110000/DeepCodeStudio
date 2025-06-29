package com.dr10.editor.ui.tabs.components

import com.dr10.autocomplete.AutoCompletion
import com.dr10.autocomplete.BasicCompletion
import com.dr10.autocomplete.DefaultCompletionProvider
import com.dr10.common.models.RegexRuleModel
import com.dr10.common.models.SelectedConfigHistoryModel
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomScrollBar
import com.dr10.common.ui.editor.setDefaultSyntaxScheme
import com.dr10.common.utilities.ColorUtils
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.Constants
import com.dr10.common.utilities.DrLogging
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.JavaUtils
import com.dr10.common.utilities.TextUtils.deleteWhiteSpaces
import com.dr10.common.utilities.setState
import com.dr10.editor.lexers.DefaultAssemblerTokenMaker
import com.dr10.editor.ui.tabs.TabModel
import com.dr10.editor.ui.tabs.utilities.AutoSaveProcess
import com.dr10.editor.ui.tabs.utilities.CodeAnalyzer
import com.dr10.editor.ui.tabs.utilities.setDocumentListener
import com.dr10.editor.ui.tabs.utilities.setSyntaxSchemeColor
import com.dr10.editor.ui.viewModels.EditorTabViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory
import org.fife.ui.rtextarea.Gutter.GutterBorder
import org.fife.ui.rtextarea.RTextScrollPane
import java.io.File
import java.io.StringReader
import java.nio.file.Path
import java.nio.file.Paths
import javax.swing.GroupLayout
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.border.EmptyBorder


/**
 * Custom [JPanel] that display the code editor and the action bottom bar
 *
 * @property tab The model containing tab information
 * @property viewModel The view model that handles the editor ui state
 * @property editorTabState The state wrapper that maintains the editor tab's current state
 */
class CodeEditor(
    private val tab: TabModel,
    private val viewModel: EditorTabViewModel,
    private val editorTabState: FlowStateHandler.StateWrapper<EditorTabViewModel.EditorTabState>
): JPanel() {

    private val logger = DrLogging(this::class.java)

    private val codeEditorLayout = GroupLayout(this)

    private val suggestionsExtracted = HashSet<BasicCompletion>()

    private val classLoader = JavaUtils.createCustomClasLoader()
    private val tokenMakerFactory: AbstractTokenMakerFactory = TokenMakerFactory.getDefaultInstance() as AbstractTokenMakerFactory

    private val codeAnalyzer = CodeAnalyzer()
    private lateinit var completionProvider: DefaultCompletionProvider
    private lateinit var autoCompletion: AutoCompletion

    private lateinit var editor: RSyntaxTextArea
    private lateinit var editorScrollPane: RTextScrollPane
    private lateinit var autoSaveProcess: AutoSaveProcess

    private val bottomActionsRow = BottomActionsRow(viewModel, editorTabState)

    init {
        CoroutineScope(Dispatchers.Swing).launch {
            layout = codeEditorLayout
            initializeComponents()
            setComponentsStructure()
            SwingUtilities.invokeLater { editor.requestFocusInWindow() }
        }
    }

    private fun initializeComponents() {
        editor = RSyntaxTextArea().apply {
            read(StringReader(File(tab.filePath).readText()), null)
            isCodeFoldingEnabled = false
            background = ThemeApp.awtColors.primaryColor
            font = ThemeApp.text.fontJetBrains
            syntaxScheme.setDefaultSyntaxScheme()
            foreground = ThemeApp.awtColors.textColor
            currentLineHighlightColor = ThemeApp.colors.hoverTab.toAWTColor()
            caretColor = ThemeApp.awtColors.complementaryColor
            caretPosition = 0
            selectionColor = ThemeApp.awtColors.complementaryColor
            autoSaveProcess = AutoSaveProcess(filePath = Paths.get(tab.filePath), syntaxTextArea = this)
            setDocumentListener(
                autoSaveProcess,
                codeAnalyzer,
                codeAnalyzeResults = { suggestions ->
                    addNewSuggestions(suggestions.map { it.name })
                    logger.info("NEW::SUGGESTIONS::$suggestions")
                }
            )
            setState(editorTabState, EditorTabViewModel.EditorTabState::isEditable) { value -> isEditable = value }
            setState(editorTabState, EditorTabViewModel.EditorTabState::selectedConfig) { config ->
                updateSelectedConfig(config)
            }
        }

        setState(editorTabState, EditorTabViewModel.EditorTabState::suggestionsFromJson) { suggestions ->
            addSuggestionsFromJson(suggestions)
        }

        setState(editorTabState, EditorTabViewModel.EditorTabState::patterns) { patterns ->
            // Deleted all the old patterns
            deleteSuggestionsExtracted()
            analyzeCode(patterns)
        }

        editorScrollPane = RTextScrollPane(editor).apply {
            border = EmptyBorder(0, 0, 0, 0)
            foreground = ThemeApp.colors.lineNumberTextColor.toAWTColor()
            font = ThemeApp.text.fontInterRegular(13f)
            gutter.lineNumberColor = ThemeApp.colors.lineNumberTextColor.toAWTColor()
            gutter.lineNumberFont = ThemeApp.text.fontInterRegular(12f)
            gutter.border = GutterBorder(0, 10, 0, 10).apply { color = ThemeApp.colors.hoverTab.toAWTColor() }
            gutter.currentLineNumberColor = ThemeApp.awtColors.complementaryColor
            verticalScrollBar.setUI(CustomScrollBar())
            horizontalScrollBar.setUI(CustomScrollBar())
        }
    }

    private fun setComponentsStructure() {
        codeEditorLayout.setHorizontalGroup(
            codeEditorLayout.createParallelGroup()
                .addComponent(editorScrollPane, 0, 0, Short.MAX_VALUE.toInt())
                .addComponent(bottomActionsRow, 0, 0, Short.MAX_VALUE.toInt())
        )

        codeEditorLayout.setVerticalGroup(
            codeEditorLayout.createSequentialGroup()
                .addComponent(editorScrollPane, 0, 0, Short.MAX_VALUE.toInt())
                .addComponent(bottomActionsRow, 0, 0, 25)
        )
    }

    private fun updateSelectedConfig(config: SelectedConfigHistoryModel?) {
        val syntaxKey = "syntax/${config?.optionName ?: Constants.DEFAULT_ASM_SYNTAX_NAME}".deleteWhiteSpaces()
        // If the config isn't null, use a custom clas loader for user-defined syntax
        if(config != null) tokenMakerFactory.putMapping(syntaxKey, config.className, classLoader)
        // If the config is null, use default assembler syntax
        else tokenMakerFactory.putMapping(syntaxKey, DefaultAssemblerTokenMaker::class.java.name)
        editor.syntaxEditingStyle = if (tab.fileName == "Makefile") SyntaxConstants.SYNTAX_STYLE_MAKEFILE else syntaxKey
        config?.let {
            editor.syntaxScheme.setSyntaxSchemeColor(it)
            foreground = ColorUtils.stringToColor(it.simpleColor)
        }
    }

    /**
     * Adds suggestions from JSON to the completion provider
     *
     * @param suggestions The list of suggestions to add
     */
    private fun addSuggestionsFromJson(suggestions: List<String>) {
        if (!::completionProvider.isInitialized) completionProvider = createCompletionProvider()
        suggestions.forEach { suggestion ->
            completionProvider.addCompletion(BasicCompletion(completionProvider, suggestion))
        }

        autoCompleteInitialized()
    }

    /**
     * Analyzes the code in the editor using the provided patterns
     *
     * @param patterns The list of regex patterns to use for analysis
     */
    private fun analyzeCode(patterns: List<RegexRuleModel>) {
        if (patterns.isNotEmpty()) {
            viewModel.setIsAnalyzing(true)
            // Adds all the patterns to be used for the analysis
            patterns.forEach { pattern ->
                codeAnalyzer.addPattern(
                    name = pattern.regexName,
                    pattern = pattern.regexPattern
                )
            }
            // Analyzes the content of the current file
            codeAnalyzer.analyzeFile(Path.of(tab.filePath)) {
                val result = codeAnalyzer.getNewAllSymbols().map { symbol -> symbol.name }
                addNewSuggestions(result)
                viewModel.setIsAnalyzing(false)
            }

        }
    }

    /**
     * Adds new suggestions to the completion provider
     *
     * @param newSuggestions The list of new suggestions to add
     */
    private fun addNewSuggestions(newSuggestions: List<String>) {
        newSuggestions.forEach { suggestion ->
            if (suggestionsExtracted.none { it.inputText == suggestion }) {
                val completion = BasicCompletion(completionProvider, suggestion)
                completionProvider.addCompletion(completion)
                suggestionsExtracted.add(completion)
            }
        }

        autoCompleteInitialized()
    }

    /**
     * Initializes the auto-completion feature if it hasn't been already
     */
    private fun autoCompleteInitialized() {
        if (!::autoCompletion.isInitialized) {
            autoCompletion = AutoCompletion(completionProvider).apply {
                isAutoActivationEnabled = true
                autoActivationDelay = 200
                autoCompleteSingleChoices = false
                install(editor)
            }
        }
    }

    /**
     * Creates a new instance of [DefaultCompletionProvider]
     */
    private fun createCompletionProvider(): DefaultCompletionProviderImpl =
        DefaultCompletionProviderImpl().apply {
            setAutoActivationRules(true, ".%@#\$_-:/\\&*+=!?|^~`[]{}()<>")
        }

    /**
     * Deletes all the suggestions extracted from the code
     */
    private fun deleteSuggestionsExtracted() {
        suggestionsExtracted.forEach { suggestion -> completionProvider.removeCompletion(suggestion) }
        suggestionsExtracted.clear()
        codeAnalyzer.clear()
    }

    fun cancelAutoSaveProcess() { autoSaveProcess.shutdown() }
}