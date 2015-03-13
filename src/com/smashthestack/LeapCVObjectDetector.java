package com.smashthestack;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.gpu.Gpu;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

public class LeapCVObjectDetector {
	public LeapCVObjectDetector(){
		
	}
	
	public static void sift(Mat image){
	FeatureDetector fd = FeatureDetector.create(FeatureDetector.SURF);
		MatOfKeyPoint keyPts = new MatOfKeyPoint();
		
		//List<Mat> images = new ArrayList<>();
		//images.add(image);
		
		//List<MatOfKeyPoint> kps = new ArrayList<>();
		//kps.add(keyPts);
		
		fd.detect(image, keyPts);
		
		Features2d.drawKeypoints(image, keyPts, image, new Scalar( 255, 0, 0 ), Features2d.NOT_DRAW_SINGLE_POINTS);
		
		//Highgui.imwrite("sift.png", image);
	}
}
