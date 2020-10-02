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
public class EntityRenderer {

	//Declare a static shader to be set in constructor so can be accessed whenever rendering 
	private StaticShader shader;
	
	//Constructor for the entity renderer to call the create matrix method as the matrix wont change and will take 
	//in the static shader. 
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
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
	
}

