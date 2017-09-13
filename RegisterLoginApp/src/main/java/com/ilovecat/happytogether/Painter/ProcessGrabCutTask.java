package com.ilovecat.happytogether.Painter;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.ilovecat.happytogether.MainShopRegisterCoupon.MainRegisterCouponShopStep1;
import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.StickerClass.StickerShopLogoImageView;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 08. 09. happytogether
 */
public class ProcessGrabCutTask extends AsyncTask<Integer, Integer, Integer> {

  private ProgressDialog dlg = new ProgressDialog(PainterActivity.appcActivity);

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    dlg.setMessage("Processing Image...");
    dlg.setCancelable(false);
    dlg.setIndeterminate(true);
    dlg.show();
  }

  @Override
  protected Integer doInBackground(Integer... params) {

    // jpg로 변환하여 저장된 스티커 파일 객체
    Mat img = Imgcodecs.imread(PainterActivity.imgPathAndFileName);
    //Mat background = new Mat(img.size(), CvType.CV_8UC3,
    //    new Scalar(255, 255, 255));
    // Mat firstMask = new Mat();

    // 드로잉뷰를 위한 객체
    Mat newMask = Imgcodecs.imread(PainterActivity.maskPathAndFileName, CvType.CV_8UC1);

    // Mat newMask2 = new Mat();
    //Imgproc.cvtColor(newMask, newMask2, Imgproc.GC_PR_FGD);

    convertToOpencvValues(newMask);

    Mat bgModel = new Mat();
    Mat fgModel = new Mat();
    Mat mask;


    // GC_PR_FGD 전경에 속할 수도 있는 화소(직사각형 내부의 화소 초기값)
    Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(Imgproc.GC_PR_FGD));
    Mat dst = new Mat();


    int r = img.rows();
    int c = img.cols();
    Point p1 = new Point(c / 10, r / 10);
    Point p2 = new Point(c - c / 10, r - r / 16);

    Rect rect = new Rect(p1, p2);
    //Rect rect = new Rect(tl, br);


    /*
    Imgproc.grabCut(
        img, // 입력영상
        firstMask, // 분활결과
        rect,  // 전겨을 포함하는 사각형
        bgModel,
        fgModel,
        5, // 반복횟수
        Imgproc.GC_INIT_WITH_RECT //사각형 사용
    );
    */

    Imgproc.grabCut(img, newMask, rect, bgModel, fgModel,
        5, Imgproc.GC_INIT_WITH_MASK);


    convertToHumanValues(newMask); // back to human readable values
    Imgproc.threshold(newMask, newMask, 128, 255, Imgproc.THRESH_TOZERO);

    // foreground 전경객체에서 검은색을 뺀 후 검은색을 보정하기 위한 마스크 객체.
    Mat newMaskForBlack = new Mat();
    newMask.copyTo(newMaskForBlack);

    // foreground 객체를 생성
    Mat foreground = new Mat(newMask.size(), CvType.CV_8UC3,
        new Scalar(0, 0, 0));

    // img 객체에 newMask객체를 foreground 객체에 저굥한다.
    //inputMat.copyTo(outputMat, maskMat);
    img.copyTo(foreground, newMask);
    //img.copyTo(foreground, newMaskForBlack);


    // foreground 객체 크기로 비트맵 생성
    // convert matrix to outputBitmap bitmap
    Bitmap outputBitmap = Bitmap.createBitmap((int) foreground.size().width, (int) foreground.size().height, Bitmap.Config.ARGB_8888);
    Bitmap outputMaskBitmapForBlack = Bitmap.createBitmap((int) foreground.size().width, (int) foreground.size().height, Bitmap.Config.ARGB_8888);


    // foregroud 객체의 색상포맷을 opencv형에서 android형으로 변환한다.
    Mat tmpInvertRGB = new Mat();
    Imgproc.cvtColor(foreground, tmpInvertRGB, Imgproc.COLOR_BGR2RGB);

    // 객체에서 비트맵을 생성한다.
    org.opencv.android.Utils.matToBitmap(tmpInvertRGB, outputBitmap);
    org.opencv.android.Utils.matToBitmap(newMaskForBlack, outputMaskBitmapForBlack);


    // 전경 객체에서 검은색을 뺀다.
    for (int x = 0; x < outputBitmap.getWidth(); x++) {
      for (int y = 0; y < outputBitmap.getHeight(); y++) {
        if (outputBitmap.getPixel(x, y) == Color.BLACK) {
          outputBitmap.setPixel(x, y, Color.TRANSPARENT);
        }
      }
    }

    //검은색 보정용 마스크 객체를 검은색으로 변환한다
    for (int x = 0; x < outputMaskBitmapForBlack.getWidth(); x++) {
      for (int y = 0; y < outputMaskBitmapForBlack.getHeight(); y++) {
        if (outputMaskBitmapForBlack.getPixel(x, y) == Color.BLACK) {
          outputMaskBitmapForBlack.setPixel(x, y, Color.TRANSPARENT);
        }
        if (outputMaskBitmapForBlack.getPixel(x, y) == Color.WHITE) {
          outputMaskBitmapForBlack.setPixel(x, y, Color.rgb(170,170,170));
        }
        if (outputMaskBitmapForBlack.getPixel(x, y) == Color.rgb(170,170,170)) {
          outputMaskBitmapForBlack.setPixel(x, y, Color.BLACK);
        }
      }


    }

    // 전경객체와 검은색보정용 비트맵을 오버레이하여 새로운 비트맵 생성
    Bitmap overlayMarkBitmap = overlayBitmapMaker(outputMaskBitmapForBlack, outputBitmap, 0, 0);

    FileOutputStream outOverLayed = null;
    try {
      // Painter_'yyyy-MM-dd_HH-mm-ss.S'.png.png
      outOverLayed = new FileOutputStream(PainterActivity.maskPathAndFileName + ".png");
      overlayMarkBitmap.compress(Bitmap.CompressFormat.PNG, 100, outOverLayed); // bmp is your Bitmap instance
      // PNG is a lossless format, the compression factor (100) is ignored
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (outOverLayed != null) {
          outOverLayed.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    outputBitmap.recycle();
    outputMaskBitmapForBlack.recycle();
    overlayMarkBitmap.recycle();

    //firstMask.release();
    source.release();
    bgModel.release();
    fgModel.release();
    tmpInvertRGB.release();
    //vals.release();


    return 0;
  }

  @Override
  protected void onPostExecute(Integer result) {
    super.onPostExecute(result);

    Bitmap png = BitmapFactory
        .decodeFile(PainterActivity.maskPathAndFileName + ".png");

    // 바탕 이미지의 실제 가로 길이를 구한다.
    DisplayMetrics dm = PainterActivity.appcActivity.getResources().getDisplayMetrics();
    int width = dm.widthPixels;
    double ratio = (double) width / (double) png.getWidth();
    double heightOfImageView = png.getHeight() * ratio;
    Bitmap mutableBitmap = Bitmap.createScaledBitmap(
        png, width, (int)heightOfImageView, false);
    png.recycle();

    PainterActivity.mDrawingView.setBackgroundColor(Color.parseColor("#00ffffff"));

    //PainterActivity.ivOrgImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    //PainterActivity.ivOrgImage.setAdjustViewBounds(true);
    //PainterActivity.ivOrgImage.setPadding(2, 2, 2, 2);
    PainterActivity.ivOrgImage.setImageBitmap(mutableBitmap);
    PainterActivity.ivOrgImage.invalidate();


    // 드로잉뷰를 안보이게 한다
    PainterActivity.mDrawingView.setVisibility(View.INVISIBLE);
    PainterActivity.toggleVisibleDrawingView.setChecked(true);
    PainterActivity.toggleVisibleDrawingView.setBackgroundDrawable(
        PainterActivity.appcActivity.getResources().
            getDrawable(R.drawable.ic_visibility_white)
    );

    PainterActivity.ivTouchPoint_White.setVisibility(View.INVISIBLE);
    PainterActivity.ivTouchPoint_Black.setVisibility(View.INVISIBLE);
    PainterActivity.mDrawingView.invalidate();



    dlg.dismiss();


    Drawable d = Drawable.createFromPath(PainterActivity.maskPathAndFileName + ".png");

    int sizeOfArray = MainRegisterCouponShopStep1.arrayListStickerShopLogoView.size();
    MainRegisterCouponShopStep1.iv_ShopLogoSticker = (StickerShopLogoImageView) MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(sizeOfArray - 1);

    MainRegisterCouponShopStep1.iv_ShopLogoSticker.setImageDrawable(d);

    MainRegisterCouponShopStep1.iv_ShopLogoSticker.setControlItemsHidden(true);
    MainRegisterCouponShopStep1.iv_ShopLogoSticker.setControlItemsMoveLock(false);

    // 애니메니션 적용
    final float growTo = 0.8f;
    final long duration = 1200;

    ScaleAnimation grow = new ScaleAnimation(1, growTo, 1, growTo,
        Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f);
    grow.setDuration(duration / 2);

    ScaleAnimation shrink = new ScaleAnimation(growTo, 1, growTo, 1,
        Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f);
    shrink.setDuration(duration / 2);
    shrink.setStartOffset(duration / 2);
    AnimationSet growAndShrink = new AnimationSet(true);
    growAndShrink.setInterpolator(new LinearInterpolator());
    growAndShrink.addAnimation(grow);
    growAndShrink.addAnimation(shrink);
    MainRegisterCouponShopStep1.iv_ShopLogoSticker.startAnimation(growAndShrink);


  }

  private static void convertToHumanValues(Mat mask) {
    byte[] buffer = new byte[3];
    for (int x = 0; x < mask.rows(); x++) {
      for (int y = 0; y < mask.cols(); y++) {
        mask.get(x, y, buffer);
        int value = buffer[0];
        if (value == Imgproc.GC_BGD) {
          buffer[0] = 0; // for sure background
        } else if (value == Imgproc.GC_PR_BGD) {
          buffer[0] = 85; // probably background
        } else if (value == Imgproc.GC_PR_FGD) {
          buffer[0] = (byte) 170; // probably foreground
        } else {
          buffer[0] = (byte) 255; // for sure foreground

        }
        mask.put(x, y, buffer);
      }
    }
  }

  /**
   * Converts level of grayscale into OpenCV values. White - foreground, Black - background.
   */
  private static void convertToOpencvValues(Mat mask) {
    byte[] buffer = new byte[3];
    for (int x = 0; x < mask.rows(); x++) {
      for (int y = 0; y < mask.cols(); y++) {
        mask.get(x, y, buffer);
        int value = buffer[0];
        if (value >= 0 && value < 64) {
          buffer[0] = Imgproc.GC_BGD; // for sure background
        } else if (value >= 64 && value < 128) {
          buffer[0] = Imgproc.GC_PR_BGD; // probably background
        } else if (value >= 128 && value < 192) {
          buffer[0] = Imgproc.GC_PR_FGD; // probably foreground
        } else {
          buffer[0] = Imgproc.GC_FGD; // for sure foreground

        }
        mask.put(x, y, buffer);
      }
    }

  }



  private Bitmap overlayBitmapMaker(Bitmap baseBmp, Bitmap overlayBmp, int distanceLeft, int distanceTop) {

    Bitmap resultBmp = Bitmap.createBitmap(baseBmp.getWidth(),
        baseBmp.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(resultBmp);
    canvas.drawBitmap(baseBmp, 0, 0, null);
    canvas.drawBitmap(overlayBmp, distanceLeft, distanceTop, null);
    return resultBmp;
  }

}

