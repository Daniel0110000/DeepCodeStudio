package ui.settings.screens.syntaxHighlight

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.repository.SettingRepository
import org.koin.java.KoinJavaComponent
import ui.ThemeApp
import ui.editor.EditorVisualTransformation
import ui.settings.lazy.SyntaxKeywordHighlighterConfigItem

@Composable
fun SyntaxHighlightSettings(modifier: Modifier) {

    // Index of the selected item
    var selectedOptionIndex by remember { mutableStateOf(0) }

    // Inject [SettingRepository] using Koin
    val settingRepository: SettingRepository by KoinJavaComponent.inject(SettingRepository::class.java)

    // All syntax highlight configurations
    var allSyntaxHighlightConfigs by remember {
        mutableStateOf(settingRepository.getAllSyntaxHighlightConfigs())
    }

    Column(modifier.padding(8.dp)) {

        var codeText by remember { mutableStateOf("") }

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
                allSyntaxHighlightConfigs.forEachIndexed { index, config ->

                    SyntaxKeywordHighlighterConfigItem(
                        config,
                        settingRepository,
                        onUpdateConfigs = {
                            // Retrieve all configurations to display the modified colors again
                            allSyntaxHighlightConfigs = settingRepository.getAllSyntaxHighlightConfigs()
                        },
                        index,
                        selectedOptionIndex,
                        onSelectedOptionIndexChanged = { selectedOptionIndex = it }
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(ThemeApp.colors.secondColor)
                .border(1.dp, SolidColor(ThemeApp.colors.hoverTab), RoundedCornerShape(0.dp))
        ) {
            BasicTextField(
                value = codeText,
                onValueChange = { codeText = it },
                textStyle = TextStyle(
                    fontSize = 13.sp,
                    color = ThemeApp.colors.textColor,
                    fontFamily = ThemeApp.text.codeTextFontFamily,
                    fontWeight = FontWeight.W500
                ),
                cursorBrush = SolidColor(ThemeApp.colors.buttonColor),
                visualTransformation = EditorVisualTransformation(),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            )
        }
    }
}