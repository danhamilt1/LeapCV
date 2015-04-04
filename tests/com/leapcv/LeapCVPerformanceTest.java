package com.leapcv;

import com.leapcv.utils.LeapCVStereoUtils;
import junit.framework.TestCase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.util.List;

public class LeapCVPerformanceTest extends TestCase {
    LeapCVController controller;
    Mat leftImage = null;
    Mat rightImage = null;
    List<LeapCVCamera> cameras = null;
    final int NUM_FRAMES = 100;
    final int MIN_FRAMERATE = 15;

    public void setUp() throws Exception {
        super.setUp();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        this.controller = new LeapCVController();
    }

    public void testFrameRateGetImage(){
        long start = 0;
        long stop = 0;

        start = System.nanoTime();

        for(int i = 0; i < NUM_FRAMES; ++i){
            leftImage = this.controller.getLeftImage();
            rightImage = this.controller.getRightImage();
            this.controller.nextValidFrame();
        }

        stop = System.nanoTime();

        long elapsed = stop - start;
        double seconds = (double)elapsed/1000000000.0;
        double fps = NUM_FRAMES/seconds;

        System.out.println("FPS GET IMAGE: " + fps);
        assertTrue(fps > MIN_FRAMERATE);
        Highgui.imwrite("/Volumes/macintosh_hdd/Users/daniel/Desktop/imageTest.png", leftImage);

    }

    public void testFrameRateGetImageUndistorted(){
        long start = 0;
        long stop = 0;

        start = System.nanoTime();

        for(int i = 0; i < NUM_FRAMES; ++i){
            leftImage = this.controller.getLeftImageUndistorted();
            rightImage = this.controller.getRightImageUndistorted();
            this.controller.nextValidFrame();
        }

        stop = System.nanoTime();

        long elapsed = stop - start;
        double seconds = (double)elapsed/1000000000.0;
        double fps = NUM_FRAMES/seconds;

        System.out.println("FPS GET IMAGE UD: " + fps);
        assertTrue(fps > MIN_FRAMERATE);
        Highgui.imwrite("/Volumes/macintosh_hdd/Users/daniel/Desktop/udTest.png", leftImage);

    }

    public void testFrameRateGetDisparity(){
        long start = 0;
        long stop = 0;
        Mat dispMat = null;
        LeapCVStereoUtils utils = new LeapCVStereoUtils();

        start = System.nanoTime();

        for(int i = 0; i < NUM_FRAMES; ++i){
            leftImage = this.controller.getLeftImageUndistorted();
            rightImage = this.controller.getRightImageUndistorted();
            dispMat = utils.getDisparityMap(leftImage,rightImage);
            this.controller.nextValidFrame();
        }

        stop = System.nanoTime();

        long elapsed = stop - start;
        double seconds = (double)elapsed/1000000000.0;
        double fps = NUM_FRAMES/seconds;

        System.out.println("FPS GET DISP: " + fps);
        assertTrue(fps > MIN_FRAMERATE);
        Highgui.imwrite("/Volumes/macintosh_hdd/Users/daniel/Desktop/dispTest.png", dispMat);
    }

    public void testFrameRateGetDisparity2(){
        long start = 0;
        long stop = 0;
        Mat dispMat = null;
        LeapCVStereoUtils utils = new LeapCVStereoUtils();

        start = System.nanoTime();

        for(int i = 0; i < NUM_FRAMES; ++i){
            leftImage = this.controller.getLeftImageUndistorted();
            rightImage = this.controller.getRightImageUndistorted();
            dispMat = utils.getDisparityMap2(leftImage, rightImage);
            this.controller.nextValidFrame();
        }

        stop = System.nanoTime();

        long elapsed = stop - start;
        double seconds = (double)elapsed/1000000000.0;
        double fps = NUM_FRAMES/seconds;

        System.out.println("FPS GET DISP2: " + fps);
        assertTrue(fps > MIN_FRAMERATE);
        Highgui.imwrite("/Volumes/macintosh_hdd/Users/daniel/Desktop/dispTest2.png", dispMat);
    }

    public void testFrameRateGetDisparity3(){
        long start = 0;
        long stop = 0;
        Mat dispMat = null;
        LeapCVStereoUtils utils = new LeapCVStereoUtils();

        start = System.nanoTime();

        for(int i = 0; i < NUM_FRAMES; ++i){
            leftImage = this.controller.getLeftImageUndistorted();
            rightImage = this.controller.getRightImageUndistorted();
            dispMat = utils.getDisparityMap3(leftImage,rightImage);
            this.controller.nextValidFrame();
        }

        stop = System.nanoTime();

        long elapsed = stop - start;
        double seconds = (double)elapsed/1000000000.0;
        double fps = NUM_FRAMES/seconds;

        System.out.println("FPS GET DISP3: " + fps);
        assertTrue(fps > MIN_FRAMERATE);
        Highgui.imwrite("/Volumes/macintosh_hdd/Users/daniel/Desktop/dispTest3.png", dispMat);
    }

    public void testFrameRateGetPointCloud(){
        long start = 0;
        long stop = 0;
        Mat dispMat = null;
        Mat pointMat = null;
        LeapCVStereoUtils utils = new LeapCVStereoUtils();

        //  Start timer
        start = System.nanoTime();

        for(int i = 0; i < NUM_FRAMES; ++i){
            leftImage = this.controller.getLeftImageUndistorted();
            rightImage = this.controller.getRightImageUndistorted();
            dispMat = utils.getDisparityMap(leftImage,rightImage);
            pointMat = utils.getPointCloud(dispMat);
            this.controller.nextValidFrame();
        }

        //  Stop timer
        stop = System.nanoTime();

        // Calculate Framerate
        long elapsed = stop - start;
        double seconds = (double)elapsed/1000000000.0;
        double fps = NUM_FRAMES/seconds;

        //  Print Framerate
        System.out.println("FPS GET PC: " + fps);
        assertTrue(fps > MIN_FRAMERATE);
        Highgui.imwrite("/Volumes/macintosh_hdd/Users/daniel/Desktop/dispTest.png", dispMat);
    }

}