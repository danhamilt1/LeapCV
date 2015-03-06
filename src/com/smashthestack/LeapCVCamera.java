package com.smashthestack;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

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

	public void setCurrentImage(LeapCVImage currentImage) {
		this.currentImage = currentImage;
	}

	 public Mat getImageUndistorted() {
		 Mat processedImage = new Mat();
		 Mat resizedImage = new Mat();
		 Point center = new Point(LeapImageUtils.IMAGE_WIDTH / 2,
		 LeapImageUtils.IMAGE_HEIGHT / 2);
		 Imgproc.remap(this.currentImage.getImageAsMat(), processedImage, this.distortionX,
				 this.distortionY, Imgproc.INTER_LINEAR);
		 Imgproc.resize(processedImage, resizedImage, new Size(200, 200));
		 // Imgproc.medianBlur(resizedImage, resizedImage, 3);
		 // Imgproc.getRectSubPix(processedImage, new Size(320, 120), center,
		 // resizedImage);
		 return resizedImage;
	 }

}
