package com.dr10.database.data.mappers

import app.deepCodeStudio.database.SyntaxHighlightOptions
import com.dr10.common.models.SyntaxHighlightOptionModel

/**
 * Converts a [SyntaxHighlightOptions] model to a [SyntaxHighlightOptionModel] object
 *
 * @return The converted [SyntaxHighlightOptionModel]
 */
fun SyntaxHighlightOptions.toModel(): SyntaxHighlightOptionModel = SyntaxHighlightOptionModel(
    uuid = uuid,
    optionName = optionName,
    jsonPath = jsonPath,
    simpleColor = simpleColor,
    instructionColor = instructionColor,
    variableColor =  variableColor,
    constantColor = constantColor,
    numberColor = numberColor,
    segmentColor = segmentColor,
    systemCallColor = systemCallColor,
    registerColor = registerColor,
    arithmeticInstructionColor = arithmeticInstructionColor,
    logicalInstructionColor = logicalInstructionColor,
    conditionColor = conditionColor,
    loopColor = loopColor,
    memoryManagementColor = memoryManagementColor,
    commentColor = commentColor,
    stringColor = stringColor,
    labelColor = labelColor
)