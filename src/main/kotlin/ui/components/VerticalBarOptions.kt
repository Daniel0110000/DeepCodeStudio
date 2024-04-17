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
import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.DirectoryChooser
import kotlinx.coroutines.launch

/**
 * Composable function to display a vertical bar containing various options
 *
 * @param isCollapseFileTree Flag indicating whether the file tree is collapsed
 * @param isOpenTerminal Flag indicating whether the terminal is open
 * @param isOpenSettings Flag indicating whether the settings are open
 * @param newDirectoryPath Callback function to be executed when a new directory is chosen
 * @param collapseOrExtendSplitPane Callback function to be executed when the split pane is collapsed or extended
 * @param openTerminal Callback function to be executed when the terminal option is clicked
 * @param openSettings Callback function to be executed when the settings option is clicked
 */
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