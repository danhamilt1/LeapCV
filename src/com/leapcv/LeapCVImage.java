package com.leapcv;

import com.leapcv.utils.LeapCVImageUtils;
import com.leapmotion.leap.Image;
import org.opencv.core.Mat;

public class LeapCVImage extends com.leapmotion.leap.Image {

    private Mat imageAsMat;
    private Image imageAsLeap;

    public LeapCVImage(Image image) {
        this.imageAsLeap = image;
        this.imageAsMat = LeapCVImageUtils.convertToMat(image);
    }

    public Mat getImageAsMat() {
        return imageAsMat;
    }

    private void setImageAsMat(Mat imageAsMat) {
        this.imageAsMat = imageAsMat;
    }

    public Image getImageAsLeap() {
        return imageAsLeap;
    }

    private void setImageAsLeap(Image imageAsLeap) {
        this.imageAsLeap = imageAsLeap;
    }

    public void setImage(Image image) {
        this.imageAsLeap = image;
        this.imageAsMat = LeapCVImageUtils.convertToMat(image);
    }
}
