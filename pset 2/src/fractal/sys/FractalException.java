/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractal.sys;

import fractal.syntax.ASTFractalForm;
import lib3652.util.VisitException;

/**
 * Parent class for all runtime exceptions that may be thrown while evaluating
 * a FRACTAL program.
 * 
 * @author newts
 */
public class FractalException extends VisitException {
    private static final long serialVersionUID = 1L;
    
    private ASTFractalForm source;
    
    private static String mkMessage(String message, ASTFractalForm form) {
        return String.format("Error: %s when evaluating %s", 
                               message, form);
    }
    
    public FractalException() {
        super();
    }
    
    public FractalException(String message) {
        super (message);
    }
    
    public FractalException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public FractalException(ASTFractalForm form) {
        super(String.format("Error in %s", form));
        source = form;
    }
    
    public FractalException(ASTFractalForm form, String message) {
        super(mkMessage(message, form));
        source = form;
    }
    
    public FractalException(ASTFractalForm node, String message, Throwable cause) {
        super(message, cause);
        source = node;
    }
    
    public ASTFractalForm getSource() {
        return source;
    }
    
    protected void setSource(ASTFractalForm src) {
        source = src;
    }
}
