/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractal.sys;

import cs34q.turtle.TurtleDisplay;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author newts
 */
public class TestDisplay {
    public static void main(String[] args) {
        final TurtleDisplay display = new TurtleDisplay();
        display.setForeground(Color.BLACK);
        JFrame frame = new JFrame("Turtle Display Test");
        frame.setSize(400, 400);
        display.setSize(400, 350);
        frame.setBackground(Color.CYAN);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(display, BorderLayout.CENTER);
        JButton stepButton = new JButton("Step");
        stepButton.addActionListener(new AbstractAction() {
            
            double dist = 20.0;
            double angle = 0.0;
            double x = 100.0;
            double y = 100.0;

            @Override
            public void actionPerformed(ActionEvent e) {
                double nx = x + dist * Math.cos(angle);
                double ny = y + dist * Math.sin(angle);
                display.drawSegment(x, y, nx, ny);
                display.showTurtle(angle, nx, ny);
                dist = dist * 1.5;
                angle = angle + Math.PI/3;    // 60 degrees
                x = nx;
                y = ny;
            }
        });
        frame.add(stepButton, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
