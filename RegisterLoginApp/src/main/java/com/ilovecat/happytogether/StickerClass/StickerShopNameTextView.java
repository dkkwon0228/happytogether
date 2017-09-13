package com.ilovecat.happytogether.StickerClass;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.ilovecat.happytogether.MainShopRegisterCoupon.MainRegisterCouponShopStep1;
import com.ilovecat.happytogether.StickerClassUtil.AutoResizeTextView;


/**
 * Created by cheungchingai on 6/15/15.
 */
public class StickerShopNameTextView extends StickerShopNameView {


    public AutoResizeTextView tv_main;

    public StickerShopNameTextView(Context context) {
        super(context);
    }

    public StickerShopNameTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerShopNameTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View getMainView() {
        if(tv_main != null)
            return tv_main;

        tv_main = new AutoResizeTextView(getContext());
        tv_main.setTextColor(MainRegisterCouponShopStep1.currentShopNameTextColor);
        tv_main.setGravity(Gravity.RIGHT | Gravity.CENTER_HORIZONTAL);
        tv_main.setTypeface(Typeface.DEFAULT_BOLD);
        tv_main.setTextSize(convertDpToPixel(MainRegisterCouponShopStep1.ShopNamefontSize, getContext()));
        //tv_main.setShadowLayer(4, 0, 0, MainRegisterCouponShopStep1.currentMessageTextColor);
        tv_main.setMaxLines(MainRegisterCouponShopStep1.ShopNameMaxLine);

        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.CENTER;


        iv_fontsize.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.v(TAG, "flip the view");


                //((MainRegisterCouponShopStep1)getContext()).ShowFontSizeDialog(tv_main);


            }
        });


        iv_fontcolor.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.v(TAG, "flip the view");

                //((MainRegisterCouponShopStep1)getContext()).getColor(tv_main);

            }
        });





        tv_main.setLayoutParams(params);
        if(getImageViewFlip()!=null)
            getImageViewFlip().setVisibility(View.GONE);
        return tv_main;

    }


    public void setText(String text){
        if(tv_main!=null)
            tv_main.setText(text);
    }

    public String getText(){
        if(tv_main!=null)
            return tv_main.getText().toString();

        return null;
    }



    private static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();

        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }




}
