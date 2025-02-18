/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fractal.syntax;

import fractal.semantics.Visitor;
import fractal.sys.FractalException;

/**
 *
 * @author newts
 */
public class ASTExpHere extends ASTExp {
    
    public ASTExpHere() {
        super("HERE");
    }

    @Override
    public <S, T> T visit(Visitor<S, T> v, S state) throws FractalException {
        return v.visitASTExpHere(this, state);
    }
    
}
