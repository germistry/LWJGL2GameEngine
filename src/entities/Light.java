package entities;

import org.lwjgl.util.vector.Vector3f;

//First light source.
public class Light {

	//Two 3d vector properties to represent position of light and it's intensity and the constructor.
	private Vector3f position;
	private Vector3f colour;
	
	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}
	//Getters & setters 

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
	
	
}
