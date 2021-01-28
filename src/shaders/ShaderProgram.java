package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

//Abstract class as this program represents a generic shader program that has all the attributes and 
//methods every shader program would have. A specific shader program will have a concrete implementation of
//this class. 
public abstract class ShaderProgram {

	//Each specific shader program will have a programID, and the vertexShaderID and fragmentShaderID that
	//makes up that specific program.
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	//Create float buffer for loading up matrices, will be using 4x4 matrices so will use 16 floats. 
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	//Constructor for the Shader Program, takes in the file path of both the vertexFile and fragmentFile 
	//to use. 
	public ShaderProgram(String vertexFile,String fragmentFile) {
		//Load up both the vertexFile and fragmentFile in OpenGL
		vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
		//Create a new program which ties both the vertex shader and fragment shader together
		programID = GL20.glCreateProgram();
		//Attach both shaders to this new program, bind attributes then link and validate program
		GL20.glAttachShader(programID,vertexShaderID);
		GL20.glAttachShader(programID,fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		//Call this abstract in shader program class to ensure all programs will have a constructor for uniform
		//variable locations.
		getAllUniformLocations();
	}
	//Abstract class to ensure all shader classes will have a method to get uniform variable locations. 
	protected abstract void getAllUniformLocations();
	
	//Method to get the Uniform Variable locations in shader code, takes in the name of the variable as it appears in 
	//the shader code and return a int which represents its location. 
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	//Method to start program whenever it is used
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	//Method to stop program when finished being used. 
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	//Method for memory management, call stop to check no program is currently being used, 
	public void cleanUp() {
		stop();
		//Detach both shaders from the program. 
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		//Delete both shaders from the program. 
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		//Delete the program 
		GL20.glDeleteProgram(programID);
	}
	
	//Abstract method for binding attributes to implementations of this class. Will link up inputs of shader
	//programs to one of attributes of the VAO. 
	protected abstract void bindAttributes();
	
	//Method for binding an attribute as it can't be done outside this class, uses the programID. Takes in 
	//the attribute number of the VAO and the variable name in the Shader code we want to bind that attribute
	//to. 
	protected void bindAttribute (int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	//Method to load up values to uniform variable locations, simplest below is a float. 
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	//Method to load up values to uniform variable locations, in this case for int. 
	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}
	//Another method to load up values to uniform variable locations, in this case for 2d vectors 
		protected void loadVector(int location, Vector2f vector) {
			GL20.glUniform2f(location, vector.x, vector.y);
		}
	//Another method to load up values to uniform variable locations, in this case for 3d vectors 
	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	//Another method to load up values to uniform variable locations, in this case for a boolean value (which is 0 or 1 
	//as shaders don't use true/false. 
	protected void loadBoolean(int location, boolean value) {
		//If true will load up a 1 if false will load a zero. 
		float toLoad = 0;
		if (value) {
			toLoad = 1;
		};
		GL20.glUniform1f(location, toLoad);
	}
	//Method to load a matrix (4x4) and load up values to uniform variable locations
	protected void loadMatrix(int location, Matrix4f matrix) {
		//Store the matrix in the floatbuffer
		matrix.store(matrixBuffer);
		//Flip float buffer to get it ready to be read from 
		matrixBuffer.flip();
		//Load into the location of the uniform variable. Transpose is false. 
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	//Method to load up shader source files. Takes in the file name as a string and an int which indicates 
	//if it is a vertexShader or fragmentShader. 
	private static int loadShader(String file, int type) {
		//Reader to read the shader file while the lines are not null or empty. Surrounded with a try and 
		//catch.
		StringBuilder shaderSource = new StringBuilder();
		try {
				BufferedReader reader = new BufferedReader(new FileReader (file));
				String line;
				while ((line = reader.readLine())!=null) {
					shaderSource.append(line).append("\n");					
				}
				reader.close();
		}catch(IOException e) {
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		//Creating the type of shader program. 
		int shaderID = GL20.glCreateShader(type);
		//Takes in the shaderID and shaderSource string created in the string builder. 
		GL20.glShaderSource(shaderID, shaderSource);
		//Compiles the shader program if compile status is not false. 
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID,GL20.GL_COMPILE_STATUS)==GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID,500));
			System.err.println("Could not compile shader.");
			System.exit(-1);
		}
		//Returns the shader ID as an int. 
		return shaderID;
	}
	
	
}

