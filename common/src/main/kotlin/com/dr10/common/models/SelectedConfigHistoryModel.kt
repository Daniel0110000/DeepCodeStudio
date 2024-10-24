package com.dr10.common.models

data class SelectedConfigHistoryModel(
    val uniqueId: String,
    val optionName: String,
    val className: String,
    val asmFilePath: String
)
