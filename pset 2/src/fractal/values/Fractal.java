/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractal.values;

import fractal.semantics.FractalState;
import fractal.syntax.ASTStmtSequence;

/**
 *
 * @author newts
 */
public class Fractal extends FractalValue {
    
    String name;
    FractalState state;
    ASTStmtSequence body;
    // double scaleVal;
    
    public Fractal(String name, ASTStmtSequence body, FractalState st) {
        super(FractalTypes.FRACTAL);
        this.name = name;
        this.body = body;
        state = st;
    }
       
    @Override
    public boolean isFractal() {
        return true;
    }
    
    /**
     *
     * @return The name of this fractal
     */
    public String getName() {
        return name;
    }
    
    /**
     *
     * @return The code defining the actions of this fractal
     */
    public ASTStmtSequence getBody() {
        return body;
    }
    
    /**
     *
     * @return The state associated with this Fractal instance.
     */
    public FractalState getState() {
        return state;
    }
    
    @Override
    public String toString() {
        return String.format("<Fractal %s>", name);
    }
    
    /**
     *
     * @return A longer description of this fractal, than the toString() method.
     */
    public String toLongString() {
        return toString();
    }
}
