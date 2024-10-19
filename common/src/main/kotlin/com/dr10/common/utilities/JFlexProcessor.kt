package com.dr10.common.utilities

import com.dr10.common.models.SyntaxHighlightModel
import com.dr10.common.utilities.TextUtils.deleteWhiteSpaces
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

object JFlexProcessor {

    /**
     * Generates and compiles syntax files for the specified option, using the given syntax highlighting model
     *
     * @param optionName The name of the option for which to generate and compile syntax files
     * @param syntaxHighlightModel The syntax highlighting model to use for generating the syntax files
     * @param onFinish A callback function that will be invoked when the syntax files have been generated and compiled successfully
     */
    fun generateAndCompileSyntaxFiles(
        optionName: String,
        syntaxHighlightModel: SyntaxHighlightModel,
        onFinish: (String) -> Unit
    ) {
        val jflexFilePath = Paths.get("${DocumentsManager.localDirectory.absolutePath}/${optionName.deleteWhiteSpaces()}${Constants.CLASS_EXTENSION}${Constants.JFLEX_EXTENSION}")

        JFlexUtils.createCustomJFlexFile(optionName, jflexFilePath, syntaxHighlightModel)
        JFlexUtils.compileJFlexFile(jflexFilePath.absolutePathString())
        JavaUtils.deleteUnnecessaryFunctionsFromJavaFile(optionName)
        val compilationSuccess = JavaUtils.compileJavaFile(optionName)
        if (compilationSuccess) {
            onFinish("${optionName.deleteWhiteSpaces()}${Constants.CLASS_EXTENSION}")
        }
    }
}