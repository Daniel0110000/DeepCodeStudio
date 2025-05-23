package com.dr10.editor.di

import com.dr10.editor.ui.viewModels.EditorTabViewModel
import com.dr10.editor.ui.viewModels.TabsViewModel
import org.koin.dsl.module

/**
 * Define the [editorModule] for dependency injection
 */
val editorModule = module {
    single { TabsViewModel() }
    single { EditorTabViewModel(get()) }
}