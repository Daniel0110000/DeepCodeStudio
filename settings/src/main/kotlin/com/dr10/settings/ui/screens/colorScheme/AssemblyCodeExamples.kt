package com.dr10.settings.ui.screens.colorScheme

object AssemblyCodeExamples {
    private val WIN_ASM_EXAMPLE = """
        ; ---------------------------------------------------------------------------------------------------
        ; Basic example of assembly code to preview the colors assigned to the editor's syntax highlighting -
        ; ---------------------------------------------------------------------------------------------------
        section .data
            msg db "DeepCode Studio", 0xd, 0xa, 0
            
        section .text
            global _start                  ; Entry point for the program
            extern _GetStdHandle@4         ; External function to get standard output handle
            extern _WriteConsoleA@20       ; External function to write to the console
            extern _ExitProcess@4          ; External function to exit the program
        
        _start:
            ; Get the handle for the standard output (stdout)
            push -11                        ; Push -11 (STD_OUTPUT_HANDLE) to the stack
            call _GetStdHandle@4            ; Call GetStdHandle to get the handle for stdout
            
            ; Write "DeepCode Studio" to the console
            push 0                          ; Push 0 (reserved, not used)
            push 0                          ; Push 0 (address of the number of bytes written, ignored here)
            push 17                         ; Push 17 (length of the messages in bytes)
            push msg                        ; Push the address of the message to be written
            push eax                        ; Push the handle to stdout returned by GetStdHandle
            call _WriteConsoleA@20          ; Call WriteConsoleA to write the message to the console
            
            ; Exit the program
            push 0                          ; Push 0 (exit code for a successful termination)
            call _ExitProcess@4             ; Call ExitProcess to exit the program
    """.trimIndent()

    private val LINUX_ASM_EXAMPLE = """
        ; ---------------------------------------------------------------------------------------------------
        ; Basic example of assembly code to preview the colors assigned to the editor's syntax highlighting -
        ; ---------------------------------------------------------------------------------------------------
        section .data
            msg db "DeepCode Studio", 0
            len_msg equ $ - msg
            
        section .text
            global _start
            
        _start:
            ; Write "DeepCode Studio" to the console
            mov eax, 4          ; System call number for sys_write (4)
            mov ebx, 1          ; File descriptor 1 (stdout)
            mov ecx, msg        ; Address of the message to write
            mov edx, len_msg    ; Length of the message
            int 0x80            ; System call to write to stdout
            
            ; Exit the program
            mov eax, 1          ; System call number for sys_exit (1)
            int 0x80            ; System call to exit the program
    """.trimIndent()

    private val osName = System.getProperty("os.name").lowercase()
    private fun isWindows() = osName.startsWith("windows")

    fun getCodeExample(): String = if (isWindows()) WIN_ASM_EXAMPLE else LINUX_ASM_EXAMPLE
}