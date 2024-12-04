package com.dr10.settings.ui.screens.syntaxAndSuggestions.components

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils
import com.dr10.common.utilities.Constants
import org.fife.ui.rsyntaxtextarea.SyntaxScheme
import org.fife.ui.rsyntaxtextarea.Token

/**
 * Configures the default syntax highlighting color scheme for JSON files
 */
fun SyntaxScheme.toJsonDefaultSyntaxScheme() {
    getStyle(Token.VARIABLE).foreground = ColorUtils.stringToColor(Constants.DEFAULT_FUNCTION_COLOR)
    getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = ColorUtils.stringToColor(Constants.DEFAULT_STRING_COLOR)
    getStyle(Token.IDENTIFIER).foreground = ThemeApp.awtColors.textColor
    getStyle(Token.SEPARATOR).foreground = ThemeApp.awtColors.textColor
    getStyle(Token.LITERAL_NUMBER_FLOAT).foreground = ColorUtils.stringToColor(Constants.DEFAULT_NUMBER_COLOR)
    getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = ColorUtils.stringToColor(Constants.DEFAULT_NUMBER_COLOR)
    getStyle(Token.COMMENT_EOL).foreground = ColorUtils.stringToColor(Constants.DEFAULT_COMMENT_COLOR)
}