package shaders;

//Implementation of the Shader Program for static shaders. 
public class StaticShader extends ShaderProgram {
	
	//Declare the file paths as strings. 
	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	

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

}
