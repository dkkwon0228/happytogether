package com.ilovecat.happytogether.Painter;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 08. 10. happytogether
 */
public class ZoomView extends ImageView {

  private PointF zoomPos;
  PointF fingerPos;
  private Paint paint = new Paint(Color.BLACK);
  boolean zooming;
  Matrix matrix;
  BitmapShader mShader;
  Paint mPaint;
  Paint outlinePaint;
  boolean Point1;

  public ZoomView(Context context) {
    super(context);
  }

  public ZoomView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ZoomView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public boolean onTouchEvent(@NonNull MotionEvent event) {

    zoomPos = new PointF();
    zoomPos.x = event.getX();
    zoomPos.y = event.getY();

    matrix = new Matrix();
    mShader = new BitmapShader(PainterActivity.mutableBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    mPaint = new Paint();
    mPaint.setShader(mShader);
    outlinePaint = new Paint(Color.BLACK);
    outlinePaint.setStyle(Paint.Style.STROKE);

    int action = event.getAction();

    switch (action) {
      case MotionEvent.ACTION_DOWN:
      case MotionEvent.ACTION_MOVE:
        zooming = true;
        this.invalidate();
        break;
      case MotionEvent.ACTION_UP:
        Point1 = true;
        zooming = false;
        this.invalidate();
        break;
      case MotionEvent.ACTION_CANCEL:
        zooming = false;
        this.invalidate();
        break;

      default:
        break;
    }


    return true;
  }

  @Override
  protected void onDraw(@NonNull Canvas canvas) {
    super.onDraw(canvas);
    if (zooming) {
      matrix.reset();
      matrix.postScale(2f, 2f, zoomPos.x, zoomPos.y);
      mPaint.getShader().setLocalMatrix(matrix);
      RectF src = new RectF(zoomPos.x - 50, zoomPos.y - 50, zoomPos.x + 50, zoomPos.y + 50);
      RectF dst = new RectF(0, 0, 100, 100);
      matrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
      matrix.postScale(2f, 2f);
      mPaint.getShader().setLocalMatrix(matrix);

      canvas.drawCircle(100, 100, 100, mPaint);
      canvas.drawCircle(zoomPos.x, zoomPos.y, 100, mPaint);
      canvas.drawCircle(zoomPos.x - 110, zoomPos.y - 110, 10, outlinePaint);

    }
    if (Point1) {
      canvas.drawCircle(zoomPos.x, zoomPos.y, 10, paint);
    }
  }
}