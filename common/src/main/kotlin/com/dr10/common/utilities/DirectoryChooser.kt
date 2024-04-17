package com.dr10.common.utilities

import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.swing.JFileChooser
import javax.swing.UIManager

object DirectoryChooser {

    /**
     * Suspend function to choose a directory using a Swing-based dialog
     *
     * @return The selected directory path or `null` if canceled or an error occurred
     */
    suspend fun chooseDirectory(): String? = kotlin.runCatching { chooseDirectorySwing() }
        .onFailure { exception -> println("A call to chooseDirectorySwing failed: ${exception.message}") }
        .getOrNull()

    /**
     * Suspended function to choose a directory using a Swing-based dialog
     *
     * @return The selected directory path or `null` if canceled or an error occurred
     */
    private suspend fun chooseDirectorySwing() = withContext(Dispatchers.IO) {
        UIManager.setLookAndFeel(FlatOneDarkIJTheme())

        val chooser = JFileChooser(DocumentsManager.getUserHome()).apply {
            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            isVisible = true
            dialogTitle = "Choose Directory"
        }

        when (val code = chooser.showOpenDialog(null)) {
            JFileChooser.APPROVE_OPTION -> chooser.selectedFile.absolutePath
            JFileChooser.CANCEL_OPTION -> null
            JFileChooser.ERROR_OPTION -> error("An error occurred while executing JFileChooser::showOpenDialog")
            else -> error("Unknown return code '${code}' from JFileChooser::showOpenDialog")
        }
    }

}