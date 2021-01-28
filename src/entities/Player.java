package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrains.Terrain;

//Represents a player entity. 
public class Player extends Entity {

	//Constants representing player run speed (units per sec)and how fast they can turn (degrees per second) 
	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	//Gravity and jump power floats  
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;

	//Initial values for run, turn speed and jump speed 
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardSpeed = 0;
	//Boolean to check whether player is in the air so no double jumping!
	private boolean isInAir = false;
	
	//Constructor
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	//Method to move the player entity
	public void move(Terrain terrain) {
		checkInputs();
		//Calculate rotation if player is turning, multiply y angle by the getFrameTime method (in secs) 
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		//Calculate the next position player is moving to  
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		//Use simple trig to calculate the x and z values 
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		//Then use these values to move the player to that position
		super.increasePosition(dx, 0, dz);
		//upwards speed (or falling) is decreased by the amount of gravity every second
		upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		//Increase y position of the player 
		super.increasePosition(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		//get height of terrain at player position
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		//if y position of player is below terrain height set upward speed back to 0 and set player position 
		//to terrain height. If player is on the terrain isInAir is set back to false so they can jump again
		if(super.getPosition().y < terrainHeight) {
			upwardSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}
	//Jump method set upward speed to the jump power, if player is in the air they can not jump 
	private void jump() {
		if(!isInAir) {
			this.upwardSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
	
	//Checks the keyboard inputs and if pressed moves player in that direction/speed 
	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = RUN_SPEED;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
		}
		else {
			this.currentSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed = TURN_SPEED;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -TURN_SPEED;
		}
		else {
			this.currentTurnSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}

}
