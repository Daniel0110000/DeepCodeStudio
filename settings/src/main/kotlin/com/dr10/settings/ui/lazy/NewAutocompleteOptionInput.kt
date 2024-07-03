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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.TooltipArea
import com.dr10.common.utilities.JsonChooser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.skiko.Cursor

/**
 * Composable function for the input area to add a new autocomplete option
 *
 * @param onAddOptionClick Callback function when the "Add" button is clicked
 * @param onOptionNameChange Callback function for changes in the option name input
 * @param onJsonPathSelection Callback function when a JSON path is selected
 * @param optionName Current value of the option name input
 * @param jsonPath Current value of the JSON path
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun NewAutocompleteOptionInput(
    optionName: String,
    jsonPath: String,
    onAddOptionClick: () -> Unit,
    onOptionNameChange: (String) -> Unit,
    onJsonPathSelection: (String) -> Unit,
){
    var isHover by remember { mutableStateOf(false) }
    val textFieldFocusRequester by remember { mutableStateOf(FocusRequester())}

    LaunchedEffect(Unit) { textFieldFocusRequester.requestFocus() }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Option Name",
                color = ThemeApp.colors.textColor
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .height(35.dp)
                    .width(300.dp)
                    .background(ThemeApp.colors.secondColor, shape = RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = optionName,
                    onValueChange = { onOptionNameChange(it) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 10.dp, horizontal = 5.dp)
                        .focusRequester(textFieldFocusRequester),
                    cursorBrush = SolidColor(ThemeApp.colors.buttonColor),
                    textStyle = TextStyle.Default.copy(
                        color = ThemeApp.colors.textColor,
                        fontSize = 12.sp,
                        fontFamily = ThemeApp.text.fontFamily
                    )
                )
            }

        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Location",
                color = ThemeApp.colors.textColor,
                fontSize = 15.sp,
                fontFamily = ThemeApp.text.fontFamily
            )

            Spacer(modifier = Modifier.width(8.dp))

            ConstraintLayout(
                modifier = Modifier
                    .height(35.dp)
                    .weight(1f)
                    .background(ThemeApp.colors.secondColor, shape = RoundedCornerShape(5.dp))
            ) {

                val (confPath, btnChooseFile) = createRefs()

                Text(
                    jsonPath,
                    color = ThemeApp.colors.textColor,
                    fontSize = 13.sp,
                    fontFamily = ThemeApp.text.fontFamily,
                    modifier = Modifier
                        .constrainAs(confPath) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start, margin = 10.dp)
                        }
                )

                TooltipArea(
                    "Choose Json",
                    modifier = Modifier
                        .constrainAs(btnChooseFile) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end, margin = 10.dp)
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(
                                if(isHover) ThemeApp.colors.hoverTab
                                else Color.Transparent,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .onPointerEvent(PointerEventType.Enter) { isHover = true }
                            .onPointerEvent(PointerEventType.Exit) { isHover = false }
                            .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))
                            .onClick { CoroutineScope(Dispatchers.Default).launch { onJsonPathSelection(JsonChooser.chooseJson() ?: "") } },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource("images/ic_folder.svg"),
                            contentDescription = "Choose Json",
                            tint = ThemeApp.colors.textColor,
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            TooltipArea("Add Option") {
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .background(ThemeApp.colors.buttonColor, shape = RoundedCornerShape(5.dp))
                        .onClick { onAddOptionClick() },
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "Add option",
                        tint = ThemeApp.colors.textColor,
                        modifier = Modifier
                            .size(23.dp)
                    )
                }
            }
        }
    }
}