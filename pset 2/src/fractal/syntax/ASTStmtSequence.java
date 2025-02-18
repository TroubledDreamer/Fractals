package fractal.syntax;

import fractal.semantics.Visitor;
import fractal.sys.FractalException;
import java.util.*;

public class ASTStmtSequence extends ASTStatement {

    ArrayList<ASTStatement> seq;		// sequence of commands

    public ASTStmtSequence() {
	seq = new ArrayList<>();
    }
    
    public ASTStmtSequence(ArrayList<ASTStatement> stmts) {
        seq = stmts;
    }

    public ASTStmtSequence(ASTStatement s) {
	this();
	seq.add(s);
    }

    public ArrayList<ASTStatement> getSeq() {
	return seq;
    }

    public ASTStmtSequence add(ASTStatement s) {
	seq.add(s);
	return this;
    }

    @Override
    public <S, T> T visit(Visitor<S, T> v, S arg) throws FractalException
    {
	return v.visitASTStmtSequence(this, arg);
    }

    @Override
    public String toString() {
        return toString(0);
    }
    
    public String toString(int indent) {
	StringBuilder result = new StringBuilder();
        StringBuilder sep = new StringBuilder();
        for (int i = 0; i < indent; i++) {
                sep.append(" ");
            }

        for (ASTStatement stmt : seq) {
            result.append(sep).append(stmt.toString()).append("\n");
        }
        
	return result.toString();
    }
    
    

}

