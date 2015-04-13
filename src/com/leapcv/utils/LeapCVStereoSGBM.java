package com.leapcv.utils;

import org.opencv.calib3d.StereoSGBM;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * Implementation of the {@link com.leapcv.utils.LeapCVStereoMatcher}
 */
public class LeapCVStereoSGBM implements LeapCVStereoMatcher {

    private StereoSGBM stereo;

    private final LeapCVMatcherType matcherType;

    public LeapCVStereoSGBM() {
        this.stereo = new StereoSGBM(-64,
                16,
                11,
                2500,
                3000,
                100,
                0,
                0,
                0,
                0,
                true);

        this.matcherType = LeapCVMatcherType.STEREO_SGBM;

    }

    /**
     * Compute disparity map with StereoSGBM
     * @param left Left camera image as {@link org.opencv.core.Mat}
     * @param right right camera image as {@link org.opencv.core.Mat}
     * @return {@link org.opencv.core.Mat} resulting disparity map
     */
    @Override
    public Mat compute(Mat left, Mat right) {
        Mat disparity = Mat.zeros(left.size(), CvType.CV_8UC1);

        stereo.compute(left, right, disparity);
        Core.normalize(disparity, disparity, 0, 255, Core.NORM_MINMAX);

        return disparity;
    }

    @Override
    public LeapCVMatcherType getType(){
        return this.matcherType;
    }
}
