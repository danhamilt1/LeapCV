package com.leapcv.utils;

import com.leapcv.LeapCVCamera;
import com.leapcv.LeapCVCamera.CameraSide;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Utility class for camera calibration
 */
public class LeapCVCalibrationUtils {

    private List<LeapCVCamera> cameras = null;
    private List<Mat> objectPoints = null;
    private Map<Integer, List<Mat>> imagePoints = null;
    private Map<Integer, List<Mat>> corners = null;
    private Size patternSize = null;

    public LeapCVCalibrationUtils(LeapCVCamera left, LeapCVCamera right) {
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

    /**
     * Find the corners of a 9x6 chessboard
     */
    public void findChessboardCorners() {
        for (LeapCVCamera camera : cameras) {
            Mat image = new Mat();
            camera.getImageUndistorted().copyTo(image);

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

    /**
     * Draw corners on the chessboard in an image
     * @param image
     * @return
     */
    public static Mat getImageWithChessboard(Mat image) {
            Size patternSize = new Size(9,6);
            Mat corners = new MatOfPoint2f();
            Mat chessImage = Mat.zeros(image.size(), image.type());

            image.copyTo(chessImage);

            boolean cornersFound = Calib3d.findChessboardCorners(chessImage, patternSize, (MatOfPoint2f) corners);
            Imgproc.cornerSubPix(chessImage, (MatOfPoint2f)corners, new Size(11,11), new Size(-1,-1), new TermCriteria(TermCriteria.MAX_ITER,30,0.1));

            Imgproc.cvtColor(image,chessImage,Imgproc.COLOR_GRAY2RGB);
            Calib3d.drawChessboardCorners(chessImage, patternSize, (MatOfPoint2f) corners, cornersFound);
            return chessImage;
    }

    /**
     * Generate Mat based on size of chessboard
     * @param boardSize size of the chessboard
     * @param squareSize size of the chessboard square sides in cm
     * @return {@link org.opencv.core.Mat}
     */
    public Mat create3dChessboardCorners(Size boardSize, float squareSize) {
        MatOfPoint3f corners = new MatOfPoint3f();

        for (int i = 0; i < boardSize.height; ++i) {
            for (int j = 0; j < boardSize.width; ++j) {
                corners.push_back(new MatOfPoint3f(new Point3(j * squareSize, i * squareSize, 0)));
            }
        }
        return corners;
    }

    /**
     * Carry out calibration procedure on the leap motion cameras
     */
    public void calibrateLeapCameras() {
        Mat rotationMatrix = new Mat();
        Mat fundamentalMatrix = new Mat();
        Mat essentialMatrix = new Mat();
        Mat tranMat = new Mat();
        Mat leftCameraMat = new Mat();
        Mat leftCameraCoeffs = new Mat();
        Mat rightCameraMat = new Mat();
        Mat rightCameraCoeffs = new Mat();

        //  Criteria for calibration
        TermCriteria tc = new TermCriteria(TermCriteria.MAX_ITER + TermCriteria.EPS, 100, 1e-5);

        //  Calibrate cameras
        Calib3d.stereoCalibrate(objectPoints,
                this.corners.get(CameraSide.RIGHT.getSideId()),
                this.corners.get(CameraSide.LEFT.getSideId()),
                leftCameraMat,
                leftCameraCoeffs,
                rightCameraMat,
                rightCameraCoeffs,
                cameras.get(0).getImageUndistorted().size(),
                rotationMatrix,
                tranMat,
                essentialMatrix,
                fundamentalMatrix,
                tc,
                Calib3d.CALIB_FIX_ASPECT_RATIO +
                        Calib3d.CALIB_ZERO_TANGENT_DIST +
                        Calib3d.CALIB_SAME_FOCAL_LENGTH +
                        Calib3d.CALIB_RATIONAL_MODEL);

    }

}
