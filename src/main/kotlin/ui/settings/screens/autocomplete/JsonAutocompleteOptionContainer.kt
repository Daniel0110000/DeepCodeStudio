package ui.settings.screens.autocomplete

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import domain.model.AutocompleteOptionModel
import domain.utilies.DocumentsManager
import domain.utilies.JsonChooser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.ThemeApp
import ui.components.TooltipArea
import ui.viewModels.settings.AutocompleteSettingsViewModel
import java.awt.Cursor
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JsonAutocompleteOptionContainer(
    model: AutocompleteOptionModel,
    viewModel: AutocompleteSettingsViewModel,
    onDeleteOptionClick: () -> Unit,
    onUpdateJsonPathClick: (String) -> Unit
){

    // Observes the value of [jsonAutocompleteOptionContainerWidth]
    val width = viewModel.jsonAutocompleteOptionContainerWidth.observeAsState().value

    Box(
        modifier = Modifier
            .width(width.dp)
            .fillMaxHeight()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ThemeApp.colors.secondColor)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painterResource("images/ic_json.svg"),
                    contentDescription = "Json Icon",
                    tint = ThemeApp.colors.textColor,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    model.optionName,
                    color = ThemeApp.colors.textColor,
                    fontSize = 13.sp,
                    fontFamily = ThemeApp.text.fontFamily
                )

                Spacer(modifier = Modifier.weight(1f))

                TooltipArea("Choose Json"){
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch { onUpdateJsonPathClick(JsonChooser.chooseJson() ?: "") }
                        },
                        modifier = Modifier.height(28.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = ThemeApp.colors.secondColor)
                    ){
                        Text(
                            "Browse",
                            color = ThemeApp.colors.textColor,
                            fontSize = 10.sp,
                            fontFamily = ThemeApp.text.fontFamily
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                TooltipArea("Delete Option"){
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Icon close",
                        tint = ThemeApp.colors.textColor,
                        modifier = Modifier
                            .size(18.dp)
                            .onClick { onDeleteOptionClick() }
                            .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))
                    )
                }

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(10.dp)
                    .border(1.dp, SolidColor(ThemeApp.colors.hoverTab), RoundedCornerShape(0.dp))
                    .verticalScroll(rememberScrollState())
            ){
                // If the JSON file exists, its content is displayed
                if(DocumentsManager.existsFile(model.jsonPath)){
                    Text(
                        File(model.jsonPath).readText(),
                        color = ThemeApp.colors.textColor,
                        fontFamily = ThemeApp.text.fontFamily,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }

        }

        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(ThemeApp.colors.hoverTab)
                .align(Alignment.CenterStart)
                .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)))
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { viewModel.setJsonAutocompleteOptionContainerWidth(it) }
                )
        )

    }

}