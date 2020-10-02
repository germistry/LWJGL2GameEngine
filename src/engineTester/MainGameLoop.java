package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MainRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		//First open the display.
		DisplayManager.createDisplay();
		//Open a new loader.  
		Loader loader = new Loader();
		
		//Load the raw model from obj file passing in the file name 
		RawModel model = OBJLoader.loadObjModel("LowPolyTree", loader);
		//Creating a textured model object, passing in the rawmodel and the texture. 
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("LowPolyTreeTexture")));
		//Get the modeltexture to set the shine dampener and reflectivity values. 
//		ModelTexture texture = staticModel.getTexture();
//		texture.setShineDampener(20);
//		texture.setReflectivity(1);
		//Creating an entity that takes in the textured model we want it to show, needs position to be rendered at,
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-25),0,0,0,1);
		//Creating the light source, setting position and colour (1,1,1) is white
		Light light = new Light(new Vector3f(3000,2000,2000),new Vector3f(1,1,1));		
		//Creating a terrain 
		Terrain terrain = new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("FancyTexture")));
		Terrain terrain2 = new Terrain(-1,-1,loader,new ModelTexture(loader.loadTexture("FancyTexture")));
		//Creating a camera
		Camera camera = new Camera();
//		//A list of entities to try out new main rendering method
//		List<Entity> allDragons = new ArrayList<Entity>();
//		Random random = new Random();
//		for(int i = 0; i < 200; i++) {
//			float x = random.nextFloat() * 100 - 50;
//			float y = random.nextFloat() * 100 - 50;
//			float z = random.nextFloat() * -300;
//			allDragons.add(new Entity(dragonModel, new Vector3f(x,y,z), random.nextFloat() * 180f,
//					random.nextFloat() * 180f, 0f, 1f));
//		}
		//Create the renderer
		MainRenderer renderer = new MainRenderer();
		//While in the game loop, objects are updated and rendering is done. Loop will continue until display
		//is closed.
		while (!Display.isCloseRequested()) {
			//entity.increasePosition(0, 0, -0.1f);
			//entity.increaseRotation(0, 1, 0);
			//Move the camera every frame
			camera.move();
			//call any terrains to be rendered
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			renderer.processEntity(entity);
//			//for any entities to render need to call that entity in the process entity method.
//			for (Entity dragon : allDragons) {
//				renderer.processEntity(dragon);
//			}
			//Call the renderer every frame
			renderer.render(light, camera);
			//The display is updated every frame.
			DisplayManager.updateDisplay();
		}
		
		//Cleanup renderer & loader once the game is closed.
		renderer.cleanUp();
		loader.cleanUp();
		
		//When the close is requested and loop exited, the display is closed.
		DisplayManager.closeDisplay();
		
	}

}
