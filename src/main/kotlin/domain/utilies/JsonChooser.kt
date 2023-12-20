package domain.utilies

import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.swing.JFileChooser
import javax.swing.UIManager
import javax.swing.filechooser.FileNameExtensionFilter

object JsonChooser {

    /**
     * Suspend function to choose json file using Swing-based dialog
     *
     * @return The selected json file path or 'null' if canceled or an error occurred
     */
    suspend fun chooseJson(): String? = kotlin.runCatching { chooseJsonSwing() }
        .onFailure { exception -> println("A call to chooseJsonSwing failed: ${exception.message}") }
        .getOrNull()

    /**
     * Suspended function to choose a json file using a Swing-based dialog
     *
     * @return The selected json file path or 'null' if canceled or an error occurred
     */
    private suspend fun chooseJsonSwing() = withContext(Dispatchers.IO){
        UIManager.setLookAndFeel(FlatOneDarkIJTheme())

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