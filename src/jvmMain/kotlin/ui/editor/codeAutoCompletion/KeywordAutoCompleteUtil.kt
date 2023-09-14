package ui.editor.codeAutoCompletion

object KeywordAutoCompleteUtil {
    /**
     * Generates auto-complete suggestions for keywords based on teh user's input
     *
     * @param input The user's input for auto-completion
     * @return A list of auto-complete suggestions matching the input
     */
    fun autoCompleteKeywords(input: String): List<String>{
        val keywords = setOf("mov", "push", "rdx", "rdi", "rcx", "call", "rax", "xor", "rsi", "lea",
            "jmp", "global", "extern", "add", "sub", "mul", "div", "cmp", "je", "jne", "jg", "ji",
            "jo", "jz", "inc", "dec", "or", "and", "not", "neg", "sar", "shi", "rol", "ror", "rcl",
            "test", "rcr", "xchg", "pop", "ecx", "jmp short", "jmp far", "ret", "iret", "leave", "retf",
            "int", "cmpxchg8b", "cmpxchg16b", "lock", "xadd", "bts", "btr", "btc", "bsf", "bsr",
            "cdq", "cwd", "rsp", "hlt", "fxch", "fnstenv", "fnstcw", "fnop", "fmul", "fld", "fist",
            "fcmovne", "fcmovu", "eax", "ebx", "edx", "esl", "edi", "edp", "esp", "rbx", "rbp", "r8",
            "r9", "r10", "r11", "r12", "r13", "r14", "r15", "rflags", "rip", "mm0", "mm1", "mm2",
            "mm3", "mm4", "mm5", "mm6", "mm7", "xmm0", "xmm1", "xmm2", "xmm3", "xmm4", "xmm5",
            "xmm6", "xmm7", "db", "dw", "dd", "dq", "dt", "do", "dy", "dz", "qword", "dword", "equ",
            "resb", "resw", "resq", "resy", "resz", "resd", "rest", "reso", "tbyte", "real4", "real8",
            "real10", "real16", "dwordptr", "qwordptr", "tbyteptr", "real4ptr", "real8ptr", "real10ptr",
            "real16ptr", "section", ".data", ".text", ".bss", "global", "section .data", "section .text",
            "section .bss", "syscall", "_start", "_start:", "jnz", "jl", "jle", "jge", "nop", "imul",
            "shr", "shl", "addpd", "subpd", "mulpd", "devpd", "rdtsc", "cpuid", "jb", "ja", "int 0x80")
        return if(input.isBlank()) emptyList() else keywords.filter { it.contains(input) }
    }
}