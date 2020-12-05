package company.ouroboros.flappy.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import company.ouroboros.flappy.utils.BufferUtils;
import company.ouroboros.flappy.utils.ShaderUtils;

public class Shader {
	
	public static final int VERTEX_ATTRIBUTE = 0;
	public static final int TCOORD_ATTRIBUTE = 1;
	
	public static Shader BG, BIRD, PIPE, FADE;
	
	private boolean enabled = false;
	
	private final int ID;
	
	private Map<String, Integer> locationCache = new HashMap<String, Integer>();
	
	public Shader(String vertex, String fragment) {
		ID = ShaderUtils.load(vertex, fragment);
	}
	
	public static void LoadAll() {
		BG = new Shader("shaders/bg.vert", "shaders/bg.frag");
		BIRD = new Shader("shaders/bird.vert", "shaders/bird.frag");
		PIPE = new Shader("shaders/pipe.vert", "shaders/pipe.frag");
		FADE = new Shader("shaders/fade.vert", "shaders/fade.frag");
	}
	
	public int getUniform(String name) {
		if(locationCache.containsKey(name)) {
			return locationCache.get(name);
		}
		
		int result = glGetUniformLocation(ID, name);
		
		if(result == -1) {
			System.err.println("Could not find uniform variable '" + name + "'!");
		} else {
			locationCache.put(name, result);
		}
		
		return result;
	}
	
	public void setUniform1i(String name, int v0) {
		if(!enabled) enable();
		glUniform1i(getUniform(name), v0);
	}
	
	public void setUniform1f(String name, float v0) {
		if(!enabled) enable();
		glUniform1f(getUniform(name), v0);
	}
	
	public void setUniform2f(String name, float v0, float v1) {
		if(!enabled) enable();
		glUniform2f(getUniform(name), v0, v1);
	}
	
	public void setUniform3f(String name, float v0, float v1, float v2) {
		if(!enabled) enable();
		glUniform3f(getUniform(name), v0, v1, v2);
	}
	
	public void setUniform3f(String name, Vector3f vector) {
		if(!enabled) enable();
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}
	
	public void setUniformMat4f(String name, Matrix4f matrix) {
		if(!enabled) enable();
		FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(new float[16]);
		matrix.get(matrixBuffer);
		glUniformMatrix4fv(getUniform(name), false, matrixBuffer);
	}

	public void enable() {
		glUseProgram(ID);
		enabled = true;
	}
	
	public void disable() {
		glUseProgram(0);
		enabled = false;
	}
	
}
