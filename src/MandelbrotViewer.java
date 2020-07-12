/*
 * Ada Clevinger
 * April 10, 2018
 * Assignment 3
 * Class: MandelbrotViewer
 * 
 * This class displays the Mandelbrot Set to the user, making use of the Complex class to calculate
 * the Mandelbrot Set.
 * 
 * Additional Features: Permits the user to interact with the Mandelbrot image, moving around the 
 * coordinate plane and zooming in/out. It also permits the user to view variations of the Mandelbrot
 * sequence that are artistically interesting, and save the image they are viewing to their file system.
 */

/**
 * The MandelbrotViewer program calculates the Mandelbrot Sequence and graphically displays its results in
 * the complex plane.
 * 
 * Additional Features: Permits the user to interact with the Mandelbrot image, moving around the 
 * coordinate plane and zooming in/out. It also permits the user to view variations of the Mandelbrot
 * sequence that are artistically interesting, and save the image they are viewing to their file system.
 * 
 * @author Ada Clevinger
 * @version 2.0
 * @since 2018-04-03
 */

import java.awt.Color;
import java.awt.Font;

import visual.frame.WindowFrame;
import visual.panel.CanvasPanel;
import visual.panel.ElementPanel;


public class MandelbrotViewer{
																/** Constant for the X component to start (Real value) */
	private static final double START_CORNER_REAL = -2.3;		/** Constant for the Y component to start (Complex value) */	
	private static final double START_CORNER_COMPLEX = 1.8;		/** Constant for the total distance moved from one end of the screen to the other */
	private static final double SIDE_LENGTH = 3.6;			/** Constant for the number of iterations of the Mandelbrot sequence performed */		
	private static final int MANDELBROT_ITERATIONS = 200;			/** Constant for the limiting value for rejecting within the Mandelbrot sequence*/ 
	private static final double MANDELBROT_FAIL_VALUE = 2;			/** Constant for the size of the screen (Width and Height; square sized) */
	private static final int SIZE_SCREEN = 600;					/** Constant for the rate at which zoomIn/Out should change the boundaries of the screen.*/
	private static final double ZOOM_SCALE_VALUE = .80;			/** Constant for the rate at which the directional commands should move relative to screen size*/
	private static final double MOVEMENT_SCALE_VALUE = 10;			
													/** Instance Variable for what the current Complex value of the top-left corner is.*/
	private static double cornerLoc_Complex;		/** Instance Variable for what the current Real value of the top-left corner is.*/
	private static double cornerLoc_Real;			/** Instance Variable for how long, in the coordinate system, each side length is.*/
	private static double sideLengthRunning;		/** Instance Variable for the object containing the image, so that it can be updated.*/
	private static int state;			
	private static int iterations;
	private static CanvasPanel pan;
	private static ElementPanel pan2;
	private static int zoom;
	
	/**
	 * The main method simply calls launch to have the start() method occur.
	 * 
	 * @param args This is the typical main method entry.
	 */
	
	public static void main(String[] args){
		cornerLoc_Complex = START_CORNER_COMPLEX;	//Constant values are assigned to variables that may change, but need to remember what they originall were.
		cornerLoc_Real = START_CORNER_REAL;
		sideLengthRunning = SIDE_LENGTH;
		iterations = MANDELBROT_ITERATIONS;
		WindowFrame frame = new WindowFrame(800, 800);
		pan2 = new ElementPanel(0, 0, 800, 100) {
			
		};
		pan = new CanvasPanel(0, 100, 800, 700, 1) {
			
			public void keyEvent(char c) {
				switch(c) {
					case 'q':
						cornerLoc_Real += (1.0 - ZOOM_SCALE_VALUE) * sideLengthRunning / 2.0;
						cornerLoc_Complex -= (1.0 - ZOOM_SCALE_VALUE) * sideLengthRunning / 2.0;
						sideLengthRunning *= ZOOM_SCALE_VALUE;
						break;
					case 'e':
						cornerLoc_Real -= (1.0 - ZOOM_SCALE_VALUE) * sideLengthRunning / 2.0;
						cornerLoc_Complex += (1.0 - ZOOM_SCALE_VALUE) * sideLengthRunning / 2.0;
						sideLengthRunning /= ZOOM_SCALE_VALUE;
						break;
					case 'w':
						cornerLoc_Complex += sideLengthRunning/MOVEMENT_SCALE_VALUE;
						break;
					case 's':
						cornerLoc_Complex -= sideLengthRunning/MOVEMENT_SCALE_VALUE;
						break;
					case 'd':
						cornerLoc_Real += sideLengthRunning/MOVEMENT_SCALE_VALUE;
						break;
					case 'a':
						cornerLoc_Real -= sideLengthRunning/MOVEMENT_SCALE_VALUE;
						break;
					case 'r':
						state += 1;
						state = state > 2 ? 0 : state;
						break;
					case 'z':
						iterations -= 50;
						iterations = iterations < 50 ? 50 : iterations;
						pan2.removeElement("text3");
						pan2.addText("text3", 10, 0, pan2.getHeight() * 3 / 4, pan2.getWidth() / 4, pan2.getHeight() / 3, "Iterations: " + iterations, new Font("Serif", Font.BOLD, 22), false, true, true);
						break;
					case 'x':
						iterations += 50;
						pan2.removeElement("text3");
						pan2.addText("text3", 10, 0, pan2.getHeight() * 3 / 4, pan2.getWidth() / 4, pan2.getHeight() / 3, "Iterations: " + iterations, new Font("Serif", Font.BOLD, 22), false, true, true);
						break;
					default:
						break;
				}
				render();
			}
			
		};

		pan2.addText("text2", 10, pan2.getWidth() / 5, pan2.getHeight() * 3 / 4, pan2.getWidth() * 4 / 5, pan2.getHeight() / 3, "WASD to move, QE to zoom, ZX to refine, R to cycle", new Font("Serif", Font.BOLD, 22), false, true, true);
		pan2.addText("text3", 10, 0, pan2.getHeight() * 3 / 4, pan2.getWidth() / 4, pan2.getHeight() / 3, "Iterations: " + iterations, new Font("Serif", Font.BOLD, 22), false, true, true);
		
		frame.reserveWindow("home");
		frame.reservePanel("home", "canvas", pan);
		frame.reservePanel("home", "pan2", pan2);
		render();
	}

