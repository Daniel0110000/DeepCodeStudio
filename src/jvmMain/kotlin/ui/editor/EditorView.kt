package ui.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.ThemeApp
import ui.editor.tabs.TabsState
import ui.editor.tabs.TabsView
import java.io.File

@Composable
fun EditorView(
    tabsState: TabsState
) {

    // Create a mutable state variable to hold the code content
    var code by remember { mutableStateOf("") }

    var linesCounter by remember { mutableStateOf(0) }

    // If there are no open tabs, clear5 the code content
    if(tabsState.tabs.isEmpty()) code = ""

    Column(modifier = Modifier.fillMaxSize()) {

        TabsView(tabsState){ code = File(it).readText() }

        Spacer(modifier = Modifier.height(5.dp))

        Row(modifier = Modifier.fillMaxSize()) {

            LinesNumberView(linesCounter)

            Spacer(modifier = Modifier.width(10.dp))

            BasicTextField(
                value = code,
                onValueChange = { code = it },
                modifier = Modifier.fillMaxSize(),
                onTextLayout = {
                    val lineCount = it.lineCount
                    if(lineCount != linesCounter) linesCounter = lineCount
                },
                textStyle = TextStyle(
                    fontSize = 13.sp,
                    color = ThemeApp.colors.textColor,
                    fontFamily = FontFamily(Font(resource = "font/JetBrainsMonoItalic.ttf")),
                    fontWeight = FontWeight.W500
                )
            )
        }

    }
}