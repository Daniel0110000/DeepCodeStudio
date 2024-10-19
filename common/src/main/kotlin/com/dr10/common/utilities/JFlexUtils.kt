package com.dr10.common.utilities

import JFlex.Main
import androidx.compose.ui.res.useResource
import com.dr10.common.models.SyntaxHighlightModel
import com.dr10.common.utilities.TextUtils.deleteWhiteSpaces
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

object JFlexUtils {

    /**
     * Creates a custom JFlex file with the given [optionName] and [model]
     *
     * @param optionName The name of the option
     * @param jflexFilePath The path to the JFlex file
     * @param model The syntax highlight model
     */
    fun createCustomJFlexFile(
        optionName: String,
        jflexFilePath: Path,
        model: SyntaxHighlightModel
    ) {

        // Read the jflex template
        val jflexTemplate = useResource("/jflex/TokenMakerTemplate.flex") {
            BufferedReader(InputStreamReader(it, StandardCharsets.UTF_8)).lines().collect(Collectors.joining(System.lineSeparator()))
        }

        val newJflexTemplate = jflexTemplate
            // Replace the default class name with the [optionName] and the [Constants.CLASS_EXTENSION]
            .replace(Constants.DEFAULT_JFLEX_CLASS_NAME, "${optionName.deleteWhiteSpaces()}${Constants.CLASS_EXTENSION}")

            // Replace all the identifiers with the values from the [model]
            .replace(Constants.INSTRUCTIONS_IDENTIFIER, model.instructions)
            .replace(Constants.VARIABLES_IDENTIFIER, model.variables)
            .replace(Constants.CONSTANTS_IDENTIFIER, model.constants)
            .replace(Constants.SEGMENTS_IDENTIFIER, model.segments)
            .replace(Constants.SYSTEM_CALLS_IDENTIFIER, model.systemCalls)
            .replace(Constants.REGISTERS_IDENTIFIER, model.registers)
            .replace(Constants.ARITH_INSTRUCTIONS_IDENTIFIER, model.arithmeticInstructions)
            .replace(Constants.LOGICAL_INSTRUCTIONS_IDENTIFIER, model.logicalInstructions)
            .replace(Constants.CONDITIONS_IDENTIFIER, model.conditions)
            .replace(Constants.MEMORY_MANAGEMENTS_IDENTIFIER, model.memoryManagements)

        // Write the new jflex file
        Files.writeString(jflexFilePath, newJflexTemplate)
    }

    /**
     * Compiles a JFlex file and save the java file generated in the [DocumentsManager.javaFilesDirectory] path
     *
     * @param jflexFilePath The path to the JFlex file
     */
    fun compileJFlexFile(jflexFilePath: String) {
        Main.main(arrayOf("-d", DocumentsManager.javaFilesDirectory.absolutePath, jflexFilePath))
    }
}