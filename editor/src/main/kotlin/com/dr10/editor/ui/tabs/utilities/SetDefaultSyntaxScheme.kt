package com.dr10.editor.ui.tabs.utilities

import com.dr10.common.utilities.ColorUtils
import com.dr10.common.utilities.Constants
import org.fife.ui.rsyntaxtextarea.SyntaxScheme
import org.fife.ui.rsyntaxtextarea.Token

fun SyntaxScheme.setDefaultSyntaxScheme() {
    getStyle(Token.NULL).foreground = ColorUtils.stringToColor(Constants.DEFAULT_SIMPLE_COLOR)
    getStyle(Token.COMMENT_EOL).foreground = ColorUtils.stringToColor(Constants.DEFAULT_COMMENT_COLOR)
    getStyle(Token.COMMENT_KEYWORD).foreground = ColorUtils.stringToColor(Constants.DEFAULT_COMMENT_COLOR)
    getStyle(Token.RESERVED_WORD).foreground = ColorUtils.stringToColor(Constants.DEFAULT_RESERVED_WORD_COLOR)
    getStyle(Token.RESERVED_WORD_2).foreground = ColorUtils.stringToColor(Constants.DEFAULT_RESERVED_WORD_2_COLOR)
    getStyle(Token.LITERAL_NUMBER_HEXADECIMAL).foreground = ColorUtils.stringToColor(Constants.DEFAULT_HEX_COLOR)
    getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = ColorUtils.stringToColor(Constants.DEFAULT_NUMBER_COLOR)
    getStyle(Token.LITERAL_NUMBER_FLOAT).foreground = ColorUtils.stringToColor(Constants.DEFAULT_NUMBER_COLOR)
    getStyle(Token.FUNCTION).foreground = ColorUtils.stringToColor(Constants.DEFAULT_FUNCTION_COLOR)
    getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = ColorUtils.stringToColor(Constants.DEFAULT_STRING_COLOR)
    getStyle(Token.DATA_TYPE).foreground = ColorUtils.stringToColor(Constants.DEFAULT_DATA_TYPE_COLOR)
    getStyle(Token.VARIABLE).foreground = ColorUtils.stringToColor(Constants.DEFAULT_VARIABLE_COLOR)
    getStyle(Token.OPERATOR).foreground = ColorUtils.stringToColor(Constants.DEFAULT_OPERATOR_COLOR)
    getStyle(Token.PREPROCESSOR).foreground = ColorUtils.stringToColor(Constants.DEFAULT_PROCESSOR_COLOR)
}
