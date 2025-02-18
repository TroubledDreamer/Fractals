/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fractal.values;

import fractal.sys.FractalException;

/**
 * A representation of a Point with floating point precision that falls under
 * the FractalValue class hierarchy.
 * @author newts
 */
public class FractalPoint extends FractalValue {
    
    public static final FractalPoint ORIGIN = new FractalPoint(0, 0);
    public static final FractalPoint X_UNIT = new FractalPoint(1, 0);
    public static final FractalPoint Y_UNIT = new FractalPoint(0, 1);
    
    float x;
    float y;
    
    public FractalPoint(float x, float y) {
        super(FractalTypes.POINT);
        this.x = x;
        this.y = y;
    }
    
    public FractalPoint(double x, double y) {
        super(FractalTypes.POINT);
        this.x = (float) x;
        this.y = (float) y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
    protected static final float TOLERANCE = 0.0000001F; 
    
    /**
     * Return true if the coordinates of the given point are the same as this 
     * one's.
     * @param other The point being compared to this one
     * @return true if the given point has the same coordinates as this one.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof FractalPoint op) {
            return (Math.abs(x - op.x) < TOLERANCE && 
                    Math.abs(y - op.y) < TOLERANCE);
        } else
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Float.floatToIntBits(this.x);
        hash = 89 * hash + Float.floatToIntBits(this.y);
        return hash;
    }
    
    @Override
    public String toString() {
        return String.format("POINT: (%.6f, %.6f)", x, y);
    }
    
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }
    
    /**
     * Return the (Euclidean) distance of this point from the given one.
     * @param p The given point
     * @return The length of the straight line between the given point and this
     * one.
     */
    public double distanceFrom(FractalPoint p) {
        double xd = x - p.x;
        double yd = y - p.y;
        return Math.sqrt(xd * xd + yd * yd);
    }
    
    public FractalPoint invert() {
        double r = magnitude();
        return new FractalPoint((float) (x/r), -(float) (y/r));
    }
    
    /**
     * Create and return the point obtained by scaling this one by the given
     * scale factor (about the origin).
     * @param sf The factor by which to scale this point.
     * @return The point that is an sf factor further away from the origin than
     * this point.
     */
    public FractalPoint scale(float sf) {
        return new FractalPoint(sf * x, sf * y);
    }
        
    @Override
    public FractalValue negate() {
        return FractalValue.make(- x, -y);
    }
    
    /**
     * The sum of two points is their vector sum.
     * @param val The value to be added.
     * @return The sum of this point and the given one.
     * @throws FractalException if the value given is incompatible with points
     * under addition.
     */
    @Override
    public FractalValue add(FractalValue val) throws FractalException {
        if (val.isPoint()) {
            return add(val.pointValue());
        } else {
            return super.add(val);
        }
    }
    
    public FractalPoint add(FractalPoint pv) {
        return new FractalPoint(x + pv.x, y + pv.y);
    }
    
    /**
     * The difference of two points is their vector difference.
     * @param val The value to be subtracted
     * @return The vector difference of the two points as a point.
     * @throws FractalException if the value given is incompatible with points
     * under subtraction.
     */
    @Override
    public FractalValue sub(FractalValue val) throws FractalException {
        if (val.isPoint()) {
            return sub(val.pointValue());
        } else {
            return super.sub(val);
        }
    }
    
    public FractalPoint sub(FractalPoint pv) {
        return new FractalPoint(x - pv.x, y - pv.y);
    }
    
    /**
     * The product of two points is the rotation of the first by the angle that
     * the second makes with the X-axis, followed by a scaling by the magnitude
     * of the second.  The resulting vector is returned as a
     * point.  (This is a commutative operation).
     * If the argument given is a scalar (integer or float) then the result
     * is this point scaled by the given quantity.
     * @param val The value to be multiplied.
     * @return The product of this point and the given one.
     * @throws FractalException if the value given is incompatible with points
     * under (post) multiplication.
     */
    @Override
    public FractalValue mul(FractalValue val) throws FractalException {
        if (val.isPoint()) {
            return mul(val.pointValue());
        } else if (val.isNumber()) {
            float scale = (float) val.realValue();
            return new FractalPoint(scale * x, scale * y);
        } else {
            return super.mul(val);
        }
    }
    
    public FractalPoint mul(FractalPoint pv) {
        return new FractalPoint(x * pv.x - y * pv.y, x * pv.y + y * pv.x);
    }
        
    /**
     * The quotient of two points is the clockwise rotation of the first by the 
     * angle the second makes with the X-axis, divided by the magnitude of the 
     * second.  The resulting vector is returned as a
     * point.  (This is not a commutative operation).  
     * If the argument given is a scalar (integer or float) then the result
     * is this point (reduced) scaled by the given quantity.
     * @param val The value to be multiplied.
     * @return The product of this point and the given one.
     * @throws FractalException if the value given is incompatible with points
     * under (post) multiplication.
     */
    @Override
    public FractalValue div(FractalValue val) throws FractalException {
        if (val.isPoint()) {
            return div(val.pointValue());
        } else if (val.isNumber()) {
            float scale = (float) (1/val.realValue());
            return new FractalPoint(scale * x, scale * y);
        } else {
            return super.mul(val);
        }
    }
    
    public FractalPoint div(FractalPoint pv) {
        FractalPoint pvInv = pv.invert();            
            return new FractalPoint(x * pvInv.x - y * pvInv.y, 
                    x * pvInv.y + y * pvInv.x);
    }
    
    /**
     * The modulo of two points is normal to the seconf point that terminates at
     * the first. It can be calculated by subtracting the projection of the first 
     * point on the second from the first point.   The resulting vector is 
     * returned as a point.  (This is not a commutative operation).  The 
     * computation is performed by taking the dot-product of the vectors and 
     * scaling the unit vector parallel to the 2nd point by that quantity.
     * If the argument given is a scalar (integer or float) then the result
     * is this point scaled by the given quantity.
     * @param val The value to be multiplied.
     * @return The product of this point and the given one.
     * @throws FractalException if the value given is incompatible with points
     * under (post) multiplication.
     */
    @Override
    public FractalValue mod(FractalValue val) throws FractalException {
        if (val.isPoint()) {
            return mod(val.pointValue());
        } else if (val.isInt()) {
            int divisor = val.intValue();
            return new FractalPoint(x % divisor, y % divisor);
        } else {
            return super.mod(val);
        }
    }
    
    public FractalPoint mod(FractalPoint pv) {
        return sub(proj(pv));
    }
    
    /**
     * Project this point onto a given direction.
     * @param pt The point representing the direction onto which this point will
     * be projected.
     * @return The image of this point under the projection described.
     */
    public FractalPoint proj(FractalPoint pt) {
        double proj = x * pt.x + y * pt.y;
        float scale = (float) (proj/pt.magnitude());
        return new FractalPoint(scale * pt.x, scale * pt.y);
    }

}
