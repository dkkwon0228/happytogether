package com.ilovecat.happytogether.ControllerCropBorderSticker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ilovecat.happytogether.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;


public class cropStickerActivity extends AppCompatActivity {

  static SizeAwareImageView cropImageView;
  //CropImageView orgImageView;
  Context stikerCropContext;
  String PATH_CROP_IMAGE_DIR = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/HappyTogether/CropImage/";
  String FULL_PATH_CROP_IMAGE;
  String FILE_NAME_CROP_IMAGE;
  String FILE_SAVE_TIME;


  Bitmap croppedImageOrgBitmap;
  Bitmap bitmapInTemp;
  static Bitmap toAdjustBitmap;
  Bitmap orgBitmap;

  ImageView btChoose_circle_MaskImageView;
  ImageView btChoose_rectangle_MaskImageView;
  ImageView btChoose_roundrectangle_MaskImageView;
  ImageView btChoose_star_MaskImageView;
  ImageView btChoose_heart_MaskImageView;
  ImageView btChoose_ext_MaskImageView0;
  ImageView btChoose_ext_MaskImageView1;
  ImageView btChoose_ext_MaskImageView2;
  ImageView btChoose_ext_MaskImageView3;
  ImageView btChoose_ext_MaskImageView4;
  ImageView btChoose_ext_MaskImageView5;

  public static StikerCropSortrView stikerCropSorterView;

  int widthOfcropviewStikerCropLayout;
  int heightOfcropviewStikerCropLayout;

  int ScaleUpRatioInt;

  static Bitmap toAdjustBitmapScaleUP;

  float displayWidth;
  float displayHeight;


