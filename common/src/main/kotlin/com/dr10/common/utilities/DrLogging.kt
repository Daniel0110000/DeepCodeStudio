package com.dr10.common.utilities

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class LogLevel { INFO, DEBUG, WARNING, ERROR }

class DrLogging <T: Any>(
    private val classInstance: T,
    private val minLogLevel: LogLevel = LogLevel.INFO
) {

    private val red = "\u001B[31m"
    private val blue = "\u001B[34m"
    private val yellow = "\u001B[33m"
    private val green = "\u001B[32m"
    private val reset = "\u001B[0m"

    private val lineSeparator = System.lineSeparator()

    private fun log(level: LogLevel, color: String, content: String) {
        if (level.ordinal >= minLogLevel.ordinal) {
            val className = color + classInstance::class.java.simpleName + reset
            val output = "${getCurrentTime()} | $className | ${classInstance::class.java.name} | $color ${level.name} $reset | $content"
            println(output + lineSeparator)
        }
    }

    fun info(content: Any) = log(LogLevel.INFO, blue, content.toString())
    fun error(content: Any) = log(LogLevel.ERROR, red, content.toString())
    fun debug(content: Any) = log(LogLevel.DEBUG, green, content.toString())
    fun warning(content: Any) = log(LogLevel.WARNING, yellow, content.toString())


    private fun getCurrentTime(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return LocalDateTime.now().format(formatter)
    }

}