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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.ThemeApp
import ui.settings.screens.syntaxHighlight.ColorOptionItem
import ui.viewModels.settings.SyntaxHighlightSettingsViewModel
import java.awt.event.MouseEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SyntaxKeywordHighlighterConfigItem(
    config: SyntaxHighlightConfigModel,
    viewModel: SyntaxHighlightSettingsViewModel,
    onUpdateConfigs: () -> Unit,
    index: Int,
    isExpandColorOptions: Boolean,
    selectedOptionIndex: Int,
    onSelectedOptionIndexChanged: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onPointerEvent(PointerEventType.Press){
                    when(it.awtEventOrNull?.button){
                        MouseEvent.BUTTON1 -> when (it.awtEventOrNull?.clickCount){
                            1 -> { onSelectedOptionIndexChanged(index) }
                            2 -> { viewModel.updateIsExpanded(index, !isExpandColorOptions) }
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
            var simpleDisplaySaveColors by remember { mutableStateOf(false) }
            var instructionDisplaySaveColors by remember { mutableStateOf(false) }
            var variableDisplaySaveColors by remember { mutableStateOf(false) }
            var constantDisplaySaveColors by remember { mutableStateOf(false) }
            var segmentDisplaySaveColors by remember { mutableStateOf(false) }
            var systemCallDisplaySaveColors by remember { mutableStateOf(false) }
            var registerDisplaySaveColors by remember { mutableStateOf(false) }
            var arithmeticInstructionDisplaySaveColors by remember { mutableStateOf(false) }
            var logicalInstructionDisplaySaveColors by remember { mutableStateOf(false) }
            var conditionDisplaySaveColors by remember { mutableStateOf(false) }
            var loopDisplaySaveColors by remember { mutableStateOf(false) }
            var memoryManagementDisplaySaveColors by remember { mutableStateOf(false) }
            var numberDisplaySaveColors by remember { mutableStateOf(false) }
            var commentDisplaySaveColors by remember { mutableStateOf(false) }
            var stringDisplaySaveColors by remember { mutableStateOf(false) }
            var labelDisplaySaveColors by remember { mutableStateOf(false) }

            var newSimpleColor by remember { mutableStateOf("") }
            var newInstructionColor by remember { mutableStateOf("") }
            var newVariableColor by remember { mutableStateOf("") }
            var newConstantColor by remember { mutableStateOf("") }
            var newSegmentColor by remember { mutableStateOf("") }
            var newSystemCallColor by remember { mutableStateOf("") }
            var newRegisterColor by remember { mutableStateOf("") }
            var newArithmeticInstructionColor by remember { mutableStateOf("") }
            var newLogicalInstructionColor by remember { mutableStateOf("") }
            var newConditionColor by remember { mutableStateOf("") }
            var newLoopColor by remember { mutableStateOf("") }
            var newMemoryManagementColor by remember { mutableStateOf("") }
            var newNumberColor by remember { mutableStateOf("") }
            var newCommentColor by remember { mutableStateOf("") }
            var newStringColor by remember { mutableStateOf("") }
            var newLabelColor by remember { mutableStateOf("") }

            ColorOptionItem(
                "Simple",
                config.simpleColor
            ) {
                newSimpleColor = it
                simpleDisplaySaveColors = it != config.simpleColor
            }

            ColorOptionItem(
                "Instruction",
                config.instructionColor
            ) {
                newInstructionColor = it
                instructionDisplaySaveColors = it != config.instructionColor
            }

            ColorOptionItem(
                "Variable",
                config.variableColor
            ) {
                newVariableColor = it
                variableDisplaySaveColors = it != config.variableColor
            }

            ColorOptionItem(
                "Constant",
                config.constantColor
            ) {
                newConstantColor = it
                constantDisplaySaveColors = it != config.constantColor
            }

            ColorOptionItem(
                "Segment",
                config.segmentColor
            ) {
                newSegmentColor = it
                segmentDisplaySaveColors = it != config.segmentColor
            }

            ColorOptionItem(
                "System Call",
                config.systemCallColor
            ) {
                newSystemCallColor = it
                systemCallDisplaySaveColors = it != config.systemCallColor
            }

            ColorOptionItem(
                "Register",
                config.registerColor
            ) {
                newRegisterColor = it
                registerDisplaySaveColors = it != config.registerColor
            }

            ColorOptionItem(
                "Arithmetic Instruction",
                config.arithmeticInstructionColor
            ) {
                newArithmeticInstructionColor = it
                arithmeticInstructionDisplaySaveColors = it != config.arithmeticInstructionColor
            }

            ColorOptionItem(
                "Logical Instruction",
                config.logicalInstructionColor
            ) {
                newLogicalInstructionColor = it
                logicalInstructionDisplaySaveColors = it != config.logicalInstructionColor
            }

            ColorOptionItem(
                "Condition",
                config.conditionColor
            ) {
                newConditionColor = it
                conditionDisplaySaveColors = it != config.conditionColor
            }

            ColorOptionItem(
                "Loop",
                config.loopColor
            ) {
                newLoopColor = it
                loopDisplaySaveColors = it != config.loopColor
            }

            ColorOptionItem(
                "Memory Management",
                config.memoryManagementColor
            ) {
                newMemoryManagementColor = it
                memoryManagementDisplaySaveColors = it != config.memoryManagementColor
            }

            ColorOptionItem(
                "Number",
                config.numberColor
            ) {
                newNumberColor = it
                numberDisplaySaveColors = it != config.numberColor
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

            if(simpleDisplaySaveColors || instructionDisplaySaveColors || variableDisplaySaveColors ||
                constantDisplaySaveColors || segmentDisplaySaveColors || systemCallDisplaySaveColors ||
                registerDisplaySaveColors || arithmeticInstructionDisplaySaveColors || logicalInstructionDisplaySaveColors ||
                conditionDisplaySaveColors || loopDisplaySaveColors || memoryManagementDisplaySaveColors ||
                numberDisplaySaveColors || commentDisplaySaveColors || stringDisplaySaveColors ||
                labelDisplaySaveColors){
                // Display "Save" button if there are unsaved color changes
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel.updateSyntaxHighlightConfig(
                                    SyntaxHighlightConfigModel(
                                        id = config.id,
                                        jsonPath = config.jsonPath,
                                        simpleColor = newSimpleColor,
                                        instructionColor = newInstructionColor,
                                        variableColor = newVariableColor,
                                        constantColor = newConstantColor,
                                        numberColor = newNumberColor,
                                        segmentColor = newSegmentColor,
                                        systemCallColor = newSystemCallColor,
                                        registerColor = newRegisterColor,
                                        arithmeticInstructionColor = newArithmeticInstructionColor,
                                        logicalInstructionColor = newLogicalInstructionColor,
                                        conditionColor = newConditionColor,
                                        loopColor = newLoopColor,
                                        memoryManagementColor = newMemoryManagementColor,
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