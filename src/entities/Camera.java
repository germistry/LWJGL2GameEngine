package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

//The Camera or View Matrices is where when the 'camera' moves left, the world actually shifts to the right
// to make it appear to the player they are moving to the left. 
public class Camera {

	//Camera set to 0,0,0 initially
	private Vector3f position = new Vector3f(0,10,0);
	//Rotation around the x,y,z axes (also known as camera pitch)
	private float pitch;
	//How far left or right
	private float yaw;
	//How far the camera is tilted to one side or another, ie. 180 degrees camera is upside down
	private float roll;
	
	public Camera() {}
	
	//Method to call to move the camera according to what keys are pressed.
	public void move() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W))	{
			//moving along -z axis, decreases
			position.z-=0.1f; 
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D))	{
			//moving along x axis, increases
			position.x+=0.1f; 
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A))	{
			//moving along -x axis, decreases
			position.x-=0.1f; 
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			//moving along z axis, increases
			position.z+=0.1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			//moving along y axis, increasing
			position.y+=0.1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			//moving along y axis, decreasing 
			position.y-=0.1f;
		}
	}

	//Getters for all the properties
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	
}
