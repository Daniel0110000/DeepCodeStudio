package com.dr10.settings.ui.lazy

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.dr10.common.models.AutocompleteOptionModel
import com.dr10.common.ui.ThemeApp

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun AutocompleteOptionItem(
    model: AutocompleteOptionModel,
    selectedOption: String,
    onClick: () -> Unit
){
    var isHover by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .background(if(selectedOption == model.uuid) ThemeApp.colors.buttonColor else if(isHover) ThemeApp.colors.hoverTab else Color.Transparent, shape = RoundedCornerShape(5.dp))
            .onPointerEvent(PointerEventType.Enter){ isHover = true }
            .onPointerEvent(PointerEventType.Exit){ isHover = false }
            .onClick { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ){

        Spacer(modifier = Modifier.width(5.dp))

        Icon(
            painterResource("images/ic_json.svg"),
            contentDescription = "Json Icon",
            tint = ThemeApp.colors.textColor,
            modifier = Modifier.size(18.dp)
        )

        Column {
            Text(
                model.optionName,
                color = ThemeApp.colors.textColor,
                fontFamily = ThemeApp.text.fontFamily,
                fontSize = 13.sp,
                modifier = Modifier.padding(start = 5.dp)
            )

            Text(
                model.jsonPath,
                color = ThemeApp.colors.textColor,
                fontFamily = ThemeApp.text.fontFamily,
                fontSize = 9.sp,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}