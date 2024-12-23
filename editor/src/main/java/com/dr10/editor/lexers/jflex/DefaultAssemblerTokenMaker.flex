package com.dr10.editor.lexers;

import java.io.*;
import javax.swing.text.Segment;
import org.fife.ui.rsyntaxtextarea.*;

%%

%public
%class DefaultAssemblerTokenMaker
%extends AbstractJFlexTokenMaker
%unicode
%ignorecase
%type org.fife.ui.rsyntaxtextarea.Token

%{

    public DefaultAssemblerTokenMaker() {
        super();
    }

    private void addToken(int tokenType) {
        addToken(zzStartRead, zzMarkedPos - 1, tokenType);
    }

    private void addToken(int start, int end, int tokenType) {
        int so = start + offsetShift;
        addToken(zzBuffer, start, end, tokenType, so);
    }

    @Override
    public void addToken(char[] array, int start, int end, int tokenType, int startOffset) {
        super.addToken(array, start, end, tokenType, startOffset);
        zzStartRead = zzMarkedPos;
    }

    @Override
    public String[] getLineCommentStartAndEnd(int languageIndex) {
        return new String[] { ";", null };
    }

    public Token getTokenList(Segment text, int initialTokenType, int startOffset) {
        resetTokenList();
        this.offsetShift = -text.offset + startOffset;

        int state = Token.NULL;
        switch (initialTokenType) {
            default -> state = Token.NULL;
        }

        s = text;
        try {
            yyreset(zzReader);
            yybegin(state);
            return yylex();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return new TokenImpl();
        }
    }


    private boolean zzRefill() {
        return zzCurrentPos >= s.offset + s.count;
    }

    public final void yyreset(Reader reader) {
        zzBuffer = s.array;
        zzStartRead = s.offset;
        zzEndRead = zzStartRead + s.count - 1;
        zzCurrentPos = zzMarkedPos = zzPushbackPos = s.offset;
        zzReader = reader;
        zzAtBOL = true;
        zzAtEOF = false;
    }

%}

Letter				= ([A-Za-z_])
Digit				= ([0-9])
Number				= ({Digit}+)
Hexadecimal         = "-"?"0"[xX][0-9A-Fa-f]+




Identifier			= (({Letter}|{Digit})[^ \t\f\n\,\.\+\-\*\/\%\[\]]+)

