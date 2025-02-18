/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractal.syntax;

/**
 *
 * @author newts
 */
public abstract class ASTStatement extends ASTFractalForm {
    
    public ASTStatement() {
        super("? STATEMENT");
    }
    
    public ASTStatement(String stmtName, ASTFractalForm... args) {
        super(stmtName, args);
    }
    
}
