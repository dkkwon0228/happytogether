package com.ilovecat.happytogether.StickerClassUtil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.ilovecat.happytogether.MainShopRegisterCoupon.MainRegisterCouponShopStep1;
import com.ilovecat.happytogether.R;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 24. happytogether
 */
public class ShowFontBorderSizeDialog {

  public static void showFontBorderSizeDialog(final AutoResizeTextView tv_main_shadow, final AppCompatActivity appCompatActivity) {


    LayoutInflater adbInflater = LayoutInflater.from(appCompatActivity);
    View notshowagainLayout = adbInflater.inflate(R.layout.check_texture_message_text, null);

    // 체크박스의 값을 읽어 오기 위해서
    //final CheckBox dontShowAgain = (CheckBox)notshowagainLayout.findViewById(R.id.skip);



    final View innerView = appCompatActivity.getLayoutInflater().inflate(R.layout.check_texture_message_text, null);
    final CheckBox checkCenterShadow = (CheckBox)innerView.findViewById(R.id.center_shadow);

    /*
    if(MainRegisterCouponShopStep1.isCenterShadow) {
      checkCenterShadow.setChecked(true);
    } else {
      checkCenterShadow.setChecked(false);
    }
    */

    if(tv_main_shadow.getCenterState()) {
      checkCenterShadow.setChecked(true);
    } else {
      checkCenterShadow.setChecked(false);
    }

    final SeekBar seek = (SeekBar) innerView.findViewById(R.id.seekbar);

    final AlertDialog.Builder popDialog = new AlertDialog.Builder(appCompatActivity);
    seek.setMax(MainRegisterCouponShopStep1.MessageMaxShadowRadius);
    seek.setProgress(MainRegisterCouponShopStep1.MessageShadowRadius);
    popDialog.setIcon(R.drawable.ic_border_outer);
    popDialog.setTitle("그림자 크기");
    popDialog.setView(innerView);

    checkCenterShadow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(isChecked) {


          tv_main_shadow.setCenterState(true);
          MainRegisterCouponShopStep1.isCenterShadow = true;

        } else {
          tv_main_shadow.setCenterState(false);
          MainRegisterCouponShopStep1.isCenterShadow = false;
        }
        tv_main_shadow.setShadowRadius(MainRegisterCouponShopStep1.MessageShadowRadius, MainRegisterCouponShopStep1.currentMessageShadowColor);

      }
    });


    seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //Do something here with new value
        //txtView.setText("Value of : " + progress);

        MainRegisterCouponShopStep1.MessageShadowRadius = progress;
        tv_main_shadow.setShadowRadius(progress, MainRegisterCouponShopStep1.currentMessageShadowColor);

      }

      public void onStartTrackingTouch(SeekBar arg0) {
        // TODO Auto-generated method stub
      }

      public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
      }
    });




    popDialog.setPositiveButton("확인",
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }

        });

    AlertDialog dialog = popDialog.create();

    dialog.show();

  }



}
