package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MainRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

	public static void main(String[] args) {
		//First open the display.
		DisplayManager.createDisplay();
		//Open a new loader.  
		Loader loader = new Loader();
		
		//-----------------------------Terrain Textures-------------------------------
		//Load each texture for the texture pack
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("GrassGreenTexture0001"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("GrassGreenTexture0004"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("soil"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("brickPath"));
		//Load the texture pack
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		//Load the blendMap
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		//-----------------------------Entities----------------------------------------
		//Load the raw model from obj file passing in the file name 
		RawModel model = OBJLoader.loadObjModel("ballTreeEdgeSplit", loader);
		//Creating a textured model object, passing in the rawmodel and the texture. 
		TexturedModel ballTree = new TexturedModel(model,new ModelTexture(loader.loadTexture("ballTreeTexture")));
		ModelTexture ballTreeTexture = ballTree.getTexture();
		ballTreeTexture.setShineDampener(100000);
		ballTreeTexture.setReflectivity(0);
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), 
				new ModelTexture(loader.loadTexture("fern")));
		ModelTexture fernTexture = fern.getTexture();
		fernTexture.setShineDampener(100);
		fernTexture.setReflectivity(0);
		fernTexture.setHasTransparency(true);
		fernTexture.setUseFakeLighting(true);
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("longGrassModel", loader),
				new ModelTexture(loader.loadTexture("longGrassTexture")));
		ModelTexture grassTexture = grass.getTexture();
		grassTexture.setShineDampener(100);
		grassTexture.setReflectivity(0);
		grassTexture.setHasTransparency(true);
		grassTexture.setUseFakeLighting(true);
		//Lists of randomised entities 
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for(int i = 0; i < 200; i++) {
			entities.add(new Entity(ballTree, new Vector3f(random.nextFloat() * 800 - 400, 0,
					random.nextFloat() * -600), 0, 0, 0, 1));
			entities.add(new Entity(fern, new Vector3f(random.nextFloat() * 800 - 400, 0,
					random.nextFloat() * -600), 0, 0, 0, 1));
			entities.add(new Entity(grass, new Vector3f(random.nextFloat() * 800 - 400, 0,
					random.nextFloat() * -600), 0, 0, 0, 1));
		}
		//Player entity
		RawModel bunnyModel = OBJLoader.loadObjModel("stanfordBunny", loader);
		TexturedModel stanfordBunny = new TexturedModel(bunnyModel, 
				new ModelTexture(loader.loadTexture("furrTexture")));
		Player player = new Player(stanfordBunny, new Vector3f(0, 0, -25), 0, 0, 0, 1);
		//Creating an entity that takes in the textured model we want it to show, needs position to be rendered at,
		//Entity entity = new Entity(staticModel, new Vector3f(0,0,-25),0,0,0,1);
				
		//-----------------------------Light----------------------------------------
		
		//Creating the light source, setting position and colour (1,1,1) is white
		Light light = new Light(new Vector3f(20000,40000,20000),new Vector3f(1,1,0.3294f));		
		
		//-----------------------------Terrains----------------------------------------
		//Creating a terrain 
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap);
		//Creating a camera
		Camera camera = new Camera(player);
				
		//Create the renderer
		MainRenderer renderer = new MainRenderer();
		//While in the game loop, objects are updated and rendering is done. Loop will continue until display
		//is closed.
		while (!Display.isCloseRequested()) {
			//entity.increasePosition(0, 0, -0.1f);
			//entity.increaseRotation(0, 1, 0);
			//Move the camera every frame
			camera.move();
			//move the player every frame
			player.move();
			//send player to be rendered 
			renderer.processEntity(player);
			//call any terrains to be rendered
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			//Call the renderer every frame
			renderer.render(light, camera);
			//renderer.processEntity(entity);
			//for any entities to render need to call that entity in the process entity method.
			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}
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
