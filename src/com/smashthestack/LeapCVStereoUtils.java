package com.smashthestack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.opencv.calib3d.Calib3d;
import org.opencv.calib3d.StereoBM;
import org.opencv.calib3d.StereoSGBM;
import org.opencv.contrib.StereoVar;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint3;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class LeapCVStereoUtils {
	
	public double getCycle(){
		return cycle;
	}
	
	public void setCycle(double cycle){
		this.cycle = cycle;
	}
	
	public double getLevels() {
		return levels;
	}

	public void setLevels(double levels) {
		this.levels = levels;
	}

	public double getPyrScale() {
		return pyrScale;
	}

	public void setPyrScale(double pyrScale) {
		this.pyrScale = pyrScale;
	}

	public double getPolyN() {
		return polyN;
	}

	public void setPolyN(double polyN) {
		this.polyN = polyN;
	}

	public double getPolySigma() {
		return polySigma;
	}

	public void setPolySigma(double polySigma) {
		this.polySigma = polySigma;
	}

	public double getFi() {
		return fi;
	}

	public void setFi(double fi) {
		this.fi = fi;
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	public double getMinDisp() {
		return minDisp;
	}

	public void setMinDisp(double minDisp) {
		this.minDisp = minDisp;
	}

	public double getMaxDisp() {
		return maxDisp;
	}

	public void setMaxDisp(double maxDisp) {
		this.maxDisp = maxDisp;
	}

	private StereoVar stereo = null;

    private double levels = 3;
    private double pyrScale = 0.25;
    private double polyN = 7;
    private double polySigma = 1.5;
    private double fi = 1;
    private double lambda = 1;
    private double minDisp = 0;
    private double maxDisp = 60;
    private double cycle = 10;
    
    private int type = CvType.CV_8UC1;
    
	public LeapCVStereoUtils(){
		this.stereo = new StereoVar();

	}
	
	public Mat getDisparityMap(Mat left, Mat right){
		double[][] td2d = {{1.0,0.0,0.0,-5.0638e+02},{0.0,1.0,0.0,-2.3762e+02},{0.0,0.0,0.0,1.3476e+03},{0.0,0.0,6.9349981e-01,3.503271}};
		//self.d2d = np.array([[1.0,0.,0.,-5.0638e+02],[0.,1.,0.,-2.3762e+02],[0.,0.,0.,1.3476e+03],[0.,0.,6.9349981e-01,3.503271]])	#depth-to-dist mapping
		Mat d2d = new Mat(4,4,CvType.CV_32F);
		Mat disp = new Mat(0,0,type);
		Mat dImg = new MatOfPoint3();
		
		for(int i = 0; i < td2d.length; ++i)
			d2d.put(i,0,td2d[i]);
		
		System.out.println(d2d.dump());

		stereo.set_levels((int)this.levels);
		stereo.set_pyrScale(this.pyrScale);
		stereo.set_nIt((int)this.cycle);
		//stereo.set_cycle((int)this.cycle);
		stereo.set_penalization(StereoVar.PENALIZATION_TICHONOV );
		//stereo.set_cycle(StereoVar.CYCLE_V);
		//stereo.set_flags(StereoVar.USE_AUTO_PARAMS);
		stereo.set_poly_n((int)this.polyN);
		stereo.set_poly_sigma(this.polySigma);
		stereo.set_fi((float)this.fi);
		stereo.set_lambda((float)this.lambda);
		stereo.set_minDisp((int)this.minDisp);
		stereo.set_maxDisp((int)this.maxDisp);
		
		stereo.compute(right,left,disp);
		Core.normalize(disp, disp, 0, 255, Core.NORM_MINMAX);
		
		Calib3d.reprojectImageTo3D(disp, dImg, d2d);
		
		System.out.println(disp.size());
		System.out.println(dImg.size());
		
		//System.out.println(dImg.dump());
		savePointCloud(dImg);
		Highgui.imwrite("out.bmp", dImg);
		
		return disp;
	}
	
	private void savePointCloud(Mat pcl){
	    //publishProgress("Saving .obj file");
	    try {
	        // open the file for writing to (.obj file)
	        File datei = new File("poincloud.obj");
	        if(!datei.exists())
	                datei.createNewFile();

	        FileOutputStream outStream = new FileOutputStream(datei);
	        OutputStreamWriter oStream = new OutputStreamWriter(outStream);

	        for(int w = 0; w < pcl.size().width; w++){
	            for(int h = 0; h < pcl.size().height; h++){
	                double[] values = new double[1];
	                values = pcl.get(w, h);
	                if(values != null)
	                //    if(values.length >= 3)
	                        oStream.append("v " + String.valueOf(values[0]) + " " +String.valueOf(values[1]) + " " +String.valueOf(values[2]) + " " +  "\n");
	            }
	        }
	        oStream.close();
	        outStream.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }


	}
	
}
