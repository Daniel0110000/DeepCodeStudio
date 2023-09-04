package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ui.splitPane.SplitPane
import ui.splitPane.SplitPaneState

@Composable
fun CodeEditorScreen() {

    val splitPaneState = remember { SplitPaneState(System.getProperty("user.home")) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
    ) {
        SplitPane(splitPaneState)
    }
}