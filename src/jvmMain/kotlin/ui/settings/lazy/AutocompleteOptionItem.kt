package ui.settings.lazy

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.onClick
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.util.JsonChooser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.ThemeApp
import java.awt.Cursor
import java.io.File

/**
 * Composable function to display an autocomplete option
 *
 * @param optionName The name of the autocomplete options
 * @param jsonPath The JSON path associated with the option
 * @param onDeleteOptionClick Callback function when the "Delete" button is clicked
 * @param onUpdateJsonPathClick  Callback function when the "Browse" button is clicked
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutocompleteOptionItem(
    optionName: String,
    jsonPath: String,
    onDeleteOptionClick: () -> Unit,
    onUpdateJsonPathClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                optionName,
                color = ThemeApp.colors.textColor,
                fontSize = 13.sp,
                fontFamily = ThemeApp.text.fontFamily
            )

            Spacer(modifier = Modifier.weight(1f))

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

            Spacer(modifier = Modifier.width(10.dp))

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

        // Display the JSON path
        Text(
            jsonPath,
            color = ThemeApp.colors.textColor,
            fontSize = 9.sp,
            fontFamily = ThemeApp.text.fontFamily
        )


        Spacer(modifier = Modifier.height(5.dp))

        // Display JSOn content with scrolling capabilities
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ThemeApp.colors.secondColor)
                .heightIn(5.dp, 200.dp)
                .verticalScroll(rememberScrollState())
        ){
            Text(
                File(jsonPath).readText(),
                color = ThemeApp.colors.textColor,
                fontFamily = ThemeApp.text.fontFamily,
                fontSize = 13.sp,
                modifier = Modifier.padding(5.dp)
            )
        }

    }
}