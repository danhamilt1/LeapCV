package com.smashthestack;

import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.Map;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.leapmotion.leap.Controller.PolicyFlag;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Image;
import com.leapmotion.leap.ImageList;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Vector;

public class LeapImages {

	protected static final int IMAGE_WIDTH = 640;
	protected static final int IMAGE_HEIGHT = 240;
	protected static final int LEFT_IMAGE_ID = 0;
	protected static final int RIGHT_IMAGE_ID = 1;
	public static final String LEFT_IMAGE_KEY = "LEFT_IMAGE";
	public static final String RIGHT_IMAGE_KEY = "RIGHT_IMAGE";

	private ImageList currentImages = null;
	
	private Mat distortionMatXLeft = null;
	private Mat distortionMatYLeft = null;
	private Mat distortionMatXRight = null;
	private Mat distortionMatYRight = null;

	private Controller leapController;

	public LeapImages() {
		// TODO Auto-generated constructor stub
		this.leapController = new Controller();
		this.leapController.setPolicy(PolicyFlag.POLICY_IMAGES);
		this.moveToNextValidFrame();
		//this.distortionMat = new MatOfFloat();
		//this.distortionMat.put(0, 0, this.currentImages.get(0).distortion());
		//System.out.println(this.distortionMat.dump());
	}

	public Map<String, Mat> getImages() {
		Map<String, Mat> images = new HashMap<>();
		// isLeapInitialised();

		if (this.currentImages == null) {
			this.moveToNextValidFrame();
		}

		for (int i = 0; i < this.currentImages.count(); ++i) {

			switch (this.currentImages.get(i).id()) {

			case LEFT_IMAGE_ID:
				images.put(LEFT_IMAGE_KEY,
						LeapImageUtils.convertToMat(this.currentImages.get(i)));
				break;

			case RIGHT_IMAGE_ID:
				images.put(RIGHT_IMAGE_KEY,
						LeapImageUtils.convertToMat(this.currentImages.get(i)));
				break;
			}

		}

		return images;
	}

	public Mat getLeftImage() {
		return LeapImageUtils.convertToMat(this
				.getImageById(LEFT_IMAGE_ID));
	}

	public Mat getRightImage() {
		return LeapImageUtils.convertToMat(this
				.getImageById(RIGHT_IMAGE_ID));
	}

	private Image getImageById(int id) {
		if(this.currentImages == null){
			this.moveToNextValidFrame();
		}
		for (Image image : this.currentImages) {
			if (image.id() == id) {
				return image;
			}
		}
		return null;
	}

	public void moveToNextValidFrame() {
		this.currentImages = this.leapController.images();
		while (!this.currentImages.get(0).isValid()) {
			this.currentImages = this.leapController.images();
		}
	}
	
	public void initDistortionMat(String imageString, int imageId){
		Image image = this.getImageById(imageId);
		
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
		        //System.out.println("X: " + input.getX() + " Y: " + input.getY());
		  //      System.out.println(pixel.getX() + "  " + pixel.getY());
		        if(pixel.getX() >= 0 && pixel.getX() < image.width() && pixel.getY() >= 0 && pixel.getY() < image.height()) {
		        	tempX.put(y, x, pixel.getX());
		        	tempY.put(y, x, pixel.getY());
		        } else {
		        	tempX.put(y, x, -1);
		        	tempY.put(y, x, -1);
		        }
		        
			}
		}
		//System.out.println(image.isValid());
		//System.out.println(tempX.dump());
		this.distortionMatXLeft = tempX;
		this.distortionMatYLeft = tempY;
	}
	
	public Mat getLeftImageUndistorted(){
		Mat image = this.getLeftImage();
		Mat processedImage = new Mat();
		Mat resizedImage = new Mat();
		Point center = new Point(IMAGE_WIDTH/2, IMAGE_HEIGHT/2);
		Imgproc.remap(image, processedImage, this.distortionMatXLeft, this.distortionMatYLeft, Imgproc.INTER_LINEAR);
		Imgproc.resize(processedImage, resizedImage, new Size(640,640));
		//Imgproc.medianBlur(resizedImage, resizedImage, 3);
		//Imgproc.getRectSubPix(processedImage, new Size(320, 120), center, resizedImage);
		return resizedImage;
	}

}
