package company.ouroboros.flappy.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import company.ouroboros.flappy.utils.BufferUtils;

public class VertexArray {
	
	private int vao, vbo, ibo, tbo;
	private int count;
	
	public VertexArray(int count) {
		this.count = count;
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
	}
	
	public VertexArray(float[] vertices, byte[] indices, float[] textureCoordinates) {
		count = indices.length;
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices), GL_STATIC_DRAW);
		glVertexAttribPointer(Shader.VERTEX_ATTRIBUTE, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(Shader.VERTEX_ATTRIBUTE);
		
		tbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(textureCoordinates), GL_STATIC_DRAW);
		glVertexAttribPointer(Shader.TCOORD_ATTRIBUTE, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(Shader.TCOORD_ATTRIBUTE);
		
		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public void bind() {
		glBindVertexArray(vao);
		if(ibo > 0)
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
	}
	
	public void unbind() {
		glBindVertexArray(0);
		if(ibo > 0)
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public void draw() {
		if(ibo > 0)
			glDrawElements(GL_TRIANGLE_FAN, count, GL_UNSIGNED_BYTE, 0);
		else 
			glDrawArrays(GL_TRIANGLE_FAN, 0, count);
	}
	
	public void render() {
		bind();
		draw();
	}
}
