package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.utilies.DirectoryChooser
import kotlinx.coroutines.launch
import ui.ThemeApp

@Composable
fun VerticalBarOptions(
    isCollapseFileTree: Boolean,
    isOpenTerminal: Boolean,
    isOpenSettings: Boolean,
    newDirectoryPath: (String?) -> Unit,
    collapseOrExtendSplitPane: () -> Unit,
    openTerminal: () -> Unit,
    openSettings: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(45.dp)
            .background(ThemeApp.colors.secondColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Spacer(modifier = Modifier.height(10.dp))

        VerticalBarOption(
            label = if(isCollapseFileTree) "Extend" else "Collapse",
            icon = if(isCollapseFileTree) painterResource("images/ic_extend.svg") else painterResource("images/ic_collapse.svg"),
            isSelected = !isCollapseFileTree,
        ){ collapseOrExtendSplitPane() }

        Spacer(modifier = Modifier.height(5.dp))

        VerticalBarOption(
            label = "Choose Folder",
            icon = painterResource("images/ic_folder.svg")
        ){ coroutineScope.launch { newDirectoryPath(DirectoryChooser.chooseDirectory()) } }

        Spacer(modifier = Modifier.weight(1f))

        VerticalBarOption(
            label = "Terminal",
            icon = painterResource("images/ic_terminal.svg"),
            isSelected = isOpenTerminal
        ){ openTerminal() }

        Spacer(modifier = Modifier.height(10.dp))

        VerticalBarOption(
            label = "Settings",
            icon = painterResource("images/ic_settings.svg"),
            isSelected = isOpenSettings
        ){ openSettings() }

        Spacer(modifier = Modifier.height(10.dp))
    }
}