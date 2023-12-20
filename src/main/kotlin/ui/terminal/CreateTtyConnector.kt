package ui.terminal

import com.jediterm.pty.PtyProcessTtyConnector
import com.jediterm.terminal.TtyConnector
import com.pty4j.PtyProcessBuilder
import java.nio.charset.StandardCharsets

/**
 * Creates a TtyConnector for a terminal emulator, specifically configured for Zsh
 *
 * @return A TtyConnector instance for communication with the terminal emulator
 * @throws IllegalStateException if an exception occurs during the process creation
 */
fun createTtyConnector(currentPath: String): TtyConnector {
    return try {
        // Initialize environment variable and command for starting Zsh
        val env: MutableMap<String, String>?
        val command: Array<String> = arrayOf("/bin/zsh", "--login")

        // Copy the system environment and set the terminal type to xterm-256color
        env = HashMap(System.getenv())
        env["TERM"] = "xterm-256color"

        // Start the PtyProcess with the configured command and environment
        val process = PtyProcessBuilder().setCommand(command).setEnvironment(env).setDirectory(currentPath).start()

        // Create a TtyConnector using the PtyProcess and UTF-8 encoding
        PtyProcessTtyConnector(process, StandardCharsets.UTF_8)
    } catch (e: Exception) {
        throw IllegalStateException(e)
    }
}