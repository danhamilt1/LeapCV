package com.leapcv;

import com.leapmotion.leap.Image;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class LeapCVCamera {

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

    protected static final int LEFT_ID = 0;
    protected static final int RIGHT_ID = 1;

    private Mat distortionX = null;
    private Mat distortionY = null;

    private CameraSide side = null;

    private LeapCVImage currentImage = null;

    public LeapCVCamera(CameraSide side) {
        this.side = side;
    }

    public Mat getDistortionX() {
        return distortionX;
    }

    public void setDistortionX(Mat distortionX) {
        this.distortionX = distortionX;
    }

    public Mat getDistortionY() {
        return distortionY;
    }

    public void setDistortionY(Mat distortionY) {
        this.distortionY = distortionY;
    }

    public CameraSide getSide() {
        return side;
    }

    public void setSide(CameraSide side) {
        this.side = side;
    }

    public LeapCVImage getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(Image image) {
        if (this.currentImage == null) {
            this.currentImage = new LeapCVImage(image);
        } else {
            this.currentImage.setImage(image);
        }
    }

    public Mat getImageUndistorted() {
        Mat processedImage = new Mat();

        Imgproc.remap(this.currentImage.getImageAsMat(), processedImage, this.distortionX,
                this.distortionY, Imgproc.INTER_LINEAR);

        processedImage = LeapCVImageUtils.crop(processedImage, 0.3);
        Imgproc.resize(processedImage, processedImage, new Size(240, 240));

        return processedImage;
    }

}
