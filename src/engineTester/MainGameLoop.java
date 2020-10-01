package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJLoader;
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
		
		//Load the raw model from obj file passing in the file name 
		RawModel model = OBJLoader.loadObjModel("stall", loader);
		//Creating a textured model object, passing in the rawmodel and the texture. 
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("stallTexture")));
		//Creating an entity that takes in the textured model we want it to show, needs position to be rendered at,
		//The 3d vector - should move it 10 positions back
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-30),0,0,0,1);
		//Creating the light source, setting position and colour (1,1,1) is white
		Light light = new Light(new Vector3f(0,0,20),new Vector3f(1,1,1));
		//Creating a camera
		Camera camera = new Camera();
		
		//While in the game loop, objects are updated and rendering is done. Loop will continue until display
		//is closed.
		while (!Display.isCloseRequested()) {
			//entity.increasePosition(0, 0, -0.1f);
			entity.increaseRotation(0, 1, 0);
			//Move the camera every frame
			camera.move();
			//Prepare the renderer every single frame.
			renderer.prepare();
			//Start the static shader before rendering. 
			shader.start();
			//Load the light 
			shader.loadLight(light);
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
