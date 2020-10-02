package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.ModelTexture;
import toolbox.Maths;

public class TerrainRenderer {

	private TerrainShader shader;
	
	//Constructor that takes in the shader & projection matrix just like other renderer classes
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	//Method for rendering all the terrains, takes in a list of them
	public void render(List<Terrain> terrains) {
		//loop through each terrain in the list, prepare & render it 
		for(Terrain terrain:terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
		}
	}
	//Method to prepare a terrain model
	public void prepareTerrain(Terrain terrain) {
		//Getting the RawModel for binding the vertex array amongst other things. 
		RawModel rawModel = terrain.getModel();
		//First bind the VAO to be able to use it. 
		GL30.glBindVertexArray(rawModel.getVaoID());
		//Activate attribute lists where data is stored.
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		//Get the model texture and load up specular lighting values.
		ModelTexture texture = terrain.getTexture();
		shader.loadShineVariables(texture.getShineDampener(), texture.getReflectivity());
		//Activate a texture bank for Open GL. 
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		//Bind the texture, takes in the type and texture id from the TexturedModel. 
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
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
	//Method to prep the instance of the terrain 
	private void loadModelMatrix(Terrain terrain) {
		//Create a transformation matrix for rendering the model
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(),0,terrain.getZ()), 
				0, 0, 0, 1);
		//Load transformationMatrix into the static shader
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
