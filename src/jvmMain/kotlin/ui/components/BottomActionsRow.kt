package ui.components

import App
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.onClick
import androidx.compose.material.Icon
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
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.repositories.AutocompleteSettingsRepository
import ui.ThemeApp
import ui.editor.EditorState

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun BottomActionsRow(editorState: EditorState) {

    val repository: AutocompleteSettingsRepository = App().autocompleteSettingsRepository

    var isHoveringAutocompleteOption by remember { mutableStateOf(false) }
    var isHoverReadOnlyButton by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(25.dp)
            .background(ThemeApp.colors.secondColor)
    ){
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .onPointerEvent(PointerEventType.Enter){ isHoveringAutocompleteOption = true }
                .onPointerEvent(PointerEventType.Exit){ isHoveringAutocompleteOption = false }
                .background(if(isHoveringAutocompleteOption) ThemeApp.colors.hoverTab else Color.Transparent)
                .onClick {
                    editorState.displayUpdateAutocompleteOption.value = !editorState.displayUpdateAutocompleteOption.value
                },
            contentAlignment = Alignment.Center
        ){
            Text(
                repository.getSelectedAutocompleteOption(editorState.filePath.value).optionName,
                color = ThemeApp.colors.textColor,
                fontFamily = ThemeApp.text.fontFamily,
                fontSize = 11.sp,
                modifier = Modifier.padding(5.dp)
            )
        }

        TooltipArea(if(editorState.readOnly.value) "File Writable" else "Read Only"){
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(25.dp)
                    .onPointerEvent(PointerEventType.Enter){ isHoverReadOnlyButton = true }
                    .onPointerEvent(PointerEventType.Exit){ isHoverReadOnlyButton = false }
                    .background(if(isHoverReadOnlyButton) ThemeApp.colors.hoverTab else Color.Transparent)
                    .onClick { editorState.readOnly.value = !editorState.readOnly.value },
                contentAlignment = Alignment.Center
            ){
                Icon(
                    if(editorState.readOnly.value) painterResource("images/ic_lock.svg") else painterResource("images/ic_lock_open.svg"),
                    contentDescription = "Lock icon",
                    tint = ThemeApp.colors.textColor,
                    modifier = Modifier.size(15.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

    }
}