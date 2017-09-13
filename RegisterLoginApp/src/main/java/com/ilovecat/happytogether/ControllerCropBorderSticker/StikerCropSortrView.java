package com.ilovecat.happytogether.ControllerCropBorderSticker;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.ilovecat.happytogether.ControllerMultiTouch.MultiTouchController;
import com.ilovecat.happytogether.ControllerMultiTouch.MultiTouchController.MultiTouchObjectCanvas;
import com.ilovecat.happytogether.ControllerMultiTouch.MultiTouchController.PointInfo;
import com.ilovecat.happytogether.ControllerMultiTouch.MultiTouchController.PositionAndScale;
import com.ilovecat.happytogether.R;

import java.util.ArrayList;


//public class PhotoSortrView extends View implements MultiTouchObjectCanvas<PhotoSortrView.Img> {
public class StikerCropSortrView extends FrameLayout implements MultiTouchObjectCanvas<StikerCropSortrView.Img> {

  //private StikerOverlayView mStikerOverlayView;

  float mDisplayWidth; //단말가의 화면 가로 크기
  float mDisplayHeight;

  long touchStartTime;
  long touchEndTime;

  int mCount = 0;
  public int[] IMAGES = new int[10]; //스티커 이미지 배열
  public int[] GUIDE_LINE = new int[10];
  public static ArrayList<Img> mImgArryList = new ArrayList<Img>();
  public MultiTouchController<Img> multiTouchController = new MultiTouchController<Img>(this);
  private PointInfo currTouchPoint = new PointInfo();
  private boolean mShowDebugInfo = false;
  private static final int UI_MODE_ROTATE = 1;

  public static int UI_MODE_ANISOTROPIC_SCALE = 2;  //0은 가로/세로 비율 유지, 2는 가로/세로 비율 무시

  //private int mUIMode = UI_MODE_ROTATE;
  private int mUIMode = UI_MODE_ANISOTROPIC_SCALE;
  // --
  private Paint mLinePaintTouchPointCircle = new Paint();
  private Paint mLinePaintTouchPointCircleRotate = new Paint();
  private Paint mLineSelectedImageBorder = new Paint();

  View myView;
  Resources mRes;
  //Context contextForAnim;

  float wsDrawable;
  float hsDrawable;

  // The bounding box around the Bitmap that we are cropping.
  private Rect mBitmapRect;
  // The Paint used to darken the surrounding areas outside the crop area.
  private Paint mBackgroundPaint;

  public StikerCropSortrView(Context context) {
    this(context, null);
  }

