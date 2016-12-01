import java.io.*;
import java.util.Scanner;
import java.io.PrintWriter;
public class ImageEditor {

	public static void main(String[] args) {

		// TODO Auto-generated method stub

		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(args[0]));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		scanner.useDelimiter("(\\s+)(#[^\\n]*\\n)?(\\s*)|(#[^\\n]*\\n)(\\s*)");
		//p3
		scanner.next();
		//width then height
		int width = scanner.nextInt();
		int height = scanner.nextInt();
		//always 255
		scanner.next();
		int row = 0;
		int col = 0;
		//int blur = Integer.parseInt(args[3]);
		Image image = new Image(height,width);
		while (scanner.hasNext()) {
			// String line = scanner.nextLine();
			Pixel pixel = new Pixel();
			String token = null;
			if (scanner.hasNext()) {
				for (int i = 0; i < 3; i++) {
					switch (i) {
					case 0:
						token = scanner.next();
						pixel.setRed(Integer.parseInt(token));
						break;
					case 1:
						token = scanner.next();
						pixel.setGreen(Integer.parseInt(token));
						break;
					case 2:
						token = scanner.next();
						pixel.setBlue(Integer.parseInt(token));
						break;
					default:
						break;
					}
				}
				image.setPixel(row, col, pixel);
				col++;
				if (col == width) {
					col = 0;
					row++;
				}
			}
		}
//Edit Image
		switch(args[2]){
		case "invert":
			image.invert(width,height);
			break;
		case "grayscale":
			image.grayscale(width,height);
			break;
		case "emboss":
			image.emboss(width,height);
			break;
			
		case "motionblur":
			int blur = Integer.parseInt(args[3]);
			image.motionblur(blur,width,height);
			break;
		default:
			break;
		}

//Print
		try {
			PrintWriter pw= new PrintWriter(new BufferedWriter(new FileWriter(args[1])));
			pw.println("P3");
			pw.print(width);
			pw.print(" ");
			pw.println(height);
			pw.println("255");
			for(int rowIT=0;rowIT<height;rowIT++){
				for(int column=0;column<width;column++){
					Pixel pixel = image.getPixel(rowIT,column);
					pw.println(pixel.getRed());
					pw.println(pixel.getGreen());
					pw.println(pixel.getBlue());
				}	
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
