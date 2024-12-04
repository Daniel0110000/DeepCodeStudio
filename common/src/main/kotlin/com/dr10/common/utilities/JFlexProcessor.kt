package com.dr10.common.utilities

import com.dr10.common.models.SyntaxHighlightModel
import com.dr10.common.utilities.TextUtils.deleteWhiteSpaces
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.absolutePathString

object JFlexProcessor {

    /**
     * Generates and compiles syntax files for the specified option, using the given syntax highlighting model
     *
     * @param optionNameBase The name of the option for which to generate and compile syntax files
     * @param syntaxHighlightModel The syntax highlighting model to use for generating the syntax files
     * @param onFinish A callback function that will be invoked when the syntax files have been generated and compiled successfully
     */
    fun generateAndCompileSyntaxFiles(
        optionNameBase: String,
        syntaxHighlightModel: SyntaxHighlightModel,
        onFinish: (String) -> Unit
    ) {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
        val currentOptionName = "${optionNameBase}_$timestamp"

        val jflexFilePath = Paths.get("${DocumentsManager.localDirectory.absolutePath}/${currentOptionName.deleteWhiteSpaces()}${Constants.CLASS_EXTENSION}${Constants.JFLEX_EXTENSION}")

        JFlexUtils.createCustomJFlexFile(currentOptionName, jflexFilePath, syntaxHighlightModel)
        JFlexUtils.compileJFlexFile(jflexFilePath.absolutePathString())
        JavaUtils.deleteUnnecessaryFunctionsFromJavaFile(currentOptionName)
        val compilationSuccess = JavaUtils.compileJavaFile(currentOptionName)
        if (compilationSuccess) {
            onFinish("${currentOptionName.deleteWhiteSpaces()}${Constants.CLASS_EXTENSION}")
        }
    }
}