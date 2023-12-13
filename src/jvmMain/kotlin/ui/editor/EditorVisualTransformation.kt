package ui.editor

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import domain.model.SyntaxHighlightConfigModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.viewModels.settings.SettingsViewModel
import ui.viewModels.settings.SyntaxHighlightSettingsViewModel

class EditorVisualTransformation(
    private val colors: SyntaxHighlightConfigModel,
    private val syntaxHighlightViewModel: SyntaxHighlightSettingsViewModel? = null,
    private val settingsViewModel: SettingsViewModel? = null
): VisualTransformation {
    /**
     * Apply the syntax keyword highlighting to the input text
     *
     * @param text The input to be transformed
     * @return TransformedText containing the modified text and offset mapping
     */
    override fun filter(text: AnnotatedString): TransformedText = try {
        TransformedText(
            text = SyntaxKeywordHighlighter.codeString(text.text, colors),
            offsetMapping = OffsetMapping.Identity
        )
    } catch (e: Exception){
        println(e.message)
        CoroutineScope(Dispatchers.IO).launch {
            settingsViewModel?.setErrorDescription(e.message.toString())
            settingsViewModel?.setDisplayErrorMessage(true)
            syntaxHighlightViewModel?.deleteConfig(colors.uuid)
            syntaxHighlightViewModel?.updateSyntaxHighlightConfigs()
        }
        TransformedText(
            text = text,
            offsetMapping = OffsetMapping.Identity
        )
    }
}