package com.dr10.database.data.mappers

import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.database.data.room.entities.SyntaxAndSuggestionsEntity

fun SyntaxAndSuggestionsEntity.toModel(): SyntaxAndSuggestionModel = SyntaxAndSuggestionModel(
    uniqueId = this.uniqueId,
    optionName = this.optionName,
    className = this.className,
    jsonPath = this.jsonPath
)