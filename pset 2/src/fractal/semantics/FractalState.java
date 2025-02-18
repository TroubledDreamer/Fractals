/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fractal.semantics;

import fractal.syntax.ASTStmtSequence;
import fractal.sys.FractalUnboundException;
import fractal.values.Fractal;
import fractal.values.FractalPoint;
import fractal.values.FractalReal;
import fractal.values.FractalValue;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for representing all of the contextual information needed to evaluate
 * FRACTAL forms. A FractalState instance is comprised of the following elements
 *  1. A Coordinate Frame (AffineTransform) to translate local points to screen 
 *     coordinates
 *  2. The current level (int) to represent the current depth of recursion of 
 *     the fractal
 *  3. The current path (GeneralPath) of lines shown on the screen
 *  4. A hash of that path (PathHash) to succinctly capture the entire path 
 *     drawn to support efficient comparison.
 *  5. A stack of active fractals (Stack<Fractal>). A reference to SELF is 
 *     always a reference to the current fractal (stored at the top of the stack).  
 *     When the drawing of a fractal is complete, the fractal should be popped 
 *     from the stack.
 *  6. An environment (Environment) to store active bindings. Since the 
 *     identifier patterns in FRACTAL are distinct for Fractals, Points and
 *     Numbers a single environment can be used to maintain all bindings without
 *     any name collisions.
 * 
 * The coordinate frame is completely determined initially by the screen 
 * coordinate system for the top level initial state.  Calls to DRAW on either a
 * fractal or SELF derive a new state from the current state and the end points
 * provided in the DRAW statement. The coordinate frame and level are 
 * automatically set under these circumstances, and the other components are 
 * shared (aliased). 
 * 
 * The path and its hash are managed internally by this class. It is therefore 
 * imperative that all line segments drawn are done using the drawing commands
 * (lineTo and moveTo) provided by this class. Direct modifications to the 
 * internal path will lead to incorrect hashes. 
 * 
 * @author newts
 */
public class FractalState {
    static final Point2D X_UNIT_VEC = new Point2D.Float(1, 0);
    static final Point2D ORIGIN = new Point2D.Float(0, 0);
    
    /**
     * The threshold below which fractals will be rendered as straight lines. 
     * If the coordinate frame of a fractal has a unit length (in screen 
     * coordinates) smaller than this threshold, then it will be rendered as
     * a line segment starting at the origin, running parallel to the X-axis.
     */
    float resolution = 0.1F; 
    
    /**
     * The max depth that any fractal will be rendered. This is present as a 
     * stop gap measure to prevent stack overflows in very high resolution 
     * situations.
     */
    final int maxDepth = 15;
    
    // FractalPoint origin;
    // FractalPoint xUnit; 
    
    /**
     * The transform that converts coordinates in the current coordinate
     * frame to screen coordinates (ie the coordinates expected by the component
     * that is rendering the path).
     */
    protected AffineTransform toScreenTransform;
    
    /**
     * The current depth of the self-similarity level of the current fractal 
     * being drawn.
     */
    protected int level;
    // These below are the shared components across all derived states.
    
    /**
     * The path traced out by the fractal.  All coordinates stored here should
     * be in screen device coordinates (i.e. they are "absolute" coordinates).
     */
    final protected GeneralPath path;
    
    /**
     * The hash of the current path. Reset after each CLEAR command is executed.
     */
    protected PathHash pathHash;
    
    /**
     * The sequence of most recent fractals being drawn in reverse order of
     * invocation. At the top level, this stack is empty, and it is an error
     * to reference the current fractal from the top level.
     */
    final protected Stack<Fractal> fractalStack;
    
    /**
     * The environment of all current name bindings.  Names for numbers, points
     * and fractals do not overlap, so they can still be maintained within a
     * single environment without any name collision or ambiguity. 
     */
    final protected Environment<FractalValue> env;

    private FractalState(GeneralPath path, PathHash pathHash, Stack<Fractal> fracStack, Environment<FractalValue> env){
        this.path = path;
        this.pathHash = pathHash;
        this.fractalStack = fracStack;
        this.env = env;
    }
    
    /**
     * Create a new state that is a clone of the given state.
     * @param state
     */
    protected FractalState(FractalState state) {
        this(state.path, state.pathHash, state.fractalStack, state.env);
        this.toScreenTransform = (AffineTransform) state.toScreenTransform.clone();
        this.level = state.level;
    }
    
    /**
     * Derive a state from the given one transforming its coordinate frame, and
     * incrementing its rendering level by 1.
     * @param coordTransform The transform to (post) concatenate to that of the
     * parent.
     * @param parent The base state from which the new one will derive (changing
     * only the transform and the level)
     */
    public FractalState(FractalState parent, AffineTransform coordTransform) {
        this(parent);
        toScreenTransform.concatenate(coordTransform);
        this.level += 1;
        initPath();
    }

