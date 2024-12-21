package com.dr10.common.models

data class RegexRuleModel(
    val id: Long = 0L,
    val uniqueId: String,
    val regexName: String,
    val regexPattern: String,
)
