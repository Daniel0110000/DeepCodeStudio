package com.dr10.common.utilities

fun ErrorType.getErrorMessage(): String = when (this) {
    ErrorType.REGEX_NAME_OR_REGEX_EMPTY -> "The Regex Name or Regex field is empty. Please fill in the required information"
    ErrorType.UNKNOWN -> "Unknown error"
    ErrorType.CUSTOM -> ""
}