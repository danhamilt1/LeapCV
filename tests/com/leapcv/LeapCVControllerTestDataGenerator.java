package com.leapcv;

import com.leapcv.utils.LeapCVStereoUtils;
import junit.framework.TestCase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.util.List;

public class LeapCVControllerTestDataGenerator extends TestCase {
    LeapCVController controller;
    Mat leftImage = null;
    Mat rightImage = null;
    Mat leftImageUndistorted = null;
    Mat rightImageUndistorted = null;
    Mat disparityImage = null;
    List<LeapCVCamera> cameras = null;

    public void setUp() throws Exception {
        super.setUp();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        this.controller = new LeapCVController();
    }

    public void testNextValidFrame() throws Exception {
        this.controller.nextValidFrame();


        leftImage = this.controller.getLeftImage();
        assertNotNull(leftImage);


        rightImage = this.controller.getRightImage();
        assertNotNull(rightImage);


        leftImageUndistorted = this.controller.getLeftImageUndistorted();
        assertNotNull(leftImageUndistorted);


        rightImageUndistorted = this.controller.getRightImageUndistorted();
        assertNotNull(rightImageUndistorted);


        LeapCVStereoUtils utils = new LeapCVStereoUtils();
        disparityImage = utils.getDisparityMap(leftImageUndistorted, rightImageUndistorted);


        cameras = this.controller.getCameras();
        assertNotNull(cameras);

        assertEquals(2, cameras.size());
        assertEquals("Cameras in wrong order", LeapCVCamera.LEFT_ID, cameras.get(LeapCVCamera.LEFT_ID).getSide().getSideId());
        assertEquals("Cameras in wrong order", LeapCVCamera.RIGHT_ID, cameras.get(LeapCVCamera.RIGHT_ID).getSide().getSideId());
        assertNotSame("Cameras same", cameras.get(0), cameras.get(1));
        this.writeImages();
    }

    private void writeImages(){
        final String path = "/Volumes/macintosh_hdd/Users/daniel/Desktop/Report/test_images/";
        Highgui.imwrite(path + "leftImage.png", leftImage);
        Highgui.imwrite(path + "rightImage.png", rightImage);
        Highgui.imwrite(path + "leftImageUndistorted.png", leftImageUndistorted);
        Highgui.imwrite(path + "rightImageUndistorted.png", rightImageUndistorted);
        Highgui.imwrite(path + "disparityImage.png", disparityImage);

    }
}