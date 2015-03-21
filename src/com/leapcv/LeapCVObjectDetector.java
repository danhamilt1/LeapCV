package com.leapcv;

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
    private DescriptorExtractor extractor;
    private DescriptorMatcher matcher;
    private FeatureDetector featureDetector;

    private Mat matchedImage;

	public LeapCVObjectDetector() {
        this.extractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);
        this.matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
        this.featureDetector = FeatureDetector.create(FeatureDetector.SIFT);

        this.matchedImage = new Mat();

	}

    /**
     * Get image key points
     * @param image
     * @return
     */
	public MatOfKeyPoint getFeatures(Mat image) {
		MatOfKeyPoint keyPts = new MatOfKeyPoint();
		this.featureDetector.detect(image, keyPts);
		return keyPts;
	}

    /**
     * Get image feature descriptors
     * @param image
     * @return
     */
	public Mat getFeatureDescriptors(Mat image) {
		Mat descriptors = new Mat();
		extractor.compute(image, getFeatures(image), descriptors);

		return descriptors;
	}

    /**
     * Remove outliers from matched features
     * @param matches
     * @return
     */
    private MatOfDMatch removeOutliers(MatOfDMatch matches){
        MatOfDMatch goodMatches = new MatOfDMatch();
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

        for (int i = 0; i < matches.rows(); i++) {
            if (matches.toList().get(i).distance < 3.5 * min_dist) {
                MatOfDMatch goodMatch = new MatOfDMatch(matches.toList().get(i));
                goodMatches.push_back(goodMatch);
            }
        }

        return goodMatches;
    }

    /**
     * Match image features
     * @param left
     * @param right
     * @return
     */
	public Mat match(Mat left, Mat right) {
		MatOfDMatch matches = new MatOfDMatch();
		MatOfKeyPoint lKeyPts = getFeatures(left);
		MatOfKeyPoint rKeyPts = getFeatures(right);
		Mat lD = getFeatureDescriptors(left);
		Mat rD = getFeatureDescriptors(right);

		List<MatOfDMatch> matchList = new ArrayList<>();
		matchList.add(matches);

		Mat img = new Mat();
		if ((!lKeyPts.empty()) && (!rKeyPts.empty())) {
            matcher.match(lD, rD, matches);
        }

        matches = this.removeOutliers(matches);

        List<DMatch> matchesList = matches.toList();
		List<KeyPoint> keypoints1List = lKeyPts.toList();
	    List<KeyPoint> keypoints2List = rKeyPts.toList();
	    LinkedList<Point> objList = new LinkedList<Point>();
	    LinkedList<Point> sceneList = new LinkedList<Point>();
		
		for (int i = 0; i < matchesList.size();i++){
	        objList.addLast(keypoints2List.get(matchesList.get(i).trainIdx).pt);
	        sceneList.addLast(keypoints1List.get(matchesList.get(i).queryIdx).pt);
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
	    
		Features2d.drawMatches(left, lKeyPts, right, rKeyPts, matches, img,
				new Scalar(255, 0, 0), new Scalar(0, 255, 255),
				new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);
	    
	    Core.line(img, new Point(scene_corners.get(0,0)), new Point(scene_corners.get(1,0)), new Scalar(0, 255, 0),1);
	    Core.line(img, new Point(scene_corners.get(1,0)), new Point(scene_corners.get(2,0)), new Scalar(0, 255, 0),1);
	    Core.line(img, new Point(scene_corners.get(2,0)), new Point(scene_corners.get(3,0)), new Scalar(0, 255, 0),1);
	    Core.line(img, new Point(scene_corners.get(3,0)), new Point(scene_corners.get(0,0)), new Scalar(0, 255, 0),1);
		


		return img;

	}
}
