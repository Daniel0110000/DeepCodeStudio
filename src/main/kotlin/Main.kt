import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.DocumentsManager
import com.dr10.database.di.databaseModule
import com.dr10.editor.di.editorModule
import com.dr10.settings.di.settingsModule
import di.appModule
import org.koin.core.context.GlobalContext.startKoin
import ui.CodeEditorScreen
import java.awt.Toolkit
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

fun main() = SwingUtilities.invokeLater {
    val toolkit = Toolkit.getDefaultToolkit().screenSize

    DocumentsManager.createNecessaryDirectories()

    // Initialize Koin
    startKoin { modules(appModule, databaseModule, settingsModule, editorModule) }

    val window = JFrame().apply {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        setSize(toolkit.width - 100, toolkit.height - 100)
        contentPane.background = ThemeApp.colors.background.toAWTColor()
        title = "DeepCode Studio"
    }

    CodeEditorScreen(window)

    window.isVisible = true
}
