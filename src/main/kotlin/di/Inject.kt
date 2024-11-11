package di

import com.dr10.editor.ui.viewModels.TabsViewModel
import com.dr10.terminal.ui.viewModel.TerminalViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.viewModels.CodeEditorViewModel
import ui.viewModels.FileTreeViewModel

/**
 * A [KoinComponent] class for provided the dependency injection
 */
class Inject: KoinComponent {
    val terminalViewModel: TerminalViewModel by inject()
    val codeEditorViewModel: CodeEditorViewModel by inject()
    val fileTreeViewModel: FileTreeViewModel by inject()
    val tabsViewModel: TabsViewModel by inject()
}