    /**
     * Create a clone of the given FRACTAL state, except that its coordinate
     * frame is located at the given points, and its rendering level is 1 more
     * than the parent.
     * @param parent The reference state from which the new instance is derived.
     * @param origin The origin of the coordinate frame
     * @param xUnit The unit vector in the X axis direction.
     */    
    public FractalState(FractalState parent, FractalPoint origin, FractalPoint xUnit) {
        this(parent, mapRigidlyXAxisTo(origin, xUnit));
    }
    
    /**
     * Create a default FRACTAL state that has an identity coordinate transform,
     * is at rendering level 0, has its last drawn point at the origin and has
     * a fresh environment, path and fractal stack. 
     * @param coordXform The transformation to convert frame coordinates to 
     * screen coordinates.
     */
    public FractalState(AffineTransform coordXform) {
        this(new GeneralPath(), new PathHash(), new Stack<>(), new Environment<>());
        this.toScreenTransform = coordXform;
        this.level = 0;
        initEnv();
        initPath();
    }
    
    /**
     * Create a new FRACTAL State instance with a coordinate frame situated at
     * specified points.
     * @param origin The origin of the coordinate frame.
     * @param xUnit The end point of the unit vector in the direction of the X-axis.
     */
    public FractalState(FractalPoint origin, FractalPoint xUnit) {
        this(mapRigidlyXAxisTo(origin, xUnit));
    }
    
    /**
     * Create a fresh FRACTAL state that uses the identity transform to convert
     * frame coordinates to screen coordinates.
     */
    public FractalState() {
        this(new AffineTransform());
    }
    
    private void initEnv() {
        // These are probably not even necessary since they are syntactically recognised
        env.put("O", FractalPoint.ORIGIN);
        env.put("X", FractalPoint.X_UNIT);
        env.put("Y", FractalPoint.Y_UNIT);
    }
    
    private void initPath() {
        Point2D screenOrigin = getScreenOrigin();
        this.path.moveTo(screenOrigin.getX(), screenOrigin.getY());
    }
    
    /**
     * Construct the affine transform that maps the line segment from (0, 0) to 
     * (1, 0) rigidly to the line segment from the given source and destination.
     * The transform will be a composition of scalings, rotations, and 
     * translations.  This transform will convert corodinates specified in the
     * new coordinate frame to the coordinates currently being used to describe
     * the source and destination points.
     * To use this transform, it should pre-multiply the point in the new 
     * coordinate frame. The output will be the point's image in the coordinate
     * system.
     * @param source The desired new origin. 
     * @param dest The desired new unit absolute location of vector (1, 0)
     * @return A toScreenTransform that converts coordinates in the new coordinate system
 to the existing coordinate system.
     */
    public static final AffineTransform mapRigidlyXAxisTo(FractalPoint source, 
                                                          FractalPoint dest) {
        double sx = source.getX();
        double sy = source.getY();
        double dx = dest.getX() - sx;
        double dy = dest.getY() - sy;
        return new AffineTransform(dx, dy, -dy, dx, sx, sy);
    }
    
    /**
     * Set the threshold below which a fractal would be rendered as a simple
     * line segment. If the current frame's length scale falls below this 
     * threshold, then drawing a fractal (either by using the fractal's name
     * or the SELF keyword would result in a line segment between the given
     * coordinates.
     * @param res The desired threshold.
     */
    public void setResolution(float res) {
        resolution = res;
    }
    
    /**
     *
     * @return The current threshold in screen coordinates below which a fractal
     * is rendered as a line segment.
     */
    public float getResolution() {
        return resolution;
    }

    /**
     *
     * @return The current level of the fractal (the depth of SELF references
     * since the initial invocation of the fractal).
     */
    public int getLevel() {
        return level;
    }

    // consider suppressing this method
    /**
     * Warning: there should not be any need to retrieve this transformation
     * from the instance of state.  All operations that depend on the current
     * transformation (such drawing a line, or moving to a point) are supported 
     * directly as methods on this instance.
     * @return The current transform for converting fractal frame coordinates to
     * screen coordinates.
     */
    public AffineTransform getTransform() {
        return toScreenTransform;
    }
    
    /**
     *
     * @return The path of points traced out by this fractal.
     */
    public GeneralPath getShape() {
        return path;
    }
    
    /**
     * Lookup the given identifier and return its current value.
     * @param id The identifier being refrenced.
     * @return The result bound to that identifier
     * @throws FractalUnboundException if that identifier is not bound within
     * the current state.
     */
    public FractalValue lookup(String id) throws FractalUnboundException {
        return env.get(id);
    }
    
    /**
     * Create a new binding for the given variable to the given value.  If the 
     * variable was previously bound, the new binding replaces the old one.
     * @param id The name of the variable being bound.
     * @param val The value being associated with the variable.
     */
    public void bind(String id, FractalValue val) {
        env.put(id, val);
    }
    
