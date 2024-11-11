package com.dr10.terminal.utils

import com.dr10.terminal.model.ShellInfo
import com.jediterm.pty.PtyProcessTtyConnector
import com.jediterm.terminal.TtyConnector
import com.pty4j.PtyProcessBuilder
import java.nio.charset.StandardCharsets

/**
 * Creates a TtyConnector for a terminal emulator, specifically configured for Zsh or PowerShell depending on the platform
 *
 * @param shellInfo The [ShellInfo] object containing the command, environment, and path for the terminal emulator
 * @return A TtyConnector instance for communication with the terminal emulator
 * @throws IllegalStateException if an exception occurs during the process creation
 */
fun createTtyConnector(shellInfo: ShellInfo): TtyConnector {
    return try {

        // Start the PtyProcess with the configured command and environment
        val process = PtyProcessBuilder()
            .setCommand(shellInfo.command.toTypedArray())
            .setEnvironment(shellInfo.environment)
            .setDirectory(shellInfo.path)
            .start()

        // Create a TtyConnector using the PtyProcess and UTF-8 encoding
        PtyProcessTtyConnector(process, StandardCharsets.UTF_8)
    } catch (e: Exception) {
        throw IllegalStateException(e)
    }
}