package ui

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import kotlinx.coroutines.launch
import util.DocumentsManager
import java.awt.Toolkit

fun main() = application {
    val toolkit = Toolkit.getDefaultToolkit().screenSize
    rememberCoroutineScope().launch { DocumentsManager.createDefaultProjectsDirectory() }
    Window(
        onCloseRequest = ::exitApplication,
        title = "DeepCode Studio",
        state = WindowState(
            position = WindowPosition(Alignment.Center),
            size = DpSize((toolkit.width - 100).dp, (toolkit.height - 100).dp)
        )
    ) {
        CodeEditorScreen()
    }
}
