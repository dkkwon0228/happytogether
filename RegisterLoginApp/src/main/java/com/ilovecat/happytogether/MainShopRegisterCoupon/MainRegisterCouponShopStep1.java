/*
 * Copyright (c) 2016.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 설명은 여기서 부터
 *
 * Created by dannykwon on 2016 . 6. 20. happytogether
 */

package com.ilovecat.happytogether.MainShopRegisterCoupon;

import static com.ilovecat.happytogether.CommonConstantsAndUtils.CommonConstants.DB_LOGTAG;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonUtils;
import com.ilovecat.happytogether.CommonConstantsAndUtils.UtilsForAlbumAndCamera;
import com.ilovecat.happytogether.ControllerCropBorderSticker.cropStickerActivity;
import com.ilovecat.happytogether.ControllerMessageBorderColorPicker.MessageBorderColorPicker;
import com.ilovecat.happytogether.ControllerMessageColorPicker.MessageColorPicker;
import com.ilovecat.happytogether.Databasehandler.DatabaseHandler;
import com.ilovecat.happytogether.Main.MainActivity;
import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.StickerClass.StickerMessageTextView;
import com.ilovecat.happytogether.StickerClass.StickerMessageView;
import com.ilovecat.happytogether.StickerClass.StickerShopLogoImageView;
import com.ilovecat.happytogether.StickerClass.StickerShopLogoView;
import com.ilovecat.happytogether.StickerClass.StickerShopNameTextView;
import com.ilovecat.happytogether.StickerClassUtil.AutoResizeTextView;
import com.ilovecat.happytogether.StickerClassUtil.ShowFontBorderSizeDialog;
import com.ilovecat.happytogether.StickerClassUtil.ShowFontSizeDialog;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.CommonUIOfTopBar;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.GoBackAction;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


/**
 * <pre>
 * 1. 패키지명 :
 * 2. 타입명 :
 * 3. 작성일 : 2016. 5. 9. 오전 12:02:47
 * 4. 작성자 : dannykwon
 * 5. 설명 :
 * </pre>
 */
