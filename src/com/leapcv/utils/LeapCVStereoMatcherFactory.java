package com.leapcv.utils;

/**
 * Factory to create a LeapCVStereoMatcher
 */
public class LeapCVStereoMatcherFactory {

    /**
     * Create a LeapCVStereoMatcher
     * @param type {@link com.leapcv.utils.LeapCVMatcherType}
     * @return {@link com.leapcv.utils.LeapCVStereoMatcher}
     */
    public static LeapCVStereoMatcher create(LeapCVMatcherType type){
        LeapCVStereoMatcher matcher = null;

        switch(type){
            case STEREO_BM:
                    matcher = new LeapCVStereoBM();
                break;
            case STEREO_SGBM:
                    matcher = new LeapCVStereoSGBM();
                break;
            case STEREO_VAR:
                    matcher = new LeapCVStereoVar();
                break;
        }

        return matcher;
    }
}
