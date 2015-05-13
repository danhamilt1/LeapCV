package com.leapcv;

import com.leapcv.utils.LeapCVImageUtils;
import com.leapmotion.leap.Image;
import junit.framework.TestCase;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;

public class LeapCVImageUtilsTest extends TestCase {
    public void setUp() throws Exception {
        super.setUp();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //this.controller = new LeapCVController();
    }

    public void testConvertToMat() throws Exception {
        LeapCVController controller = new LeapCVController();
        Image image = Image.invalid();
        Mat imageMat = LeapCVImageUtils.convertToMat(image);
        //LeapCVController controller = new LeapCVController();

        if(image.isValid()) failNotEquals("isValid", false, image.isValid());

        image = controller.getCameras().get(0).getCurrentImage().getImageAsLeap();

        if(!image.isValid()) failNotEquals("isValid", true, image.isValid());

        imageMat = LeapCVImageUtils.convertToMat(image);

        System.out.println(imageMat);

    }

    public void testInitDistortionMat() throws Exception {
        LeapCVController controller = new LeapCVController();
        Image image = controller.getCameras().get(0).getCurrentImage().getImageAsLeap();

        assertTrue(LeapCVImageUtils.initDistortionMat(image).containsValue(LeapCVImageUtils.X_MAT_KEY));
        assertTrue(LeapCVImageUtils.initDistortionMat(image).containsValue(LeapCVImageUtils.Y_MAT_KEY));
    }

    public void testToBufferedImage() throws Exception {
        LeapCVController controller = new LeapCVController();
        Image image = controller.getCameras().get(0).getCurrentImage().getImageAsLeap();

        BufferedImage imageTest = LeapCVImageUtils.toBufferedImage(image);

        assertTrue(image.width() == imageTest.getWidth());
        assertTrue(image.height() == imageTest.getHeight());
    }
//
//    public void testToWritableImage() throws Exception {
//        LeapCVController controller = new LeapCVController();
//        Image image = controller.getCameras().get(0).getCurrentImage().getImageAsLeap();
//        Mat imageMat = LeapCVImageUtils.convertToMat(image);
//        javafx.scene.image.Image writableImage =  LeapCVImageUtils.matToWritableImage(imageMat);
//
//        assertTrue(image.width() == writableImage.getWidth());
//        assertTrue(image.height() == writableImage.getHeight());
//    }

//    public void testMatToWritableImage() throws Exception {
//        LeapCVController controller = new LeapCVController();
//        Mat image = controller.getLeftImage();
//
//        javafx.scene.image.Image writableImage = LeapCVImageUtils.matToWritableImage(image);
//
//        assertTrue(image.width() == writableImage.getWidth());
//        assertTrue(image.height() == writableImage.getHeight());
//    }

    public void testGaussianBlur() throws Exception {
        LeapCVController controller = new LeapCVController();
        Mat image = controller.getLeftImage();

        image = LeapCVImageUtils.gaussianBlur(image);

        assertNotNull(image);
    }

    public void testMedianBlur() throws Exception {
        LeapCVController controller = new LeapCVController();
        Mat image = controller.getLeftImage();

        image = LeapCVImageUtils.medianBlur(image);

        assertNotNull(image);
    }

    public void testCrop() throws Exception {
        LeapCVController controller = new LeapCVController();
        Mat image = controller.getLeftImage();

        Mat newImage = LeapCVImageUtils.crop(image, 0.5);

        assertTrue(newImage.width() < image.width());
        assertTrue(newImage.height() < image.height());
    }

    public void testDenoise() throws Exception {
//        LeapCVController controller = new LeapCVController();
//        Image image = controller.getCameras().get(0).getCurrentImage().getImageAsLeap();
//
//        BufferedImage imageTest = LeapCVImageUtils.toBufferedImage(image);
//
//        assertTrue(image.width() == imageTest.getWidth());
//        assertTrue(image.height() == imageTest.getHeight());
    }
}