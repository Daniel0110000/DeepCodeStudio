package ui.editor.codeAutoCompletion

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.ThemeApp

/**
 * Composable function to display an auto-complete dropdown
 *
 * @param items List of auto-complete suggestions
 * @param cursorX X-coordinate of the cursor position
 * @param cursorY Y-coordinate of the cursor position
 * @param selectedItemIndex Index of the currently selected item
 */
@Composable
fun AutoCompleteDropdown(
    items: List<String>,
    cursorX: Int,
    cursorY: Int,
    selectedItemIndex: Int
) = Box(
    modifier = Modifier
        .absoluteOffset { IntOffset(cursorX, cursorY) }
        .background(ThemeApp.colors.secondColor)
        .width(200.dp)
        .heightIn(0.dp, 150.dp)
) {

    // Create scroll state for the dropdown list
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(end = 10.dp, start = 5.dp, bottom = 5.dp, top = 5.dp)
    ) {
        // Iterate through the auto-complete suggestions
        items.forEachIndexed { index, suggestion ->
            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if(selectedItemIndex == index) ThemeApp.colors.background else Color.Transparent),
                verticalAlignment = Alignment.CenterVertically
            ){

                Icon(
                    painterResource("images/ic_cube.svg"),
                    contentDescription = "Cube icon",
                    tint = if(selectedItemIndex == index) ThemeApp.colors.textColor else ThemeApp.colors.buttonColor,
                    modifier = Modifier.size(15.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    suggestion,
                    modifier = Modifier
                        .padding(3.dp),
                    color = ThemeApp.colors.textColor,
                    fontSize = 13.sp,
                    fontFamily = ThemeApp.text.fontFamily
                )
            }

        }
    }

    // Display a vertical scrollbar if there are more than 4 items
    if(items.size > 4){
        VerticalScrollbar(
            ScrollbarAdapter(scrollState),
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            style = defaultScrollbarStyle().copy(
                unhoverColor = ThemeApp.colors.buttonColor,
                hoverColor = ThemeApp.colors.buttonColor,
                thickness = 5.dp
            )
        )
    }

    // Automatically scroll to the selected item when it changes
    LaunchedEffect(selectedItemIndex){ scrollState.scrollTo(selectedItemIndex * 25) }

}