  Bitmap resultBitmap;
  String maskName;


  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.stikercrop);

    stikerCropContext = getApplicationContext();
    final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View rootView = inflater.inflate(R.layout.stikercrop, null);


    cropImageView = (SizeAwareImageView) findViewById(R.id.CropImageViewCropStiker);

    Intent intent = getIntent();
    FILE_NAME_CROP_IMAGE = intent.getExtras().getString("FILE_NAME_FOR_CROP_IMAGE");
    FULL_PATH_CROP_IMAGE = intent.getExtras().getString("FULL_PATH_FOR_CROP_IMAGE");


    String arrfileName = FILE_NAME_CROP_IMAGE.split("_")[2];
    FILE_SAVE_TIME =  arrfileName.substring(0,13);




    //String[] pathElement = new String(FULL_PATH_CROP_IMAGE).split("/");
    //final String orgFileName =  pathElement[pathElement.length -1];
    //final String IMAGEPATH_TEMP_FILENAME = IMAGESPATH_TEMP + orgFileName;
    //final String IMAGEPATH_TEMP_FILENAME_THUM = IMAGESPATH_TEMP + "thum" + orgFileName;
    //final String IMAGEPATH_TEMP_FILENAME_COPYED = IMAGESPATH_TEMP + "copyed_" + orgFileName;

    //끝

    /*
     * 원본이미지
    */
    //orgBitmap = BitmapFactory.decodeFile(IMAGESPATH_ORG);


    //int w = orgBitmap.getWidth();
    //int h = orgBitmap.getHeight();
    //Log.i("w orgBitmap", String.valueOf(w));
    //Log.i("h orgBitmap", String.valueOf(h));

    //끝


    DisplayMetrics metrics = getResources().getDisplayMetrics();
    displayWidth = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.max(metrics.widthPixels,
        metrics.heightPixels) : Math.min(metrics.widthPixels, metrics.heightPixels);
    displayHeight = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.min(metrics.widthPixels,
        metrics.heightPixels) : Math.max(metrics.widthPixels, metrics.heightPixels);


    int degree = ImageUtil.GetExifOrientation(FULL_PATH_CROP_IMAGE);
    int tempImageWidth = getBitmapOfWidth(FULL_PATH_CROP_IMAGE);
    int tempImageHeight = getBitmapOfHeight(FULL_PATH_CROP_IMAGE);

    //if (tempImageWidth > widthOfcropviewStikerCropLayout || tempImageHeight > heightOfcropviewStikerCropLayout) {
    if (tempImageWidth > 2880) {

      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inSampleSize = 4;
      bitmapInTemp = BitmapFactory.decodeFile(FULL_PATH_CROP_IMAGE, options);
      bitmapInTemp = ImageUtil.GetRotatedBitmap(bitmapInTemp, degree);

    } else if (tempImageWidth <= 2880 && tempImageWidth >= 1440) {

      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inSampleSize = 2;
      bitmapInTemp = BitmapFactory.decodeFile(FULL_PATH_CROP_IMAGE, options);
      bitmapInTemp = ImageUtil.GetRotatedBitmap(bitmapInTemp, degree);


    } else {

      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inSampleSize = 1;
      bitmapInTemp = BitmapFactory.decodeFile(FULL_PATH_CROP_IMAGE, options);
      bitmapInTemp = ImageUtil.GetRotatedBitmap(bitmapInTemp, degree);

    }
    //End

    //Log.i("getBitmapOfWidth(IMAGEPATH_TEMP_FILENAME_COPYED)",String.valueOf(getBitmapOfWidth(FULL_PATH_CROP_IMAGE)));
    Log.i("w Temp", String.valueOf(getBitmapOfWidth(FULL_PATH_CROP_IMAGE)));
    Log.i("h Temp", String.valueOf(getBitmapOfHeight(FULL_PATH_CROP_IMAGE)));

    SaveBitmapToFileCache(bitmapInTemp, FULL_PATH_CROP_IMAGE);
    toAdjustBitmap = bitmapInTemp;


    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    /*
     *  사용할 비트맵의 가로 길이와 스크린의 가로길이를 비교하여
     *  사용할 비트맵의 가로길이가 스크린 가로길이보다 적거나 같을 경우는
     *  비율만큼 확대힌다.
     */


    float screenWidth = stikerCropContext.getResources().getDisplayMetrics().widthPixels;
    int screenWidthInt = (int) screenWidth;

    Log.i("sceenWidth", String.valueOf(screenWidthInt));
    Log.i("w toAdjustBitmap", String.valueOf(toAdjustBitmap.getWidth()));
    Log.i("h toAdjustBitmap", String.valueOf(toAdjustBitmap.getHeight()));


    if (toAdjustBitmap.getWidth() < screenWidthInt) {
      //if (toAdjustBitmap.getWidth() < 1440) {

      float ratio = (float) ((screenWidth / toAdjustBitmap.getWidth()) * 1.0);

      float scaleUpWidth = toAdjustBitmap.getWidth() * ratio;
      float scaleUpHeight = toAdjustBitmap.getHeight() * ratio;


      Log.i("ration - Screen", String.valueOf(ratio));

      toAdjustBitmapScaleUP = Bitmap.createScaledBitmap(
          toAdjustBitmap
          , screenWidthInt //(int) scaleUpWidth 스크린 해상도보다 1이 적은 값이 나오는 오류가 있어 강제로
          // 스크린 길이를 줌
          , (int) scaleUpHeight
          , true);
    } else if (toAdjustBitmap.getWidth() < screenWidthInt) { // 같다면

      float ratio = 1.0f;

      float scaleUpWidth = toAdjustBitmap.getWidth() * ratio;
      float scaleUpHeight = toAdjustBitmap.getHeight() * ratio;


      Log.i("ration  - btmW", String.valueOf(ratio));

      toAdjustBitmapScaleUP = Bitmap.createScaledBitmap(
          toAdjustBitmap
          , (int) scaleUpWidth
          , (int) scaleUpHeight
          , true);

    }

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////


    //cropImageView.setImageBitmap(filterBitmap);
    Log.i("w toAdjustBitmapScaleUP", String.valueOf(toAdjustBitmapScaleUP.getWidth()));
    Log.i("h toAdjustBitmapScaleUP", String.valueOf(toAdjustBitmapScaleUP.getHeight()));

    cropImageView.setImageBitmap(toAdjustBitmapScaleUP);


    Log.i("w bitmapOnView", String.valueOf(cropImageView.getDrawable().getIntrinsicWidth()));
    Log.i("h bitmapOnView", String.valueOf(cropImageView.getDrawable().getIntrinsicHeight()));


    stikerCropSorterView = (StikerCropSortrView) findViewById(R.id.stikerCropSortsStikersView);

    btChoose_circle_MaskImageView = (ImageView) findViewById(R.id.button_choose_circleMask);
    btChoose_circle_MaskImageView.setImageResource(R.drawable.mask_circle);
    //Log.i("imageUrls[position] is in ORGFILTER: ",imageUrls[position]);
    //ImageManager2.from(selectedImagesActivity.this).displayImageFilter(ORG_STYLE, btChoose_org_filterImageView, imageUrlsForFilter[position], R.drawable.camera_default,pixel,pixel);
    //Bitmap orgBitmap = BitmapFilter.changeStyle(bitmapForFilter, BitmapFilter.ORG_STYLE);
    //btChoose_circle_MaskImageView.setImageBitmap(BitmapFilter.changeStyle(bitmapForFilter[position], BitmapFilter.ORG_STYLE));
    //btChoose_circle_MaskImageView.setBackgroundColor( Color.TRANSPARENT );
    btChoose_circle_MaskImageView.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        stikerCropSorterView.mImgArryList.clear();

        btChoose_circle_MaskImageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btChoose_rectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_roundrectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_star_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_heart_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView0.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView1.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView2.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView3.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView4.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView5.setBackgroundColor(Color.parseColor("#88FFFFFF"));

        String stikerPath = "drawable://" + R.drawable.mask_circle_onview;
        String[] arr = stikerPath.split("//");
        String stikerNewPath = arr[1];
        String[] arrNewPath = stikerNewPath.split("/");
        int arrNewPathSize = arrNewPath.length;
        int stikerCropResId = Integer.parseInt(arrNewPath[arrNewPathSize - 1]);
        Log.i("stikerCropResId is ", String.valueOf(stikerCropResId));


        String stikerPathMask = "drawable://" + R.drawable.mask_circle_onviewbg;
        String[] arrMask = stikerPathMask.split("//");
        String stikerNewPathMask = arrMask[1];
        String[] arrNewPathMask = stikerNewPathMask.split("/");
        int arrNewPathSizeMask = arrNewPathMask.length;
        int stikerCropResIdMask = Integer.parseInt(arrNewPathMask[arrNewPathSizeMask - 1]);

        stikerCropSorterView.setVisibility(View.VISIBLE);
        //stikerCropSorterView.setBackgroundColor(0x88000000);
        // rl.setBackgroundColor(0x88000000)

        stikerCropSorterView.loadImages(stikerCropContext, stikerCropResId, stikerCropResIdMask,
            stikerCropSorterView,
            widthOfcropviewStikerCropLayout,
            heightOfcropviewStikerCropLayout, rootView);
      }


    });

    btChoose_rectangle_MaskImageView = (ImageView) findViewById(R.id.button_choose_rectangleMask);
    btChoose_rectangle_MaskImageView.setImageResource(R.drawable.mask_rectangle);
    btChoose_rectangle_MaskImageView.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        stikerCropSorterView.mImgArryList.clear();

        btChoose_circle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_rectangle_MaskImageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btChoose_roundrectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_star_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_heart_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView0.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView1.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView2.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView3.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView4.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView5.setBackgroundColor(Color.parseColor("#88FFFFFF"));

        String stikerPath = "drawable://" + R.drawable.mask_rectangle_onview;
        String[] arr = stikerPath.split("//");
        String stikerNewPath = arr[1];
        String[] arrNewPath = stikerNewPath.split("/");
        int arrNewPathSize = arrNewPath.length;

        int stikerCropResId = Integer.parseInt(arrNewPath[arrNewPathSize - 1]);
        Log.i("stikerCropResId is ", String.valueOf(stikerCropResId));

        String stikerPathMask = "drawable://" + R.drawable.mask_rectangle_onviewbg;
        String[] arrMask = stikerPathMask.split("//");
        String stikerNewPathMask = arrMask[1];
        String[] arrNewPathMask = stikerNewPathMask.split("/");
        int arrNewPathSizeMask = arrNewPathMask.length;
        int stikerCropResIdMask = Integer.parseInt(arrNewPathMask[arrNewPathSizeMask - 1]);

        stikerCropSorterView.setVisibility(View.VISIBLE);
        stikerCropSorterView.loadImages(stikerCropContext, stikerCropResId, stikerCropResIdMask,
            stikerCropSorterView,
            widthOfcropviewStikerCropLayout,
            heightOfcropviewStikerCropLayout, rootView);

      }

    });

    btChoose_roundrectangle_MaskImageView = (ImageView) findViewById(R.id.button_choose_roundrectangleMask);
    btChoose_roundrectangle_MaskImageView.setImageResource(R.drawable.mask_roundrectangle);
    btChoose_roundrectangle_MaskImageView.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        stikerCropSorterView.mImgArryList.clear();

        btChoose_circle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_rectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_roundrectangle_MaskImageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btChoose_star_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_heart_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView0.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView1.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView2.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView3.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView4.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView5.setBackgroundColor(Color.parseColor("#88FFFFFF"));

        String stikerPath = "drawable://" + R.drawable.mask_roundrectangle_onview;
        String[] arr = stikerPath.split("//");
        String stikerNewPath = arr[1];
        String[] arrNewPath = stikerNewPath.split("/");
        int arrNewPathSize = arrNewPath.length;

        int stikerCropResId = Integer.parseInt(arrNewPath[arrNewPathSize - 1]);
        Log.i("stikerCropResId is ", String.valueOf(stikerCropResId));

        String stikerPathMask = "drawable://" + R.drawable.mask_roundrectangle_onviewbg;
        String[] arrMask = stikerPathMask.split("//");
        String stikerNewPathMask = arrMask[1];
        String[] arrNewPathMask = stikerNewPathMask.split("/");
        int arrNewPathSizeMask = arrNewPathMask.length;
        int stikerCropResIdMask = Integer.parseInt(arrNewPathMask[arrNewPathSizeMask - 1]);

        stikerCropSorterView.setVisibility(View.VISIBLE);
        stikerCropSorterView.loadImages(stikerCropContext, stikerCropResId, stikerCropResIdMask,
            stikerCropSorterView,
            widthOfcropviewStikerCropLayout,
            heightOfcropviewStikerCropLayout, rootView);

      }

    });

    btChoose_star_MaskImageView = (ImageView) findViewById(R.id.button_choose_starMask);
    btChoose_star_MaskImageView.setImageResource(R.drawable.mask_star);
    btChoose_star_MaskImageView.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        stikerCropSorterView.mImgArryList.clear();

        btChoose_circle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_rectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_roundrectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_star_MaskImageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btChoose_heart_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView0.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView1.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView2.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView3.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView4.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView5.setBackgroundColor(Color.parseColor("#88FFFFFF"));

        String stikerPath = "drawable://" + R.drawable.mask_star_onview;
        String[] arr = stikerPath.split("//");
        String stikerNewPath = arr[1];
        String[] arrNewPath = stikerNewPath.split("/");
        int arrNewPathSize = arrNewPath.length;

        int stikerCropResId = Integer.parseInt(arrNewPath[arrNewPathSize - 1]);
        Log.i("stikerCropResId is ", String.valueOf(stikerCropResId));

        String stikerPathMask = "drawable://" + R.drawable.mask_star_onviewbg;
        String[] arrMask = stikerPathMask.split("//");
        String stikerNewPathMask = arrMask[1];
        String[] arrNewPathMask = stikerNewPathMask.split("/");
        int arrNewPathSizeMask = arrNewPathMask.length;
        int stikerCropResIdMask = Integer.parseInt(arrNewPathMask[arrNewPathSizeMask - 1]);

        stikerCropSorterView.setVisibility(View.VISIBLE);
        stikerCropSorterView.loadImages(stikerCropContext, stikerCropResId, stikerCropResIdMask,
            stikerCropSorterView,
            widthOfcropviewStikerCropLayout,
            heightOfcropviewStikerCropLayout, rootView);

      }

    });

    btChoose_heart_MaskImageView = (ImageView) findViewById(R.id.button_choose_heartMask);
    btChoose_heart_MaskImageView.setImageResource(R.drawable.mask_heart);
    btChoose_heart_MaskImageView.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        stikerCropSorterView.mImgArryList.clear();

        btChoose_circle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_rectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_roundrectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_star_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_heart_MaskImageView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        btChoose_ext_MaskImageView0.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView1.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView2.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView3.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView4.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView5.setBackgroundColor(Color.parseColor("#88FFFFFF"));

        String stikerPath = "drawable://" + R.drawable.mask_heart_onview;
        String[] arr = stikerPath.split("//");
        String stikerNewPath = arr[1];
        String[] arrNewPath = stikerNewPath.split("/");
        int arrNewPathSize = arrNewPath.length;

        int stikerCropResId = Integer.parseInt(arrNewPath[arrNewPathSize - 1]);
        Log.i("stikerCropResId is ", String.valueOf(stikerCropResId));

        String stikerPathMask = "drawable://" + R.drawable.mask_heart_onviewbg;
        String[] arrMask = stikerPathMask.split("//");
        String stikerNewPathMask = arrMask[1];
        String[] arrNewPathMask = stikerNewPathMask.split("/");
        int arrNewPathSizeMask = arrNewPathMask.length;
        int stikerCropResIdMask = Integer.parseInt(arrNewPathMask[arrNewPathSizeMask - 1]);

        stikerCropSorterView.setVisibility(View.VISIBLE);
        stikerCropSorterView.loadImages(stikerCropContext, stikerCropResId, stikerCropResIdMask,
            stikerCropSorterView,
            widthOfcropviewStikerCropLayout,
            heightOfcropviewStikerCropLayout, rootView);

      }

    });

    btChoose_ext_MaskImageView0 = (ImageView) findViewById(R.id.button_choose_extMask0);
    btChoose_ext_MaskImageView0.setImageResource(R.drawable.mask_ext0);
    btChoose_ext_MaskImageView0.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        stikerCropSorterView.mImgArryList.clear();

        btChoose_circle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_rectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_roundrectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_star_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_heart_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView0.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        btChoose_ext_MaskImageView1.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView2.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView3.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView4.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView5.setBackgroundColor(Color.parseColor("#88FFFFFF"));

        String stikerPath = "drawable://" + R.drawable.mask_ext0_onview;
        String[] arr = stikerPath.split("//");
        String stikerNewPath = arr[1];
        String[] arrNewPath = stikerNewPath.split("/");
        int arrNewPathSize = arrNewPath.length;

        int stikerCropResId = Integer.parseInt(arrNewPath[arrNewPathSize - 1]);
        Log.i("stikerCropResId is ", String.valueOf(stikerCropResId));

        String stikerPathMask = "drawable://" + R.drawable.mask_ext0_onviewbg;
        String[] arrMask = stikerPathMask.split("//");
        String stikerNewPathMask = arrMask[1];
        String[] arrNewPathMask = stikerNewPathMask.split("/");
        int arrNewPathSizeMask = arrNewPathMask.length;
        int stikerCropResIdMask = Integer.parseInt(arrNewPathMask[arrNewPathSizeMask - 1]);

        stikerCropSorterView.setVisibility(View.VISIBLE);
        stikerCropSorterView.loadImages(stikerCropContext, stikerCropResId, stikerCropResIdMask,
            stikerCropSorterView,
            widthOfcropviewStikerCropLayout,
            heightOfcropviewStikerCropLayout, rootView);

      }

    });

    btChoose_ext_MaskImageView1 = (ImageView) findViewById(R.id.button_choose_extMask1);
    btChoose_ext_MaskImageView1.setImageResource(R.drawable.mask_ext1);
    btChoose_ext_MaskImageView1.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        stikerCropSorterView.mImgArryList.clear();

        btChoose_circle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_rectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_roundrectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_star_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_heart_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView0.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView1.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        btChoose_ext_MaskImageView2.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView3.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView4.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView5.setBackgroundColor(Color.parseColor("#88FFFFFF"));

        String stikerPath = "drawable://" + R.drawable.mask_ext1_onview;
        String[] arr = stikerPath.split("//");
        String stikerNewPath = arr[1];
        String[] arrNewPath = stikerNewPath.split("/");
        int arrNewPathSize = arrNewPath.length;

        int stikerCropResId = Integer.parseInt(arrNewPath[arrNewPathSize - 1]);
        Log.i("stikerCropResId is ", String.valueOf(stikerCropResId));

        String stikerPathMask = "drawable://" + R.drawable.mask_ext1_onviewbg;
        String[] arrMask = stikerPathMask.split("//");
        String stikerNewPathMask = arrMask[1];
        String[] arrNewPathMask = stikerNewPathMask.split("/");
        int arrNewPathSizeMask = arrNewPathMask.length;
        int stikerCropResIdMask = Integer.parseInt(arrNewPathMask[arrNewPathSizeMask - 1]);

        stikerCropSorterView.setVisibility(View.VISIBLE);
        stikerCropSorterView.loadImages(stikerCropContext, stikerCropResId, stikerCropResIdMask,
            stikerCropSorterView,
            widthOfcropviewStikerCropLayout,
            heightOfcropviewStikerCropLayout, rootView);

      }

    });

    btChoose_ext_MaskImageView2 = (ImageView) findViewById(R.id.button_choose_extMask2);
    btChoose_ext_MaskImageView2.setImageResource(R.drawable.mask_ext2);
    btChoose_ext_MaskImageView2.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        stikerCropSorterView.mImgArryList.clear();

        btChoose_circle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_rectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_roundrectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_star_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_heart_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView0.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView1.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView2.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        btChoose_ext_MaskImageView3.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView4.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView5.setBackgroundColor(Color.parseColor("#88FFFFFF"));

        String stikerPath = "drawable://" + R.drawable.mask_ext2_onview;
        String[] arr = stikerPath.split("//");
        String stikerNewPath = arr[1];
        String[] arrNewPath = stikerNewPath.split("/");
        int arrNewPathSize = arrNewPath.length;

        int stikerCropResId = Integer.parseInt(arrNewPath[arrNewPathSize - 1]);
        Log.i("stikerCropResId is ", String.valueOf(stikerCropResId));

        String stikerPathMask = "drawable://" + R.drawable.mask_ext2_onviewbg;
        String[] arrMask = stikerPathMask.split("//");
        String stikerNewPathMask = arrMask[1];
        String[] arrNewPathMask = stikerNewPathMask.split("/");
        int arrNewPathSizeMask = arrNewPathMask.length;
        int stikerCropResIdMask = Integer.parseInt(arrNewPathMask[arrNewPathSizeMask - 1]);

        stikerCropSorterView.setVisibility(View.VISIBLE);
        stikerCropSorterView.loadImages(stikerCropContext, stikerCropResId, stikerCropResIdMask,
            stikerCropSorterView,
            widthOfcropviewStikerCropLayout,
            heightOfcropviewStikerCropLayout, rootView);

      }

    });

    btChoose_ext_MaskImageView3 = (ImageView) findViewById(R.id.button_choose_extMask3);
    btChoose_ext_MaskImageView3.setImageResource(R.drawable.mask_ext3);
    btChoose_ext_MaskImageView3.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        stikerCropSorterView.mImgArryList.clear();

        btChoose_circle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_rectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_roundrectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_star_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_heart_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView0.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView1.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView2.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView3.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        btChoose_ext_MaskImageView4.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView5.setBackgroundColor(Color.parseColor("#88FFFFFF"));

        String stikerPath = "drawable://" + R.drawable.mask_ext3_onview;
        String[] arr = stikerPath.split("//");
        String stikerNewPath = arr[1];
        String[] arrNewPath = stikerNewPath.split("/");
        int arrNewPathSize = arrNewPath.length;

        int stikerCropResId = Integer.parseInt(arrNewPath[arrNewPathSize - 1]);
        Log.i("stikerCropResId is ", String.valueOf(stikerCropResId));

        String stikerPathMask = "drawable://" + R.drawable.mask_ext3_onviewbg;
        String[] arrMask = stikerPathMask.split("//");
        String stikerNewPathMask = arrMask[1];
        String[] arrNewPathMask = stikerNewPathMask.split("/");
        int arrNewPathSizeMask = arrNewPathMask.length;
        int stikerCropResIdMask = Integer.parseInt(arrNewPathMask[arrNewPathSizeMask - 1]);

        stikerCropSorterView.setVisibility(View.VISIBLE);
        stikerCropSorterView.loadImages(stikerCropContext, stikerCropResId, stikerCropResIdMask,
            stikerCropSorterView,
            widthOfcropviewStikerCropLayout,
            heightOfcropviewStikerCropLayout, rootView);

      }

    });

    btChoose_ext_MaskImageView4 = (ImageView) findViewById(R.id.button_choose_extMask4);
    btChoose_ext_MaskImageView4.setImageResource(R.drawable.mask_ext4);
    btChoose_ext_MaskImageView4.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        stikerCropSorterView.mImgArryList.clear();

        btChoose_circle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_rectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_roundrectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_star_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_heart_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView0.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView1.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView2.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView3.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView4.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        btChoose_ext_MaskImageView5.setBackgroundColor(Color.parseColor("#88FFFFFF"));

        String stikerPath = "drawable://" + R.drawable.mask_ext4_onview;
        String[] arr = stikerPath.split("//");
        String stikerNewPath = arr[1];
        String[] arrNewPath = stikerNewPath.split("/");
        int arrNewPathSize = arrNewPath.length;

        int stikerCropResId = Integer.parseInt(arrNewPath[arrNewPathSize - 1]);
        Log.i("stikerCropResId is ", String.valueOf(stikerCropResId));

        String stikerPathMask = "drawable://" + R.drawable.mask_ext4_onviewbg;
        String[] arrMask = stikerPathMask.split("//");
        String stikerNewPathMask = arrMask[1];
        String[] arrNewPathMask = stikerNewPathMask.split("/");
        int arrNewPathSizeMask = arrNewPathMask.length;
        int stikerCropResIdMask = Integer.parseInt(arrNewPathMask[arrNewPathSizeMask - 1]);

        stikerCropSorterView.setVisibility(View.VISIBLE);
        stikerCropSorterView.loadImages(stikerCropContext, stikerCropResId, stikerCropResIdMask,
            stikerCropSorterView,
            widthOfcropviewStikerCropLayout,
            heightOfcropviewStikerCropLayout, rootView);

      }

    });

    btChoose_ext_MaskImageView5 = (ImageView) findViewById(R.id.button_choose_extMask5);
    btChoose_ext_MaskImageView5.setImageResource(R.drawable.mask_ext5);
    btChoose_ext_MaskImageView5.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        stikerCropSorterView.mImgArryList.clear();

        btChoose_circle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_rectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_roundrectangle_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_star_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_heart_MaskImageView.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView0.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView1.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView2.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView3.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView4.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        btChoose_ext_MaskImageView5.setBackgroundColor(Color.parseColor("#FFFFFFFF"));

        String stikerPath = "drawable://" + R.drawable.mask_ext5_onview;
        String[] arr = stikerPath.split("//");
        String stikerNewPath = arr[1];
        String[] arrNewPath = stikerNewPath.split("/");
        int arrNewPathSize = arrNewPath.length;

        int stikerCropResId = Integer.parseInt(arrNewPath[arrNewPathSize - 1]);
        Log.i("stikerCropResId is ", String.valueOf(stikerCropResId));

        String stikerPathMask = "drawable://" + R.drawable.mask_ext5_onviewbg;
        String[] arrMask = stikerPathMask.split("//");
        String stikerNewPathMask = arrMask[1];
        String[] arrNewPathMask = stikerNewPathMask.split("/");
        int arrNewPathSizeMask = arrNewPathMask.length;
        int stikerCropResIdMask = Integer.parseInt(arrNewPathMask[arrNewPathSizeMask - 1]);

        stikerCropSorterView.setVisibility(View.VISIBLE);
        stikerCropSorterView.loadImages(stikerCropContext, stikerCropResId, stikerCropResIdMask,
            stikerCropSorterView,
            widthOfcropviewStikerCropLayout,
            heightOfcropviewStikerCropLayout, rootView);
      }

    });


    /*
    Button btSave = (Button) findViewById(R.id.saveStikerCrop);
    btSave.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        new SaveAsyncTask().execute(0);

      }
    });

    Button btClose = (Button) findViewById(R.id.closeStikerCrop);
    btClose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
    */

    ImageView btnOk = (ImageView) findViewById(R.id.ivOk);
    btnOk.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        new SaveAsyncTask().execute(0);
      }
    });


    ImageView btnClose = (ImageView) findViewById(R.id.ivClose);
    btnClose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });


    ImageView btnCropDin = (ImageView) findViewById(R.id.ivCropDin);
    btnCropDin.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {


        //stikerCropSorterView.invalidate();

        StikerCropSortrView.UI_MODE_ANISOTROPIC_SCALE = 0;


        Log.i("sdsds", String.valueOf(StikerCropSortrView.UI_MODE_ANISOTROPIC_SCALE));



      }
    });

    ImageView btnCropLand= (ImageView) findViewById(R.id.ivCropLand);
    btnCropLand.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        //stikerCropSorterView.invalidate();

        StikerCropSortrView.UI_MODE_ANISOTROPIC_SCALE = 2;

        Log.i("sdsds", String.valueOf(StikerCropSortrView.UI_MODE_ANISOTROPIC_SCALE));




      }
    });


  }


