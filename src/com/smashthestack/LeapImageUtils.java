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
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import com.leapmotion.leap.Image;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Leap;
import com.leapmotion.leap.Vector;
import com.leapmotion.leap.Controller.PolicyFlag;
import com.leapmotion.leap.ImageList;


class LeapImageUtils {
	public static final int IMAGE_WIDTH = 640;
	public static final int IMAGE_HEIGHT = 240;
	public static final String LEFT_IMAGE_KEY = "LEFT_IMAGE";
	public static final String RIGHT_IMAGE_KEY = "RIGHT_IMAGE";
	public static final String X_MAT_KEY = "X";
	public static final String Y_MAT_KEY = "Y";
	
	private Controller leapController = null;
	
	public LeapImageUtils(){
		
	}
	
	/**
	 * Initialise leap motion controller, waits for the first valid frame
	 */
	public void initLeap(){
		this.leapController = new Controller();
		this.leapController.setPolicy(PolicyFlag.POLICY_IMAGES);
		
		/* Wait for some valid frames to come through */
		while(!this.leapController.frame().isValid()){
			System.out.println("initializing...");
		}
	}
	
	/**
	 * Convert a leap type {@link Image} to an OpenCV {@link Mat}
	 * @param image of type {@link Image}
	 * @return {@link Mat} of original image
	 */
	public static Mat convertToMat(Image image){
		Mat convertedImage;
		Mat denoisedImage;
		
		convertedImage = new Mat(image.height(), image.width(), CvType.CV_8UC1);
		denoisedImage = new Mat();
		
		convertedImage.put(0, 0, image.data());
		Photo.fastNlMeansDenoising(convertedImage, denoisedImage, 1, 7, 3);

		return denoisedImage;
	}
	
	/**
	 * Initialise the distortion matrices for use with OpenCV {@link Imgproc.remap} method.
	 * @param image
	 * @return {@code Map<String,Mat>} of X and Y matrix.
	 */
	public static Map<String,Mat> initDistortionMat(Image image){
		
		Mat tempX = new Mat();
		Mat tempY = new Mat();
		
		tempX = Mat.ones(image.height(),image.width(),CvType.CV_32F);
		tempY = Mat.ones(image.height(),image.width(),CvType.CV_32F);
		
		for(int y = 0; y < image.height(); ++y){
			for(int x = 0; x < image.width();++x){
				Vector input = new Vector((float)x/image.width(), (float)y/image.height(), 0);
				input.setX((input.getX() - image.rayOffsetX()) / image.rayScaleX());
		        input.setY((input.getY() - image.rayOffsetY()) / image.rayScaleY());
		        Vector pixel = image.warp(input);
		        if(pixel.getX() >= 0 && pixel.getX() < image.width() && pixel.getY() >= 0 && pixel.getY() < image.height()) {
		        	tempX.put(y, x, pixel.getX());
		        	tempY.put(y, x, pixel.getY());
		        } else {
		        	tempX.put(y, x, -1);
		        	tempY.put(y, x, -1);
		        }
		        
			}
		}
		
		Map<String, Mat> retVal = new HashMap<>();
		retVal.put(X_MAT_KEY, tempX);
		retVal.put(Y_MAT_KEY, tempY);
		
		return retVal;
	}
	
	private void isLeapInitialised() throws InvalidObjectException{
		if(this.leapController == null){
			throw new InvalidObjectException("Leap motion has not been initialised");			
		}
	}
}
