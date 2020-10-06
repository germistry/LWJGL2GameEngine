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
import textures.TerrainTexturePack;
import toolbox.Maths;

public class TerrainRenderer {

	private TerrainShader shader;
	
	//Constructor that takes in the shader & projection matrix just like other renderer classes
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
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
		//Call the bind textures method to bind the textures
		bindTextures(terrain);
		//Load up specular lighting values - passing in values at this stage.
		shader.loadShineVariables(1, 0);
		
	}
	//Method to bind textures and blend map to the 5 separate texture units
	private void bindTextures(Terrain terrain) {
		//Get the texture pack
		TerrainTexturePack texturePack = terrain.getTexturePack();
		//Bind each to a texture unit by activating a texture bank and then binding.
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
	}
	
	
	
	//Method to unbind Textured Model once all the terrains using that model are rendered
	private void unbindTexturedModel() {
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
