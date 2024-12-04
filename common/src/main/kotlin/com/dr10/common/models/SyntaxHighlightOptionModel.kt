package com.dr10.common.models

import com.dr10.common.utilities.Constants

data class SyntaxHighlightOptionModel(
    val uuid: String = "",
    val optionName: String = "",
    val jsonPath: String = "",
    val simpleColor: String = Constants.DEFAULT_SIMPLE_COLOR,
    val commentColor: String = Constants.DEFAULT_COMMENT_COLOR,
    val reservedWordColor: String = Constants.DEFAULT_RESERVED_WORD_COLOR,
    val reservedWord2Color: String = Constants.DEFAULT_RESERVED_WORD_2_COLOR,
    val hexadecimalColor: String = Constants.DEFAULT_HEX_COLOR,
    val numberColor: String = Constants.DEFAULT_NUMBER_COLOR,
    val functionColor: String = Constants.DEFAULT_FUNCTION_COLOR,
    val stringColor: String = Constants.DEFAULT_STRING_COLOR,
    val dataTypeColor: String = Constants.DEFAULT_DATA_TYPE_COLOR,
    val variableColor: String = Constants.DEFAULT_VARIABLE_COLOR,
    val operatorColor: String = Constants.DEFAULT_OPERATOR_COLOR,
    val processorColor: String = Constants.DEFAULT_PROCESSOR_COLOR
)