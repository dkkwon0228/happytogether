package com.ilovecat.happytogether.Painter;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonConstants;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonUtils;
import com.ilovecat.happytogether.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;


public class PainterActivity extends AppCompatActivity {
  public static DrawingView mDrawingView;
  public static String imgPathAndFileName;
  public static String maskPathAndFileName;
  public static ImageView ivOrgImage;
  public static AppCompatActivity appcActivity;


  private Handler mUiHandler = new Handler();

  private SeekBar mBrushStroke;
  private TableLayout mBrushColors;


  private boolean isOpen_filter = true;
  RelativeLayout layout_brushView;

  int orgBitmapWidth;
  int orgBitmapHeight;


  // see: http://stackoverflow.com/questions/25758294/how-to-fill-different-color-on-same-area-of-imageview-color-over-another-color/
  static int[] COLORS = {

      /*
      Color.rgb(255, 51, 255), // DARK PINK
      Color.rgb(255, 230, 102), // LIGHT YELLOW

      Color.rgb(148, 66, 50), // DARK MAROON
      Color.rgb(186, 123, 68), // LIGHT MAROON
      Color.rgb(252, 20, 20), // RED
      Color.rgb(102, 255, 255), // LIGHT BLUE

      Color.rgb(70, 78, 202), // DARK BLUE
      Color.rgb(190, 255, 91), // LIGHT GREEN
      Color.rgb(15, 230, 0), // DARK GREEN
      Color.rgb(123, 0, 230), // JAMBLI
      Color.rgb(255, 187, 50), // ORANGE
      Color.rgb(7, 5, 0), // BLACK

      Color.rgb(129, 128, 127), // GRAY
      Color.rgb(255, 4, 139), // PINK RED
      Color.rgb(51, 204, 255), // NAVY BLUE
      Color.rgb(102, 255, 204), // BRIGHT GREEN
      */

      Color.rgb(255, 255, 255),
      Color.rgb(0, 0, 0)

  };


  public static ImageView ivTouchPoint_White;
  public static ImageView ivTouchPoint_Black;


  // Magnifire
  static PointF zoomPos;
  static BitmapShader mShader;
  static Bitmap mutableBitmap;
  static Matrix matrix;
  static Paint mPaint;
  static Paint Paint;

  public static Bitmap orgImageBitmap;
  public static ToggleButton toggleVisibleDrawingView;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getWindow().setBackgroundDrawable(Utils.createCheckerBoard(getResources(), 16));
    setContentView(R.layout.painter_activity);
    appcActivity = this;


    // MainRegisterCoponShopStep1 클래스에서 Intent로 넘겨받은 스티커의 파일이름
    // 파일이름.확장자.jpg로 내부저장소/CropImage 폴더에 저장한다

    Intent intent = getIntent();
    String strCropedFileName = intent.getExtras().getString("CROPEDFILENAME");

    File imgFile = new File(CommonConstants.INNER_DISK_PATH + "CropImage/" + strCropedFileName);
    if (imgFile.exists()) {
      Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
      CommonUtils.SaveBitmapToFileCache(myBitmap, CommonConstants.INNER_DISK_PATH + "CropImage/" + strCropedFileName + ".jpg");
      myBitmap.recycle();
    }

    // 이미지뷰에 .jpg를세팅한다.
    imgPathAndFileName = CommonConstants.INNER_DISK_PATH + "CropImage/" + strCropedFileName + ".jpg";
    ivOrgImage = (ImageView) findViewById(R.id.ivOrgImage);
    Bitmap orgImageBitmap = BitmapFactory.decodeFile(imgPathAndFileName);
    orgBitmapWidth = orgImageBitmap.getWidth();
    orgBitmapHeight = orgImageBitmap.getHeight();

    // 바탕 이미지의 실제 가로 길이를 구한다.
    DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
    int width = dm.widthPixels;
    double ratio = (double) width / (double) orgImageBitmap.getWidth();
    double heightOfImageView = orgImageBitmap.getHeight() * ratio;
    Log.i("sas", String.valueOf(width));
    Log.i("sas", String.valueOf(orgImageBitmap.getWidth()));
    Log.i("sas", String.valueOf(ratio));
    Log.i("sas", String.valueOf(heightOfImageView));
    //


    mutableBitmap = Bitmap.createScaledBitmap(
        orgImageBitmap, width, (int) heightOfImageView, false);
    orgImageBitmap.recycle();


