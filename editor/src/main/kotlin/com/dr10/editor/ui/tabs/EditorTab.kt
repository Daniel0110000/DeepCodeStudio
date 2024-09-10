package com.dr10.editor.ui.tabs

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import java.io.File
import javax.swing.GroupLayout
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class EditorTab(
    private val tab: TabModel
): JPanel() {

    init { onCreate() }

    private fun onCreate() {
        val editorTabLayout = GroupLayout(this)
        layout = editorTabLayout
        background = ThemeApp.colors.background.toAWTColor()

        val editor = RSyntaxTextArea().apply {
            syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86
            isCodeFoldingEnabled = false
            background = ThemeApp.colors.background.toAWTColor()
            foreground = ThemeApp.colors.textColor.toAWTColor()
            font = ThemeApp.text.fontJetBrains
            currentLineHighlightColor = ThemeApp.colors.hoverTab.toAWTColor()
            caretColor = ThemeApp.colors.buttonColor.toAWTColor()
            text = File(tab.filePath).readText()
        }

        val scrollPane = RTextScrollPane(editor).apply {
            border = EmptyBorder(0, 0, 0, 0)
            foreground = ThemeApp.colors.lineNumberTextColor.toAWTColor()
            font = ThemeApp.text.fontInterRegular(13f)
            gutter.borderColor = ThemeApp.colors.lineNumberTextColor.toAWTColor()
        }

        editorTabLayout.setHorizontalGroup(
            editorTabLayout.createParallelGroup()
                .addComponent(scrollPane, 0, 0, Short.MAX_VALUE.toInt())
        )

        editorTabLayout.setVerticalGroup(
            editorTabLayout.createParallelGroup()
                .addComponent(scrollPane, 0, 0, Short.MAX_VALUE.toInt())
        )

    }

}