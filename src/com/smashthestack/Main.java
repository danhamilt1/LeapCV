package com.smashthestack;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import com.leapmotion.leap.Controller.PolicyFlag;

public class Main {
	private static com.leapmotion.leap.Controller leapController;
	private static LeapImageUtils util;
	public static void main(String[] args) throws InvalidObjectException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		leapController = new com.leapmotion.leap.Controller();
		leapController.setPolicy(PolicyFlag.POLICY_IMAGES);
		
		LeapImages images = new LeapImages(leapController);
		
		for(int i = 0; i < 300; ++i){
			Highgui.imwrite("img"+ i + ".png", images.getImages().get(LeapImages.LEFT_IMAGE_KEY));
			images.moveToNextValidFrame();
			System.out.println("Image" + i);
			
		}
		
		//Highgui.imencode(".bmp", images.get(0), byteMat);
		
		//System.out.println(images.get(0).dump());
		
	}
}
