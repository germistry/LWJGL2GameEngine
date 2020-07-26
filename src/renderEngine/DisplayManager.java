package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	//DisplayMode needs to know the width and height of the display, can use for glViewport too.
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	//Set the Frame Rate Cap. 
	private static final int FPS_CAP = 120;
	
	//Method to open up the display when starting the game.
	public static void createDisplay() {
		
		//Setting some context attributes first - version of Open GL, forward compatibility, 
		//core profile of OpenGL(minus depreciated functions).
		ContextAttribs attribs = new ContextAttribs(3,2);
		attribs.withForwardCompatible(true);
		attribs.withProfileCore(true);
				
		try {
			//First need to determine the size of the display. 
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			//Now can create the display. 
			Display.create(new PixelFormat(), attribs);
			//Set title of the display.
			Display.setTitle("A Display of Set Width and Height");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		//Need to tell OpenGL where in the display it can render the game, this case using the whole display, 
		//with 0,0 being the bottom left of the display and width and height being the constants in pixels.
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
	}
	//Method to update the display for every single frame.
	public static void updateDisplay() {
		//Need to sync the display to keep steady framerate and then update the display.
		Display.sync(FPS_CAP);
		Display.update();
	}
	//Method to close down the display when the game is exited.
	public static void closeDisplay() {
		
		Display.destroy();
	}
}
