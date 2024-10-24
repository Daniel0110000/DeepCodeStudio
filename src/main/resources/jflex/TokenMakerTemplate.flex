import java.io.*;
import javax.swing.text.Segment;
import org.fife.ui.rsyntaxtextarea.*;

%%

%public
%class TokenMarkerTemplate
%extends AbstractJFlexTokenMaker
%unicode
%ignorecase
%type org.fife.ui.rsyntaxtextarea.Token

%{

    public TokenMarkerTemplate() {
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
    {{INSTRUCTIONS}} { addToken(Token.RESERVED_WORD); }
    {{VARIABLES}} { addToken(Token.VARIABLE); }
    {{CONSTANTS}} { addToken(Token.VARIABLE); }
    {{SEGMENTS}} { addToken(Token.FUNCTION); }
    {{SYSTEM_CALLS}} { addToken(Token.PREPROCESSOR); }
    {{REGISTERS}} { addToken(Token.RESERVED_WORD_2); }
    {{ARITH_INSTRUCTIONS}} { addToken(Token.OPERATOR); }
    {{LOGICAL_INSTRUCTIONS}} { addToken(Token.OPERATOR); }
    {{CONDITIONS}} { addToken(Token.RESERVED_WORD); }
    {{MEMORY_MANAGEMENTS}} { addToken(Token.PREPROCESSOR); }
}

<YYINITIAL> {
    {LineTerminator}				{ addNullToken(); return firstToken; }

    {WhiteSpace}+					{ addToken(Token.WHITESPACE); }

    {CharLiteral}					{ addToken(Token.LITERAL_CHAR); }
    {UnclosedCharLiteral}			{ addToken(Token.ERROR_CHAR); }
    {StringLiteral}				{ addToken(Token.LITERAL_STRING_DOUBLE_QUOTE); }
    {UnclosedStringLiteral}			{ addToken(Token.ERROR_STRING_DOUBLE); addNullToken(); return firstToken; }

    {Label}						{ addToken(Token.FUNCTION); }

    {CommentBegin}.*				{ addToken(Token.COMMENT_EOL); addNullToken(); return firstToken; }

    {Operator}					{ addToken(Token.OPERATOR); }

    {Number}						{ addToken(Token.LITERAL_NUMBER_DECIMAL_INT); }
    {Hexadecimal}                   { addToken(Token.LITERAL_NUMBER_HEXADECIMAL); }

    <<EOF>> { addNullToken(); return firstToken; }

    {Identifier}					{ addToken(Token.IDENTIFIER); }
    .							{ addToken(Token.IDENTIFIER); }

}