package com.dr10.common.models

data class SyntaxHighlightModel(
    val instructions: String = "",
    val variables: String = "",
    val constants: String = "",
    val segments: String = "",
    val systemCalls: String = "",
    val registers: String = "",
    val arithmeticInstructions: String = "",
    val logicalInstructions: String = "",
    val conditions: String = "",
    val loops: String = "",
    val memoryManagements: String = ""
)