	/**
	 * The renderScene method isolates the portion of code that recalculates the color values for
	 * each pixel in the WritableImage object that displays the Mandelbrot set. It calls on doIterations to
	 * receive a value immediately passed to pickColor, which is then assigned to the pixel; this is done
	 * for every pixel in the WritableImage via a PixelWriter.
	 * 
	 */
	
	private static void render(){
		for(int i = 0; i < pan.getWidth(); i++){				//Each pixel can be set to a color derived from the Mandelbrot Sequence values
			for(int j = 0; j < pan.getHeight(); j++){		//The passed arguments correspond to the pixel's location on the screen with scale described by constant values
				double x = cornerLoc_Real + (double)i * sideLengthRunning / (double)pan.getWidth();
				double y = cornerLoc_Complex - (double)j  * sideLengthRunning / (double)pan.getHeight();
				Color col = pickColor(doIterations(new Complex(x , y)));
				pan.setPixelColor(i, j, col);
			}
		}
		pan2.removeElement("text");
		pan2.addText("text", 10, 0, pan2.getHeight() / 3, pan2.getWidth(), pan2.getHeight() / 2, "Zoom: " + (SIDE_LENGTH / sideLengthRunning), new Font("Serif", Font.BOLD, 32), false, true, true);
	
	}
	
	/**
	 * This method performs 200 iterations of the Mandelbrot Sequence to a 
	 * starting Complex number as supplied to it as an argument. Returns
	 * the index that fails the Mandelbrot Sequence, or 200 in the event that
	 * it succeeds.
	 * 
	 * @param c This parameter is a Complex object that is used to calculate the Mandelbrot Sequence
	 * @return Returns an integer value that corresponds to when the Mandelbrot Sequence failed, or 200 if it succeeded.
	 */
	
	private static int doIterations(Complex c){
		Complex copyC = new Complex(c);
		int iter = iterations;				//This is the success value; if not changed, then know that it passed.
		for(int i = 0; i < iterations; i++){
			if(copyC.modulus() > MANDELBROT_FAIL_VALUE){
				iter = i;
				break;
			}
			switch(state){
			case 0: copyC.multiply(copyC); break;		//Performs Mandelbrot Sequence if still within legal boundary
			case 1: copyC.multiplyVar1(copyC); break;
			case 2: copyC.multiplyVar2(copyC); break;
			default: copyC.multiply(copyC); break;
			}
			copyC.add(c);
		}
		return iter;
	}
	
	/**
	 * This method uses a given integer, expectedly from the doIterations method, that is
	 * used to calculate a Color which is returned to the caller; this Color is related
	 * to displaying the Mandelbrot Sequence.
	 * 
	 * @param numIter This parameter is used to decide whether the returned color should be Black (too far away) or
	 * a calculated color corresponding to the submitted value.
	 * @return This method returns a Color describing whether a coordinate is in or how near to being in the Mandelbrot Sequence.
	 */
	
	private static Color pickColor(int numIter){
		Color out;
		int max = iterations;
		if(numIter == max) {
			out = new Color(0, 0, 0, 1);		//It's just Black, the other color below uses input to derive a Color that just happens to look pretty nice. 
		}
		float r = numIter / (4f * max);
		float g = numIter / (4f * max);
		float b = numIter / (max / 2f);
		b = b > 1 ? 1 : b;
		out = new Color(r, g, b, 1f);
		return out;
	}
	
}