  public StikerCropSortrView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public StikerCropSortrView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    init(context);
  }


  public void setmUIMode(int mUIMode) {
    this.UI_MODE_ANISOTROPIC_SCALE = mUIMode;
  }

  void init(Context context) {

    mLinePaintTouchPointCircle.setColor(Color.YELLOW);
    mLinePaintTouchPointCircle.setStrokeWidth(10);
    mLinePaintTouchPointCircle.setStyle(Style.STROKE);
    mLinePaintTouchPointCircle.setAntiAlias(true);
    //setBackgroundColor(Color.BLACK);

    mLinePaintTouchPointCircleRotate.setColor(Color.BLUE);
    mLinePaintTouchPointCircleRotate.setStrokeWidth(10);
    mLinePaintTouchPointCircleRotate.setStyle(Style.STROKE);
    mLinePaintTouchPointCircleRotate.setAntiAlias(true);
    //setBackgroundColor(Color.BLACK);

    mLineSelectedImageBorder.setColor(Color.RED);
    mLineSelectedImageBorder.setStrokeWidth(5);
    mLineSelectedImageBorder.setAntiAlias(true);


  }

  /**
   * Called by activity's onResume() method to load the images
   */
  //public void loadImages(Drawable drawable, Context context, int resId, StikerCropSortrView view, int imageViewWidth, int imageViewHeight, View rootView) {
  public void loadImages(Context context, int resId, int resIdMask, StikerCropSortrView view, int imageViewWidth, int imageViewHeight, View rootView) {


    myView = view;

    //contextForAnim = context;

    mRes = context.getResources();
    Log.i("mRes is ", "" + mRes);

		/*
		 * 단말기의 화면 크기
		 */
    DisplayMetrics metrics = mRes.getDisplayMetrics();
    mDisplayWidth = mRes.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.max(metrics.widthPixels,
        metrics.heightPixels) : Math.min(metrics.widthPixels, metrics.heightPixels);
    mDisplayHeight = mRes.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.min(metrics.widthPixels,
        metrics.heightPixels) : Math.max(metrics.widthPixels, metrics.heightPixels);

    Log.i("mDisplayWidth is ", "" + mDisplayWidth);
    Log.i("mDisplayHeight is ", "" + mDisplayHeight);
    //끝


    if (mImgArryList.size() >= 1) {
      mCount = mImgArryList.size();
    } else {
      mCount = 0;
    }

    //동적할당
    IMAGES[mCount] = resId;
    Log.i("resId is ", "" + resId);
		
		/*
		 *  Img형 어래이리스트에 할당
		 */
    final Img stikerImg = new Img(IMAGES[mCount], resIdMask, mRes, context);
    mImgArryList.add(mCount, stikerImg);
		
		
		
		/*
		 * Img형 어래이리스트 마지막에 할당하고 로드한다.
		 */
    int k = mImgArryList.size() - 1;
    Img firstLoadingImg = mImgArryList.get(k);
    //firstLoadingImg.load(drawable, mRes, IMAGES[k], firstLoadingImg, context, imageViewWidth, imageViewHeight, myView, mDisplayHeight);
    firstLoadingImg.load(mRes, IMAGES[k], resIdMask, firstLoadingImg, context, imageViewWidth, imageViewHeight, myView);
    //
		
		
		/*
		// XML에 있는 레이아웃을 가져온다.
	    RelativeLayout l = (RelativeLayout) rootView.findViewById(R.id.menuviewStiker);
	    // 이미지뷰에 등록할 비트맵을 생성한다.
        Bitmap bm = BitmapFactory.decodeResource(getResources(), resId);
        // 매트릭스를 생성하고 초기화한다.
        Matrix m = new Matrix();
        m.postTranslate( 0,  0);    // 이미지뷰에 등록할 비트맵의 초기위치.
        
        // 이미지뷰를 생성하고 초기화한다.
       // ImageView v2 = new ImageView(getBaseContext());
        ImageView v2 = new ImageView(context);
        v2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        v2.setScaleType(ScaleType.MATRIX);  // 매트릭스로 이동시키기 때문에 scaleType은 반드시 Matrix로 지정해야한다.
        v2.setImageBitmap(bm);      // 이미지를 등록한다.
        v2.setImageMatrix(m);       // 매트릭스를 이미지뷰에 적용한다.
         
        l.addView(v2);      // 레이아웃에 이미지뷰를 등록한다.
         */

    invalidate();
    mCount++;
  }


  /**
   * Called by activity's onPause() method to free memory used for loading the images
   */
  public void unloadImages() {
    int n = mImgArryList.size();
    for (int i = 0; i < n; i++)
      mImgArryList.get(i).unload();
  }

  // ---------------------------------------------------------------------------------------------------
  @Override
  protected void onDraw(Canvas canvas) {

    super.onDraw(canvas);


    // Draw translucent background for the cropped area.
    //drawBackground(canvas, mBitmapRect, mImgArryList);

    int n = mImgArryList.size();
    for (int i = 0; i < n; i++) {

      int k = n - 1;
      if (i == k) {

        if (mImgArryList.get(i).isSelectStikerSpace) {

          mImgArryList.get(i).drawableGuideLine.mutate().setAlpha(255);
          mImgArryList.get(i).drawableGuideLineRotate.mutate().setAlpha(255);
          mImgArryList.get(i).drawableGuideLineDel.mutate().setAlpha(255);
          mImgArryList.get(i).drawable.mutate().setAlpha(255);

          if (mImgArryList.get(i).isSelectScaleButton) {
            //Img img = mImgArryList.get(i);

            //mUIMode = UI_MODE_ANISOTROPIC_SCALE;
								
								/*
								 * 스티커 스케일의 터치상태를 보여준다
								 */
            //scaleStikerDisplay(canvas, img);
            //
								
								/*
								mImgArryList.get(i).drawableGuideLine.mutate().setAlpha(255);
								mImgArryList.get(i).drawableGuideLineRotate.mutate().setAlpha(255);
								mImgArryList.get(i).drawableGuideLineDel.mutate().setAlpha(255);
								*/
          }


        } else {

          mImgArryList.get(i).isSelectStikerSpace = false;

          mImgArryList.get(i).drawableGuideLine.mutate().setAlpha(255);
          mImgArryList.get(i).drawableGuideLineRotate.mutate().setAlpha(255);
          mImgArryList.get(i).drawableGuideLineDel.mutate().setAlpha(255);
          mImgArryList.get(i).drawable.mutate().setAlpha(255);
        }
				
			/*
			 * 선택이 안된 아이템
			 */
      } else {

        mImgArryList.get(i).drawableGuideLine.mutate().setAlpha(0);
        mImgArryList.get(i).drawableGuideLineRotate.mutate().setAlpha(0);
        mImgArryList.get(i).drawableGuideLineDel.mutate().setAlpha(0);
        mImgArryList.get(i).drawable.mutate().setAlpha(255);

      }

      mImgArryList.get(i).draw(canvas, n, myView);
      Log.i("mImgArryList.get(i) ", "" + i + "=>" + mImgArryList.get(i));
			
			/*
			ObjectAnimator anim 
			    = ObjectAnimator.ofFloat(mImgArryList.get(i), "alpha", 
			        1.0f, 0.25f, 0.75f, 0.15f, 0.5f, 0.0f);  
			anim.setDuration(5000);
			anim.setInterpolator(new LinearInterpolator());
			anim.start();
			*/

    }
		
		
		/*
		 * 페이저 화면에 스티커가 로드될 때 스티커의 테두리, 버튼 지움
		 */
		/*
		int n2 = com.appmaker.gcmtest.localtakealbum.selectedImagesActivity.stikersSorterViewInSIA0.mImgArryList.size();
		for (int i = 0; i < n2; i++) {
		
			com.appmaker.gcmtest.localtakealbum.selectedImagesActivity.stikersSorterViewInSIA0.mImgArryList.get(i).isSelectStikerSpace = false;
			com.appmaker.gcmtest.localtakealbum.selectedImagesActivity.stikersSorterViewInSIA0.mImgArryList.get(i).drawableGuideLine.mutate().setAlpha(0);
			com.appmaker.gcmtest.localtakealbum.selectedImagesActivity.stikersSorterViewInSIA0.mImgArryList.get(i).drawableGuideLineRotate.mutate().setAlpha(0);
			com.appmaker.gcmtest.localtakealbum.selectedImagesActivity.stikersSorterViewInSIA0.mImgArryList.get(i).drawableGuideLineDel.mutate().setAlpha(0);
			//com.appmaker.gcmtest.localtakealbum.selectedImagesActivity.stikersSorterViewInSIA0.mImgArryList.get(i).draw(canvas);
		}
		//끝 
		*/
    if (mShowDebugInfo) {
			/*
			 * 스티커의 터치상태를 화면에 표시한다.
			 */
      //drawMultitouchDebugMarks(canvas);
    }
  }


  // ---------------------------------------------------------------------------------------------------


  public void trackballClicked() {
    mUIMode = (mUIMode + 1) % 3;
    invalidate();
  }


  public int getDispledSkikerNum() {
    return mCount;
  }

  private void drawMultitouchDebugMarks(Canvas canvas) {
    if (currTouchPoint.isDown()) {
      float[] xs = currTouchPoint.getXs();
      float[] ys = currTouchPoint.getYs();
      float[] pressures = currTouchPoint.getPressures();
      int numPoints = Math.min(currTouchPoint.getNumTouchPoints(), 2);
      for (int i = 0; i < numPoints; i++) {
        canvas.drawCircle(xs[i], ys[i], 50 + pressures[i] * 80, mLinePaintTouchPointCircle);
        //canvas.drawLine(this.minX, rotateMINY, xs[i], ys[i], mLinePaintTouchPointCircle);
      }
      if (numPoints == 2)
        canvas.drawLine(xs[0], ys[0], xs[1], ys[1], mLinePaintTouchPointCircle);
    }
  }

  private void scaleStikerDisplay(Canvas canvas, Img img) {
    if (currTouchPoint.isDown()) {
      float[] xs = currTouchPoint.getXs();
      float[] ys = currTouchPoint.getYs();
      float[] pressures = currTouchPoint.getPressures();
      int numPoints = Math.min(currTouchPoint.getNumTouchPoints(), 2);
      for (int i = 0; i < numPoints; i++) {
        //터치된 좌표 동그라미
        canvas.drawCircle(xs[i], ys[i], 25 + pressures[i] * 40, mLinePaintTouchPointCircleRotate);
        //스티커의 왼쪽 상단 꼭지점 동그라미
        canvas.drawCircle(img.rotateMINX, img.rotateMINY, 25 + pressures[i] * 40, mLinePaintTouchPointCircleRotate);
        //왼쪽상단 꼭지점 에서 두번째 터치된 좌표
        canvas.drawLine(img.rotateMINX, img.rotateMINY, xs[i], ys[i], mLinePaintTouchPointCircleRotate);
      }
      if (numPoints == 2)
        canvas.drawLine(xs[0], ys[0], xs[1], ys[1], mLinePaintTouchPointCircle);
    }
  }

  // ---------------------------------------------------------------------------------------------------

  /**
   * Pass touch events to the MT controller
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {


    float touchX1 = event.getX();
    float touchY1 = event.getY();

    //Log.i("typeOfButtonClick is",""+typeOfButtonClick);
    Log.i("touchX1 is", "" + touchX1);
    Log.i("touchY1 is", "" + touchY1);
    //Log.i("touchX2 is",""+touchX2);
    //Log.i("touchY2 is",""+touchY2);

    int n = mImgArryList.size();
    Log.i("mImgArryList.size() is", "" + n);

    Img im = null;

    if (n != 0) {
      im = mImgArryList.get(n - 1);
      im.isSelectStikerSpace = im.containsPoint(touchX1, touchY1);
      //Log.i("im.isSelectStikerSpace is",""+im.isSelectStikerSpace);

      im.isSelectScaleButton = im.containsPointScale(touchX1, touchY1);
      //Log.i("im.isSelectScaleButton is",""+im.isSelectScaleButton);

      invalidate();

    }


    return multiTouchController.onTouchEvent(event); // 이동

		/*
		if ( im.minX > 0 && im.minY > 0 && im.minX + im.scaledWidthDrawable < im.displayWidth 
				&& im.minY + im.scaledHeightDrawable < im.displayHeight ) {
			
		}
		*/
		
		/*
		if(im.isSelectScaleButton) {
			myView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0.0f, 0.0f, 0)); 
			im.isSelectScaleButton = im.containsPointScale(touchX1, touchY1);
		}
		*/
		
		
		
				
		
		
		
		
		/*
		switch (event.getAction()) {

        case MotionEvent.ACTION_DOWN:
            onActionDown(event.getX(), event.getY());
            return true;

        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            getParent().requestDisallowInterceptTouchEvent(false);
            onActionUp();
            return true;

        case MotionEvent.ACTION_MOVE:
            onActionMove(event.getX(), event.getY());
            getParent().requestDisallowInterceptTouchEvent(true);
            return true;

        default:
            return false;
		}
		*/
  }


  /**
   * Get the image that is under the single-touch point, or return null (canceling the drag op) if
   * none
   */
  public Img getDraggableObjectAtPoint(PointInfo pt) {

    float x = pt.getX(), y = pt.getY();
    Log.i("XXXX is", "" + x);
    Log.i("YYYY is", "" + y);

    final float left = mImgArryList.get(0).getMinX();
    final float top = mImgArryList.get(0).getMinY();
    final float right = mImgArryList.get(0).getMaxX();
    final float bottom = mImgArryList.get(0).getMaxY();


    int n = mImgArryList.size();
    for (int i = n - 1; i >= 0; i--) {
      Img im = mImgArryList.get(i);
      Log.i("im  is", "" + im);
      if (im.containsPoint(x, y)) {
        im.isSelectStikerSpace = true;

        return im;
      }

      if (im.containsPointDel(x, y)) {
        mImgArryList.remove(i);
        mCount = mImgArryList.size();
        invalidate();
        Log.i("mImgArryList", mImgArryList.toString());
      }

      if (im.containsPointScale(x, y)) {
        im.isSelectScaleButton = true;
      }

    }
    return null;
  }

  /**
   * Select an object for dragging. Called whenever an object is found to be under the point
   * (non-null is returned by getDraggableObjectAtPoint()) and a drag operation is starting. Called
   * with null when drag op ends.
   */
  public void selectObject(Img img, PointInfo touchPoint) {
    currTouchPoint.set(touchPoint);

    if (img != null) {
      // Move image to the top of the stack when selected
      Log.i("currTouchPoint", currTouchPoint.toString());
      Log.i("img.getCenterX() is ", "" + img.getCenterX());
      Log.i("img.getCenterY() is ", "" + img.getCenterY());
      Log.i("img.getScaleX() is ", "" + img.getScaleX());
      Log.i("img.getScaleY() is ", "" + img.getScaleY());
      Log.i("img.getAngle() is ", "" + img.getAngle());
      Log.i("mImgArryList.size() is ", "" + mImgArryList.size());
      mImgArryList.remove(img);
      mImgArryList.add(img);
			
			/*
			 * 페이저뷰에 스티커가 바로 보이는 방식을 경우 스티커를 선택할때 페이저뷰가 드래그 안되게 한다
			 * 
			com.appmaker.gcmtest.localtakealbum.selectedImagesActivity.pager.setOnTouchListener(new OnTouchListener()
		    {           
		        @Override
		        public boolean onTouch(View v, MotionEvent event)
		        {
		            return true;
		        }
		    });
		    */


    } else {
      // Called with img == null when drag stops.
			
			/*
			 * 페이저뷰에 스티커가 바로 보이는 방식을 경우 스티커를 선택이 해제되었을 경우 페이저뷰가 드래그 되게 한다
			com.appmaker.gcmtest.localtakealbum.selectedImagesActivity.pager.setOnTouchListener(new OnTouchListener()
		    {           
		        @Override
		        public boolean onTouch(View v, MotionEvent event)
		        {
		            return false;
		        }
		    });
		    */
    }


    invalidate();
  }


  /**
   * Get the current position and scale of the selected image. Called whenever a drag starts or is
   * reset.
   */

  public void getPositionAndScale(Img img, PositionAndScale objPosAndScaleOut) {
    // FIXME affine-izem (and fix the fact that the anisotropic_scale part requires averaging the two scale factors)
    objPosAndScaleOut.set(
        img.getCenterX(),
        img.getCenterY(),
        (mUIMode & UI_MODE_ANISOTROPIC_SCALE) == 0,
        (img.getScaleX() + img.getScaleY()) / 2,
        (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0,
        img.getScaleX(), img.getScaleY(),
        (mUIMode & UI_MODE_ROTATE) != 0,
        img.getAngle()
    );
  }

  /**
   * Set the position and scale of the dragged/stretched image.
   */

  public boolean setPositionAndScale(Img img, PositionAndScale newImgPosAndScale, PointInfo touchPoint) {
    currTouchPoint.set(touchPoint);
    boolean ok = img.setPos(newImgPosAndScale);
    if (ok)
      invalidate();
    return ok;
  }

  /*
   * Img Class Start
   */
  public class Img {

    private int resId;
    private int resIdMask;
    Drawable drawable;
    Drawable drawableMask;
    public Drawable drawableGuideLine;
    public Drawable drawableGuideLineRotate;
    public Drawable drawableGuideLineDel;
    private boolean firstLoad;
    private boolean isSelectScaleButton;
    public boolean isSelectStikerSpace;
    int widthDrawable;
    int heightDrawable;
    private int widthDrawableDel;
    private int heightDrawableDel;
    private int widthDrawableRotate;
    private int heightDrawableRotate;
    private int displayWidth;
    private int displayHeight;
    public float centerX;
    public float centerY;
    float scaleX;
    float scaleY;
    public float angle;
    float minX;
    float maxX;
    float minY;
    float maxY;
    private float minXDel;
    private float maxXDel;
    private float minYDel;
    private float maxYDel;
    private float minXRotate;
    private float maxXRotate;
    private float minYRotate;
    private float maxYRotate;
    private float rotateMINX, rotateMINY, rotateMAXX, rotateMAXY;
    private float rotateMINXDel, rotateMINYDel, rotateMAXXDel, rotateMAXYDel;
    private float rotateMINXRotate, rotateMINYRotate, rotateMAXXRotate, rotateMAXYRotate;
    private static final float SCREEN_MARGIN = 100;
    public Bitmap forMaskBitmap;
    public Bitmap toAdjustBitmap;
    public float scaledWidthDrawable;
    public float scaledHeightDrawable;


    public Img(int resId, int resIdMask, Resources res, Context context) {
      this.resId = resId;
      this.resIdMask = resIdMask;
      this.firstLoad = true;
      getMetrics(res);

      init(context);

    }

    private void getMetrics(Resources res) {
      DisplayMetrics metrics = res.getDisplayMetrics();
      // The DisplayMetrics don't seem to always be updated on screen rotate, so we hard code a portrait
      // screen orientation for the non-rotated screen here...
      // this.displayWidth = metrics.widthPixels;
      // this.displayHeight = metrics.heightPixels;
      this.displayWidth = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.max(metrics.widthPixels,
          metrics.heightPixels) : Math.min(metrics.widthPixels, metrics.heightPixels);
      this.displayHeight = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.min(metrics.widthPixels,
          metrics.heightPixels) : Math.max(metrics.widthPixels, metrics.heightPixels);
    }

    /**
     * Called by activity's onResume() method to load the images
     */
    //public void load(Drawable drawableFromTextView, Resources res, int resId, Img img, Context context
    //				, int imgaeViewWidth, int imageViewHeight, View view, float mDisplayHeight) {
    public void load(Resources res, int resId, int resIdMask, Img img, Context context
        , int imgaeViewWidth, int imageViewHeight, View view) {

      getMetrics(res);
      this.drawable = res.getDrawable(resId);
      this.drawableMask = res.getDrawable(resIdMask);
      //this.drawable = drawableFromTextView;
      this.drawableGuideLine = getResources().getDrawable(R.drawable.stiker_guideline);
      this.drawableGuideLineRotate = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_stiker_rotate);
      this.drawableGuideLineDel = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_stiker_del);
      this.widthDrawable = drawable.getIntrinsicWidth();
      this.heightDrawable = drawable.getIntrinsicHeight();
      this.widthDrawableDel = drawableGuideLineDel.getIntrinsicWidth();
      this.heightDrawableDel = drawableGuideLineDel.getIntrinsicHeight();
      this.widthDrawableRotate = drawableGuideLineRotate.getIntrinsicWidth();
      this.heightDrawableRotate = drawableGuideLineRotate.getIntrinsicHeight();
      float cx, cy, sx, sy;

      if (img.firstLoad) {

        //cx = displayWidth / 2;
        //cy = displayHeight / 2;
        cx = imgaeViewWidth / 2;
        cy = imageViewHeight / 2;
        //cy = mDisplayHeight / 2;

        Log.i("cx is ", "" + cx);
        Log.i("cy is ", "" + cy);


        //########################################
        // 최초 로드될때 마스트 이미지의 혹대 배율이다.
        //########################################
        float sc = 1.5f;
        //

        sx = sy = sc;
        this.firstLoad = false;
        this.isSelectStikerSpace = true;
        setPos(cx, cy, sx, sy, 0.0f);

        //Log.i("drawableFromTextView is ",""+drawableFromTextView);
        Log.i("this.drawable is ", "" + this.drawable);

      } else {

        PositionAndScale objPosAndScaleOut = new PositionAndScale();
        objPosAndScaleOut.set(
            img.getCenterX(),
            img.getCenterY(),
            (mUIMode & UI_MODE_ANISOTROPIC_SCALE) == 0,
            (img.getScaleX() + img.getScaleY()) / 2,
            (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0,
            img.getScaleX(), img.getScaleY(),
            (mUIMode & UI_MODE_ROTATE) != 0,
            img.getAngle()
        );

        getPositionAndScale(img, objPosAndScaleOut);

      }

      //setPos(cx, cy, sx, sy, 0.0f);
    }


    /**
     * Called by activity's onPause() method to free memory used for loading the images
     */
    public void unload() {
      this.drawable = null;
      //com.appmaker.gcmtest.localtextmain.TextMainActivity.textView.setDrawingCacheEnabled(false);
    }

    /**
     * Set the position and scale of an image in screen coordinates
     */
    public boolean setPos(PositionAndScale newImgPosAndScale) {
      return setPos(newImgPosAndScale.getXOff(), newImgPosAndScale.getYOff(), (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0 ? newImgPosAndScale
          .getScaleX() : newImgPosAndScale.getScale(), (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0 ? newImgPosAndScale.getScaleY()
          : newImgPosAndScale.getScale(), newImgPosAndScale.getAngle());
      // FIXME: anisotropic scaling jumps when axis-snapping
      // FIXME: affine-ize
      // return setPos(newImgPosAndScale.getXOff(), newImgPosAndScale.getYOff(), newImgPosAndScale.getScaleAnisotropicX(),
      // newImgPosAndScale.getScaleAnisotropicY(), 0.0f);
    }

    /**
     * Set the position and scale of an image in screen coordinates
     */
    public boolean setPos(float centerX, float centerY, float scaleX, float scaleY, float angle) {


      wsDrawable = (widthDrawable / 2) * scaleX;
      hsDrawable = (heightDrawable / 2) * scaleY;
      //float wsDrawable = (widthDrawable / 2);
      //float hsDrawable = (heightDrawable / 2);
      float newMinX = centerX - wsDrawable;
      float newMinY = centerY - hsDrawable;
      float newMaxX = centerX + wsDrawable;
      float newMaxY = centerY + hsDrawable;

      ////
      float cosq = (float) Math.cos(angle);
      float sinq = (float) Math.sin(angle);
      ////

      if (newMinX > displayWidth - SCREEN_MARGIN || newMaxX < SCREEN_MARGIN || newMinY > displayHeight - SCREEN_MARGIN
          || newMaxY < SCREEN_MARGIN)
        return false;

      this.centerX = centerX;
      this.centerY = centerY;
      this.scaleX = scaleX;
      this.scaleY = scaleY;
      this.angle = angle;

      this.minX = newMinX;
      this.minY = newMinY;
      this.maxX = newMaxX;
      this.maxY = newMaxY;
      Log.i("newMinX", "" + newMinX);
      Log.i("newMinY", "" + newMinY);
      Log.i("newMaxX", "" + newMaxX);
      Log.i("newMaxY", "" + newMaxY);

      Log.i("minX", "" + minX);
      Log.i("minY", "" + minY);
      Log.i("maxX", "" + maxX);
      Log.i("maxY", "" + maxY);

      Log.i("drawableWidth", "" + wsDrawable);
      Log.i("drawableHeight", "" + hsDrawable);


      ///////////////////////////////////////////////////

      float wsDrawableDel = (widthDrawableDel / 2);
      float hsDrawableDel = (heightDrawableDel / 2);

      float newMinXDel = minX - wsDrawableDel;
      float newMinYDel = minY - hsDrawableDel;
      float newMaxXDel = minX + wsDrawableDel;
      float newMaxYDel = minY + hsDrawableDel;

      ///////////
      float sxMinDel = newMinXDel - centerX;
      float syMinDel = newMinYDel - centerY;
      float rXMinDel = (sxMinDel * cosq - syMinDel * sinq) + centerX;
      float rYMinDel = (syMinDel * sinq + syMinDel * cosq) + centerY;

      float sxMaxDel = newMaxXDel - centerX;
      float syMaxDel = newMaxYDel - centerY;
      float rXMaxDel = (sxMaxDel * cosq - syMaxDel * sinq) + centerX;
      float rYMaxDel = (syMaxDel * sinq + syMaxDel * cosq) + centerY;
      this.rotateMINXDel = rXMinDel;
      this.rotateMINYDel = rYMinDel;
      this.rotateMAXXDel = rXMaxDel;
      this.rotateMAXYDel = rYMaxDel;
      //////////


      this.minXDel = newMinXDel;
      this.minYDel = newMinYDel;
      this.maxXDel = newMaxXDel;
      this.maxYDel = newMaxYDel;

      ///////////////////////////////////////////////////


      float wsDrawableRotate = (widthDrawableRotate / 2);
      float hsDrawableRotate = (heightDrawableRotate / 2);
      float newMinXRotate = centerX - wsDrawableRotate;
      float newMinYRotate = centerY - hsDrawableRotate;
      float newMaxXRotate = centerX + wsDrawableRotate;
      float newMaxYRotate = centerY + hsDrawableRotate;


      ///////////
      float sxMinRotate = newMinXRotate - centerX;
      float syMinRotate = newMinYRotate - centerY;
      float rXMinRotate = (sxMinRotate * cosq - syMinRotate * sinq) + centerX;
      float rYMinRotate = (syMinRotate * sinq + syMinRotate * cosq) + centerY;

      float sxMaxRotate = newMaxXRotate - centerX;
      float syMaxRotate = newMaxYRotate - centerY;
      float rXMaxRotate = (sxMaxRotate * cosq - syMaxRotate * sinq) + centerX;
      float rYMaxRotate = (syMaxRotate * sinq + syMaxRotate * cosq) + centerY;
      this.rotateMINXRotate = rXMinRotate;
      this.rotateMINYRotate = rYMinRotate;
      this.rotateMAXXRotate = rXMaxRotate;
      this.rotateMAXYRotate = rYMaxRotate;
      ///////


      this.minXRotate = newMinXRotate;
      this.minYRotate = newMinYRotate;
      this.maxXRotate = newMaxXRotate;
      this.maxYRotate = newMaxYRotate;
      ///////////////////////////////////////////////////


      ////////////
      float sxMin = newMinX - centerX;
      float syMin = newMinY - centerY;
      float rXMin = (sxMin * cosq - syMin * sinq) + centerX;
      float rYMin = (syMin * sinq + syMin * cosq) + centerY;

      float sxMax = newMaxXRotate - centerX;
      float syMax = newMaxYRotate - centerY;
      float rXMax = (sxMax * cosq - syMax * sinq) + centerX;
      float rYMax = (syMax * sinq + syMax * cosq) + centerY;

      this.rotateMINX = rXMin;
      this.rotateMINY = rYMin;
      this.rotateMAXX = rXMax;
      this.rotateMAXY = rYMax;
      ////////////

      return true;
    }

    /**
     * Return whether or not the given screen coords are inside this image
     */
    public boolean containsPoint(float scrnX, float scrnY) {
      // FIXME: need to correctly account for image rotation
      //return (scrnX >= this.rotateMINX && scrnX <= this.rotateMAXX && scrnY >= this.rotateMINY && scrnY <= this.rotateMAXY);
      return (scrnX >= minX && scrnX <= maxX && scrnY >= minY && scrnY <= maxY);
    }

    public boolean containsPointDel(float scrnX, float scrnY) {
      // FIXME: need to correctly account for image rotation
      return (scrnX >= this.rotateMINXDel && scrnX <= this.rotateMAXXDel && scrnY >= this.rotateMINYDel && scrnY <= this.rotateMAXYDel);
    }

    public boolean containsPointScale(float scrnX, float scrnY) {
      // FIXME: need to correctly account for image rotation
      return (scrnX >= this.rotateMINXRotate && scrnX <= this.rotateMAXXRotate && scrnY >= this.rotateMINYRotate && scrnY <= this.rotateMAXYRotate);
    }


    public void draw(Canvas canvas, int n, View view) {

      canvas.save();

      float dx = (maxX + minX) / 2;
      float dy = (maxY + minY) / 2;

      drawable.setBounds((int) minX, (int) minY, (int) maxX, (int) maxY);
      drawableGuideLine.setBounds((int) minX, (int) minY, (int) maxX, (int) maxY);
      drawableGuideLineRotate.setBounds((int) minXRotate, (int) minYRotate, (int) maxXRotate, (int) maxYRotate);
      drawableGuideLineDel.setBounds((int) minXDel, (int) minYDel, (int) maxXDel, (int) maxYDel);
      Log.i("angle is ", "" + angle);


      canvas.translate(dx, dy);
      canvas.rotate(angle * 180.0f / (float) Math.PI);
      canvas.translate(-dx, -dy);

      drawable.draw(canvas);
      drawableGuideLine.draw(canvas);
      drawableGuideLineRotate.draw(canvas);
      drawableGuideLineDel.draw(canvas);

      scaledWidthDrawable = this.widthDrawable * this.scaleX;
      scaledHeightDrawable = this.heightDrawable * this.scaleY;
						
			/*
			Bitmap drawableBitmap = ((BitmapDrawable)this.drawable).getBitmap();
			drawableBitmap = Bitmap.createScaledBitmap(
					drawableBitmap, (int)scaledWidthDrawable, (int)scaledHeightDrawable, true);
			*/

      forMaskBitmap = ((BitmapDrawable) this.drawableMask).getBitmap();
      forMaskBitmap = Bitmap.createScaledBitmap(
          forMaskBitmap, (int) scaledWidthDrawable, (int) scaledHeightDrawable, true);

      float minX2 = this.minX
          - (cropStickerActivity.cropImageView.getMeasuredWidth() / 2
          - (cropStickerActivity.toAdjustBitmap.getWidth() / 2));
      float minY2 = this.minY
          - (cropStickerActivity.cropImageView.getMeasuredHeight() / 2
          - (cropStickerActivity.toAdjustBitmap.getHeight() / 2));
			
			/*
			if(minX2 > com.appmaker.gcmtest.localstikermain.cropStickerActivity.toAdjustBitmap.getWidth()) {
				//scaledWidthDrawable = scaledWidthDrawable - minX;
				minX2 = com.appmaker.gcmtest.localstikermain.cropStickerActivity.toAdjustBitmap.getWidth();
			}
			
			if(minX2 < 0) {
				
				//this.isSelectStikerSpace = false;
				
				scaledWidthDrawable = scaledWidthDrawable + minX2;
				
				Log.i("scaledWidthDrawable is ", String.valueOf(scaledWidthDrawable));
				Log.i("scaledHeightDrawable is ", String.valueOf(scaledHeightDrawable));
				
				
				
				drawableBitmap = Bitmap.createBitmap(
						drawableBitmap,
						(int)-(minX2), //부호반전
						(int)0,
						(int)scaledWidthDrawable ,
						(int)scaledHeightDrawable);
				
				forMaskBitmap = Bitmap.createBitmap(
						forMaskBitmap,
						(int)-(minX2), //부호반전
						(int)0,
						(int)scaledWidthDrawable ,
						(int)scaledHeightDrawable);
				
				
				minX2 = 0;
				
				/*
				Matrix matrix = new Matrix();
				matrix.setTranslate(-mShip.getCurrentBitmap().getWidth()/2f, -mShip.getCurrentBitmap().getHeight()/2f);
				matrix.postTranslate(mShip.getX(), mShip.getY());
				matrix.postScale((1.0f * mShip.getWidth() / mShip.getCurrentBitmap().getWidth()), (1.0f * mShip.getHeight() / mShip.getCurrentBitmap().getHeight()), mShip.getX(), mShip.getY());
				
				toAdjustBitmap = Bitmap.createbitmap(toAdjustBitmap, x, y, width, height, matrix, true); 
				*/
						
			
			/*
			if(this.minY < (com.appmaker.gcmtest.localstikermain.cropStickerActivity.cropImageView.getMeasuredHeight()/2
					- (com.appmaker.gcmtest.localstikermain.cropStickerActivity.toAdjustBitmap.getHeight()/2))) {
				//scaledHeightDrawable = scaledHeightDrawable - minY;
				minY2 = (com.appmaker.gcmtest.localstikermain.cropStickerActivity.cropImageView.getMeasuredHeight()/2
						- (com.appmaker.gcmtest.localstikermain.cropStickerActivity.toAdjustBitmap.getHeight()/2));
			}
			*/
			
			/*
			toAdjustBitmap = Bitmap.createBitmap(
					com.appmaker.gcmtest.localstikermain.cropStickerActivity.toAdjustBitmap,
					(int)minX2,
					(int)minY2,
					(int)scaledWidthDrawable,
					(int)scaledHeightDrawable);
			toAdjustBitmap = Bitmap.createScaledBitmap(
					toAdjustBitmap, (int)scaledWidthDrawable, (int)scaledHeightDrawable, true);
			*/
			/*
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
			canvas.drawBitmap(toAdjustBitmap, this.minX, this.minY, null);
			canvas.drawBitmap(forMaskBitmap, this.minX, this.minY, paint);
			paint.setXfermode(null);
			*/

      final float left = mImgArryList.get(0).getMinX();
      final float top = mImgArryList.get(0).getMinY();
      final float right = mImgArryList.get(0).getMaxX();
      final float bottom = mImgArryList.get(0).getMaxY();

      mBitmapRect = ImageViewUtil.getBitmapRectCenterInside(
          cropStickerActivity.toAdjustBitmapScaleUP.getWidth(),
          cropStickerActivity.toAdjustBitmapScaleUP.getHeight(),
          cropStickerActivity.cropImageView.getMeasuredWidth(),
          cropStickerActivity.cropImageView.getMeasuredHeight());
			
	        /*-
	          -------------------------------------
	          |                top                |
	          -------------------------------------
	          |      |                    |       |
	          |      |                    |       |
	          | left |                    | right |
	          |      |                    |       |
	          |      |                    |       |
	          -------------------------------------
	          |              bottom               |
	          -------------------------------------
	         */
      // Draw "top", "bottom", "left", then "right" quadrants.
      canvas.drawRect(-(mBitmapRect.right * 2), -(mBitmapRect.top * 2) , (mBitmapRect.right *2), top, mBackgroundPaint);
      //canvas.drawRect(-(mBitmapRect.left * 2), -(mBitmapRect.top * 2) , (mBitmapRect.right *2), top, mBackgroundPaint);
      canvas.drawRect(-(mBitmapRect.right * 2), bottom, mBitmapRect.right * 2, (mBitmapRect.bottom *2), mBackgroundPaint);
      //canvas.drawRect(-(mBitmapRect.left * 2), bottom, mBitmapRect.right * 2, (mBitmapRect.bottom *2), mBackgroundPaint);
      canvas.drawRect(-(mBitmapRect.right * 2), top, left, bottom, mBackgroundPaint);
      //canvas.drawRect(-(mBitmapRect.left * 2), top, left, bottom, mBackgroundPaint);
      canvas.drawRect(right, top, (mBitmapRect.right * 2), bottom, mBackgroundPaint);

      canvas.restore();

			
			/*
			 * 터치시 스티커의 좌상단, 우하단 점을 원으로 표시한다.
			 */
			/*
			canvas.drawCircle(rotateMINX, rotateMINY, 40, mLinePaintTouchPointCircle);
			canvas.drawCircle(rotateMAXX, rotateMAXY, 40, mLinePaintTouchPointCircle);
			
			canvas.drawCircle(rotateMINXDel, rotateMINYDel, 20, mLinePaintTouchPointCircle);
			canvas.drawCircle(rotateMAXXDel, rotateMAXYDel, 20, mLinePaintTouchPointCircle);
			
			canvas.drawCircle(rotateMINXRotate, rotateMINYRotate, 20, mLinePaintTouchPointCircle);
			canvas.drawCircle(rotateMAXXRotate, rotateMAXYRotate, 20, mLinePaintTouchPointCircle);
			*/
    }

    // Private Methods /////////////////////////////////////////////////////////

    private void init(Context context) {

      //DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
      mBackgroundPaint = PaintUtil.newBackgroundPaint(context);
	        
	        /*
	        mHandleRadius = HandleUtil.getTargetRadius(context);
	        mSnapRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
	                                                SNAP_RADIUS_DP,

	        mBorderPaint = PaintUtil.newBorderPaint(context);
	        mGuidelinePaint = PaintUtil.newGuidelinePaint();
	        mCornerPaint = PaintUtil.newCornerPaint(context);

	        // Sets the values for the corner sizes
	        mCornerOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
	                                                  DEFAULT_CORNER_OFFSET_DP,
	                                                  displayMetrics);
	        mCornerExtension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
	                                                     DEFAULT_CORNER_EXTENSION_DP,
	                                                     displayMetrics);
	        mCornerLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
	                                                  DEFAULT_CORNER_LENGTH_DP,
	                                                  displayMetrics);

	        // Sets guidelines to default until specified otherwise
	        mGuidelines = CropImageView.DEFAULT_GUIDELINES;
	        */
    }

    public int getResId() {
      return resId;
    }

    public Drawable getDrawable() {
      return drawable;
    }

    public Drawable getDrawableMask() {
      return drawableMask;
    }

    public Drawable getDrawableGuideLine() {
      return drawableGuideLine;
    }

    public int getWidth() {
      return widthDrawable;
    }

    public int getHeight() {
      return heightDrawable;
    }

    public int getWidthAfterTouch() {
      return (int) ((widthDrawable / 2) * scaleX);
    }

    public int getHeightAfterTouch() {
      return (int) ((heightDrawable / 2) * scaleY);
    }

    public float getCenterX() {
      return centerX;
    }

    public void setCenterX(float varCenterX) {
      centerX = varCenterX;
    }

    public void setCenterY(float varCenterY) {
      centerY = varCenterY;
    }

    public float getCenterY() {
      return centerY;
    }

    public float getScaleX() {
      return scaleX;
    }

    public float getScaleY() {
      return scaleY;
    }

    public float getAngle() {
      return angle;
    }

    // FIXME: these need to be updated for rotation
    public float getMinX() {
      return minX;
    }

    public float getMaxX() {
      return maxX;
    }

    public float getMinY() {
      return minY;
    }

    public float getMaxY() {
      return maxY;
    }

  }


}

