package com.dr10.database.data.mappers

import com.dr10.common.models.ColorSchemeModel
import com.dr10.database.data.room.entities.ColorSchemesEntity

fun ColorSchemeModel.toEntity(): ColorSchemesEntity = ColorSchemesEntity(
    uniqueId = this.uniqueId,
    simpleColor = this.simpleColor,
    commentColor = this.commentColor,
    reservedWordColor = this.reservedWordColor,
    reservedWord2Color = this.reservedWord2Color,
    hexadecimalColor = this.hexadecimalColor,
    numberColor = this.numberColor,
    functionColor = this.functionColor,
    stringColor = this.stringColor,
    dataTypeColor = this.dataTypeColor,
    variableColor = this.dataTypeColor,
    operatorColor = this.operatorColor,
    processorColor = this.processorColor
)