package ui.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.onClick
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ui.ThemeApp
import ui.viewModels.settings.SettingsViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun SettingsOptions(viewModel: SettingsViewModel) {
    val selectedItem = viewModel.selectedItem.observeAsState().value

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .width(220.dp)
            .background(ThemeApp.colors.secondColor)
    ) {

        item { Spacer(modifier = Modifier.height(8.dp)) }

        items(viewModel.options.value.size) {
            var hoverOption by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp)
                    .background(if(selectedItem == it) ThemeApp.colors.buttonColor else if(hoverOption) ThemeApp.colors.hoverTab else Color.Transparent)
                    .onPointerEvent(PointerEventType.Enter){ hoverOption = true }
                    .onPointerEvent(PointerEventType.Exit){ hoverOption = false }
                    .onClick {
                        viewModel.setSelectedItem(it)
                        viewModel.setScreen(viewModel.screens.value[it])
                    },
                contentAlignment = Alignment.Center
            ){
                Text(
                    viewModel.options.value[it],
                    color = ThemeApp.colors.textColor,
                    fontFamily = ThemeApp.text.fontFamily,
                    fontSize = 12.sp,
                )
            }
        }
    }
}