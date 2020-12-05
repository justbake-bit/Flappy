package company.ouroboros.flappy.level;

import static org.lwjgl.glfw.GLFW.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import company.ouroboros.flappy.graphics.Shader;
import company.ouroboros.flappy.graphics.Texture;
import company.ouroboros.flappy.graphics.VertexArray;
import company.ouroboros.flappy.input.Input;

public class Bird {
	
	private float SIZE = 1.0f;
	private VertexArray mesh;
	private Texture texture;
	
	private Vector3f position = new Vector3f();
	private float rot;
	private float delta = 0.0f;
	
	public Bird() {
		float[] vertices = new float[] {
			-SIZE / 2.0f, -SIZE / 2.0f, 0.2f,
			-SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
			 SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
			 SIZE / 2.0f, -SIZE / 2.0f, 0.2f,
		};
		
		byte[] indices = new byte[] {
			0, 1, 2,
			2, 3, 0
		};
		
		float[] tcs = new float[] {
			0, 1,
			0, 0,
			1, 0,
			1, 1
		};
		
		mesh = new VertexArray(vertices, indices, tcs);
		texture = new Texture("res/bird.png");
	}
	
	public void update() {
		position.y -= delta;
		if(Input.isKeyDown(GLFW_KEY_SPACE)) {
			delta = -0.10f;
		} else {
			delta += 0.01;
		}
		
		rot = -delta;
	}
	
	public void fall() {
		delta = -0.15f;
	}
	
	public void render() {
		Shader.BIRD.enable();
		Shader.BIRD.setUniformMat4f("ml_matrix", new Matrix4f().translate(position).rotate(rot, new Vector3f(0.0f, 0.0f, 2.0f)));
		texture.bind();
		mesh.render();
		Shader.BIRD.disable();
	}

	public float getY() {
		return position.y;
	}
	
	public float getSize() {
		return SIZE;
	}
	
}
