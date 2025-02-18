package fractal.sys;

import cs34q.gfx.CanvasFrame;
import cs34q.gfx.CanvasFrame.CmdListener;
import cs34q.gfx.FileHandler;
import cs34q.gfx.GraphingPanel;
import cs34q.gfx.PenTip;
import fractal.semantics.FractalEvaluator;
import fractal.semantics.FractalState;
import fractal.syntax.FractalLexer;
import fractal.syntax.FractalParser;
import fractal.values.FractalPoint;
import fractal.values.FractalValue;
import java.awt.Color;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java_cup.runtime.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import lib3652.util.Walker;
import lib3652.util.Result;
import lib3652.util.TokenException;
import lib3652.util.MainHelper;
import lib3652.util.PersistentWalker;

public class Repl {

    /*
    public static final String PROMPT = "> ";

    private static final String MESSAGE = "Type your input at the prompt." +
	"  Terminate with a '.' on a line by itself.\n" +
	"Quit by entering a '.' as the only line or by sending EOF to input.";
    */

    public static void usage() {
	String[] usageMsg = new String[]{
	    String.format("Usage: <javaexec> %s [file ...]", 
                      Repl.class.getName()),
	    "",
	    "The sequence of filenames provided afterwards is optional.  Each",
	    "will be read and traversed in the order given.  If a '-' is",
	    "specified, input will be read from stdin.  If no files are given,",
	    "input willl be read from stdin."
	};
	for (String line : usageMsg) {
	    System.out.println(line);
	}
    }

    public static <S, T> void main(String args[]) {
	int n = args.length;
	String walkerName = "";
	ArrayList<String> filenames = new ArrayList<>();
        
	// Parse command line arguments
	for (int i = 0; i  < n; i++) {
	    String arg = args[i];
	    if (arg.equals("-h") || arg.equals("--help")) {
		usage();
		System.exit(0);
	    } else if (arg.equals("-w")) {
		walkerName = args[i+1];
		i += 1;
	    } else {
		filenames.add(arg);
	    }
	}
        FractalEvaluator fEval = new FractalEvaluator();
        PersistentWalker<FractalState, FractalValue> walker =
                new PersistentWalker<FractalState, FractalValue>(fEval) {
                    @Override
                    public lr_parser mkParser(Reader input) {
                        return new FractalParser(new FractalLexer(input));
                    }
                };
        walker.setDebugging(true);
        FractalState state = walker.getState();
        
        CanvasFrame frame = new CanvasFrame("FRACTAL Display");
        GraphingPanel canvas = frame.getGraphArea();
        PenTip defaultPen = new PenTip(2, Color.BLACK);	
        canvas.addPath(state.getShape(), defaultPen);
        
        canvas.addListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (pce.getPropertyName().equals(GraphingPanel.EVT_EXTENTS_CHANGED)) {
                    float res = getResolution(canvas);
                    state.setResolution(res);
                    System.out.println(String.format("Changing resolution to %.3f", 
                            res));
                }
            }
        });
        
        frame.addCmdListener(new CmdListener() {
            @Override
            public void cmdReceived(String text, CanvasFrame frame) {
                Result res = walker.readParseWalk(new StringReader(text));
                frame.display(walker.mkOutput(res)+"\n\n");
                canvas.repaint();
            }
        });
        
        FileHandler fh = new FileHandler("FRACTAL Programs", "fal", "frac") {
            
            @Override
            public String processFile(Reader fileReader) {
                Result res = walker.readParseWalk(fileReader);
                frame.display("\n Read input from file ...\n");
                frame.display(res.toString());
                return res.toString();
            }
        };
        
        frame.setFileHandler(fh );
        
        // canvas.addPath(state.getShape(), new PenTip(1, Color.BLACK));
        //canvas.setBackground(Color.decode("0x4080FF"));
        java.awt.EventQueue.invokeLater(() -> {
            frame.setVisible(true);
            // set the resolution of the curren state based on the size of the frame
            float res = getResolution(canvas);
            // System.out.println(String.format("Setting initial resolution to %.3f", res));
            state.setResolution(res);
        });
        MainHelper.walkFiles(walker, filenames);
    }
    
    private static  float getResolution(GraphingPanel canvas) {
        double xs = Math.abs(canvas.getXScale());
        double ys = Math.abs(canvas.getYScale());
        float res = (float) Math.min(1/xs, 1/ys);
        return res;
    }
}
