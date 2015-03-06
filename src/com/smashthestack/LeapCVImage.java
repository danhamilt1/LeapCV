package com.smashthestack;

import org.opencv.core.Mat;

import com.leapmotion.leap.Image;

public class LeapCVImage extends com.leapmotion.leap.Image{

	private Mat imageAsMat;
	private Image imageAsLeap;
	
	public LeapCVImage(Image image) {
		this.imageAsLeap = image;
		this.imageAsMat = LeapImageUtils.convertToMat(image);
	}

	public Mat getImageAsMat() {
		return imageAsMat;
	}

	public void setImageAsMat(Mat imageAsMat) {
		this.imageAsMat = imageAsMat;
	}

	public Image getImageAsLeap() {
		return imageAsLeap;
	}

	public void setImageAsLeap(Image imageAsLeap) {
		this.imageAsLeap = imageAsLeap;
	}
}
