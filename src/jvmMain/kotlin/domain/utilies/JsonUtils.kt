package domain.utilies

import domain.model.SyntaxHighlightRegexModel
import org.json.JSONArray
import org.json.JSONObject
import ui.editor.EditorErrorState
import ui.settings.SettingsErrorState
import java.io.File
import java.io.FileNotFoundException

object JsonUtils {

    /**
     * Convert the entire content of a JSON located at [jsonPath] into a list of strings
     *
     * @param jsonPath The file path of the JSON to be converted
     * @param errorState The state object to manage and display errors in case of an exception
     * @return A list of strings with all the keywords from the JSON
     */
    fun jsonToListString(
        jsonPath: String,
        errorState: EditorErrorState
    ): List<String>{
        return try {
            val jsonString = File(jsonPath).readText()
            val jsonObject = JSONObject(jsonString)
            val dataObject = jsonObject.getJSONObject("data")

            dataObject.keys().asSequence().flatMap { key ->
                dataObject.getJSONArray(key).toList().map { it.toString() }
            }.toList()
        } catch (e: Exception){
            errorState.displayErrorMessage.value = true
            errorState.shouldCloseTab.value = true
            errorState.errorDescription.value = "Error in [JsonToListString]" + e.message.toString()
            emptyList()
        }
    }

    /**
     * Converts the 'variables' and 'constants' objects from the JSON file located at [jsonPath],
     * ... and returns a string by joining the contents of these two objects, separating each keyword with '|'
     *
     * @param jsonPath The file path of the JSON to be converted
     * @param errorState The state object to manage and display errors in case of an exception
     * @return A string containing all the keywords of the variables and constants in the JSON
     */
    fun extractVariablesAndConstantsKeywordsFromJson(
        jsonPath: String,
        errorState: EditorErrorState
    ): String{
        return try {
            val jsonString = File(jsonPath).readText()

            val jsonObject = JSONObject(jsonString)
            val dataObject = jsonObject.getJSONObject("data")

            val variablesList = dataObject.getJSONArray("variables")
            val constantsList = dataObject.getJSONArray("constants")

            variablesList.joinToString("|"){ it.toString() } + "|" + constantsList.joinToString("|"){ it.toString() }
        } catch (e: Exception){
            errorState.displayErrorMessage.value = true
            errorState.shouldCloseTab.value = true
            errorState.errorDescription.value = "Error in [extractVariablesAndConstantsKeywordsFromJson]" + e.message.toString()
            ""
        }
    }

    /**
     * Converts a JSON file into a [SyntaxHighlightRegexModel] object
     *
     * @param jsonPath The path to the JSON file
     * @param errorState The state object to manage and display errors in case of an exception
     * @return The [SyntaxHighlightRegexModel] parsed from the JSON
     */
    fun jsonToSyntaxHighlightRegexModel(
        jsonPath: String,
        errorState: EditorErrorState? = null,
        settingsErrorState: SettingsErrorState? = null
    ): SyntaxHighlightRegexModel{
        return try {
            val jsonString = File(jsonPath).readText()

            val jsonObject = JSONObject(jsonString)
            val dataObject = jsonObject.getJSONObject("data")

            val instructionsList = dataObject.getJSONArray("instructions")
            val variablesList = dataObject.getJSONArray("variables")
            val constantsList = dataObject.getJSONArray("constants")
            val segmentsList = dataObject.getJSONArray("segments")
            val registersList = dataObject.getJSONArray("registers")
            val systemCallsList = dataObject.getJSONArray("systemCall")
            val arithmeticInstructionsList = dataObject.getJSONArray("arithmeticInstructions")
            val logicalInstructionsList = dataObject.getJSONArray("logicalInstructions")
            val conditionsList = dataObject.getJSONArray("conditions")
            val loopsList = dataObject.getJSONArray("loops")
            val memoryManagementList = dataObject.getJSONArray("memoryManagement")

            SyntaxHighlightRegexModel(
                instructionsList.joinToString(),
                variablesList.joinToString(),
                constantsList.joinToString(),
                segmentsList.joinToString(),
                registersList.joinToString(),
                systemCallsList.joinToString(),
                arithmeticInstructionsList.joinToString(),
                logicalInstructionsList.joinToString(),
                conditionsList.joinToString(),
                loopsList.joinToString(),
                memoryManagementList.joinToString(),
            )
        } catch (e: FileNotFoundException){
            e.printStackTrace()
            SyntaxHighlightRegexModel()
        } catch (e: Exception){
            errorState?.displayErrorMessage?.value = true
            errorState?.shouldCloseTab?.value = true
            errorState?.errorDescription?.value = "Error in jsonToSyntaxHighlightRegexModel" + e.message.toString()

            settingsErrorState?.displayErrorMessage?.value = true
            settingsErrorState?.errorDescription?.value = e.message.toString()

            SyntaxHighlightRegexModel()
        }
    }

    /**
     * Helper function to join elements of a JSONArray into a single string separated by '|'
     *
     * @receiver The JSONArray to join
     * @return The joined string
     */
    private fun JSONArray.joinToString() = joinToString("|"){ it.toString() }

}