package ui.editor

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import ui.ThemeApp

object SyntaxKeywordHighlighter {
    /**
     * Highlight syntax keywords, variables, numbers, strings, comments, and labels in the code
     *
     * @param str The input code string to be highlighted
     * @return An annotated string with syntax highlighting applied
     */
    fun codeString(str: String) = buildAnnotatedString {
        withStyle(ThemeApp.code.simple){
            append(str)
            addStyle(ThemeApp.code.keyword, str.lowercase(), RegExp.keyword)
            addStyle(ThemeApp.code.number, str.lowercase(), RegExp.number)
            addStyle(ThemeApp.code.variable, str.lowercase(), RegExp.variable)
            addStyle(ThemeApp.code.string, str, RegExp.string)
            addStyle(ThemeApp.code.comment, str, RegExp.comment)
            addStyle(ThemeApp.code.sectionAndLabel, str.lowercase(), RegExp.section)
            addStyle(ThemeApp.code.sectionAndLabel, str, RegExp.label)
        }
    }

    /**
     * Add a specific style to text matching a regular expression within the string
     *
     * @param style The style to be applied to matching text
     * @param text The input test string
     * @param regex The regular expression pattern to match against
     */
    private fun AnnotatedString.Builder.addStyle(style: SpanStyle, text: String, regex: Regex){
        for (result in regex.findAll(text)){
            addStyle(style, result.range.first, result.range.last + 1)
        }
    }

    /**
     * Regular expressions to match various syntax elements in code
     */
    private object RegExp{
        val keyword = Regex("\\b(" +
                "mov|push|rdx|rdi|rcx|call|rax|xor|rsi|lea|jmp|global|extern|" +
                "add|sub|mul|div|cmp|je|jne|jg|ji|jo|jz|inc|dec|or|and|not|neg|" +
                "sar|shi|rol|ror|rcl|test|rcr|xchg|pop|jmp short|jmp far|ret|" +
                "iret|leave|retf|int|cmpxchg8b|cmpxchg16b|lock|xadd|bts|btr|btc|" +
                "bsf|bsr|cdq|cwd|rsp|hlt|fxch|fnstenv|fnstcw|fnop|fmul|fld|fist|" +
                "fcmovne|fcmovu|eax|ebx|edx|esl|edi|edp|esp|rbx|rbp|r8|r9|r10|r11|" +
                "r12|r13|r14|r15|rflags|rip|mm0|mm1|mm2|mm3|mm4|mm5|mm6|mm7|" +
                "xmm0|xmm1|xmm2|xmm3|xmm4|xmm5|xmm6|xmm7|jnz|jl|jle|jge|nop|imul|shr|" +
                "shl|addpd|subpd|mulpd|devpd|rdtsc|cpuid|jb|ja|ecx" +
                ")\\b")
        val variable = Regex("\\b(" +
                "db|dw|dd|dq|dt|do|dy|dz|qword|dword|equ|resb|resw|resq|resy|resz|" +
                "resd|rest|reso|tbyte|real4|real8|real10|real16|dwordptr|qwordptr|" +
                "tbyteptr|real4ptr|real8ptr|real10ptr|real16ptr" +
                ")\\b")
        val number = Regex("\\d+")
        val section = Regex("section")
        val comment = Regex(";.*")
        val string = Regex( "['\"](.*?)['\"]")
        val label = Regex(".*:")
    }
}