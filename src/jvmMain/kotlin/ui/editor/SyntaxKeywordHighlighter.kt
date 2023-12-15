package ui.editor

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import domain.model.SyntaxHighlightConfigModel
import domain.model.SyntaxHighlightRegexModel
import domain.utilies.ColorUtils

object SyntaxKeywordHighlighter {

    /**
     * Generates an annotated string applying syntax highlighting styles based on the provided [SyntaxHighlightConfigModel]
     *
     * @param str The input string to be highlighted
     * @param colors The [SyntaxHighlightConfigModel] containing color information for different syntax elements
     * @param model The [SyntaxHighlightRegexModel] containing regular expressions for syntax elements
     * @return [AnnotatedString] with applied syntax highlighting styles
     */
    fun codeString(
        str: String,
        colors: SyntaxHighlightConfigModel,
        model: SyntaxHighlightRegexModel
    ) = buildAnnotatedString {

        val regExp = RegExpTest(model)

        withStyle(SpanStyle(ColorUtils.hexToColor(colors.simpleColor))){
            append(str)
            addStyle(colors.instructionColor, str.lowercase(), regExp.instructions)
            addStyle(colors.variableColor, str.lowercase(), regExp.variables)
            addStyle(colors.constantColor, str.lowercase(), regExp.constants)
            addStyle(colors.segmentColor, str.lowercase(), regExp.segments)
            addStyle(colors.systemCallColor, str.lowercase(), regExp.systemCalls)
            addStyle(colors.registerColor, str.lowercase(), regExp.registers)
            addStyle(colors.arithmeticInstructionColor, str.lowercase(), regExp.arithmeticInstructions)
            addStyle(colors.logicalInstructionColor, str.lowercase(), regExp.logicalInstructions)
            addStyle(colors.conditionColor, str.lowercase(), regExp.conditions)
            addStyle(colors.loopColor, str.lowercase(), regExp.loops)
            addStyle(colors.memoryManagementColor, str.lowercase(), regExp.memoryManagements)
            addStyle(colors.numberColor, str.lowercase(), regExp.number)
            addStyle(colors.commentColor, str.lowercase(), regExp.comment)
            addStyle(colors.stringColor, str.lowercase(), regExp.string)
            addStyle(colors.labelColor, str.lowercase(), regExp.label)
        }
    }

    /**
     * Helper function to add a specified [color] style to the matched [text] within the provided [regex]
     *
     * @param color The color code in string format to apply to the matching text
     * @param text The input text in which to find matches
     * @param regex The regular expression to find matches in the text
     */
    private fun AnnotatedString.Builder.addStyle(color: String, text: String, regex: Regex){
        for (result in regex.findAll(text)){
            addStyle(SpanStyle(ColorUtils.hexToColor(color)), result.range.first, result.range.last + 1)
        }
    }


    /**
     * A class containing regular expressions for syntax highlighting based on the provided [SyntaxHighlightRegexModel]
     *
     * @param model The model containing regex patterns for various code elements
     */
    class RegExpTest(model: SyntaxHighlightRegexModel){
        val instructions = Regex("\\b(${model.instructions.lowercase()})\\b")
        val variables = Regex("\\b(${model.variables.lowercase()})\\b")
        val constants = Regex("\\b(${model.constants.lowercase()})\\b")
        val segments = Regex("\\b(${model.segments.lowercase()})\\b")
        val systemCalls = Regex("\\b(${model.systemCalls.lowercase()})\\b")
        val registers = Regex("\\b(${model.registers.lowercase()})\\b")
        val arithmeticInstructions = Regex("\\b(${model.arithmeticInstructions.lowercase()})\\b")
        val logicalInstructions = Regex("\\b(${model.logicalInstructions.lowercase()})\\b")
        val conditions = Regex("\\b(${model.conditions.lowercase()})\\b")
        val loops = Regex("\\b(${model.loops.lowercase()})\\b")
        val memoryManagements = Regex("\\b(${model.memoryManagements.lowercase()})\\b")
        val number = Regex("\\d+")
        val comment = Regex(";.*")
        val string = Regex( "['\"](.*?)['\"]")
        val label = Regex(".*:")
    }
}