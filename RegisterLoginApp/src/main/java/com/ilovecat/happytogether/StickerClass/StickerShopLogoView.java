package com.ilovecat.happytogether.StickerClass;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ilovecat.happytogether.MainShopRegisterCoupon.MainRegisterCouponShopStep1;
import com.ilovecat.happytogether.Painter.PainterActivity;
import com.ilovecat.happytogether.R;


public abstract class StickerShopLogoView extends FrameLayout {


  public static final String TAG = "com.knef.stickerView";
  private BorderView iv_border;
  private ImageView iv_scale;
  private ImageView iv_delete;
  private ImageView iv_flip;
  private ImageView iv_removebg;


  // For scalling
  private float this_orgX = -1, this_orgY = -1;
  private float scale_orgX = -1, scale_orgY = -1;
  private double scale_orgWidth = -1, scale_orgHeight = -1;
  // For rotating
  private float rotate_orgX = -1, rotate_orgY = -1, rotate_newX = -1, rotate_newY = -1;

  private float move_orgX = -1, move_orgY = -1;
  // For moving
  private float objWidth_ForTempSave = -1, objHeight_ForTempSave = -1;
  private double centerX, centerY;

  private final static int BUTTON_SIZE_DP = 30;

  private int SELF_SIZE_DP_WIDTH = MainRegisterCouponShopStep1.ShopLogoWidth;
  private int SELF_SIZE_DP_HEIGHT = MainRegisterCouponShopStep1.ShopLogoHeight;
  //private int SELF_SIZE_DP_WIDTH = 130;
  //private int SELF_SIZE_DP_HEIGHT= 130;


  private String pathAndFileName;
  private String FileName;


  public StickerShopLogoView(Context context) {
    super(context);
    init(context);
  }

  public StickerShopLogoView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public StickerShopLogoView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  private void init(final Context context) {
    this.iv_border = new BorderView(context);
    this.iv_scale = new ImageView(context);
    this.iv_delete = new ImageView(context);
    this.iv_flip = new ImageView(context);
    this.iv_removebg = new ImageView(context);

    this.iv_scale.setImageResource(R.drawable.sticker_zoominout);
    this.iv_delete.setImageResource(R.drawable.sticker_remove);
    this.iv_flip.setImageResource(R.drawable.sticker_flip);
    this.iv_removebg.setImageResource(R.drawable.ic_action_remove_background);

    this.setTag("DraggableViewGroup");
    this.iv_border.setTag("iv_border");
    this.iv_scale.setTag("iv_scale");
    this.iv_delete.setTag("iv_delete");
    this.iv_flip.setTag("iv_flip");
    this.iv_removebg.setTag("iv_removebg");

    int margin = convertDpToPixel(BUTTON_SIZE_DP, getContext()) / 2;

    int size_width = convertDpToPixel(SELF_SIZE_DP_WIDTH, getContext());
    int size_height = convertDpToPixel(SELF_SIZE_DP_HEIGHT, getContext());

    LayoutParams this_params =
        new LayoutParams(
            size_width,
            size_height
        );
    this_params.gravity = Gravity.LEFT | Gravity.TOP;
    this_params.setMargins(convertDpToPixel(MainRegisterCouponShopStep1.ShopLogoBoxLeftMargin, context), margin, margin, margin);


    LayoutParams iv_main_params =
        new LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );

    iv_main_params.setMargins(margin, margin, margin, margin);

