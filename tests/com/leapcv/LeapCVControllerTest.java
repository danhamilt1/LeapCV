package com.leapcv;

import junit.framework.TestCase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.List;

public class LeapCVControllerTest extends TestCase {
    LeapCVController controller;
    Mat leftImage = null;
    Mat rightImage = null;
    Mat leftImageUndistorted = null;
    Mat rightImageUndistorted = null;
    List<LeapCVCamera> cameras = null;

    public void setUp() throws Exception {
        super.setUp();
        System.out.println(Core.NATIVE_LIBRARY_NAME);
        System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        this.controller = new LeapCVController();
    }

    public void testNextValidFrame() throws Exception {
        this.controller.nextValidFrame();

    }

    public void testGetLeftImage() throws Exception {
        leftImage = this.controller.getLeftImage();
        assertNotNull(leftImage);
    }

    public void testGetRightImage() throws Exception {
        rightImage = this.controller.getRightImage();
        assertNotNull(rightImage);
    }

    public void testGetLeftImageUndistorted() throws Exception {
        leftImageUndistorted = this.controller.getLeftImageUndistorted();
        assertNotNull(leftImageUndistorted);
    }

    public void testGetRightImageUndistorted() throws Exception {
        rightImageUndistorted = this.controller.getRightImageUndistorted();
        assertNotNull(rightImageUndistorted);
    }

    public void testGetCameras() throws Exception {
        this.cameras = this.controller.getCameras();
        assertNotNull(cameras);

        assertEquals(2, cameras.size());
        assertEquals("Cameras in wrong order", LeapCVCamera.LEFT_ID, cameras.get(LeapCVCamera.LEFT_ID).getSide().getSideId());
        assertEquals("Cameras in wrong order",LeapCVCamera.RIGHT_ID, cameras.get(LeapCVCamera.RIGHT_ID).getSide().getSideId());
        assertNotSame("Cameras same", cameras.get(0), cameras.get(1));

        leftImage = this.controller.getLeftImage();
        rightImage = this.controller.getRightImage();
        leftImageUndistorted = this.controller.getLeftImageUndistorted();
        rightImageUndistorted = this.controller.getRightImageUndistorted();

        this.writeImages();
    }

    private void writeImages(){
        final String path = Paths.ROOT_PATH + "/disp/testData/";
        Imgcodecs.imwrite(path + "leftImage.png", leftImage);
        Imgcodecs.imwrite(path + "rightImage.png", rightImage);
        Imgcodecs.imwrite(path + "leftImageUndistorted.png", leftImageUndistorted);
        Imgcodecs.imwrite(path + "rightImageUndistorted.png", rightImageUndistorted);

    }
}