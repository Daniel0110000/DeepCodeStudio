package domain.utilies

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

object TextUtils {

    /**
     * Extracts the word surrounding the cursor position in a given string
     *
     * @param cursorPosition The position of the cursor in the string
     * @param str The input string
     * @return The word that surrounds the cursor position
     */
    fun extractSurroundingWord(cursorPosition: Int, str: String): String{
        // Initialize the start and end positions for the selected word
        var start = cursorPosition
        var end = cursorPosition

        // Move the start position to the left until a whitespace character is encountered
        while (start > 0 && !str[start - 1].isWhitespace()){ start -- }

        // Move the end position to the right until a whitespace character is encountered
        while (end < str.length && !str[end].isWhitespace()){ end++ }

        // Extract the selected word based on the start and end positions
        val selectedWord = str.subSequence(start, end)

        return selectedWord.toString()
    }

    /**
     * Inserts text at the cursor position within the current text
     *
     * @param cursorPosition The position of the cursor in the current text
     * @param currentText The text to which new text is to be inserted
     * @param newText The text to be inserted
     * @return The modified text with the new text inserted at the cursor position
     *
     */
    fun insertTextAtCursorPosition(cursorPosition: Int, currentText: String, newText: String): String{
        // Extract the substring of the current text up to the cursor position
        val subString = currentText.substring(0, cursorPosition)
        // Split the substring into words based on whitespace
        val words = subString.split("\\s+".toRegex())
        // Find the position of the last space in the substring
        val lastSpace = subString.lastIndexOf(words.last().toString())
        // Extract the text before the last space new Str
        val endText = subString.substring(0, lastSpace)

        // Combine the end text, new text, and the remaining part of the current text
        return endText + newText + currentText.substring(cursorPosition)
    }

    /**
     * Inserts spaces and text at a specified index in the original text and return a new [TextFieldValue]
     *
     * @param originalText The original text where spaces and text will be inserted
     * @param insertionIndex The index at which the insertion should occur
     * @param textToInsert The text to insert at the specified index
     * @return A new [TextFieldValue] with the inserted text and updated cursor position
     */
    fun insertSpacesInText(originalText: String, insertionIndex: Int, textToInsert: String): TextFieldValue{
        val textBeforeInsertion = originalText.substring(0, insertionIndex)
        val newTextWithSpaces = textBeforeInsertion + textToInsert + originalText.substring(insertionIndex)
        return TextFieldValue(newTextWithSpaces, TextRange(insertionIndex + textToInsert.length))
    }

    /**
     * Extracts variable names from a given [str] using a regular expression specified by [regexValue]
     *
     * @param str The assembly code where variable declarations are searched
     * @param regexValue The regular expression pattern used to match variable names
     * @return A list of variable names declared in the assembly code
     */
    fun extractVariableNames(str: String, regexValue: String): List<String>{
        val strWithoutColon = str.replace(":", "")
        val pattern = Regex("""\s+(\w+)\s+($regexValue)\s+""".trimMargin())
        val con = pattern.findAll(strWithoutColon)
        return (con.map { it.groupValues[1] } + con.map { "[${it.groupValues[1]}]" }).toList()
    }

    /**
     * Extracts function name declared in assembly code
     *
     * @param str The assembly code where function declarations are searched
     * @return A list of function names declared in the assembly code
     */
    fun extractFunctionNames(str: String): List<String>{
        val lines = str.split("\n")
        val pattern = Regex("""(\w+):$""".trimMargin())
        val functionNames = mutableListOf<String>()

        for (line in lines){
            val match = pattern.find(line.trim())
            match?.let {
                functionNames.add(match.groupValues[1])
            }
        }

        return functionNames + functionNames.map { "[$it]" }
    }

}