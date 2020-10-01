package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

//Class that renders the model from the VAO. 
public class Renderer {

	//Define a few constants for the projection Matrix, such as field of view, near plane & far plane. 
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	//Declare a projection matrix variable to use & static shader to be set in constructor so can be accessed
	//whenever rendering 
	private Matrix4f projectionMatrix;
	private StaticShader shader;
	
	//Constructor for the Renderer to call the create matrix method as the matrix wont change and will take 
	//in the static shader. GL cull face used to not bother rendering back faces of models as are never seen
	//by the camera.
	public Renderer(StaticShader shader) {
		this.shader = shader;
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	//Called once every frame to prepare OpenGL to render the game. 
	public void prepare() {
		//Tell OpenGL which triangles to render, the depth test will test which triangles are in front
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		//Clear the colour and depth buffer every single frame.
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		//(0, 0, 0, 0) is transparent which would be black. 
		GL11.glClearColor(1, 0, 0, 1);
	}
	
	//New method to render an entity using the hash map created in the main renderer class. Broken into smaller 
	//methods below
	public void render(Map<TexturedModel,List<Entity>> entities) {
		//Loop through the hash map and prep the textured model for the entities that use that model
		for(TexturedModel model:entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			//Then for each entity in this batch prep the instance 
			for(Entity entity:batch) {
				prepareInstance(entity);
				//Now rendering the VAO, as now using indices vbo will use Draw Element method. Mode is triangles, no 
				//of indices to render which is in the model, type of data is unsigned ints and where in the data it 
				//should start which is the beginning or 0.
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			//once all entities are rendered unbind the textured model so it can loop back to the next model
			unbindTexturedModel();
		}
	}
	//Method to prepare a textured model
	public void prepareTexturedModel(TexturedModel model) {
		//Getting the RawModel for binding the vertex array amongst other things. 
		RawModel rawModel = model.getRawModel();
		//First bind the VAO to be able to use it. 
		GL30.glBindVertexArray(rawModel.getVaoID());
		//Activate attribute lists where data is stored.
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		//Get the model texture and load up specular lighting values every time an entity is rendered.
		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(texture.getShineDampener(), texture.getReflectivity());
		//Activate a texture bank for Open GL. 
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		//Bind the texture, takes in the type and texture id from the TexturedModel. 
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	//Method to unbind Textured Model once all the entities using that model are rendered
	public void unbindTexturedModel() {
		//Disable attribute lists once finished using them.
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		//Unbind the VAO once finished using it by using 0 instead of an ID. 
		GL30.glBindVertexArray(0);
	}
	//Method to prep the instances of the entities 
	private void prepareInstance(Entity entity) {
		//Create a transformation matrix for rendering the model
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		//Load transformationMatrix into the static shader
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
//	//Old method to render an entity. Not very efficient!
//	public void render(Entity entity, StaticShader shader) {
//		//Getting the TexturedModel for the StaticShader & Entity.
//		TexturedModel model = entity.getModel();
//		
//		//Getting the RawModel for binding the vertex array amongst other things. 
//		RawModel rawModel = model.getRawModel();
//		//First bind the VAO to be able to use it. 
//		GL30.glBindVertexArray(rawModel.getVaoID());
//		//Activate attribute lists where data is stored.
//		GL20.glEnableVertexAttribArray(0);
//		GL20.glEnableVertexAttribArray(1);
//		GL20.glEnableVertexAttribArray(2);
//		
//		//Create a transformation matrix for rendering the model
//		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
//				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
//		//Load transformationMatrix into the static shader
//		shader.loadTransformationMatrix(transformationMatrix);
//		
//		//Get the model texture and load up specular lighting values every time an entity is rendered.
//		ModelTexture texture = model.getTexture();
//		shader.loadShineVariables(texture.getShineDampener(), texture.getReflectivity());
//		//Activate a texture bank for Open GL. 
//		GL13.glActiveTexture(GL13.GL_TEXTURE0);
//		//Bind the texture, takes in the type and texture id from the TexturedModel. 
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
//		//Now rendering the VAO, as now using indices vbo will use Draw Element method. Mode is triangles, no 
//		//of indices to render which is in the model, type of data is unsigned ints and where in the data it 
//		//should start which is the beginning or 0.
//		
//		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
//		
//		//Disable attribute lists once finished using them.
//		GL20.glDisableVertexAttribArray(0);
//		GL20.glDisableVertexAttribArray(1);
//		GL20.glDisableVertexAttribArray(2);
//		//Unbind the VAO once finished using it by using 0 instead of an ID. 
//		GL30.glBindVertexArray(0);
//	}
	
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
	
	
}

