/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractal.values;

import fractal.sys.FractalException;
import fractal.sys.FractalTypeException;

/**
 *
 * @author newts
 */
public class FractalInt extends FractalValue {
    int value;
    
    public FractalInt(int val) {
        super(FractalTypes.INTEGER);
        value = val;
    }
    
    @Override
    public boolean isInt() {
        return true;
    }
    
    @Override
    public int intValue() {
        return value;
    }
    
    @Override
    public double realValue() {
        return value;
    }
    
    @Override
    public FractalValue negate() {
        return FractalValue.make(- value);
    }
    
    @Override
    public FractalValue add(FractalValue val) throws FractalException {
        if (! val.isNumber()) {
            throw new FractalTypeException(FractalTypes.REAL, val.getType());
        } else if (val.isInt()) {
            return new FractalInt(intValue() + val.intValue());
        } else {
            return new FractalReal(realValue() + val.realValue());
        }
    }
    
    @Override
    public FractalValue sub(FractalValue val) throws FractalException {
        if (! val.isNumber()) {
            throw new FractalTypeException(FractalTypes.REAL, val.getType());
        } else if (val.isInt()) {
            return new FractalInt(intValue() - val.intValue());
        } else {
            return new FractalReal(realValue() - val.realValue());
        }
    }
    @Override
    public FractalValue mul(FractalValue val) throws FractalException {
        if (! val.isNumber()) {
            throw new FractalTypeException(FractalTypes.REAL, val.getType());
        } else if (val.isInt()) {
            return new FractalInt(intValue() * val.intValue());
        } else {
            return new FractalReal(realValue() * val.realValue());
        }
    }
    @Override
    public FractalValue div(FractalValue val) throws FractalException {
        if (! val.isNumber()) {
            throw new FractalTypeException(FractalTypes.REAL, val.getType());
        } else {
            return new FractalReal(realValue() / val.realValue());
        }
    }
    
    @Override
    public FractalValue mod(FractalValue val) throws FractalException {
        if (! val.isNumber()) {
            throw new FractalTypeException(FractalTypes.REAL, val.getType());
        } else if (val.isInt()) {
            return new FractalInt(intValue() % val.intValue());
        } else {
            return new FractalReal(realValue() % val.realValue());
        }
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
