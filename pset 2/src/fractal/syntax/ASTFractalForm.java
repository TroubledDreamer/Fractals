package fractal.syntax;

import fractal.semantics.Visitor;
import fractal.sys.FractalException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib3652.util.ASTNode;
import lib3652.util.ASTVisitor;
import lib3652.util.VisitException;

/**
 * Abstract parent IR for all FRACTAL forms (statements and expressions).
 * 
 * @author newts
 */
public abstract class ASTFractalForm extends ASTNode<ASTFractalForm> {
    
    public ASTFractalForm(String name, ASTFractalForm... subForms) {
        super(name, subForms);
    }

    /**
     * The generic visit method used to allow an object that implements the
     * Visitor interface to traverse the subtree rooted at this node.  The 
     * implementation of this method determines which method of the Visitor
     * will be called, which allows all the behaviours associated with a 
     * particular activity (e.g. intrepreting or compiling), to be consolidated
     * within the implementation of the Visitor.  The more direct implementation
     * of that activity would require individual methods be spread across all
     * the subclasses of this class, which makes maintenance more tedious.
     * @param <S> The type of the input argument used by the visitor
     * @param <T> The type of the output returned by the visitor
     * @param v The visitor instance
     * @param state The argument required by the visitor, usually some sort of state
     * @return The result of visiting the subtree rooted at this node, according
     * to the rules implemented by the visitor supplied.
     * @throws FractalException if the visitor encounters a problem while traversing
     * the tree rooted at this node.
     */
    public abstract <S, T> T visit(Visitor<S, T> v, S state) throws FractalException;

    @Override
    public <S, T> T visit(ASTVisitor<S, T> v, S arg) throws VisitException {
        Visitor<S, T> fractalVisitor = (Visitor<S, T>) v;
        
        try {
            return visit(fractalVisitor, arg);
        } catch (FractalException ex) {
            throw new VisitException(ex.getMessage(), ex);
        }
    }

    @Override
    public String toString() {
        return getName();
    }
    
}