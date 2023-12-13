package ui.editor.autocompleteCode

object KeywordAutoCompleteUtil {

    /**
     * Generates autocomplete suggestions for keywords based on the user's input
     *
     * @param input The user's input for autocomplete
     * @param keywords List of keywords for filtering for autocomplete.
     * @return A list of autocomplete suggestions matching the input
     */
    fun autocompleteKeywords(input: String, keywords: List<String>): List<String> =
        if(input.isNotBlank() && input.length > 1) keywords.filter { it.contains(input) } else emptyList()

    /**
     * Filters a list of variable names to provide autocomplete suggestions based on user input.
     *
     * @param variableNames The list of variable names to filter
     * @param input The input text provided by the user for filtering
     * @return A list of variable names that match the user's input
     */
    fun filterVariableNamesForAutocomplete(variableNames: List<String>, input: String): List<String> =
        if(input.isNotBlank() && input.length > 1) variableNames.filter { it.contains(input) } else emptyList()

    /**
     * Filters a list of a function names to provide autocomplete suggestions based on user input
     *
     * @param functionNames The list of function names to filter
     * @param input The input text provided by the user for filtering
     * @return A list of function names that match the user's input
     */
    fun filterFunctionNamesForAutocomplete(functionNames: List<String>, input: String): List<String> =
        if(input.isNotBlank() && input.length > 1) functionNames.filter { it.contains(input) } else emptyList()
}