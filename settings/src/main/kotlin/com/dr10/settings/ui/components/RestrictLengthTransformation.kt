package com.dr10.settings.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * A custom VisualTransformation to restrict the length of an input text.
 * This transformation trims the text to a maximum length of 7 characters.
 */
class RestrictLengthTransformation : VisualTransformation {

    /**
     * Filters the input text and returns a TransformedText with restricted length
     *
     * @param text The input text to be transformed
     * @return TransformedText with a maximum length of 7 characters
     */
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if(text.text.length > 7){
            text.text.substring(0, 7)
        } else text.text
        return TransformedText(AnnotatedString(trimmed), OffsetMapping.Identity)
    }
}