package com.dr10.editor.ui.tabs.utilities

import com.dr10.common.models.SelectedConfigHistoryModel
import com.dr10.common.utilities.ColorUtils
import org.fife.ui.rsyntaxtextarea.SyntaxScheme
import org.fife.ui.rsyntaxtextarea.Token

/**
 * Sets up the default color scheme for syntax highlighting in the code editor
 */
fun SyntaxScheme.setSyntaxSchemeColor(model: SelectedConfigHistoryModel) {
    getStyle(Token.NULL).foreground = ColorUtils.stringToColor(model.simpleColor)
    getStyle(Token.COMMENT_EOL).foreground = ColorUtils.stringToColor(model.commentColor)
    getStyle(Token.COMMENT_KEYWORD).foreground = ColorUtils.stringToColor(model.commentColor)
    getStyle(Token.RESERVED_WORD).foreground = ColorUtils.stringToColor(model.reservedWordColor)
    getStyle(Token.RESERVED_WORD_2).foreground = ColorUtils.stringToColor(model.reservedWord2Color)
    getStyle(Token.LITERAL_NUMBER_HEXADECIMAL).foreground = ColorUtils.stringToColor(model.hexadecimalColor)
    getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = ColorUtils.stringToColor(model.numberColor)
    getStyle(Token.LITERAL_NUMBER_FLOAT).foreground = ColorUtils.stringToColor(model.numberColor)
    getStyle(Token.FUNCTION).foreground = ColorUtils.stringToColor(model.functionColor)
    getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = ColorUtils.stringToColor(model.stringColor)
    getStyle(Token.DATA_TYPE).foreground = ColorUtils.stringToColor(model.dataTypeColor)
    getStyle(Token.VARIABLE).foreground = ColorUtils.stringToColor(model.variableColor)
    getStyle(Token.OPERATOR).foreground = ColorUtils.stringToColor(model.operatorColor)
    getStyle(Token.PREPROCESSOR).foreground = ColorUtils.stringToColor(model.processorColor)
}