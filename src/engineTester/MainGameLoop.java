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
		RawModel model = OBJLoader.loadObjModel("ballTreeEdgeSplit", loader);
		//Creating a textured model object, passing in the rawmodel and the texture. 
		TexturedModel ballTreeModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("ballTreeTexture")));
		//Get the modeltexture to set the shine dampener and reflectivity values. 
		ModelTexture texture = ballTreeModel.getTexture();
		texture.setShineDampener(30);
		texture.setReflectivity(0);
		//Creating an entity that takes in the textured model we want it to show, needs position to be rendered at,
//		Entity entity = new Entity(staticModel, new Vector3f(0,0,-25),0,0,0,1);
		//Creating the light source, setting position and colour (1,1,1) is white
		Light light = new Light(new Vector3f(2000,2000,2000),new Vector3f(1,1,0.3294f));		
		//Creating a terrain 
		Terrain terrain = new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("grassTerrain")));
		Terrain terrain2 = new Terrain(-1,-1,loader,new ModelTexture(loader.loadTexture("grassTerrain")));
		//Creating a camera
		Camera camera = new Camera();
		//A list of entities to try out new main rendering method
		List<Entity> allBallTrees = new ArrayList<Entity>();
		Random random = new Random();
		for(int i = 0; i < 50; i++) {
			float x = random.nextFloat() * 800 - 50;
			float y = 0f;
			float z = random.nextFloat() * -600;
			allBallTrees.add(new Entity(ballTreeModel, new Vector3f(x,y,z), 0f,
					random.nextFloat() * 180f, 0f, 4f));
		}
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
//			renderer.processEntity(entity);
			//for any entities to render need to call that entity in the process entity method.
			for (Entity ballTree : allBallTrees) {
				renderer.processEntity(ballTree);
			}
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
