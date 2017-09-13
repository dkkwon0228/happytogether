package com.ilovecat.happytogether.ControllerCropBorderSticker;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;



public class SizeAwareImageView extends ImageView {

    public SizeAwareImageView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }
    
    int actW;
    int actH;
    
    int origW;
    int origH;
    
    float scaleX; 
    float scaleY; 

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Get image matrix values and place them in an array
        float[] f = new float[9];
        getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        scaleX = f[Matrix.MSCALE_X];
        scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = getDrawable();
        origW = d.getIntrinsicWidth();
        origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        actW = Math.round(origW * scaleX);
        actH = Math.round(origH * scaleY);

        Log.e("DBG", "["+origW+","+origH+"] -> ["+actW+","+actH+"] & scales: x="+scaleX+" y="+scaleY);
    }
    
    int getActW() {
		return actW;
    }
    
    int getActH() {
		return actH;
    }
    
    int getOrigW() {
		return origW;
    }
    
    int getOrigH() {
		return origH;
    }
    
    float getScaleXImageView() {
		return scaleX;
    }
    
    float getScaleYImageView() {
		return scaleY;
    }

}  
