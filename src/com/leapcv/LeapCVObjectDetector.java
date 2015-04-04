package com.leapcv;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.*;

import java.util.ArrayList;
import java.util.List;

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
     *
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
     *
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
     *
     * @param matches
     * @return
     */
    private MatOfDMatch removeOutliers(MatOfDMatch matches) {
        MatOfDMatch goodMatches = new MatOfDMatch();
        double maxDist = 0;
        double minDist= 100;

        for (int i = 0; i < matches.rows(); i++) {
            double dist = matches.toList().get(i).distance;
            System.out.println(dist);
            if (dist < minDist)
                minDist = dist;
            if (dist > maxDist)
                maxDist = dist;
        }

        for (int i = 0; i < matches.rows(); i++) {
            if (matches.toList().get(i).distance < 3.5 * minDist) {
                MatOfDMatch goodMatch = new MatOfDMatch(matches.toList().get(i));
                goodMatches.push_back(goodMatch);
            }
        }

        return goodMatches;
    }

    /**
     * Match image features
     *
     * @param left
     * @param right
     * @return
     */
    public Mat match(Mat left, Mat right) {
        MatOfDMatch matches = new MatOfDMatch();
        MatOfKeyPoint leftKeyPoints = getFeatures(left);
        MatOfKeyPoint rightKeyPoints = getFeatures(right);
        Mat leftDescriptors = getFeatureDescriptors(left);
        Mat rightDescriptors = getFeatureDescriptors(right);

        List<MatOfDMatch> matchList = new ArrayList<>();
        matchList.add(matches);

        //  Only try and match if some features have been found
        if ((!leftKeyPoints.empty()) && (!rightKeyPoints.empty())) {
            //  Match the features
            matcher.match(leftDescriptors, rightDescriptors, matches);
        }

        //  Remove any outliers from the matches
        matches = this.removeOutliers(matches);

        //  Prepare keypoints for finding perspective transform
        List<DMatch> matchesList = matches.toList();
        List<KeyPoint> leftKeyPointsList = leftKeyPoints.toList();
        List<KeyPoint> rightKeyPointsList = rightKeyPoints.toList();
        List<Point> objectList = new ArrayList<>();
        List<Point> sceneList = new ArrayList<>();

        for (int i = 0; i < matchesList.size(); i++) {
            objectList.add(rightKeyPointsList.get(matchesList.get(i).trainIdx).pt);
            sceneList.add(leftKeyPointsList.get(matchesList.get(i).queryIdx).pt);
        }

        MatOfPoint2f objects = new MatOfPoint2f();
        MatOfPoint2f scene = new MatOfPoint2f();
        objects.fromList(objectList);
        scene.fromList(sceneList);

        //  Find the perspective transformation between the images
        Mat H = Calib3d.findHomography(objects, scene, Calib3d.RANSAC, 5);
        Mat objectCorners = new Mat(4, 1, CvType.CV_32FC2);
        Mat sceneCorners = new Mat(4, 1, CvType.CV_32FC2);

        //  Initialise perfect square of original image
        objectCorners.put(0, 0, 0, 0);
        objectCorners.put(1, 0, right.cols(), 0);
        objectCorners.put(2, 0, right.cols(), right.rows());
        objectCorners.put(3, 0, 0, right.rows());

        //  Transform the square in objectCorners to match the transformation given in H
        Core.perspectiveTransform(objectCorners, sceneCorners, H);

        //  Draw the matches in both images, with joining line between them, into a new image
        Mat image = new Mat();
        Features2d.drawMatches(left, leftKeyPoints, right, rightKeyPoints, matches, image,
                new Scalar(255, 0, 0), new Scalar(0, 255, 255),
                new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);

        //  Draw square with transformed corners from sceneCorners
        Core.line(image, new Point(sceneCorners.get(0, 0)), new Point(sceneCorners.get(1, 0)), new Scalar(0, 255, 0), 1);
        Core.line(image, new Point(sceneCorners.get(1, 0)), new Point(sceneCorners.get(2, 0)), new Scalar(0, 255, 0), 1);
        Core.line(image, new Point(sceneCorners.get(2, 0)), new Point(sceneCorners.get(3, 0)), new Scalar(0, 255, 0), 1);
        Core.line(image, new Point(sceneCorners.get(3, 0)), new Point(sceneCorners.get(0, 0)), new Scalar(0, 255, 0), 1);


        return image;

    }
}
