package com.dr10.editor.ui.tabs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import java.nio.file.Path
import kotlin.io.path.bufferedWriter

/**
 * Represents an auto save process that saves the content of a [RSyntaxTextArea] to a file
 *
 * @param filePath The path of the file to save the content to
 * @param syntaxTextArea The [RSyntaxTextArea] to get the content from
 */
class AutoSaveProcess(
    private val filePath: Path,
    private val syntaxTextArea: RSyntaxTextArea
) {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val contentChange = MutableSharedFlow<Unit>()
    private var lastSaveHast = 0
    private var isAutoSaveEnabled = false

    /**
     * Notifies the auto save process that the content has changed
     */
    fun contentChanged() {
        if (!isAutoSaveEnabled) {
            isAutoSaveEnabled = true
            startAutoSave()
        }

        scope.launch { contentChange.emit(Unit) }
    }

    /**
     * Starts the auto save process with a debounce of 2 seconds
     */
    @OptIn(FlowPreview::class)
    private fun startAutoSave() {
        scope.launch {
            contentChange
                .debounce(2000L)
                .collectLatest { saveContent() }
        }
    }

    /**
     * Saves the content of the syntax text area to the file path
     */
    private suspend fun saveContent() {
        val content = withContext(Dispatchers.Swing) { syntaxTextArea.text }
        val currentHash = content.hashCode()

        if (currentHash != lastSaveHast && filePath.toFile().exists()) {
            withContext(Dispatchers.IO) {
                try {
                    filePath.bufferedWriter().use { writer ->
                        writer.write(content)
                        writer.flush()
                    }
                    lastSaveHast = currentHash
                } catch (e: Exception) {
                    // TODO: Should display a notification with the error
                    e.printStackTrace()
                }
            }
        }

    }

    /**
     * Shuts down the auto save process and saves the content one last time
     */
    fun shutdown() {
        scope.launch {
            saveContent()
            scope.cancel()
        }
    }


}