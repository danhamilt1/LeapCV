package com.leapcv;

import com.leapcv.LeapCVCamera.CameraSide;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LeapCVStereoCalib {

    private List<LeapCVCamera> cameras = null;
    private List<Mat> objectPoints = null;
    private Map<Integer, List<Mat>> imagePoints = null;
    private Map<Integer, List<Mat>> corners = null;
    private Size patternSize = null;

    public LeapCVStereoCalib(LeapCVCamera left, LeapCVCamera right) {
        this.cameras = new ArrayList<>();
        this.objectPoints = new ArrayList<>();
        this.imagePoints = new HashMap<>();
        this.corners = new HashMap<>();
        this.patternSize = new Size(9, 6);
        this.cameras.add(left);
        this.cameras.add(right);

        System.out.println("Size 1: " + left.getImageUndistorted().size());
        System.out.println("Size 2: " + right.getImageUndistorted().size());
    }

    public void findChessboardCorners() {
        for (LeapCVCamera camera : cameras) {
            Mat image = new Mat();
            camera.getImageUndistorted().copyTo(image);
            //camera.getCurrentImage().getImageAsMat().copyTo(image);;

            Mat corners = new MatOfPoint2f();
            boolean cornersFound = Calib3d.findChessboardCorners(image, patternSize, (MatOfPoint2f) corners);
            Calib3d.drawChessboardCorners(image, patternSize, (MatOfPoint2f) corners, cornersFound);
            Highgui.imwrite("chess_" + image.toString() + ".png", image);

            if (this.corners.get(camera.getSide().getSideId()) == null) {
                this.corners.put(camera.getSide().getSideId(), new ArrayList<Mat>());
            }

            this.corners.get(camera.getSide().getSideId()).add(corners);
            this.objectPoints.add(create3dChessboardCorners(patternSize, 3));
            System.out.println(camera.getSide().getSideId());
            System.out.println(cornersFound);
        }
    }

    public Mat create3dChessboardCorners(Size boardSize, float squareSize) {
        MatOfPoint3f corners = new MatOfPoint3f();

        for (int i = 0; i < boardSize.height; ++i) {
            for (int j = 0; j < boardSize.width; ++j) {
                corners.push_back(new MatOfPoint3f(new Point3(j * squareSize, i * squareSize, 0)));
            }
        }
        return corners;
    }

    public void calibrateLeapCameras() {
        Mat rotMat = new Mat();
        Mat fundMat = new Mat();
        Mat esMat = new Mat();
        Mat tranMat = new Mat();
        Mat cMatL = new Mat();//Mat.eye(3, 3, CvType.CV_8UC1);
        Mat dCoffL = new Mat();
        Mat cMatR = new Mat();//Mat.eye(3, 3, CvType.CV_8UC1);
        Mat dCoffR = new Mat();

        TermCriteria tc = new TermCriteria(TermCriteria.MAX_ITER + TermCriteria.EPS, 100, 1e-5);

        System.out.println(this.corners.get(CameraSide.LEFT.getSideId()).size());
        System.out.println(this.corners.get(CameraSide.LEFT.getSideId()).get(0).size());
        System.out.println(this.corners.get(CameraSide.LEFT.getSideId()).get(1).size());
        System.out.println(this.corners.get(CameraSide.RIGHT.getSideId()).size());
        System.out.println(this.corners.get(CameraSide.RIGHT.getSideId()).get(0).size());
        System.out.println(this.corners.get(CameraSide.RIGHT.getSideId()).get(1).size());

        System.out.println(this.objectPoints.size());

        for (LeapCVCamera leapCVCamera : cameras) {
            for (Mat list : this.corners.get(leapCVCamera.getSide().getSideId())) {
                System.out.println(list.toString());
                System.out.println(list.dump());
            }
        }

        Calib3d.stereoCalibrate(objectPoints,
                this.corners.get(CameraSide.RIGHT.getSideId()),
                this.corners.get(CameraSide.LEFT.getSideId()),
                cMatL,
                dCoffL,
                cMatR,
                dCoffR,
                cameras.get(0).getImageUndistorted().size(),
                rotMat,
                tranMat,
                esMat,
                fundMat,
                tc,
                Calib3d.CALIB_FIX_ASPECT_RATIO +
                        Calib3d.CALIB_ZERO_TANGENT_DIST +
                        Calib3d.CALIB_SAME_FOCAL_LENGTH +
                        Calib3d.CALIB_RATIONAL_MODEL);

        //Calib3d.s
    }


}
