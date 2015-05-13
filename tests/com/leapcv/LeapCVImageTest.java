package com.leapcv;

import junit.framework.TestCase;
import org.opencv.core.Core;
import org.opencv.highgui.Highgui;

public class LeapCVImageTest extends TestCase {

    LeapCVController controller;
    public void setUp() throws Exception {
        super.setUp();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        this.controller = new LeapCVController();
    }

    public void testGetImageAsMat() throws Exception {

    }

    public void testGetImageAsLeap() throws Exception {

    }

    public void testSetImage() throws Exception {

    }
}