    //ivOrgImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    //ivOrgImage.setAdjustViewBounds(true);
    ivOrgImage.setImageBitmap(mutableBitmap);

    /*
    matrix = new Matrix();
    mShader = new BitmapShader(mutableBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    mPaint = new Paint();
    mPaint.setShader(mShader);
    zoomPos = new PointF();
    Paint = new Paint(Color.RED);
    */


    // 드로잉뷰 투명하게.
    mDrawingView = (DrawingView) findViewById(R.id.drawing_view);
    mDrawingView.setBackgroundColor(Color.parseColor("#00ffffff"));
    //mDrawingView.setShape(R.drawable.img_a_inner, R.drawable.img_a);
    mDrawingView.setDrawingColor(getResources().getColor(R.color.ab_color));

    // 드로잉뷰의 가로길이를 이미지뷰의 가로길이로 세팅한다.
    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mDrawingView.getLayoutParams();
    params.height = (int) heightOfImageView;
    params.width = MATCH_PARENT;
    mDrawingView.setLayoutParams(params);

    // 터치포인트뷰
    AbsoluteLayout absLayoutTouchPont = (AbsoluteLayout) findViewById(R.id.abslayoutTouchPont);
    FrameLayout.LayoutParams paramsTouchPoint = (FrameLayout.LayoutParams) absLayoutTouchPont.getLayoutParams();
    paramsTouchPoint.height = (int) heightOfImageView;
    paramsTouchPoint.width = MATCH_PARENT;
    absLayoutTouchPont.setLayoutParams(params);


    ivTouchPoint_White = (ImageView) findViewById(R.id.ivTouchPoint_white);
    ivTouchPoint_Black = (ImageView) findViewById(R.id.ivTouchPoint_black);

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
    final AnimationSet growAndShrink = new AnimationSet(true);
    growAndShrink.setInterpolator(new LinearInterpolator());
    growAndShrink.addAnimation(grow);
    growAndShrink.addAnimation(shrink);

    ivTouchPoint_White.startAnimation(growAndShrink);


    // 브러쉬 SeekBar
    mBrushStroke = (SeekBar) findViewById(R.id.brush_stroke);

