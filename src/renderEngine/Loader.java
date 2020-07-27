package renderEngine;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

//Loads 3D models into memory by storing positional data about the model in a VAO.  
public class Loader {

	//Will want some memory management so that all the VAOs and VBOs that have been created are 
	//deleted out of memory. 
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	
	//Method takes in positions of the model's vertices, puts this data into a VAO and returns information 
	//about the VAO as a RawModel object.
	public RawModel loadtoVAO(float[] positions) {
		//CreateVAO and store the id variable. 
		int vaoID = createVAO();
		//Store the vao into the list so it can be deleted from memory later. 
		vaos.add(vaoID);
		//Store the data into attributes list. Zero to begin with (doesn't really matter). 
		storeDataInAttributeList(0, positions);
		//Unbind the VAO once its finished being used. 
		unbindVAO();
		//Returns the raw model which has a vaoID and positions of the vertices, divided by 3 as there are 
		//x, y and z vertices. 
		return new RawModel(vaoID, positions.length/3);
	}
	
	//Method to delete all the created vaos and vbos from memory. 
	public void CleanUp() {
		//Will loop through vao list and for each vao will delete it.  
		for (int vao:vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		//Will loop through vbo list and for each vbo will delete it. 
		for (int vbo:vbos) {
			GL15.glDeleteBuffers(vbo);
		}
	}
	
	//First step for the RawModel loadtoVAO method is creating a new empty VAO and returning the ID.
	private int createVAO() {
		//Create a new empty VAO.
		int vaoID = GL30.glGenVertexArrays();
		//Have to activate the VAO to do anything to it, so bind it. 
		GL30.glBindVertexArray(vaoID);
		//Return the vaoID. 
		return vaoID;
	}
	//This stores the data into one of the attribute lists of the VAO. Takes in the number of the attribute 
	//and the data itself.  
	private void storeDataInAttributeList(int attributeNumber, float[] data) {
		//Need to store the data into one of the attribute lists as a vbo, so creating a VBO.
		int vboID = GL15.glGenBuffers();
		//Store the vbo into the list so it can be deleted from memory later. 
		vbos.add(vboID);
		//Need to bind the VBO to do anything to it like the VAO. Specifies the type and the id. 
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		//Calling the FloatBuffer method to store the data. 
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		//Storing the data into the vbo, takes in type, the data and what it is used for, this case it's 
		//static data as we are not going to then edit it. 
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		//Placing the vbo into one of the vao attribute lists, first arg is the number of the attribute
		//list, the size which is the length as they are 3d vectors, type of data, needs to know if data 
		//is normalised, distance between vertices and the offset ie should it start at the beginning of the data. 
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
		//Unbind the vbo now finished being used. 
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	//Method to unbind the VAO when its finished being used. 
	private void unbindVAO() {
		//Using a zero to unbind the VAO instead of ID. 
		GL30.glBindVertexArray(0);
	}
	
	//Data needs to be stored into a vbo as a float buffer, so this method converts float array of data 
	//into a float buffer. 
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		//Creating an empty float buffer that takes in the data length.  
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		//Put the data into the float buffer.
		buffer.put(data);
		//Prepare buffer to be read from as it otherwise expects to be written to. 
		buffer.flip();
		//Returning the buffer to use it to store data into the vbo. 
		return buffer;
	}
	
}
