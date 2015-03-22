package com.leapcv;

import com.leapmotion.leap.Image;
import junit.framework.TestCase;
import org.opencv.core.Core;
import org.opencv.core.Mat;

public class LeapCVImageUtilsTest extends TestCase {
    public void setUp() throws Exception {
        super.setUp();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //this.controller = new LeapCVController();
    }

    public void testConvertToMat() throws Exception {
        Image image = new Image();
        Mat imageMat = LeapCVImageUtils.convertToMat(image);
        //LeapCVController controller = new LeapCVController();

        if(imageMat == null) fail();

        if(imageMat.width() != 0) failNotEquals("Width", 0, imageMat.width());

        if(imageMat.height() != 0) failNotEquals("Height", 0, imageMat.height());

        if(image.isValid() != true) failNotEquals("isValid", true, image.isValid());

        image = Image.invalid();

        if(image.isValid() != false) failNotEquals("isValid", false, image.isValid());

        imageMat = LeapCVImageUtils.convertToMat(image);

        System.out.println(imageMat);

    }

    public void testInitDistortionMat() throws Exception {

    }

    public void testToBufferedImage() throws Exception {

    }

    public void testToWritableImage() throws Exception {

    }

    public void testMatToWritableImage() throws Exception {

    }

    public void testGaussianBlur() throws Exception {

    }

    public void testMedianBlur() throws Exception {

    }

    public void testCrop() throws Exception {

    }

    public void testDenoise() throws Exception {

    }
}