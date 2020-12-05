package company.ouroboros.flappy.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {
	
	private FileUtils() {
	}

	public static String loadAsString(String file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String buffer = "";
			
			while((buffer = reader.readLine()) != null) {
				result.append(buffer + "\n");
			}
			reader.close();
		} catch (IOException e) {
			/*try {
				if(file.contains("res/")) {
					file = file.substring(4);
				} else if (file.contains("shaders/")) {
					file = file.substring(7);
				}
				System.out.println(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(FileReader.class.getClassLoader().getResourceAsStream(file)));
				
				String buffer = "";
				
				while((buffer = reader.readLine()) != null) {
					result.append(buffer + "\n");
				}
				reader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}*/
			e.printStackTrace();
		}
		return result.toString();
	}
	
}