/*
  File 	: execute, doInBackground 의 파라미터 타입
  Integer	: onProgressUpdate 의 파라미터 타입
  Int	: doInBackground 의 리턴값, onPostExecute 의 파라미터로 설정됩니다.
  */

  public class SaveAsyncTask extends AsyncTask<Integer, Integer, Integer> {

    ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
      //btn.setText("Thread START!!!!");

      dialog = ProgressDialog.show(cropStickerActivity.this, "",
          "적용중입니다.", true);

      super.onPreExecute();

      if (stikerCropSorterView.mImgArryList.size() > 0) {

        Log.i("w maskDrawable", String.valueOf(stikerCropSorterView.mImgArryList.get(0).getWidth()));
        Log.i("h maskDrawable", String.valueOf(stikerCropSorterView.mImgArryList.get(0).getHeight()));

        Log.i("w cropImageView", String.valueOf(cropImageView.getMeasuredWidth()));
        Log.i("h cropImageView", String.valueOf(cropImageView.getMeasuredHeight()));

        Log.i("w toAdjustBitmap", String.valueOf(toAdjustBitmap.getWidth()));
        Log.i("h toAdjustBitmap", String.valueOf(toAdjustBitmap.getHeight()));
        Log.i("w toAdjustBitmapScaleUp", String.valueOf(toAdjustBitmapScaleUP.getWidth()));
        Log.i("h toAdjustBitmapScaleUp", String.valueOf(toAdjustBitmapScaleUP.getHeight()));

        Log.i("ScaleUpRatioInt", String.valueOf(ScaleUpRatioInt));

        Log.i("w actW", String.valueOf(cropImageView.getActW()));
        Log.i("w actH", String.valueOf(cropImageView.getActH()));

        Log.i("w ", String.valueOf(widthOfcropviewStikerCropLayout));
        Log.i("h ", String.valueOf(heightOfcropviewStikerCropLayout));

        float scaledWidthDrawable = stikerCropSorterView.mImgArryList.get(0).scaledWidthDrawable;
        float scaledHeightDrawable = stikerCropSorterView.mImgArryList.get(0).scaledHeightDrawable;

        float scaledWidthDrawableScaleUp = stikerCropSorterView.mImgArryList.get(0).scaledWidthDrawable * (1.0f / cropImageView.getScaleXImageView());
        float scaledHeightDrawableScaleUp = stikerCropSorterView.mImgArryList.get(0).scaledHeightDrawable * (1.0f / cropImageView.getScaleYImageView());
        float scaledWidthDrawableScaleUpOutSide = scaledWidthDrawableScaleUp;
        float scaledHeightDrawableScaleUpOutSide = scaledHeightDrawableScaleUp;

        float minX2 = 0.0f;
        float minY2 = 0.0f;
        float maxX2 = 0.0f;
        float maxY2 = 0.0f;

        float minX2ScaleUp = 0.0f;
        float minY2ScaleUp = 0.0f;
        float maxX2ScaleUp = 0.0f;
        float maxY2ScaleUp = 0.0f;

        resultBitmap = null;

        float scaleNormal = 1.0f;
        float scaleXState = cropImageView.getScaleXImageView();

        if (cropImageView.getDrawable().getIntrinsicHeight() < heightOfcropviewStikerCropLayout) {

          minX2 = stikerCropSorterView.mImgArryList.get(0).minX
              - ((float) widthOfcropviewStikerCropLayout / 2
              - (float) cropImageView.getActW() / 2);
          minY2 = stikerCropSorterView.mImgArryList.get(0).minY
              - ((float) heightOfcropviewStikerCropLayout / 2
              - (float) cropImageView.getActH() / 2);

          maxX2 = stikerCropSorterView.mImgArryList.get(0).maxX
              - ((float) widthOfcropviewStikerCropLayout / 2
              - (float) cropImageView.getActW() / 2);
          maxY2 = stikerCropSorterView.mImgArryList.get(0).maxY
              - ((float) heightOfcropviewStikerCropLayout / 2
              - (float) cropImageView.getActH() / 2);

          Bitmap mask = stikerCropSorterView.mImgArryList.get(0).forMaskBitmap;

          if (cropImageView.getDrawable().getIntrinsicHeight() < heightOfcropviewStikerCropLayout) {

          } else {

          }

          if (minX2 < 0) {

            scaledWidthDrawable = scaledWidthDrawable + minX2;
            Log.i("scaledWidthDrawable is", String.valueOf(scaledWidthDrawable));
            Log.i("scaledHeightDrawable is", String.valueOf(scaledHeightDrawable));

            mask = Bitmap.createBitmap(
                mask,
                (int) -(minX2), //부호반전
                (int) 0,
                (int) scaledWidthDrawable,
                (int) scaledHeightDrawable);

            minX2 = 0;

          }

          if (minY2 < 0) {
            scaledHeightDrawable = scaledHeightDrawable + minY2;

            mask = Bitmap.createBitmap(
                mask,
                0,
                (int) -(minY2),
                (int) scaledWidthDrawable,
                (int) scaledHeightDrawable);

            minY2 = 0;
          }


          if (maxX2 > cropImageView.getActW()) {
            scaledWidthDrawable = scaledWidthDrawable - (maxX2 - cropImageView.getActW());

            Log.i("scaledWidth is ", String.valueOf(scaledWidthDrawable));
            Log.i("scaledHeight is ", String.valueOf(scaledHeightDrawable));

            mask = Bitmap.createBitmap(
                mask,
                0,
                0,
                (int) scaledWidthDrawable,
                (int) scaledHeightDrawable);

            maxX2 = cropImageView.getActW();
          }

          if (maxY2 > cropImageView.getActH()) {
            scaledHeightDrawable = scaledHeightDrawable - (maxY2 - (cropImageView.getActH()));
            Log.i("scaledWidth is ", String.valueOf(scaledWidthDrawable));
            Log.i("scaledHeight is ", String.valueOf(scaledHeightDrawable));

            mask = Bitmap.createBitmap(
                mask,
                0,
                0,
                (int) scaledWidthDrawable,
                (int) scaledHeightDrawable);

            maxY2 = (maxY2 - (cropImageView.getActH()));
          }


          Log.i("sdsds", String.valueOf(minY2));

          toAdjustBitmapScaleUP = Bitmap.createBitmap(
              toAdjustBitmapScaleUP,
              (int) minX2,
              (int) minY2,
              (int) scaledWidthDrawable,
              (int) scaledHeightDrawable);

          toAdjustBitmapScaleUP = Bitmap.createScaledBitmap(
              toAdjustBitmapScaleUP, (int) scaledWidthDrawable, (int) scaledHeightDrawable, true);
          resultBitmap = Bitmap.createBitmap(
              (int) scaledWidthDrawable,
              (int) scaledHeightDrawable, Config.ARGB_8888);

          Canvas mCanvas = new Canvas(resultBitmap);
          Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
          paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
          mCanvas.drawBitmap(toAdjustBitmapScaleUP, 0, 0, null);
          mCanvas.drawBitmap(mask, 0, 0, paint);
          paint.setXfermode(null);
          //cropImageView.setImageBitmap(result);

        } else {

          minX2ScaleUp = stikerCropSorterView.mImgArryList.get(0).minX
              - ((float) widthOfcropviewStikerCropLayout / 2
              - (float) cropImageView.getActW() / 2);
          minX2ScaleUp = minX2ScaleUp * (1.0f / cropImageView.getScaleXImageView());

          minY2ScaleUp = stikerCropSorterView.mImgArryList.get(0).minY
              - ((float) heightOfcropviewStikerCropLayout / 2
              - (float) cropImageView.getActH() / 2);
          minY2ScaleUp = minY2ScaleUp * (1.0f / cropImageView.getScaleYImageView());

          maxX2ScaleUp = stikerCropSorterView.mImgArryList.get(0).maxX
              - ((float) widthOfcropviewStikerCropLayout / 2
              - (float) cropImageView.getActW() / 2);
          maxX2ScaleUp = maxX2ScaleUp * (1.0f / cropImageView.getScaleXImageView());

          maxY2ScaleUp = stikerCropSorterView.mImgArryList.get(0).maxY
              - ((float) heightOfcropviewStikerCropLayout / 2
              - (float) cropImageView.getActH() / 2);
          maxY2ScaleUp = maxY2ScaleUp * (1.0f / cropImageView.getScaleYImageView());

          Bitmap mask = stikerCropSorterView.mImgArryList.get(0).forMaskBitmap;

          mask = Bitmap.createScaledBitmap(
              mask
              , (int) scaledWidthDrawableScaleUpOutSide
              , (int) scaledHeightDrawableScaleUpOutSide, true);

          //float leftStart = (displayWidth/2 - toAdjustBitmapScaleUP.getWidth()/2);
          if (minX2ScaleUp < 0) {
            scaledWidthDrawableScaleUpOutSide = scaledWidthDrawableScaleUpOutSide + minX2ScaleUp;
            Log.i("scaledWidth is ", String.valueOf(scaledWidthDrawableScaleUpOutSide));

            mask = Bitmap.createBitmap(
                mask,
                (int) -(minX2ScaleUp), //부호반전
                (int) 0,
                (int) scaledWidthDrawableScaleUpOutSide,
                (int) scaledHeightDrawableScaleUpOutSide);

            minX2ScaleUp = 0;
          }

          if (minY2ScaleUp < 0) {
            scaledHeightDrawableScaleUpOutSide = scaledHeightDrawableScaleUpOutSide + minY2ScaleUp;

            mask = Bitmap.createBitmap(
                mask,
                0,
                (int) -(minY2ScaleUp),
                (int) scaledWidthDrawableScaleUpOutSide,
                (int) scaledHeightDrawableScaleUpOutSide);

            minY2ScaleUp = 0;
          }

          if (maxX2ScaleUp > toAdjustBitmapScaleUP.getWidth()) {
            scaledWidthDrawableScaleUpOutSide = scaledWidthDrawableScaleUpOutSide - (maxX2ScaleUp - toAdjustBitmapScaleUP.getWidth());
            Log.i("scaledWidth is ", String.valueOf(scaledWidthDrawableScaleUpOutSide));

            mask = Bitmap.createBitmap(
                mask,
                0,
                0,
                (int) scaledWidthDrawableScaleUpOutSide,
                (int) scaledHeightDrawableScaleUpOutSide);

            maxX2ScaleUp = toAdjustBitmapScaleUP.getWidth();
          }


          if (maxY2ScaleUp > toAdjustBitmapScaleUP.getHeight()) {
            scaledHeightDrawableScaleUpOutSide = scaledHeightDrawableScaleUpOutSide - (maxY2ScaleUp - (toAdjustBitmapScaleUP.getHeight()));
            Log.i("scaledHeight is ", String.valueOf(scaledHeightDrawableScaleUpOutSide));

            mask = Bitmap.createBitmap(
                mask,
                0,
                0,
                (int) scaledWidthDrawableScaleUpOutSide,
                (int) scaledHeightDrawableScaleUpOutSide);

            maxY2ScaleUp = toAdjustBitmapScaleUP.getHeight();
          }

          //if(minX2ScaleUp >= 0 && minY2ScaleUp >= 0 && maxX2ScaleUp <= cropImageView.getActW() && maxY2ScaleUp <= cropImageView.getActH()) {
          //}

          toAdjustBitmapScaleUP = Bitmap.createBitmap(
              toAdjustBitmapScaleUP,
              (int) minX2ScaleUp,
              (int) minY2ScaleUp,
              (int) scaledWidthDrawableScaleUpOutSide,
              (int) scaledHeightDrawableScaleUpOutSide);

          toAdjustBitmapScaleUP = Bitmap.createScaledBitmap(
              toAdjustBitmapScaleUP,
              (int) scaledWidthDrawableScaleUpOutSide,
              (int) scaledHeightDrawableScaleUpOutSide, true);

          resultBitmap = Bitmap.createBitmap(
              (int) scaledWidthDrawableScaleUpOutSide,
              (int) scaledHeightDrawableScaleUpOutSide, Config.ARGB_8888);

          Canvas mCanvas = new Canvas(resultBitmap);
          Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
          paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
          mCanvas.drawBitmap(toAdjustBitmapScaleUP, 0, 0, null);
          mCanvas.drawBitmap(mask, 0, 0, paint);
          paint.setXfermode(null);

        }


      } else {

        Toast.makeText(cropStickerActivity.this, "오려붙힐 모양을 선택해 주세요!",
            Toast.LENGTH_SHORT).show();

      }


    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      //pb.setProgress(values[0]);
      super.onProgressUpdate(values);
    }

    @Override
    protected Integer doInBackground(Integer... params) {


      File file = new File(PATH_CROP_IMAGE_DIR);
      if (!file.exists()) { // 원하는 경로에 폴더가 있는지 확인
        file.mkdirs();
      }

      maskName = null;
      try {


        maskName = "croped_" + FILE_SAVE_TIME + ".png";
        //maskName = "croped_" + Long.toString(System.currentTimeMillis()) + ".png";
        File finalImage = new File(PATH_CROP_IMAGE_DIR, maskName);
        FileOutputStream out = new FileOutputStream(finalImage);
        resultBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

	/*
         BitmapFactory.Options options = new BitmapFactory.Options();
	 ptions.inSampleSize = 4;
	 result = BitmapFactory.decodeFile(PATH_CROP_IMAGE_DIR + maskName, options);
	 SaveBitmapToFileCache(result, PATH_CROP_IMAGE_DIR + maskName);
	*/

      } catch (Exception e) {
        e.printStackTrace();
      }

      return 1;


    }


    @Override
    protected void onCancelled() {
      super.onCancelled();
    }

    @Override
    protected void onPostExecute(Integer result) {
      //btn.setText("Thread END");
      super.onPostExecute(result);


      if (null != dialog && dialog.isShowing()) {
        dialog.dismiss();


        if (resultBitmap != null) {

          // 임시 파일 지운다.
          File tempFile = new File (FULL_PATH_CROP_IMAGE);
          tempFile.delete();

          BitmapDrawable resultDrawable = new BitmapDrawable(getResources(), resultBitmap);
          BitmapDrawable bD = compositeDrawableWithMask(getResources(), resultDrawable);
          Bitmap compositeBitmap = drawableToBitmap(bD);

          String compositeName = null;
          try {
            compositeName = "border_" + FILE_SAVE_TIME + ".png";
            //compositeName = "border_" + Long.toString(System.currentTimeMillis()) + ".png";
            File finalImage = new File(PATH_CROP_IMAGE_DIR, compositeName);
            FileOutputStream out = new FileOutputStream(finalImage);
            compositeBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
          } catch (Exception e) {
            e.printStackTrace();
          }


          Intent intent = new Intent(cropStickerActivity.this, borderStickerActivity.class);
          intent.putExtra("STIKERCROP_FILENAME", maskName);
          intent.putExtra("STIKERBORDER_FILENAME", compositeName);
          intent.putExtra("WIDTH_DISPLAY", widthOfcropviewStikerCropLayout);
          intent.putExtra("HEIGHT_DISPLAY", heightOfcropviewStikerCropLayout);

          startActivity(intent);
          finish();


        } else {

          Toast.makeText(cropStickerActivity.this, "오려붙힐 모양을 선택해 주세요!",
              Toast.LENGTH_SHORT).show();

        }


      }
    }
  }


  public interface OnCropImageListener {
    void onCropImageListener(Bitmap bitm2ap);
  }

  /**
   * Get Bitmap's Width
   **/
  public static int getBitmapOfWidth(String fileName) {
    try {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(fileName, options);
      return options.outWidth;
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * Get Bitmap's height
   **/
  public static int getBitmapOfHeight(String fileName) {

    try {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(fileName, options);

      return options.outHeight;
    } catch (Exception e) {
      return 0;
    }
  }

  public Uri getImageUri(Context inContext, Bitmap inImage) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
    String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
    return Uri.parse(path);
  }

  public String getRealPathFromURI(Uri uri) {
    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
    cursor.moveToFirst();
    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
    return cursor.getString(idx);
  }

  private void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {

    File fileCacheItem = new File(strFilePath);
    OutputStream out = null;

    try {
      fileCacheItem.createNewFile();
      out = new FileOutputStream(fileCacheItem);
      bitmap.compress(CompressFormat.JPEG, 100, out);

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static boolean copyFile(File srcFile, File destFile) {

    FileInputStream inputStream = null;
    FileOutputStream outputStream = null;
    FileChannel fcin = null;
    FileChannel fcout = null;

    try {
      inputStream = new FileInputStream(srcFile);
      outputStream = new FileOutputStream(destFile);
      fcin = inputStream.getChannel();
      fcout = outputStream.getChannel();

      long size = fcin.size();
      fcin.transferTo(0, size, fcout);
      //fcout = null;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {

      try {
        fcout.close();
      } catch (IOException ioe) {
        return false;
      }
      try {
        fcin.close();
      } catch (IOException ioe) {
        return false;
      }
      try {
        outputStream.close();
      } catch (IOException ioe) {
        return false;
      }
      try {
        inputStream.close();
      } catch (IOException ioe) {
        return false;
      }
    }
    return true;
  }


  private File BackupBitmap(Bitmap bitmap, String strFilePath) {

    String strBACKUPFILE = strFilePath + "_ORG";
    Log.i("strBACKUPFILE is ", strBACKUPFILE);

    File ORGfile = new File(strFilePath);
    ORGfile.renameTo(new File(strBACKUPFILE));

    return ORGfile;

  }

  public static Bitmap createTransparentBitmapFromBitmap(Bitmap bitmap, int replaceThisColor) {
    if (bitmap != null) {
      int picw = bitmap.getWidth();
      int pich = bitmap.getHeight();
      int[] pix = new int[picw * pich];
      bitmap.getPixels(pix, 0, picw, 0, 0, picw, pich);

      for (int y = 0; y < pich; y++) {
        for (int x = 0; x < picw; x++) {
          int index = y * picw + x;
          int r = (pix[index] >> 16) & 0xff;
          int g = (pix[index] >> 8) & 0xff;
          int b = pix[index] & 0xff;
          if (pix[index] != replaceThisColor)
            pix[index] = Color.TRANSPARENT;
        }
      }

      Bitmap bm = Bitmap.createBitmap(pix, picw, pich, Bitmap.Config.ARGB_8888);

      return bm;
    }
    return null;
  }

  public static Bitmap drawableToBitmap(Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    }

    int width = drawable.getIntrinsicWidth();
    width = width > 0 ? width : 1;
    int height = drawable.getIntrinsicHeight();
    height = height > 0 ? height : 1;

    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);

    return bitmap;
  }


  @Override
  public void onWindowFocusChanged(boolean hasFocus) {


    View layoutMainView = (View) this.findViewById(R.id.cropviewStikerCrop);
    widthOfcropviewStikerCropLayout = layoutMainView.getWidth();
    heightOfcropviewStikerCropLayout = layoutMainView.getHeight();
    Log.w("Layout Width - ", String.valueOf(layoutMainView.getWidth()));
    Log.w("Layout Height - ", String.valueOf(layoutMainView.getHeight()));

  }

  /*
   public static BitmapDrawable compositeDrawableWithMask(Resources resources,
                      BitmapDrawable rgbDrawable, BitmapDrawable alphaDrawable) {
                    Bitmap rgbBitmap = rgbDrawable.getBitmap();
                    Bitmap alphaBitmap = alphaDrawable.getBitmap();
                    int width = rgbBitmap.getWidth();
                    int height = rgbBitmap.getHeight();
                    if (width != alphaBitmap.getWidth() || height != alphaBitmap.getHeight()) {
                      throw new IllegalStateException("image size mismatch!");
                    }

                    Bitmap destBitmap = Bitmap.createBitmap(width, height,
                        Bitmap.Config.ARGB_8888);

                    int[] pixels = new int[width];
                    int[] alpha = new int[width];
                    for (int y = 0; y < height; y++) {
                      rgbBitmap.getPixels(pixels, 0, width, 0, y, width, 1);
                      alphaBitmap.getPixels(alpha, 0, width, 0, y, width, 1);

                      for (int x = 0; x < width; x++) {
                        // Replace the alpha channel with the r value from the bitmap.
                        pixels[x] = (pixels[x] & 0x00FFFFFF) | ((alpha[x] << 8) & 0xFF000000);
                      }
                      destBitmap.setPixels(pixels, 0, width, 0, y, width, 1);
                    }

                    return new BitmapDrawable(resources, destBitmap);
  }
  */
  public static BitmapDrawable compositeDrawableWithMask(Resources resources,
                                                         BitmapDrawable rgbDrawable) {
    Bitmap rgbBitmap = rgbDrawable.getBitmap();
    //Bitmap alphaBitmap = alphaDrawable.getBitmap();
    int width = rgbBitmap.getWidth();
    int height = rgbBitmap.getHeight();
		  /*
		  if (width != alphaBitmap.getWidth() || height != alphaBitmap.getHeight()) {
		    throw new IllegalStateException("image size mismatch!");
		  }
		   */
    Bitmap destBitmap = Bitmap.createBitmap(width, height,
        Bitmap.Config.ARGB_8888);

    int[] pixels = new int[width];
    //int[] alpha = new int[width];
    for (int y = 0; y < height; y++) {
      rgbBitmap.getPixels(pixels, 0, width, 0, y, width, 1);
      //alphaBitmap.getPixels(alpha, 0, width, 0, y, width, 1);

      for (int x = 0; x < width; x++) {
        // Replace the alpha channel with the r value from the bitmap.
        // pixels[x] = (pixels[x] & 0x00FFFFFF) | ((alpha[x] << 8) & 0xFF000000);
        pixels[x] = (0x00FFFFFF) | (pixels[x]);
      }
      destBitmap.setPixels(pixels, 0, width, 0, y, width, 1);
    }

    return new BitmapDrawable(resources, destBitmap);
  }


  public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                       int reqWidth, int reqHeight) {

    // First decode with inJustDecodeBounds=true to check dimensions
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeResource(res, resId, options);

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeResource(res, resId, options);
  }

  public static int calculateInSampleSize(
      BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

      final int halfHeight = height / 2;
      final int halfWidth = width / 2;

      // Calculate the largest inSampleSize value that is a power of 2 and keeps both
      // height and width larger than the requested height and width.
      while ((halfHeight / inSampleSize) > reqHeight
          && (halfWidth / inSampleSize) > reqWidth) {
        inSampleSize *= 2;
      }
    }

    return inSampleSize;
  }


}
