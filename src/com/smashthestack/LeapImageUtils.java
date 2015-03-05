package com.smashthestack;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import com.leapmotion.leap.ImageList;


class LeapImageUtils {
	private Controller leapController = null;
	
	public LeapImageUtils(){
		
	}
	
	public void initLeap(){
		this.leapController = new Controller();
		this.leapController.setPolicy(PolicyFlag.POLICY_IMAGES);
		
		/* Wait for some valid frames to come through */
		while(!this.leapController.frame().isValid()){
			System.out.println("initializing...");
		}
	}
	
	public static Mat convertToMat(Image image){
		Mat convertedImage;
		Mat denoisedImage;
		
		convertedImage = new Mat(image.height(), image.width(), CvType.CV_8UC1);
		denoisedImage = new Mat();
		
		convertedImage.put(0, 0, image.data());
		Photo.fastNlMeansDenoising(convertedImage, denoisedImage, 1, 7, 3);

		return denoisedImage;
	}
	
	private void isLeapInitialised() throws InvalidObjectException{
		if(this.leapController == null){
			throw new InvalidObjectException("Leap motion has not been initialised");			
		}
	}
}
