package company.ouroboros.flappy;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import company.ouroboros.flappy.graphics.Shader;
import company.ouroboros.flappy.input.Input;
import company.ouroboros.flappy.level.Level;
import company.ouroboros.flappy.utils.BufferUtils;

public class Main implements Runnable{
	
	private int width = 1920;
	private int height = 1080;
	
	private Thread thread;
	private boolean running = false;
	
	private long window;
	private Level level;
	
	public void start() {
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}
	
	public void init() {
		if(!glfwInit()) {
			// TODO: handle
			return;
		}
		
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		
		window = glfwCreateWindow(width, height, "Flappy", NULL, NULL);
		
		if(window == NULL) {
			//TODO: handle
			return;
		}
		
		//window Icon
		
		//load image from file
		int[] RGB = null;
		int iconWidth = 0, iconHeight = 0;
		try {
			BufferedImage image = ImageIO.read(new FileInputStream("res/bird.png"));
			iconWidth = image.getWidth();
			iconHeight = image.getHeight();
			RGB = new int[iconWidth * iconHeight];
			image.getRGB(0, 0, iconWidth, iconHeight, RGB, 0, iconWidth);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//convert to bgr
		int[] BGR = new int[iconWidth * iconHeight];
		for (int i = 0; i < iconWidth * iconHeight; i++) {
			int a = (RGB[i]& 0xff000000) >> 24;
			int r = (RGB[i]& 0xff0000) >> 16;
			int g = (RGB[i]& 0xff00) >> 8;
			int b = (RGB[i]& 0xff) >> 0;
			
			BGR[i] = a << 24 | b << 16 | g << 8 | r << 0; 
		}
		
		//create buffers and set image
		GLFWImage.Buffer buffer = GLFWImage.malloc(1);
		GLFWImage icon = GLFWImage.malloc();
	    icon.set(iconWidth, iconHeight, BufferUtils.createByteBuffer(BGR));
	    buffer.put(icon);
	    buffer.position(0);
	    glfwSetWindowIcon(window, buffer);
	    buffer.free();

		//Center window
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
		
		glfwSetKeyCallback(window, new Input());
		
		//show window
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GL.createCapabilities();
		
		//opengl
		glEnable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE1);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		
		Shader.LoadAll();
		
		Matrix4f pr_matrix = new Matrix4f().ortho(-10.0f, 10.0f, -10.0f * 9.0f /16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
		Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BG.setUniform1i("tex", 1);
		
		Shader.BIRD.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BIRD.setUniform1i("tex", 1);
		
		Shader.PIPE.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.PIPE.setUniform1i("tex", 1);
		
		level = new Level();
	}
	
	public void run() {
		init();
		
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double delta = 0;
		double ns = 100000000.0 / 60.0;
		int updates = 0;
		int frames = 0;

		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1.0) {
				update();
				updates++;
				delta--;
			}
			render();
			frames ++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				
				System.out.println(updates + " ups, " + frames + " fps");
				
				updates = 0;
				frames = 0;
			}
			
			if(glfwWindowShouldClose(window)) {
				running = false;
			}
		}
		
		glfwDestroyWindow(window);
		glfwTerminate();
	}
	
	public void update() {
		glfwPollEvents();

		level.update();
		if(level.isGameOver()) {
			level = new Level();
		}
	}
	
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		level.render();

		int error = glGetError();
		if(error != GL_NO_ERROR) {
			System.out.println(error);
		}
		
		glfwSwapBuffers(window);
	}

	public static void main(String[] args) {
		new Main().start();
	}


}
