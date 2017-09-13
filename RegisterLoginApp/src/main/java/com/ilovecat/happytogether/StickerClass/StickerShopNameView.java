package com.ilovecat.happytogether.StickerClass;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ilovecat.happytogether.MainShopRegisterCoupon.MainRegisterCouponShopStep1;
import com.ilovecat.happytogether.R;


public abstract class StickerShopNameView extends FrameLayout {

  public static final String TAG = "com.knef.stickerView";
  private BorderView iv_border;
  private ImageView iv_scale;
  private ImageView iv_delete;
  private ImageView iv_flip;

  public ImageView iv_fontformat;
  public ImageView iv_fontsize;
  public ImageView iv_fontcolor;


  // For scalling
  private float this_orgX = -1, this_orgY = -1;
  private float scale_orgX = -1, scale_orgY = -1;
  private double scale_orgWidth = -1, scale_orgHeight = -1;
  // For rotating
  private float rotate_orgX = -1, rotate_orgY = -1, rotate_newX = -1, rotate_newY = -1;
  // For moving
  private float move_orgX = -1, move_orgY = -1;

  private double centerX, centerY;

  private final static int BUTTON_SIZE_DP = 30;
  private final static int SELF_SIZE_DP = 100;





  private static int SELF_SIZE_DP_WIDTH = 200;
  private static int SELF_SIZE_DP_HEIGHT = 50;



  public static int w;
  public static int h;


  public StickerShopNameView(Context context) {
    super(context);

    init(context, w, h);
  }

