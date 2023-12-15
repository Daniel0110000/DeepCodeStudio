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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import domain.utilies.DocumentsManager
import domain.utilies.JsonUtils
import ui.ThemeApp
import ui.components.EmptyMessage
import ui.editor.EditorVisualTransformation
import ui.settings.SettingsErrorState
import ui.settings.lazy.SyntaxKeywordHighlighterConfigItem
import ui.viewModels.settings.SyntaxHighlightSettingsViewModel

@Composable
fun SyntaxHighlightSettingsScreen(
    modifier: Modifier,
    settingsErrorState: SettingsErrorState
) {

    val textFieldFocusRequester = mutableStateOf(FocusRequester())

    // Inject [SyntaxHighlightViewModel]
    val viewModel: SyntaxHighlightSettingsViewModel = App().syntaxHighlightSettingsViewModel

    // Observe the list of all syntax highlight configurations
    val configs = viewModel.allSyntaxHighlightConfigs.observeAsState().value
    // Observe the selected option index
    val selectedOptionIndex = viewModel.selectedOptionIndex.observeAsState().value
    //Observe the state of the color options list expansion
    val isExpandColorOptionsList = viewModel.isExpandColorOptionsList.observeAsState().value
    // Observe the code text form the view model
    val codeText = viewModel.codeText.observeAsState()

    // LaunchedEffect to request focus for the TextField when the view is created
    LaunchedEffect(Unit){ textFieldFocusRequester.value.requestFocus() }

    Column(modifier.padding(8.dp)) {

        Box(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, SolidColor(ThemeApp.colors.hoverTab), RoundedCornerShape(0.dp))
        ) {

            val scrollState = rememberScrollState()

            // If [configs] is not empty, the configurations are displayed
            if(configs.isNotEmpty()){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(8.dp)
                ) {
                    configs.forEachIndexed { index, config ->
                        // If the JSON file exists, [SyntaxKeywordHighlighterConfigItem] is displayed
                        if(DocumentsManager.existsFile(config.jsonPath)){
                            SyntaxKeywordHighlighterConfigItem(
                                config,
                                viewModel,
                                onUpdateConfigs = { viewModel.updateSyntaxHighlightConfigs() },
                                index,
                                isExpandColorOptionsList[index],
                                selectedOptionIndex,
                                onSelectedOptionIndexChanged = { viewModel.updateSelectedIndex(it) }
                            )
                        } else {
                            // If the JSON file does not exist, update the data in [EditorErrorState] to handle this erro
                            settingsErrorState.uuid.value = config.uuid
                            settingsErrorState.displayErrorMessage.value = true
                            settingsErrorState.errorDescription.value = "JSON file not found at the specified path '${config.jsonPath}'"
                        }
                    }
                }
            } else EmptyMessage() // If [configs] is empty, [EmptyMessage] is displayed

            VerticalScrollbar(
                ScrollbarAdapter(scrollState),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                style = ThemeApp.scrollbar.scrollbarStyle
            )

        }

        Spacer(modifier = Modifier.height(15.dp))

        if(configs.isNotEmpty()){
            settingsErrorState.uuid.value = configs[selectedOptionIndex].uuid
            Text(
                configs[selectedOptionIndex].optionName,
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
                        configs[selectedOptionIndex],
                        JsonUtils.jsonToSyntaxHighlightRegexModel(configs[selectedOptionIndex].jsonPath, settingsErrorState = settingsErrorState)
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 10.dp, horizontal = 20.dp)
                        .focusRequester(textFieldFocusRequester.value)
                )
            }
        }
    }
}