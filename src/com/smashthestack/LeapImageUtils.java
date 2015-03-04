package com.smashthestack;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;

import com.leapmotion.leap.Controller;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.Highgui;
import org.opencv.photo.Photo;

import com.leapmotion.leap.Image;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Controller.PolicyFlag;


class LeapImageUtils {
	private final Controller leapController = new Controller();
	//private final LeapListener leapListener = new LeapListener();
	private static final int IMAGE_WIDTH = 640;
	private static final int IMAGE_HEIGHT = 240;
	
	public LeapImageUtils(){
		
	}
	
	public void initLeap(){
		//this.leapController = new Controller();
		//this.leapListener = new LeapListener();
		//this.leapController.addListener(this.leapListener);
		this.leapController.setPolicy(PolicyFlag.POLICY_IMAGES);
		
		while(!this.leapController.frame().isValid()){
			System.out.println("initializing...");
		}
	}
	
	public static void getImageUndistorted(){
		
	}
	
	public ArrayList<Mat> getImages(){
		ArrayList<Mat> images = new ArrayList<>();
		Frame frame = null;
		
		while(!this.leapController.frame().isValid() && this.leapController.frame().images().isEmpty()){
		}
		
		frame = this.leapController.frame();
		
		images.add(this.convertToMat(frame.images().get(0)));
		images.add(this.convertToMat(frame.images().get(1)));
		
		return images;
	}
	
	private Mat convertToMat(Image image){
		Mat convertedImage;
		Mat denoisedImage;
		
		convertedImage = new Mat(LeapImageUtils.IMAGE_HEIGHT, LeapImageUtils.IMAGE_WIDTH, CvType.CV_8UC1);
		denoisedImage = new Mat();		
		convertedImage.put(0, 0, image.data());
		Photo.fastNlMeansDenoising(convertedImage, denoisedImage, 1, 7, 3);

		return denoisedImage;
	}
	
}
