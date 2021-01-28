package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

//Implementation of the Shader Program for static shaders. 
public class StaticShader extends ShaderProgram {
	
	//Declare the file paths as strings. 
	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	//So that uniform variable locations can be stored. 
	private int location_transformationMatrix; 
	private int location_projectionMatrix;
	private int location_viewMatrix;
	//Both for light locations
	private int location_lightPosition;
	private int location_lightColour;
	//Both for specular lighting locations
	private int location_shineDampener;
	private int location_reflectivity;
	//For transparency 
	private int location_useFakeLighting;
	//For Fog
	private int location_skyColour;
	//for texture atlas
	private int location_numberOfRows;
	private int location_offset;

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
		//Binding the VAO listID two to normals variable in shader files
		super.bindAttribute(2, "normal");
	}
	//Override method to get the uniform variable names from shader file and store to the 
	//class variables. 
	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_shineDampener = super.getUniformLocation("shineDampener");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		location_skyColour = super.getUniformLocation("skyColour");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
	}
	
	//Methods to load up number of rows & offset for texture atlas
	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(location_numberOfRows, numberOfRows);
	}
	public void loadOffset(float x, float y) {
		super.loadVector(location_offset, new Vector2f(x,y));
	}
	
	//Method to load up sky colour for fog effect.
	public void loadSkyColour(float r, float g, float b) {
		super.loadVector(location_skyColour, new Vector3f(r,g,b));
	}
	//Method to load up use Fake lighting variable.
	public void loadFakeLightingVariable(boolean useFake) {
		super.loadBoolean(location_useFakeLighting, useFake);
	}
	//Method to load up specular lighting variables. 
	public void loadShineVariables(float dampener, float reflectivity) {
		super.loadFloat(location_shineDampener, dampener);
		super.loadFloat(location_reflectivity, dampener);
	}
	//Method to load up a transformation Matrix to the class variable. 
	public void loadTransformationMatrix(Matrix4f matrix) {
		//Already have this method in the shader class so just use super. to call it here.
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	//Methods to load up the light variables into the class.
	public void loadLight(Light light) {
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
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
