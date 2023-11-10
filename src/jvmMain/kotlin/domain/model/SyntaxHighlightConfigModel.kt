package domain.model

import domain.util.Constants

data class SyntaxHighlightConfigModel(
    val uuid: String = "",
    val optionName: String = "",
    val jsonPath: String = "",
    val simpleColor: String = Constants.DEFAULT_SIMPLE_COLOR,
    val instructionColor: String = Constants.DEFAULT_INSTRUCTION_COLOR,
    val variableColor: String = Constants.DEFAULT_VARIABLE_COLOR,
    val constantColor: String = Constants.DEFAULT_CONSTANT_COLOR,
    val numberColor: String = Constants.DEFAULT_NUMBER_COLOR,
    val segmentColor: String = Constants.DEFAULT_SEGMENT_COLOR,
    val systemCallColor: String = Constants.DEFAULT_SYSTEM_CALL_COLOR,
    val registerColor: String = Constants.DEFAULT_REGISTER_COLOR,
    val arithmeticInstructionColor: String = Constants.DEFAULT_ARITHMETIC_INSTRUCTION_COLOR,
    val logicalInstructionColor: String = Constants.DEFAULT_LOGICAl_INSTRUCTION_COLOR,
    val conditionColor: String = Constants.DEFAULT_CONDITION_COLOR,
    val loopColor: String = Constants.DEFAULT_LOOP_COLOR,
    val memoryManagementColor: String = Constants.DEFAULT_MEMORY_MANAGEMENT,
    val commentColor: String = Constants.DEFAULT_COMMENT_COLOR,
    val stringColor: String = Constants.DEFAULT_STRING_COLOR,
    val labelColor: String = Constants.DEFAULT_LABEL_COLOR
)