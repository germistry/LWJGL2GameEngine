package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

//class to do useful things in the game engine, like the maths needed to get transformation matrices
public class Maths {
	//Method for creating transformation matrices. Takes in the 3d vector translation, rotation xy & 
	//z values & scale value. 
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, 
			float rz, float scale) {
		//Create a new empty matrix 
		Matrix4f matrix = new Matrix4f();
		//Set the matrix to the identity matrix (resetting the matrix)
		matrix.setIdentity();
		//LWJGL does the matrix maths. First translate the matrix, takes in the translation, source and destination
		Matrix4f.translate(translation, matrix, matrix);
		//Second rotate matrix around each 3 axes. Done for each axes.
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		//Lastly scale the matrix. Scaled uniformly around each axes 
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		return matrix;
	}
	//Method for creating camera view matrices. Doing the opposite of what would what be done with a transformation matrix. 
	public static Matrix4f createViewMatrix(Camera camera) {
		//Create a new empty matrix 
		Matrix4f viewMatrix = new Matrix4f();
		//Set the matrix to the identity matrix (resetting the matrix)
		viewMatrix.setIdentity();
		//Rotate matrix around the x axis
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), viewMatrix, viewMatrix);
		//Rotate matrix around the y axis
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), viewMatrix, viewMatrix);
		//Get camera position
		Vector3f cameraPos = camera.getPosition();
		//Get the negative of camera position
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		//Now translate the matrix
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}
}
