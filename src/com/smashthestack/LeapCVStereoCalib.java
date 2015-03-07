package com.smashthestack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

import com.smashthestack.LeapCVCamera;


public class LeapCVStereoCalib {

	private List<LeapCVCamera> cameras = null;
	private Map<Integer, List<Mat>> objectPoints = null;
	private Map<Integer, Mat> corners = null;
	
	public LeapCVStereoCalib(LeapCVCamera left, LeapCVCamera right) {
		this.cameras = new ArrayList<>();
		this.objectPoints = new HashMap<>();
		this.corners = new HashMap<>();
		
		this.cameras.add(left);
		this.cameras.add(right);
	}
	
	public void findChessboardCorners(){
		for (LeapCVCamera camera : cameras) {
			Mat image = new Mat();
			camera.getImageUndistorted().copyTo(image);	
			Mat corners = new MatOfPoint2f();
			Size size = new Size(9,6);
			boolean cornersFound = Calib3d.findChessboardCorners(image, size, (MatOfPoint2f)corners);
			Calib3d.drawChessboardCorners(image, size, (MatOfPoint2f)corners, cornersFound);
			//Highgui.imwrite("chess_"+image.toString()+".png", image);
			this.corners.put(camera.getSide().getSideId(), corners);
		}
	}
	
	public void addChessboardCorners(){
		for(Integer side : this.corners.keySet()){
			if(this.objectPoints.get(side) == null){
				this.objectPoints.put(side, new ArrayList<>());
			}
			System.out.println(this.corners.get(side).dump());
			this.objectPoints.get(side).add(this.corners.get(side).reshape(-1,2));
			for(Mat mat : this.objectPoints.get(side)){
				System.out.println(mat.dump());
			}
		}
		
		
	}

	
	
}
