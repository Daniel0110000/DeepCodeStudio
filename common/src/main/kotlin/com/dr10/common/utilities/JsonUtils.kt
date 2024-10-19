package com.dr10.common.utilities

import com.dr10.common.models.SyntaxHighlightModel
import com.dr10.common.models.SyntaxHighlightRegexModel
import com.dr10.common.ui.editor.EditorErrorState
import com.dr10.common.utilities.TextUtils.toFormat
import org.json.JSONArray
import org.json.JSONObject
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
     * Converts a JSON file into a [SyntaxHighlightModel] object
     *
     * @param jsonPath The path to the JSON file
     * @param errorState The state object to manage and display errors in case of an exception
     * @return The [SyntaxHighlightModel] parsed from the JSON
     */
    fun jsonToSyntaxHighlightModel(
        jsonPath: String,
        errorState: EditorErrorState? = null,
        settingsErrorState: SettingsErrorState? = null
    ): SyntaxHighlightModel = try {
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

        SyntaxHighlightModel(
            instructions = instructionsList.toList().toFormat(),
            variables = variablesList.toList().toFormat(),
            constants = constantsList.toList().toFormat(),
            segments = segmentsList.toList().toFormat(),
            systemCalls = systemCallsList.toList().toFormat(),
            registers = registersList.toList().toFormat(),
            arithmeticInstructions = arithmeticInstructionsList.toList().toFormat(),
            logicalInstructions = logicalInstructionsList.toList().toFormat(),
            conditions = conditionsList.toList().toFormat(),
            loops = loopsList.toList().toFormat(),
            memoryManagements = memoryManagementList.toList().toFormat()
        )
    } catch (e: Exception) {
        errorState?.displayErrorMessage?.value = true
        errorState?.shouldCloseTab?.value = true
        errorState?.errorDescription?.value = "Error in jsonToSyntaxHighlightRegexModel" + e.message.toString()

        settingsErrorState?.displayErrorMessage?.value = true
        settingsErrorState?.errorDescription?.value = e.message.toString()

        SyntaxHighlightModel()
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
    ): SyntaxHighlightRegexModel {
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