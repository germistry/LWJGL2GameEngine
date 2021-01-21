package renderEngine;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.opengl.ImageIOImageData;

public class DisplayManager {

	//DisplayMode needs to know the width and height of the display, can use for glViewport too.
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	//Set the Frame Rate Cap. 
	private static final int FPS_CAP = 120;
	//Time at end of last frame.
	private static long lastFrameTime;
	//Holds time taken to render previous frame.
	private static float delta;
	
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
			Display.setTitle("A 3D Game Demo");
			//setting icon
			try {
				Display.setIcon(new ByteBuffer[] {
				        new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/favicon16.png")), false, false, null),
				        new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/favicon32.png")), false, false, null)
				        });
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		//Need to tell OpenGL where in the display it can render the game, this case using the whole display, 
		//with 0,0 being the bottom left of the display and width and height being the constants in pixels.
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		//Initialise lastFrameTime when display is created. 
		lastFrameTime = getCurrentTime();
		
	}
	//Method to update the display for every single frame.
	public static void updateDisplay() {
		//Need to sync the display to keep steady framerate and then update the display.
		Display.sync(FPS_CAP);
		Display.update();
		//get current frame time
		long currentFrameTime = getCurrentTime();
		//calc delta by subtracting lastFrameTime from the currentFrameTime. Convert to secs by / 1000
		delta = (currentFrameTime - lastFrameTime)/1000f;
		//Set lastFrameTime to currentFrameTime ready for next calculation. 
		lastFrameTime = currentFrameTime;
	}
	//Get delta which is frame time in seconds
	public static float getFrameTimeSeconds() {
		return delta;
	}
	//Method to close down the display when the game is exited.
	public static void closeDisplay() {
		
		Display.destroy();
	}
	//Private method to get the current time in milliseconds. 
	private static long getCurrentTime() {
		//getTime returns current time in ticks so divide by getTimerResolution which returns ticks per second
		return Sys.getTime()*1000/Sys.getTimerResolution();
	}
	
	
	
}
