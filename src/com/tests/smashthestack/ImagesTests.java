package com.tests.smashthestack;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import com.smashthestack.*;

public class ImagesTests {
	private static int NUM_FRAMES = 500;
	@Before
	public void setUp() throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	@Test
	public void testGetLeftImage() {
		
		LeapImages images = new LeapImages();
		for(int i = 0; i < NUM_FRAMES; ++i){
			Mat image = images.getLeftImage();
			images.moveToNextValidFrame();
			Highgui.imwrite("img" + "_left_" + ".png", image);
		}

	}
	
	@Test
	public void testGetRightImage(){
		
		LeapImages images = new LeapImages();
		Mat image = images.getRightImage();
		Highgui.imwrite("img" + "_right_" + ".png", image);
	}
	
	@Test
	public void testGetImages(){
		LeapImages images = new LeapImages();
		Map<String,Mat> image = images.getImages();
		
	}

}
