package com.dr10.editor.di

import com.dr10.database.domain.repositories.EditorRepository
import com.dr10.database.domain.repositories.SyntaxAndSuggestionsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * A [KoinComponent] class for provided the dependency inject to Editor module
 */
class Inject: KoinComponent {
    val syntaxAndSuggestionsRepository: SyntaxAndSuggestionsRepository by inject()
    val editorRepository: EditorRepository by inject()
}