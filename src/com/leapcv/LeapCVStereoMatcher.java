package com.leapcv;

import org.opencv.core.Mat;

/**
 * Interface for StereoMatcher factory.
 */
public interface LeapCVStereoMatcher {

    public Mat compute();
}


