/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractal.sys;

import fractal.syntax.ASTFractalForm;

/**
 *
 * @author newts
 */
public class FractalUnboundException extends FractalException {
    private static final long serialVersionUID = 1L;

    public FractalUnboundException(String message) {
        super(message);
    }

    public FractalUnboundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FractalUnboundException(ASTFractalForm form, String message) {
        super(form, message);
    }

    public FractalUnboundException(ASTFractalForm form, String message, Throwable cause) {
        super(form, message, cause);
    }
    
    public FractalUnboundException(ASTFractalForm form) {
        super(form, "Unbound variable " + form);
    }
}
