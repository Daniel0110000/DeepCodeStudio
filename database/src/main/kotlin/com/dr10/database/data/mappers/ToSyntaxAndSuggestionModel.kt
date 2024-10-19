package com.dr10.database.data.mappers

import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.database.data.room.SyntaxAndSuggestionsEntity

/**
 * Convert [SyntaxAndSuggestionsEntity] model to a [SyntaxAndSuggestionModel] model
 *
 * @return The converted [SyntaxAndSuggestionModel] model
 */
fun SyntaxAndSuggestionsEntity.toModel(): SyntaxAndSuggestionModel = SyntaxAndSuggestionModel(
    uniqueId = uniqueId,
    optionName = optionName,
    className = className,
    jsonPath = jsonPath
)