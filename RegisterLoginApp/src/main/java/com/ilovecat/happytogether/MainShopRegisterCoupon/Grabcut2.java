package com.ilovecat.happytogether.MainShopRegisterCoupon;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.ilovecat.happytogether.R;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 08. 06. happytogether
 */
public class Grabcut2 extends Activity {
  ImageView iv;
  Bitmap bitmap;
  Canvas canvas;
  Scalar color = new Scalar(255, 0, 0, 255);
  Point tl, br;
  int counter;
  Bitmap bitmapResult, bitmapBackground;
  Mat dst = new Mat();
  final String pathToImage  = Environment.getExternalStorageDirectory()+"/gcut.png";
  public static final String TAG = "Grabcut_Step1 demo";
  static {
    if (!OpenCVLoader.initDebug()) {
      // Handle initialization error
    }
  }
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.grabcut_main);
    iv = (ImageView) this.findViewById(R.id.imgDisplay);


    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sul);
    Log.d(TAG, "bitmap: " + bitmap.getWidth() + "x" + bitmap.getHeight());


    bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
    Log.d(TAG, "bitmap 8888: " + bitmap.getWidth() + "x" + bitmap.getHeight());


    //GrabCut part
    Mat img = new Mat();
    Utils.bitmapToMat(bitmap, img);
    Log.d(TAG, "img: " + img);

    int r = img.rows();
    int c = img.cols();

    Point p1 = new Point(c/10, r/10);
    Point p2 = new Point(c-c/10, r-r/16);

    Rect rect = new Rect(p1,p2);
    //Rect rect = new Rect(50,30, 100,200);
    Log.d(TAG, "rect: " + rect);

    Mat mask = new Mat();
    debugger(""+mask.type());
    mask.setTo(new Scalar(125));
    Mat fgdModel = new Mat();
    fgdModel.setTo(new Scalar(255, 255, 255));
    Mat bgdModel = new Mat();
    bgdModel.setTo(new Scalar(255, 255, 255));

    Mat imgC3 = new Mat();
    Imgproc.cvtColor(img, imgC3, Imgproc.COLOR_RGBA2RGB);
    Log.d(TAG, "imgC3: " + imgC3);

    Log.d(TAG, "Grabcut_Step1 begins");
    Imgproc.grabCut(imgC3, mask, rect, bgdModel, fgdModel, 5, Imgproc.GC_INIT_WITH_RECT);

    Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(3.0));


    Core.compare(mask, source, mask, Core.CMP_EQ);
    Mat foreground = new Mat(img.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
    img.copyTo(foreground, mask);
    Imgproc.rectangle(img, p1, p2, color);

    Mat background = new Mat();
    try {
      background = Utils.loadResource(getApplicationContext(),
          R.drawable.wall2 );
    } catch (IOException e) {

      e.printStackTrace();
    }
    Mat tmp = new Mat();
    Imgproc.resize(background, tmp, img.size());

    background = tmp;

    Mat tempMask = new Mat(foreground.size(), CvType.CV_8UC1, new Scalar(255, 255, 255));
    Imgproc.cvtColor(foreground, tempMask, 6/* COLOR_BGR2GRAY */);
    //Imgproc.threshold(tempMask, tempMask, 254, 255, 1 /* THRESH_BINARY_INV */);

    Mat vals = new Mat(1, 1, CvType.CV_8UC3, new Scalar(0.0));
    dst = new Mat();
    background.setTo(vals, tempMask);
    Imgproc.resize(foreground, tmp, mask.size());
    foreground = tmp;




    Core.add(background, foreground, dst, tempMask);

    //convert to Bitmap
    Log.d(TAG, "Convert to Bitmap");
    Utils.matToBitmap(dst, bitmap);

    iv.setBackgroundResource(R.drawable.wall2);
    iv.setImageBitmap(bitmap);
    //release MAT part
    img.release();
    imgC3.release();
    mask.release();
    fgdModel.release();
    bgdModel.release();

  }

  public void debugger(String s){
    Log.v("","########### "+s);
  }
}