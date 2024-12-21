package com.dr10.settings.ui.viewModels

import com.dr10.common.models.NotificationData
import com.dr10.common.models.RegexRuleModel
import com.dr10.common.models.SyntaxAndSuggestionModel
import com.dr10.common.ui.notification.NotificationType
import com.dr10.common.utilities.ErrorType
import com.dr10.common.utilities.RegexUtils
import com.dr10.database.domain.repositories.RegexRulesRepository
import com.dr10.database.domain.repositories.SyntaxAndSuggestionsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegexRulesViewModel(
    private val syntaxAndSuggestionRepository: SyntaxAndSuggestionsRepository,
    private val regexRulesRepository: RegexRulesRepository
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _state = MutableStateFlow(RegexRulesState())
    val state: StateFlow<RegexRulesState> = _state.asStateFlow()

    /**
     * Data class for the state of the code extraction screen
     *
     * @property allConfigs A list of all the syntax and suggestion configurations
     */
    data class RegexRulesState(
        val allConfigs: List<SyntaxAndSuggestionModel> = emptyList(),
        val regexRules: List<RegexRuleModel> = emptyList(),
        val selectedConfigUniqueId: String = "",
        val notificationData: NotificationData? = null,
        val clearFields: Boolean = false
    )

    init { retrieveConfigs() }

    /**
     * Retrieves all the syntax and suggestion configurations
     */
    private fun retrieveConfigs() {
        scope.launch {
            syntaxAndSuggestionRepository.getConfigs().collect { configs ->
                val newState = _state.value.copy(
                    allConfigs = configs,
                    selectedConfigUniqueId = configs.firstOrNull()?.uniqueId ?: ""
                )
                _state.emit(newState)
                retrieveRegexRulesByUniqueId(newState.selectedConfigUniqueId)
            }
        }
    }

    /**
     * Inserts a new regex rule to the database
     *
     * @param name The name of the regex rule
     * @param pattern The pattern of the regex
     */
    fun addRegexRule(name: String, pattern: String) {
        validateAndInsert(name, pattern) {
            scope.launch {
                regexRulesRepository.insertRegexRule(
                    RegexRuleModel(
                        uniqueId = _state.value.selectedConfigUniqueId,
                        regexName = name,
                        regexPattern = pattern
                    )
                )
                retrieveRegexRulesByUniqueId(_state.value.selectedConfigUniqueId)
            }
        }
        clearFields(true)
    }

    /**
     * Validates if the name and pattern are not empty and if the pattern is valid
     *
     * @param name The name of the regex rule
     * @param pattern The pattern of the regex
     * @param onValid The function to be executed if the validation is successful
     */
    private fun validateAndInsert(name: String, pattern: String, onValid: () -> Unit) {
        when {
            name.isBlank() || pattern.isBlank() -> showNotification(errorType = ErrorType.REGEX_NAME_OR_REGEX_EMPTY)
            !RegexUtils.isValidPattern(pattern).first -> showNotification(message = RegexUtils.isValidPattern(pattern).second)
            else -> onValid()
        }
    }

    /**
     * Deletes a regex rule from the database and retrieves the regex rules again
     *
     * @param model The regex rule to be deleted
     */
    fun deleteRegexRule(model: RegexRuleModel) {
        scope.launch {
            regexRulesRepository.deleteRegexRule(model)
            retrieveRegexRulesByUniqueId(_state.value.selectedConfigUniqueId)
        }
    }


    /**
     * Sets the selected option in the state and retrieves the regex rules using the unique id
     *
     * @param uniqueId The unique id of the selected option
     */
    fun setSelectedOption(uniqueId: String) {
        retrieveRegexRulesByUniqueId(uniqueId)
        updateState { copy(selectedConfigUniqueId = uniqueId) }
    }

    /**
     * Retrieves the regex rules from the database using the unique id
     *
     * @param uniqueId The unique id of the selected option
     */
    private fun retrieveRegexRulesByUniqueId(uniqueId: String) {
        scope.launch {
            val rules = regexRulesRepository.getRegexRulesByUniqueId(uniqueId)
            updateState { copy(regexRules = rules) }
        }
    }

    /**
     * Shows a notification with the given message and error type
     *
     * @param message The message to be shown
     * @param errorType The type of error to be shown
     */
    private fun showNotification(message: String = "", errorType: ErrorType = ErrorType.CUSTOM) {
        updateState {
            copy(notificationData = NotificationData(
                message = message,
                type = NotificationType.ERROR,
                isAutoDismiss = true,
                errorType = errorType
            ))
        }
    }

    fun clearNotificationData() { updateState { copy(notificationData = null) } }

    fun clearFields(value: Boolean) {
        updateState { copy(clearFields = value) }
    }

    private fun updateState(update: RegexRulesState.() -> RegexRulesState) { _state.update(update) }

}