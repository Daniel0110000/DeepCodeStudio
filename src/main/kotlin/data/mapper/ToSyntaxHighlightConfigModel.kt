package data.mapper

import dev.daniel.database.SyntaxHighlightTable
import domain.model.SyntaxHighlightConfigModel

fun SyntaxHighlightTable.toSyntaxHighlightConfigModel(): SyntaxHighlightConfigModel =
    SyntaxHighlightConfigModel(
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