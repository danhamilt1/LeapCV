package com.smashthestack;

import org.opencv.calib3d.Calib3d;
import org.opencv.calib3d.StereoBM;
import org.opencv.calib3d.StereoSGBM;
import org.opencv.contrib.StereoVar;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public class LeapCVStereoUtils {
	
	private StereoSGBM stereo = null;
	private StereoVar stereo2 = null;
//    private int minDisparity = 0;
//    private int numDisparities = 16;
//    private int SADWindowSize = 7;
//    private int P1 = 322412;//8*numChannels*SADWindowSize*SADWindowSize;
//    private int P2 = 529456;//32*numChannels*SADWindowSize*SADWindowSize;
//    private int disp12MaxDiff = 100;
//    private int preFilterCap = 0;
//    private int uniquenessRatio = 8;
//    private int speckleWindowSize = 0; 
//    private int speckleRange = 8;
//    private boolean fullDp = true;
    private int numChannels = 1;
    private int minDisparity = 1;
    private int numDisparities = 16;
    private int SADWindowSize = 13;
    private int P1 = 318162;//8*numChannels*SADWindowSize*SADWindowSize;
    private int P2 = 529456;//*numChannels*SADWindowSize*SADWindowSize;
    private int disp12MaxDiff = 100;
    private int preFilterCap = 0;
    private int uniquenessRatio = 0;
    private int speckleWindowSize = 0; 
    private int speckleRange = 12;
    private boolean fullDP = true;
    private int type = CvType.CV_8UC1;
    
	public LeapCVStereoUtils(){
		this.stereo = new StereoSGBM(minDisparity, 
        		numDisparities, 
        		SADWindowSize,
        		P1, 
        		P2, 
        		disp12MaxDiff, 
        		preFilterCap, 
        		uniquenessRatio, 
        		speckleWindowSize, 
        		speckleRange, 
        		fullDP);
		this.stereo2 = new StereoVar();

	}
	
	public Mat getDisparityMap(Mat left, Mat right){
		//self.d2d = np.array([[1.0,0.,0.,-5.0638e+02],[0.,1.,0.,-2.3762e+02],[0.,0.,0.,1.3476e+03],[0.,0.,6.9349981e-01,3.503271]])	#depth-to-dist mapping
		Mat d2d = new Mat(4,4,type);
		Rect roi = new Rect(120,140,400,200);
		//d2d.pu
		//Calib3d.stereoCalibrate(objectPoints, imagePoints1, imagePoints2, cameraMatrix1, distCoeffs1, cameraMatrix2, distCoeffs2, imageSize, R, T, E, F)
		Mat disp = new Mat(0,0,type);
		
		

		stereo2.set_levels(3);
		stereo2.set_pyrScale(0.25);
		//stereo2.set_penalization(StereoVar.PENALIZATION_PERONA_MALIK );
//		stereo2.set_cycle(StereoVar.CYCLE_V);
		stereo2.set_flags(StereoVar.USE_AUTO_PARAMS);
		stereo2.set_poly_n(7);
		stereo2.set_poly_sigma(1.5);
		stereo2.set_fi(3);
		stereo2.set_lambda(5);
		stereo2.set_minDisp(-64);
		stereo2.set_maxDisp(255);
		
		stereo2.compute(right.submat(roi),left.submat(roi),disp);
		Core.normalize(disp, disp, 0, 255, Core.NORM_MINMAX);
		
		return disp;
	}
	
	public void getImageLeft(){
		
	}
	
	public void getImageRight(){
		
	}
	
}
