package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

//Class that renders the model from the VAO. 
public class Renderer {

	//Called once every frame to prepare OpenGL to render the game. 
	public void prepare() {
		//Clear the colour from the last frame, background and model. Using red for the background.
		//(0, 0, 0, 0) is transparent which would be black. 
		GL11.glClearColor(1, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	//Method to render the actual model.
	public void render(RawModel model) {
		//First bind the VAO to be able to use it. 
		GL30.glBindVertexArray(model.getVaoID());
		//Activate attribute list where 0 is the list number, where data is stored.
		GL20.glEnableVertexAttribArray(0);
		//Now rendering the VAO, mode is Triangles, where in the data it starts rendering and how many
		//vertices it should render. 
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
		//Disable attribute list once finished using it.
		GL20.glDisableVertexAttribArray(0);
		//Unbind the VAO once finished using it by using 0 instead of an ID. 
		GL30.glBindVertexArray(0);
	}
}
