package com.dr10.database.data.mappers

import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.database.data.room.SyntaxAndSuggestionsEntity

/**
 * Converts [SyntaxAndSuggestionModel] to [SyntaxAndSuggestionsEntity]
 *
 * @return The converted [SyntaxAndSuggestionsEntity]
 */
fun SyntaxAndSuggestionModel.toEntity(): SyntaxAndSuggestionsEntity = SyntaxAndSuggestionsEntity(
    uniqueId = uniqueId,
    optionName = optionName,
    className = className,
    jsonPath = jsonPath
)