package domain.model

import domain.util.Constants

data class SyntaxHighlightConfigModel(
    val id: Int = 0,
    val optionName: String = "",
    val jsonPath: String,
    val keywordColor: String = Constants.DEFAULT_KEYWORD_COLOR,
    val variableColor: String = Constants.DEFAULT_VARIABLE_COLOR,
    val numberColor: String = Constants.DEFAULT_NUMBER_COLOR,
    val sectionColor: String = Constants.DEFAULT_SECTION_COLOR,
    val commentColor: String = Constants.DEFAULT_COMMENT_COLOR,
    val stringColor: String = Constants.DEFAULT_STRING_COLOR,
    val labelColor: String = Constants.DEFAULT_LABEL_COLOR
)