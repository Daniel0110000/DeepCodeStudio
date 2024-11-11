package com.dr10.terminal.utils

import com.dr10.terminal.model.ShellInfo
import com.jediterm.terminal.ui.UIUtil
import com.pty4j.PtyProcessBuilder

object ShellUtils {

    /**
     * Returns the default shell to use for the current platform
     * If the shell environment variable is not set, use the powershell for windows and bash for linux
     *
     * @return The [ShellInfo] object representing the default shell
     */
    fun getDefaultShell(): ShellInfo = ShellInfo(
        name = "Default",
        type = ShellType.DEFAULT,
        command = listOf(System.getenv("SHELL") ?: if (UIUtil.isWindows) "powershell.exe" else "/bin/bash"),
        environment = if (UIUtil.isWindows) mapOf("TERM" to "xterm") else System.getenv()
    )

    /**
     * Returns a list of available shells to use for the current platform
     * If the current platform is windows, the powershell and cmd shells are included
     * If the current platform is linux, the bash, zsh, and fish shells are included
     *
     * @return A list of [ShellInfo] objects representing the available shells
     */
    fun getAvailableShells(): List<ShellInfo> {
        val defaultShell = mutableListOf(getDefaultShell())
        val commonShells = if (UIUtil.isWindows) {
            listOf(
                ShellInfo(
                    name ="PowerShell",
                    command = listOf("powershell.exe"),
                    type = ShellType.POWERSHELL,
                    environment = mapOf("TERM" to "xterm")
                ),
                ShellInfo(
                    name ="CMD",
                    command = listOf("cmd.exe"),
                    type = ShellType.CMD,
                    environment = mapOf("TERM" to "xterm")
                )
            )
        } else {
            listOf(
                ShellInfo(name = "Bash", type = ShellType.BASH, command = listOf("/bin/bash")),
                ShellInfo(name = "Zsh", type = ShellType.ZSH, command = listOf("/bin/zsh")),
                ShellInfo(name = "Fish", type = ShellType.FISH, command = listOf("/usr/bin/fish")),
            )
        }

        return defaultShell + commonShells.filter { shellInfo ->
            // Check if the shell is available on the system
            try {
                val process = PtyProcessBuilder()
                    .setCommand(shellInfo.command.toTypedArray())
                    .setEnvironment(shellInfo.environment)
                    .start()
                process.destroy()
                true
            } catch (e: Exception) {
                false
            }
        }

    }

}