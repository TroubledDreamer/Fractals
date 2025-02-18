package fractal.values;

import fractal.sys.FractalException;
import fractal.sys.FractalTypeException;
import fractal.sys.FractalUnsupportedOpException;
import java.awt.geom.Point2D;

public class FractalValue {
    /**
     * The default value returned when statements are executed.
     */
    public static final FractalValue NO_VALUE = new FractalValue();
    
//    private Object val;
    private FractalTypes type;
    
    /**
     * Create an integer FRACTAL value instance
     * @param val the value of the integer
     * @return a new instance of FractalInt that wraps the given integer
     */
    public static FractalValue make(int val) {
        return new FractalInt(val);
    }
    
    /**
     * Create a real FRACTAL value instance
     * @param val the value of the real
     * @return a new instance of FractalReal that wraps the given real
     */
    public static FractalValue make(double val) {
        return new FractalReal(val);
    }
    
    /**
     * Create a point FRACTAL value instance
     * @param pt the point being wrapped
     * @return a new instance of FractalPoint that wraps the given point
     */
    public static FractalValue make(Point2D pt) {
        return new FractalPoint(pt.getX(), pt.getY());
    }
    
    /**
     * Create a point FRACTAL value instance
     * @param x the x-coordinate of the point being wrapped
     * @param y the y-coordinate of the point being wrapped
     * @return a new instance of FractalPoint that wraps the given point
     */
    public static FractalValue make(double x, double y) {
        return new FractalPoint(x, y);
    }
    
    protected FractalValue() {
        this.type = null;
    }
    
    protected FractalValue(FractalTypes type) {
        this.type = type;
    }
    
    protected void setType(FractalTypes t) {
        type = t;
    }

    public FractalTypes getType() {
        return type;
    }
    
    /**
     *
     * @return <code>true</code> if this value represents an integer
     */
    public boolean isInt() {
	return type == FractalTypes.INTEGER;
    }
    
    /**
     *
     * @return The integer that this value represents
     * @throws FractalException if this value does not represent an integer 
     */
    public int intValue() throws FractalException {
        throw new FractalTypeException(FractalTypes.INTEGER, type);
    }

    /**
     *
     * @return <code>true</code> if this value represents a floating point number
     */
    public boolean isReal() {
	return type == FractalTypes.REAL;
    }
    
    /**
     *
     * @return The floating point number that this value represents
     * @throws FractalException if this value does not represent a real number 
     */
    public double realValue() throws FractalException {
        throw new FractalTypeException(FractalTypes.REAL, type);
    }
    
    /**
     *
     * @return <code>true</code> if this value represents either an (exact) 
     * integer or a real (inexact floating point) number.
     */
    public boolean isNumber() {
        return isInt() || isReal();
    }

    /**
     *
     * @return <code>true</code> if this value is an instance of a fractal
     */
    public boolean isFractal() {
	return type == FractalTypes.FRACTAL;
    }
    
    public Fractal fractalValue() throws FractalException {
        if (type == FractalTypes.FRACTAL) 
            return (Fractal) this;
        else
            throw new FractalTypeException(FractalTypes.FRACTAL, type);
    }
    
    public boolean isPoint() {
        return type == FractalTypes.POINT;
    }
    
    public FractalPoint pointValue() throws FractalException {
        if (type == FractalTypes.POINT)
            return (FractalPoint) this;
        else
            throw new FractalTypeException(FractalTypes.POINT, type);
    }
    
    public FractalValue negate() throws FractalException {
        throw new FractalUnsupportedOpException(this, "negate");
    }
    
    /**
     * Add two values
     * @param val The value to be added to this one.
     * @return The sum of the two values
     * @throws FractalException if the types of this value and the given one are
     * incompatible.
     */
    public FractalValue add(FractalValue val) throws FractalException {
        throw new FractalTypeException(type, val.getType());
    }
    
    /**
     * Subtract two values
     * @param val The value to be subtracted from this one.
     * @return The difference of the two values
     * @throws FractalException if the types of this value and the given one are
     * incompatible.
     */
    public FractalValue sub(FractalValue val) throws FractalException {
        throw new FractalTypeException(type, val.getType());
    }
    
    /**
     * Multiply two values
     * @param val The value to be multiplied by this one.
     * @return The product of the two values
     * @throws FractalException if the types of this value and the given one are
     * incompatible.
     */
    public FractalValue mul(FractalValue val) throws FractalException {
        throw new FractalTypeException(type, val.getType());
    }
    
    /**
     * Divide two values to find the quotient
     * @param val The divisor.
     * @return The quotient of this value and the given one.
     * @throws FractalException if the types of this value and the given one are
     * incompatible.
     */
    public FractalValue div(FractalValue val) throws FractalException {
        throw new FractalTypeException(type, val.getType());
    }
    
    /**
     * Divide two values to find the remainder
     * @param val The divisor
     * @return The modulo (remainder) of this value and the given one.
     * @throws FractalException if the types of this value and the given one are
     * incompatible.
     */
    public FractalValue mod(FractalValue val) throws FractalException {
        throw new FractalTypeException(type, val.getType());
    }
    
    @Override
    public String toString() {
        if (type == null) {
            return "NO VALUE";
        } else
            return "<Unimplemented toString() for " + type + ">";
    }
    
}
