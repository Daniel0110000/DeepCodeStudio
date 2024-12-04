package com.dr10.editor.ui.tabs.components

import com.dr10.autocomplete.AutoCompletion
import com.dr10.autocomplete.BasicCompletion
import com.dr10.autocomplete.CompletionProvider
import com.dr10.autocomplete.DefaultCompletionProvider
import com.dr10.common.models.SelectedConfigHistoryModel
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomScrollBar
import com.dr10.common.utilities.ColorUtils
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.Constants
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.JavaUtils
import com.dr10.common.utilities.TextUtils.deleteWhiteSpaces
import com.dr10.common.utilities.setState
import com.dr10.editor.lexers.DefaultAssemblerTokenMaker
import com.dr10.editor.ui.tabs.TabModel
import com.dr10.editor.ui.tabs.utilities.AutoSaveProcess
import com.dr10.editor.ui.tabs.utilities.setDefaultSyntaxScheme
import com.dr10.editor.ui.tabs.utilities.setDocumentListener
import com.dr10.editor.ui.tabs.utilities.setSyntaxSchemeColor
import com.dr10.editor.ui.viewModels.EditorTabViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory
import org.fife.ui.rtextarea.Gutter.GutterBorder
import org.fife.ui.rtextarea.RTextScrollPane
import java.io.File
import java.io.StringReader
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

    private val classLoader = JavaUtils.createCustomClasLoader()
    private val tokenMakerFactory: AbstractTokenMakerFactory = TokenMakerFactory.getDefaultInstance() as AbstractTokenMakerFactory

    private lateinit var editor: RSyntaxTextArea
    private lateinit var autoSaveProcess: AutoSaveProcess

    init { onCreate() }

    private fun onCreate() = CoroutineScope(Dispatchers.Swing).launch {
        val codeEditorLayout = GroupLayout(this@CodeEditor)
        layout = codeEditorLayout

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
            setDocumentListener(autoSaveProcess)
            setState(editorTabState, EditorTabViewModel.EditorTabState::isEditable) { value -> isEditable = value }
            setState(editorTabState, EditorTabViewModel.EditorTabState::selectedConfig) { config ->
                updateSelectedConfig(config)
            }
        }

        setState(editorTabState, EditorTabViewModel.EditorTabState::suggestionsFromJson) { suggestions ->
            updateSuggestions(suggestions)
        }


        val bottomActionsRow = BottomActionsRow(viewModel, editorTabState)


        val scrollPane = RTextScrollPane(editor).apply {
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

        codeEditorLayout.setHorizontalGroup(
            codeEditorLayout.createParallelGroup()
                .addComponent(scrollPane, 0, 0, Short.MAX_VALUE.toInt())
                .addComponent(bottomActionsRow, 0, 0, Short.MAX_VALUE.toInt())
        )

        codeEditorLayout.setVerticalGroup(
            codeEditorLayout.createSequentialGroup()
                .addComponent(scrollPane, 0, 0, Short.MAX_VALUE.toInt())
                .addComponent(bottomActionsRow, 0, 0, 25)
        )

        SwingUtilities.invokeLater { editor.requestFocusInWindow() }
    }

    private fun updateSelectedConfig(config: SelectedConfigHistoryModel?) {
        val syntaxKey = "syntax/${config?.optionName ?: Constants.DEFAULT_ASM_SYNTAX_NAME}".deleteWhiteSpaces()
        // If the config isn't null, use a custom clas loader for user-defined syntax
        if(config != null) tokenMakerFactory.putMapping(syntaxKey, config.className, classLoader)
        // If the config is null, use default assembler syntax
        else tokenMakerFactory.putMapping(syntaxKey, DefaultAssemblerTokenMaker::class.java.name)
        editor.syntaxEditingStyle = syntaxKey
        config?.let {
            editor.syntaxScheme.setSyntaxSchemeColor(it)
            foreground = ColorUtils.stringToColor(it.simpleColor)
        }
    }

    private fun updateSuggestions(suggestions: List<String>) {
        val provider = createCompletionProvider(suggestions)
        val autoCompletion = AutoCompletion(provider).apply { isAutoActivationEnabled = true }
        autoCompletion.install(editor)
    }

    private fun createCompletionProvider(suggestions: List<String>): CompletionProvider {
        val provider = DefaultCompletionProvider()
        provider.setAutoActivationRules(true, ".")

        suggestions.forEach { suggestion ->
            provider.addCompletion(BasicCompletion(provider, suggestion.replace(".", "")))
        }

        return provider
    }

    fun cancelAutoSaveProcess() { autoSaveProcess.shutdown() }
}