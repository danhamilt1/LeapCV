package com.leapcv;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Controller.PolicyFlag;
import com.leapmotion.leap.ImageList;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to be initialised to interface with the leap motion.
 */
public class LeapCVController {

    private ImageList currentImages = null;

    private Controller leapController;
    private LeapCVCamera leftCam;
    private LeapCVCamera rightCam;

    /**
     * Constructor for {@link com.leapcv.LeapCVController}
     */
    public LeapCVController() {
        this.leapController = new Controller();
        this.initLeap();
        this.initDistortionMats();
    }

    /**
     * Initialize leap controller and wait for the next valid frame to be received
     */
    private void initLeap() {
        this.leapController.setPolicy(PolicyFlag.POLICY_IMAGES);
        this.leftCam = new LeapCVCamera(LeapCVCamera.CameraSide.LEFT);
        this.rightCam = new LeapCVCamera(LeapCVCamera.CameraSide.RIGHT);

        this.nextValidFrame();
    }

    /**
     * Initialize the leap motion controller distortion matrices for the left and right camera
     */
    private void initDistortionMats() {
        // Initialise distortion mats for fast image remap
        Map<String, Mat> mats = LeapCVImageUtils.initDistortionMat(this.leftCam.getCurrentImage().getImageAsLeap());
        this.leftCam.setDistortionX(mats.get(LeapCVImageUtils.X_MAT_KEY));
        this.leftCam.setDistortionY(mats.get(LeapCVImageUtils.Y_MAT_KEY));

        mats = LeapCVImageUtils.initDistortionMat(this.rightCam.getCurrentImage().getImageAsLeap());
        this.rightCam.setDistortionX(mats.get(LeapCVImageUtils.X_MAT_KEY));
        this.rightCam.setDistortionY(mats.get(LeapCVImageUtils.Y_MAT_KEY));
    }

    /**
     * Move the leap motion controller on to the next valid frame
     */
    public void nextValidFrame() {
        final int MAX_INVALID_FRAMES = 100;
        int framesInvalid = 0;
        this.currentImages = this.leapController.images();

        while (!this.currentImages.get(0).isValid() && framesInvalid < MAX_INVALID_FRAMES) {
            this.currentImages = this.leapController.images();
            ++framesInvalid;
        }

        //if(framesInvalid == MAX_INVALID_FRAMES)

        for (int i = 0; i < this.currentImages.count(); ++i) {
            switch (this.currentImages.get(i).id()) {
                case LeapCVCamera.LEFT_ID:
                    this.leftCam.setCurrentImage(this.currentImages.get(i));
                    break;
                case LeapCVCamera.RIGHT_ID:
                    this.rightCam.setCurrentImage(this.currentImages.get(i));
                    break;
            }
        }
    }

    /**
     * Get raw image from the left side camera
     *
     * @return {@link Mat}
     */
    public Mat getLeftImage() {
        return this.leftCam.getCurrentImage().getImageAsMat();
    }

    /**
     * Get raw image from the right side camera
     *
     * @return {@link Mat}
     */
    public Mat getRightImage() {
        return this.rightCam.getCurrentImage().getImageAsMat();
    }

    /**
     * Get undistorted image from the left side camera
     *
     * @return {@link Mat}
     */
    public Mat getLeftImageUndistorted() {
        return this.leftCam.getImageUndistorted();
    }

    /**
     * Get undistorted image from the right side camera
     *
     * @return
     */
    public Mat getRightImageUndistorted() {
        return this.rightCam.getImageUndistorted();
    }

    /**
     * Get the {@link LeapCVCamera} objects from the {@link LeapCVController}
     *
     * @return {@link List} - containing {@link LeapCVCamera}
     */
    public List<LeapCVCamera> getCameras() {
        List<LeapCVCamera> cameras = new ArrayList<>();
        cameras.add(leftCam);
        cameras.add(rightCam);

        return cameras;
    }

}
