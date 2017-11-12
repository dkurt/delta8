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
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity implements CvCameraViewListener2 {

  private static final String TAG = "Delta8/Test";
  private final int SERVER_SOCKET_PORT = 43656;
  private OutputStream clientSocketOut = null;
  private CameraBridgeViewBase mOpenCvCameraView;

  public void onCameraViewStarted(int width, int height) {
    new Thread() {
      public void run() {
        try {
          ServerSocket serverSocket = new ServerSocket(SERVER_SOCKET_PORT);
          clientSocketOut = serverSocket.accept().getOutputStream();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }.start();
  }

  private void sendImg(Mat img) {
    int rows = img.rows();
    int cols = img.cols();
    int channels = img.channels();
    byte[] imgData = new byte[rows * cols * channels];
    img.get(0, 0, imgData);

    byte[] data = ByteBuffer.allocate(4 * 3)
            .order(ByteOrder.nativeOrder())
            .putInt(rows).putInt(cols).putInt(channels)
            .array();
    try {
      clientSocketOut.write(data);
      clientSocketOut.write(imgData);
      clientSocketOut.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
    // Get a new frame.
    Mat frame = inputFrame.rgba();
    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);

    if (clientSocketOut != null) {
      // Send to client.
      sendImg(frame);
    }
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
