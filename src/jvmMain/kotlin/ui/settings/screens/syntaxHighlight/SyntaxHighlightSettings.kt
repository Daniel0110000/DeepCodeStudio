package ui.settings.screens.syntaxHighlight

import App
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ui.ThemeApp
import ui.editor.EditorVisualTransformationPreviewColors
import ui.settings.lazy.SyntaxKeywordHighlighterConfigItem

@Composable
fun SyntaxHighlightSettings(modifier: Modifier) {

    // Inject [SyntaxHighlightViewModel]
    val viewModel = App().syntaxHighlightSettingsViewModel

    // Observe the list of all syntax highlight configurations
    val configs = viewModel.allSyntaxHighlightConfigs.observeAsState()
    // Observe the selected option index
    val selectedOptionIndex = viewModel.selectedOptionIndex.observeAsState()
    //Observe the state of the color options list expansion
    val isExpandColorOptionsList = viewModel.isExpandColorOptionsList.observeAsState()
    // Observe the code text form the view model
    val codeText = viewModel.codeText.observeAsState()

    Column(modifier.padding(8.dp)) {

        Box(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, SolidColor(ThemeApp.colors.hoverTab), RoundedCornerShape(0.dp))
        ) {

            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(8.dp)
            ) {
                configs.value.forEachIndexed { index, config ->

                    SyntaxKeywordHighlighterConfigItem(
                        config,
                        viewModel,
                        onUpdateConfigs = { viewModel.updateSyntaxHighlightConfigs() },
                        index,
                        isExpandColorOptionsList.value[index],
                        selectedOptionIndex.value,
                        onSelectedOptionIndexChanged = { viewModel.updateSelectedIndex(it) }
                    )
                }
            }

            VerticalScrollbar(
                ScrollbarAdapter(scrollState),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                style = ThemeApp.scrollbar.scrollbarStyle
            )

        }

        Spacer(modifier = Modifier.height(15.dp))

        if(configs.value.isNotEmpty()){
            Text(
                configs.value[selectedOptionIndex.value].optionName,
                color = ThemeApp.colors.textColor,
                fontFamily = ThemeApp.text.fontFamily,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(ThemeApp.colors.secondColor)
                    .border(1.dp, SolidColor(ThemeApp.colors.hoverTab), RoundedCornerShape(0.dp))
            ) {
                BasicTextField(
                    value = codeText.value,
                    onValueChange = { viewModel.updateCodeTex(it) },
                    textStyle = TextStyle(
                        fontSize = 13.sp,
                        color = ThemeApp.colors.textColor,
                        fontFamily = ThemeApp.text.codeTextFontFamily,
                        fontWeight = FontWeight.W500
                    ),
                    cursorBrush = SolidColor(ThemeApp.colors.buttonColor),
                    visualTransformation = EditorVisualTransformationPreviewColors(configs.value[selectedOptionIndex.value]),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 10.dp, horizontal = 20.dp)
                )
            }
        }
    }
}