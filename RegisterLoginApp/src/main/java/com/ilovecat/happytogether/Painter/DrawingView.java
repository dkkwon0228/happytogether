package com.ilovecat.happytogether.Painter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;

import java.util.ArrayList;


public class DrawingView extends View {


  // Magnifieer
  private PointF zoomPos;
  PointF fingerPos;
  private Paint paint = new Paint(Color.BLACK);
  boolean zooming;
  Matrix matrix;
  BitmapShader mShader;
  Paint mPaint;
  Paint outlinePaint;
  boolean Point1;
  private Bitmap overLayedBitmap;


  private final Paint mPaintSrcIn = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
  private final Paint mPaintDstIn = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
  private final Paint mPaintColor = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint mPaintEraser = new Paint(Paint.ANTI_ALIAS_FLAG);

  private final Matrix mMatrix = new Matrix();
  private final Canvas mLayerCanvas = new Canvas();

  private Bitmap mInnerShape;
  private Bitmap mOuterShape;
  private Bitmap mLayerBitmap;


  private ArrayList<DrawOp> mDrawOps = new ArrayList<DrawOp>();
  private DrawOp mCurrentOp = new DrawOp();

  private ArrayList<DrawOp> mUndoneOps = new ArrayList<DrawOp>();

  private boolean isWhite = true;


  public DrawingView(Context context) {
    this(context, null, 0);
  }

  public DrawingView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DrawingView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    mPaintSrcIn.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    mPaintDstIn.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

    mPaintColor.setStyle(Paint.Style.STROKE);
    mPaintColor.setStrokeJoin(Paint.Join.ROUND);
    mPaintColor.setStrokeCap(Paint.Cap.ROUND);

