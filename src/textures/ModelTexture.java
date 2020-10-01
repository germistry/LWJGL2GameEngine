package textures;

//Class that represents the texture of the model. Note that textures need to be stored in the "res" folder, 
//need to be square .png, and must be 2^n x 2^n pixels in size, so 2x2, 4x4, 8x8, 16x16, 32x32, 64x64, 128x128, 
//256x256, 512x512 etc etc. 
public class ModelTexture {

	//Texture Id property, specular lighting properties. 
	private int textureID;
	private float shineDampener = 1;
	private float reflectivity = 0;
	
	
	//Simple constructor that passes in the texture id. 
	public ModelTexture(int id) {
		this.textureID = id;
	}
	//Getter for the texture ID. 
	public int getID() {
		return this.textureID;
	}
	//Getters & setters for specular lighting properties. 
	public float getShineDampener() {
		return shineDampener;
	}
	public void setShineDampener(float shineDampener) {
		this.shineDampener = shineDampener;
	}
	public float getReflectivity() {
		return reflectivity;
	}
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}	
}
