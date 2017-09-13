package com.ilovecat.happytogether.StickerClass;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ReplacementSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ilovecat.happytogether.MainShopRegisterCoupon.MainRegisterCouponShopStep1;
import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.StickerClassUtil.AutoResizeTextView;


/**
 * Created by cheungchingai on 6/15/15.
 */
public class StickerMessageTextView extends StickerMessageView {


    public AutoResizeTextView tv_main;
    public AutoResizeTextView tv_main_shadow;

    public StickerMessageTextView(Context context) {
        super(context);
    }

    public StickerMessageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerMessageTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View getMainView() {
        if(tv_main != null)
            return tv_main;

        tv_main = new AutoResizeTextView(getContext());
        tv_main.setTextColor(MainRegisterCouponShopStep1.currentMessageTextColor);
        tv_main.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        tv_main.setTypeface(Typeface.DEFAULT_BOLD);
        tv_main.setTextSize(convertDpToPixel(MainRegisterCouponShopStep1.MessagefontSize, getContext()));
        //tv_main.setShadowLayer(15, 0, 0, Color.BLACK);
        tv_main.setMaxLines(MainRegisterCouponShopStep1.MessageMaxLine);
        //tv_main.setPadding(10, 0, convertDpToPixel(10, getContext()), 0);


        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.CENTER;



        tv_main.setLayoutParams(params);
        if(getImageViewFlip()!=null)
            getImageViewFlip().setVisibility(View.GONE);


        return tv_main;

    }

    @Override
    public View getMainView_Shadow() {
        if(tv_main_shadow != null)
            return tv_main_shadow;

        tv_main_shadow = new AutoResizeTextView(getContext());
        tv_main_shadow.setTextColor(MainRegisterCouponShopStep1.currentMessageTextColor);
        tv_main_shadow.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        tv_main_shadow.setTypeface(Typeface.DEFAULT_BOLD);
        tv_main_shadow.setTextSize(convertDpToPixel(MainRegisterCouponShopStep1.MessagefontSize, getContext()));
        //tv_main.setShadowLayer(15, 0, 0, Color.BLACK);
        tv_main_shadow.setMaxLines(MainRegisterCouponShopStep1.MessageMaxLine);
        //tv_main.setPadding(10, 0, convertDpToPixel(10, getContext()), 0);



        tv_main_shadow.setShadowRadius(MainRegisterCouponShopStep1.MessageShadowRadius,
            MainRegisterCouponShopStep1.currentMessageShadowColor
        );

        tv_main_shadow.setShadowColor(MainRegisterCouponShopStep1.MessageShadowRadius,
            MainRegisterCouponShopStep1.currentMessageShadowColor
        );

        tv_main_shadow.setCenterState(MainRegisterCouponShopStep1.isCenterShadow);



        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.CENTER;


        tv_main_shadow.setLayoutParams(params);
        if(getImageViewFlip()!=null)
            getImageViewFlip().setVisibility(View.GONE);
        return tv_main_shadow;

    }


    public void setText(String text){
        if(tv_main!=null)
            tv_main.setText(text);
        if(tv_main_shadow!=null)
            tv_main_shadow.setText(text);
    }

    public String getText(){
        if(tv_main!=null)
            return tv_main.getText().toString();

        if(tv_main_shadow!=null)
            return tv_main_shadow.getText().toString();

        return null;
    }



    private static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();

        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }






}
