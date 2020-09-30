package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

//An entity is basically an instance of a textured model. A single entity can be rendered many times in the world but 
//the model will only need to be loaded up once. 
public class Entity {
	//Contains a textured model & position, rotation and scale that the model is to be rendered at. 
	private TexturedModel model;
	private Vector3f position;
	private float rotX,rotY,rotZ;
	private float scale;
	
	//Constructor for the entity. 
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	//Method to move entity around, takes in x y & z values of how far entity is to be moved. 
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	//Method to rotate the entity, takes in x y & z values of the angle to rotate the entity
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}
	
	//Getters and setters for the entity 
	public TexturedModel getModel() {
		return model;
	}
	public void setModel(TexturedModel model) {
		this.model = model;
	}
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public float getRotX() {
		return rotX;
	}
	public void setRotX(float rotX) {
		this.rotX = rotX;
	}
	public float getRotY() {
		return rotY;
	}
	public void setRotY(float rotY) {
		this.rotY = rotY;
	}
	public float getRotZ() {
		return rotZ;
	}
	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}
	public float getScale() {
		return scale;
	}
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	
	
	
}
