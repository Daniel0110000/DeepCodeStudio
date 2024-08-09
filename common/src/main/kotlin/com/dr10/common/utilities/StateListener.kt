package com.dr10.common.utilities

interface StateListener<T> {
    fun onStatedChanged(state: T)
}