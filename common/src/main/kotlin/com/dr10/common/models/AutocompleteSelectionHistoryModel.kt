package com.dr10.common.models

data class AutocompleteSelectionHistoryModel(
    val uuid: String = "",
    val asmFilePath: String,
    val optionName: String,
    val jsonPath: String
)