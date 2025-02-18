/* Specification for Fractal tokens */

// user customisations

package fractal.syntax;
import java_cup.runtime.*;
import fractal.sys.FractalException;
import fractal.sys.FractalLexerException;

// JFlex directives
    
%%

%cup
%public

%class FractalLexer
%throws FractalException

%type java_cup.runtime.Symbol

%eofval{
	return new Symbol(sym.EOF);
%eofval}

%eofclose false

%char
%column
%line

%{
    private Symbol mkSymbol(int id) {
        return new Symbol( (int) id, (int) yychar - yytext().length(), (int) yychar);
    }

    private Symbol mkSymbol(int id, Object val) {
        yychar = yytext().length();
        return new Symbol( (int) id, (int) yychar, (int) yychar, val);
    }

    public long getChar() {
	return yychar + 1;
    }

    public int getColumn() {
    	return yycolumn + 1;
    }

    public int getLine() {
	return yyline + 1;
    }

    public String getText() {
	return yytext();
    }
%}

nl = [\n\r]

cc = ([\b\f]|{nl})

ws = {cc}|[\t ]

digit = [0-9]

lalpha = [a-z]
ualpha = [A-Z]

alpha = {lalpha}|{ualpha}|[_]

alphnum = {digit}|{alpha}



%%

<YYINITIAL>	{nl}	{
			 //skip newline
			}
<YYINITIAL>	{ws}	{
			 // skip whitespace
			}

<YYINITIAL>	"//".*	{
			 // skip line comments
			}

<YYINITIAL> {
    "+"			{return mkSymbol(sym.PLUS);}
    "-"			{return mkSymbol(sym.MINUS);}
    "*"			{return mkSymbol(sym.MUL);}
    "/"			{return mkSymbol(sym.DIV);}
    "%"			{return mkSymbol(sym.MOD);}

    "@"                 {return mkSymbol(sym.CCROT);}
    "X"                 {return mkSymbol(sym.X_UNIT);}
    "Y"                 {return mkSymbol(sym.Y_UNIT);}
    "O"                 {return mkSymbol(sym.ORIGIN);}




    "="                 {return mkSymbol(sym.EQUAL);}

    "("			{return mkSymbol(sym.LPAREN);}
    ")"			{return mkSymbol(sym.RPAREN);}

    ","			{return mkSymbol(sym.COMMA);}
    ":"			{return mkSymbol(sym.COLON);}
    ";"			{return mkSymbol(sym.SEMI);}	



    "to"        {return mkSymbol(sym.TO);}
    "end"       {return mkSymbol(sym.END);}
    "self"      {return mkSymbol(sym.SELF);}
    "line"      {return mkSymbol(sym.LINE);}
    "draw"      {return mkSymbol(sym.DRAW);}
    "here"      {return mkSymbol(sym.HERE);}
    "from"      {return mkSymbol(sym.FROM);}



    "clear"|"clr"	{return mkSymbol(sym.CLEAR);}
    "home"		{return mkSymbol(sym.HOME);}

    "let"|"LET"	        {return mkSymbol(sym.LET);}
    "fractal"		{return mkSymbol(sym.FRACTAL);}

    {digit}+ 		{
			 // INTEGER
	       		 return mkSymbol(sym.INT, 
			 	         Integer.parseInt(yytext()));
	       		}

    {digit}*"."{digit}+ {
	       		 // REAL
	       		 return mkSymbol(sym.REAL, Double.parseDouble(yytext()));
	       		}

    n_{alphnum}+   {
    		      	 // POINT IDENTIFIERS
	       		 return mkSymbol(sym.NUM_ID, yytext());
	       		}


    {lalpha}+{alphnum} {
    		      	 // Fractal IDENTIFIERS
	       		 return mkSymbol(sym.FRACTAL_ID, yytext());
	       		}
    
    {ualpha}+|{ualpha}+_{alphnum} {
    		      	 // Fractal IDENTIFIERS
	       		 return mkSymbol(sym.POINT_ID, yytext());
	       		}

    

    .			{ // ** Do not add any rules below here **
                          // Unknown token
    			  throw new FractalLexerException(yytext(), getLine(),
							  getColumn());
    			}
}
