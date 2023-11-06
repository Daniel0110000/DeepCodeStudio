import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.viewModels.settings.SyntaxHighlightSettingsViewModel

/**
 * A [KoinComponent] class for provided the dependency injection
 */
class App: KoinComponent {
    // Inject [SyntaxHighlightViewModel]
    val syntaxHighlightSettingsViewModel: SyntaxHighlightSettingsViewModel by inject()
}