package com.leapcv;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.leapmotion.leap.Image;

public class LeapCVCamera {

	public enum CameraSide {
		LEFT(0), RIGHT(1);

		private int side;

		CameraSide(int side) {
			this.side = side;
		}

		public int getSideId() {
			return this.side;
		}
	}

	protected static final int LEFT_ID = 0;
	protected static final int RIGHT_ID = 1;

	private Mat distortionX = null;
	private Mat distortionY = null;

	private CameraSide side = null;

	private LeapCVImage currentImage = null;

	public LeapCVCamera(CameraSide side) {
		this.side = side;
	}

	public Mat getDistortionX() {
		return distortionX;
	}

	public void setDistortionX(Mat distortionX) {
		this.distortionX = distortionX;
	}

	public Mat getDistortionY() {
		return distortionY;
	}

	public void setDistortionY(Mat distortionY) {
		this.distortionY = distortionY;
	}

	public CameraSide getSide() {
		return side;
	}

	public void setSide(CameraSide side) {
		this.side = side;
	}

	public LeapCVImage getCurrentImage() {
		return currentImage;
	}

	public void setCurrentImage(Image image) {
		if(this.currentImage == null){
			this.currentImage = new LeapCVImage(image);
		} else {
			this.currentImage.setImage(image);
		}
	}

	 public Mat getImageUndistorted() {
		 Mat processedImage = new Mat();
         int width;
         int height;
         int x;
         int y;

         Imgproc.remap(this.currentImage.getImageAsMat(), processedImage, this.distortionX,
				 this.distortionY, Imgproc.INTER_LINEAR);

         width = processedImage.cols();
         height = processedImage.rows();

         System.out.println("width: " + width + " height: " + height);

         x = (int)(width*0.3);
         y = (int)(height*0.3);
         width = width - x*2;
         height = height - y*2;

         System.out.println(" x: " + x + " y: " + y + " width: " + width + " height: " + height);

         Rect roi = new Rect(x, y, width, height);
         processedImage = processedImage.submat(roi);

		 Imgproc.resize(processedImage, processedImage, new Size(240, 240));
		 //Imgproc.medianBlur(processedImage, processedImage, 3);
		 
		 // Imgproc.getRectSubPix(processedImage, new Size(320, 120), center,
		 // resizedImage);
		 return processedImage;
	 }

}
