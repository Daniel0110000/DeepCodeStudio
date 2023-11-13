package ui.settings.screens.syntaxHighlight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.utilies.ColorUtils
import ui.ThemeApp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColorOptionItem(
    title: String,
    currentColor: String,
    onColorChange: (String) -> Unit
) {

    // Contains the newly selected color
    var selectedColor by remember { mutableStateOf(currentColor) }
    // A boolean variable to track the hover state
    var isHover by remember { mutableStateOf(false) }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(if(isHover) ThemeApp.colors.hoverTab else Color.Transparent, shape = RoundedCornerShape(5.dp))
            .onPointerEvent(PointerEventType.Enter){ isHover = true }
            .onPointerEvent(PointerEventType.Exit){ isHover = false },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            color = ThemeApp.colors.textColor,
            fontFamily = ThemeApp.text.fontFamily,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 30.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .height(20.dp)
                .width(100.dp)
                .background(
                    // Check if the selected color is a valid hexadecimal color
                    if(ColorUtils.isHexadecimalColor(selectedColor) && selectedColor.length == 7){
                        // Invoke the onColorChange callback and apply the selected color
                        onColorChange(selectedColor)
                        ColorUtils.hexToColor(selectedColor)
                    } else ThemeApp.code.simple.color,
                    shape = RoundedCornerShape(4.dp)
                )
        )

        Spacer(modifier = Modifier.width(5.dp))

        // Allow the user to input a new color value
        Box(
            modifier = Modifier
                .background(ThemeApp.colors.secondColor, shape = RoundedCornerShape(5.dp))
                .width(70.dp)
                .height(25.dp),
            contentAlignment = Alignment.Center
        ){
            BasicTextField(
                value = selectedColor,
                onValueChange = { if(it.length <= 7) selectedColor = it.uppercase() },
                singleLine = true,
                textStyle = TextStyle.Default.copy(
                    color = ThemeApp.colors.textColor,
                    fontFamily = ThemeApp.text.fontFamily,
                    fontSize = 13.sp
                ),
                cursorBrush = SolidColor(ThemeApp.colors.buttonColor),
                modifier = Modifier.padding(horizontal = 5.dp),
                visualTransformation = RestrictLengthTransformation()
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

    }
}