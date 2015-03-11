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
	private static LeapCVImageUtils util;
	public static void main(String[] args) throws InvalidObjectException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		LeapCVController leapController = new LeapCVController();
		leapController.nextValidFrame();
		LeapCVStereoCalib sc = new LeapCVStereoCalib(leapController.getCameras().get(0), leapController.getCameras().get(1));
		sc.findChessboardCorners();
		//sc.addChessboardCorners();
		leapController.nextValidFrame();
		sc.findChessboardCorners();
		sc.calibrateLeapCameras();
		
		//Highgui.imencode(".bmp", images.get(0), byteMat);
		
		//System.out.println(images.get(0).dump());
		
	}
}
