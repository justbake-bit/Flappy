package company.ouroboros.flappy.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import company.ouroboros.flappy.utils.BufferUtils;

public class Texture {
	
	private int width, height;
	private int texture;

	public Texture(String path) {
		texture = load(path);
	}
	
	private int load(String path) {
		int[] RGB = null;
		try {
			BufferedImage image = ImageIO.read(new FileInputStream(path));
			width = image.getWidth();
			height = image.getHeight();
			RGB = new int[width * height];
			image.getRGB(0, 0, width, height, RGB, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int[] BGR = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			int a = (RGB[i]& 0xff000000) >> 24;
			int r = (RGB[i]& 0xff0000) >> 16;
			int g = (RGB[i]& 0xff00) >> 8;
			int b = (RGB[i]& 0xff) >> 0;
			
			BGR[i] = a << 24 | b << 16 | g << 8 | r << 0; 
		}
		
		int result = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, result);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(BGR));
		
		glBindTexture(GL_TEXTURE_2D, 0);
		
		return result;
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texture);
	}
	
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
