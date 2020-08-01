package models;

//This class represents the 3d model stored in memory.
public class RawModel {

	//Need the ID of the 3d model (the VAO) and number of vertices it has. 
	private int vaoID;
	private int vertexCount;
	
	//Simple constructor that takes in the vaoID and vertexCount. 
	public RawModel (int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
}
