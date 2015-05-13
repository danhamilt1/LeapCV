package com.leapcv.utils;

/**
 * Factory to create a LeapCVStereoMatcher
 */
public class LeapCVStereoMatcherFactory {
    private static LeapCVStereoMatcher matcher = null;
    /**
     * Create a LeapCVStereoMatcher
     * @param type {@link com.leapcv.utils.LeapCVMatcherType}
     * @return {@link com.leapcv.utils.LeapCVStereoMatcher}
     */
    public static LeapCVStereoMatcher create(LeapCVMatcherType type){
        if (matcher == null) {
            matcher = new LeapCVStereoVar();
        }

        if(matcher.getType() != type) {
            switch (type) {
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
        }

        return matcher;
    }
}
