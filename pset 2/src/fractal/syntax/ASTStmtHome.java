package fractal.syntax;

import fractal.semantics.Visitor;
import fractal.sys.FractalException;

/**
  Class ASTTCmdHome.
  Intermediate representation class autogenerated by CS34Q semantic generator.
  Created on Sat Oct 12 03:13:16 2013
*/
public class ASTStmtHome extends ASTStatement {

  public ASTStmtHome () {
  }

  @Override
  public <S, T> T visit(Visitor<S, T> v, S state) throws FractalException {
    return v.visitASTStmtHome(this, state);
  }
  
  @Override
  public String toString() {
      return "HOME()";
  }

}
