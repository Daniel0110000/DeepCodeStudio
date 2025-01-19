package com.dr10.editor.ui.tabs.utilities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

fun RSyntaxTextArea.setDocumentListener(
    autoSaveProcess: AutoSaveProcess,
    codeAnalyzer: CodeAnalyzer,
    codeAnalyzeResults: (Set<CodeSymbol>) -> Unit,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {

    var analyzeJob: Job? = null

    /**
     * Function that uses debounce to improve the performance of real-time code analysis
     *
     * @param e The editor document for the analysis
     */
    fun debounceAnalyze(e: DocumentEvent) {
        analyzeJob?.cancel()
        analyzeJob = scope.launch {
            delay(1000)
            val (line, lineNumber) = getCurrentLineContent(this@setDocumentListener, e.offset)
            codeAnalyzer.processLine(line, lineNumber)
            val results = codeAnalyzer.getNewAllSymbols()
            if (results.isNotEmpty()) codeAnalyzeResults(results)
        }
    }

    document.addDocumentListener(object: DocumentListener {
        override fun insertUpdate(e: DocumentEvent) {
            autoSaveProcess.contentChanged()
            debounceAnalyze(e)
        }

        override fun removeUpdate(e: DocumentEvent) { autoSaveProcess.contentChanged() }

        override fun changedUpdate(e: DocumentEvent?) { autoSaveProcess.contentChanged() }

    })
}