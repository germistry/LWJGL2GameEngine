package entities;

//import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

//The Camera or View Matrices is where when the 'camera' moves left, the world actually shifts to the right
// to make it appear to the player they are moving to the left. 
public class Camera {

	//Variables for camera movement the player can control - distance from player (zoom) 
	//and rotation (angle around player.
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	//Camera set to 0,10,0 initially
	private Vector3f position = new Vector3f(0,10,0);
	//Rotation around the x,y,z axes - the player can control this.
	private float pitch = 20;
	//How far left or right
	private float yaw;
	//How far the camera is tilted to one side or another, ie. 180 degrees camera is upside down
	private float roll;
	//Camera will follow the player entity. 
	private Player player;
	
	public Camera(Player player) {
		this.player = player;
	}
	
	//Method to call to move the camera after calc all the player/camera variables. 
	//(currently using keybindings for controlling the player movement. Will change this to WASD & Mouse, camera other
	//keys).
	public void move() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		//To rotate the camera around the player, yaw becomes 180 degrees - (player.rotY + angleAroundPlayer)
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
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
	//Method to calculate zoom (distance camera from player) using mouse scroll
	private void calculateZoom() {
		//multiplied by a float to reduce size of number so zoom doesnt zoom too quick
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		//decrease distance from player
		distanceFromPlayer -= zoomLevel;
	}
	//Method to calculate pitch using right mouse button
	private void calculatePitch() {
		if(Mouse.isButtonDown(1)) {
			//multiplied by a float to reduce size of number so it not so fast
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
	//Method to calculate rotation using left mouse button
	private void calculateAngleAroundPlayer() {
		if(Mouse.isButtonDown(0)) {
			//multiplied by a float to reduce size of number so not so fast
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
	//calculate the horizontal distance using distanceFromPlayer (h) * cos(pitch)
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	//calculate the vertical distance using distanceFromPlayer (h) * sin(pitch)
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	//Calculate the actual position of camera (x,y,z) using the horizontal & vertical distances
	private void calculateCameraPosition(float horiDistance, float vertDistance) {
		//Assuming camera starts off behind the player, and can be rotated by player, angle needed 
		//is the angleAroundPlayer + the rotY of the player. The x offset is therefore distanceFromPlayer 
		//(h) * sin(theta) and z offset is distanceFromPlayer (h) * cos(theta)
		float theta = player.getRotY() + angleAroundPlayer;
		float xOffset = (float) (horiDistance * Math.sin(Math.toRadians(theta)));
		float zOffset = (float) (horiDistance * Math.cos(Math.toRadians(theta)));
		//take the offsets away from the player position as both offset directions are negative
		position.x = player.getPosition().x - xOffset;
		position.z = player.getPosition().z - zOffset;
		//y calculation straightforward as just added to player y position
		position.y = player.getPosition().y + vertDistance;
	}
}
