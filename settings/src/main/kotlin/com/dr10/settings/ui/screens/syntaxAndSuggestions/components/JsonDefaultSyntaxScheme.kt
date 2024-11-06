package com.dr10.settings.ui.screens.syntaxAndSuggestions.components

import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.Constants
import org.fife.ui.rsyntaxtextarea.SyntaxScheme
import org.fife.ui.rsyntaxtextarea.Token

/**
 * Configures the default syntax highlighting color scheme for JSON files
 */
fun SyntaxScheme.toJsonDefaultSyntaxScheme() {
    getStyle(Token.VARIABLE).foreground = ColorUtils.hexToColor(Constants.FUNCTION_COLOR).toAWTColor()
    getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = ColorUtils.hexToColor(Constants.STRING_COLOR).toAWTColor()
    getStyle(Token.IDENTIFIER).foreground = ThemeApp.awtColors.textColor
    getStyle(Token.SEPARATOR).foreground = ThemeApp.awtColors.textColor
    getStyle(Token.LITERAL_NUMBER_FLOAT).foreground = ColorUtils.hexToColor(Constants.NUMBER_COLOR).toAWTColor()
    getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = ColorUtils.hexToColor(Constants.NUMBER_COLOR).toAWTColor()
    getStyle(Token.COMMENT_EOL).foreground = ColorUtils.hexToColor(Constants.COMMENT_COLOR).toAWTColor()
}