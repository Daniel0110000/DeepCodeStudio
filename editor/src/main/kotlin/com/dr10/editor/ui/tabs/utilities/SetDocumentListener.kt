package com.dr10.editor.ui.tabs.utilities

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

fun RSyntaxTextArea.setDocumentListener(autoSaveProcess: AutoSaveProcess) {
    document.addDocumentListener(object: DocumentListener {
        override fun insertUpdate(e: DocumentEvent?) { autoSaveProcess.contentChanged() }

        override fun removeUpdate(e: DocumentEvent?) { autoSaveProcess.contentChanged() }

        override fun changedUpdate(e: DocumentEvent?) { autoSaveProcess.contentChanged() }

    })
}