package domain.util

import com.google.gson.Gson
import ui.editor.codeAutoCompletion.models.AutocompleteModel
import java.io.File

object JsonUtils {

    /**
     * Converts a JSOn file located at [jsonPath] into an AutocompleteModel object
     *
     * @param jsonPath The file path of the JSON to be converted
     * @return An Autocompleted representing the JSON content
     */
    fun jsonToAutocompleteModel(jsonPath: String): AutocompleteModel{
        val gson = Gson()

        val jsonString: String = File(jsonPath)
            .bufferedReader()
            .use { it.readText() }

        return gson.fromJson(jsonString, AutocompleteModel::class.java)
    }

}