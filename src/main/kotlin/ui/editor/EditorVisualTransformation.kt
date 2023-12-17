package ui.editor

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import domain.model.SyntaxHighlightConfigModel
import domain.model.SyntaxHighlightRegexModel

class EditorVisualTransformation(
    private val colors: SyntaxHighlightConfigModel,
    private val model: SyntaxHighlightRegexModel
): VisualTransformation {
    /**
     * Apply the syntax keyword highlighting to the input text
     *
     * @param text The input to be transformed
     * @return TransformedText containing the modified text and offset mapping
     */
    override fun filter(text: AnnotatedString): TransformedText = TransformedText(
        text = SyntaxKeywordHighlighter.codeString(text.text, colors, model),
        offsetMapping = OffsetMapping.Identity
    )
}