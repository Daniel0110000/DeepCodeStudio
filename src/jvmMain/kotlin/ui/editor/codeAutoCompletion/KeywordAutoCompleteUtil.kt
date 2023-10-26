package ui.editor.codeAutoCompletion

object KeywordAutoCompleteUtil {

//    /**
//     * Generates auto-complete suggestions for keywords based on teh user's input
//     *
//     * @param input The user's input for auto-completion
//     * @return A list of auto-complete suggestions matching the input
//     */

    /**
     * Generates autocomplete suggestions for keywords based on the user's input
     *
     * @param input The user's input for autocomplete
     * @param keywords List of keywords for filtering for autocomplete.
     * @return A list of autocomplete suggestions matching the input
     */
    fun autocompleteKeywords(input: String, keywords: List<String>): List<String> =
        if(input.isBlank()) emptyList() else keywords.filter { it.contains(input) }

    /**
     * Filters a list of variable names to provide autocomplete suggestions based on user input.
     *
     * @param variableNames The list of variable names to filter
     * @param input The input text provided by the user for filtering
     * @return A list of variable names that match the user's input
     */
    fun filterVariableNamesForAutocomplete(variableNames: List<String>, input: String): List<String> =
        if(input.isBlank()) emptyList() else variableNames.filter { it.contains(input) }

    /**
     * Filters a list of a function names to provide autocomplete suggestions based on user input
     *
     * @param functionNames The list of function names to filter
     * @param input The input text provided by the user for filtering
     * @return A list of function names that match the user's input
     */
    fun filterFunctionNamesForAutocomplete(functionNames: List<String>, input: String): List<String> =
        if(input.isBlank()) emptyList() else functionNames.filter { it.contains(input) }
}