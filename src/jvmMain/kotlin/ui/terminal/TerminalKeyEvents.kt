package ui.terminal

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import ui.viewModels.terminal.TerminalViewModel

@OptIn(ExperimentalComposeUiApi::class)
fun terminalKeyEvents(
    keyEvent: KeyEvent,
    viewModel: TerminalViewModel,
    selectedWord: String,
    allCommandHistory: List<String>,
    onErrorOccurred: (String) -> Unit
): Boolean{
    return when{
        (keyEvent.key == Key.Enter && !viewModel.isKeyBeingPressed.value && viewModel.suggestions.value.isEmpty()) -> {
            if(viewModel.command.value.text.isNotBlank()){
                // Execute command when Enter is pressed, clear terminal if the command is "clear"
                if(viewModel.command.value.text == "clear") viewModel.clearTerminal()
                else{
                    val commandResult = ExecuteCommands.executeCommand(viewModel.command.value.text, viewModel)
                    viewModel.setDirectory(viewModel.currentDirectory.value)
                    viewModel.setCommandExecuted(viewModel.command.value.text)
                    viewModel.setResult(commandResult)
                    // If [commandResult] contains 'error' or 'warning', we call the callback [onErrorOccurred] passing the [commandResult]
                    if(commandResult.contains("error") || commandResult.contains("warning")) onErrorOccurred(commandResult)
                }

                viewModel.addCommand(viewModel.command.value.text)
                viewModel.setCommand(TextFieldValue(""))
                viewModel.setCommandIndex(-1)
            }

            viewModel.setIsKeyBeingPressed(true)
            true
        }
        (keyEvent.isCtrlPressed && keyEvent.key == Key.L && !viewModel.isKeyBeingPressed.value) -> {
            // Handle Ctrl + L to clear the terminal
            viewModel.clearTerminal()
            viewModel.setIsKeyBeingPressed(true)
            true
        }
        (keyEvent.key == Key.Tab && !viewModel.isKeyBeingPressed.value) -> {
            // Handle Tab key for autocomplete suggestions
            viewModel.setSelectedItemIndex(0)
            viewModel.getSuggestions(selectedWord)
            if(viewModel.suggestions.value.size == 1){
                // Assigns the selected command from suggestions to the input through the viewModel
                viewModel.setSuggestionToInput(viewModel.suggestions.value.last())

                // Clear suggestions
                viewModel.clearSuggestions()
            }
            viewModel.setIsKeyBeingPressed(true)
            true
        }
        (keyEvent.key == Key.DirectionDown && !viewModel.isKeyBeingPressed.value && viewModel.suggestions.value.isNotEmpty()) -> {
            // Handle Down arrow key for navigating suggestions
            if(viewModel.selectedItemIndex.value < viewModel.suggestions.value.size - 1) viewModel.setSelectedItemIndex(viewModel.selectedItemIndex.value + 1)
            viewModel.setIsKeyBeingPressed(true)
            true
        }
        (keyEvent.key == Key.DirectionUp && !viewModel.isKeyBeingPressed.value && viewModel.suggestions.value.isNotEmpty()) ->{
            // Handle Up arrow key for navigating suggestions
            if(viewModel.selectedItemIndex.value > 0) viewModel.setSelectedItemIndex(viewModel.selectedItemIndex.value - 1)
            viewModel.setIsKeyBeingPressed(true)
            true
        }
        (keyEvent.key == Key.DirectionDown && !viewModel.isKeyBeingPressed.value && viewModel.suggestions.value.isEmpty()) -> {
            val index = viewModel.commandIndex.value
            // If there are previous commands, update the index and content of the text field
            if(index > 0) {
                viewModel.setCommandIndex(index - 1)
                viewModel.setCommand(TextFieldValue(allCommandHistory[viewModel.commandIndex.value], TextRange(allCommandHistory[viewModel.commandIndex.value].length)))
            }
            viewModel.setIsKeyBeingPressed(true)
            true
        }
        (keyEvent.key == Key.DirectionUp && !viewModel.isKeyBeingPressed.value && viewModel.suggestions.value.isEmpty()) -> {
            val index = viewModel.commandIndex.value
            // If there are subsequent commands, update the index and content of the text field
            if(index < allCommandHistory.size - 1) {
                viewModel.setCommandIndex(index + 1)
                viewModel.setCommand(TextFieldValue(allCommandHistory[viewModel.commandIndex.value], TextRange(allCommandHistory[viewModel.commandIndex.value].length)))
            }

            viewModel.setIsKeyBeingPressed(true)
            true
        }
        (keyEvent.key == Key.Enter && viewModel.suggestions.value.isNotEmpty() && !viewModel.isKeyBeingPressed.value) ->{
            // Assigns the selected command from suggestions to the input through the viewModel
            viewModel.setSuggestionToInput(viewModel.suggestions.value[viewModel.selectedItemIndex.value])

            // Reset selected item and clear suggestions
            viewModel.setSelectedItemIndex(0)
            viewModel.clearSuggestions()
            viewModel.setIsKeyBeingPressed(true)
            true
        }
        (keyEvent.type == KeyEventType.KeyUp) ->{
            // Release key press flag when key is released
            viewModel.setIsKeyBeingPressed(false)
            false
        }
        else -> false
    }
}