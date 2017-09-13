package com.ilovecat.happytogether.ControllerCropBorderSticker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.ilovecat.happytogether.MainShopRegisterCoupon.MainRegisterCouponShopStep1;
import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.StickerClass.StickerShopLogoImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class borderStickerActivity extends Activity implements OnSeekBarChangeListener {

  Context stikerBorderContext;
  ImageView cropStiker;
  //ImageView borderImageBg;
  String PATH_CROP_IMAGE_DIR = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/HappyTogether/CropImage/";
  int Ratio = 0;

  int compositeBitmapWidth;
  int compositeBitmapHeight;

  String cropImage_FileName;
  String borderImage_FileName;

  Bitmap bmStikerBorder;
  Bitmap overlayedBitmap;
  Bitmap bmStikerCrop;

  float scaledWidth;
  float scaledHeight;

  Bitmap bmStikerBorderScaleUp;

  Bitmap maskScaled;

  int widthMaskScaled;
  int heightMaskScaled;

  Bitmap bmPatternScaled;
  Bitmap bmPattern;

  int pagerPosition;

  int WIDTH_DISPLAY;
  int HEIGHT_DISPLAY;

  View rootView;

  @SuppressWarnings("deprecation")
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.stikerborder);

    stikerBorderContext = getApplicationContext();
    final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    rootView = inflater.inflate(R.layout.stikerborder, null);

    ImageView ivSclae0 = (ImageView) findViewById(R.id.ivScale0);
    Drawable drawableScale0 = getResources().getDrawable(R.drawable.ic_scales0);
    ivSclae0.setImageDrawable(drawableScale0);

    ImageView ivSclae1 = (ImageView) findViewById(R.id.ivScale1);
    Drawable drawableScale1 = getResources().getDrawable(R.drawable.ic_scales1);
    ivSclae1.setImageDrawable(drawableScale1);

    final NumberSeekBar sbStikerBorder = (NumberSeekBar) findViewById(R.id.seekbar_stikerborder);
    sbStikerBorder.setProgress(1);
    sbStikerBorder.setOnSeekBarChangeListener(this);
    //sbStikerBorder.incrementProgressBy(1);
    sbStikerBorder.setTextSize(40);// 设置字体大小
    sbStikerBorder.setTextColor(Color.WHITE);// 颜色
    //sbStikerBorder.setMyPadding(10, 10, 10, 10);// 设置padding 调用setpadding会无效
    sbStikerBorder.setImagePadding(0, 1);// 可以不设置
    sbStikerBorder.setTextPadding(0, 0);// 可以不设置


    //borderImageBg = (ImageView) findViewById(R.id.BorderImageBg);
    cropStiker = (ImageView) findViewById(R.id.BorderImageViewBorderStiker);

    Intent intent = getIntent();
    cropImage_FileName = intent.getExtras().getString("STIKERCROP_FILENAME");
    borderImage_FileName = intent.getExtras().getString("STIKERBORDER_FILENAME");
    //pagerPosition = intent.getExtras().getInt("PAGER_POSITION");
    WIDTH_DISPLAY = intent.getExtras().getInt("WIDTH_DISPLAY");
    HEIGHT_DISPLAY = intent.getExtras().getInt("HEIGHT_DISPLAY");


    ImageView ivFrameNo = (ImageView) findViewById(R.id.button_choose_borderNone);
    final Drawable drawableNo = getResources().getDrawable(R.drawable.frame_no);
    ivFrameNo.setImageDrawable(drawableNo);

    ImageView ivFrameGray = (ImageView) findViewById(R.id.button_choose_grayBorder);
    final Drawable drawableGray = getResources().getDrawable(R.drawable.frame_gray);
    ivFrameGray.setImageDrawable(drawableGray);

    ImageView ivFrameBlack = (ImageView) findViewById(R.id.button_choose_blackBorder);
    final Drawable drawableBlack = getResources().getDrawable(R.drawable.frame_black);
    ivFrameBlack.setImageDrawable(drawableBlack);

    ImageView ivFrameOrange = (ImageView) findViewById(R.id.button_choose_orangeBorder);
    final Drawable drawableOrange = getResources().getDrawable(R.drawable.frame_orange);
    ivFrameOrange.setImageDrawable(drawableOrange);

    ImageView ivFramePattern0 = (ImageView) findViewById(R.id.button_choose_imageBorder0);
    final Drawable drawablePattern0 = getResources().getDrawable(R.drawable.frame_pattern22);
    ivFramePattern0.setImageDrawable(drawablePattern0);

    ImageView ivFramePattern1 = (ImageView) findViewById(R.id.button_choose_imageBorder1);
    final Drawable drawablePattern1 = getResources().getDrawable(R.drawable.frame_pattern2);
    ivFramePattern1.setImageDrawable(drawablePattern1);

    ImageView ivFramePattern2 = (ImageView) findViewById(R.id.button_choose_imageBorder2);
    final Drawable drawablePattern2 = getResources().getDrawable(R.drawable.frame_pattern3);
    ivFramePattern2.setImageDrawable(drawablePattern2);

    ImageView ivFramePattern3 = (ImageView) findViewById(R.id.button_choose_imageBorder3);
    final Drawable drawablePattern3 = getResources().getDrawable(R.drawable.frame_pattern4);
    ivFramePattern3.setImageDrawable(drawablePattern3);

    ImageView ivFramePattern4 = (ImageView) findViewById(R.id.button_choose_imageBorder4);
    final Drawable drawablePattern4 = getResources().getDrawable(R.drawable.frame_pattern5);
    ivFramePattern4.setImageDrawable(drawablePattern4);

    ImageView ivFramePattern5 = (ImageView) findViewById(R.id.button_choose_imageBorder5);
    final Drawable drawablePattern5 = getResources().getDrawable(R.drawable.frame_pattern6);
    ivFramePattern5.setImageDrawable(drawablePattern5);

    ImageView ivFramePattern6 = (ImageView) findViewById(R.id.button_choose_imageBorder6);
    final Drawable drawablePattern6 = getResources().getDrawable(R.drawable.frame_pattern18);
    ivFramePattern6.setImageDrawable(drawablePattern6);



    /*
    패톤없음으로 초기화
     */
    RelativeLayout layout = (RelativeLayout) findViewById(R.id.seekbarAndScale_layout);
    layout.setVisibility(View.GONE);


    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inSampleSize = 4;
    bmStikerCrop = BitmapFactory.decodeFile(PATH_CROP_IMAGE_DIR + cropImage_FileName, options);
    Bitmap mask = BitmapFactory.decodeFile(PATH_CROP_IMAGE_DIR + borderImage_FileName, options);


    float widthMaskF = (float) mask.getWidth() * 1.0f;
    float heightMaskF = (float) mask.getHeight() * 1.0f;
    widthMaskScaled = (int) widthMaskF;
    heightMaskScaled = (int) heightMaskF;

    maskScaled = Bitmap.createScaledBitmap(
        mask, (int) widthMaskScaled, (int) heightMaskScaled, true);

    bmPattern = drawableToBitmap(drawableNo);
    bmPatternScaled = Bitmap.createScaledBitmap(
        bmPattern, (int) widthMaskScaled, (int) heightMaskScaled, true);

    Bitmap result = Bitmap.createBitmap(
        (int) widthMaskScaled,
        (int) heightMaskScaled, Config.ARGB_8888);

    Canvas mCanvas = new Canvas(result);
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    mCanvas.drawBitmap(bmPatternScaled, 0, 0, null);
    mCanvas.drawBitmap(maskScaled, 0, 0, paint);
    paint.setXfermode(null);

    int widthBmStikerCrop = bmStikerCrop.getWidth();
    int heightBmStikerCrop = bmStikerCrop.getHeight();

    int distanceLeft = (widthMaskScaled - widthBmStikerCrop) / 2;
    int distanceTop = (heightMaskScaled - heightBmStikerCrop) / 2;

    overlayedBitmap = overlayMark(result, bmStikerCrop, distanceLeft, distanceTop);

    cropStiker.setImageBitmap(overlayedBitmap);
    //cropStiker.setImageBitmap(bmStikerCrop);


    ivFrameNo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.seekbarAndScale_layout);
        layout.setVisibility(View.GONE);

        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = 1;
        Bitmap mask = BitmapFactory.decodeFile(PATH_CROP_IMAGE_DIR + borderImage_FileName, options2);

        float widthMaskF = (float) mask.getWidth() * 1.0f;
        float heightMaskF = (float) mask.getHeight() * 1.0f;
        int widthMaskScaled = (int) widthMaskF;
        int heightMaskScaled = (int) heightMaskF;

        Bitmap maskScaled = Bitmap.createScaledBitmap(
            mask, (int) widthMaskScaled, (int) heightMaskScaled, true);

        bmPattern = drawableToBitmap(drawableNo);
        Bitmap bmPatternScaled = Bitmap.createScaledBitmap(
            bmPattern, (int) widthMaskScaled, (int) heightMaskScaled, true);

        Bitmap result = Bitmap.createBitmap(
            (int) widthMaskScaled,
            (int) heightMaskScaled, Config.ARGB_8888);

        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(bmPatternScaled, 0, 0, null);
        mCanvas.drawBitmap(maskScaled, 0, 0, paint);
        paint.setXfermode(null);

        int widthBmStikerCrop = bmStikerCrop.getWidth();
        int heightBmStikerCrop = bmStikerCrop.getHeight();

        int distanceLeft = (widthMaskScaled - widthBmStikerCrop) / 2;
        int distanceTop = (heightMaskScaled - heightBmStikerCrop) / 2;

        overlayedBitmap = overlayMark(result, bmStikerCrop, distanceLeft, distanceTop);

        cropStiker.setImageBitmap(overlayedBitmap);
      }
    });


    ivFrameGray.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.seekbarAndScale_layout);
        layout.setVisibility(View.VISIBLE);

        sbStikerBorder.setProgress(1);

        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = 1;
        Bitmap mask = BitmapFactory.decodeFile(PATH_CROP_IMAGE_DIR + borderImage_FileName, options2);

        float widthMaskF = (float) mask.getWidth() * 1.1f;
        float heightMaskF = (float) mask.getHeight() * 1.1f;
        widthMaskScaled = (int) widthMaskF;
        heightMaskScaled = (int) heightMaskF;

        maskScaled = Bitmap.createScaledBitmap(
            mask, (int) widthMaskScaled, (int) heightMaskScaled, true);

        bmPattern = drawableToBitmap(drawableGray);
        bmPatternScaled = Bitmap.createScaledBitmap(
            bmPattern, (int) widthMaskScaled, (int) heightMaskScaled, true);

        Bitmap result = Bitmap.createBitmap(
            (int) widthMaskScaled,
            (int) heightMaskScaled, Config.ARGB_8888);

        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(bmPatternScaled, 0, 0, null);
        mCanvas.drawBitmap(maskScaled, 0, 0, paint);
        paint.setXfermode(null);


        int widthBmStikerCrop = bmStikerCrop.getWidth();
        int heightBmStikerCrop = bmStikerCrop.getHeight();

        int distanceLeft = (widthMaskScaled - widthBmStikerCrop) / 2;
        int distanceTop = (heightMaskScaled - heightBmStikerCrop) / 2;

        overlayedBitmap = overlayMark(result, bmStikerCrop, distanceLeft, distanceTop);

        cropStiker.setImageBitmap(overlayedBitmap);

      }
    });

    ivFrameBlack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.seekbarAndScale_layout);
        layout.setVisibility(View.VISIBLE);

        sbStikerBorder.setProgress(1);

        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = 1;
        Bitmap mask = BitmapFactory.decodeFile(PATH_CROP_IMAGE_DIR + borderImage_FileName, options2);

        float widthMaskF = (float) mask.getWidth() * 1.1f;
        float heightMaskF = (float) mask.getHeight() * 1.1f;
        widthMaskScaled = (int) widthMaskF;
        heightMaskScaled = (int) heightMaskF;

        maskScaled = Bitmap.createScaledBitmap(
            mask, (int) widthMaskScaled, (int) heightMaskScaled, true);

        bmPattern = drawableToBitmap(drawableBlack);
        bmPatternScaled = Bitmap.createScaledBitmap(
            bmPattern, (int) widthMaskScaled, (int) heightMaskScaled, true);

        Bitmap result = Bitmap.createBitmap(
            (int) widthMaskScaled,
            (int) heightMaskScaled, Config.ARGB_8888);

        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(bmPatternScaled, 0, 0, null);
        mCanvas.drawBitmap(maskScaled, 0, 0, paint);
        paint.setXfermode(null);

        int widthBmStikerCrop = bmStikerCrop.getWidth();
        int heightBmStikerCrop = bmStikerCrop.getHeight();

        int distanceLeft = (widthMaskScaled - widthBmStikerCrop) / 2;
        int distanceTop = (heightMaskScaled - heightBmStikerCrop) / 2;

        overlayedBitmap = overlayMark(result, bmStikerCrop, distanceLeft, distanceTop);

        cropStiker.setImageBitmap(overlayedBitmap);

      }
    });

    ivFrameOrange.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.seekbarAndScale_layout);
        layout.setVisibility(View.VISIBLE);

        sbStikerBorder.setProgress(1);

        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = 1;
        Bitmap mask = BitmapFactory.decodeFile(PATH_CROP_IMAGE_DIR + borderImage_FileName, options2);

        float widthMaskF = (float) mask.getWidth() * 1.1f;
        float heightMaskF = (float) mask.getHeight() * 1.1f;
        widthMaskScaled = (int) widthMaskF;
        heightMaskScaled = (int) heightMaskF;

        maskScaled = Bitmap.createScaledBitmap(
            mask, (int) widthMaskScaled, (int) heightMaskScaled, true);

        bmPattern = drawableToBitmap(drawableOrange);
        bmPatternScaled = Bitmap.createScaledBitmap(
            bmPattern, (int) widthMaskScaled, (int) heightMaskScaled, true);

        Bitmap result = Bitmap.createBitmap(
            (int) widthMaskScaled,
            (int) heightMaskScaled, Config.ARGB_8888);

        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(bmPatternScaled, 0, 0, null);
        mCanvas.drawBitmap(maskScaled, 0, 0, paint);
        paint.setXfermode(null);

        int widthBmStikerCrop = bmStikerCrop.getWidth();
        int heightBmStikerCrop = bmStikerCrop.getHeight();

        int distanceLeft = (widthMaskScaled - widthBmStikerCrop) / 2;
        int distanceTop = (heightMaskScaled - heightBmStikerCrop) / 2;

        overlayedBitmap = overlayMark(result, bmStikerCrop, distanceLeft, distanceTop);

        cropStiker.setImageBitmap(overlayedBitmap);

      }
    });

    ivFramePattern0.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.seekbarAndScale_layout);
        layout.setVisibility(View.VISIBLE);

        sbStikerBorder.setProgress(1);

        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = 1;
        Bitmap mask = BitmapFactory.decodeFile(PATH_CROP_IMAGE_DIR + borderImage_FileName, options2);

        float widthMaskF = (float) mask.getWidth() * 1.1f;
        float heightMaskF = (float) mask.getHeight() * 1.1f;
        widthMaskScaled = (int) widthMaskF;
        heightMaskScaled = (int) heightMaskF;

        maskScaled = Bitmap.createScaledBitmap(
            mask, (int) widthMaskScaled, (int) heightMaskScaled, true);

        bmPattern = drawableToBitmap(drawablePattern0);
        bmPatternScaled = Bitmap.createScaledBitmap(
            bmPattern, (int) widthMaskScaled, (int) heightMaskScaled, true);

        Bitmap result = Bitmap.createBitmap(
            (int) widthMaskScaled,
            (int) heightMaskScaled, Config.ARGB_8888);

        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(bmPatternScaled, 0, 0, null);
        mCanvas.drawBitmap(maskScaled, 0, 0, paint);
        paint.setXfermode(null);

        int widthBmStikerCrop = bmStikerCrop.getWidth();
        int heightBmStikerCrop = bmStikerCrop.getHeight();

        int distanceLeft = (widthMaskScaled - widthBmStikerCrop) / 2;
        int distanceTop = (heightMaskScaled - heightBmStikerCrop) / 2;

        overlayedBitmap = overlayMark(result, bmStikerCrop, distanceLeft, distanceTop);

        cropStiker.setImageBitmap(overlayedBitmap);

      }
    });

    ivFramePattern1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.seekbarAndScale_layout);
        layout.setVisibility(View.VISIBLE);

        sbStikerBorder.setProgress(1);

        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = 1;
        Bitmap mask = BitmapFactory.decodeFile(PATH_CROP_IMAGE_DIR + borderImage_FileName, options2);

        float widthMaskF = (float) mask.getWidth() * 1.1f;
        float heightMaskF = (float) mask.getHeight() * 1.1f;
        widthMaskScaled = (int) widthMaskF;
        heightMaskScaled = (int) heightMaskF;

        maskScaled = Bitmap.createScaledBitmap(
            mask, (int) widthMaskScaled, (int) heightMaskScaled, true);

        bmPattern = drawableToBitmap(drawablePattern1);
        bmPatternScaled = Bitmap.createScaledBitmap(
            bmPattern, (int) widthMaskScaled, (int) heightMaskScaled, true);

        Bitmap result = Bitmap.createBitmap(
            (int) widthMaskScaled,
            (int) heightMaskScaled, Config.ARGB_8888);

        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(bmPatternScaled, 0, 0, null);
        mCanvas.drawBitmap(maskScaled, 0, 0, paint);
        paint.setXfermode(null);

        int widthBmStikerCrop = bmStikerCrop.getWidth();
        int heightBmStikerCrop = bmStikerCrop.getHeight();

        int distanceLeft = (widthMaskScaled - widthBmStikerCrop) / 2;
        int distanceTop = (heightMaskScaled - heightBmStikerCrop) / 2;

        overlayedBitmap = overlayMark(result, bmStikerCrop, distanceLeft, distanceTop);

        cropStiker.setImageBitmap(overlayedBitmap);

      }
    });

    ivFramePattern2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.seekbarAndScale_layout);
        layout.setVisibility(View.VISIBLE);

        sbStikerBorder.setProgress(1);

        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = 1;
        Bitmap mask = BitmapFactory.decodeFile(PATH_CROP_IMAGE_DIR + borderImage_FileName, options2);

        float widthMaskF = (float) mask.getWidth() * 1.1f;
        float heightMaskF = (float) mask.getHeight() * 1.1f;
        widthMaskScaled = (int) widthMaskF;
        heightMaskScaled = (int) heightMaskF;

        maskScaled = Bitmap.createScaledBitmap(
            mask, (int) widthMaskScaled, (int) heightMaskScaled, true);

        bmPattern = drawableToBitmap(drawablePattern2);
        bmPatternScaled = Bitmap.createScaledBitmap(
            bmPattern, (int) widthMaskScaled, (int) heightMaskScaled, true);

        Bitmap result = Bitmap.createBitmap(
            (int) widthMaskScaled,
            (int) heightMaskScaled, Config.ARGB_8888);

        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(bmPatternScaled, 0, 0, null);
        mCanvas.drawBitmap(maskScaled, 0, 0, paint);
        paint.setXfermode(null);

        int widthBmStikerCrop = bmStikerCrop.getWidth();
        int heightBmStikerCrop = bmStikerCrop.getHeight();

        int distanceLeft = (widthMaskScaled - widthBmStikerCrop) / 2;
        int distanceTop = (heightMaskScaled - heightBmStikerCrop) / 2;

        overlayedBitmap = overlayMark(result, bmStikerCrop, distanceLeft, distanceTop);

        cropStiker.setImageBitmap(overlayedBitmap);

      }
    });

    ivFramePattern3.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.seekbarAndScale_layout);
        layout.setVisibility(View.VISIBLE);

        sbStikerBorder.setProgress(1);

        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = 1;
        Bitmap mask = BitmapFactory.decodeFile(PATH_CROP_IMAGE_DIR + borderImage_FileName, options2);

        float widthMaskF = (float) mask.getWidth() * 1.1f;
        float heightMaskF = (float) mask.getHeight() * 1.1f;
        widthMaskScaled = (int) widthMaskF;
        heightMaskScaled = (int) heightMaskF;

        maskScaled = Bitmap.createScaledBitmap(
            mask, (int) widthMaskScaled, (int) heightMaskScaled, true);

        bmPattern = drawableToBitmap(drawablePattern3);
        bmPatternScaled = Bitmap.createScaledBitmap(
            bmPattern, (int) widthMaskScaled, (int) heightMaskScaled, true);

        Bitmap result = Bitmap.createBitmap(
            (int) widthMaskScaled,
            (int) heightMaskScaled, Config.ARGB_8888);

        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(bmPatternScaled, 0, 0, null);
        mCanvas.drawBitmap(maskScaled, 0, 0, paint);
        paint.setXfermode(null);

        int widthBmStikerCrop = bmStikerCrop.getWidth();
        int heightBmStikerCrop = bmStikerCrop.getHeight();

        int distanceLeft = (widthMaskScaled - widthBmStikerCrop) / 2;
        int distanceTop = (heightMaskScaled - heightBmStikerCrop) / 2;

        overlayedBitmap = overlayMark(result, bmStikerCrop, distanceLeft, distanceTop);

        cropStiker.setImageBitmap(overlayedBitmap);

      }
    });

    ivFramePattern4.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.seekbarAndScale_layout);
        layout.setVisibility(View.VISIBLE);

        sbStikerBorder.setProgress(1);

        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = 1;
        Bitmap mask = BitmapFactory.decodeFile(PATH_CROP_IMAGE_DIR + borderImage_FileName, options2);

        float widthMaskF = (float) mask.getWidth() * 1.1f;
        float heightMaskF = (float) mask.getHeight() * 1.1f;
        widthMaskScaled = (int) widthMaskF;
        heightMaskScaled = (int) heightMaskF;

        maskScaled = Bitmap.createScaledBitmap(
            mask, (int) widthMaskScaled, (int) heightMaskScaled, true);

        bmPattern = drawableToBitmap(drawablePattern4);
        bmPatternScaled = Bitmap.createScaledBitmap(
            bmPattern, (int) widthMaskScaled, (int) heightMaskScaled, true);

        Bitmap result = Bitmap.createBitmap(
            (int) widthMaskScaled,
            (int) heightMaskScaled, Config.ARGB_8888);

        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(bmPatternScaled, 0, 0, null);
        mCanvas.drawBitmap(maskScaled, 0, 0, paint);
        paint.setXfermode(null);

        int widthBmStikerCrop = bmStikerCrop.getWidth();
        int heightBmStikerCrop = bmStikerCrop.getHeight();

        int distanceLeft = (widthMaskScaled - widthBmStikerCrop) / 2;
        int distanceTop = (heightMaskScaled - heightBmStikerCrop) / 2;

        overlayedBitmap = overlayMark(result, bmStikerCrop, distanceLeft, distanceTop);

        cropStiker.setImageBitmap(overlayedBitmap);

      }
    });

    ivFramePattern5.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.seekbarAndScale_layout);
        layout.setVisibility(View.VISIBLE);

        sbStikerBorder.setProgress(1);

        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = 1;
        Bitmap mask = BitmapFactory.decodeFile(PATH_CROP_IMAGE_DIR + borderImage_FileName, options2);

        float widthMaskF = (float) mask.getWidth() * 1.1f;
        float heightMaskF = (float) mask.getHeight() * 1.1f;
        widthMaskScaled = (int) widthMaskF;
        heightMaskScaled = (int) heightMaskF;

        maskScaled = Bitmap.createScaledBitmap(
            mask, (int) widthMaskScaled, (int) heightMaskScaled, true);

        bmPattern = drawableToBitmap(drawablePattern5);
        bmPatternScaled = Bitmap.createScaledBitmap(
            bmPattern, (int) widthMaskScaled, (int) heightMaskScaled, true);

        Bitmap result = Bitmap.createBitmap(
            (int) widthMaskScaled,
            (int) heightMaskScaled, Config.ARGB_8888);

        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(bmPatternScaled, 0, 0, null);
        mCanvas.drawBitmap(maskScaled, 0, 0, paint);
        paint.setXfermode(null);

        int widthBmStikerCrop = bmStikerCrop.getWidth();
        int heightBmStikerCrop = bmStikerCrop.getHeight();

        int distanceLeft = (widthMaskScaled - widthBmStikerCrop) / 2;
        int distanceTop = (heightMaskScaled - heightBmStikerCrop) / 2;

        overlayedBitmap = overlayMark(result, bmStikerCrop, distanceLeft, distanceTop);

        cropStiker.setImageBitmap(overlayedBitmap);

      }
    });

    ivFramePattern6.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.seekbarAndScale_layout);
        layout.setVisibility(View.VISIBLE);

        sbStikerBorder.setProgress(1);

        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = 1;
        Bitmap mask = BitmapFactory.decodeFile(PATH_CROP_IMAGE_DIR + borderImage_FileName, options2);

        float widthMaskF = (float) mask.getWidth() * 1.1f;
        float heightMaskF = (float) mask.getHeight() * 1.1f;
        widthMaskScaled = (int) widthMaskF;
        heightMaskScaled = (int) heightMaskF;

        maskScaled = Bitmap.createScaledBitmap(
            mask, (int) widthMaskScaled, (int) heightMaskScaled, true);

        bmPattern = drawableToBitmap(drawablePattern6);
        bmPatternScaled = Bitmap.createScaledBitmap(
            bmPattern, (int) widthMaskScaled, (int) heightMaskScaled, true);

        Bitmap result = Bitmap.createBitmap(
            (int) widthMaskScaled,
            (int) heightMaskScaled, Config.ARGB_8888);

        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(bmPatternScaled, 0, 0, null);
        mCanvas.drawBitmap(maskScaled, 0, 0, paint);
        paint.setXfermode(null);

        int widthBmStikerCrop = bmStikerCrop.getWidth();
        int heightBmStikerCrop = bmStikerCrop.getHeight();

        int distanceLeft = (widthMaskScaled - widthBmStikerCrop) / 2;
        int distanceTop = (heightMaskScaled - heightBmStikerCrop) / 2;

        overlayedBitmap = overlayMark(result, bmStikerCrop, distanceLeft, distanceTop);
        cropStiker.setImageBitmap(overlayedBitmap);

      }
    });

    /*
    Button btSave = (Button) findViewById(R.id.saveStikerBorder);
    btSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub


        new SaveAsyncTask().execute(0);


      }
    });

    Button btColse = (Button) findViewById(R.id.closeStikerBorder);
    btColse.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
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

  }


  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    //overlayedBitmap = null;
    Ratio = progress;

    Log.i("Ratio is ", String.valueOf(Ratio));

    float scaledWidth = (float) widthMaskScaled * (1.0f + ((float) Ratio / 10.0f));
    float scaledHeight = (float) heightMaskScaled * (1.0f + ((float) Ratio / 10.0f));

    Bitmap maskScaledOnSeek = Bitmap.createScaledBitmap(
        maskScaled, (int) scaledWidth, (int) scaledHeight, true);


    //Drawable drawableBg = new BitmapDrawable(getResources(), bmStikerBorder);
    //borderImageBg.setImageBitmap(bmStikerBorder);
                /*
            BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inSampleSize =1;
	    Bitmap bmStikerCropScaleUp = BitmapFactory.decodeFile(PATH_CROP_IMAGE_DIR + cropImage_FileName, options);
	    */
    //cropStiker.setImageBitmap(bmStikerCrop);
    //cropStiker.setBackgroundDrawable(drawableBg);

    Bitmap result = Bitmap.createBitmap(
        (int) scaledWidth,
        (int) scaledHeight, Config.ARGB_8888);

    Bitmap bmPatternScaled = Bitmap.createScaledBitmap(
        bmPattern, (int) scaledWidth, (int) scaledHeight, true);


    Canvas mCanvas = new Canvas(result);
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    mCanvas.drawBitmap(bmPatternScaled, 0, 0, null);
    mCanvas.drawBitmap(maskScaledOnSeek, 0, 0, paint);
    paint.setXfermode(null);

    int widthBmMask = maskScaledOnSeek.getWidth();
    int heightBmMask = maskScaledOnSeek.getHeight();
    int widthBmStikerCrop = bmStikerCrop.getWidth();
    int heightBmStikerCrop = bmStikerCrop.getHeight();

    int distanceLeft = (widthBmMask - widthBmStikerCrop) / 2;
    int distanceTop = (heightBmMask - heightBmStikerCrop) / 2;

    overlayedBitmap = overlayMark(result, bmStikerCrop, distanceLeft, distanceTop);

    cropStiker.setImageBitmap(overlayedBitmap);
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {
    // not implemented
  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {
    // not implemented
  }

  private Bitmap overlayMark(Bitmap baseBmp, Bitmap overlayBmp, int distanceLeft, int distanceTop) {

    Bitmap resultBmp = Bitmap.createBitmap(baseBmp.getWidth(),
        baseBmp.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(resultBmp);
    canvas.drawBitmap(baseBmp, 0, 0, null);
    canvas.drawBitmap(overlayBmp, distanceLeft, distanceTop, null);
    return resultBmp;
  }

  private boolean SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {

    File fileCacheItem = new File(strFilePath);
    OutputStream out = null;


    try {


      fileCacheItem.createNewFile();
      out = new FileOutputStream(fileCacheItem);
      bitmap.compress(CompressFormat.PNG, 70, out);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        out.close();
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(strFilePath)));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return true;
  }


  public static Bitmap drawableToBitmap(Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    }

    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);

    return bitmap;
  }
	
  /*
  File 	: execute, doInBackground 의 파라미터 타입
  Integer	: onProgressUpdate 의 파라미터 타입
  Int	: doInBackground 의 리턴값, onPostExecute 의 파라미터로 설정됩니다.
  */


  public class SaveAsyncTask extends AsyncTask<Integer, Integer, Integer> {

    ProgressDialog dialog;

    @Override
    protected void onCancelled() {
      super.onCancelled();
    }

    @Override
    protected void onPostExecute(Integer result) {
      //btn.setText("Thread END");
      super.onPostExecute(result);
      if (result == 1) {
        Log.i("copy", "SUCESS");

        cropStickerActivity.stikerCropSorterView.mImgArryList.clear();

        // 임시 받은 파일 지운다.
        String borderStikerPath = PATH_CROP_IMAGE_DIR + borderImage_FileName;
        File fileBorderImage = new File(borderStikerPath);
        fileBorderImage.delete();

        String borderedStikerPath = PATH_CROP_IMAGE_DIR + cropImage_FileName;
        Log.i("sads", String.valueOf(borderedStikerPath));
        Log.i("sads", String.valueOf(cropImage_FileName));


        //String[] arrNewPath =borderedStikerPath.split("/");
        int stikerBorderResId = getResources().getIdentifier("BorderImageViewBorderStiker", "id", getPackageName());
        Log.i("stikerBorderpResId is ", String.valueOf(stikerBorderResId));

        Drawable d = Drawable.createFromPath(PATH_CROP_IMAGE_DIR + cropImage_FileName);
        String drawablePath = borderedStikerPath;


        int dHeight = d.getIntrinsicHeight();
        int dWidth = d.getIntrinsicWidth();

        Log.i("dWidth is ", String.valueOf(dWidth));
        Log.i("dHeight is ", String.valueOf(dHeight));

        if (d == null) {
          Toast.makeText(borderStickerActivity.this, "적용할 패턴을 선택해 주세요!",
              Toast.LENGTH_SHORT).show();
        } else {


          //crop된  비율에 따라 세로길이 조정됨

          if(dWidth >= dHeight) {

            float ratio = (float) dHeight / (float) dWidth;
            float shopLogoHeightF = 130 * ratio;
            int shopLogoHeight = (int)shopLogoHeightF;
            Log.i("sads", String.valueOf(ratio));
            Log.i("sads", String.valueOf(shopLogoHeight));

            //MainRegisterCouponShopStep1.iv_ShopLogoSticker.removeAllViews();
            MainRegisterCouponShopStep1.ShopLogoWidth = 130;
            MainRegisterCouponShopStep1.ShopLogoHeight = shopLogoHeight;



          } else  {

            float ratio = (float) dWidth / (float) dHeight;
            float shopLogoWidthF = 130 * ratio;
            int shopLogoWidth = (int)shopLogoWidthF;
            Log.i("sads", String.valueOf(ratio));
            Log.i("sads", String.valueOf(shopLogoWidth));

            //MainRegisterCouponShopStep1.iv_ShopLogoSticker.removeAllViews();
            MainRegisterCouponShopStep1.ShopLogoWidth = shopLogoWidth;
            MainRegisterCouponShopStep1.ShopLogoHeight = 130;

          }


          MainRegisterCouponShopStep1.iv_ShopLogoSticker = new StickerShopLogoImageView(MainRegisterCouponShopStep1.context);
          MainRegisterCouponShopStep1.iv_ShopLogoSticker.setImageDrawable(d);

          MainRegisterCouponShopStep1.arrayListStickerShopLogoView.add(MainRegisterCouponShopStep1.iv_ShopLogoSticker);

          int size = MainRegisterCouponShopStep1.arrayListStickerShopLogoView.size();

          MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(size-1).setControlItemsHidden(true);
          MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(size-1).setControlItemsMoveLock(false);


          int[] posXY = new int[2];
          MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(size-1).getLocationOnScreen(posXY);
          int x = posXY[0];
          int y = posXY[1];
          MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(size-1).setMoveOrgX((float)x);
          MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(size-1).setMoveOrgY((float)y);


          MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(size-1).setObjWidth((float) MainRegisterCouponShopStep1.ShopLogoWidth);
          MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(size-1).setObjHeight((float) MainRegisterCouponShopStep1.ShopLogoHeight);

          MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(size-1).setPathAndFileName(borderedStikerPath);
          MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(size-1).setFileName(cropImage_FileName);

          MainRegisterCouponShopStep1.canvas.addView(MainRegisterCouponShopStep1.iv_ShopLogoSticker);


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

      if (null != dialog && dialog.isShowing()) {
        dialog.dismiss();
      }
    }

    @Override
    protected void onPreExecute() {
      //btn.setText("Thread START!!!!");

      dialog = ProgressDialog.show(borderStickerActivity.this, "",
          "적용중입니다.", true);

      super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      //pb.setProgress(values[0]);
      super.onProgressUpdate(values);
    }

    @Override
    protected Integer doInBackground(Integer... params) {
      int result = 0;
      int n = params[0];


      if (SaveBitmapToFileCache(overlayedBitmap, PATH_CROP_IMAGE_DIR + cropImage_FileName)) {
        result = 1;
      }
      return result;
    }
  }

}
