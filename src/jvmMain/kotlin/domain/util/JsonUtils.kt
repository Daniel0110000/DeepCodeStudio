package domain.util

import org.json.JSONObject
import java.io.File

object JsonUtils {

    /**
     * Convert the entire content of a JSON located at [jsonPath] into a list of strings
     *
     * @param jsonPath The file path of the JSON to be converted
     * @return Return a list of strings with all the keywords from the JSON
     */
    fun jsonToListString(jsonPath: String): List<String>{
        val jsonString = File(jsonPath).readText()
        val jsonObject = JSONObject(jsonString)
        val dataObject = jsonObject.getJSONObject("data")

        val allElements = dataObject.keys().asSequence().flatMap { key ->
            dataObject.getJSONArray(key).toList().map { it.toString() }
        }.toList()

        return allElements
    }

    /**
     * Converts the 'variables' and 'constants' objects from the JSON file located at [jsonPath],
     * ... and returns a string by joining the contents of these two objects, separating each keyword with '|'
     *
     * @param jsonPath The file path of the JSON to be converted
     * @return A string containing all the keywords of the variables and constants in the JSON
     */
    fun extractVariablesAndConstantsKeywordsFromJson(jsonPath: String): String{
        val jsonString = File(jsonPath).readText()

        val jsonObject = JSONObject(jsonString)
        val dataObject = jsonObject.getJSONObject("data")

        val variablesList = dataObject.getJSONArray("variables")
        val constantsList = dataObject.getJSONArray("constants")

        return variablesList.joinToString("|"){ it.toString() } + "|" + constantsList.joinToString("|"){ it.toString() }

    }

}