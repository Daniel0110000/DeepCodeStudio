package ui.terminal

import domain.utilies.DocumentsManager
import ui.viewModels.terminal.TerminalViewModel
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object ExecuteCommands {

    /**
     * Executes the specified command in the terminal and returns the result
     *
     * @param command The command to be executed
     * @param viewModel The [TerminalViewModel] associated with the terminal
     * @return The result of executing the command
     */
    fun executeCommand(
        command: String,
        viewModel: TerminalViewModel
    ): String{
        val process = ProcessBuilder("/bin/zsh", "-c", command)
            .directory(File(viewModel.currentDirectory.value))
            .redirectErrorStream(true)
            .start()

        val read = BufferedReader(InputStreamReader(process.inputStream))
        var line: String?
        var result = ""

        while (read.readLine().also { line = it } != null){
            result += "$line \n"
        }

        process.waitFor()
        updateCurrentDirectory(
            command,
            viewModel
        )
        return result
    }

    /**
     * Updates the current directory in the [viewModel] based on the executed command
     *
     * @param command The executed command
     * @param viewModel The [TerminalViewModel] associated with the terminal
     */
    private fun updateCurrentDirectory(
        command: String,
        viewModel: TerminalViewModel
    ){
        if(command.startsWith("cd")){
            val args = command.substring(2).trim()
            if(args.isNotEmpty()) viewModel.setCurrentDirectory(File(viewModel.currentDirectory.value, args).canonicalPath)
            else viewModel.setCurrentDirectory(DocumentsManager.getUserHome())
        }
    }

}