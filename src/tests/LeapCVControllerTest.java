package tests;

import com.leapcv.LeapCVController;
import junit.framework.TestCase;
import org.opencv.core.Core;

public class LeapCVControllerTest extends TestCase {
    LeapCVController controller;
    public void setUp() throws Exception {
        super.setUp();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        this.controller = new LeapCVController();
    }

    public void testNextValidFrame() throws Exception {
        this.controller.nextValidFrame();

    }

    public void testGetLeftImage() throws Exception {
        this.controller.getLeftImage();
    }

    public void testGetRightImage() throws Exception {
        this.controller.getRightImage();
    }

    public void testGetLeftImageUndistorted() throws Exception {
        this.controller.getLeftImageUndistorted();
    }

    public void testGetRightImageUndistorted() throws Exception {
        this.controller.getRightImageUndistorted();
    }

    public void testGetCameras() throws Exception {
        this.controller.getCameras();
    }
}