    /**
     * Compute the scale of the current coordinate transformation in screen 
     * coordinates.  This is the length in screen coordinates of a unit-length
     * vector in the current frame.
     * @return The length of a unit vector in the current fractal frame after 
     * it is mapped to screen coordinates. 
     */
    public double getCurrentScale() {
        Point2D originImg = toScreenTransform.transform(ORIGIN, null);
        Point2D xUnitImg = toScreenTransform.transform(X_UNIT_VEC, null);
        return Point2D.distance(originImg.getX(), originImg.getY(), 
                                xUnitImg.getX(), xUnitImg.getY());
    }
    
    /**
     * Return the state whose transformation is modified to have its origin and
     * X-axis unit at the given points, but share all the shareable components 
     * (ie the path, environment, fractal stack, etc) of this state.  
     * Note that the path and environment are aliased, so the
     * effect is not the same as creating a new state using the constructor.
     * @param start The point where the origin of the new state will be
     * @param stop The location of X (= (1, 0)) in the new coordinate system
     * @return a newly created FractalState instance that is based on this
     * state, except for the current transformation and the level.
     */
    public FractalState deriveState(FractalPoint start, FractalPoint stop) {
        FractalState result = new FractalState(this, start, stop);
        result.moveTo(FractalPoint.ORIGIN);
        return result;
    }
    
    /**
     * Determine whether this state can be further resolved to support a deeper
     * level fractal.
     * @return True if the length of a unit vector in this coordinate frame
     * is still larger than the current resolution AND the max. depth has not 
     * yet been reached.
     */
    public boolean isResolvable() {
        return getCurrentScale() >= getResolution() && level < maxDepth;
    }
    
    // **-- Methods for manipulating the path of points --*

    /**
     * Clear the path of points accumulated so far.
     */
    public void clear() {
        path.reset();
        pathHash = new PathHash();
        Point2D screenO = getScreenOrigin();
        path.moveTo(screenO.getX(), screenO.getY());
    }
    
    /**
     *
     * @return The hash value for the current visible path. This is reset
     * every time the screen is cleared (resetting the internal path).
     */
    public long getPathHash() {
        return pathHash.getHash();
    }
    
    public Point2D getPathResultant() {
        return pathHash.getHashPt();
    }
    
    /**
     * Obtain the screen coordinates of the given point if it were to be 
     * rendered within the current fractal's coordinate frame.
     * @param p The point in the fractal's coordinate system
     * @return The image of the point in the screen's coordinate system.
     */
    public Point2D toScreen(FractalPoint p) {
        Point2D.Float pt = new Point2D.Float(p.getX(), p.getY());
        return toScreenTransform.transform(pt, null);
    }
    
    /**
     *
     * @return The location of the frame's origin in screen coordinates
     */
    public Point2D getScreenOrigin() {
        return toScreen(FractalPoint.ORIGIN);
    }
    
    /**
     *
     * @return The location of the current point in the current frame's 
     * coordinates.
     */
    public FractalPoint getCurrentPoint() {
        try {
            // return lastPoint;
            Point2D userSpacePt = toScreenTransform.inverseTransform(path.getCurrentPoint(), null);
            return new FractalPoint(userSpacePt.getX(), userSpacePt.getY());
        } catch (NoninvertibleTransformException ex) {
            String msg = "Uncrecoverable error: Fractal frame is non-invertible!";
            Logger.getLogger(FractalState.class.getName()).log(Level.SEVERE, msg, ex);
            throw new Error(msg);
        }
    }
    


    /**
     * Draw a line from the current point (HERE) to the given point.
     * @param p The end point of the line segment to be added.
     */
    public void lineTo(FractalPoint p) {
        var pCoords = toScreen(p);
        pathHash.addSegment(path.getCurrentPoint(), pCoords);
        path.lineTo(pCoords.getX(), pCoords.getY());
    }
    
    public void moveTo(FractalPoint p) {
        var pCoords = toScreen(p);
        path.moveTo(pCoords.getX(), pCoords.getY());
    }
    
    // ** -- Methods to manage control flow of fractal invocations

    /**
     * Install a new fractal as the current fractal.  This will cause the 
     * DRAW SELF form to reference this fractal until another one is started
     * or this one is stopped.
     * @param f The new fractal to be installed as current.
     */    
    public void startFractal(Fractal f) {
        fractalStack.push(f);
    }
    
    /**
     * Stop the current fractal (usually because its drawing has just been 
     * completed). 
     */
    public void stopFractal() {
        fractalStack.pop();
    }
        
    /**
     *
     * @return The current fractal being drawn. Returns null if no fractal is
     * being drawn.
     */
    public Fractal getCurrentFractal() {
        return fractalStack.peek();
    }

}