  public StickerShopNameView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, w, h);
  }

  public StickerShopNameView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, w, h);
  }

  private void init(final Context context, int width, int height) {


    this.iv_border = new BorderView(context);
    this.iv_scale = new ImageView(context);
    this.iv_delete = new ImageView(context);
    this.iv_flip = new ImageView(context);
    this.iv_fontformat = new ImageView(context);
    this.iv_fontsize = new ImageView(context);
    this.iv_fontcolor = new ImageView(context);

    this.iv_scale.setImageResource(R.drawable.sticker_zoominout);
    this.iv_delete.setImageResource(R.drawable.sticker_remove);
    this.iv_flip.setImageResource(R.drawable.sticker_flip);
    this.iv_fontformat.setImageResource(R.drawable.sticker_fontformat);
    this.iv_fontsize.setImageResource(R.drawable.sticker_fontsize);
    this.iv_fontcolor.setImageResource(R.drawable.sticker_fontcolor);

    this.setTag("DraggableViewGroup");
    this.iv_border.setTag("iv_border");
    this.iv_scale.setTag("iv_scale");
    this.iv_delete.setTag("iv_delete");
    this.iv_flip.setTag("iv_flip");
    this.iv_fontformat.setTag("iv_fontformat");
    this.iv_fontsize.setTag("iv_fontsize");
    this.iv_fontcolor.setTag("iv_fontcolor");

    int margin = convertDpToPixel(BUTTON_SIZE_DP, getContext()) / 2;
    int size = convertDpToPixel(SELF_SIZE_DP, getContext());



    DisplayMetrics metrics = this.getResources().getDisplayMetrics();
    int widthD = metrics.widthPixels;


    int size_width =  convertDpToPixel(SELF_SIZE_DP_WIDTH, getContext());
    int size_height = convertDpToPixel(SELF_SIZE_DP_HEIGHT, getContext());

    LayoutParams this_params =
        new LayoutParams(
            size_width,
            size_height
        );
    this_params.gravity = Gravity.TOP | Gravity.RIGHT;
    this_params.setMargins(
        margin
        ,convertDpToPixel(MainRegisterCouponShopStep1.ShopNameBoxTopMargin,context)
        ,convertDpToPixel(MainRegisterCouponShopStep1.ShopNameBoxRightMargin,context)
        ,margin);


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


    LayoutParams iv_fontformat_params =
        new LayoutParams(
            convertDpToPixel(BUTTON_SIZE_DP, getContext()),
            convertDpToPixel(BUTTON_SIZE_DP, getContext())
        );

    iv_fontformat_params.gravity = Gravity.BOTTOM | Gravity.CENTER;
    iv_fontformat_params.setMargins(-convertDpToPixel(35, context), 0, 0, 0);


    LayoutParams iv_fontsize_params =
        new LayoutParams(
            convertDpToPixel(BUTTON_SIZE_DP, getContext()),
            convertDpToPixel(BUTTON_SIZE_DP, getContext())
        );

    iv_fontsize_params.gravity = Gravity.BOTTOM | Gravity.CENTER;


    LayoutParams iv_fontcolor_params =
        new LayoutParams(
            convertDpToPixel(BUTTON_SIZE_DP, getContext()),
            convertDpToPixel(BUTTON_SIZE_DP, getContext())
        );

    iv_fontcolor_params.gravity = Gravity.BOTTOM | Gravity.CENTER;
    iv_fontcolor_params.setMargins(convertDpToPixel(35, context), 0, 0, 0);

    this.setLayoutParams(this_params);
    this.addView(getMainView(), iv_main_params);
    this.addView(iv_border, iv_border_params);
    this.addView(iv_scale, iv_scale_params);
    this.addView(iv_delete, iv_delete_params);
    this.addView(iv_flip, iv_flip_params);
    this.addView(iv_fontformat, iv_fontformat_params);
    this.addView(iv_fontsize, iv_fontsize_params);
    this.addView(iv_fontcolor, iv_fontcolor_params);
    //this.setControlItemsHidden(true);
    this.setOnTouchListener(mTouchListener);
    this.iv_scale.setOnTouchListener(mTouchListener);
    this.iv_delete.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (StickerShopNameView.this.getParent() != null) {
          ViewGroup myCanvas = ((ViewGroup) StickerShopNameView.this.getParent());
          myCanvas.removeView(StickerShopNameView.this);
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



    /*
    this.iv_fontsize.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View view) {
        Log.v(TAG, "flip the view");

        ((MainRegisterCouponShopStep1)mContext).ShowFontSizeDialog();

      }
    });


  this.iv_fontcolor.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View view) {
        Log.v(TAG, "flip the view");

        ((MainRegisterCouponShopStep1)mContext).getColor(view);

      }
    });
  */

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


           // setControlItemsHidden(false);


            StickerShopNameView.this.bringToFront();
            StickerShopNameView.this.invalidate();
            requestLayout();


            move_orgX = event.getRawX();
            move_orgY = event.getRawY();
          break;

          case MotionEvent.ACTION_MOVE:
            Log.v(TAG, "sticker view action move");
            float offsetX = event.getRawX() - move_orgX;
            float offsetY = event.getRawY() - move_orgY;
            StickerShopNameView.this.setX(StickerShopNameView.this.getX() + offsetX);
            StickerShopNameView.this.setY(StickerShopNameView.this.getY() + offsetY);
            move_orgX = event.getRawX();
            move_orgY = event.getRawY();


            Log.i("X",String.valueOf(StickerShopNameView.this.getX()));
            Log.i("Y",String.valueOf(StickerShopNameView.this.getY()));
            getRelativePos(StickerShopNameView.this.getX(), StickerShopNameView.this.getY());
            break;

          case MotionEvent.ACTION_UP:


            Log.v(TAG, "sticker view action up");


            break;



        }
      } else if (view.getTag().equals("iv_scale")) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            Log.v(TAG, "iv_scale action down");

            this_orgX = StickerShopNameView.this.getX();
            this_orgY = StickerShopNameView.this.getY();

            scale_orgX = event.getRawX();
            scale_orgY = event.getRawY();
            scale_orgWidth = StickerShopNameView.this.getLayoutParams().width;
            scale_orgHeight = StickerShopNameView.this.getLayoutParams().height;

            rotate_orgX = event.getRawX();
            rotate_orgY = event.getRawY();

            centerX = StickerShopNameView.this.getX() +
                ((View) StickerShopNameView.this.getParent()).getX() +
                (float) StickerShopNameView.this.getWidth() / 2;


            //double statusBarHeight = Math.ceil(25 * getContext().getResources().getDisplayMetrics().density);
            int result = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
              result = getResources().getDimensionPixelSize(resourceId);
            }
            double statusBarHeight = result;
            centerY = StickerShopNameView.this.getY() +
                ((View) StickerShopNameView.this.getParent()).getY() +
                statusBarHeight +
                (float) StickerShopNameView.this.getHeight() / 2;

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

            int size = convertDpToPixel(SELF_SIZE_DP, getContext());
            if (length2 > length1
                && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                ) {
              //scale up
              double offsetX = Math.abs(event.getRawX() - scale_orgX);
              double offsetY = Math.abs(event.getRawY() - scale_orgY);
              double offset = Math.max(offsetX, offsetY);
              offset = Math.round(offset);
              StickerShopNameView.this.getLayoutParams().width += offset;
              StickerShopNameView.this.getLayoutParams().height += offset;
              onScaling(true);
              //DraggableViewGroup.this.setX((float) (getX() - offset / 2));
              //DraggableViewGroup.this.setY((float) (getY() - offset / 2));
            } else if (length2 < length1
                && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                && StickerShopNameView.this.getLayoutParams().width > size / 2
                && StickerShopNameView.this.getLayoutParams().height > size / 2) {
              //scale down
              double offsetX = Math.abs(event.getRawX() - scale_orgX);
              double offsetY = Math.abs(event.getRawY() - scale_orgY);
              double offset = Math.max(offsetX, offsetY);
              offset = Math.round(offset);
              StickerShopNameView.this.getLayoutParams().width -= offset;
              StickerShopNameView.this.getLayoutParams().height -= offset;
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

    Log.v(TAG, "getRelativePos absX:" + absX);
    Log.v(TAG, "getRelativePos relativeX:" + pos[0]);



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

      iv_fontcolor.setVisibility(View.INVISIBLE);
      iv_fontsize.setVisibility(View.INVISIBLE);
      iv_fontformat.setVisibility(View.INVISIBLE);

    } else {
      iv_border.setVisibility(View.VISIBLE);
      iv_scale.setVisibility(View.VISIBLE);
      iv_delete.setVisibility(View.VISIBLE);

      iv_fontcolor.setVisibility(View.VISIBLE);
      iv_fontsize.setVisibility(View.VISIBLE);
      iv_fontformat.setVisibility(View.VISIBLE);
    }
  }


  public void setControlItemsMoveLock(boolean isMoveLock) {
    if (isMoveLock) {


      this.setTag("NonDraggableViewGroup");

    } else {

      this.setTag("DraggableViewGroup");
      }
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
      borderPaint.setPathEffect(new DashPathEffect(new float[]{ 5, 5, }, 0));
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


  /**
   * px을 dp로 변환
   * @param con
   * @param px
   * @return dp
   */
  public static int getPxToDp(Context con, int px) {
    float density = 0.0f;
    density  = con.getResources().getDisplayMetrics().density;
    Log.d("Desity", "density = " + density);
    return (int)(px / density);
  }

}