    mBrushStroke.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
      }

      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mDrawingView.setDrawingStroke(progress);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
      }
    });
    mBrushStroke.setProgress(15);

    // 전경 그리기
    final ImageView ivForeGroundDraw = (ImageView) findViewById(R.id.ivForeGroundDraw);
    ivForeGroundDraw.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {


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


        ivTouchPoint_White.setVisibility(v.VISIBLE);
        ivTouchPoint_Black.setVisibility(v.INVISIBLE);

        ivTouchPoint_White.startAnimation(growAndShrink);
        //

        //mDrawingView.setDrawingColor(COLORS[((Integer) v.getTag()).intValue()]);
        mDrawingView.setDrawingColor(Color.WHITE);
        mDrawingView.setColorModeWhite();
      }
    });

    // 배경 그리기
    final ImageView ivBackGroundDraw = (ImageView) findViewById(R.id.ivBackGroundDraw);
    ivBackGroundDraw.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {


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

        ivTouchPoint_White.setVisibility(v.INVISIBLE);
        ivTouchPoint_Black.setVisibility(v.VISIBLE);
        ivTouchPoint_Black.startAnimation(growAndShrink);

        //

        //mDrawingView.setDrawingColor(COLORS[((Integer) v.getTag()).intValue()]);
        mDrawingView.setDrawingColor(Color.BLACK);
        mDrawingView.setColorModeBlack();
      }
    });


    // Undo
    ImageView ivUndo = (ImageView) findViewById(R.id.action_undo);
    ivUndo.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        mDrawingView.undoOperation();
      }
    });

    // Redo
    ImageView ivRedo = (ImageView) findViewById(R.id.action_redo);
    ivRedo.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        mDrawingView.redoOperation();
      }
    });

    // Eraser
    ImageView ivEraser = (ImageView) findViewById(R.id.action_eraser);
    ivEraser.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        mDrawingView.enableEraser();
      }
    });

    // Toggle Drawing View Visibility
    toggleVisibleDrawingView = (ToggleButton) findViewById(R.id.action_ToggleVisible);
    toggleVisibleDrawingView.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        if (toggleVisibleDrawingView.isChecked()) {
          toggleVisibleDrawingView.setBackgroundDrawable(
              getResources().
                  getDrawable(R.drawable.ic_visibility_white)
          );

          mDrawingView.setVisibility(View.INVISIBLE);
          PainterActivity.ivTouchPoint_White.setVisibility(View.INVISIBLE);
          PainterActivity.ivTouchPoint_Black.setVisibility(View.INVISIBLE);



        } else {
          toggleVisibleDrawingView.setBackgroundDrawable(
              getResources().
                  getDrawable(R.drawable.ic_visibility_off_white)
          );

          mDrawingView.setVisibility(View.VISIBLE);
          PainterActivity.ivTouchPoint_White.setVisibility(View.VISIBLE);
          PainterActivity.ivTouchPoint_Black.setVisibility(View.VISIBLE);


        } // end if
      }
    });

    // Eraser All
    ImageView iveraserAll = (ImageView) findViewById(R.id.action_eraserAll);
    iveraserAll.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        mDrawingView.clearDrawing();
      }
    });

    // 브러쉬뷰 레이아웃
    layout_brushView = (RelativeLayout) findViewById(R.id.brushView_layout); //필터 레이아웃

    ImageView ivBrush = (ImageView) findViewById(R.id.action_brush);
    ivBrush.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        toggle_filter();
      }
    });


    ImageView ivRunGrabCut = (ImageView) findViewById(R.id.action_removebg);
    ivRunGrabCut.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        mDrawingView.setBackgroundColor(Color.parseColor("#525252"));
        mDrawingView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mDrawingView.setDrawingCacheEnabled(true);
        mDrawingView.buildDrawingCache();
        Bitmap viewCache = mDrawingView.getDrawingCache();
        Bitmap bitmap = viewCache.copy(viewCache.getConfig(), false);
        Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, orgBitmapWidth, orgBitmapHeight, true);
        mDrawingView.setDrawingCacheEnabled(false);


        boolean isContainWhite = false;
        // 전경 객체에서 흰색영역 체크
        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
          for (int y = 0; y < resizeBitmap.getHeight(); y++) {
            if (resizeBitmap.getPixel(x, y) == Color.WHITE) {
              isContainWhite = true;
              break;
            }
          }

        }

        boolean isContainBlack = false;
        // 배경 객체에서 검은색영역 체크
        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
          for (int y = 0; y < resizeBitmap.getHeight(); y++) {
            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
              isContainBlack = true;
              break;
            }
          }

        }

        if (isContainWhite && isContainBlack) {
          new SavePngtoJpgTask().execute(resizeBitmap);
        }


        if (!isContainWhite) {

          mDrawingView.setBackgroundColor(Color.parseColor("#00ffffff"));
          viewCache.recycle();
          bitmap.recycle();
          resizeBitmap.recycle();


          Toast.makeText(PainterActivity.appcActivity, "전경영역에 흰색으로 그려주세요", Toast.LENGTH_LONG).show();

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


          ivTouchPoint_White.setVisibility(v.VISIBLE);
          ivTouchPoint_Black.setVisibility(v.INVISIBLE);

          ivTouchPoint_White.startAnimation(growAndShrink);
          ivForeGroundDraw.startAnimation(growAndShrink);

          //

          //mDrawingView.setDrawingColor(COLORS[((Integer) v.getTag()).intValue()]);
          mDrawingView.setDrawingColor(Color.WHITE);
          mDrawingView.setColorModeWhite();


        }

        if (!isContainBlack) {

          mDrawingView.setBackgroundColor(Color.parseColor("#00ffffff"));
          viewCache.recycle();
          bitmap.recycle();
          resizeBitmap.recycle();


          Toast.makeText(PainterActivity.appcActivity, "배경영역에 검은색으로 그려주세요", Toast.LENGTH_LONG).show();

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


          ivTouchPoint_White.setVisibility(v.INVISIBLE);
          ivTouchPoint_Black.setVisibility(v.VISIBLE);

          ivTouchPoint_Black.startAnimation(growAndShrink);
          ivBackGroundDraw.startAnimation(growAndShrink);


          //mDrawingView.setDrawingColor(COLORS[((Integer) v.getTag()).intValue()]);
          mDrawingView.setDrawingColor(Color.BLACK);
          mDrawingView.setColorModeBlack();


        }
      }
    });


    ImageView ivSave = (ImageView) findViewById(R.id.action_save);
    ivSave.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {


        finish();


      }
    });


    // 애니메니션 적용
    ivForeGroundDraw.startAnimation(growAndShrink);
    ivBackGroundDraw.startAnimation(growAndShrink);


    //mBrushColors = (TableLayout) findViewById(R.id.brush_colors);
    //createBrushPanelContent();

  }

  private void createBrushPanelContent() {
    TableRow tableRow = null;
    final int rowLimit = 8;
    for (int i = 0; i < COLORS.length; i++) {
      if ((i % rowLimit) == 0) {
        tableRow = new TableRow(this);
        mBrushColors.addView(tableRow, new TableLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
      }
      tableRow.addView(createToolButton(tableRow, R.drawable.ic_paint_splot, i));
    }
  }


  private ImageButton createToolButton(ViewGroup parent, int drawableResId, int index) {
    ImageButton button = (ImageButton) getLayoutInflater().inflate(R.layout.painter_button_spot, parent, false);
    button.setImageResource(drawableResId);
    button.setOnClickListener(mButtonClick);
    if (index != -1) {
      button.setTag(Integer.valueOf(index));
      button.setColorFilter(COLORS[index]);
    }
    return button;
  }

  private View.OnClickListener mButtonClick = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      mDrawingView.setDrawingColor(COLORS[((Integer) v.getTag()).intValue()]);
    }
  };

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    return true;
  }


  /*
   * 레이아웃의 왼쪽 상단이 (0.0f, 0.0f)에서 아래로 이동은 +, 위로 이동은 -, 1.0f는 100% 크기만큼
   * 필터, 스티커, 텍스트 버튼을 클릭하면 그 하위메뉴가 아래로 이동, 위로 이동, 토글한다.
   * 위의 코드가 작성되면서 이 코드들은 적용 되지 않았음
   * 14/10/29
   *
   */
  /*
   * 필터 하위메뉴 토글
  */
  public void toggle_filter() {

    /*
     *스티커 선택화면에서 스티커 선택시 스티커가 페이저뷰에 바로 보이는 방식일 경우
    */
    //CURRENT_MENU_POSITION = "filter";

    TranslateAnimation anim = null;

    isOpen_filter = !isOpen_filter;

    if (isOpen_filter) {
      layout_brushView.setVisibility(View.VISIBLE);
      anim = new TranslateAnimation(
          0.0f,
          0.0f,
          layout_brushView.getHeight(),
          0.0f); //Y 좌표로 Up

      //anim.setFillAfter(true); // 애니메이션 종료후 이동한좌표에 그대로 자리잡는다는 설정
      anim.setDuration(300);
      anim.setInterpolator(new AccelerateInterpolator(1.0f));
      layout_brushView.startAnimation(anim);

    } else {

      anim = new TranslateAnimation(
          0.0f,
          0.0f,
          0.0f,
          layout_brushView.getHeight()); //Y 좌표로 Down
      //Animation.RELATIVE_TO_SELF 는 뷰자신의 크기를 기준으로 이동하는 타입
      anim.setAnimationListener(filter_collapseListener); //해당레이아웃들이 터치가 안되게 한다. 14/10/27

      //anim.setFillAfter(true); // 애니메이션 종료후 이동한좌표에 그대로 자리잡는다는 설정
      anim.setDuration(300);
      anim.setInterpolator(new AccelerateInterpolator(1.0f));
      layout_brushView.startAnimation(anim);
    }

  }

  /*
   * 레이아웃이 아래로 내려간 후 원래위치에서 클릭이 안되게 한다. 14/10/29
   */
  Animation.AnimationListener filter_collapseListener = new Animation.AnimationListener() {
    public void onAnimationEnd(Animation animation) {

      //RelativeLayout filterLayout = (RelativeLayout) findViewById(R.id.filter_layout);
      layout_brushView.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }
  };

  private void resizeView(View view, int newWidth, int newHeight) {
    try {
      Constructor<? extends LinearLayout.LayoutParams> ctor = (Constructor<? extends LinearLayout.LayoutParams>) view.getLayoutParams().getClass().getDeclaredConstructor(int.class, int.class);
      view.setLayoutParams(ctor.newInstance(newWidth, newHeight));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    // put your code here...
    mDrawingView.setBackgroundColor(Color.parseColor("#00ffffff"));
    mDrawingView.invalidate();

  }


}
