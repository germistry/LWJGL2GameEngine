package engineTester;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;

public class MainGameLoop {

	public static void main(String[] args) {
		//First open the display.
		DisplayManager.createDisplay();
		//Test to see if a quad can be rendered by creating a loader and renderer object
		//to use those new methods. 
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		//List of vertices for the quads to render. OpenGL expects vertices to be defined counter 
		//clockwise by default. 
		float[] vertices = {
				//Left bottom triangle
				-0.5f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f,
				//Right top triangle 
				0.5f, -0.5f, 0f,
				0.5f, 0.5f, 0f,
				-0.5f, 0.5f, 0f
		};
		//Load the list of vertices into a RawModel.
		RawModel model = loader.loadtoVAO(vertices);
		
		//While in the game loop, objects are updated and rendering is done. Loop will continue until display
		//is closed.
		while (!Display.isCloseRequested()) {
			//Prepare the renderer every single frame.
			renderer.prepare();
			//game logic
			
			//Rendering the objects. 
			renderer.render(model);
			
			//The display is updated every frame.
			DisplayManager.updateDisplay();
		}
		//Cleanup loader once the game is closed. 
		loader.CleanUp();
		
		//When the close is requested and loop exited, the display is closed.
		DisplayManager.closeDisplay();
		
	}

}
