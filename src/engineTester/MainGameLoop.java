package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
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
		//Open a new loader.  
		Loader loader = new Loader();
		//Load the static shader so can be used when rendering. 
		StaticShader shader = new StaticShader();
		//Load the renderer that takes in the static shader.
		Renderer renderer = new Renderer(shader);
		
		float[] vertices = {			
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,0.5f,-0.5f,		
				
				-0.5f,0.5f,0.5f,	
				-0.5f,-0.5f,0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				0.5f,0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				-0.5f,-0.5f,0.5f,	
				-0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f
				
		};
		
		float[] textureCoords = {
				
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0

				
		};
		
		int[] indices = {
				0,1,3,	
				3,1,2,	
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14,	
				16,17,19,
				19,17,18,
				20,21,23,
				23,21,22

		};
		
//		//List of vertices for the quad. OpenGL expects vertices to be defined counter 
//		//clockwise by default. 
//		float[] vertices = {
//				-0.5f, 0.5f, 0f,  //V0
//				-0.5f, -0.5f, 0f, //V1
//				0.5f, -0.5f, 0f,  //V2
//				0.5f, 0.5f, 0f    //V3
//		};
//		//List of indices for the quad. OpenGL expects them to be defined counter-clockwise
//		//by default.
//		int[] indices = {
//				0,1,3,		//Top left triangle  (V0,V1,V3)
//				3,1,2		//Bottom right triangle (V3,V1,V2)
//		};
//		//Texture co-ordinates for texturing the quad. Need to be defined the same as the vertices. 0,0 is top left.
//		float[] textureCoords = {
//				0,0,  //V0
//				0,1,  //V1
//				1,1,  //V2
//				1,0	  //V3
//		};
				
		//Load the list of vertices, textureCoords and indices into a RawModel.
		RawModel model = loader.loadtoVAO(vertices, textureCoords, indices);
		//Creating a textured model object, passing in the rawmodel and the texture. 
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("FancyTexture")));
		//Creating an entity that takes in the textured model we want it to show, needs position to be rendered at,
		//The 3d vector should move it one position to the left. Changed to -5 now adding viewMatrix
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-5),0,0,0,1);
		//Creating a camera
		Camera camera = new Camera();
		
		//While in the game loop, objects are updated and rendering is done. Loop will continue until display
		//is closed.
		while (!Display.isCloseRequested()) {
			//entity.increasePosition(0, 0, -0.1f);
			entity.increaseRotation(1, 1, 0);
			//Move the camera every frame
			camera.move();
			//Prepare the renderer every single frame.
			renderer.prepare();
			//Start the static shader before rendering. 
			shader.start();
			//Every frame load up view matrix to the shader 
			shader.loadViewMatrix(camera);
			//Rendering the objects. 
			renderer.render(entity,shader);
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
