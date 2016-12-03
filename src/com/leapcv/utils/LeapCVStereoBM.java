package com.leapcv.utils;

import org.opencv.calib3d.StereoBM;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;


/**
 * Implementation of the {@link com.leapcv.utils.LeapCVStereoMatcher}
 */
public class LeapCVStereoBM implements LeapCVStereoMatcher {

    private StereoBM stereo;

    private final LeapCVMatcherType matcherType;

    public LeapCVStereoBM() {
        this.stereo = StereoBM.create(32, 7);

        this.matcherType = LeapCVMatcherType.STEREO_BM;

    }

    /**
     * Compute disparity map with StereoBM
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
