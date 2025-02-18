/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractal.sys;

/**
 *
 * @author newts
 */
public class FractalLexerException extends FractalException {
    private static final long serialVersionUID = 1L;
    
    String badToken;
    int lineNo;
    int column;
    
    public FractalLexerException(String token, int line, int col) {
        super("Unrecognised token " + token + " encountered on line: " + line + ", column: " + col);
        badToken = token;
        lineNo = line;
        column = col;
    }

    public String getBadToken() {
        return badToken;
    }

    public int getLineNo() {
        return lineNo;
    }

    public int getColumn() {
        return column;
    }
}
