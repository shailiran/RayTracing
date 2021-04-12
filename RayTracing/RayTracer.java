package RayTracing;

import src.*;

import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {

	public int imageWidth;
	public int imageHeight;
	Camera camera;
	Set set;
	List<Surfaces> surfaces;
	List<Materials> materials;
	List<Light> lights;
	Scene scene;

	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size as input.
	 */
	public static void main(String[] args) {
		try {
			RayTracer tracer = new RayTracer();
                        // Default values:
			tracer.imageWidth = 500;
			tracer.imageHeight = 500;

			if (args.length < 2)
				throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

			String sceneFileName = args[0];
			String outputFileName = args[1];

			if (args.length > 3) {
				tracer.imageWidth = Integer.parseInt(args[2]);
				tracer.imageHeight = Integer.parseInt(args[3]);
			}

			// Parse scene file:
			tracer.parseScene(sceneFileName);

			// Render scene:
			tracer.renderScene(outputFileName);

//		} catch (IOException e) {
//			System.out.println(e.getMessage());
		} catch (RayTracerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Parses the scene file and creates the scene. Change this function so it generates the required objects.
	 */
	public void parseScene(String sceneFileName) throws IOException, RayTracerException {
		FileReader fr = new FileReader(sceneFileName);

		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		System.out.println("Started parsing scene file " + sceneFileName);

		while ((line = r.readLine()) != null) {
			line = line.trim();
			++lineNum;
			if (line.isEmpty() || (line.charAt(0) == '#')) {
				// This line in the scene file is a comment
				continue;
			} 
			else {
				String code = line.substring(0, 3).toLowerCase();
				// Split according to white space characters:
				String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

				if (code.equals("cam")) {
					camera = new Camera(params);
					System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
				}
				else if (code.equals("set")) {
					set = new Set(params);
					System.out.println(String.format("Parsed general settings (line %d)", lineNum));
				}
				else if (code.equals("mtl")) {
					Materials material = new Materials(params);
					materials.add(material);
					System.out.println(String.format("Parsed material (line %d)", lineNum));
				}
				else if (code.equals("sph")) {
					Sphere sphere = new Sphere(params);
					surfaces.add(sphere);
					System.out.println(String.format("Parsed sphere (line %d)", lineNum));
				}
				else if (code.equals("pln")) {
					Plane plane = new Plane(params);
					surfaces.add(plane);
					System.out.println(String.format("Parsed plane (line %d)", lineNum));
				}
				else if(code.equals("box")) {
					Box box = new Box(params);
					surfaces.add(box);
					System.out.println(String.format("Parsed Box (line %d)", lineNum));
				}
				else if (code.equals("lgt")) {
					Light light = new Light(params);
					lights.add(light);
					System.out.println(String.format("Parsed light (line %d)", lineNum));
				}
				else
				{
					System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
				}
			}
		}
		scene = new Scene(camera, set, surfaces, materials, lights);
		if (!scene.isValid()) {
			System.out.println("The scene is not valid!");
		}
		System.out.println("Finished parsing scene file " + sceneFileName);
	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(String outputFileName) {
		long startTime = System.currentTimeMillis();

		// Create a byte array to hold the pixel data:
		byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];

		//credit: https://en.wikipedia.org/wiki/Ray_tracing_(graphics)
		double pixel = camera.getScreenWidth() / imageWidth;
		Vector screenCenter = camera.getPosition().addVectors(camera.getTowardsVector())
				.multByScalar(camera.getScreenDistance()); // Center = position + towards * distance
		
		Vector tmp1 = camera.getRightVector().multByScalar(camera.getScreenWidth() - pixel); // (width - pixel size) * right vector
		Vector tmp2 = camera.getUpVector().multByScalar(imageHeight - pixel); // (height - pixel size) * Up Vector
		Vector tmp = (tmp1.addVectors(tmp2)).multByScalar(0.5); // tmp = (tmp1 + tmp2) * 0.5
		Vector p_0 = screenCenter.subVectors(tmp); // The left top pixel

		// double g_y = 0.5 * (imageHeight - pixel);
		// double g_x = 0.5 * (imageWidth - pixel);
		// double m = imageHeight / pixel;
		// double k = imageWidth / pixel;
		// double aspectRatio = (m - 1) / (k - 1);
		// Vector q_x = camera.getRightVector().multByScalar((2 * g_x) / (k-1));
		// Vector q_y = camera.getUpVector().multByScalar((2 * g_y) / m - 1);
		// Vector p_11 = (camera.getTowardsVector().multByScalar(camera.getScreenDistance()))
		// .addVectors((camera.getRightVector().multByScalar(g_x)))
		// 		.subVectors (camera.getUpVector().multByScalar(g_y)); //p_11 = towards * distance + g_x * right - g_y * Up

		Vector delta_x = camera.getRightVector().multByScalar(pixel);
		Vector delta_y = camera.getUpVector().multByScalar(pixel);

		for (int i = 0; i < imageWidth; i++) {
			for (int j = 0; j < imageHeight; j++) {
				// Vector p_ij = p_11.addVectors(q_x.multByScalar(i)).subVectors(q_y.multByScalar(j));

				// Construct ray through pixel
				Vector move = delta_y.multByScalar(i).addVectors(delta_x.multByScalar(j));
				Vector currentPixel = p_0.addVectors(move); 
				Vector directionVector = currentPixel.subVectors(camera.getPosition());
				Ray ray = new Ray(currentPixel, directionVector);

				// Find intersection
				Intersection hit = Intersection.findIntersection(ray, scene);
				
				// Color
				Color color;
				if (!hit.getHasIntersection()) {
					// no intersection - need background color
					color = set.getBackgroundColor();
				} else {
					// has intersection
					
				}



			}
		}

		// Put your ray tracing code here!
		//
		// Write pixel color values in RGB format to rgbData:
		// Pixel [x, y] red component is in rgbData[(y * this.imageWidth + x) * 3]
		//            green component is in rgbData[(y * this.imageWidth + x) * 3 + 1]
		//             blue component is in rgbData[(y * this.imageWidth + x) * 3 + 2]
		//
		// Each of the red, green and blue components should be a byte, i.e. 0-255


		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;

		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

    // This is already implemented, and should work without adding any code.
		saveImage(this.imageWidth, rgbData, outputFileName);

		System.out.println("Saved file " + outputFileName);

	}

	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

	/*
	 * Saves RGB data as an image in png format to the specified location.
	 */
	public static void saveImage(int width, byte[] rgbData, String fileName) {
		try {
			BufferedImage image = bytes2RGB(width, rgbData);
			ImageIO.write(image, "png", new File(fileName));
		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}
	}

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
	 */
	public static BufferedImage bytes2RGB(int width, byte[] buffer) {
	    int height = buffer.length / width / 3;
	    ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	    ColorModel cm = new ComponentColorModel(cs, false, false,
	            Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    SampleModel sm = cm.createCompatibleSampleModel(width, height);
	    DataBufferByte db = new DataBufferByte(buffer, width * height);
	    WritableRaster raster = Raster.createWritableRaster(sm, db, null);
	    BufferedImage result = new BufferedImage(cm, raster, false, null);

	    return result;
	}

	public static class RayTracerException extends Exception {
		public RayTracerException(String msg) {  super(msg); }
	}

}
