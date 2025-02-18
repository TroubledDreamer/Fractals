/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fractal.semantics;

import fractal.sys.FractalException;
import fractal.values.FractalPoint;
import fractal.values.FractalValue;
import java.awt.geom.Point2D;

/**
 * A utility class to succinctly represent a path in a way that if two instances
 * are equal, then it is highly likely that the paths were the same.
 * 
 * @author newts
 */
public class PathHash {
    
    private final static int MODULO = 1000000007;
    final int MULTIPLIER = 10; // only previous value multiplied, creating asymmetry
    
    private double absDistance;
    private double relDistance;
    Point2D hashPt;
    int count;
    
    public PathHash() {
        absDistance = 0;
        relDistance = 0;
        hashPt = new Point2D.Double(0, 0);
        count = 0;
    }
    
    private float modAdd(float v1, float v2) {
        return (v1 + v2) % MODULO;
    }
   
    /**
     * Local method of combining values. Operation is not commutative (ie order
     * matters).
     * @param v1 The first value
     * @param v2 The second value
     * @return The resulting hashed combination of the two.
     */
    private double combine(double v1, double v2) {
        return (v1 * MULTIPLIER + v2) % MODULO;
    }
    
    private void updateHashPt(double x, double y) {
       double hx = combine(hashPt.getX(), x);
       double hy = combine(hashPt.getY(), y);
       hashPt = new Point2D.Double(hx, hy);
    }
    
    /**
     *
     * @return The point representing the hash of the path.  (This is a value
     * used internally to determine the hash value).
     */
    public Point2D getHashPt() {
        return hashPt;
    }
    
    /**
     *
     * @return The current hash value of the implicit path, aggregated from all
     * the segments previously added.
     */
    public long getHash() {
        long result = (long) (hashPt.getX() * MODULO + hashPt.getY());
        return result;
    }
    
    /**
     * Add a segment to the path being hashed.
     * @param p The start point of the segment being added.
     * @param q The destination point of the segment being added.
     */
    public void addSegment(Point2D p, Point2D q) {
        double dx = q.getX() - p.getX();
        double dy = q.getY() - p.getY();
        count = (count + 1) % MODULO;
        updateHashPt(dx, dy);
    }
}
