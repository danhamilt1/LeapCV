package com.leapcv.utils;

import org.opencv.core.Mat;

/**
 * Interface for StereoMatcher factory.
 */
public interface LeapCVStereoMatcher {

    /**
     * Compute disparity map
     * @param left left image as {@link org.opencv.core.Mat}
     * @param right right image as {@link org.opencv.core.Mat}
     * @return @return {@link org.opencv.core.Mat} resulting disparity map
     */
    public Mat compute(Mat left, Mat right);

    /**
     * Get matcher type
     * @return {@link com.leapcv.utils.LeapCVMatcherType}
     */
    public LeapCVMatcherType getType();
}


