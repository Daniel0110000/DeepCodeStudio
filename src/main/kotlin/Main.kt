import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import di.appModule
import domain.utilies.DocumentsManager
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import ui.CodeEditorScreen
import java.awt.Toolkit

fun main() = application {

    val toolkit = Toolkit.getDefaultToolkit().screenSize
    rememberCoroutineScope().launch { DocumentsManager.createDefaultProjectsDirectory() }

    // Initialize Koin
    startKoin { modules(appModule) }

    Window(
        onCloseRequest = ::exitApplication,
        title = "DeepCode Studio",
        icon = painterResource("images/ic_launcher.svg"),
        state = WindowState(
            position = WindowPosition(Alignment.Center),
            size = DpSize((toolkit.width - 100).dp, (toolkit.height - 100).dp)
        )
    ) {
        CodeEditorScreen()
    }
}
