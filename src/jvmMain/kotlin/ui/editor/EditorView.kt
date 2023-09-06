package ui.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
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

    // If there are not open tabs, clear5 the code content
    if(tabsState.tabs.isEmpty()) code = ""

    Column(modifier = Modifier.fillMaxSize()) {

        TabsView(tabsState){ code = File(it).readText() }

        BasicTextField(
            value = code,
            onValueChange = { code = it },
            modifier = Modifier.fillMaxSize(),
            textStyle = TextStyle(
                color = ThemeApp.colors.textColor
            )
        )

    }
}