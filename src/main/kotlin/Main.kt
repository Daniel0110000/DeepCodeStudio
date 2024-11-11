import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.UIManagerConfig
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.DocumentsManager
import com.dr10.database.di.databaseModule
import com.dr10.editor.di.editorModule
import com.dr10.settings.di.settingsModule
import com.dr10.terminal.di.terminalModule
import di.appModule
import org.koin.core.context.GlobalContext.startKoin
import ui.CodeEditorScreen
import java.awt.Toolkit
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants


fun main() = SwingUtilities.invokeLater {

    // Set the necessary properties of the UI Manager
    UIManagerConfig.config()

    val toolkit = Toolkit.getDefaultToolkit().screenSize

    // Create the necessary directories for the program
    DocumentsManager.createNecessaryDirectories()

    // Initialize Koin
    startKoin { modules(appModule, databaseModule, settingsModule, editorModule, terminalModule) }

    val window = JFrame().apply {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        setSize(toolkit.width - 100, toolkit.height - 100)
        contentPane.background = ThemeApp.colors.background.toAWTColor()
        title = "DeepCode Studio"
        setLocationRelativeTo(null)
    }

    CodeEditorScreen(window)

    window.isVisible = true
}
