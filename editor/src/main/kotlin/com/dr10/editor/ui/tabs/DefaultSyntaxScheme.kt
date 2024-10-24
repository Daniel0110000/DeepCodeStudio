package com.dr10.editor.ui.tabs

import com.dr10.common.utilities.ColorUtils
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.Constants
import org.fife.ui.rsyntaxtextarea.SyntaxScheme
import org.fife.ui.rsyntaxtextarea.Token

/**
 * Sets up the default color scheme for syntax highlighting in the code editor
 */
fun SyntaxScheme.setDefaultSyntaxScheme() {
    getStyle(Token.NULL).foreground = ColorUtils.hexToColor(Constants.DEFAULT_SIMPLE_COLOR).toAWTColor()
    getStyle(Token.COMMENT_EOL).foreground = ColorUtils.hexToColor(Constants.COMMENT_COLOR).toAWTColor()
    getStyle(Token.COMMENT_KEYWORD).foreground = ColorUtils.hexToColor(Constants.COMMENT_COLOR).toAWTColor()
    getStyle(Token.RESERVED_WORD).foreground = ColorUtils.hexToColor(Constants.RESERVED_WORD_COLOR).toAWTColor()
    getStyle(Token.RESERVED_WORD_2).foreground = ColorUtils.hexToColor(Constants.REVERSED_WORD_2_COLOR).toAWTColor()
    getStyle(Token.LITERAL_NUMBER_HEXADECIMAL).foreground = ColorUtils.hexToColor(Constants.HEX_COLOR).toAWTColor()
    getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = ColorUtils.hexToColor(Constants.NUMBER_COLOR).toAWTColor()
    getStyle(Token.LITERAL_NUMBER_FLOAT).foreground = ColorUtils.hexToColor(Constants.NUMBER_COLOR).toAWTColor()
    getStyle(Token.FUNCTION).foreground = ColorUtils.hexToColor(Constants.FUNCTION_COLOR).toAWTColor()
    getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = ColorUtils.hexToColor(Constants.STRING_COLOR).toAWTColor()
    getStyle(Token.DATA_TYPE).foreground = ColorUtils.hexToColor(Constants.DATA_TYPE_COLOR).toAWTColor()
    getStyle(Token.VARIABLE).foreground = ColorUtils.hexToColor(Constants.VARIABLE_COLOR).toAWTColor()
    getStyle(Token.OPERATOR).foreground = ColorUtils.hexToColor(Constants.OPERATOR_COLOR).toAWTColor()
    getStyle(Token.PREPROCESSOR).foreground = ColorUtils.hexToColor(Constants.PREPROCESSOR_COLOR).toAWTColor()
}