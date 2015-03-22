package com.leapcv;

import org.opencv.core.Core;

import java.io.InvalidObjectException;

public class Main {
    private static LeapCVImageUtils util;

    public static void main(String[] args) throws InvalidObjectException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        LeapCVController leapController = new LeapCVController();
        try {
            leapController.nextValidFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LeapCVStereoCalib sc = new LeapCVStereoCalib(leapController.getCameras().get(0), leapController.getCameras().get(1));
        sc.findChessboardCorners();
        //sc.addChessboardCorners();
        try {
            leapController.nextValidFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sc.findChessboardCorners();
        sc.calibrateLeapCameras();

        //Highgui.imencode(".bmp", images.get(0), byteMat);

        //System.out.println(images.get(0).dump());

    }
}
