/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fractal.semantics;

import fractal.syntax.ASTDrawFractal;
import fractal.syntax.ASTDrawLine;
import fractal.syntax.ASTExp;
import fractal.syntax.ASTExpAdd;
import fractal.syntax.ASTExpDiv;
import fractal.syntax.ASTExpHere;
import fractal.syntax.ASTExpLit;
import fractal.syntax.ASTExpMod;
import fractal.syntax.ASTExpMul;
import fractal.syntax.ASTExpPtVar;
import fractal.syntax.ASTExpSub;
import fractal.syntax.ASTExpVar;
import fractal.syntax.ASTFracVar;
import fractal.syntax.ASTDefFractal;
import fractal.syntax.ASTProgram;
import fractal.syntax.ASTDrawSelf;
import fractal.syntax.ASTExpPtCCRot;
import fractal.syntax.ASTExpNegate;
import fractal.syntax.ASTStatement;
import fractal.syntax.ASTStmtSequence;
import fractal.syntax.ASTStmtClear;
import fractal.syntax.ASTStmtHome;
import fractal.syntax.ASTStmtLet;
import fractal.sys.FractalException;
import fractal.values.Fractal;
import fractal.values.FractalPoint;
import fractal.values.FractalReal;
import fractal.values.FractalValue;

/**
 *
 * @author newts
 */
public class FractalEvaluator extends AbstractFractalEvaluator {
    
    public FractalEvaluator() {
        super();
    }

    @Override
    public FractalValue visitFractalProgram(ASTProgram program, FractalState state) throws FractalException {
        // if any operations should happen between programs, but not in other 
        // code blocks, then they would go in this method.
        // (It is not anticipated that you need to edit this method.)
        
        ASTStmtSequence stmts = program.getStatements();
        FractalValue result = visitASTStmtSequence(stmts, state);
        // The following hash results are supposed to be identical for the same
        // fractal rendered between the same end points.  
        System.out.println(String.format("Resultant Pt: %s", state.getPathResultant()));
        System.out.println(String.format("Hash: %x", state.getPathHash()));
        
        // The return values based on combining the detination points that arise 
        // are also supposed to be unique to the fractal and scale, but are 
        // dependent on your FractalEvaluator implementation.
        return result;
    }

    @Override
    public FractalValue visitASTStmtSequence(ASTStmtSequence seq, FractalState state) throws FractalException {
        FractalPoint result = FractalPoint.ORIGIN;
        for (ASTStatement s : seq.getSeq()) {
            result = combine(result, s.visit(this, state));
        }
        return result;
    }

    @Override
    public FractalValue visitASTFracVar(ASTFracVar form, FractalState state) throws FractalException {
        String id = form.getVar();
        return state.lookup(id);
    }

    @Override
    public FractalValue visitASTDefFractal(ASTDefFractal form, FractalState state) throws FractalException {
        String name = form.getFracName();
        ASTStmtSequence bod = form.getBody();
        FractalValue result = new Fractal(name, bod, state);
        state.bind(name, result);
        return result;
    }
    
    @Override
    public FractalValue visitASTDrawFractal(ASTDrawFractal form, FractalState state) throws FractalException {
        String fractalName = form.getFractalName();
        Fractal fractal = state.lookup(fractalName).fractalValue();
        ASTExp srcExp = form.getSrcExp();
        ASTExp destExp = form.getDestExp();

        FractalPoint srcPt = state.getCurrentPoint();
        FractalPoint destPt = destExp.visit(this, state).pointValue();

        if (srcExp != null) {
            srcPt = srcExp.visit(this, state).pointValue();
        }

        FractalState newState = state.deriveState(srcPt, destPt);
        state.startFractal(fractal);

        FractalValue result = fractal.getBody().visit(this, newState);

        state.stopFractal();
        return result;
    }



    @Override
    public FractalValue visitASTDrawSelf(ASTDrawSelf form, FractalState state) throws FractalException {
        // extract the source and destination expressions from the form
        ASTExp srcExp = form.getSrcExp();
        ASTExp destExp = form.getDestExp();

        // compute the source and destination points
        FractalPoint srcPt = state.getCurrentPoint(); 
        FractalPoint destPt = destExp.visit(this, state).pointValue();

        if (state.isResolvable()) {
                FractalState newState = state.deriveState(srcPt, destPt);
                Fractal fractal = state.getCurrentFractal();
                FractalValue result = fractal.getBody().visit(this, newState);
       
            return combine(destPt, result);
        } else {
            state.lineTo(destPt);
            return destPt;
        }
        //+++++++++++
        // if the current coordinate frame is resolvable for the current depth
        // then derive a new state scaled to the source and destination points
        //         and render (visit) the current fractal's body w.r.t it.
        //         combine the destination with the result of the rendering in 
        //            the new frame and return the result
        // else draw a line between the points (use state.lineTo).
    }
    
