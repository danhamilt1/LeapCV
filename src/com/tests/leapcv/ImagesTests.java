package com.tests.leapcv;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import com.leapcv.*;

public class ImagesTests {
	private static int NUM_FRAMES = 100;
	@Before
	public void setUp() throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
//	@Test
//	public void testGetLeftImage() {
//		LeapCVController controller = new LeapCVController();
//		Highgui.imwrite("img_left.png", controller.getLeftImage());
//	}
//	
//	@Test
//	public void testGetRightImage(){
//		LeapCVController controller = new LeapCVController();
//		Highgui.imwrite("img_right.png", controller.getRightImage());
//	}
//	
//	@Test
//	public void testGetLeftImageUndistorted(){
//		LeapCVController controller = new LeapCVController();
//		Highgui.imwrite("img_left_u.png", controller.getLeftImageUndistorted());
//	}
//	
//	@Test
//	public void testGetRightImageUndistorted(){
//		LeapCVController controller = new LeapCVController();
//		Highgui.imwrite("img_right_u.png", controller.getRightImageUndistorted());
//	}
//	
//	@Test
//	public void testFrameRate(){
//		LeapCVController controller = new LeapCVController();
//		long startTime = System.nanoTime();
//		
//		for(int i = 0; i < NUM_FRAMES; ++i){
//			controller.moveToNextValidFrame();
//		}
//		
//		long endTime = System.nanoTime();
//		long elapsedTime = endTime - startTime;
//		long frameRate = NUM_FRAMES / (elapsedTime/1000000000);
//		System.out.println("Framerate: " + frameRate);
//		
//	}
	
//	@Test
//	public void testDisparity(){
//		LeapCVStereoUtils stereo = new LeapCVStereoUtils();
//		LeapCVController controller = new LeapCVController();
//		//Highgui.imwrite("disp1.png", stereo.getDisparityMap(controller.getRightImage(), controller.getLeftImage()));
//		Highgui.imwrite("disp2.png", stereo.getDisparityMap(controller.getLeftImageUndistorted(), controller.getRightImageUndistorted()));
//		Highgui.imwrite("displ.png", controller.getLeftImage());
//		Highgui.imwrite("dispr.png", controller.getRightImage());
//	}
	
	@Test
	public void testSift(){
		LeapCVController controller = new LeapCVController();
		//LeapCVObjectDetector.sift(controller.getLeftImageUndistorted());
	}
	
//	@Test
//	public void testGetImages(){
//		LeapImages images = new LeapImages();
//		Map<String,Mat> image = images.getImages();
//		
//	}

}