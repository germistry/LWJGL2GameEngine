package terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

public class Terrain {

	//final settings for the terrain, eg 800 chunks 
	private static final float SIZE = 800;
	private static final float MAX_HEIGHT = 40; //ie min height therefore -40
	private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;  // 0-255 on each r g b channel 
	
	//Flat terrain just has a x and z value. 
	private float x;
	private float z;
	//Mesh for the terrain and the texturepack & blendmap for the terrain. 
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	//Store height of each terrain vertex for player/entity collision detection in a table (float array of arrays). 
	private float[] [] heights;
	
	//Constructor for the terrain, takes in the coordinates, the loader & texture.
	//The Raw model is generated in this class.
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, 
			TerrainTexture blendMap, String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		//Work out the x & z position by multipling by the size 
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		//Generate the terrain (using the code below at this stage for a simple flat terrain
		this.model = generateTerrain(loader, heightMap);
	}
	
	//Raw Model code to generate the terrain with a height map.
	private RawModel generateTerrain(Loader loader, String heightMap){
		//Load heightmap into a buffered image.
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("res/" + heightMap + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Vertex Count based on height of image used for heightmap 
		int VERTEX_COUNT = image.getHeight();
		//set number of vertices to each side of terrain
		heights = new float[VERTEX_COUNT] [VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				//calculating vertices 
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j, i, image);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				//calculating normals so lighting effects work on the terrain
				Vector3f normal = calculateNormal(j, i, image);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				//calculating texture Coords
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadtoVAO(vertices, textureCoords, normals, indices);
	}
	//Method to calculate the normal for a vertex on heightmap based on neighbouring vertices
	//TODO Optimise this as the height is being calc twice but not large effect as only happens once when 
	//game starts
	private Vector3f calculateNormal(int x, int z, BufferedImage image) {
		//calculating the vertex's neighbours
		float heightL = getHeight(x-1, z, image);
		float heightR = getHeight(x+1, z, image);
		float heightD = getHeight(x, z-1, image);
		float heightU = getHeight(x, z+1, image);
		//calculate the normal vector by creating a new vector3f with calculated x & z and y just set at 2f 
		Vector3f normal = (new Vector3f(heightL-heightR, 2f, heightD-heightU));
		//normalise vector & then return it
		normal.normalise();
		return normal;
	}
	
	//Method to return the height represented by a pixel on heightmap
	private float getHeight(int x, int y, BufferedImage image) {
		//if out of bounds return 0
		if(x<0 || x>=image.getHeight() || y<0 || y>=image.getHeight()) {
			return 0;
		}
		//get the pixel height
		float height =  image.getRGB(x, y);
		height += MAX_PIXEL_COLOUR/2f;
		height /= MAX_PIXEL_COLOUR/2f;
		height *= MAX_HEIGHT;
		return height;
	}
	//Method to get the height of terrain given x or z coordinates
	public float getHeightOfTerrain(float worldX, float worldZ) {
		//Convert world coordinate into position relative to the terrain
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		//Calculate size of each grid square
		float gridSquareSize = SIZE / ((float)heights.length - 1);
		//calculate which grid square the player is in
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		//test if this x,z position is actually on the terrain, ie not to the left or above the terrain looking
		//from straight down
		if(gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}
		//use modulas to find out distance player is from the 0,0 origin, result gives a x,z coord between 0 and 1
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		//Work out which triangle the player is on, ie top left triangle has vertices 0,0 0,1 1,0 so h boundary is 
		// x < 1-z and so bottom triangle h boundary is modelled by x > 1-z. Use barycentric maths for this one
		float answer;
		if(xCoord <= (1 - zCoord)) {
			answer = Maths.barycentricCalc(
					new Vector3f(0, heights[gridX][gridZ] , 0), 
					new Vector3f(1, heights[gridX + 1][gridZ] , 0), 
					new Vector3f(0, heights[gridX][gridZ + 1] , 1), 
					new Vector2f(xCoord, zCoord)); 
		} else {
			answer = Maths.barycentricCalc(
					new Vector3f(1, heights[gridX + 1][gridZ] , 0), 
					new Vector3f(1, heights[gridX + 1][gridZ + 1] , 1), 
					new Vector3f(0, heights[gridX][gridZ + 1] , 1), 
					new Vector2f(xCoord, zCoord)); 
		}
		return answer;
	}
	//Getters for the properties, not needed for the static values 
	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
}
