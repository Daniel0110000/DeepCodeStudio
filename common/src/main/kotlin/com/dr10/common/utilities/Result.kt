package com.dr10.common.utilities

typealias RootError = Error

sealed interface Result<out B, out E: RootError> {
    data class Success<out B, out E: RootError>(val value: B): Result<B, E>
    data class Error<out B, out E: RootError>(val error: E): Result<B, E>
}