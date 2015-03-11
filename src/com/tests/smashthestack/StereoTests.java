package com.tests.smashthestack;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;

import com.smashthestack.LeapCVController;
import com.smashthestack.LeapCVStereoUtils;
import com.smashthestack.LeapCVStereoCalib;

public class StereoTests {

	@Before
	public void setUp() throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		LeapCVController leapController = new LeapCVController();
		leapController.nextValidFrame();
		LeapCVStereoCalib sc = new LeapCVStereoCalib(leapController.getCameras().get(0), leapController.getCameras().get(1));
		sc.findChessboardCorners();
	    
		//sc.addChessboardCorners();
		leapController.nextValidFrame();
		sc.findChessboardCorners();
		//sc.findChessboardCorners();
		//sc.findChessboardCorners();
		sc.calibrateLeapCameras();
	}

}
