
public class Pixel {

	private int green;
	private int red;
	private int blue;
	
	public Pixel(){
		green = 0;
		red = 0;
		blue = 0;
	}
		
	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}
	
	public void invertPixel(){
		red = 255 - red;
		green = 255 - green;
		blue = 255 - blue;
	}
	
	public void grayscalePixel(){
		int grayscalePixel = (red + green+ blue)/3;
		red = grayscalePixel;
		green = grayscalePixel;
		blue = grayscalePixel;
	}
	
	public void emboss(int redV,int greenV,int blueV){
		int v=128;
		int max=0;
		int[]  differenceValues = new int[3];
		differenceValues[0]= red-redV;
		differenceValues[1]= green-greenV;
		differenceValues[2]= blue-blueV;
		max=0;
		for(int i = 0;i<differenceValues.length;i++){
			if(Math.abs(differenceValues[i])>Math.abs(max)){
				max=differenceValues[i];
			}
		}
		v=v+max;
		if(v>255){
			v=255;
		}
		if(v<0){
			v=0;
		}
		
		red = v;
		green = v;
		blue = v;
	}
	
	public void blurPixel(Pixel[] blurPixels){
		int redAVG=0;
		int greenAVG=0;
		int blueAVG=0;
		for(int i = 0; i<blurPixels.length;i++){
			redAVG=blurPixels[i].getRed()+redAVG;
			greenAVG=blurPixels[i].getGreen()+greenAVG;
			blueAVG=blurPixels[i].getBlue()+blueAVG;
		}
		redAVG=redAVG/blurPixels.length;
		greenAVG=greenAVG/blurPixels.length;
		blueAVG=blueAVG/blurPixels.length;
		red = redAVG;
		green=greenAVG;
		blue=blueAVG;	
	}
}
