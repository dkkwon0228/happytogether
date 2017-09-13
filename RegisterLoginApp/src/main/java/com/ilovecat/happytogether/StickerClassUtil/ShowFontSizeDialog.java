package com.ilovecat.happytogether.StickerClassUtil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.SeekBar;

import com.ilovecat.happytogether.MainShopRegisterCoupon.MainRegisterCouponShopStep1;
import com.ilovecat.happytogether.R;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 24. happytogether
 */
public class ShowFontSizeDialog {

  public static void showFontSizeDialog(final AutoResizeTextView tv_main, final AutoResizeTextView tv_main_shadow, final AppCompatActivity appCompatActivity) {
    final AlertDialog.Builder popDialog = new AlertDialog.Builder(appCompatActivity);
    final SeekBar seek = new SeekBar(appCompatActivity);
    seek.setMax(MainRegisterCouponShopStep1.MessageMaxfontSize);
    seek.setProgress(MainRegisterCouponShopStep1.MessagefontSize);
    popDialog.setIcon(R.drawable.sticker_fontsize);
    popDialog.setTitle("글자크기");
    popDialog.setView(seek);


    seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //Do something here with new value
        //txtView.setText("Value of : " + progress);


        MainRegisterCouponShopStep1.MessagefontSize = progress;

        tv_main.setTextSize(convertDpToPixel(progress, appCompatActivity));
        tv_main_shadow.setTextSize(convertDpToPixel(progress, appCompatActivity));
        //tv_sticker2.tv_main.setTextSize(convertDpToPixel(progress, context));
        //MainRegisterCouponShopStep1.tv_MessageSticker.setControlItemsHidden(false);


      }

      public void onStartTrackingTouch(SeekBar arg0) {
        // TODO Auto-generated method stub

      }

      public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

      }
    });


    // Button OK


    popDialog.setPositiveButton("확인",
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }

        });

    AlertDialog dialog = popDialog.create();

    dialog.show();


  }


  private static int convertDpToPixel(float dp, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();

    float px = dp * (metrics.densityDpi / 160f);
    return (int) px;
  }

}
