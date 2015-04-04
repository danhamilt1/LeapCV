package com.leapcv;

import com.leapcv.utils.LeapCVStereoUtils;
import junit.framework.TestCase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.util.List;

/**
 * Test the performance of the LeapCV functionality
 */
public class LeapCVPerformanceTest extends TestCase {
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
        double average = 0;

        for(int i = 0; i < NUM_TESTS; ++i){
            average += testFrameRateGetImage();
        }

        average = average/NUM_TESTS;
        System.out.println("Average: " + average);
        assertTrue(average > MIN_FRAMERATE);
    }

    public void testGetImageUndistorted(){
        double average = 0;

        for(int i = 0; i < NUM_TESTS; ++i){
            average += testFrameRateGetImageUndistorted();
        }

        average = average/NUM_TESTS;
        System.out.println("Average: " + average);
        assertTrue(average > MIN_FRAMERATE);
    }

    public void testGetDisparity(){
        double average = 0;

        for(int i = 0; i < NUM_TESTS; ++i){
            average += testFrameRateGetDisparity();
        }

        average = average/NUM_TESTS;
        System.out.println("Average: " + average);
        assertTrue(average > MIN_FRAMERATE);
    }

    public void testGetDisparity2(){
        double average = 0;

        for(int i = 0; i < NUM_TESTS; ++i){
            average += testFrameRateGetDisparity2();
        }

        average = average/NUM_TESTS;
        System.out.println("Average: " + average);
        assertTrue(average > MIN_FRAMERATE);
    }

    public void testGetDisparity3(){
        double average = 0;

        for(int i = 0; i < NUM_TESTS; ++i){
            average += testFrameRateGetDisparity3();
        }

        average = average/NUM_TESTS;
        System.out.println("Average: " + average);
        assertTrue(average > MIN_FRAMERATE);
    }

    public void testGetPointCloud(){
        double average = 0;

        for(int i = 0; i < NUM_TESTS; ++i){
            average += testFrameRateGetPointCloud();
        }

        average = average/NUM_TESTS;
        System.out.println("Average: " + average);
        assertTrue(average > MIN_FRAMERATE);
    }

    private double testFrameRateGetImage(){
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

        Highgui.imwrite("/Volumes/macintosh_hdd/Users/daniel/Desktop/imageTest.png", leftImage);

        return fps;

    }

    private double testFrameRateGetImageUndistorted(){
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

        Highgui.imwrite("/Volumes/macintosh_hdd/Users/daniel/Desktop/udTest.png", leftImage);

        return fps;
    }

    private double testFrameRateGetDisparity(){
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

        Highgui.imwrite("/Volumes/macintosh_hdd/Users/daniel/Desktop/dispTest.png", dispMat);

        return fps;
    }

    private double testFrameRateGetDisparity2(){
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

        Highgui.imwrite("/Volumes/macintosh_hdd/Users/daniel/Desktop/dispTest2.png", dispMat);

        return fps;
    }

    private double testFrameRateGetDisparity3(){
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

        Highgui.imwrite("/Volumes/macintosh_hdd/Users/daniel/Desktop/dispTest3.png", dispMat);

        return fps;
    }

    private double testFrameRateGetPointCloud(){
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

        Highgui.imwrite("/Volumes/macintosh_hdd/Users/daniel/Desktop/dispTest.png", dispMat);

        return fps;
    }

}