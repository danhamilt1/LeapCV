package com.smashthestack;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class Controller {
	private static LeapImageUtils util;
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		util = new LeapImageUtils();
		util.initLeap();
		ArrayList<Mat> images = null;
		
		for(int i = 0; i < 30; ++i){
			images = util.getImages();
			Highgui.imwrite("img"+ i + ".png", images.get(0));
			System.out.println("Image" + i);
		}
		
		//Highgui.imencode(".bmp", images.get(0), byteMat);
		
		//System.out.println(images.get(0).dump());
		
	}
}
