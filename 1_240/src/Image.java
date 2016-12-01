
public class Image {

	Pixel[][] image;

	public Image(int height, int width) {
		image = new Pixel[height][width];
	}

	public void setPixel(int height, int width, Pixel pixel) {
		image[height][width] = pixel;
	}

	public Pixel getPixel(int height, int width) {
		return image[height][width];
	}

	public void invert(int width, int height){
		for(int rowIT=0;rowIT<height;rowIT++){
			for(int column=0;column<width;column++){
				image[rowIT][column].invertPixel();
			}
		}
		System.out.println("invert");
	}
	
	public void grayscale(int width, int height){
		for(int rowIT=0;rowIT<height;rowIT++){
			for(int column=0;column<width;column++){
				image[rowIT][column].grayscalePixel();
			}
		}
		System.out.println("grayscale");
	}
	
	
	
	public void emboss(int width, int height){
		Pixel[][] newImage = new Pixel[height][width];
//Make copy of image		
		for(int rowIT=0;rowIT<height;rowIT++){
			for(int column=0;column<width;column++){
			Pixel newpixel = new Pixel();
			newpixel.setRed(image[rowIT][column].getRed());
			newpixel.setBlue(image[rowIT][column].getBlue());
			newpixel.setGreen(image[rowIT][column].getGreen());
			newImage[rowIT][column]=newpixel;
			}
		}
// do the emboss	
		for(int rowIT=0;rowIT<height;rowIT++){
			for(int column=0;column<width;column++){
				if(rowIT == 0|column==0){
					newImage[rowIT][column].setRed(128);
					newImage[rowIT][column].setGreen(128);
					newImage[rowIT][column].setBlue(128);
				}
				else{
				newImage[rowIT][column].emboss(image[rowIT-1][column-1].getRed(),image[rowIT-1][column-1].getGreen(),image[rowIT-1][column-1].getBlue());
				}
			}
		}
// copy new image to the original		
		for(int rowIT=0;rowIT<height;rowIT++){
			for(int column=0;column<width;column++){
				Pixel newpixel = new Pixel();
				newpixel.setRed(newImage[rowIT][column].getRed());
				newpixel.setBlue(newImage[rowIT][column].getBlue());
				newpixel.setGreen(newImage[rowIT][column].getGreen());
				image[rowIT][column]=newpixel;
			}
		}
		System.out.println("emboss");
	}
	
	
	
	
	public void motionblur(int blur,int width, int height){
		Pixel[][] newImage = new Pixel[height][width];
//Make copy of image		
		for(int rowIT=0;rowIT<height;rowIT++){
			for(int column=0;column<width;column++){
			Pixel newpixel = new Pixel();
			newpixel.setRed(image[rowIT][column].getRed());
			newpixel.setBlue(image[rowIT][column].getBlue());
			newpixel.setGreen(image[rowIT][column].getGreen());
			newImage[rowIT][column]=newpixel;
			}
		}
		//motion blur
	int blurwidth = blur;
	for(int rowIT=0;rowIT<height;rowIT++){
		for(int column=0;column<width;column++){
			if(width-(blur+column)<0){
				blurwidth=width-column;
			}
			else{
				blurwidth=blur;
			}
			Pixel[] blurPixels = new Pixel[blurwidth];
			for(int i=0;i<blurwidth;i++){
				Pixel newpixel = new Pixel();
				newpixel.setRed(image[rowIT][column+i].getRed());
				newpixel.setBlue(image[rowIT][column+i].getBlue());
				newpixel.setGreen(image[rowIT][column+i].getGreen());
				blurPixels[i]=newpixel;
			}	
			newImage[rowIT][column].blurPixel(blurPixels);
		}
	}
		// copy new image to the original		
				for(int rowIT=0;rowIT<height;rowIT++){
					for(int column=0;column<width;column++){
						Pixel newpixel = new Pixel();
						newpixel.setRed(newImage[rowIT][column].getRed());
						newpixel.setBlue(newImage[rowIT][column].getBlue());
						newpixel.setGreen(newImage[rowIT][column].getGreen());
						image[rowIT][column]=newpixel;
					}
				}

		System.out.println("motionblur");
	}
	
}
