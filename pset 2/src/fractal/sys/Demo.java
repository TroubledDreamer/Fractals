/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package fractal.sys;
import cs34q.gfx.CanvasFrame;
import cs34q.gfx.GraphingPanel;
import cs34q.gfx.PenTip;
import fractal.semantics.FractalState;
import fractal.values.FractalPoint;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author newts
 */
public class Demo {
    
    static PenTip defaultPen = new PenTip(1, Color.BLACK);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        CanvasFrame frame = new CanvasFrame();
        GraphingPanel canvas = frame.getGraphArea();
        //canvas.setBackground(Color.decode("0x4080FF"));
        
        
        java.awt.EventQueue.invokeLater(() -> {
            frame.setVisible(true);
            // run a simple test on the canvas
            // screenRendering(canvas);
            // frameRendering0(canvas);
            fractalRendering(canvas);
        });        
    }
    
    /**
     * Draw a pair of lines crossing at the origin at 90 degrees to each other.
     * The line from bottom left to top right is twice as long as the other.
     * When drawing directly to a GraphingPanel instance, screen coordinates
     * are used. (These are further translated to actual pixel coordinates, but
     * that happens transparently and automatically.)
     * @param canvas The GraphingPanel instance on which to draw.
     */
    public static void screenRendering(GraphingPanel canvas) {
        canvas.setPenTip(new PenTip(1, Color.BLUE));
        canvas.drawLine(-10, -10, 10, 10);
        canvas.setPenTip(new PenTip(1, Color.GREEN));
        canvas.drawLine(-5, 5, 5, -5);
        canvas.repaint();
    }
    
    public static void setupFractalFrame(GraphingPanel canvas, 
            FractalState state, PenTip pen) {
        canvas.addPath(state.getShape(), pen);
    }
    
    public static void frameRendering0(GraphingPanel canvas) {
        // Set the fractal from with origin at (-10, 0) on the screen
        // and the unit X vector at (10, 0) on the screen.
        FractalState state = new FractalState(new FractalPoint(-10, 0), 
                new FractalPoint(10, 0));
        setupFractalFrame(canvas, state, defaultPen);
        // draw a horizontal line from origin to (1, 0) in fractal frame
        state.lineTo(FractalPoint.X_UNIT);
        // now draw a line from (1, 0) to (0, 1). 
        // (Should be a line at 45 degrees to the left)
        state.lineTo(FractalPoint.Y_UNIT);
        // now a vertical line to (0, -1)
        state.lineTo(new FractalPoint(0, -1));
        // and back up to (1, 1); This also demonstrates how points can be added
        FractalPoint trPt;
        try {
            trPt = FractalPoint.X_UNIT.add(FractalPoint.Y_UNIT).pointValue();
            state.lineTo(trPt);
        } catch (FractalException ex) {
            Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, 
                    "Sum of 2 points did not yield a point!", ex);
        }
        
    }
    
    private static void gosper(FractalState state, int level) {
        // assume that state is pristine and lastPoint is the origin
        if (level == 0) {
            state.lineTo(FractalPoint.X_UNIT);
        } else {
            FractalPoint midPt = new FractalPoint(0.5, 0.5);
            FractalState s1 = state.deriveState(FractalPoint.ORIGIN, midPt);
            gosper(s1, level - 1);
            FractalState s2 = state.deriveState(midPt, FractalPoint.X_UNIT);
            gosper(s2, level - 1);
        }
    }
    
    public static void fractalRendering(GraphingPanel canvas) {
        // Setup a fractal frame to span the screen, then render a level 1 
        // fractal within it
        FractalState state = new FractalState(new FractalPoint(-10, 0), 
                new FractalPoint(10, 0));
        setupFractalFrame(canvas, state, defaultPen);
        int level = 5;
        gosper(state, level);
        
    }
}
