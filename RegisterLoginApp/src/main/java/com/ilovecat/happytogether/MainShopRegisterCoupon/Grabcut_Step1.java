package com.ilovecat.happytogether.MainShopRegisterCoupon;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 08. 05. happytogether
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonConstants;
import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.StickerClass.StickerShopLogoImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Grabcut_Step1 extends ActionBarActivity {

  static final int REQUEST_OPEN_IMAGE = 1;

  static String mCurrentPhotoPath;
  Bitmap mBitmap;
  ImageView mImageView;
  ImageView iv;
  int touchCount = 0;
  Point tl;
  Point br;
  boolean targetChose = false;
  ProgressDialog dlg;
  //final String imgPathAndFileName  = CommonConstants.INNER_DISK_PATH + "dduc.jpg";
  //final String maskPathAndFileName  = CommonConstants.INNER_DISK_PATH + "dduc.png";

  String maskPath;
  String imgPath;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.grabcut_main2);


    Intent intent = getIntent();
    String strPngFileName = intent.getExtras().getString("PNGFILENAME");
    String strBackFileName = intent.getExtras().getString("BACKFILENAME");



    maskPath   = CommonConstants.INNER_DISK_PATH + "CropImage/" + strPngFileName;
    imgPath =  strBackFileName;

    Log.i("ddfd", maskPath);

    Log.i("ddfd", imgPath);

    iv = (ImageView) this.findViewById(R.id.imgDisplay);

    mImageView = (ImageView) findViewById(R.id.imgDisplay);

    dlg = new ProgressDialog(this);
    tl = new Point();
    br = new Point();
    if (!OpenCVLoader.initDebug()) {
      // Handle initialization error
    }



    final Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        //Do something after 100ms
        new ProcessImageTask().execute();

      }
    }, 0);




  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_grabcut2, menu);
    return true;
  }

  private void setPic() {
    int targetW = mImageView.getWidth();
    int targetH = mImageView.getHeight();

    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
    bmOptions.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    int photoW = bmOptions.outWidth;
    int photoH = bmOptions.outHeight;

    int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

    bmOptions.inJustDecodeBounds = false;
    bmOptions.inSampleSize = scaleFactor;
    bmOptions.inPurgeable = true;

    mBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    mImageView.setImageBitmap(mBitmap);

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case REQUEST_OPEN_IMAGE:
        if (resultCode == RESULT_OK) {
          Uri imgUri = data.getData();
          String[] filePathColumn = { MediaStore.Images.Media.DATA };

          Cursor cursor = getContentResolver().query(imgUri, filePathColumn,
              null, null, null);
          cursor.moveToFirst();

          int colIndex = cursor.getColumnIndex(filePathColumn[0]);
          mCurrentPhotoPath = cursor.getString(colIndex);
          cursor.close();
          setPic();
        }
        break;
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    switch (id) {
      case R.id.action_open_img:
        Intent getPictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getPictureIntent.setType("image/*");
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent chooserIntent = Intent.createChooser(getPictureIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {
            pickPictureIntent
        });
        startActivityForResult(chooserIntent, REQUEST_OPEN_IMAGE);
        return true;
      case R.id.action_choose_target:
        if (mCurrentPhotoPath != null)
          targetChose = false;
        mImageView.setOnTouchListener(new View.OnTouchListener() {

          @Override
          public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
              if (touchCount == 0) {
                tl.x = event.getX();
                tl.y = event.getY();
                touchCount++;
              }
              else if (touchCount == 1) {
                br.x = event.getX();
                br.y = event.getY();

                Paint rectPaint = new Paint();
                rectPaint.setARGB(255, 255, 0, 0);
                rectPaint.setStyle(Paint.Style.STROKE);
                rectPaint.setStrokeWidth(3);
                Bitmap tmpBm = Bitmap.createBitmap(mBitmap.getWidth(),
                    mBitmap.getHeight(), Bitmap.Config.RGB_565);
                Canvas tmpCanvas = new Canvas(tmpBm);

                tmpCanvas.drawBitmap(mBitmap, 0, 0, null);
                tmpCanvas.drawRect(new RectF((float) tl.x, (float) tl.y, (float) br.x, (float) br.y),
                    rectPaint);
                mImageView.setImageDrawable(new BitmapDrawable(getResources(), tmpBm));

                targetChose = true;
                touchCount = 0;
                mImageView.setOnTouchListener(null);
              }
            }

            return true;
          }
        });

        return true;
      case R.id.action_cut_image:
        if (mCurrentPhotoPath != null && targetChose) {
          new ProcessImageTask().execute();
          targetChose = false;
        }
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private class ProcessImageTask extends AsyncTask<Integer, Integer, Integer> {

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


      //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sul_s);
      //bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

      //Bitmap bitmapS = BitmapFactory.decodeResource(getResources(), R.drawable.sul);
      //bitmapS = bitmapS.copy(Bitmap.Config.RGB_565, true);


      //Mat imgS = new Mat();
      //Utils.bitmapToMat(bitmapS, imgS);
      //Mat img = new Mat();
      //Imgproc.cvtColor(imgS, img, Imgproc.COLOR_RGBA2RGB);


      Mat img = Imgcodecs.imread(imgPath);
      Mat background = new Mat(img.size(), CvType.CV_8UC3,
          new Scalar(255, 255, 255));

      Mat firstMask = new Mat();

      Mat newMask =  Imgcodecs.imread(maskPath,  CvType.CV_8UC1);
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
      Point p1 = new Point(c/10, r/10);
      Point p2 = new Point(c-c/10, r-r/16);

      Rect rect = new Rect(p1,p2);
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
      Imgproc.threshold(newMask,newMask,128,255,Imgproc.THRESH_TOZERO);


      /*
      //
      Mat dstNewMask = new Mat(newMask.size(), CvType.CV_8UC1);
      Mat tmpNewMask = new Mat(newMask.size(), CvType.CV_8UC1);
      Mat alphaNewMask = new Mat(newMask.size(), CvType.CV_8UC1);

      // convert image to grayscale
      //Imgproc.cvtColor(newMask, tmpNewMask, Imgproc.COLOR_BGR2GRAY);
      newMask.copyTo(tmpNewMask);

      // threshold the image to create alpha channel
      // with complete transparency in black background region
      // and zero transparency in foreground object region.
      Imgproc.threshold(tmpNewMask, alphaNewMask, 100, 255, Imgproc.THRESH_BINARY);



      // split the original image into three single channel.
      List<Mat> rgbNewMask = new ArrayList<Mat>(3);
      Core.split(newMask, rgbNewMask);
      // Create the final result by merging three single channel and alpha(BGRA order)
      List<Mat> rgbaNewMask = new ArrayList<Mat>(1);
      //rgbaNewMask.add(rgbNewMask.get(0));
      //rgbaNewMask.add(rgbNewMask.get(1));
      //rgbaNewMask.add(rgbNewMask.get(2));
      rgbaNewMask.add(alphaNewMask);
      Core.merge(rgbaNewMask, dstNewMask);
      //
      */


      // GC_PR_FGD와 동일한 값을 갖는 화소를 추출해 분할한 이진 영상을 얻음
      //Core.compare(newMask, source, newMask, Core.CMP_EQ);


      // 전경일 가능성이 있는 화소를 마크한 것을 가져오기
      //Mat foreground = new Mat(newMask.size(), CvType.CV_8UC3,
      //    new Scalar(255, 255, 255));




      Mat foreground = new Mat(newMask.size(), CvType.CV_8UC3,
          new Scalar(0,0,0));




      /*
      //
      Mat dstForeGround = new Mat(foreground.size(), CvType.CV_8UC1);
      Mat tmpForeGround = new Mat(foreground.size(), CvType.CV_8UC4);
      Mat alphaForeGround = new Mat(foreground.size(), CvType.CV_8UC4);

      // convert image to grayscale
      Imgproc.cvtColor(foreground, tmpForeGround, Imgproc.COLOR_BGR2GRAY);

      // threshold the image to create alpha channel
      // with complete transparency in black background region
      // and zero transparency in foreground object region.
      Imgproc.threshold(tmpForeGround, alphaForeGround, 100, 255, Imgproc.THRESH_BINARY);


    // split the original image into three single channel.
      List<Mat> rgb = new ArrayList<Mat>(3);
      Core.split(foreground, rgb);
      // Create the final result by merging three single channel and alpha(BGRA order)
      List<Mat> rgba = new ArrayList<Mat>(4);
      rgba.add(rgb.get(0));
      rgba.add(rgb.get(1));
      rgba.add(rgb.get(2));
      rgba.add(alphaForeGround);
      Core.merge(rgba, dstForeGround);
      //
      */

      //inputMat.copyTo(outputMat, maskMat);
      img.copyTo(foreground, newMask);



      /*
      // draw rectangle on original image
      Scalar color = new Scalar(255, 0, 0, 255);
      Imgproc.rectangle(img, tl, br, color);


      // 백그라운드 이미지 크기 재조정
      Mat tmp = new Mat();
      Imgproc.resize(background, tmp, img.size());
      background = tmp;



      // 추출된 전경이미지의 마스크
      mask = new Mat(foreground.size(), CvType.CV_8UC1,
          new Scalar(255, 255, 255));

      // 마스크를 그래이로 전환
      Imgproc.cvtColor(foreground, mask, Imgproc.COLOR_BGR2GRAY);
      // 마스크 이진화
      Imgproc.threshold(mask, mask, 254, 255, Imgproc.THRESH_BINARY_INV);

      System.out.println();

      Mat vals = new Mat(1, 1, CvType.CV_8UC3, new Scalar(0.0));

      // 백그라운드를 dst로 복사
      background.copyTo(dst);
      // 백그라운드에 mask 적용
      background.setTo(vals, mask);

      //add(imageA, imageB, resultC, mask) // if(mask[i]) c[i]=a[i]+b[i];
      // 마스크를 적용하여
      Core.add(background, foreground, dst, mask);

      firstMask.release();
      source.release();
      bgModel.release();
      fgModel.release();
      //vals.release();
      */


      /*

      Mat tmp2 = new Mat();
      Mat alpha = new Mat();
      Imgproc.cvtColor(foreground, tmp2, Imgproc.COLOR_BGR2GRAY);
      Imgproc.threshold(tmp2, alpha, 100, 255, Imgproc.THRESH_BINARY);

      List<Mat> rgb = new ArrayList<Mat>(3);
      Core.split(foreground, rgb);


      List<Mat> rgba = new ArrayList<Mat>(4);
      rgba.add(rgb.get(0));
      rgba.add(rgb.get(1));
      rgba.add(rgb.get(2));
      rgba.add(alpha);
      Core.merge(rgba, foreground);
    */




      // convert matrix to output bitmap
      Bitmap output = Bitmap.createBitmap((int)foreground.size().width, (int)foreground.size().height, Bitmap.Config.ARGB_8888);
      Mat tmpInvertRGB = new Mat();
      Imgproc.cvtColor(foreground, tmpInvertRGB, Imgproc.COLOR_BGR2RGB);

      Utils.matToBitmap(tmpInvertRGB, output);
      //output.eraseColor(Color.WHITE);
      for(int x = 0; x < output.getWidth(); x++){
        for(int y = 0; y < output.getHeight(); y++){
          if(output.getPixel(x, y) == Color.BLACK){
            output.setPixel(x, y, Color.TRANSPARENT);
          }
        }
      }




      FileOutputStream out = null;
      try {
        out = new FileOutputStream(maskPath + ".png");
        output.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        // PNG is a lossless format, the compression factor (100) is ignored
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {
          if (out != null) {
            out.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      output.recycle();

      firstMask.release();
      source.release();
      bgModel.release();
      fgModel.release();
      tmpInvertRGB.release();
      //vals.release();



      /*
      MatOfInt matInt=new MatOfInt();
      matInt.fromArray(Imgcodecs.CV_IMWRITE_PNG_COMPRESSION, 0);
      //matInt.fromArray(9);

      Imgcodecs.imwrite(mCurrentPhotoPath + ".png", foreground, matInt);
      */
      return 0;
    }

    @Override
    protected void onPostExecute(Integer result) {
      super.onPostExecute(result);

      Bitmap png = BitmapFactory
          .decodeFile(maskPath + ".png");


      //Bitmap resultBitmap = Bitmap.createBitmap(png.getWidth(), png.getHeight(), Bitmap.Config.ARGB_8888);
      //Utils.matToBitmap(foreground, resultBitmap);
      //resultBitmap.eraseColor(Color.WHITE);

      // convert matrix to output bitmap
      //Bitmap transPng = Bitmap.createBitmap(png.getWidth(), png.getHeight(), Bitmap.Config.ARGB_8888);


      //png.eraseColor(0);

      //Bitmap transPng = makeBlackTransparent(png);


      /*
      float[] colorTransform = {
          0, 1f, 0, 0, 0,
          0, 0, 0f, 0, 0,
          0, 0, 0, 0f, 0,
          0, 0, 0, 1f, 0};

      ColorMatrix colorMatrix = new ColorMatrix();
      colorMatrix.setSaturation(0f); //Remove Colour
      colorMatrix.set(colorTransform); //Apply the Red

      ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
      Paint paint = new Paint();
      paint.setColorFilter(colorFilter);

      Display display = getWindowManager().getDefaultDisplay();

      Bitmap resultBitmap = Bitmap.createBitmap(transPng, 0, 0, transPng.getWidth(), (int)(transPng.getHeight()));

      //image.setImageBitmap(resultBitmap);

      mImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      mImageView.setAdjustViewBounds(true);
      mImageView.setPadding(2, 2, 2, 2);
      mImageView.setImageBitmap(resultBitmap);
      mImageView.invalidate();

      Canvas canvas = new Canvas(resultBitmap);
      canvas.drawBitmap(resultBitmap, 0, 0, paint);
      */
      mImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      mImageView.setAdjustViewBounds(true);
      mImageView.setPadding(2, 2, 2, 2);
      mImageView.setImageBitmap(png);
      mImageView.invalidate();


      //Canvas canvas = new Canvas(resultBitmap);
      //canvas.drawBitmap(resultBitmap, 0, 0, null);

      dlg.dismiss();



      Drawable d = Drawable.createFromPath(maskPath + ".png");
      //MainRegisterCouponShopStep1.iv_ShopLogoSticker = new StickerShopLogoImageView(MainRegisterCouponShopStep1.context);

      int sizeOfArray = MainRegisterCouponShopStep1.arrayListStickerShopLogoView.size();
      MainRegisterCouponShopStep1.iv_ShopLogoSticker = (StickerShopLogoImageView) MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(sizeOfArray - 1);



      MainRegisterCouponShopStep1.iv_ShopLogoSticker.setImageDrawable(d);

      MainRegisterCouponShopStep1.iv_ShopLogoSticker.setControlItemsHidden(true);
      MainRegisterCouponShopStep1.iv_ShopLogoSticker.setControlItemsMoveLock(false);
      //MainRegisterCouponShopStep1.iv_ShopLogoSticker.setPathAndFileName(cropImage_FileName);
     // MainRegisterCouponShopStep1.arrayListStickerShopLogoView.add(MainRegisterCouponShopStep1.iv_ShopLogoSticker);


     // MainRegisterCouponShopStep1.canvas.addView(MainRegisterCouponShopStep1.iv_ShopLogoSticker);

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



      finish();




    }
  }

  public void debugger(String s){
    Log.v("","########### "+s);
  }


  /**

   * Make the black background of a PNG-Bitmap-Image transparent.
   * code based on example at http://j.mp/1uCxOV5
   * @Param image png bitmap image
   * @return output image
   */


  private static Bitmap makeBlackTransparent(Bitmap image) {

    // convert image to matrix
    Mat src = new Mat(image.getWidth(), image.getHeight(), CvType.CV_8UC4);
    Utils.bitmapToMat(image, src);

    // init new matrices
    Mat dst = new Mat(image.getWidth(), image.getHeight(), CvType.CV_8UC4);
    Mat tmp = new Mat(image.getWidth(), image.getHeight(), CvType.CV_8UC4);
    Mat alpha = new Mat(image.getWidth(), image.getHeight(), CvType.CV_8UC4);

    // convert image to grayscale
    Imgproc.cvtColor(src, tmp, Imgproc.COLOR_BGR2GRAY);

    // threshold the image to create alpha channel
    // with complete transparency in black background region
    // and zero transparency in foreground object region.
    Imgproc.threshold(tmp, alpha, 100, 255, Imgproc.THRESH_BINARY);

    // split the original image into three single channel.
    List<Mat> rgb = new ArrayList<Mat>(3);
    Core.split(src, rgb);

    // Create the final result by merging three single channel and alpha(BGRA order)
    List<Mat> rgba = new ArrayList<Mat>(4);
    rgba.add(rgb.get(0));
    rgba.add(rgb.get(1));
    rgba.add(rgb.get(2));
    rgba.add(alpha);
    Core.merge(rgba, dst);


    //Imgcodecs.imwrite(mCurrentPhotoPath + ".png", dst);

    // convert matrix to output bitmap
    Bitmap output = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
    Utils.matToBitmap(dst, output);


    return output;
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
   * Converts level of grayscale into OpenCV values. White - foreground, Black
   * - background.
   *
   * @param mask
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
}