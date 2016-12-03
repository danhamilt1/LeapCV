
#LeapCV

LeapCV is an open source framework that aims to bridge the gap between developers of Leap Motion Controller applications and OpenCV. The idea is to make it easier for someone that wants to get into computer vision, to do so.

The Leap Motion Controller is made up of two stereoscopic fisheye lens cameras. Leap Motion have now allowed camera raw image access to their LeapSDK meaning that frameworks such as LeapCV can be created.

This framework is still very young, having been born out of a university undergraduates final year project. It is believed that it might be useful to someone though!



###OpenCV

This has been built and "tested" using  version 2.4.10 of OpenCV. I did build against version 3 and a few things had changed but didn't get a chance to fix.

You will need to install OpenCV WITH it's jars and then point your project at the jar library and the native libraries,

###Leap Motion Developer Tools

This has been built and tested using version 2.2.3 of the LeapSDK. 

You will need the Leap Motion SDK and point your project at the jar and the native libraries for the SDK.



I haven't had time to work on this for ages and I know it is really bad... For an example of getting this one running have a look at: https://github.com/danhamilt1/LeapCVTestHarness
