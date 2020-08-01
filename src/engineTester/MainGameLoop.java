package engineTester;

import org.lwjgl.opengl.Display;

import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

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
		//Texture co-ordinates for texturing the quad. Need to be defined the same as the vertices. 0,0 is top left.
		float[] textureCoords = {
				0,0,  //V0
				0,1,  //V1
				1,1,  //V2
				1,0	  //V3
		};
				
		//Load the list of vertices, textureCoords and indices into a RawModel.
		RawModel model = loader.loadtoVAO(vertices, textureCoords, indices);
		//Load a texture for the model. Constructor uses the Loader method to get the ID of the texture. 
		//Pass in the fileName of the texture to use. 
		ModelTexture texture = new ModelTexture(loader.loadTexture("FancyTexture"));
		//Creating a textured model object, passing in the rawmodel and the texture. 
		TexturedModel texturedModel = new TexturedModel(model,texture);
		
		
		//While in the game loop, objects are updated and rendering is done. Loop will continue until display
		//is closed.
		while (!Display.isCloseRequested()) {
			//Prepare the renderer every single frame.
			renderer.prepare();
			//Start the static shader before rendering. 
			shader.start();
			//Rendering the objects. 
			renderer.render(texturedModel);
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
