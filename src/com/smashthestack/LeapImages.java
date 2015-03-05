package com.smashthestack;

import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Mat;

import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Image;
import com.leapmotion.leap.ImageList;
import com.leapmotion.leap.Controller;

public class LeapImages {

	protected static final int IMAGE_WIDTH = 640;
	protected static final int IMAGE_HEIGHT = 240;
	protected static final int LEFT_IMAGE_ID = 0;
	protected static final int RIGHT_IMAGE_ID = 1;
	public static final String LEFT_IMAGE_KEY = "LEFT_IMAGE";
	public static final String RIGHT_IMAGE_KEY = "RIGHT_IMAGE";

	private ImageList currentImages = null;

	private Controller leapController;

	public LeapImages(Controller controller) {
		// TODO Auto-generated constructor stub
		this.leapController = controller;
	}

	public Map<String, Mat> getImages() {
		Map<String, Mat> images = new HashMap<>();
		// isLeapInitialised();

		if (this.currentImages == null) {
			this.moveToNextValidFrame();
		}

		for (int i = 0; i < this.currentImages.count(); ++i) {

			switch (this.currentImages.get(i).id()) {

			case LeapImages.LEFT_IMAGE_ID:
				images.put(LEFT_IMAGE_KEY,
						LeapImageUtils.convertToMat(this.currentImages.get(i)));
				break;

			case LeapImages.RIGHT_IMAGE_ID:
				images.put(RIGHT_IMAGE_KEY,
						LeapImageUtils.convertToMat(this.currentImages.get(i)));
				break;
			}

		}

		return images;
	}

	public Mat getLeftImage() {
		return LeapImageUtils.convertToMat(this
				.getImageById(LeapImages.LEFT_IMAGE_ID));
	}

	public Mat getRightImage() {
		return LeapImageUtils.convertToMat(this
				.getImageById(LeapImages.RIGHT_IMAGE_ID));
	}

	private Image getImageById(int id) {
		for (Image image : this.currentImages) {
			if (image.id() == id) {
				return image;
			}
		}
		return null;
	}

	public void moveToNextValidFrame() {
		this.currentImages = this.leapController.images();
		while (!this.currentImages.get(0).isValid()) {
			this.currentImages = this.leapController.images();
		}
	}

}
