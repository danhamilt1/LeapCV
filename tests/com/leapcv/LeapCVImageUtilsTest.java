package com.leapcv;

import com.leapcv.utils.LeapCVImageUtils;
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