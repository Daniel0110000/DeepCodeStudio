package com.dr10.database.data.mappers

import com.dr10.common.models.ColorSchemeModel
import com.dr10.database.data.room.relations.ColorSchemeRelation

fun ColorSchemeRelation.toModel(): ColorSchemeModel = ColorSchemeModel(
    uniqueId = this.syntaxAndSuggestionsEntity.uniqueId,
    optionName = this.syntaxAndSuggestionsEntity.optionName,
    className = this.syntaxAndSuggestionsEntity.className,
    jsonPath = this.syntaxAndSuggestionsEntity.jsonPath,
    simpleColor = this.colorSchemesEntity.simpleColor,
    commentColor = this.colorSchemesEntity.commentColor,
    reservedWordColor = this.colorSchemesEntity.reservedWordColor,
    reservedWord2Color = this.colorSchemesEntity.reservedWord2Color,
    hexadecimalColor = this.colorSchemesEntity.hexadecimalColor,
    numberColor = this.colorSchemesEntity.numberColor,
    functionColor = this.colorSchemesEntity.functionColor,
    stringColor = this.colorSchemesEntity.stringColor,
    dataTypeColor = this.colorSchemesEntity.dataTypeColor,
    variableColor = this.colorSchemesEntity.variableColor,
    operatorColor = this.colorSchemesEntity.operatorColor,
    processorColor = this.colorSchemesEntity.processorColor
)