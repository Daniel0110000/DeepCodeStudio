import com.dr10.editor.ui.viewModels.TabsViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.viewModels.CodeEditorViewModel
import ui.viewModels.FileTreeViewModel

/**
 * A [KoinComponent] class for provided the dependency injection
 */
class App: KoinComponent {
    // Inject [CodeEditorViewModel]
    val codeEditorViewModel: CodeEditorViewModel by inject()

    // Inject [FileTreeViewModel]
    val fileTreeViewModel: FileTreeViewModel by inject()

    // Inject [TabsViewModel]
    val tabsViewModel: TabsViewModel by inject()
}