UnclosedStringLiteral	= ([\"][^\"]*)
StringLiteral			= ({UnclosedStringLiteral}[\"])
UnclosedCharLiteral		= ([\'][^\']*)
CharLiteral			= ({UnclosedCharLiteral}[\'])

CommentBegin			= ([;])

LineTerminator			= (\n)
WhiteSpace			= ([ \t\f])

Label				= (({Letter}|{Digit})+[\:])

Operator				= ("+"|"-"|"*"|"/"|"%"|"^"|"|"|"&"|"~"|"!"|"="|"<"|">")

%%

<YYINITIAL> {
	/* Keywords */
	".186" | ".286" | ".286P" | ".287" | ".386" |
    ".386P" | ".387" | ".486" | ".486P" | ".586" |
    ".586P" | ".686" | ".686P" | ".8086" | ".8087" |
    ".ALPHA" | ".BREAK" | ".BSS" | ".CODE" | ".CONST" |
    ".CONTINUE" | ".CREF" | ".DATA" | ".DATA?" | ".DOSSEG" |
    ".ELSE" | ".ELSEIF" | ".ENDIF" | ".ENDW" | ".ERR" |
    ".ERR1" | ".ERR2" | ".ERRB" | ".ERRDEF" | ".ERRDIF" |
    ".ERRDIFI" | ".ERRE" | ".ERRIDN" | ".ERRIDNI" | ".ERRNB" |
    ".ERRNDEF" | ".ERRNZ" | ".EXIT" | ".FARDATA" | ".FARDATA?" |
    ".IF" | ".K3D" | ".LALL" | ".LFCOND" | ".LIST" |
    ".LISTALL" | ".LISTIF" | ".LISTMACRO" | ".LISTMACROALL" | ".MMX" |
    ".MODEL" | ".MSFLOAT" | ".NO87" | ".NOCREF" | ".NOLIST" |
    ".NOLISTIF" | ".NOLISTMACRO" | ".RADIX" | ".REPEAT" | ".SALL" |
    ".SEQ" | ".SFCOND" | ".STACK" | ".STARTUP" | ".TEXT" |
    ".TFCOND" | ".UNTIL" | ".UNTILCXZ" | ".WHILE" | ".XALL" |
    ".XCREF" | ".XLIST" | ".XMM" | "__FILE__" | "__LINE__" |
    "A16" | "A32" | "ADDR" | "ALIGN" | "ALIGNB" |
    "ASSUME" | "BITS" | "CARRY?" | "CATSTR" | "CODESEG" |
    "COMM" | "COMMENT" | "COMMON" | "DATASEG" | "DOSSEG" |
    "ECHO" | "ELSE" | "ELSEIF" | "ELSEIF1" | "ELSEIF2" |
    "ELSEIFB" | "ELSEIFDEF" | "ELSEIFE" | "ELSEIFIDN" | "ELSEIFNB" |
    "ELSEIFNDEF" | "END" | "ENDIF" | "ENDM" | "ENDP" |
    "ENDS" | "ENDSTRUC" | "EVEN" | "EXITM" | "EXPORT" |
    "EXTERN" | "EXTERNDEF" | "EXTRN" | "FAR" | "FOR" |
    "FORC" | "GLOBAL" | "GOTO" | "GROUP" | "HIGH" |
    "HIGHWORD" | "IEND" | "IF" | "IF1" | "IF2" |
    "IFB" | "IFDEF" | "IFDIF" | "IFDIFI" | "IFE" |
    "IFIDN" | "IFIDNI" | "IFNB" | "IFNDEF" | "IMPORT" |
    "INCBIN" | "INCLUDE" | "INCLUDELIB" | "INSTR" | "INVOKE" |
    "IRP" | "IRPC" | "ISTRUC" | "LABEL" | "LENGTH" |
    "LENGTHOF" | "LOCAL" | "LOW" | "LOWWORD" | "LROFFSET" |
    "MACRO" | "NAME" | "NEAR" | "NOSPLIT" | "O16" |
    "O32" | "OFFSET" | "OPATTR" | "OPTION" | "ORG" |
    "OVERFLOW?" | "PAGE" | "PARITY?" | "POPCONTEXT" | "PRIVATE" |
    "PROC" | "PROTO" | "PTR" | "PUBLIC" | "PURGE" |
    "PUSHCONTEXT" | "RECORD" | "REPEAT" | "REPT" | "SECTION" |
    "SEG" | "SEGMENT" | "SHORT" | "SIGN?" | "SIZE" |
    "SIZEOF" | "SIZESTR" | "STACK" | "STRUC" | "STRUCT" |
    "SUBSTR" | "SUBTITLE" | "SUBTTL" | "THIS" | "TITLE" |
    "TYPE" | "TYPEDEF" | "UNION" | "USE16" | "USE32" |
    "USES" | "WHILE" | "WRT" | "ZERO?"                   { addToken(Token.PREPROCESSOR); }

	"DB" | "DW" | "DD" | "DF" | "DQ" |
    "DT" | "RESB" | "RESW" | "RESD" | "RESQ" |
    "REST" | "EQU" | "TEXTEQU" | "TIMES" | "DUP"         { addToken(Token.FUNCTION); }

	"BYTE" | "WORD" | "DWORD" | "FWORD" | "QWORD" |
    "TBYTE" | "SBYTE" | "TWORD" | "SWORD" | "SDWORD" |
    "REAL4" | "REAL8" | "REAL10"		                 { addToken(Token.DATA_TYPE); }

	/* Registers */
	"AL" | "BL" | "CL" | "DL" | "AH" |
    "BH" | "CH" | "DH" | "AX" | "BX" |
    "CX" | "DX" | "SI" | "DI" | "SP" |
    "BP" | "EAX" | "EBX" | "ECX" | "EDX" |
    "ESI" | "EDI" | "ESP" | "EBP" | "CS" |
    "DS" | "SS" | "ES" | "FS" | "GS" |
    "ST" | "ST0" | "ST1" | "ST2" | "ST3" |
    "ST4" | "ST5" | "ST6" | "ST7" | "MM0" |
    "MM1" | "MM2" | "MM3" | "MM4" | "MM5" |
    "MM6" | "MM7" | "XMM0" | "XMM1" | "XMM2" |
    "XMM3" | "XMM4" | "XMM5" | "XMM6" | "XMM7" |
    "CR0" | "CR2" | "CR3" | "CR4" | "DR0" |
    "DR1" | "DR2" | "DR3" | "DR4" | "DR5" |
    "DR6" | "DR7" | "TR3" | "TR4" | "TR5" |
    "TR6" | "TR7"		                                { addToken(Token.VARIABLE); }

	/* Pentium III Instructions. */
	"AAA" | "AAD" | "AAM" | "AAS" | "ADC" |
    "ADD" | "ADDPS" | "ADDSS" | "AND" | "ANDNPS" |
    "ANDPS" | "ARPL" | "BOUND" | "BSF" | "BSR" |
    "BSWAP" | "BT" | "BTC" | "BTR" | "BTS" |
    "CALL" | "CBW" | "CDQ" | "CLC" | "CLD" |
    "CLI" | "CLTS" | "CMC" | "CMOVA" | "CMOVAE" |
    "CMOVB" | "CMOVBE" | "CMOVC" | "CMOVE" | "CMOVG" |
    "CMOVGE" | "CMOVL" | "CMOVLE" | "CMOVNA" | "CMOVNAE" |
    "CMOVNB" | "CMOVNBE" | "CMOVNC" | "CMOVNE" | "CMOVNG" |
    "CMOVNGE" | "CMOVNL" | "CMOVNLE" | "CMOVNO" | "CMOVNP" |
    "CMOVNS" | "CMOVNZ" | "CMOVO" | "CMOVP" | "CMOVPE" |
    "CMOVPO" | "CMOVS" | "CMOVZ" | "CMP" | "CMPPS" |
    "CMPS" | "CMPSB" | "CMPSD" | "CMPSS" | "CMPSW" |
    "CMPXCHG" | "CMPXCHGB" | "COMISS" | "CPUID" | "CWD" |
    "CWDE" | "CVTPI2PS" | "CVTPS2PI" | "CVTSI2SS" | "CVTSS2SI" |
    "CVTTPS2PI" | "CVTTSS2SI" | "DAA" | "DAS" | "DEC" |
    "DIV" | "DIVPS" | "DIVSS" | "EMMS" | "ENTER" |
    "F2XM1" | "FABS" | "FADD" | "FADDP" | "FBLD" |
    "FBSTP" | "FCHS" | "FCLEX" | "FCMOVB" | "FCMOVBE" |
    "FCMOVE" | "FCMOVNB" |
    "FCMOVNBE" | "FCMOVNE" | "FCMOVNU" | "FCMOVU" | "FCOM" |
    "FCOMI" | "FCOMIP" | "FCOMP" | "FCOMPP" | "FCOS" |
    "FDECSTP" | "FDIV" | "FDIVP" | "FDIVR" | "FDIVRP" |
    "FFREE" | "FIADD" | "FICOM" | "FICOMP" | "FIDIV" |
    "FIDIVR" | "FILD" | "FIMUL" | "FINCSTP" | "FINIT" |
    "FIST" | "FISTP" | "FISUB" | "FISUBR" | "FLD1" |
    "FLDCW" | "FLDENV" | "FLDL2E" | "FLDL2T" | "FLDLG2" |
    "FLDLN2" | "FLDPI" | "FLDZ" | "FMUL" | "FMULP" |
    "FNCLEX" | "FNINIT" | "FNOP" | "FNSAVE" | "FNSTCW" |
    "FNSTENV" | "FNSTSW" | "FPATAN" | "FPREM" | "FPREMI" |
    "FPTAN" | "FRNDINT" | "FRSTOR" | "FSAVE" | "FSCALE" |
    "FSIN" | "FSINCOS" | "FSQRT" | "FST" | "FSTCW" |
    "FSTENV" | "FSTP" | "FSTSW" | "FSUB" | "FSUBP" |
    "FSUBR" | "FSUBRP" | "FTST" | "FUCOM" | "FUCOMI" |
    "FUCOMIP" | "FUCOMP" | "FUCOMPP" | "FWAIT" | "FXAM" |
    "FXCH" | "FXRSTOR" | "FXSAVE" | "FXTRACT" | "FYL2X" |
    "FYL2XP1" | "HLT" | "IDIV" | "IMUL" | "IN" |
    "INC" | "INS" | "INSB" | "INSD" | "INSW" |
    "INT" | "INTO" | "INVD" | "INVLPG" | "IRET" |
    "JA" | "JAE" | "JB" | "JBE" | "JC" |
    "JCXZ" | "JE" | "JECXZ" | "JG" | "JGE" |
    "JL" | "JLE" | "JMP" | "JNA" | "JNAE" |
    "JNB" | "JNBE" | "JNC" | "JNE" | "JNG" |
    "JNGE" | "JNL" | "JNLE" | "JNO" | "JNP" |
    "JNS" | "JNZ" | "JO" | "JP" | "JPE" |
    "JPO" | "JS" | "JZ" | "LAHF" | "LAR" |
    "LDMXCSR" | "LDS" | "LEA" | "LEAVE" | "LES" |
    "LFS" | "LGDT" | "LGS" | "LIDT" | "LLDT" |
    "LMSW" | "LOCK" | "LODS" | "LODSB" | "LODSD" |
    "LODSW" | "LOOP" | "LOOPE" | "LOOPNE" | "LOOPNZ" |
    "LOOPZ" | "LSL" | "LSS" | "LTR" | "MASKMOVQ" |
    "MAXPS" | "MAXSS" | "MINPS" | "MINSS" | "MOV" |
    "MOVAPS" | "MOVD" | "MOVHLPS" | "MOVHPS" | "MOVLHPS" |
    "MOVLPS" | "MOVMSKPS" | "MOVNTPS" | "MOVNTQ" | "MOVQ" |
    "MOVS" | "MOVSB" | "MOVSD" | "MOVSS" | "MOVSW" |
    "MOVSX" | "MOVUPS" | "MOVZX" | "MUL" | "MULPS" |
    "MULSS" | "NEG" | "NOP" | "NOT" | "OR" |
    "ORPS" | "OUT" | "OUTS" | "OUTSB" | "OUTSD" |
    "OUTSW" | "PACKSSDW" | "PACKSSWB" | "PACKUSWB" | "PADDB" |
    "PADDD" | "PADDSB" | "PADDSW" | "PADDUSB" | "PADDUSW" |
    "PADDW" | "PAND" | "PANDN" | "PAVGB" | "PAVGW" |
    "PCMPEQB" | "PCMPEQD" | "PCMPEQW" | "PCMPGTB" | "PCMPGTD" |
    "PCMPGTW" | "PEXTRW" | "PINSRW" | "PMADDWD" | "PMAXSW" |
    "PMAXUB" | "PMINSW" | "PMINUB" | "PMOVMSKB" | "PMULHUW" |
    "PMULHW" | "PMULLW" | "POP" | "POPA" | "POPAD" |
    "POPAW" | "POPF" | "POPFD" | "POPFW" | "POR" |
    "PREFETCH" | "PSADBW" | "PSHUFW" | "PSLLD" | "PSLLQ" |
    "PSLLW" | "PSRAD" | "PSRAW" | "PSRLD" | "PSRLQ" |
    "PSRLW" | "PSUBB" | "PSUBD" | "PSUBSB" | "PSUBSW" |
    "PSUBUSB" | "PSUBUSW" | "PSUBW" | "PUNPCKHBW" | "PUNPCKHDQ" |
    "PUNPCKHWD" | "PUNPCKLBW" | "PUNPCKLDQ" | "PUNPCKLWD" | "PUSH" |
    "PUSHA" | "PUSHAD" | "PUSHAW" | "PUSHF" | "PUSHFD" |
    "PUSHFW" | "PXOR" | "RCL" | "RCR" | "RDMSR" |
    "RDPMC" | "RDTSC" | "REP" | "REPE" | "REPNE" |
    "REPNZ" | "REPZ" | "RET" | "RETF" | "RETN" |
    "ROL" | "ROR" | "RSM" | "SAHF" | "SAL" |
    "SAR" | "SBB" | "SCAS" | "SCASB" | "SCASD" |
    "SCASW" | "SETA" | "SETAE" | "SETB" | "SETBE" |
    "SETC" | "SETE" | "SETG" | "SETGE" | "SETL" |
    "SETLE" | "SETNA" | "SETNAE" | "SETNB" | "SETNBE" |
    "SETNC" | "SETNE" | "SETNG" | "SETNGE" | "SETNL" |
    "SETNLE" | "SETNO" | "SETNP" | "SETNS" | "SETNZ" |
    "SETO" | "SETP" | "SETPE" | "SETPO" | "SETS" |
    "SETZ" | "SFENCE" | "SGDT" | "SHL" | "SHLD" |
    "SHR" | "SHRD" | "SHUFPS" | "SIDT" | "SLDT" |
    "SMSW" | "SQRTPS" | "SQRTSS" | "STC" | "STD" |
    "STI" | "STMXCSR" | "STOS" | "STOSB" | "STOSD" |
    "STOSW" | "STR" | "SUB" | "SUBPS" | "SUBSS" |
    "SYSENTER" | "SYSEXIT" | "TEST" | "UB2" | "UCOMISS" |
    "UNPCKHPS" | "UNPCKLPS" | "WAIT" | "WBINVD" | "VERR" |
    "VERW" | "WRMSR" | "XADD" | "XCHG" | "XLAT" |
    "XLATB" | "XOR" | "XORPS"                    { addToken(Token.RESERVED_WORD); }
}

<YYINITIAL> {

	{LineTerminator}				{ addNullToken(); return firstToken; }

	{WhiteSpace}+					{ addToken(Token.WHITESPACE); }

	{CharLiteral}					{ addToken(Token.LITERAL_CHAR); }
	{UnclosedCharLiteral}			{ addToken(Token.ERROR_CHAR); }
	{StringLiteral}				{ addToken(Token.LITERAL_STRING_DOUBLE_QUOTE); }
	{UnclosedStringLiteral}			{ addToken(Token.ERROR_STRING_DOUBLE); addNullToken(); return firstToken; }

	{Label}						{ addToken(Token.PREPROCESSOR); }

	^%({Letter}|{Digit})*			{ addToken(Token.FUNCTION); }

	{CommentBegin}.*				{ addToken(Token.COMMENT_EOL); addNullToken(); return firstToken; }

	{Operator}					{ addToken(Token.OPERATOR); }

	{Number}						{ addToken(Token.LITERAL_NUMBER_DECIMAL_INT); }
    {Hexadecimal}                   { addToken(Token.LITERAL_NUMBER_HEXADECIMAL); }

	<<EOF>>						{ addNullToken(); return firstToken; }

	{Identifier}					{ addToken(Token.IDENTIFIER); }
	.							{ addToken(Token.IDENTIFIER); }

}