package di

import org.koin.dsl.module
import ui.viewModels.CodeEditorViewModel
import ui.viewModels.FileTreeViewModel

/**
 * Define the [appModule] for dependency injection
 */
val appModule = module {
    single { CodeEditorViewModel() }
    single { CodeEditorViewModel() }
    single { FileTreeViewModel(get()) }
}