    mPaintEraser.set(mPaintColor);
    mPaintEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    mPaintEraser.setMaskFilter(new BlurMaskFilter(getResources()
        .getDisplayMetrics().density * 4, BlurMaskFilter.Blur.NORMAL));


  }

  public void setShape(int inner, int outer) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inPreferredConfig = Bitmap.Config.ALPHA_8;
    setShape(BitmapFactory.decodeResource(getResources(), inner, options),
        BitmapFactory.decodeResource(getResources(), outer, options));
  }

  public void setShape(Bitmap inner, Bitmap outer) {
    mInnerShape = inner;
    mOuterShape = outer;
    requestLayout();
    invalidate();
  }

  public void setDrawingColor(int color) {
    mCurrentOp.reset();
    mCurrentOp.type = DrawOp.Type.PAINT;
    mCurrentOp.color = color;
  }

  public void setDrawingStroke(int stroke) {
    mCurrentOp.reset();
    mCurrentOp.type = DrawOp.Type.PAINT;
    mCurrentOp.stroke = stroke;
  }

  public void enableEraser() {
    mCurrentOp.reset();
    mCurrentOp.type = DrawOp.Type.ERASE;
  }

  public void clearDrawing() {
    mDrawOps.clear();
    mUndoneOps.clear();
    mCurrentOp.reset();
    invalidate();
  }

  public void undoOperation() {
    if (mDrawOps.size() > 0) {
      DrawOp last = mDrawOps.remove(mDrawOps.size() - 1);
      mUndoneOps.add(last);
      invalidate();
    }
  }

  public void redoOperation() {
    if (mUndoneOps.size() > 0) {
      DrawOp redo = mUndoneOps.remove(mUndoneOps.size() - 1);
      mDrawOps.add(redo);
      invalidate();
    }
  }


  public void setColorModeBlack() {
    isWhite = false;
  }

  public void setColorModeWhite() {
    isWhite = true;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mLayerBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    mLayerCanvas.setBitmap(mLayerBitmap);

    if (mOuterShape != null) {
      int dx = (w - mOuterShape.getWidth()) / 2;
      int dy = (h - mOuterShape.getHeight()) / 2;
      mMatrix.setTranslate(dx, dy);
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    if (isInEditMode()) {
      return;
    }

    // NOTE: Without extra bitmap or layer.. but HW Acceleration does not support setMaskFilter which means
    // eraser has strong edges whilst drawing.
    // @see http://developer.android.com/guide/topics/graphics/hardware-accel.html#unsupported
                /*
		canvas.drawBitmap(mOuterShape, 0, 0, null);
		canvas.saveLayer(null, mPaint, Canvas.FULL_COLOR_LAYER_SAVE_FLAG);
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		canvas.drawBitmap(mInnerShape, 0, 0, null);
		canvas.saveLayer(null, mPaintSrcIn, Canvas.FULL_COLOR_LAYER_SAVE_FLAG);
		canvas.drawBitmap(mBitmapDraw, 0, 0, null);
		canvas.drawPath(mPath, mPaintDraw);
		canvas.restore();
		canvas.restore();
		*/

    // Clear software canvas
    mLayerCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

    // Draw picture from ops
    for (DrawOp op : mDrawOps) {
      drawOp(mLayerCanvas, op);
    }
    drawOp(mLayerCanvas, mCurrentOp);

    // Mask the drawing to the inner surface area of the shape
    //mLayerCanvas.drawBitmap(mInnerShape, mMatrix, mPaintDstIn);

    // Draw orignal shape to view
    //canvas.drawBitmap(mOuterShape, mMatrix, null);

    // Draw masked image to view
    canvas.drawBitmap(mLayerBitmap, 0, 0, null);

    // Magnifier
    if (zooming) {
      matrix.reset();
      matrix.postScale(4f, 4f, zoomPos.x, zoomPos.y);
      mPaint.getShader().setLocalMatrix(matrix);
      RectF src = new RectF(zoomPos.x - 50, zoomPos.y - 50, zoomPos.x + 50, zoomPos.y + 50);
      RectF dst = new RectF(0, 0, 100, 100);
      matrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
      matrix.postScale(3f, 3f);
      mPaint.getShader().setLocalMatrix(matrix);


      canvas.drawCircle(200, 200, 200, mPaint);
      //canvas.drawCircle(zoomPos.x, zoomPos.y, 100, mPaint);
      //canvas.drawCircle(zoomPos.x - 110, zoomPos.y - 110, 10, outlinePaint);

    }
    if (Point1) {
      //canvas.drawCircle(zoomPos.x, zoomPos.y, 10, paint);
    }


    overLayedBitmap = overlayBitmapMaker(PainterActivity.mutableBitmap, mLayerBitmap, 0, 0);


  }

  private void drawOp(Canvas canvas, DrawOp op) {
    if (op.path.isEmpty()) {
      return;
    }
    final Paint paint;
    if (op.type == DrawOp.Type.PAINT) {
      paint = mPaintColor;
      paint.setColor(op.color);
      paint.setStrokeWidth(op.stroke);
    } else {
      paint = mPaintEraser;
      paint.setStrokeWidth(op.stroke);
    }
    mLayerCanvas.drawPath(op.path, paint);
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouchEvent(MotionEvent event) {

    final float x = event.getX();
    final float y = event.getY();

    int W;
    int H;

    if (isWhite) {
      W = PainterActivity.ivTouchPoint_White.getWidth();
      H = PainterActivity.ivTouchPoint_White.getHeight();

    } else {
      W = PainterActivity.ivTouchPoint_Black.getWidth();
      H = PainterActivity.ivTouchPoint_Black.getHeight();
    }

    // Magnifier
    zoomPos = new PointF();
    zoomPos.x = event.getX();
    zoomPos.y = event.getY();


    matrix = new Matrix();
    mShader = new BitmapShader(overLayedBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    mPaint = new Paint();
    mPaint.setShader(mShader);
    outlinePaint = new Paint(Color.BLACK);
    outlinePaint.setStyle(Paint.Style.STROKE);


    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        mUndoneOps.clear();
        mCurrentOp.path.moveTo(x, y);

        if (isWhite) {
          AbsoluteLayout.LayoutParams laypam = new AbsoluteLayout.LayoutParams(
              PainterActivity.ivTouchPoint_White.getWidth(),
              PainterActivity.ivTouchPoint_White.getHeight(), (int) x - W / 2, (int) y - H / 2);
          PainterActivity.ivTouchPoint_White.setLayoutParams(laypam);

        } else {
          AbsoluteLayout.LayoutParams laypam = new AbsoluteLayout.LayoutParams(
              PainterActivity.ivTouchPoint_Black.getWidth(),
              PainterActivity.ivTouchPoint_Black.getHeight(), (int) x - W / 2, (int) y - H / 2);
          PainterActivity.ivTouchPoint_Black.setLayoutParams(laypam);
        }





        break;

      case MotionEvent.ACTION_MOVE:


        if (isWhite) {
          AbsoluteLayout.LayoutParams laypam = new AbsoluteLayout.LayoutParams(
              PainterActivity.ivTouchPoint_White.getWidth(),
              PainterActivity.ivTouchPoint_White.getHeight(), (int) x - W / 2, (int) y - H / 2);
          PainterActivity.ivTouchPoint_White.setLayoutParams(laypam);

        } else {
          AbsoluteLayout.LayoutParams laypam = new AbsoluteLayout.LayoutParams(
              PainterActivity.ivTouchPoint_Black.getWidth(),
              PainterActivity.ivTouchPoint_Black.getHeight(), (int) x - W / 2, (int) y - H / 2);
          PainterActivity.ivTouchPoint_Black.setLayoutParams(laypam);
        }


        for (int i = 0; i < event.getHistorySize(); i++) {
          mCurrentOp.path.lineTo(event.getHistoricalX(i), event.getHistoricalY(i));
        }
        mCurrentOp.path.lineTo(x, y);

        // Magnifier
        zooming = true;
        this.invalidate();

        break;

      case MotionEvent.ACTION_UP:
        /*
        // Magnifier
        Point1 = true;
        zooming = false;
        this.invalidate();

        break;
        */

      case MotionEvent.ACTION_CANCEL:
        mCurrentOp.path.lineTo(x, y);
        mDrawOps.add(new DrawOp(mCurrentOp));
        mCurrentOp.path.reset();

        // Magnifier
        zooming = false;
        this.invalidate();

        break;
    }

    invalidate();

    return true;
  }

  private static class DrawOp {
    public final Path path = new Path();
    public Type type;
    public int color;
    public int stroke;


    public DrawOp() {
      //
    }

    public void reset() {
      this.path.reset();
    }

    public DrawOp(DrawOp op) {
      this.path.set(op.path);
      this.type = op.type;
      this.color = op.color;
      this.stroke = op.stroke;
    }

    public static enum Type {
      PAINT, ERASE;
    }
  }

  private Bitmap overlayBitmapMaker(Bitmap baseBmp, Bitmap overlayBmp, int distanceLeft, int distanceTop) {

    Bitmap resultBmp = Bitmap.createBitmap(baseBmp.getWidth(),
        baseBmp.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(resultBmp);
    canvas.drawBitmap(baseBmp, 0, 0, null);
    canvas.drawBitmap(overlayBmp, distanceLeft, distanceTop, null);
    return resultBmp;
  }

}
