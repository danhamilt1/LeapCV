package com.leapcv;

import com.leapcv.utils.LeapCVImageUtils;
import com.leapmotion.leap.Image;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Class that stores information about the leap motion cameras
 */
public class LeapCVCamera {

    /**
     * Enumeration of the camera sides
     * 0 = left
     * 1 = right
     */
    public enum CameraSide {
        LEFT(0), RIGHT(1);

        private int side;

        CameraSide(int side) {
            this.side = side;
        }

        public int getSideId() {
            return this.side;
        }
    }

//    protected static final int LEFT_ID = 0;
//    protected static final int RIGHT_ID = 1;

    private Mat distortionX = null;
    private Mat distortionY = null;

    private CameraSide side = null;

    private LeapCVImage currentImage = null;

    public LeapCVCamera(CameraSide side) {
        this.side = side;
    }

    /**
     * Get the X distortion {@link org.opencv.core.Mat} for this camera
     * for use with the OpenCV remap method
     * @return {@link org.opencv.core.Mat}
     */
    public Mat getDistortionX() {
        return distortionX;
    }

    /**
     * Set the X distortion {@link org.opencv.core.Mat} for this camera
     * @param distortionX Distortion {@link org.opencv.core.Mat} for X
     */
    public void setDistortionX(Mat distortionX) {
        this.distortionX = distortionX;
    }

    /**
     * Get the Y distortion {@link org.opencv.core.Mat} for this camera
     * for use with the OpenCV remap method
     * @return
     */
    public Mat getDistortionY() {
        return distortionY;
    }

    /**
     * Set the Y distortion {@link org.opencv.core.Mat} for this camera
     * @param distortionY Distortion {@link org.opencv.core.Mat} for Y
     */
    public void setDistortionY(Mat distortionY) {
        this.distortionY = distortionY;
    }

    /**
     * Get the side of this camera
     * @return {@link com.leapcv.LeapCVCamera.CameraSide}
     */
    public CameraSide getSide() {
        return side;
    }


    /**
     * Set the side of this camera. Each camera already has a side set from the LeapSDK so make sure this gets
     * set within LeapCV.
     * @return {@link com.leapcv.LeapCVCamera.CameraSide}
     */
    public void setSide(CameraSide side) {
        this.side = side;
    }

    /**
     * Get the image within the current frame state
     * @return {@link com.leapcv.LeapCVImage}
     */
    public LeapCVImage getCurrentImage() {
        return currentImage;
    }

    /**
     * Set the image in the current frame state
     * @param image Image to be set
     */
    public void setCurrentImage(Image image) {
        if (this.currentImage == null) {
            this.currentImage = new LeapCVImage(image);
        } else {
            this.currentImage.setImage(image);
        }
    }

    /**
     * Get the undistorted image from this camera. distortionX and distortionY need to be set first.
     * Makes use of the OpenCV {@link org.opencv.imgproc.Imgproc} remap() method.
     * @return {@link org.opencv.core.Mat}
     */
    public Mat getImageUndistorted() {
        Mat processedImage = new Mat();

        Imgproc.remap(this.currentImage.getImageAsMat(), processedImage, this.distortionX,
                this.distortionY, Imgproc.INTER_LINEAR);
        processedImage = LeapCVImageUtils.crop(processedImage, 0.3);
        Imgproc.resize(processedImage, processedImage, new Size(600, 600));

        return processedImage;
    }

}
