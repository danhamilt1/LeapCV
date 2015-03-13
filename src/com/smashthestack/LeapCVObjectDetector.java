package com.smashthestack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.gpu.Gpu;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

public class LeapCVObjectDetector {
	public LeapCVObjectDetector() {

	}

	public static MatOfKeyPoint getFeatures(Mat image) {
		FeatureDetector fd = FeatureDetector.create(FeatureDetector.SURF);
		MatOfKeyPoint keyPts = new MatOfKeyPoint();
		// List<Mat> images = new ArrayList<>();
		// images.add(image);

		// List<MatOfKeyPoint> kps = new ArrayList<>();
		// kps.add(keyPts);
		fd.detect(image, keyPts);

		// Features2d.drawKeypoints(image, keyPts, image, new Scalar( 255, 0, 0
		// ), Features2d.NOT_DRAW_SINGLE_POINTS);
		// System.out.println(keyPts.dump());
		// Highgui.imwrite("sift.png", image);
		return keyPts;
	}

	public static Mat getFeatureDescriptors(Mat image) {
		Mat descriptors = new Mat();

		DescriptorExtractor extractor = DescriptorExtractor
				.create(DescriptorExtractor.SURF);

		extractor.compute(image, getFeatures(image), descriptors);

		return descriptors;
	}

	public static Mat match(Mat left, Mat right) {
		//Imgproc.GaussianBlur(left, left, new Size(15, 15), 2);
		//Imgproc.GaussianBlur(right, right, new Size(15, 15), 2);
		DescriptorMatcher matcher = DescriptorMatcher
				.create(DescriptorMatcher.FLANNBASED);
		MatOfDMatch matches = new MatOfDMatch();
		MatOfKeyPoint lKeyPts = getFeatures(left);
		MatOfKeyPoint rKeyPts = getFeatures(right);
		Mat lD = getFeatureDescriptors(left);
		Mat rD = getFeatureDescriptors(right);

		List<MatOfDMatch> matchList = new ArrayList<>();
		matchList.add(matches);

		Mat img = new Mat();// 480,1280,CvType.CV_8UC3);
		if ((!lKeyPts.empty()) && (!rKeyPts.empty())) {
			matcher.match(lD, rD, matches);

			// System.out.println(img.toString());
			// Highgui.imwrite("match.png", img);
		}

		double max_dist = 0;
		double min_dist = 100;

		for (int i = 0; i < matches.rows(); i++) {
			double dist = matches.toList().get(i).distance;
			System.out.println(dist);
			if (dist < min_dist)
				min_dist = dist;
			if (dist > max_dist)
				max_dist = dist;
		}

		MatOfDMatch goodMatches = new MatOfDMatch();

		for (int i = 0; i < matches.rows(); i++) {
			if (matches.toList().get(i).distance < 2.75 * min_dist) {
				MatOfDMatch goodMatch = new MatOfDMatch(matches.toList().get(i));
				goodMatches.push_back(goodMatch);
			}
		}

		System.out.println(max_dist + " " + min_dist);

		
		List<KeyPoint> keypoints1_List = lKeyPts.toList();
	    List<KeyPoint> keypoints2_List = rKeyPts.toList();
	    LinkedList<Point> objList = new LinkedList<Point>();
	    LinkedList<Point> sceneList = new LinkedList<Point>();
		
		for (int i = 0; i < goodMatches.toList().size();i++){
	        objList.addLast(keypoints2_List.get(goodMatches.toList().get(i).trainIdx).pt);
	        sceneList.addLast(keypoints1_List.get(goodMatches.toList().get(i).queryIdx).pt);
		}
		
		MatOfPoint2f obj = new MatOfPoint2f();
	    MatOfPoint2f scene = new MatOfPoint2f();
	    obj.fromList(objList);
	    scene.fromList(sceneList);
		
		
		Mat H = Calib3d.findHomography(obj, scene, Calib3d.RANSAC, 5);
	    Mat tmp_corners = new Mat(4,1,CvType.CV_32FC2);
	    Mat scene_corners = new Mat(4,1,CvType.CV_32FC2);
		
	    tmp_corners.put(0, 0, new double[] {0,0});
	    tmp_corners.put(1, 0, new double[] {right.cols(),0});
	    tmp_corners.put(2, 0, new double[] {right.cols(),right.rows()});
	    tmp_corners.put(3, 0, new double[] {0,right.rows()});
	    
	    Core.perspectiveTransform(tmp_corners,scene_corners, H);
	    
		Features2d.drawMatches(left, lKeyPts, right, rKeyPts, goodMatches, img,
				new Scalar(255, 0, 0), new Scalar(0, 255, 255),
				new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);
	    
	    Core.line(img, new Point(scene_corners.get(0,0)), new Point(scene_corners.get(1,0)), new Scalar(0, 255, 0),4);
	    Core.line(img, new Point(scene_corners.get(1,0)), new Point(scene_corners.get(2,0)), new Scalar(0, 255, 0),4);
	    Core.line(img, new Point(scene_corners.get(2,0)), new Point(scene_corners.get(3,0)), new Scalar(0, 255, 0),4);
	    Core.line(img, new Point(scene_corners.get(3,0)), new Point(scene_corners.get(0,0)), new Scalar(0, 255, 0),4);
		


		return img;

	}
}
