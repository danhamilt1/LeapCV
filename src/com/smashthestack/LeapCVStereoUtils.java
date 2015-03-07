package com.smashthestack;

import org.opencv.calib3d.Calib3d;
import org.opencv.calib3d.StereoBM;
import org.opencv.calib3d.StereoSGBM;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class LeapCVStereoUtils {
	
	private StereoSGBM stereo = null;
    private int minDisparity = 0;
    private int numDisparities = 16;
    private int SADWindowSize = 7;
    private int P1 = 322412;//8*numChannels*SADWindowSize*SADWindowSize;
    private int P2 = 529456;//32*numChannels*SADWindowSize*SADWindowSize;
    private int disp12MaxDiff = 100;
    private int preFilterCap = 0;
    private int uniquenessRatio = 8;
    private int speckleWindowSize = 0; 
    private int speckleRange = 8;
    private boolean fullDp = true;
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
        		fullDp);

	}
	
	public Mat getDisparityMap(Mat left, Mat right){
		//self.d2d = np.array([[1.0,0.,0.,-5.0638e+02],[0.,1.,0.,-2.3762e+02],[0.,0.,0.,1.3476e+03],[0.,0.,6.9349981e-01,3.503271]])	#depth-to-dist mapping
		Mat d2d = new Mat(4,4,type);
		//d2d.pu
		//Calib3d.stereoCalibrate(objectPoints, imagePoints1, imagePoints2, cameraMatrix1, distCoeffs1, cameraMatrix2, distCoeffs2, imageSize, R, T, E, F)
		Mat disp = new Mat(0,0,type);
		
		stereo.compute(left,right,disp);
		return disp;
	}
	
	public void getImageLeft(){
		
	}
	
	public void getImageRight(){
		
	}
	
}
