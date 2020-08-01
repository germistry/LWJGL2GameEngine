package textures;

//Class that represents the texture of the model. Note that textures need to be stored in the "res" folder, 
//need to be square .png, and must be 2^n x 2^n pixels in size, so 2x2, 4x4, 8x8, 16x16, 32x32, 64x64, 128x128, 
//256x256, 512x512 etc etc. 
public class ModelTexture {

	//Just need the texture ID at this stage, will be added to later. 
	private int textureID;
	//Simple constructor that passes in the texture id. 
	public ModelTexture(int id) {
		this.textureID = id;
	}
	//Getter for the texture ID. 
	public int getID() {
		return this.textureID;
	}
	
}
