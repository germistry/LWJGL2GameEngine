package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

//Final rendering class to handle all of the rendering in the game. Optimises the original 'Renderer' class 
//as if multiple of the same model need to be rendered, the binding and unbinding to the VAO doesn't have to 
//repeated. Instead done once for each model
public class MainRenderer {

	//Define a few constants for the projection Matrix, such as field of view, near plane & far plane.
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	//Define constants for sky colour.
	private static final float RED = 0.5098f;
	private static final float GREEN = 1;
	private static final float BLUE = 0.7059f;
		
	//Projection matrix created in main renderer for all the rendering 
	private Matrix4f projectionMatrix;
	//Attributes for entity renderer
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	//Attributes for terrain renderer
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	//Hash map of all the models & their textures needed to render a frame. 
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	//List of terrains to render each frame
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	//Constructor for the main renderer which will create the projection matrix for the renderers. 
	//GL cull face used to not bother rendering back faces of models as are never seen by the camera.
	public MainRenderer() {
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader,projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}
	//Method to enable backface culling for solid textures 
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	//Method to disable backface culling for transparent textures 
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	//Method that called once to render all the entities once per scene 
	public void render(Light lightSource, Camera camera) {
		prepare();
		//render the entities 
		shader.start();
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLight(lightSource);
		shader.loadViewMatrix(camera);
		//renderer now called and given all entities in the hash map
		renderer.render(entities);
		shader.stop();
		//render all the terrains 
		terrainShader.start();
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLight(lightSource);
		terrainShader.loadViewMatrix(camera);
		//terrain renderer called & given all the terrains in the list 
		terrainRenderer.render(terrains);
		terrainShader.stop();
		//Make sure the hashmap is cleared after the rendering
		entities.clear();
		//Make sure the list is cleared after rendering 
		terrains.clear();
	}
	//Method to add terrains to the list to be rendered
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
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
	//Called once every frame to prepare OpenGL to render the game. 
		public void prepare() {
			//Tell OpenGL which triangles to render, the depth test will test which triangles are in front
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			//Clear the colour and depth buffer every single frame.
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
			//(0, 0, 0, 0) is transparent which would be black. 
			GL11.glClearColor(RED, GREEN, BLUE, 1);
		}
	//Create a projection matrix to use. Code not that important to understand 
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustam_length = FAR_PLANE - NEAR_PLANE;
			
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustam_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustam_length);
		projectionMatrix.m33 = 0;
	}
	//Method to clean up when game is closed
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}
}
