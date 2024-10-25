package com.dr10.common.utilities

class CallHandler {
    companion object{
        /**
         * Executes a provided [block] and handles exceptions gracefully by printing error messages
         *
         * @param block The function to execute
         */
        suspend fun <T> callHandler(block: suspend () -> T){
            try { block() }
            catch (e: Exception){ println("An unexpected error has occurred: ${e.message}") }
        }
    }
}