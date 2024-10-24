package com.dr10.editor.ui.tabs

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.Constants
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.JavaUtils
import com.dr10.common.utilities.TextUtils.deleteWhiteSpaces
import com.dr10.common.utilities.setState
import com.dr10.editor.lexers.DefaultAssemblerTokenMaker
import com.dr10.editor.ui.viewModels.EditorTabViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory
import org.fife.ui.rtextarea.RTextScrollPane
import java.io.File
import javax.swing.GroupLayout
import javax.swing.JPanel
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
    private val abstractTokenMakerFactory: AbstractTokenMakerFactory = TokenMakerFactory.getDefaultInstance() as AbstractTokenMakerFactory

    init { onCreate() }

    private fun onCreate() = CoroutineScope(Dispatchers.Swing).launch {
        val codeEditorLayout = GroupLayout(this@CodeEditor)
        layout = codeEditorLayout

        val editor = RSyntaxTextArea().apply {
            isCodeFoldingEnabled = false
            background = ThemeApp.colors.background.toAWTColor()
            foreground = ThemeApp.colors.textColor.toAWTColor()
            syntaxScheme.setDefaultSyntaxScheme()
            font = ThemeApp.text.fontJetBrains
            currentLineHighlightColor = ThemeApp.colors.hoverTab.toAWTColor()
            caretColor = ThemeApp.colors.buttonColor.toAWTColor()
            text = File(tab.filePath).readText()
            caretPosition = 0
            setState(editorTabState, EditorTabViewModel.EditorTabState::isEditable) { value -> isEditable = value }
            setState(editorTabState, EditorTabViewModel.EditorTabState::selectedConfig) { config ->
                val syntaxKey = "syntax/${config?.optionName ?: Constants.DEFAULT_ASM_SYNTAX_NAME}".deleteWhiteSpaces()
                // If the config isn't null, use a custom clas loader for user-defined syntax
                if(config != null) abstractTokenMakerFactory.putMapping(syntaxKey, config.className, classLoader)
                // If the config is null, use default assembler syntax
                else abstractTokenMakerFactory.putMapping(syntaxKey, DefaultAssemblerTokenMaker::class.java.name)
                syntaxEditingStyle = syntaxKey
            }
        }

        val bottomActionsRow = BottomActionsRow(viewModel, editorTabState)


        val scrollPane = RTextScrollPane(editor).apply {
            border = EmptyBorder(0, 0, 0, 0)
            foreground = ThemeApp.colors.lineNumberTextColor.toAWTColor()
            font = ThemeApp.text.fontInterRegular(13f)
            gutter.borderColor = ThemeApp.colors.lineNumberTextColor.toAWTColor()
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
    }
}