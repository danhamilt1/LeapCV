package com.leapcv.utils;

import org.opencv.calib3d.Calib3d;
import org.opencv.calib3d.StereoBM;
import org.opencv.calib3d.StereoSGBM;
import org.opencv.contrib.StereoVar;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class LeapCVStereoUtils {

    private StereoVar stereo = null;
    private StereoSGBM stereo2 = null;

    private final double[][] tQ = {{1.0, 0.0, 0.0, -5.0638e+02},
            {0.0, 1.0, 0.0, -2.3762e+02},
            {0.0, 0.0, 0.0, 1.3476e+03},
            {0.0, 0.0, 6.9349981e-01, 3.503271}};

    private int type = CvType.CV_8UC1;

    public static LeapCVStereoMatcher createMatcher(LeapCVMatcherType matcherType){
        return LeapCVStereoMatcherFactory.create(matcherType);
    }

    public LeapCVStereoUtils() {
        this.stereo = new StereoVar();
        this.stereo2 = new StereoSGBM(-64,
                16,
                11,
                2500,
                3000,
                100,
                0,
                0,
                0,
                0,
                true);
    }

    public Mat getPointCloud(Mat disparityMap) {

        Mat pointCloud = MatOfPoint3.zeros(0, 0, type);
        Mat Q = Mat.zeros(4, 4, CvType.CV_32F);

        for (int i = 0; i < tQ.length; ++i)
            Q.put(i, 0, tQ[i]);

        Calib3d.reprojectImageTo3D(disparityMap, pointCloud, Q);

        return pointCloud;
    }

    public void savePointCloud(Mat pointCloud, File destination) {
        try {
            // open the file for writing to (.obj file)
            File output = destination;
            if (!output.exists())
                output.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(output);
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
            System.out.println(pointCloud.size().width);
            System.out.println(pointCloud.size().height);

            for (int w = 0; w < pointCloud.size().width; w++) {
                for (int h = 0; h < pointCloud.size().height; h++) {

                    //  Get XYZ from pointCloud
                    double[] values = pointCloud.get(h, w);
                    if (values != null)
                        //  Append XYZ to file
                        if (values.length >= 3)
                            writer.append("v " + String.valueOf(values[0]) + " " + String.valueOf(values[1]) + " " + String.valueOf(values[2]) + " " + "\n");
                }
            }
            writer.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
