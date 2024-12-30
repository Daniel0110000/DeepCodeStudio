package com.dr10.editor.ui.tabs.utilities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.nio.file.Path

class CodeAnalyzer {
    private val symbolTable = mutableMapOf<String, MutableSet<CodeSymbol>>()
    private val regexPatterns = mutableMapOf<String, Regex>()

    /**
     * Adds a new regex pattern to the analyzer
     */
    fun addPattern(name: String, pattern: String) {
        regexPatterns[name] = Regex(pattern)
    }

    /**
     * Analyzes the provided file to extract symbols
     *
     * @param filePath The path of the file to analyze
     * @param onAnalyzeFinished A callback to be invoked when the analysis is finished
     */
    fun analyzeFile(
        filePath: Path,
        onAnalyzeFinished: () -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        symbolTable.clear()
        FileInputStream(filePath.toFile()).use { input ->
            input.bufferedReader().useLines { lines ->
                processLines(lines)
                onAnalyzeFinished()
            }
        }
    }

    /**
     * Processes the provided lines using the regex patterns and populates the symbol table
     *
     * @param lines The sequence of lines to process
     */
    private fun processLines(lines: Sequence<String>) {
        lines.forEachIndexed { lineNumber, line ->
            regexPatterns.forEach { (patternName, regex) ->
                regex.findAll(line).forEach { match ->
                    val index = if (match.groupValues.size > 1) 1 else 0
                    val symbol = CodeSymbol(
                        name = match.groupValues[index].takeIf { it.isNotEmpty() } ?: match.value,
                        lineNumber = lineNumber,
                        content = line.trim()
                    )
                    symbolTable.getOrPut(patternName){ mutableSetOf() }.add(symbol)
                }
            }
        }
    }

    /**
     * Gets all the symbols that match the provided type
     */
    fun getSymbols(type: String): Set<CodeSymbol> = symbolTable[type]?.toSet() ?: emptySet()
}
