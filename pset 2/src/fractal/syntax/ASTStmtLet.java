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
public class ASTStmtLet extends ASTStatement {
    
    String id;
    
    public ASTStmtLet(String id, ASTExp ptExp) {
        super("LET " + id, ptExp);
        this.id = id;
    }

    /**
     *
     * @return The name of the variable being bound.
     */
    public String getId() {
        return id;
    }
    
    /**
     *
     * @return The point expression binding the variable.
     */
    public ASTExp getPtExp() {
        return (ASTExp) this.getSubTree(0);
    }

    @Override
    public <S, T> T visit(Visitor<S, T> v, S state) throws FractalException {
        return v.visitASTStmtLet(this, state);
    }
    
}
