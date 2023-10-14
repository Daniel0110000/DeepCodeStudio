package ui.fileTree.lazy

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import ui.ThemeApp
import ui.fileTree.FileInfo
import ui.fileTree.FileOptionsMenu
import java.awt.event.MouseEvent

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun FileTreeItemView(
    model: FileInfo,
    selectedItem: FileInfo?,
    onClickListener: () -> Unit,
    onSelectedItemClickListener: () -> Unit
) {

    var dropDownExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(if(selectedItem == model) ThemeApp.colors.buttonColor else Color.Transparent, shape = RoundedCornerShape(5.dp))
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(start = 24.dp * model.depthLevel)
                .fillMaxWidth()
                .onClick{ onSelectedItemClickListener() }
                .onClick(
                    matcher = PointerMatcher.mouse(PointerButton.Secondary),
                    onClick = { dropDownExpanded = true }
                )
                .onPointerEvent(PointerEventType.Press){
                       when(it.awtEventOrNull?.button){
                           MouseEvent.BUTTON1 -> when (it.awtEventOrNull?.clickCount){
                               2 -> { onClickListener() }
                           }
                       }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            FileItemIcon(model)

            Spacer(modifier = Modifier.width(5.dp))

            FileItemText(model, Modifier.align(Alignment.CenterVertically))

            FileOptionsMenu(
                model = model,
                dropDownExpanded = dropDownExpanded,
                dismissDropDown = { dropDownExpanded = false }
            )
        }
    }
}