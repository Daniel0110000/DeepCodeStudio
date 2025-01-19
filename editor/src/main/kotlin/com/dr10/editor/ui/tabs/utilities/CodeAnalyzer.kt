package com.dr10.editor.ui.tabs.utilities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.nio.file.Path

class CodeAnalyzer {

    private val symbolTable = mutableMapOf<String, HashSet<CodeSymbol>>()
    private val returnedSymbols = mutableMapOf<String, HashSet<CodeSymbolReturned>>()
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
     fun processLines(lines: Sequence<String>) {
        lines.forEachIndexed { lineNumber, line -> processLine(line, lineNumber) }
    }

    /**
     * Processes a single line using the regex patterns and populates the symbol table
     *
     * @param line The line to process
     * @param lineNumber The line number of the line
     */
    fun processLine(line: String, lineNumber: Int) {
        regexPatterns.forEach { (patternName, regex) ->
            regex.findAll(line).forEach { match ->
                val index = if (match.groupValues.size > 1) 1 else 0
                val symbol = CodeSymbol(
                    name = match.groupValues[index].takeIf { it.isNotEmpty() } ?: match.value,
                    lineNumber = lineNumber,
                    content = line.trim()
                )
                symbolTable.getOrPut(patternName){ HashSet() }.add(symbol)
            }
        }
    }

    /**
     * Returns the new symbols found in the analyzed file
     *
     * @return The new symbols found in the analyzed file
     */
    fun getNewAllSymbols(): Set<CodeSymbol> {
        val returnedLookup = returnedSymbols.values.flatten().groupBy { it.name to it.lineNumber }
        val symbolByKey = symbolTable.values.flatten().groupBy { it.name to it.lineNumber }
        val newSymbols = symbolByKey.filterKeys { it !in returnedLookup }.values.flatten().toSet()

        newSymbols.forEach { symbol ->
            val type = symbolTable.entries.find { (_, symbols) -> symbol in symbols }?.key ?: return@forEach

            returnedSymbols.getOrPut(type) { HashSet() }.add(
                CodeSymbolReturned(
                    name = symbol.name,
                    lineNumber = symbol.lineNumber
                )
            )
        }

        return newSymbols
    }

    /**
     * Clears the analyzer's data
     */
    fun clear() {
        symbolTable.clear()
        returnedSymbols.clear()
        regexPatterns.clear()
    }

}
