package com.leapcv.utils;

import org.opencv.contrib.StereoVar;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * Implementation of the {@link com.leapcv.utils.LeapCVStereoMatcher}
 */
public class LeapCVStereoVar implements LeapCVStereoMatcher {

    private double levels = 3;
    private double pyrScale = 0.25;
    private double polyN = 7;
    private double polySigma = 1.5;
    private double fi = 0.5;
    private double lambda = 10;
    private double minDisp = -80;
    private double maxDisp = 0;
    private double cycle = 150;

    private StereoVar stereo;

    private final LeapCVMatcherType matcherType;

    public LeapCVStereoVar() {
        this.stereo = new StereoVar();

        this.stereo.set_cycle((int) this.cycle);
        this.stereo.set_fi((float) this.fi);
        this.stereo.set_lambda((float) this.lambda);
        this.stereo.set_levels((int) this.levels);
        this.stereo.set_minDisp((int) this.minDisp);
        this.stereo.set_maxDisp((int) this.maxDisp);
        this.stereo.set_poly_sigma(this.polySigma);
        this.stereo.set_poly_n((int) this.polyN);
        this.stereo.set_pyrScale(this.pyrScale);

        this.matcherType = LeapCVMatcherType.STEREO_VAR;

    }


    /**
     * Compute disparity map with StereoVar
     * @param left Left camera image as {@link org.opencv.core.Mat}
     * @param right right camera image as {@link org.opencv.core.Mat}
     * @return {@link org.opencv.core.Mat} resulting disparity map
     */
    @Override
    public Mat compute(Mat left, Mat right) {
        Mat disparity = Mat.zeros(left.size(), CvType.CV_8UC1);

        stereo.compute(right, left, disparity);
        Core.normalize(disparity, disparity, 0, 255, Core.NORM_MINMAX);

        return disparity;
    }

    @Override
    public LeapCVMatcherType getType(){
        return this.matcherType;
    }
}
