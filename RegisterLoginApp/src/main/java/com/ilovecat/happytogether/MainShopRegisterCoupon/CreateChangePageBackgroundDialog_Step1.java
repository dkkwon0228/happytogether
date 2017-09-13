package com.ilovecat.happytogether.MainShopRegisterCoupon;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 08. 14. happytogether
 */
public class CreateChangePageBackgroundDialog_Step1 extends AppCompatActivity{


  /******************************************************
   * CreateChangePageBackgroundDialog_Step1() 페이지백그라운드 교체 백그라운을 클릭했을 때
   ******************************************************/
  public static void CreateChangePageBackgroundDialog(final AppCompatActivity appcActivity) {
    AlertDialog.Builder alt_bld = new AlertDialog.Builder(appcActivity);
    alt_bld.setMessage("배경화면을 변경하시겠습니까?").setCancelable(
        false).setPositiveButton("예",
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {

            new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                // TODO Auto-generated method stub
                //doTakePhotoActionCHANGE();

                Intent intent = new Intent(MainRegisterCouponShopStep1.context, BackGroundMainActivity_Step1.class);
                // intent.putExtra("keyIsFrom", "CHANGEPAGE");
                // intent.putExtra("keyWherePagerFrom", strOfFromTempState);
                // intent.putExtra("keyTotalPageNum", imageUrls.size());
                // intent.putExtra("keyPageNum", CURRNET_PAGER_PAGE);
                // intent.putExtra("keyTempSaveId", TEMPSAVE_ID);
                appcActivity.startActivity(intent);
                // overridePendingTransition(R.layout.slide_in_right, R.layout.slide_out_left);

              }
            }, 500);

          }

        }).setNegativeButton("아니요",
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            // Action for 'NO' Button
            dialog.cancel();

          }
        });
    AlertDialog alert = alt_bld.create();
    // Title for AlertDialog
    //alert.setTitle("Title");
    // Icon for AlertDialog
    //alert.setIcon(R.drawable.icon);
    alert.show();
  }

}
