package com.dr10.settings.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.onClick
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.TooltipArea
import com.dr10.common.utilities.DocumentsManager
import com.dr10.settings.ui.viewModels.SyntaxAndSuggestionsViewModel
import java.awt.Cursor
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JsonAutocompleteOptionContainer(
    model: SyntaxAndSuggestionModel,
    viewModel: SyntaxAndSuggestionsViewModel,
    width: Float
){

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

                TooltipArea("Delete Option"){
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Icon close",
                        tint = ThemeApp.colors.textColor,
                        modifier = Modifier
                            .size(18.dp)
                            .onClick { viewModel.deleteConfig(model) }
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