    LayoutParams iv_border_params =
        new LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );
    iv_border_params.setMargins(margin, margin, margin, margin);

    LayoutParams iv_scale_params =
        new LayoutParams(
            convertDpToPixel(BUTTON_SIZE_DP, getContext()),
            convertDpToPixel(BUTTON_SIZE_DP, getContext())
        );
    iv_scale_params.gravity = Gravity.BOTTOM | Gravity.RIGHT;

    LayoutParams iv_delete_params =
        new LayoutParams(
            convertDpToPixel(BUTTON_SIZE_DP, getContext()),
            convertDpToPixel(BUTTON_SIZE_DP, getContext())
        );
    iv_delete_params.gravity = Gravity.TOP | Gravity.RIGHT;

    LayoutParams iv_flip_params =
        new LayoutParams(
            convertDpToPixel(BUTTON_SIZE_DP, getContext()),
            convertDpToPixel(BUTTON_SIZE_DP, getContext())
        );
    iv_flip_params.gravity = Gravity.TOP | Gravity.LEFT;


    LayoutParams iv_removebg_params =
        new LayoutParams(
            convertDpToPixel(BUTTON_SIZE_DP, getContext()),
            convertDpToPixel(BUTTON_SIZE_DP, getContext())
        );
    iv_removebg_params.gravity = Gravity.BOTTOM | Gravity.CENTER;


    this.setLayoutParams(this_params);
    this.addView(getMainView(), iv_main_params);
    this.addView(iv_border, iv_border_params);
    this.addView(iv_scale, iv_scale_params);
    this.addView(iv_delete, iv_delete_params);
    this.addView(iv_flip, iv_flip_params);
    this.addView(iv_removebg, iv_removebg_params);
    //this.setControlItemsHidden(true);
    this.setOnTouchListener(mTouchListener);
    this.iv_scale.setOnTouchListener(mTouchListener);
    this.iv_delete.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {



        if (StickerShopLogoView.this.getParent() != null) {
          ViewGroup myCanvas = ((ViewGroup) StickerShopLogoView.this.getParent());
          myCanvas.removeView(StickerShopLogoView.this);

          // ######################################################################################
          // 매우중요 16/8/25
          // 어래이리스트에서 해당 스티커를 제거한다.
          MainRegisterCouponShopStep1.arrayListStickerShopLogoView.remove(StickerShopLogoView.this);
          // ######################################################################################

        }
      }
    });

    this.iv_flip.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View view) {
        Log.v(TAG, "flip the view");

        View mainView = getMainView();
        mainView.setRotationY(mainView.getRotationY() == -180f ? 0f : -180f);
        mainView.invalidate();
        requestLayout();
      }
    });


    this.iv_removebg.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View view) {
        Log.v(TAG, "go removebg");

        final String fn = getFileName();

        Log.i("dsds", fn);

        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            Intent intent = new Intent(MainRegisterCouponShopStep1.context, PainterActivity.class);
            // Close all views before launching
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("CROPEDFILENAME", fn);
            MainRegisterCouponShopStep1.context.startActivity(intent);
            // 액티비비티 에니메이션
            //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            //Intent intent = new Intent();
            //intent.setClass(MainRegisterCouponShopStep1.context, PainterActivity.class);
            //MainRegisterCouponShopStep1.context.startActivity(intent);
          }
        }, 0);
      }
    });
  }


  public boolean isFlip() {
    return getMainView().getRotationY() == -180f;
  }

  protected abstract View getMainView();

  private OnTouchListener mTouchListener = new OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent event) {

      if (view.getTag().equals("DraggableViewGroup")) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            Log.v(TAG, "sticker view action down");

            int sizeOfMessageArray = MainRegisterCouponShopStep1.arrayListStickerMessageView.size();
            for (int i = 0; i < sizeOfMessageArray; i++) {
              MainRegisterCouponShopStep1.arrayListStickerMessageView.get(i).setControlItemsHidden(true);
            }


            int sizeOfShopLogoArray = MainRegisterCouponShopStep1.arrayListStickerShopLogoView.size();
            for (int i = 0; i < sizeOfShopLogoArray; i++) {
              MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(i).setControlItemsHidden(true);
            }

            StickerShopLogoView.this.setControlItemsHidden(false);

            StickerShopLogoView.this.bringToFront();
            //StickerViewShopLogo.bringToFront();
            StickerShopLogoView.this.invalidate();
            requestLayout();

            move_orgX = event.getRawX();
            move_orgY = event.getRawY();


            // ###################################
            // 매우중요 16/8/25
            // 선택한 스티커를 어래이리스트 맨마지막으로 할당한다.
            StickerShopLogoView tempStickerShopLogoView = StickerShopLogoView.this;
            MainRegisterCouponShopStep1.arrayListStickerShopLogoView.remove(StickerShopLogoView.this);
            MainRegisterCouponShopStep1.arrayListStickerShopLogoView.add(tempStickerShopLogoView);
            // ###################################



            break;

          case MotionEvent.ACTION_MOVE:
            Log.v(TAG, "sticker view action move");
            float offsetX = event.getRawX() - move_orgX;
            float offsetY = event.getRawY() - move_orgY;
            StickerShopLogoView.this.setX(StickerShopLogoView.this.getX() + offsetX);
            StickerShopLogoView.this.setY(StickerShopLogoView.this.getY() + offsetY);
            move_orgX = event.getRawX();
            move_orgY = event.getRawY();

            // Info
            Log.i("W", String.valueOf(objWidth_ForTempSave));
            Log.i("D", String.valueOf(objHeight_ForTempSave));
            MainRegisterCouponShopStep1.x.setText(String.valueOf(StickerShopLogoView.this.getX()));
            MainRegisterCouponShopStep1.y.setText(String.valueOf(StickerShopLogoView.this.getY()));
            MainRegisterCouponShopStep1.drawablePath.setText(StickerShopLogoView.this.getPathAndFileName());

            MainRegisterCouponShopStep1.w.setText(String.valueOf(objWidth_ForTempSave));
            MainRegisterCouponShopStep1.h.setText(String.valueOf(objHeight_ForTempSave));

            break;

          case MotionEvent.ACTION_UP:
            Log.v(TAG, "sticker view action up");

            break;

          case MotionEvent.ACTION_OUTSIDE:
            Log.v(TAG, "sticker view action outside");

            StickerShopLogoView.this.setControlItemsHidden(true);


            break;

        }
      } else if (view.getTag().equals("iv_scale")) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            Log.v(TAG, "iv_scale action down");

            this_orgX = StickerShopLogoView.this.getX();
            this_orgY = StickerShopLogoView.this.getY();

            scale_orgX = event.getRawX();
            scale_orgY = event.getRawY();
            scale_orgWidth = StickerShopLogoView.this.getLayoutParams().width;
            scale_orgHeight = StickerShopLogoView.this.getLayoutParams().height;

            rotate_orgX = event.getRawX();
            rotate_orgY = event.getRawY();

            centerX = StickerShopLogoView.this.getX() +
                ((View) StickerShopLogoView.this.getParent()).getX() +
                (float) StickerShopLogoView.this.getWidth() / 2;


            TypedValue tv = new TypedValue();
            MainRegisterCouponShopStep1.context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
            int actionBarHeight = getResources().getDimensionPixelSize(tv.resourceId);


            //double statusBarHeight = Math.ceil(25 * getContext().getResources().getDisplayMetrics().density);
            int result = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
              result = getResources().getDimensionPixelSize(resourceId);
            }
            double statusBarHeight = result;
            centerY = StickerShopLogoView.this.getY() +
                ((View) StickerShopLogoView.this.getParent()).getY() +
                statusBarHeight +
                actionBarHeight +
                (float) StickerShopLogoView.this.getHeight() / 2;

            break;
          case MotionEvent.ACTION_MOVE:
            Log.v(TAG, "iv_scale action move");

            rotate_newX = event.getRawX();
            rotate_newY = event.getRawY();

            double angle_diff = Math.abs(
                Math.atan2(event.getRawY() - scale_orgY, event.getRawX() - scale_orgX)
                    - Math.atan2(scale_orgY - centerY, scale_orgX - centerX)) * 180 / Math.PI;

            Log.v(TAG, "angle_diff: " + angle_diff);

            double length1 = getLength(centerX, centerY, scale_orgX, scale_orgY);
            double length2 = getLength(centerX, centerY, event.getRawX(), event.getRawY());

            int sizeWidth = convertDpToPixel(SELF_SIZE_DP_WIDTH, getContext());
            int sizeHeight = convertDpToPixel(SELF_SIZE_DP_HEIGHT, getContext());

            if (length2 > length1
                && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                ) {
              //scale up

              /*
              double offsetX = Math.abs(event.getRawX() - scale_orgX);
              double offsetY = Math.abs(event.getRawY() - scale_orgY);
              double offset = Math.max(offsetX, offsetY);
              offset = Math.round(offset);
              */
              double ratio = Math.abs(event.getRawX() / scale_orgX);
              double ratioWidth = StickerShopLogoView.this.getLayoutParams().width * ratio;
              ratioWidth = Math.round(ratioWidth);
              double ratioHeight = StickerShopLogoView.this.getLayoutParams().height * ratio;
              ratioHeight = Math.round(ratioHeight);

              StickerShopLogoView.this.getLayoutParams().width = (int) ratioWidth;
              StickerShopLogoView.this.getLayoutParams().height = (int) ratioHeight;


              objWidth_ForTempSave = (float)convertPixelToDp((float)ratioWidth,getContext());
              objHeight_ForTempSave = (float)convertPixelToDp((float)ratioHeight,getContext());


              onScaling(true);
              //DraggableViewGroup.this.setX((float) (getX() - offset / 2));
              //DraggableViewGroup.this.setY((float) (getY() - offset / 2));
            } else if (length2 < length1
                && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                && StickerShopLogoView.this.getLayoutParams().width > sizeWidth / 2
                && StickerShopLogoView.this.getLayoutParams().height > sizeHeight / 2) {
                            /*
                            double offsetX = Math.abs(event.getRawX() - scale_orgX);
                            double offsetY = Math.abs(event.getRawY() - scale_orgY);
                            double offset = Math.max(offsetX, offsetY);
                            offset = Math.round(offset);
                            */

              double ratio = Math.abs(event.getRawX() / scale_orgX);
              double ratioWidth = StickerShopLogoView.this.getLayoutParams().width * ratio;
              ratioWidth = Math.round(ratioWidth);
              double ratioHeight = StickerShopLogoView.this.getLayoutParams().height * ratio;
              ratioHeight = Math.round(ratioHeight);


              StickerShopLogoView.this.getLayoutParams().width = (int) ratioWidth;
              StickerShopLogoView.this.getLayoutParams().height = (int) ratioHeight;


              objWidth_ForTempSave = (float)convertPixelToDp((float)ratioWidth,getContext());
              objHeight_ForTempSave = (float)convertPixelToDp((float)ratioHeight,getContext());

              onScaling(false);
            }

            //rotate
            /*
            double angle = Math.atan2(event.getRawY() - centerY, event.getRawX() - centerX) * 180 / Math.PI;
            Log.v(TAG, "log angle: " + angle);

            //setRotation((float) angle - 45);
            setRotation((float) angle - 45);
            Log.v(TAG, "getRotation(): " + getRotation());

            onRotating();

            rotate_orgX = rotate_newX;
            rotate_orgY = rotate_newY;
            */

            scale_orgX = event.getRawX();
            scale_orgY = event.getRawY();

            // TempSaveXML
            //objWidth_ForTempSave = StickerShopLogoView.this.getLayoutParams().width;
            //objHeight_ForTempSave = StickerShopLogoView.this.getLayoutParams().height;

            // Info
            MainRegisterCouponShopStep1.w.setText(String.valueOf(objWidth_ForTempSave));
            MainRegisterCouponShopStep1.h.setText(String.valueOf(objHeight_ForTempSave));

            postInvalidate();
            requestLayout();
            break;

          case MotionEvent.ACTION_UP:
            Log.v(TAG, "iv_scale action up");
            break;
        }
      }
      return true;
    }
  };

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
  }

  private double getLength(double x1, double y1, double x2, double y2) {
    return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
  }

  private float[] getRelativePos(float absX, float absY) {
    Log.v("ken", "getRelativePos getX:" + ((View) this.getParent()).getX());
    Log.v("ken", "getRelativePos getY:" + ((View) this.getParent()).getY());
    float[] pos = new float[]{
        absX - ((View) this.getParent()).getX(),
        absY - ((View) this.getParent()).getY()
    };
    Log.v(TAG, "getRelativePos absY:" + absY);
    Log.v(TAG, "getRelativePos relativeY:" + pos[1]);
    return pos;
  }

  public void setControlItemsHidden(boolean isHidden) {
    if (isHidden) {
      iv_border.setVisibility(View.INVISIBLE);
      iv_scale.setVisibility(View.INVISIBLE);
      iv_delete.setVisibility(View.INVISIBLE);
      iv_flip.setVisibility(View.INVISIBLE);
      iv_removebg.setVisibility(View.INVISIBLE);
    } else {
      iv_border.setVisibility(View.VISIBLE);
      iv_scale.setVisibility(View.VISIBLE);
      iv_delete.setVisibility(View.VISIBLE);
      iv_flip.setVisibility(View.VISIBLE);
      iv_removebg.setVisibility(View.VISIBLE);
    }
  }

  public void setControlItemsMoveLock(boolean isMoveLock) {
    if (isMoveLock) {


      this.setTag("NonDraggableViewGroup");

    } else {

      this.setTag("DraggableViewGroup");
    }
  }

  public void setPathAndFileName(String fileName) {
    this.pathAndFileName = fileName;
  }
  public String getPathAndFileName() {

    return pathAndFileName;
  }


  public void setFileName(String fileName) {
    this.FileName = fileName;
  }
  private String getFileName() {

    return FileName;
  }


  protected View getImageViewFlip() {
    return iv_flip;
  }

  protected void onScaling(boolean scaleUp) {
  }

  protected void onRotating() {
  }

  private class BorderView extends View {

    public BorderView(Context context) {
      super(context);
    }

    public BorderView(Context context, AttributeSet attrs) {
      super(context, attrs);
    }

    public BorderView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
      super.onDraw(canvas);
      // Draw sticker border

      LayoutParams params = (LayoutParams) this.getLayoutParams();

      Log.v(TAG, "params.leftMargin: " + params.leftMargin);

      Rect border = new Rect();
      border.left = (int) this.getLeft() - params.leftMargin;
      border.top = (int) this.getTop() - params.topMargin;
      border.right = (int) this.getRight() - params.rightMargin;
      border.bottom = (int) this.getBottom() - params.bottomMargin;
      Paint borderPaint = new Paint();
      borderPaint.setStrokeWidth(6);
      borderPaint.setColor(Color.LTGRAY);
      borderPaint.setStyle(Paint.Style.STROKE);


      borderPaint.setAntiAlias(true);
      borderPaint.setDither(true);
      borderPaint.setPathEffect(new DashPathEffect(new float[]{5, 5,}, 0));
      borderPaint.setStrokeWidth(6);


      canvas.drawRect(border, borderPaint);

    }
  }

  private static int convertDpToPixel(float dp, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float px = dp * (metrics.densityDpi / 160f);
    return (int) px;
  }

  // TempSave
  public float getMoveOrgX() {
    return move_orgX;
  }
  public void setMoveOrgX(float x) {
    this.move_orgX = x;
  }


  public float getMoveOrgY() {
    return move_orgY;
  }
  public void setMoveOrgY(float y) {
    this.move_orgY = y;
  }


  public float getObjWidth() {
    return objWidth_ForTempSave;
  }
  public void setObjWidth(float w) {
    this.objWidth_ForTempSave = w;
  }


  public float getObjHeight() {
    return objHeight_ForTempSave;
  }
  public void setObjHeight(float h) {
    this.objHeight_ForTempSave = h;
  }

  private static int convertPixelToDp(float pixel, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float dp = pixel / (metrics.densityDpi / 160f);
    return (int) dp;
  }




}
