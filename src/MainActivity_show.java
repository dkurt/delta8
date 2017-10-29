package org.delta8.testproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CvCameraViewListener2 {

  private static final String TAG = "Delta8/Test";
  private CameraBridgeViewBase mOpenCvCameraView;

  public void onCameraViewStarted(int width, int height) {}

  public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
    // Get a new frame
    Mat frame = inputFrame.rgba();
    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);
    return frame;
  }

  // Initialize OpenCV manager.
  private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
    @Override
    public void onManagerConnected(int status) {
      switch (status) {
        case LoaderCallbackInterface.SUCCESS: {
          Log.i(TAG, "OpenCV loaded successfully");
          mOpenCvCameraView.enableView();
          break;
        }
        default: {
          super.onManagerConnected(status);
          break;
        }
      }
    }
  };

  @Override
  public void onResume() {
    super.onResume();
    OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Set up camera listener.
    mOpenCvCameraView = (CameraBridgeViewBase)findViewById(R.id.CameraView);
    mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
    mOpenCvCameraView.setCvCameraViewListener(this);
  }

  public void onCameraViewStopped() {}
}
