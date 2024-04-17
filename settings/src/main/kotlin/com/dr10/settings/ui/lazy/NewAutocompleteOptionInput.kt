package com.dr10.settings.ui.lazy

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.onClick
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.TooltipArea
import com.dr10.common.utilities.JsonChooser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.Cursor

/**
 * Composable function for the input area to add a new autocomplete option
 *
 * @param onAddOptionClick Callback function when the "Add" button is clicked
 * @param onOptionNameChange Callback function for changes in the option name input
 * @param onJsonPathSelection Callback function when a JSON path is selected
 * @param optionName Current value of the option name input
 * @param jsonPath Current value of the JSON path
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewAutocompleteOptionInput(
    optionName: String,
    jsonPath: String,
    onAddOptionClick: () -> Unit,
    onOptionNameChange: (String) -> Unit,
    onJsonPathSelection: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(28.dp)
                    .background(ThemeApp.colors.secondColor),
                contentAlignment = Alignment.Center
            ){
                BasicTextField(
                    value = optionName,
                    onValueChange = { onOptionNameChange(it) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(5.dp),
                    cursorBrush = SolidColor(ThemeApp.colors.buttonColor),
                    textStyle = TextStyle.Default.copy(
                        color = ThemeApp.colors.textColor,
                        fontSize = 12.sp,
                        fontFamily = ThemeApp.text.fontFamily
                    )
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            TooltipArea("Choose Json"){
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch { onJsonPathSelection(JsonChooser.chooseJson() ?: "") }
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

            Spacer(modifier = Modifier.width(5.dp))

            TooltipArea("Add Option"){
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "Icon add",
                    tint = ThemeApp.colors.textColor,
                    modifier = Modifier
                        .size(17.dp)
                        .onClick { onAddOptionClick() }
                        .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        // Displays the selected JSON path if [jsonPath] is not blank
        if(jsonPath.isNotBlank()){
            Text(
                jsonPath,
                color = ThemeApp.colors.textColor,
                fontSize = 10.sp,
                fontFamily = ThemeApp.text.fontFamily
            )
        }
    }
}