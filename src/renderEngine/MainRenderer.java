package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;

//Final rendering class to handle all of the rendering in the game. Optimises the original 'Renderer' class 
//as if multiple of the same model need to be rendered, the binding and unbinding to the VAO doesn't have to 
//repeated. Instead done once for each model
public class MainRenderer {

	private StaticShader shader = new StaticShader();
	private Renderer renderer = new Renderer(shader);
	
	//Hash map of all the models & their textures needed to render a frame. 
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	
	//Method that called once to render all the entities once per scene 
	public void render(Light lightSource, Camera camera) {
		renderer.prepare();
		shader.start();
		shader.loadLight(lightSource);
		shader.loadViewMatrix(camera);
		//renderer now called and given all entities in the hash map
		renderer.render(entities);
		shader.stop();
		//Make sure the hashmap is cleared after the rendering
		entities.clear(); 
	}
	//Method to sort entities every frame and put them in the hash map
	public void processEntity(Entity entity) {
		//Get the model the entity is using
		TexturedModel entityModel = entity.getModel();
		//Get the list that corresponds to that entity from the hash map
		List<Entity> batch = entities.get(entityModel);
		//if batch for that textured model already exists then just add entity to the batch
		if(batch!=null) {
			batch.add(entity);
		}else {
			//if entity's textured model is not in the batch create a new batch 
			List<Entity> newBatch = new ArrayList<Entity>();
			//add the entity to the batch and then add the batch to the hash map
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	//Method to clean up when game is closed
	public void cleanUp() {
		shader.cleanUp();
	}
}
