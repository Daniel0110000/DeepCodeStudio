package com.dr10.database.data.mappers

import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.database.data.room.SyntaxAndSuggestionsEntity

/**
 * Convert [SyntaxAndSuggestionModel] model to a [SyntaxAndSuggestionsEntity] model
 *
 * @return The converted [SyntaxAndSuggestionsEntity] model
 */
fun SyntaxAndSuggestionModel.toEntity(): SyntaxAndSuggestionsEntity = SyntaxAndSuggestionsEntity(
    uniqueId = uniqueId,
    optionName = optionName,
    className = className,
    jsonPath = jsonPath
)