public class MainRegisterCouponShopStep1 extends AppCompatActivity implements
    MessageColorPicker.OnColorChangedListener, MessageBorderColorPicker.OnColorChangedListener, FontFormatRowAdapter_Step1.KeyFontClickListener {

  public static AppCompatActivity appcActivity;
  public static Context context;


  public static ImageView ivBgCoupon;
  public static String BackGroundFileName;
  public static String BackGroundFilePathAndName;


  //###########################################
  /*
  Canvas View Variables
  */
  public static FrameLayout canvas;


  public static ArrayList<StickerShopLogoView> arrayListStickerShopLogoView;
  public static ArrayList<StickerMessageView> arrayListStickerMessageView;

  public static StickerShopLogoImageView iv_ShopLogoSticker;
  public static StickerMessageTextView tv_MessageSticker;


  public static ToggleButton tbControlHelper;


  public static int MessageBordrRepeatCount = 10;
  public static int MessageShadowRadius = 5;

  public static int MessageMaxShadowRadius = 20;
  public static int currentMessageShadowColor = Color.BLACK;

  public static boolean isCenterShadow = false;


  public static int MessagefontSize = 28;
  public static int MessageMaxfontSize = 112;
  public static int MessageMinfontSize = 12;
  public static int MessageMaxLine = 5;
  public static int MessageBoxLeftMargin = 0;
  public static int currentMessageTextColor = Color.YELLOW;
  public static int MessageWidth;
  public static int MessageHeight;

  public static int ShopLogoBoxLeftMargin = 0;
  public static int ShopLogoWidth;
  public static int ShopLogoHeight;


  public static int ShopNamefontSize = 12;
  public static int ShopNameMaxLine = 1;
  public static int ShopNameBoxRightMargin = -5;
  public static int ShopNameBoxTopMargin = -5;

  public static int currentShopNameTextColor = Color.WHITE;
  //###########################################


  public static AutoCompleteTextView actvCouponTitle;

  public static EditText etCouponNoticeTitle;
  public static EditText etCouponNoticeEndDate;
  public static EditText etCouponNoticeMultiLines;


  private List<FloatingActionMenu> fabMenus = new ArrayList<>();
  private Handler mUiHandler = new Handler();
  private FloatingActionMenu fabStickerMenuDown;
  private FloatingActionButton fabStickerCamera;
  private FloatingActionButton fabStickerPhotoAlbum;
  private FloatingActionButton fabStickerTempSave;


  private FloatingActionMenu fabBackgroundMenuDown;
  private FloatingActionButton fabBackgroundCamera;
  private FloatingActionButton fabBackgroundPhotoAlbum;
  private FloatingActionButton fabBackgroundBackground;


  //###########################################
  /*
  Camera And Album
   */
  private static final int PICK_FROM_CAMERA = 0;
  private static final int PICK_FROM_ALBUM = 1;
  private static final int CROP_FROM_CAMERA = 2;

  public static String FOR_CROP_IMAGE_FILENAME;
  Uri TAKECAMERA_FILENAME_URI;

  public static String tempDir_path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/HappyTogether/Temp/";
  public static String cropedImageDir_path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/HappyTogether/CropImage/";
  //###########################################


  // Font Format And Texture
  public static PopupWindow popupWindowFontFormat;
  public static PopupWindow popupWindowFontTexture;
  public static LinearLayout subMenuFontsFormatLayout;
  public static LinearLayout subMenuFontsTextureLayout;
  LinearLayout parentLayout;
  public static View popUpFontsFormatView;
  public static View popUpFontsTextureView;
  String typeFacePath = null;
  static boolean isKeyBoardVisible;
  private int keyboardHeight;

  private String[] background00ImageUrls;
  public static String[] fontTextureImageUrls;
  public static DisplayImageOptions options;

  //
  public static Bitmap captureBitmap;
  public static String title;
  public static String endDate;
  public static String couponNotice;


  // Info
  public static TextView x;
  public static TextView y;
  public static TextView w;
  public static TextView h;
  public static TextView drawablePath;
  public static TextView shadowColor;
  public static TextView shadowColorSize;
  public static TextView shadowCenter;
  public static TextView textColor;
  public static TextView textColorSize;
  public static TextView textFormat;
  public static TextView textPattern;


  //TempSAve
  public static String TEMPSAVE_XML_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/HappyTogether/TempSave/tempsave.xml";
  public static String TEMPSAVE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/HappyTogether/TempSave/";
  public static String photo_path_tempsaveXMLInAdapter = TEMPSAVE_PATH + "tempsave.xml";
  public static String TEMPSAVE_PATH_OF_SET;
  public static String SAVE_TIME;


  // From TempSave
  String strOfFromTempState = null;


  static {
    System.loadLibrary("opencv_java3"); //the name of the .so file, without the 'lib' prefix
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_register_coupon_shop_step1_layout);

    appcActivity = this;
    context = getApplicationContext();

    // 상단바 공통 UI
    CommonUIOfTopBar.setCommonUI(this);
    GoBackAction.goBackAndDoAnimation(this);


    initImageLoader(context);


    background00ImageUrls = new String[]{
        "assets://coupon_absbg02.jpg",
        "assets://coupon_absbg03.jpg",
        "assets://coupon_absbg04.jpg",
        "assets://coupon_absbg05.jpg",
        "assets://coupon_absbg08.jpg",
        "assets://coupon_patternbg02.jpg",
        "assets://coupon_patternbg04.jpg",
        "assets://coupon_patternbg05.jpg",
        "assets://coupon_patternbg07.jpg",
        "assets://coupon_patternbg08.jpg",
        "assets://coupon_patternbg09.jpg",
        "assets://coupon_patternbg10.jpg",
        "assets://coupon_patternbg11.jpg"
    };

    fontTextureImageUrls = new String[]{
        "assets://frame_no.png",
        "assets://frame_pattern1.png",
        "assets://frame_pattern2.png",
        "assets://frame_pattern3.png",
        "assets://frame_pattern4.png",
        "assets://frame_pattern5.png",
        "assets://frame_pattern6.png",
        "assets://frame_pattern7.png",
        "assets://frame_pattern8.png",
        "assets://frame_pattern9.png",
        "assets://frame_pattern10.png",
        "assets://frame_pattern10_2.png",
        "assets://frame_pattern10_3.png",
        "assets://frame_pattern10_4.png",
        "assets://frame_pattern11.png",
        "assets://frame_pattern12.png",
        "assets://frame_pattern13.png",
        "assets://frame_pattern14.png",
        "assets://frame_pattern15.png",
        "assets://frame_pattern16.png"

    };

    options = new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.ic_stub)
        .showImageForEmptyUri(R.drawable.ic_empty)
        .showImageOnFail(R.drawable.ic_error)
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .considerExifParams(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();


    // Sticker View Canvas
    canvas = (FrameLayout) findViewById(R.id.canvasView);

    // add a stickerText to canvas
    arrayListStickerShopLogoView = new ArrayList<StickerShopLogoView>();
    arrayListStickerMessageView = new ArrayList<StickerMessageView>();


    Intent intent = getIntent();
    strOfFromTempState = intent.getExtras().getString("keyWhere");

    //View Pager
    Bundle bundle = getIntent().getExtras();
    assert bundle != null;

    if (strOfFromTempState.equals("TEMPSAVE")) {

      String TEMPSAVE_ID = intent.getExtras().getString("keyId");
      ArrayList<String> imageUrls = (ArrayList<String>) bundle.getSerializable("keyImgUrl");
      int TOTAL_PAGER_NUM = imageUrls.size();


      ArrayList<String> arrStikerTotalNum = (ArrayList<String>) bundle.getSerializable("keyStikerTotalNum");
      Log.i("arrStikerTotalNum", String.valueOf(arrStikerTotalNum));
      ArrayList<String[]> arrStikerCenterX = (ArrayList<String[]>) bundle.getSerializable("keyStikerCenterX");
      Log.i("arrStikerCenterX", String.valueOf(arrStikerCenterX));
      ArrayList<String[]> arrStikerCenterY = (ArrayList<String[]>) bundle.getSerializable("keyStikerCenterY");
      Log.i("arrStikerCenterY", String.valueOf(arrStikerCenterY));
      ArrayList<String[]> arrStikerWidth = (ArrayList<String[]>) bundle.getSerializable("keyStikerWidth");
      Log.i("arrStikerWidth", String.valueOf(arrStikerWidth));
      ArrayList<String[]> arrStikerHeight = (ArrayList<String[]>) bundle.getSerializable("keyStikerHeight");
      Log.i("arrStikerHeight", String.valueOf(arrStikerHeight));
      ArrayList<String[]> arrStikerDrawable = (ArrayList<String[]>) bundle.getSerializable("keyStikerDrawable");
      Log.i("arrStikerDrawable", String.valueOf(arrStikerDrawable));



      ArrayList<String> arrTextTotalNum = (ArrayList<String>) bundle.getSerializable("keyTextTotalNum");
      Log.i("arrTextTotalNum", String.valueOf(arrTextTotalNum));
      ArrayList<String[]> arrTextCenterX = (ArrayList<String[]>) bundle.getSerializable("keyTextCenterX");
      Log.i("arrTextCenterX", String.valueOf(arrTextCenterX));
      ArrayList<String[]> arrTextCenterY = (ArrayList<String[]>) bundle.getSerializable("keyTextCenterY");
      Log.i("arrTextCenterY", String.valueOf(arrTextCenterY));
      ArrayList<String[]> arrTextWidth = (ArrayList<String[]>) bundle.getSerializable("keyTextWidth");
      Log.i("arrTextWidth", String.valueOf(arrTextWidth));
      ArrayList<String[]> arrTextHeight = (ArrayList<String[]>) bundle.getSerializable("keyTextHeight");
      Log.i("arrTextHeight", String.valueOf(arrTextHeight));
      ArrayList<String[]> arrTextContent = (ArrayList<String[]>) bundle.getSerializable("keyTextContent");
      Log.i("arrTextContent", String.valueOf(arrTextContent));


      // 백그라운드 이미지
      int size = imageUrls.size();
      String randomStr = imageUrls.get(size - 1);
      BackGroundFilePathAndName = randomStr;
      String[] arr = randomStr.split("//");
      String backgroundNewPath = arr[1];
      String[] arrNewPath = backgroundNewPath.split("/");
      int arrNewPathSize = arrNewPath.length;
      String BackGroundFileName = arrNewPath[arrNewPathSize - 1];
      Bitmap btm = loadBitmapFromAssets(BackGroundFileName);

      ivBgCoupon = (ImageView) findViewById(R.id.ivCouponImg);
      ivBgCoupon.setImageBitmap(btm);


      for (int i = 0; i < Integer.parseInt(arrStikerTotalNum.get(0)); i++) {

        iv_ShopLogoSticker = new StickerShopLogoImageView(MainRegisterCouponShopStep1.this);
        iv_ShopLogoSticker.setControlItemsHidden(true);
        iv_ShopLogoSticker.setControlItemsMoveLock(false);

        float density = context.getResources().getDisplayMetrics().density;

        int size2 = arrStikerTotalNum.size();
        Log.i("size2", String.valueOf(size2));

        // DRAWABLE
        String[] strDrawable = arrStikerDrawable.get(size2 - 1);
        Bitmap bmImg = BitmapFactory.decodeFile(strDrawable[i]);
        iv_ShopLogoSticker.setImageBitmap(bmImg);

        // FileName
        String fileName = strDrawable[i].split("_")[1];
        String fileName2 = "croped_" + fileName;
        iv_ShopLogoSticker.setFileName(fileName2);

        // ###################################################################
        // 매우중요 16/8/24
        // TempSave로 부터 로드할 경우 스티커 전체경로+파일명을 셋팅해주어야 한다.
        iv_ShopLogoSticker.setPathAndFileName(cropedImageDir_path + fileName2);
        // ###################################################################
        Log.i("fileName2", fileName2);

        //X Y
        String[] x = arrStikerCenterX.get(size2 - 1);
        float f_x = Float.parseFloat(x[i]);
        iv_ShopLogoSticker.setX(f_x);
        //iv_ShopLogoSticker.setMoveOrgX(f_x);
        Log.i("X", String.valueOf((int) (f_x)));

        String[] y = arrStikerCenterY.get(size2 - 1);
        float f_y = Float.parseFloat(y[i]);
        iv_ShopLogoSticker.setY(f_y);
        //iv_ShopLogoSticker.setMoveOrgX(f_y);
        Log.i("Y", String.valueOf((int) (f_y)));


        // WIDTH AND HEIGHT
        String[] w = arrStikerWidth.get(size2 - 1);
        float f_w = Float.parseFloat(w[i]);
        iv_ShopLogoSticker.getLayoutParams().width = CommonUtils.convertDpToPixel(f_w, context);

        // ###################################################################
        // 매우중요 16/8/24
        // TempSave로 부터 로드할 경우 스티커 가로길이를 셋팅해주어야 한다.
        iv_ShopLogoSticker.setObjWidth(f_w);
        // ###################################################################
        Log.i("ShopLogoWidth", String.valueOf((int) (f_w)));

        String[] h = arrStikerHeight.get(size2 - 1);
        float f_h = Float.parseFloat(h[i]);
        iv_ShopLogoSticker.getLayoutParams().height = CommonUtils.convertDpToPixel(f_h, context);

        // ###################################################################
        // 매우중요 16/8/24
        // TempSave로 부터 로드할 경우 스티커 세로길이를 셋팅해주어야 한다.
        iv_ShopLogoSticker.setObjHeight(f_h);
        // ###################################################################
        Log.i("ShopLogoHeight", String.valueOf((int) (f_h)));

        MainRegisterCouponShopStep1.arrayListStickerShopLogoView.add(MainRegisterCouponShopStep1.iv_ShopLogoSticker);

        canvas.addView(iv_ShopLogoSticker);

      }


      for (int k = 0; k < Integer.parseInt(arrTextTotalNum.get(0)); k++) {

        tv_MessageSticker = new StickerMessageTextView(MainRegisterCouponShopStep1.this);
        tv_MessageSticker.setControlItemsHidden(true);
        tv_MessageSticker.setControlItemsMoveLock(false);

        float density = context.getResources().getDisplayMetrics().density;

        int size2 = arrTextTotalNum.size();
        Log.i("size2", String.valueOf(size2));

        //X Y
        String[] x = arrTextCenterX.get(size2 - 1);
        float f_x = Float.parseFloat(x[k]);
        tv_MessageSticker.setX(f_x);
        Log.i("X", String.valueOf((int) (f_x)));

        String[] y = arrTextCenterY.get(size2 - 1);
        float f_y = Float.parseFloat(y[k]);
        tv_MessageSticker.setY(f_y);
        Log.i("Y", String.valueOf((int) (f_y)));


        // WIDTH AND HEIGHT
        String[] w = arrTextWidth.get(size2 - 1);
        float f_w = Float.parseFloat(w[k]);
        tv_MessageSticker.getLayoutParams().width = CommonUtils.convertDpToPixel(f_w, context);

        // ###################################################################
        // 매우중요 16/8/24
        // TempSave로 부터 로드할 경우 스티커 가로길이를 셋팅해주어야 한다.
        tv_MessageSticker.setObjWidth(f_w);
        // ###################################################################
        Log.i("MessageWidth", String.valueOf((int) (f_w)));

        String[] h = arrTextHeight.get(size2 - 1);
        float f_h = Float.parseFloat(h[k]);
        tv_MessageSticker.getLayoutParams().height = CommonUtils.convertDpToPixel(f_h, context);

        // ###################################################################
        // 매우중요 16/8/24
        // TempSave로 부터 로드할 경우 스티커 세로길이를 셋팅해주어야 한다.
        tv_MessageSticker.setObjHeight(f_h);
        // ###################################################################
        Log.i("MessageHeight", String.valueOf((int) (f_h)));


        String[] textContent = arrTextContent.get(size2 - 1);
        String strTextContent = textContent[k];
        tv_MessageSticker.setText(strTextContent);
        Log.i("TextContent", strTextContent);


        MainRegisterCouponShopStep1.arrayListStickerMessageView.add(MainRegisterCouponShopStep1.tv_MessageSticker);

        canvas.addView(tv_MessageSticker);

      }

    } else {

      int TOTAL_PAGER_NUM = 1;
      ArrayList<String> arrStikerTotalNum = new ArrayList<String>();
      arrStikerTotalNum.add(0, "1");
      ArrayList<String> arrTextTotalNum = new ArrayList<String>();
      arrTextTotalNum.add(0, "1");


      // 백그라운드 이미지
      String randomStr = background00ImageUrls[new Random().nextInt(background00ImageUrls.length)];
      BackGroundFilePathAndName = randomStr;
      String[] arr = randomStr.split("//");
      String backgroundNewPath = arr[1];
      String[] arrNewPath = backgroundNewPath.split("/");
      int arrNewPathSize = arrNewPath.length;
      String BackGroundFileName = arrNewPath[arrNewPathSize - 1];
      Bitmap btm = loadBitmapFromAssets(BackGroundFileName);

      ivBgCoupon = (ImageView) findViewById(R.id.ivCouponImg);
      ivBgCoupon.setImageBitmap(btm);


      iv_ShopLogoSticker = new StickerShopLogoImageView(MainRegisterCouponShopStep1.this);
      //iv_ShopLogoSticker.setImageDrawable(getResources().getDrawable(R.drawable.coupon_center_big));
      iv_ShopLogoSticker.setControlItemsHidden(true);
      iv_ShopLogoSticker.setControlItemsMoveLock(false);

      ShopLogoWidth = 130;
      ShopLogoHeight = 130;


      canvas.addView(iv_ShopLogoSticker);



      // add a stickerText to canvas
      tv_MessageSticker = new StickerMessageTextView(MainRegisterCouponShopStep1.this);
      tv_MessageSticker.setControlItemsHidden(true);
      tv_MessageSticker.setControlItemsMoveLock(false);
      tv_MessageSticker.setText("신메뉴출시 30% SALE 이벤트!! 서두르세요~ ");
      //tv_MessageSticker.setBackgroundColor(Color.WHITE);

      //MessageWidth = 270;
      //MessageHeight = 120;

      arrayListStickerMessageView.add(tv_MessageSticker);
      canvas.addView(tv_MessageSticker);




    }


    // Font Format and Texture
    popupWindowFontFormat = new PopupWindow();
    popupWindowFontTexture = new PopupWindow();

    subMenuFontsFormatLayout = (LinearLayout) findViewById(R.id.subMenuFontsFormat);
    subMenuFontsTextureLayout = (LinearLayout) findViewById(R.id.subMenuFontsTexture);

    parentLayout = (LinearLayout) findViewById(R.id.textSubBottons);
    popUpFontsFormatView = getLayoutInflater().inflate(R.layout.fontformat_popup, null);
    popUpFontsTextureView = getLayoutInflater().inflate(R.layout.fonttexture_popup, null);




    // add a stickerText to canvas
    final StickerShopNameTextView tv_ShopNameSticker = new StickerShopNameTextView(MainRegisterCouponShopStep1.this);
    tv_ShopNameSticker.setControlItemsHidden(true);
    tv_ShopNameSticker.setControlItemsMoveLock(false);
    tv_ShopNameSticker.setText(MainActivity.shopNameFromInnerDB);
    canvas.addView(tv_ShopNameSticker);


    final Button btnRegCouponShop = (Button) findViewById(R.id.btnRegCouponShop);


    // AutoCompleteTextView
    actvCouponTitle = (AutoCompleteTextView) findViewById(R.id.atctvRegisterCouponTitle);
    // 초기입력은 국문으로
    actvCouponTitle.setPrivateImeOptions("defaultInputmode=korea;");
    actvCouponTitle.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        popupWindowFontFormat.dismiss();
      } // end onClick()
    });


    actvCouponTitle.addTextChangedListener(new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

        String inputText = actvCouponTitle.getText().toString();

        int sizeOfArray = arrayListStickerMessageView.size();
        StickerMessageTextView clickedView = (StickerMessageTextView) arrayListStickerMessageView.get(sizeOfArray - 1);
        clickedView.setText(inputText);

      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void afterTextChanged(Editable s) {
      }
    });


    //###########################################################
    // Conttrol Icons

    // Canvas View And Message Box Controller On Screen
    ImageView ivAlignLeft = (ImageView) findViewById(R.id.icAlignLeft);
    ivAlignLeft.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        int sizeOfArray = arrayListStickerMessageView.size();
        StickerMessageTextView clickedView = (StickerMessageTextView) arrayListStickerMessageView.get(sizeOfArray - 1);


        clickedView.tv_main.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        clickedView.tv_main_shadow.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
      } // end onClick()
    });

    ImageView ivAlignCenter = (ImageView) findViewById(R.id.icAlignCenter);
    ivAlignCenter.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        int sizeOfArray = arrayListStickerMessageView.size();
        StickerMessageTextView clickedView = (StickerMessageTextView) arrayListStickerMessageView.get(sizeOfArray - 1);

        clickedView.tv_main.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        clickedView.tv_main_shadow.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
      } // end onClick()
    });

    ImageView ivAlignRight = (ImageView) findViewById(R.id.icAlignRight);
    ivAlignRight.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        int sizeOfArray = arrayListStickerMessageView.size();
        StickerMessageTextView clickedView = (StickerMessageTextView) arrayListStickerMessageView.get(sizeOfArray - 1);

        clickedView.tv_main.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        clickedView.tv_main_shadow.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
      } // end onClick()
    });


    ImageView ivMessageFontBorderColor = (ImageView) findViewById(R.id.icFontBorderColor);
    ivMessageFontBorderColor.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        int sizeOfArray = arrayListStickerMessageView.size();
        StickerMessageTextView clickedView = (StickerMessageTextView) arrayListStickerMessageView.get(sizeOfArray - 1);

        getBorderColor(clickedView.tv_main_shadow, clickedView.tv_main_shadow.getCenterState());
      } // end onClick()
    });


    ImageView ivMessageFontBorder = (ImageView) findViewById(R.id.icFontBorder);
    ivMessageFontBorder.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        int sizeOfArray = arrayListStickerMessageView.size();
        StickerMessageTextView clickedView = (StickerMessageTextView) arrayListStickerMessageView.get(sizeOfArray - 1);

        ShowFontBorderSizeDialog.showFontBorderSizeDialog(
            clickedView.tv_main_shadow,
            appcActivity);
      } // end onClick()
    });


    ImageView ivMessageFontSize = (ImageView) findViewById(R.id.icFontSize);
    ivMessageFontSize.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        int sizeOfArray = arrayListStickerMessageView.size();
        StickerMessageTextView clickedView = (StickerMessageTextView) arrayListStickerMessageView.get(sizeOfArray - 1);

        ShowFontSizeDialog.showFontSizeDialog(
            clickedView.tv_main,
            clickedView.tv_main_shadow,
            appcActivity);
      } // end onClick()
    });


    ImageView ivMessageFontColor = (ImageView) findViewById(R.id.icFontColor);
    ivMessageFontColor.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        int sizeOfArray = arrayListStickerMessageView.size();
        StickerMessageTextView clickedView = (StickerMessageTextView) arrayListStickerMessageView.get(sizeOfArray - 1);
        getColor(clickedView.tv_main);
      } // end onClick()
    });


    ImageView ivMessageFontTexture = (ImageView) findViewById(R.id.icFontTexture);
    ivMessageFontTexture.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        //getColor(tv_MessageSticker.tv_main);

        popupWindowFontTexture.setHeight((int) (getResources().getDimension(
            R.dimen.keyboard_height)));
        subMenuFontsTextureLayout.setVisibility(LinearLayout.VISIBLE);
        parentLayout.setVisibility(LinearLayout.VISIBLE);
        popupWindowFontTexture.showAtLocation(parentLayout, Gravity.BOTTOM, 0, 0);

      } // end onClick()
    });


    ImageView ivMessageFontFormat = (ImageView) findViewById(R.id.icFontFormat);
    ivMessageFontFormat.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        //getColor(tv_MessageSticker.tv_main);

        popupWindowFontFormat.setHeight((int) (getResources().getDimension(
            R.dimen.keyboard_height)));
        subMenuFontsFormatLayout.setVisibility(LinearLayout.VISIBLE);
        parentLayout.setVisibility(LinearLayout.VISIBLE);
        popupWindowFontFormat.showAtLocation(parentLayout, Gravity.BOTTOM, 0, 0);

      } // end onClick()
    });

    EnablePopUpFontsFormatView_Step1.EnablePopUpFontsFormatView();
    EnablePopUpFontsTextureView_Step1.EnablePopUpFontsTextureView();
    checkKeyboardHeight(parentLayout);


    ImageView ivFontTextureDropDown = (ImageView) popUpFontsTextureView.findViewById(R.id.fonttexture_down);
    ivFontTextureDropDown.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        popupWindowFontTexture.dismiss();

      } // end onClick()
    });


    ImageView ivFontFormatDropDown = (ImageView) popUpFontsFormatView.findViewById(R.id.fontformat_down);
    ivFontFormatDropDown.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        popupWindowFontFormat.dismiss();

      } // end onClick()
    });


    //###########################################################

    etCouponNoticeTitle = (EditText) findViewById(R.id.etRegisterCouponNotice);
    etCouponNoticeEndDate = (EditText) findViewById(R.id.etRegisterCouponEndDate);
    etCouponNoticeMultiLines = (EditText) findViewById(R.id.etCouponNoticeMultiLines);
    etCouponNoticeMultiLines.setVisibility(View.INVISIBLE);

    /*
     * xml 레이아웃에 추가해야 함.
     * android:focusable="false"
     */
    etCouponNoticeEndDate.setInputType(0);
    // 클릭 리스너
    etCouponNoticeEndDate.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View arg0) {

        //CreateCouponNoticeListViewDialog_Step1.createCouponNoticeListViewDialog(appcActivity);
        DialogDatePicker();
      }
    });


    //#############################################################3

    // Floating Menu
    fabStickerMenuDown = (FloatingActionMenu) findViewById(R.id.sticker_menu_down);
    fabStickerCamera = (FloatingActionButton) findViewById(R.id.fabCamera);
    fabStickerPhotoAlbum = (FloatingActionButton) findViewById(R.id.fabPhotoAlbum);
    fabStickerTempSave = (FloatingActionButton) findViewById(R.id.fabTempSave);

    fabBackgroundMenuDown = (FloatingActionMenu) findViewById(R.id.backgroud_menu_down);
    fabBackgroundCamera = (FloatingActionButton) findViewById(R.id.background_fabCamera);
    fabBackgroundPhotoAlbum = (FloatingActionButton) findViewById(R.id.background_fabPhotoAlbum);
    fabBackgroundBackground = (FloatingActionButton) findViewById(R.id.background_fabBackground);


    fabStickerMenuDown.hideMenuButton(false);
    fabBackgroundMenuDown.hideMenuButton(false);

    fabMenus.add(fabStickerMenuDown);
    fabMenus.add(fabBackgroundMenuDown);


    int delay = 400;
    for (final FloatingActionMenu menu : fabMenus) {
      mUiHandler.postDelayed(new Runnable() {
        @Override
        public void run() {

          menu.showMenuButton(true);

        }
      }, delay);
      delay += 150;
    }

    fabStickerMenuDown.setOnMenuButtonClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (fabStickerMenuDown.isOpened()) {
          actvCouponTitle.setEnabled(true);
          etCouponNoticeEndDate.setEnabled(true);
          etCouponNoticeTitle.setEnabled(true);
          btnRegCouponShop.setEnabled(true);

        } else {

          actvCouponTitle.setEnabled(false);
          etCouponNoticeEndDate.setEnabled(false);
          etCouponNoticeTitle.setEnabled(false);
          btnRegCouponShop.setEnabled(false);

        }
        popupWindowFontFormat.dismiss();
        popupWindowFontTexture.dismiss();

        fabStickerMenuDown.toggle(true);
      }
    });


    fabBackgroundMenuDown.setOnMenuButtonClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (fabBackgroundMenuDown.isOpened()) {
          actvCouponTitle.setEnabled(true);
          etCouponNoticeEndDate.setEnabled(true);
          etCouponNoticeTitle.setEnabled(true);
          btnRegCouponShop.setEnabled(true);

        } else {

          actvCouponTitle.setEnabled(false);
          etCouponNoticeEndDate.setEnabled(false);
          etCouponNoticeTitle.setEnabled(false);
          btnRegCouponShop.setEnabled(false);

        }
        popupWindowFontFormat.dismiss();
        popupWindowFontTexture.dismiss();


        fabBackgroundMenuDown.toggle(true);
      }
    });

    fabStickerCamera.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        doTakeCameraAction();
      }
    });

    fabStickerPhotoAlbum.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        doTakeAlbumAction();
      }
    });

    fabStickerTempSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        CreateTempSaveDialog_Step1.CreateTempSaveDialog(appcActivity);
      }
    });

    fabBackgroundBackground.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        CreateChangePageBackgroundDialog_Step1.CreateChangePageBackgroundDialog(appcActivity);
        //setDismiss(addRemoveDialog);
      }

    });
    //#############################################################


    // ADD Text Message
    ImageView ivbg = (ImageView) findViewById(R.id.sticker_bg);
    ivbg.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {


        MainRegisterCouponShopStep1.tv_MessageSticker = new StickerMessageTextView(MainRegisterCouponShopStep1.context);
        tv_MessageSticker.tv_main.setText("내용 입력");

        MainRegisterCouponShopStep1.arrayListStickerMessageView.add(MainRegisterCouponShopStep1.tv_MessageSticker);
        MainRegisterCouponShopStep1.canvas.addView(MainRegisterCouponShopStep1.tv_MessageSticker);

        // 메시지 어래이의 맨 마지막을 제외한 나머지의 컨트롤뷰를 안보이게 한다.
        int sizeOfArray = arrayListStickerMessageView.size();
        for (int i = 0; i < sizeOfArray - 1; i++) {

          arrayListStickerMessageView.get(i).setControlItemsHidden(true);

        }


        // 애니메니션 적용
        final float growTo = 0.8f;
        final long duration = 600;

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
        MainRegisterCouponShopStep1.tv_MessageSticker.startAnimation(growAndShrink);


      } // end onClick()
    });


    // Move Lock Controller
    final ToggleButton tbMoveLock =
        (ToggleButton) this.findViewById(R.id.LockToggleButton);

    tbMoveLock.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        if (tbMoveLock.isChecked()) {
          tbMoveLock.setBackgroundDrawable(
              getResources().
                  getDrawable(R.drawable.sticker_lock_open)
          );


          int sizeOfArray = arrayListStickerMessageView.size();
          for (int i = 0; i < sizeOfArray; i++) {
            arrayListStickerMessageView.get(i).setControlItemsMoveLock(true);
          }

          int sizeOfArray2 = arrayListStickerShopLogoView.size();
          for (int i = 0; i < sizeOfArray2; i++) {
            arrayListStickerShopLogoView.get(i).setControlItemsMoveLock(true);
          }

          tv_ShopNameSticker.setControlItemsMoveLock(true);


        } else {
          tbMoveLock.setBackgroundDrawable(
              getResources().
                  getDrawable(R.drawable.sticker_lock)
          );

          int sizeOfArray = arrayListStickerMessageView.size();
          for (int i = 0; i < sizeOfArray; i++) {
            arrayListStickerMessageView.get(i).setControlItemsMoveLock(false);
          }

          int sizeOfArray2 = arrayListStickerShopLogoView.size();
          for (int i = 0; i < sizeOfArray2; i++) {
            arrayListStickerShopLogoView.get(i).setControlItemsMoveLock(false);
          }


          tv_ShopNameSticker.setControlItemsMoveLock(false);


        } // end if
      } // end onClick()
    });

    // Helper Visible Controller
    tbControlHelper =
        (ToggleButton) this.findViewById(R.id.VisibleToggleButton);

    tbControlHelper.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        if (tbControlHelper.isChecked()) {
          tbControlHelper.setBackgroundDrawable(
              getResources().
                  getDrawable(R.drawable.sticker_help_invisible)
          );

          int sizeOfShopLogoArray = arrayListStickerShopLogoView.size();
          for (int i = 0; i < sizeOfShopLogoArray; i++) {

            arrayListStickerShopLogoView.get(i).setControlItemsHidden(true);

          }

          int sizeOfMessageArray = arrayListStickerMessageView.size();
          for (int i = 0; i < sizeOfMessageArray; i++) {

            arrayListStickerMessageView.get(i).setControlItemsHidden(true);

          }


        } else {
          tbControlHelper.setBackgroundDrawable(
              getResources().
                  getDrawable(R.drawable.sticker_help_visible)
          );

          int sizeOfShopLogoArray = arrayListStickerShopLogoView.size();
          for (int i = 0; i < sizeOfShopLogoArray; i++) {

            arrayListStickerShopLogoView.get(i).setControlItemsHidden(false);

          }

          int sizeOfMessageArray = arrayListStickerMessageView.size();
          for (int i = 0; i < sizeOfMessageArray; i++) {

            arrayListStickerMessageView.get(i).setControlItemsHidden(false);

          }

        } // end if
      } // end onClick()
    });


    // INFO
    x = (TextView) findViewById(R.id.objX);
    y = (TextView) findViewById(R.id.objY);
    w = (TextView) findViewById(R.id.objW);
    h = (TextView) findViewById(R.id.objH);
    drawablePath = (TextView) findViewById(R.id.objDrawable);

    shadowColor = (TextView) findViewById(R.id.fontShadowColor);
    shadowColorSize = (TextView) findViewById(R.id.fontShadowColorSize);
    shadowCenter = (TextView) findViewById(R.id.fontShadowCenter);

    textColor = (TextView) findViewById(R.id.textColor);
    textColorSize = (TextView) findViewById(R.id.textColorSize);
    textFormat = (TextView) findViewById(R.id.textFormat);
    textPattern = (TextView) findViewById(R.id.textPattern);


    // xml 레이아웃에 추가해야 함.
    // android:focusable="false"
    etCouponNoticeTitle.setInputType(0);
    // 클릭 리스너
    etCouponNoticeTitle.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View arg0) {

        CreateCouponNoticeListViewDialog_Step1.createCouponNoticeListViewDialog(appcActivity);

      }
    });


    // DatabaseHandle In databasehandler package
    DatabaseHandler handlerDatabase = new DatabaseHandler(this);
    int count = handlerDatabase.getRowCountAdminkey();
    Log.i(DB_LOGTAG, "ADMINKEY_COUNT in REGISTER = " + count);
    handlerDatabase.close();


    if (Build.VERSION.SDK_INT > 9) {
      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
      StrictMode.setThreadPolicy(policy);
    }

    // 내부 DB의 adminkey_tb 테이블의 row 값이 0일 경우 (앱 관리자 등록이 안되어 있을 경우)
    if (count == 0) {

    } else {

      btnRegCouponShop.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          actvCouponTitle.setError(null);
          // actvCouponEndDate.setError(null);


          title = actvCouponTitle.getText().toString().trim();
          endDate = etCouponNoticeEndDate.getText().toString().trim();
          couponNotice = etCouponNoticeMultiLines.getText().toString().trim();
          // 유효 입력 검사.
          if (TextUtils.isEmpty(title)) {
            actvCouponTitle.setError(getString(R.string.error_field_required));
          } else {


            new AsyncTaskCaptureImage_Step1().execute(0);

          }


        }
      });
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    //setContentView(R.layout.main_register_coupon_shop_step1_layout);
    // postIncomeTotals();
  }

  /*
  * DataPiker 년/월/일
  */
  private void DialogDatePicker() {
    Calendar c = Calendar.getInstance();
    int cyear = c.get(Calendar.YEAR);
    int cmonth = c.get(Calendar.MONTH);
    int cday = c.get(Calendar.DAY_OF_MONTH);

    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
      // onDateSet method
      public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        String date_selected = String.valueOf(year) + "년" + " "
            + String.valueOf(monthOfYear + 1) + "월" + " "
            + String.valueOf(dayOfMonth) + "일";

        etCouponNoticeEndDate.setText(date_selected);


      }
    };
    DatePickerDialog alert = new DatePickerDialog(this, mDateSetListener,
        cyear, cmonth, cday);
    alert.show();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }


  //Activity activity;
  public void getColor(AutoResizeTextView tv_main) {
    MessageColorPicker colorPickerDialog = new MessageColorPicker(appcActivity,
        MainRegisterCouponShopStep1.this,
        Color.BLACK,
        tv_main);

    Window window = colorPickerDialog.getWindow();
    window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
    window.setGravity(Gravity.CENTER);

    //The below code is EXTRA - to dim the parent view by 70%
    WindowManager.LayoutParams lp = window.getAttributes();
    lp.dimAmount = 0.7f;
    lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
    colorPickerDialog.getWindow().setAttributes(lp);


    colorPickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    colorPickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    colorPickerDialog.show();

  }


  @Override
  public void colorChanged(int color, AutoResizeTextView tv_main) {
    tv_main.setTextColor(color);
  }

  @Override
  public void colorChanging(int color, AutoResizeTextView tv_main) {
    currentMessageTextColor = color;
    tv_main.setTextColor(color);
  }


  public void getBorderColor(AutoResizeTextView tv_main_shadow, boolean isCenterShadow) {
    MessageBorderColorPicker colorPickerDialog = new MessageBorderColorPicker(appcActivity,
        MainRegisterCouponShopStep1.this,
        Color.BLACK,
        tv_main_shadow,
        isCenterShadow);

    Window window = colorPickerDialog.getWindow();
    window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
    window.setGravity(Gravity.CENTER);

    //The below code is EXTRA - to dim the parent view by 70%
    WindowManager.LayoutParams lp = window.getAttributes();
    lp.dimAmount = 0.7f;
    lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
    colorPickerDialog.getWindow().setAttributes(lp);


    colorPickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    colorPickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    colorPickerDialog.show();

  }


  @Override
  public void bordercolorChanged(int color, AutoResizeTextView tv_main_shadow, boolean isCentered) {
    currentMessageShadowColor = color;
    tv_main_shadow.setShadowColor(MessageShadowRadius, color);
    tv_main_shadow.setCenterState(isCentered);
  }

  @Override
  public void bordercolorChanging(int color, AutoResizeTextView tv_main_shadow, boolean isCentered) {
    currentMessageShadowColor = color;
    tv_main_shadow.setShadowColor(MessageShadowRadius, color);
    tv_main_shadow.setCenterState(isCentered);
  }


  private void doTakeCameraAction() {

    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    TAKECAMERA_FILENAME_URI = UtilsForAlbumAndCamera.saveFileForCropImage();
    intentCamera.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, TAKECAMERA_FILENAME_URI);
    startActivityForResult(intentCamera, PICK_FROM_CAMERA);

  }

  private void doTakeAlbumAction() {
    Intent intentAlbum = new Intent(Intent.ACTION_PICK);
    intentAlbum.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);  //갤러리 전체 접근
    //intent.setType("image/*"); //갤러리 목록으로 접근
    startActivityForResult(intentAlbum, PICK_FROM_ALBUM); //
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (resultCode != RESULT_OK) {
      return;
    }
    switch (requestCode) {

      case PICK_FROM_CAMERA: {

        /*
        Intent intentCrop = new Intent("com.android.camera.action.CROP");
        intentCrop.setDataAndType(TAKECAMERA_FILENAME_URI, "image/*");
        intentCrop.putExtra("output", TAKECAMERA_FILENAME_URI);
        startActivityForResult(intentCrop, CROP_FROM_CAMERA);

        break;
        */


        final String full_path = tempDir_path + FOR_CROP_IMAGE_FILENAME;

        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            Intent intentCamera = new Intent(MainRegisterCouponShopStep1.this, cropStickerActivity.class);   // main.java 파일에서 이벤트를 발생시켜서 test를 불러옵니다.
            intentCamera.putExtra("FULL_PATH_FOR_CROP_IMAGE", full_path);
            intentCamera.putExtra("FILE_NAME_FOR_CROP_IMAGE", FOR_CROP_IMAGE_FILENAME);
            startActivity(intentCamera);
          }
        }, 0);

        break;

      }

      case CROP_FROM_CAMERA: {

        final String full_path = tempDir_path + FOR_CROP_IMAGE_FILENAME;

        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            Intent intentCamera = new Intent(MainRegisterCouponShopStep1.this, cropStickerActivity.class);   // main.java 파일에서 이벤트를 발생시켜서 test를 불러옵니다.
            intentCamera.putExtra("FULL_PATH_FOR_CROP_IMAGE", full_path);
            intentCamera.putExtra("FILE_NAME_FOR_CROP_IMAGE", FOR_CROP_IMAGE_FILENAME);
            startActivity(intentCamera);
          }
        }, 0);

        break;
      }


      case PICK_FROM_ALBUM: //앨범에서 이미지 선택 후
      {

        final Uri uriFromAlbum = data.getData();
        File original_file_forSticker = UtilsForAlbumAndCamera.getImageFile(uriFromAlbum, context);

        Uri uriForCropImage = UtilsForAlbumAndCamera.saveFileForCropImage();
        File copy_file_forSticker = new File(uriForCropImage.getPath());

        UtilsForAlbumAndCamera.copyFile(original_file_forSticker, copy_file_forSticker);

        final String fullPath = tempDir_path + FOR_CROP_IMAGE_FILENAME;

        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            Intent intentPickAlbum = new Intent(MainRegisterCouponShopStep1.this, cropStickerActivity.class);
            intentPickAlbum.putExtra("FULL_PATH_FOR_CROP_IMAGE", fullPath);
            intentPickAlbum.putExtra("FILE_NAME_FOR_CROP_IMAGE", FOR_CROP_IMAGE_FILENAME);

            startActivity(intentPickAlbum);
          }
        }, 0);

      }
    }

  }

  @Override
  public void keyFontClickedIndex(int position, Typeface typeface) {

    int sizeOfArray = arrayListStickerMessageView.size();
    final StickerMessageTextView clickedView = (StickerMessageTextView) arrayListStickerMessageView.get(sizeOfArray - 1);


    Toast.makeText(MainRegisterCouponShopStep1.this, "position is " + position,
        Toast.LENGTH_SHORT).show();
    Log.i("typeface is In Click", "" + String.valueOf(typeface));

    clickedView.tv_main.setTypeface(typeface);
    clickedView.tv_main.invalidate();

    clickedView.tv_main_shadow.setTypeface(typeface);
    clickedView.tv_main_shadow.invalidate();


    if (position == 0) {
      typeFacePath = "NBGothic.ttf";
    }

    if (position == 1) {
      typeFacePath = "lotte_happy_bold.ttf";
    }

    if (position == 2) {
      typeFacePath = "yalolja_OTF_Regular.otf";
    }

    if (position == 3) {
      typeFacePath = "BMDOHYEON_otf.otf";
    }
    if (position == 4) {
      typeFacePath = "BMHANNA_11yrs_otf.otf";
    }
    if (position == 5) {
      typeFacePath = "BMJUA_otf.otf";
    }
    if (position == 6) {
      typeFacePath = "Typo_DecoSolidSlash.ttf";
    }


    clickedView.tv_main.setTypefacePath(typeFacePath);


  }


  public Bitmap loadBitmapFromAssets(String urlStr) {
    Bitmap bitmap = null;

    AssetManager mngr = getResources().getAssets();

    try {
      InputStream is = mngr.open(urlStr);

      bitmap = BitmapFactory.decodeStream(is);

    } catch (Exception e) {
      Log.e("asa", "loadDrawable exception" + e.toString());
    }

    return bitmap;
  }


  /**
   * Checking keyboard height and keyboard visibility
   */
  int previousHeightDiffrence = 0;

  private void checkKeyboardHeight(final View parentLayout) {

    parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(
        new ViewTreeObserver.OnGlobalLayoutListener() {

          @Override
          public void onGlobalLayout() {

            Rect r = new Rect();
            parentLayout.getWindowVisibleDisplayFrame(r);

            int screenHeight = parentLayout.getRootView()
                .getHeight();
            Log.i("screenHeight", String.valueOf(screenHeight));

            int heightDifference = screenHeight - (r.bottom);
            Log.i("heightDifference", String.valueOf(heightDifference));

            if (previousHeightDiffrence - heightDifference > 50) {
              popupWindowFontFormat.dismiss();
            }

            previousHeightDiffrence = heightDifference;

            if (heightDifference > 100) {
              isKeyBoardVisible = true;
              changeKeyboardHeight(heightDifference);
            } else {
              isKeyBoardVisible = false;
            }
          }
        });
  }

  private void changeKeyboardHeight(int height) {

    if (height > 100) {
      keyboardHeight = height;

    /*
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
    LayoutParams.MATCH_PARENT, keyboardHeight);
    subMenuFontsFormatLayout.setLayoutParams(params);
    */
    }
  }


  public static void initImageLoader(Context context) {
    // This configuration tuning is custom. You can tune every option, you may tune some of them,
    // or you can create default configuration by
    //  ImageLoaderConfiguration.createDefault(this);
    // method.
    ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
    config.threadPriority(Thread.NORM_PRIORITY - 2);
    config.denyCacheImageMultipleSizesInMemory();
    config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
    config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
    config.tasksProcessingOrder(QueueProcessingType.LIFO);
    config.writeDebugLogs(); // Remove for release app

    // Initialize ImageLoader with configuration.
    ImageLoader.getInstance().init(config.build());
  }


  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      CreateTempSaveDialog_Step1.CreateTempSaveDialog(appcActivity);

      return true;
    }

    return super.onKeyDown(keyCode, event);
  }


}
