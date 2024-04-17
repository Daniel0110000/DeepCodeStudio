package com.dr10.settings.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.onClick
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr10.common.ui.ThemeApp
import com.dr10.settings.ui.viewModels.SettingsViewModel
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import java.awt.Cursor

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun SettingOptions(viewModel: SettingsViewModel) {
    val selectedItem = viewModel.selectedItem.observeAsState().value
    val width = viewModel.settingsOptionsWidth.observeAsState().value

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(width.dp)
            .background(ThemeApp.colors.secondColor)
    ){
        LazyColumn(modifier = Modifier.fillMaxSize()) {

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

        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(ThemeApp.colors.hoverTab)
                .align(Alignment.CenterEnd)
                .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)))
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { viewModel.setSettingsOptionsWidth(it) }
                )
        )

    }
}