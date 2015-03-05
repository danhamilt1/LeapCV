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
	private static LeapImageUtils util;
	public static void main(String[] args) throws InvalidObjectException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		LeapImages images = new LeapImages();
		
		images.initDistortionMat(LeapImages.LEFT_IMAGE_KEY, LeapImages.LEFT_IMAGE_ID);
		
		
		for(int i = 0; i < 300; ++i){
			Highgui.imwrite("img"+ i + ".png", images.getLeftImageUndistorted());
			images.moveToNextValidFrame();
			System.out.println("Image" + i);
			
		}
		
		//Highgui.imencode(".bmp", images.get(0), byteMat);
		
		//System.out.println(images.get(0).dump());
		
	}
}
