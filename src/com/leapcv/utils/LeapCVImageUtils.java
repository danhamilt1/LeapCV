package com.leapcv.utils;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Controller.PolicyFlag;
import com.leapmotion.leap.Image;
import com.leapmotion.leap.Vector;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.Map;


/**
 * Utility class for images.
 */
public class LeapCVImageUtils {
    public static final int IMAGE_WIDTH = 640;
    public static final int IMAGE_HEIGHT = 240;
    public static final String LEFT_IMAGE_KEY = "LEFT_IMAGE";
    public static final String RIGHT_IMAGE_KEY = "RIGHT_IMAGE";
    public static final String X_MAT_KEY = "X";
    public static final String Y_MAT_KEY = "Y";

    //private Controller leapController = null;

    public LeapCVImageUtils() {

    }

//    /**
//     * Initialize leap motion controller and wait for the first valid frame to be received
//     */
//    public void initLeap() {
//        this.leapController = new Controller();
//        this.leapController.setPolicy(PolicyFlag.POLICY_IMAGES);
//
//		/* Wait for some valid frames to come through */
//        while (!this.leapController.frame().isValid()) {
//            System.out.println("initializing...");
//        }
//    }

    /**
     * Convert a leap type {@link Image} to an OpenCV {@link Mat}
     *
     * @param image of type {@link Image}
     * @return {@link Mat} of original image,
     */
    public static Mat convertToMat(Image image) {
        Mat convertedImage;
        convertedImage = new Mat(image.height(), image.width(), CvType.CV_8UC1);
        convertedImage.put(0, 0, image.data());

        if (convertedImage.width() == 0) {
            convertedImage = null;
        }

        return convertedImage;
    }

    /**
     * Initialise the distortion matrices for use with OpenCV {@link org.opencv.imgproc.Imgproc} method.
     *
     * @param image The {@link com.leapmotion.leap.Image} which contains the distortion data
     * @return {@code Map<String,Mat>} of X and Y matrix.
     */
    public static Map<String, Mat> initDistortionMat(Image image) {

        Mat tempX = new Mat();
        Mat tempY = new Mat();

        tempX = Mat.ones(image.height(), image.width(), CvType.CV_32F);
        tempY = Mat.ones(image.height(), image.width(), CvType.CV_32F);

        for (int y = 0; y < image.height(); ++y) {
            for (int x = 0; x < image.width(); ++x) {
                Vector input = new Vector((float) x / image.width(), (float) y / image.height(), 0);
                input.setX((input.getX() - image.rayOffsetX()) / image.rayScaleX());
                input.setY((input.getY() - image.rayOffsetY()) / image.rayScaleY());
                Vector pixel = image.warp(input);
                if (pixel.getX() >= 0 && pixel.getX() < image.width() && pixel.getY() >= 0 && pixel.getY() < image.height()) {
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

//    /**
//     * Check if the leap motion controller has been initialized
//     *
//     * @throws InvalidObjectException
//     */
//    private void isLeapInitialised() throws InvalidObjectException {
//        if (this.leapController == null) {
//            throw new InvalidObjectException("Leap motion has not been initialised");
//        }
//    }

    /**
     * Turn a leap motion {@link Image} type into a {@link BufferedImage}
     *
     * @param image - {@link Image}
     * @return {@link BufferedImage}
     */
    public static BufferedImage toBufferedImage(Image image) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        byte[] imagePixels;

        // Get all the pixels
        imagePixels = image.data();

        // Write all of the pixels to the buffer in a new BufferedImage
        BufferedImage bufferedImage = new BufferedImage(image.width(), image.height(), type);
        final byte[] bufferedImagePixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        System.arraycopy(imagePixels, 0, bufferedImagePixels, 0, imagePixels.length);
        return bufferedImage;
    }

    /**
     * Turn a {@link BufferedImage} into a {@link WritableImage}, useful for displaying in JavaFX
     *
     * @param image - {@link BufferedImage}
     * @return {@link WritableImage}
     */
    public static WritableImage toWritableImage(BufferedImage image) {
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
     * Turn a {@link Mat} into a {@link javafx.scene.image.Image}, useful for displaying in JavaFX
     */
    public static javafx.scene.image.Image matToWritableImage(Mat image, double width, double height) {
        MatOfByte byteMat = new MatOfByte();

        Imgproc.resize(image, image, new Size(width, height));

        Imgcodecs.imencode(".bmp", image, byteMat);

        return new javafx.scene.image.Image(new ByteArrayInputStream(byteMat.toArray()));
    }

    public static Mat gaussianBlur(Mat image) {
        double sigmaX = 3;
        Imgproc.GaussianBlur(image, image, new Size(11, 11), sigmaX);
        return image;
    }

    public static Mat medianBlur(Mat image) {
        Imgproc.medianBlur(image, image, 5);
        return image;
    }

    /**
     * Crops even percentage from each side of an image
     *
     * @param image The {@link com.leapmotion.leap.Image} to be cropped.
     * @param percentageCrop The percentage to which the image passed in should be cropped. Between 0 and 1.
     * @return {@link org.opencv.core.Mat}
     */
    public static Mat crop(Mat image, double percentageCrop) {
        int width;
        int height;
        int x;
        int y;
        Mat newImage = image;

        width = newImage.cols();
        height = newImage.rows();

        //System.out.println("width: " + width + " height: " + height);

        x = (int) (width * percentageCrop);
        y = (int) (height * percentageCrop);
        width = width - x * 2;
        height = height - y * 2;

        //System.out.println(" x: " + x + " y: " + y + " width: " + width + " height: " + height);

        Rect roi = new Rect(x, y, width, height);
        return newImage.submat(roi);
    }

    public static Mat denoise(Mat image, double denoisingFactor) {
        Mat newImage = new Mat();

        Photo.fastNlMeansDenoising(image, newImage);

        return newImage;
    }
}
