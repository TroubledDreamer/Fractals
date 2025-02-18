/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package fractal.sys;

import fractal.syntax.ASTFractalForm;
import fractal.values.FractalValue;

/**
 *
 * @author newts
 */
public class FractalUnsupportedOpException extends FractalException {

    /**
     * Creates a new instance of <code>FractalUnsupportedOpException</code>
     * without detail message.
     */
    public FractalUnsupportedOpException(String op) {
        super("Unsupported operation: " + op);
    }

    /**
     * Constructs an instance of <code>FractalUnsupportedOpException</code> with
     * the specified detail message.
     *
     * @param op the detail message.
     */
    public FractalUnsupportedOpException(FractalValue val, String op) {
        super(String.format("%s doesn not support operation: %s", val,op));
    }
}
