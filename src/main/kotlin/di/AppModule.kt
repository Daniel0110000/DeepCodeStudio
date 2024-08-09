package di

import org.koin.dsl.module
import ui.viewModels.CodeEditorViewModel
import ui.viewModels.splitPane.SplitPaneViewModel

/**
 * Define the [appModule] for dependency injection
 */
val appModule = module {
    single { CodeEditorViewModel() }
    single { SplitPaneViewModel() }
    single { CodeEditorViewModel() }
}