package ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.SelectedAutocompleteOptionModel
import domain.repository.SettingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.ThemeApp

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun bottomActionsRow(
    repository: SettingRepository,
    filePath: String,
    onUpdateSelectedOption: () -> Unit
) {
    var isHoveringAutocompleteOption by remember { mutableStateOf(false) }
    var isDisplayingOptions by remember { mutableStateOf(false) }

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
                .onClick { isDisplayingOptions = true },
            contentAlignment = Alignment.Center
        ){
            Text(
                repository.getSelectedAutocompleteOption(filePath).optionName,
                color = ThemeApp.colors.textColor,
                fontFamily = ThemeApp.text.fontFamily,
                fontSize = 11.sp,
                modifier = Modifier.padding(5.dp)
            )

            // If [isDisplayingOptions] is true, display a DropdownMenu containing all options
            if(isDisplayingOptions){
                MaterialTheme(
                    colors = MaterialTheme.colors.copy(surface = ThemeApp.colors.secondColor),
                    shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(0.dp))
                ){
                    DropdownMenu(
                        expanded = true,
                        onDismissRequest = { isDisplayingOptions = false },
                        offset = DpOffset((-5).dp, 0.dp)
                    ){
                        repository.getAllAutocompleteOptions().forEach { option ->
                            var isHoveringOption by remember { mutableStateOf(false) }
                            Row(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .background(if(isHoveringOption) ThemeApp.colors.buttonColor else Color.Transparent, shape = RoundedCornerShape(5.dp))
                                    .onPointerEvent(PointerEventType.Enter){ isHoveringOption = true }
                                    .onPointerEvent(PointerEventType.Exit){ isHoveringOption = false }
                                    .onClick {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            // Update the selected autocomplete option for the current file
                                            repository.updateSelectedAutocompleteOption(SelectedAutocompleteOptionModel(
                                                uuid = option.uuid,
                                                asmFilePath = filePath,
                                                optionName = option.optionName,
                                                jsonPath = option.jsonPath
                                            ))
                                            onUpdateSelectedOption()
                                            isDisplayingOptions = false
                                        }
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Spacer(modifier = Modifier.width(5.dp))

                                Icon(
                                    painterResource("images/ic_asm.svg"),
                                    contentDescription = "ASM icon",
                                    tint = ThemeApp.colors.asmIconColor,
                                    modifier = Modifier.size(15.dp)
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                    option.optionName,
                                    color = ThemeApp.colors.textColor,
                                    fontFamily = ThemeApp.text.fontFamily,
                                    fontSize = 12.sp
                                )

                                Spacer(modifier = Modifier.width(5.dp))
                            }
                        }
                    }
                }
            }

        }

        Spacer(modifier = Modifier.width(10.dp))

    }
}