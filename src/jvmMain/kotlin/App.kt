import domain.repositories.AutocompleteSettingsRepository
import domain.repositories.SyntaxHighlightSettingsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.viewModels.CodeEditorViewModel
import ui.viewModels.editor.EditorViewModel
import ui.viewModels.editor.TabsViewModel
import ui.viewModels.settings.AutocompleteSettingsViewModel
import ui.viewModels.settings.SettingsViewModel
import ui.viewModels.settings.SyntaxHighlightSettingsViewModel
import ui.viewModels.splitPane.SplitPaneViewModel

/**
 * A [KoinComponent] class for provided the dependency injection
 */
class App: KoinComponent {

    // Inject [AutocompleteSettingsRepository]
    val autocompleteSettingsRepository: AutocompleteSettingsRepository by inject()

    // Inject [SyntaxHighlightSettingsRepository]
    val syntaxHighlightSettingsRepository: SyntaxHighlightSettingsRepository by inject()

    // Inject [SyntaxHighlightViewModel]
    val syntaxHighlightSettingsViewModel: SyntaxHighlightSettingsViewModel by inject()

    // Inject [AutocompleteSettingsViewModel]
    val autocompleteSettingsViewModel: AutocompleteSettingsViewModel by inject()

    // Inject [CodeEditorViewModel]
    val codeEditorViewModel: CodeEditorViewModel by inject()

    // Inject [TabsViewModel[
    val tabsViewModel: TabsViewModel by inject()

    // Inject [EditorViewModel]
    val editorViewModel: EditorViewModel by inject()

    // Inject [SettingsViewModel]
    val settingsViewModel: SettingsViewModel by inject()

    // Inject [SplitPaneViewModel]
    val splitPaneViewModel: SplitPaneViewModel by inject()
}