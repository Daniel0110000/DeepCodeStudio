import com.dr10.database.domain.repositories.AutocompleteSettingsRepository
import com.dr10.editor.ui.viewModels.TabsViewModel
import com.dr10.settings.ui.viewModels.AutocompleteSettingsViewModel
import com.dr10.settings.ui.viewModels.SettingsViewModel
import com.dr10.settings.ui.viewModels.SyntaxHighlightSettingsViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.viewModels.CodeEditorViewModel
import ui.viewModels.FileTreeViewModel

/**
 * A [KoinComponent] class for provided the dependency injection
 */
class App: KoinComponent {

    // Inject [AutocompleteSettingsRepository]
    val autocompleteSettingsRepository: AutocompleteSettingsRepository by inject()

    // Inject [SyntaxHighlightSettingsViewModel]
    val syntaxHighlightSettingsViewModel: SyntaxHighlightSettingsViewModel by inject()

    // Inject [AutocompleteSettingsViewModel
    val autocompleteSettingsViewModel: AutocompleteSettingsViewModel by inject()

    // Inject [SettingsViewModel]
    val settingsViewModel: SettingsViewModel by inject()

    // Inject [CodeEditorViewModel]
    val codeEditorViewModel: CodeEditorViewModel by inject()

    // Inject [FileTreeViewModel]
    val fileTreeViewModel: FileTreeViewModel by inject()

    // Inject [TabsViewModel]
    val tabsViewModel: TabsViewModel by inject()

}