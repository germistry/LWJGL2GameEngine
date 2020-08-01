package models;

import textures.ModelTexture;

//Class represents textured models.
public class TexturedModel {

	//Bring in the RawModel class which contains all the data on the model so it can be used for a textured 
	//model also. 
	private RawModel rawModel;
	//Bring in the ModelTexture class which has the texture data. 
	private ModelTexture texture;
	
	//Simple Constructor that takes in the model and texture.
	public TexturedModel(RawModel model, ModelTexture texture) {
		this.rawModel = model;
		this.texture = texture;
	}
	//Getter for RawModel
	public RawModel getRawModel() {
		return rawModel;
	}
	//Getter for ModelTexture
	public ModelTexture getTexture() {
		return texture;
	}
	
}
