package domain.utilies

class CallHandler {
    companion object{
        /**
         * Executes a provided [block] and handles exceptions gracefully by printing error messages
         *
         * @param block The function to execute
         */
        fun <T> callHandler(block: () -> T){
            try { block() }
            catch (e: Exception){ println("An unexpected error has occurred: ${e.message}") }
        }
    }
}