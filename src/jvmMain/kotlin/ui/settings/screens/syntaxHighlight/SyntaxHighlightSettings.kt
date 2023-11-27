package ui.settings.screens.syntaxHighlight

import App
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.ThemeApp
import ui.editor.EditorVisualTransformation
import ui.settings.lazy.SyntaxKeywordHighlighterConfigItem
import ui.viewModels.settings.SettingsViewModel
import java.io.File

@Composable
fun SyntaxHighlightSettings(
    modifier: Modifier,
    settingsViewModel: SettingsViewModel,
    onErrorOccurred: (String) -> Unit
) {

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
                    // If the JSON file does not exist in the specified path, the [onErrorOccurred] callback is called,
                    // ... and the option is removed from the database
                    if(!File(config.jsonPath).exists()){
                        CoroutineScope(Dispatchers.IO).launch {
                            onErrorOccurred("JSON file not found at the specified path '${config.jsonPath}'")
                            viewModel.deleteConfig(config.uuid)
                            viewModel.updateSyntaxHighlightConfigs()
                        }
                    } else {
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
                    visualTransformation = EditorVisualTransformation(
                        configs.value[selectedOptionIndex.value],
                        viewModel,
                        settingsViewModel
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 10.dp, horizontal = 20.dp)
                )
            }
        }
    }
}