    @Override
    public FractalValue visitASTDrawLine(ASTDrawLine form, FractalState state) 
            throws FractalException {
        ASTExp srcExp = form.getSrcExp();
        ASTExp destExp = form.getDestExp();
        FractalPoint destPt = destExp.visit(this, state).pointValue();
        if (srcExp == null) {
            state.lineTo(destPt);
        } else {
            FractalPoint srcPt = srcExp.visit(this, state).pointValue();
            state.moveTo(srcPt);  // make sure not to draw a line to source
            state.lineTo(destPt);
        }
        return destPt;  // return the destination point for all draw commands
    }

    @Override
    public FractalValue visitASTStmtClear(ASTStmtClear form, FractalState state) throws FractalException {
        // implement this
        state.clear();
        return FractalValue.NO_VALUE;
    }

    @Override
    public FractalValue visitASTStmtHome(ASTStmtHome form, FractalState state) throws FractalException {
        // implement me
        // reset the current point to the origin
        state.moveTo(FractalPoint.ORIGIN);


        return FractalValue.NO_VALUE;
    }
    
    @Override
    public FractalValue visitASTExpHere(ASTExpHere form, FractalState state) throws FractalException {
        // This should be a 1-liner.  Look in FractalState.java
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public FractalValue visitASTStmtLet(ASTStmtLet form, FractalState state) throws FractalException {
        // implement me
        // bind the variable to the value of the expression in the current state
        state.bind(form.getId(), form.getPtExp().visit(this, state));
        return FractalValue.NO_VALUE;
    }

    @Override
    public FractalValue visitASTExpAdd(ASTExpAdd form, FractalState state) throws FractalException {
        ASTExp e1 = form.getFirst();
        ASTExp e2 = form.getSecond();
        return e1.visit(this, state).add(e2.visit(this, state));
    }

    @Override
    public FractalValue visitASTExpSub(ASTExpSub form, FractalState state) throws FractalException {
        ASTExp e1 = form.getFirst();
        ASTExp e2 = form.getSecond();
        return e1.visit(this, state).sub(e2.visit(this, state));
    }

    @Override
    public FractalValue visitASTExpMul(ASTExpMul form, FractalState state) throws FractalException {
        ASTExp e1 = form.getFirst();
        ASTExp e2 = form.getSecond();
        return e1.visit(this, state).mul(e2.visit(this, state));
    }

    @Override
    public FractalValue visitASTExpDiv(ASTExpDiv form, FractalState state) throws FractalException {
        ASTExp e1 = form.getFirst();
        ASTExp e2 = form.getSecond();
        return e1.visit(this, state).div(e2.visit(this, state));
    }

    @Override
    public FractalValue visitASTExpMod(ASTExpMod form, FractalState state) throws FractalException {
        ASTExp e1 = form.getFirst();
        ASTExp e2 = form.getSecond();
        return e1.visit(this, state).mod(e2.visit(this, state));
    }

    @Override
    public FractalValue visitASTExpLit(ASTExpLit form, FractalState state) throws FractalException {
        return form.getValue();
    }

    @Override
    public FractalValue visitASTExpVar(ASTExpVar form, FractalState state) throws FractalException {
        return state.lookup(form.getVar());
    }

    @Override
    public FractalValue visitASTExpPtVar(ASTExpPtVar form, FractalState state) throws FractalException {
        return state.lookup(form.getVar());
    } 

    @Override
    public FractalValue visitASTExpPtCCRot(ASTExpPtCCRot form, FractalState state) throws FractalException {
        ASTExp angleExp = form.getAngleExp();
        FractalValue angleVal = angleExp.visit(this, state);
        double angle = angleVal.realValue();

        return new FractalPoint(Math.cos(angle), Math.sin(angle));
        // don't forget to convert the angle to radians
        // eventually return the point (cos angle, sin angle)
    }

    @Override
    public FractalValue visitASTExpNegate(ASTExpNegate form, FractalState state) throws FractalException {
        return form.getExp().visit(this, state).negate();
    }
  
}
