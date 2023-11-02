package ui.settings.lazy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.SyntaxHighlightConfigModel
import domain.repository.SettingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.ThemeApp
import ui.settings.screens.syntaxHighlight.ColorOptionItem
import java.awt.event.MouseEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SyntaxKeywordHighlighterConfigItem(
    config: SyntaxHighlightConfigModel,
    settingRepository: SettingRepository,
    onUpdateConfigs: () -> Unit,
    index: Int,
    selectedOptionIndex: Int,
    onSelectedOptionIndexChanged: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        // Manage expansion of color options
        var isExpandColorOptions by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onPointerEvent(PointerEventType.Press){
                    when(it.awtEventOrNull?.button){
                        MouseEvent.BUTTON1 -> when (it.awtEventOrNull?.clickCount){
                            1 -> { onSelectedOptionIndexChanged(index) }
                            2 -> { isExpandColorOptions = !isExpandColorOptions }
                        }
                    }
                }
                .background(if(index == selectedOptionIndex) ThemeApp.colors.buttonColor else Color.Transparent, shape = RoundedCornerShape(5.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isExpandColorOptions) Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowRight,
                contentDescription = "",
                tint = ThemeApp.colors.textColor,
                modifier = Modifier.size(20.dp)
            )

            Text(
                config.optionName,
                color = ThemeApp.colors.textColor,
                fontFamily = ThemeApp.text.fontFamily,
                fontSize = 13.sp
            )

        }

        if (isExpandColorOptions) {
            var keywordDisplaySaveColors by remember { mutableStateOf(false) }
            var variableDisplaySaveColors by remember { mutableStateOf(false) }
            var numberDisplaySaveColors by remember { mutableStateOf(false) }
            var sectionDisplaySaveColors by remember { mutableStateOf(false) }
            var commentDisplaySaveColors by remember { mutableStateOf(false) }
            var stringDisplaySaveColors by remember { mutableStateOf(false) }
            var labelDisplaySaveColors by remember { mutableStateOf(false) }

            var newKeywordColor by remember { mutableStateOf("") }
            var newVariableColor by remember { mutableStateOf("") }
            var newNumberColor by remember { mutableStateOf("") }
            var newSectionColor by remember { mutableStateOf("") }
            var newCommentColor by remember { mutableStateOf("") }
            var newStringColor by remember { mutableStateOf("") }
            var newLabelColor by remember { mutableStateOf("") }

            // Color option items for keyword, variable, number, section, comment, string, and label
            ColorOptionItem(
                "Keyword",
                config.keywordColor
            ) {
                newKeywordColor = it
                keywordDisplaySaveColors = it != config.keywordColor
            }

            ColorOptionItem(
                "Variable",
                config.variableColor
            ) {
                newVariableColor = it
                variableDisplaySaveColors = it != config.variableColor
            }

            ColorOptionItem(
                "Number",
                config.numberColor
            ) {
                newNumberColor = it
                numberDisplaySaveColors = it != config.numberColor
            }

            ColorOptionItem(
                "Section",
                config.sectionColor
            ) {
                newSectionColor = it
                sectionDisplaySaveColors = it != config.sectionColor
            }

            ColorOptionItem(
                "Comment",
                config.commentColor
            ) {
                newCommentColor = it
                commentDisplaySaveColors = it != config.commentColor
            }

            ColorOptionItem(
                "String",
                config.stringColor
            ) {
                newStringColor = it
                stringDisplaySaveColors = it != config.stringColor
            }

            ColorOptionItem(
                "Label",
                config.labelColor
            ) {
                newLabelColor = it
                labelDisplaySaveColors = it != config.labelColor
            }

            Spacer(modifier = Modifier.height(5.dp))

            if(keywordDisplaySaveColors || variableDisplaySaveColors ||
                numberDisplaySaveColors || sectionDisplaySaveColors ||
                commentDisplaySaveColors || stringDisplaySaveColors ||
                labelDisplaySaveColors){
                // Display "Save" button if there are unsaved color changes
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                settingRepository.updateSyntaxHighlightConfig(
                                    SyntaxHighlightConfigModel(
                                        jsonPath = config.jsonPath,
                                        keywordColor = newKeywordColor,
                                        variableColor = newVariableColor,
                                        numberColor = newNumberColor,
                                        sectionColor = newSectionColor,
                                        commentColor = newCommentColor,
                                        stringColor = newStringColor,
                                        labelColor = newLabelColor
                                    )
                                )

                                onUpdateConfigs()

                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = ThemeApp.colors.secondColor),
                        modifier = Modifier.height(28.dp)
                    ){
                        Text(
                            "Save",
                            color = ThemeApp.colors.textColor,
                            fontSize = 10.sp,
                            fontFamily = ThemeApp.text.fontFamily
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))
                }

                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}