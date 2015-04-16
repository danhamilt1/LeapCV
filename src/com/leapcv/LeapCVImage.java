package com.leapcv;

import com.leapcv.utils.LeapCVImageUtils;
import com.leapmotion.leap.Image;
import org.opencv.core.Mat;

/**
 * Image class for leap motion and OpenCV types
 */
public class LeapCVImage extends Mat {

    private Mat imageAsMat;
    private Image imageAsLeap;

    //TODO Inherit Mat constructors and implement throughout the framework so that it isn't using raw Mat Type
    
    public LeapCVImage(Image image) {
        this.imageAsLeap = image;
        this.imageAsMat = LeapCVImageUtils.convertToMat(image);
    }

    /**
     * Get the image as a {@link org.opencv.core.Mat}
     * @return {@link org.opencv.core.Mat}
     */
    public Mat getImageAsMat() {
        return imageAsMat;
    }

    private void setImageAsMat(Mat imageAsMat) {
        this.imageAsMat = imageAsMat;
    }

    /**
     * Get the image as a {@link com.leapmotion.leap.Image}
     * @return {@link com.leapmotion.leap.Image}
     */
    public Image getImageAsLeap() {
        return imageAsLeap;
    }

    private void setImageAsLeap(Image imageAsLeap) {
        this.imageAsLeap = imageAsLeap;
    }

    /**
     * Set the image
     * @param image {@link com.leapmotion.leap.Image}
     */
    public void setImage(Image image) {
        this.imageAsLeap = image;
        this.imageAsMat = LeapCVImageUtils.convertToMat(image);
    }
}
