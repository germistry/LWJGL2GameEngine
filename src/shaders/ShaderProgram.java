package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

//Abstract class as this program represents a generic shader program that has all the attributes and 
//methods every shader program would have. A specific shader program will have a concrete implementation of
//this class. 
public abstract class ShaderProgram {

	//Each specific shader program will have a programID, and the vertexShaderID and fragmentShaderID that
	//makes up that specific program.
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	//Constructor for the Shader Program, takes in the file path of both the vertexFile and fragmentFile 
	//to use. 
	public ShaderProgram(String vertexFile,String fragmentFile) {
		//Load up both the vertexFile and fragmentFile in OpenGL
		vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
		//Create a new program which ties both the vertex shader and fragment shader together
		programID = GL20.glCreateProgram();
		//Attach both shaders to this new program, then link and validate program
		GL20.glAttachShader(programID,vertexShaderID);
		GL20.glAttachShader(programID,fragmentShaderID);
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		//Calling abstract method to link inputs of shader programs to one of attributes of the VAO.
		bindAttributes();
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

