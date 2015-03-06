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

public class LeapCVController {

	private ImageList currentImages = null;

	private Controller leapController;
	private LeapCVCamera leftCam;
	private LeapCVCamera rightCam;
	
	public LeapCVController() {
		this.leapController = new Controller();
		this.initLeap();
		this.initDistortionMats();
	}

	private void initLeap() {
		this.leapController.setPolicy(PolicyFlag.POLICY_IMAGES);
		this.leftCam = new LeapCVCamera(LeapCVCamera.CameraSide.LEFT);
		this.rightCam = new LeapCVCamera(LeapCVCamera.CameraSide.RIGHT);
		
		this.moveToNextValidFrame();
	}
	
	private void initDistortionMats() {
		// Initialise distortion mats for fast image remap
		Map<String, Mat> mats = LeapImageUtils.initDistortionMat(this.leftCam.getCurrentImage().getImageAsLeap());
		this.leftCam.setDistortionX(mats.get(LeapImageUtils.X_MAT_KEY));
		this.leftCam.setDistortionY(mats.get(LeapImageUtils.Y_MAT_KEY));

		mats = LeapImageUtils.initDistortionMat(this.rightCam.getCurrentImage().getImageAsLeap());
		this.rightCam.setDistortionX(mats.get(LeapImageUtils.X_MAT_KEY));
		this.rightCam.setDistortionY(mats.get(LeapImageUtils.Y_MAT_KEY));
	}

	public void moveToNextValidFrame() {
		this.currentImages = this.leapController.images();
		while (!this.currentImages.get(0).isValid()) {
			this.currentImages = this.leapController.images();
		}
		
		for(int i = 0; i < this.currentImages.count(); ++i){
			switch(this.currentImages.get(i).id()){
			case LeapCVCamera.LEFT_ID:
				this.leftCam.setCurrentImage(new LeapCVImage(this.currentImages.get(i)));
				break;
			case LeapCVCamera.RIGHT_ID:
				this.rightCam.setCurrentImage(new LeapCVImage(this.currentImages.get(i)));
				break;
			}
		}
	}
	
	public Mat getLeftImage(){
		return this.leftCam.getCurrentImage().getImageAsMat();
	}
	
	public Mat getRightImage(){
		return this.rightCam.getCurrentImage().getImageAsMat();
	}
	
	public Mat getLeftImageUndistorted(){
		return this.leftCam.getImageUndistorted();
	}

	public Mat getRightImageUndistorted(){
		return this.rightCam.getImageUndistorted();
	}

}
