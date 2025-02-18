/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractal.sys;

import fractal.syntax.ASTFractalForm;

/**
 * The exception raised when the stack in the state is empty, and a pop is 
 * attempted.
 * @author newts
 */
public class FractalRestoreException extends FractalException {
    private static final long serialVersionUID = 1L;
    
    public FractalRestoreException() {
        super("RESTORE Error: No state to restore!");
    }
    
    public FractalRestoreException(ASTFractalForm source) {
        super(source, "RESTORE Error: No state to restore!");
    }
    
}
