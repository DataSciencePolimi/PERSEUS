package perseus.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.util.Base64;

import perseus.Application;

public class ImageFinder {
	
	public static void findNarrativeImage(String id_narrative, Blob blob) {
		
		try {
			Path path = Paths.get(Application.IMG_SC);

			if (!Files.exists(path)) {
				try {
					Files.createDirectories(path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
			
			File image = new File(Application.IMG_SC + id_narrative + ".png");
			boolean exists = image.exists();
			
			if(!exists) {
				byte[] decodedImg = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(blob.getBytes(1, (int) blob.length())));
				Path destinationFile = Paths.get(Application.IMG_SC, id_narrative + ".png");
				try {
					Files.write(destinationFile, decodedImg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
