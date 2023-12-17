package domain.utilies

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.lwjgl.system.MemoryUtil
import org.lwjgl.util.nfd.NativeFileDialog
import javax.swing.JFileChooser
import javax.swing.UIManager

object DirectoryChooser {

    /**
     * Suspend function to choose a directory using native dialogs if available,
     * or fall back to using a Swing-based dialog
     *
     * @return The selected directory path or `null` if canceled or an error occurred
     */
    suspend fun chooseDirectory(): String? {
        return kotlin.runCatching { chooseDirectoryNative() }
            .onFailure { nativeException ->
                println("A call to chooseDirectoryNative failed: ${nativeException.message}")

                return kotlin.runCatching { chooseDirectorySwing() }
                    .onFailure { swingException ->
                        println("A call to chooseDirectorySwing failed: ${swingException.message}")
                    }
                    .getOrNull()
            }
            .getOrNull()
    }

    /**
     * Suspended function to choose a directory using the native dialog
     *
     * @return The selected directory path or `null` if canceled or an error occurred
     */
    private suspend fun chooseDirectoryNative() = withContext(Dispatchers.IO) {
        val pathPointer = MemoryUtil.memAllocPointer(1)
        try {
            return@withContext when (val code = NativeFileDialog.NFD_PickFolder(DocumentsManager.getUserHome(), pathPointer)) {
                NativeFileDialog.NFD_OKAY -> {
                    val path = pathPointer.stringUTF8
                    NativeFileDialog.nNFD_Free(pathPointer[0])

                    path
                }
                NativeFileDialog.NFD_CANCEL -> null
                NativeFileDialog.NFD_ERROR -> error("An error occurred while executing NativeFileDialog.NFD_PickFolder")
                else -> error("Unknown return code '${code}' from NativeFileDialog.NFD_PickFolder")
            }
        } finally {
            MemoryUtil.memFree(pathPointer)
        }
    }

    /**
     * Suspended function to choose a directory using a Swing-based dialog
     *
     * @return The selected directory path or `null` if canceled or an error occurred
     */
    private suspend fun chooseDirectorySwing() = withContext(Dispatchers.IO) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

        val chooser = JFileChooser(DocumentsManager.getUserHome()).apply {
            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            isVisible = true
        }

        when (val code = chooser.showOpenDialog(null)) {
            JFileChooser.APPROVE_OPTION -> chooser.selectedFile.absolutePath
            JFileChooser.CANCEL_OPTION -> null
            JFileChooser.ERROR_OPTION -> error("An error occurred while executing JFileChooser::showOpenDialog")
            else -> error("Unknown return code '${code}' from JFileChooser::showOpenDialog")
        }
    }

}