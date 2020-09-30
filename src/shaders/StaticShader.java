package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import toolbox.Maths;

//Implementation of the Shader Program for static shaders. 
public class StaticShader extends ShaderProgram {
	
	//Declare the file paths as strings. 
	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	//So that uniform variable locations can be stored. 
	private int location_transformationMatrix;
	//So that projection matrix variables can be stored. 
	private int location_projectionMatrix;
	//So that view matrice variables can be stored.
	private int location_viewMatrix;

	//Defined constructor from the abstract shader program class. Pass in the vertex file path strings. 
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	//Override Method to bind the attributes
	@Override
	protected void bindAttributes() {
		//Binding the VAO listID zero to position variable in shader files
		super.bindAttribute(0, "position");		
		//Binding the VAO listID one to texture coords variable in shader files
		super.bindAttribute(1, "textureCoords");
	}
	//Override method to get the uniform variable names from shader file and store to the 
	//class variables. 
	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}
	//Method to load up a transformation Matrix to the class variable. 
	public void loadTransformationMatrix(Matrix4f matrix) {
		//Already have this method in the shader class so just use super. to call it here.
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	//Method to load up a view matrix to the class variable.
	public void loadViewMatrix(Camera camera) {
		//Create the view matrix
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		//Load the matrix
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	//Method to load up a projection Matrix to the class variable. 
	public void loadProjectionMatrix(Matrix4f projection) {
		//Already have this method in the shader class so just use super. to call it here. 
		super.loadMatrix(location_projectionMatrix, projection);
	}
}
