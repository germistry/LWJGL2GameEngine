package engineTester;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;
import shaders.StaticShader;

public class MainGameLoop {

	public static void main(String[] args) {
		//First open the display.
		DisplayManager.createDisplay();
		//Test to see if a quad can be rendered by creating a loader and renderer object
		//to use those new methods. 
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		//Load the static shader so can be used when rendering. 
		StaticShader shader = new StaticShader();
		
		//List of vertices for the quad. OpenGL expects vertices to be defined counter 
		//clockwise by default. 
		float[] vertices = {
				-0.5f, 0.5f, 0f,  //V0
				-0.5f, -0.5f, 0f, //V1
				0.5f, -0.5f, 0f,  //V2
				0.5f, 0.5f, 0f    //V3
		};
		//List of indices for the quad. OpenGL expects them to be defined counter-clockwise
		//by default.
		int[] indices = {
				0,1,3,		//Top left triangle  (V0,V1,V3)
				3,1,2		//Bottom right triangle (V3,V1,V2)
		};
		//Load the list of vertices and indices into a RawModel.
		RawModel model = loader.loadtoVAO(vertices,indices);
		
		//While in the game loop, objects are updated and rendering is done. Loop will continue until display
		//is closed.
		while (!Display.isCloseRequested()) {
			//Prepare the renderer every single frame.
			renderer.prepare();
			//Start the static shader before rendering. 
			shader.start();
			//Rendering the objects. 
			renderer.render(model);
			//Stop the static shader once rendering finished.
			shader.stop();
			//The display is updated every frame.
			DisplayManager.updateDisplay();
		}
		
		//Cleanup static shader & loader once the game is closed.
		shader.cleanUp();
		loader.cleanUp();
		
		//When the close is requested and loop exited, the display is closed.
		DisplayManager.closeDisplay();
		
	}

}
