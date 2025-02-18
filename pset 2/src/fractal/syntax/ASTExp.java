/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractal.syntax;

import fractal.semantics.Visitor;
import fractal.sys.FractalException;

/**
 *
 * @author newts
 */
public abstract class ASTExp extends ASTFractalForm {
    
    public ASTExp() {
        this("? EXP");
    }
    
    public ASTExp(String name, ASTFractalForm... subExps) {
        super(name, subExps);
    }
    
}
