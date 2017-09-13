package com.ilovecat.happytogether.ControllerMessageColorPicker;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 19. happytogether
 */

    import android.content.res.Resources;
    import android.os.Bundle;
    import android.app.Dialog;
    import android.content.Context;
    import android.graphics.*;
    import android.util.DisplayMetrics;
    import android.view.MotionEvent;
    import android.view.View;

    import com.ilovecat.happytogether.MainShopRegisterCoupon.MainRegisterCouponShopStep1;
    import com.ilovecat.happytogether.StickerClassUtil.AutoResizeTextView;

public class MessageColorPicker extends Dialog {

  public interface OnColorChangedListener {
    void colorChanged(int color ,AutoResizeTextView tv_main);
    void colorChanging(int color ,AutoResizeTextView tv_main);
  }

  private OnColorChangedListener mListener;
  private int mInitialColor;
  private static AutoResizeTextView mTV_main;

  private static class ColorPickerView extends View {
    private Paint mPaint;
    private Paint mCenterPaint;
    private final int[] mColors;
    private OnColorChangedListener mListener;

    ColorPickerView(Context c, OnColorChangedListener l, int color) {
      super(c);
      mListener = l;
      mColors = new int[] {
          0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
          0xFFFFFF00, 0xFFFF0000
      };
      Shader s = new SweepGradient(0, 0, mColors, null);

      mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      mPaint.setShader(s);
      mPaint.setStyle(Paint.Style.STROKE);
      mPaint.setStrokeWidth(convertDpToPixel(40, MainRegisterCouponShopStep1.context));

      mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      mCenterPaint.setColor(color);
      mCenterPaint.setStrokeWidth(5);
    }

    private boolean mTrackingCenter;
    private boolean mHighlightCenter;

    @Override
    protected void onDraw(Canvas canvas) {
      float r = CENTER_X - mPaint.getStrokeWidth()*0.5f;

      canvas.translate(CENTER_X, CENTER_X);

      canvas.drawOval(new RectF(-r, -r, r, r), mPaint);
      canvas.drawCircle(0, 0, CENTER_RADIUS, mCenterPaint);

      if (mTrackingCenter) {
        int c = mCenterPaint.getColor();
        mCenterPaint.setStyle(Paint.Style.STROKE);

        if (mHighlightCenter) {
          mCenterPaint.setAlpha(0xFF);
        } else {
          mCenterPaint.setAlpha(0x80);
        }
        canvas.drawCircle(0, 0, CENTER_RADIUS + mCenterPaint.getStrokeWidth(), mCenterPaint);

        mCenterPaint.setStyle(Paint.Style.FILL);
        mCenterPaint.setColor(c);
      }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      setMeasuredDimension(CENTER_X*2, CENTER_Y*2);
    }

    private static final int CENTER_X = convertDpToPixel(100, MainRegisterCouponShopStep1.context);
    private static final int CENTER_Y = convertDpToPixel(100, MainRegisterCouponShopStep1.context);
    private static final int CENTER_RADIUS = convertDpToPixel(40, MainRegisterCouponShopStep1.context);

    private int ave(int s, int d, float p) {
      return s + java.lang.Math.round(p * (d - s));
    }

    private int interpColor(int colors[], float unit) {
      if (unit <= 0) {
        return colors[0];
      }
      if (unit >= 1) {
        return colors[colors.length - 1];
      }

      float p = unit * (colors.length - 1);
      int i = (int)p;
      p -= i;

      int c0 = colors[i];
      int c1 = colors[i+1];
      int a = ave(Color.alpha(c0), Color.alpha(c1), p);
      int r = ave(Color.red(c0), Color.red(c1), p);
      int g = ave(Color.green(c0), Color.green(c1), p);
      int b = ave(Color.blue(c0), Color.blue(c1), p);

      return Color.argb(a, r, g, b);
    }

    private static final float PI = 3.1415926f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
      float x = event.getX() - CENTER_X;
      float y = event.getY() - CENTER_Y;
      boolean inCenter = java.lang.Math.sqrt(x*x + y*y) <= CENTER_RADIUS;

      switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
          mTrackingCenter = inCenter;
          if (inCenter) {
            mHighlightCenter = true;
            invalidate();
            break;
          }
        case MotionEvent.ACTION_MOVE:
          if (mTrackingCenter) {
            if (mHighlightCenter != inCenter) {
              mHighlightCenter = inCenter;
              invalidate();
            }
          } else {
            float angle = (float)java.lang.Math.atan2(y, x);
            float unit = angle/(2*PI);
            if (unit < 0) {
              unit += 1;
            }
            mCenterPaint.setColor(interpColor(mColors, unit));


            mListener.colorChanging(mCenterPaint.getColor(), mTV_main);


            invalidate();
          }
          break;
        case MotionEvent.ACTION_UP:
          if (mTrackingCenter) {
            if (inCenter) {
              mListener.colorChanged(mCenterPaint.getColor(),mTV_main);
            }
            mTrackingCenter = false;
            invalidate();
          }
          break;
      }
      return true;
    }
  }

  public MessageColorPicker(Context context, OnColorChangedListener listener, int initialColor, AutoResizeTextView tv_main) {
    super(context);

    mListener = listener;
    mInitialColor = initialColor;
    mTV_main =  tv_main;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    OnColorChangedListener l = new OnColorChangedListener() {

      public void colorChanged(int color, AutoResizeTextView tv_main) {
        mListener.colorChanged(color, tv_main);

        dismiss();
      }

      public void colorChanging(int color, AutoResizeTextView tv_main) {
        mListener.colorChanging(color, tv_main);
        //dismiss();
      }

    };

    setContentView(new ColorPickerView(getContext(), l, mInitialColor));
    //setTitle("Pick a Color");
  }


  private static int convertDpToPixel(float dp, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float px = dp * (metrics.densityDpi / 160f);
    return (int) px;
  }

}
