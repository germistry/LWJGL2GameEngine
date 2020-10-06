package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

//class to load up OBJ files for models
public class OBJLoader {

	public static RawModel loadObjModel(String fileName, Loader loader) {
		//new file reader to open the file, will always be in the res folder with extension of .obj
		//surrounded with try/catch in case file is missing
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/obj/"+fileName+".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Could not locate object file.");
			e.printStackTrace();
		}
		//Buffered reader created to allow reading from file.
		BufferedReader reader = new BufferedReader(fr);
		//Line being read
		String line;
		//List of vertices read stored into ArrayList as 3d vectors
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		//List of texture co-ords stored into ArrayList as 2d vectors.
		List<Vector2f> textures = new ArrayList<Vector2f>();
		//Another list of normal vectors, used for lighting
		List<Vector3f> normals = new ArrayList<Vector3f>();
		//List of indices as integers
		List<Integer> indices = new ArrayList<Integer>();
		//Create float/int arrays as that is how the loader needs the data to load into a VAO
		float[] verticesArray = null;
		float[] texturesArray = null;
		float[] normalsArray = null;
		int[] indicesArray = null;
		//All surrounded with a try & catch to deal with any invalid file format.
		try {
			//Infinite loop to read through the file until ready to break
			while(true) {
				line = reader.readLine();
				//split each line at a space
				String[] currentLine = line.split(" ");
				//Check if line begins with v (indicates a vertex)
				if (line.startsWith("v ")) {
					//Read in data from that line and create a new vertex, position [0] of the line is the 'v'
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
					//Add to list of vertices
					vertices.add(vertex);				
				//Check if line begins with vt (indicates a texture co-ord)
				}else if (line.startsWith("vt ")) {
					//Read in data from that line and create a new texture co-ord, position [0] of the line is the 'v'
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]));
					//Add to list of textures
					textures.add(texture);
				//Check if line begins with vn (indicates a normal)
				}else if (line.startsWith("vn ")) {
					//Read in data from that line and create a new normal, position [0] of the line is the 'v'
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
					//Add to list of normals
					normals.add(normal);
				//check if line begins with f (indicates a face)
				}else if (line.startsWith("f ")) {
					//If starts with f we can now break out of the loop as we have read in all the data
					//First set up Arrays as have size of them 
					texturesArray = new float[vertices.size()*2];
					normalsArray = new float[vertices.size()*3];
					break;
				}
			}
			while (line!=null) {
				//if the line doesn't start with the f skip it 
				if (!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				//Once line with a face is read break up into 3 parts for each vertex and will give a string array of 3 numbers
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				//Use the process vertex method to parse data and put into correct positions in the arrays
				//called for each vertex in triangle currently being processed
				processVertex(vertex1,indices,textures,normals,texturesArray,normalsArray);
				processVertex(vertex2,indices,textures,normals,texturesArray,normalsArray);
				processVertex(vertex3,indices,textures,normals,texturesArray,normalsArray);
				//then continue reading the next line
				line = reader.readLine();
			}
			//close reader once finished
			reader.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		//Convert lists into arrays
		verticesArray = new float[vertices.size()*3];
		indicesArray = new int[indices.size()];
		//Loop through vertices list and for each vertex put each component of the vector into the array
		int vertexPointer = 0;
		for(Vector3f vertex:vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		//Loop through indices list and for each indices put each component into the array
		for(int i=0;i<indices.size();i++) {
			indicesArray[i] = indices.get(i);
		}
		//load the arrays into the Vao
		return loader.loadtoVAO(verticesArray, texturesArray, normalsArray, indicesArray);
	}
	//simplify code by separate method to process each vertex string array 
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures,
			List<Vector3f> normals, float[] texturesArray, float[] normalsArray) {
		//Putting the texture & normal values in the correct array, obj files start at 1 so have to -1
		int currentVertexPointer = Integer.parseInt(vertexData[0])-1;
		indices.add(currentVertexPointer);
		//textures have 2 coords so *2 
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
		texturesArray[currentVertexPointer*2] = currentTex.x;
		//GL starts from top left whereas blender starts from bottom left
		texturesArray[currentVertexPointer*2+1] = 1 - currentTex.y;
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
		//normals are 3d vectors so *3
		normalsArray[currentVertexPointer*3] = currentNorm.x;
		normalsArray[currentVertexPointer*3+1] = currentNorm.y;
		normalsArray[currentVertexPointer*3+2] = currentNorm.z;
	}
}

