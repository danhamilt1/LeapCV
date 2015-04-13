package com.leapcv;

import com.leapcv.utils.LeapCVCalibrationUtils;
import junit.framework.TestCase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.util.List;

/**
 * Test the performance of the LeapCV functionality
 */
public class LeapCVChessboardTest extends TestCase {
    LeapCVController controller;
    Mat leftImage = null;
    Mat rightImage = null;
    List<LeapCVCamera> cameras = null;
    final int NUM_TESTS = 20;
    final int NUM_FRAMES = 100;
    final int MIN_FRAMERATE = 15;

    public void setUp() throws Exception {
        super.setUp();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        this.controller = new LeapCVController();
    }

    public void testGetImage(){
        Mat image = this.controller.getLeftImage();
        Highgui.imwrite("/Volumes/macintosh_hdd/Users/daniel/Desktop/1.png", image);
        Mat chess = LeapCVCalibrationUtils.getImageWithChessboard(image);
        Highgui.imwrite("/Volumes/macintosh_hdd/Users/daniel/Desktop/4.png", chess);

        image = this.controller.getLeftImageUndistorted();
        Highgui.imwrite("/Volumes/macintosh_hdd/Users/daniel/Desktop/3.png", image);
        chess = LeapCVCalibrationUtils.getImageWithChessboard(image);
        Highgui.imwrite("/Volumes/macintosh_hdd/Users/daniel/Desktop/5.png", chess);

    }

}