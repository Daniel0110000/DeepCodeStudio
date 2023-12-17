package domain.utilies

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.lwjgl.system.MemoryUtil
import org.lwjgl.util.nfd.NativeFileDialog
import javax.swing.JFileChooser
import javax.swing.UIManager
import javax.swing.filechooser.FileNameExtensionFilter

object ChooseJson {

    /**
     * Suspend function to choose json file using native dialogs if available,
     * or fall back to using Swing-based dialog
     *
     * @return The selected json file path or 'null' if canceled or an error occurred
     */
    suspend fun chooseJson(): String?{
        return kotlin.runCatching { chooseJsonNative() }
            .onFailure { nativeException ->
                println("A call to chooseJsonNative failed: ${nativeException.message}")

                return kotlin.runCatching { chooseJsonSwing() }
                    .onFailure { swingException ->
                        println("A call to chooseJsonSwing failed: ${swingException.message}")
                    }
                    .getOrNull()
            }
            .getOrNull()
    }

    /**
     * Suspended function to choose a json file using the native dialog
     *
     * @return The selected json file path or 'null' if canceled or an error occurred
     */
    private suspend fun chooseJsonNative() = withContext(Dispatchers.IO){
        val pathPointer = MemoryUtil.memAllocPointer(1)
        try {
            return@withContext when (val code = NativeFileDialog.NFD_OpenDialog("json", DocumentsManager.getUserHome(), pathPointer)){
                NativeFileDialog.NFD_OKAY -> {
                    val path = pathPointer.stringUTF8
                    NativeFileDialog.nNFD_Free(pathPointer[0])

                    path
                }
                NativeFileDialog.NFD_CANCEL -> null
                NativeFileDialog.NFD_ERROR -> error("An error occurred while executing NativeFileDialog.NFD_OpenDialog")
                else -> error("Unknown return code '$code' from NativeFileDialog.NFD_OpenDialog")
            }
        } finally {
            MemoryUtil.memFree(pathPointer)
        }
    }

    /**
     * Suspended function to choose a json file using a Swing-based dialog
     *
     * @return The selected json file path or 'null' if canceled or an error occurred
     */
    private suspend fun chooseJsonSwing() = withContext(Dispatchers.IO){
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

        val chooser = JFileChooser(DocumentsManager.getUserHome()).apply {
            fileSelectionMode = JFileChooser.FILES_ONLY
            fileFilter = FileNameExtensionFilter("File Json", "json")
            isVisible = true
        }

        when (val code = chooser.showOpenDialog(null)){
            JFileChooser.APPROVE_OPTION -> chooser.selectedFile.absolutePath
            JFileChooser.CANCEL_OPTION -> null
            JFileChooser.ERROR_OPTION -> error("An error occurred while executing JFileChooser::showOpenDialog")
            else -> error("Unknown return code '$code' from JFileChooser::showOpenDialog")
        }

    }

}