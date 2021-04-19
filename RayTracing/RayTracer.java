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
	List<Surfaces> surfaces = new ArrayList<>();
	List<Materials> materials = new ArrayList<>();
	List<Light> lights = new ArrayList<>();
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
		double pixelSize = camera.getScreenWidth() / imageWidth;
		Vector screenCenter = camera.getPosition().addVectors(camera.getTowardsVector()
				.multByScalar(camera.getScreenDistance())); // Center = position + towards * distance

		// calc first pixel
		double aspectRatio = imageHeight / imageWidth;
		Vector delta_x = camera.getRightVector().multByScalar(pixelSize);
		Vector delta_y = camera.getUpVector().multByScalar(pixelSize);

		Vector center = camera.getTowardsVector().multByScalar(camera.getScreenDistance()).addVectors(camera.getPosition());
		Vector left = camera.getRightVector().multByScalar(-0.5 * camera.getScreenWidth());
		Vector top = camera.getUpVector().multByScalar(0.5 * aspectRatio * camera.getScreenWidth());
		Vector topLeft = left.addVectors(top.addVectors(center));

		Vector firstPixel = topLeft.addVectors(delta_y.multByScalar(-0.5));
		firstPixel = firstPixel.addVectors(delta_x.multByScalar(0.5));
//		Vector firstPixel = new Vector(-0.5, 8.591141964605509, -1.525532962844443);
		Color background = set.getBackgroundColor();

		for (int i = 0; i < imageWidth; i++) {
			Color color = new Color(0, 0, 0);
			for (int j = 0; j < imageHeight; j++) {
				// Construct ray through pixel
				Vector move = delta_y.multByScalar(-j).addVectors(delta_x.multByScalar(i));
				Vector currentPixel = firstPixel.addVectors(move);
				Vector directionVector = currentPixel.subVectors(camera.getPosition()).normalizeVector();
				Ray ray = new Ray(camera.getPosition(), directionVector);

				// Find intersection
				Intersection intersection = Intersection.findIntersection(ray, scene);
				// Color
				if (intersection.getMinT() == Double.MAX_VALUE) {
					// no intersection - need background color
					color.setRed(background.getRed());
					color.setGreen(background.getGreen());
					color.setBlue(background.getBlue());
				} else {
					// has intersection
					Color tmpColor = ColorUtils.calcColor(intersection, ray, scene,
							scene.getSet().getMaxRecursionLevel());

					color.setRed(tmpColor.getRed());
					color.setGreen(tmpColor.getGreen());
					color.setBlue(tmpColor.getBlue());
//
//					System.out.println(11111);
//					System.out.println(color.getRed()+ "    " + color.getGreen()  +"   "+ color.getBlue());
				}

				rgbData[(i * this.imageWidth + j) * 3] = (byte) (color.getRed() * 255);
				rgbData[(i * this.imageWidth + j) * 3 + 1] = (byte) (color.getGreen() * 255);
				rgbData[(i * this.imageWidth + j) * 3 + 2] = (byte) (color.getBlue() * 255);

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
