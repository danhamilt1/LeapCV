package com.smashthestack;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import com.leapmotion.leap.Controller;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import com.leapmotion.leap.Image;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Leap;
import com.leapmotion.leap.Vector;
import com.leapmotion.leap.Controller.PolicyFlag;
import com.leapmotion.leap.ImageList;


public class LeapCVImageUtils {
	public static final int IMAGE_WIDTH = 640;
	public static final int IMAGE_HEIGHT = 240;
	public static final String LEFT_IMAGE_KEY = "LEFT_IMAGE";
	public static final String RIGHT_IMAGE_KEY = "RIGHT_IMAGE";
	public static final String X_MAT_KEY = "X";
	public static final String Y_MAT_KEY = "Y";
	
	private Controller leapController = null;
	
	public LeapCVImageUtils(){
		
	}
	
	/**
	 * Initialize leap motion controller and wait for the first valid frame to be received
	 */
	public void initLeap(){
		this.leapController = new Controller();
		this.leapController.setPolicy(PolicyFlag.POLICY_IMAGES);
		
		/* Wait for some valid frames to come through */
		while(!this.leapController.frame().isValid()){
			System.out.println("initializing...");
		}
	}
	
	/**
	 * Convert a leap type {@link Image} to an OpenCV {@link Mat}
	 * @param image of type {@link Image}
	 * @return {@link Mat} of original image
	 */
	public static Mat convertToMat(Image image){
		Mat convertedImage;
		//Mat denoisedImage;
		
		convertedImage = new Mat(image.height(), image.width(), CvType.CV_8UC1);
		//denoisedImage = new Mat();
		
		convertedImage.put(0, 0, image.data());
		//Photo.fastNlMeansDenoising(convertedImage, convertedImage, 5, 7, 5);
		return convertedImage;
	}
	
	/**
	 * Initialise the distortion matrices for use with OpenCV {@link Imgproc.remap} method.
	 * @param image
	 * @return {@code Map<String,Mat>} of X and Y matrix.
	 */
	public static Map<String,Mat> initDistortionMat(Image image){
		
		Mat tempX = new Mat();
		Mat tempY = new Mat();
		
		tempX = Mat.ones(image.height(),image.width(),CvType.CV_32F);
		tempY = Mat.ones(image.height(),image.width(),CvType.CV_32F);
		
		for(int y = 0; y < image.height(); ++y){
			for(int x = 0; x < image.width();++x){
				Vector input = new Vector((float)x/image.width(), (float)y/image.height(), 0);
				input.setX((input.getX() - image.rayOffsetX()) / image.rayScaleX());
		        input.setY((input.getY() - image.rayOffsetY()) / image.rayScaleY());
		        Vector pixel = image.warp(input);
		        if(pixel.getX() >= 0 && pixel.getX() < image.width() && pixel.getY() >= 0 && pixel.getY() < image.height()) {
		        	tempX.put(y, x, pixel.getX());
		        	tempY.put(y, x, pixel.getY());
		        } else {
		        	tempX.put(y, x, -1);
		        	tempY.put(y, x, -1);
		        }
		        
			}
		}
		
		Map<String, Mat> retVal = new HashMap<>();
		retVal.put(X_MAT_KEY, tempX);
		retVal.put(Y_MAT_KEY, tempY);
		
		return retVal;
	}
	
	/**
	 * Check if the leap motion controller has been initialized
	 * @throws InvalidObjectException
	 */
	private void isLeapInitialised() throws InvalidObjectException{
		if(this.leapController == null){
			throw new InvalidObjectException("Leap motion has not been initialised");			
		}
	}
	
	/**
	 * Turn a leap motion {@link Image} type into a {@link BufferedImage}
	 * @param toProcess - {@link Image}
	 * @return {@link BufferedImage}
	 */
    public static BufferedImage toBufferedImage(Image toProcess){
        int type = BufferedImage.TYPE_BYTE_GRAY;
        int bufferSize = toProcess.width()*toProcess.height()*toProcess.bytesPerPixel();
        byte [] b = new byte[bufferSize];
        b = toProcess.data();// get all the pixels
        
        BufferedImage bufferedImage = new BufferedImage(640,240, type);
        final byte[] targetPixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);  
        return bufferedImage;
    }
    
    /**
     * Turn a {@link BufferedImage} into a {@link WritableImage}, useful for displaying in JavaFX
     * @param image - {@link BufferedImage}
     * @return {@link WritableImage}
     */
    public static WritableImage toWritableImage(BufferedImage image){
    	//BufferedImage image = toBufferedImage(toProcess);
    	WritableImage wImage = new WritableImage(image.getWidth(), image.getHeight());
    	PixelWriter pw = wImage.getPixelWriter();
    	for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                pw.setArgb(x, y, image.getRGB(x, y));
            }
        }
    	return wImage;
    }
    
    /**
     * Turn a {@link Mat} into a {@link WritableImage}, useful for displaying in JavaFX
     * @param image - {@link Mat}
     * @return {@link WritableImage}
     */
    public static javafx.scene.image.Image matToWritableImage(Mat image){
    	MatOfByte byteMat = new MatOfByte();
    	Highgui.imencode(".bmp", image, byteMat);
    	return new javafx.scene.image.Image(new ByteArrayInputStream(byteMat.toArray()));
    }
}
