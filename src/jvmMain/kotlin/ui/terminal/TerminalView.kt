package ui.terminal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme
import com.jediterm.terminal.ui.JediTermWidget
import ui.ThemeApp
import java.awt.Cursor
import javax.swing.UIManager

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TerminalView(onCloseTerminal: () -> Unit){

    // State to track hover effect on the close terminal button
    val hoverCloseTerminal = remember { mutableStateOf(false) }

    // State that stores the height of the terminal
    val terminalHeight = remember { mutableStateOf(300f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(terminalHeight.value.dp)
            .background(ThemeApp.colors.secondColor)
    ) {
        Column{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    "Terminal",
                    fontFamily = ThemeApp.text.fontFamily,
                    fontSize = 14.sp,
                    color = ThemeApp.colors.textColor
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .height(25.dp)
                        .width(30.dp)
                        .background(
                            if (hoverCloseTerminal.value) ThemeApp.colors.background else Color.Transparent,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .onPointerEvent(PointerEventType.Enter) { hoverCloseTerminal.value = true }
                        .onPointerEvent(PointerEventType.Exit) { hoverCloseTerminal.value = false }
                        .clickable { onCloseTerminal() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Close icon",
                        tint = ThemeApp.colors.textColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

            }

            Spacer(modifier = Modifier.height(5.dp))

            SwingPanel(
                background = ThemeApp.colors.secondColor,
                modifier = Modifier.fillMaxSize(),
                factory = {
                    try {
                        UIManager.setLookAndFeel(FlatOneDarkIJTheme())
                        UIManager.put("ScrollBar.width", 1)
                        UIManager.put("ScrollBar.track", java.awt.Color(0x1E2229))
                        UIManager.put("ScrollBar.thumb", java.awt.Color(0x1E2229))
                    } catch (e: Exception){
                        println(e.message)
                    }

                    JediTermWidget(
                        80,
                        24,
                        TerminalSettingsProvider()
                    ).apply {
                        ttyConnector = createTtyConnector()
                        start()
                    }
                }
            )

        }

        Box(
            modifier = Modifier
                .background(ThemeApp.colors.background)
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.TopCenter)
                .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR)))
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { terminalHeight.value += -it }
                )
        )

    }
}