package ui.editor.autocompleteCode.models

data class Data(
    val addressingModes: List<String>,
    val arithmeticInstructions: List<String>,
    val conditions: List<String>,
    val constants: List<String>,
    val instructions: List<String>,
    val logicalInstructions: List<String>,
    val loops: List<String>,
    val memoryManagement: List<String>,
    val registers: List<String>,
    val segments: List<String>,
    val systemCall: List<String>,
    val variables: List<String>
)