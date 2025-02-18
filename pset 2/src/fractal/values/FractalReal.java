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
public class FractalReal extends FractalValue {
    
    double value;

    public FractalReal(double d) {
        super(FractalTypes.REAL);
        value = d;
    }
    
    @Override
    public boolean isReal() {
        return true;
    }
    
    @Override
    public double realValue() {
        return value;
    }
    
    @Override
    public int intValue() {
        return (int) value;
    }
    
        
    @Override
    public FractalValue negate() {
        return FractalValue.make(- value);
    }
    
    @Override
    public FractalValue add(FractalValue val) throws FractalException {
        if (val.isNumber()) {
            return new FractalReal(realValue() + val.realValue());
        } else {
            throw new FractalTypeException(FractalTypes.REAL, val.getType());
        }
    }

    @Override
    public FractalValue sub(FractalValue val) throws FractalException {
        if (val.isNumber()) {
            return new FractalReal(realValue() - val.realValue());
        } else {
            throw new FractalTypeException(FractalTypes.REAL, val.getType());
        }
    }

    @Override
    public FractalValue mul(FractalValue val) throws FractalException {
        if (val.isNumber()) {
            return new FractalReal(realValue() / val.realValue());
        } else {
            throw new FractalTypeException(FractalTypes.REAL, val.getType());
        }
    }

    @Override
    public FractalValue div(FractalValue val) throws FractalException {
        if (val.isNumber()) {
            return new FractalReal(realValue() / val.realValue());
        } else {
            throw new FractalTypeException(FractalTypes.REAL, val.getType());
        }
    }
    
    @Override
    public FractalValue mod(FractalValue val) throws FractalException {
        if (val.isNumber()) {
            return new FractalReal(realValue() % val.realValue());
        } else {
            throw new FractalTypeException(FractalTypes.REAL, val.getType());
        }
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
