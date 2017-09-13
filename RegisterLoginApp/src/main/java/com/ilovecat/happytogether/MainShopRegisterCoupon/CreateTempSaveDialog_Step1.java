package com.ilovecat.happytogether.MainShopRegisterCoupon;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.ilovecat.happytogether.MainShopListingCoupon.MainCouponShopFragment;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 08. 14. happytogether
 */
public class CreateTempSaveDialog_Step1 extends AppCompatActivity{

  /******************************************************
   * CreateTempSaveDialog_Step1()
   *
   * 다음 버튼을 눌렀을때
   ******************************************************/

  public static void CreateTempSaveDialog(final AppCompatActivity appcActivity) {
    AlertDialog.Builder alt_bld = new AlertDialog.Builder(appcActivity);
    alt_bld.setMessage("임시보관소에 저장하시겠습니까?" + "\n" + "차후 수정할 수 있습니다.").setCancelable(
        false).setPositiveButton("예",
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {


            //for(int n=0; n < TOTAL_PAGER_NUM; n++ ) {
            new AsyncTaskTempSave_Step1().execute(0);



            appcActivity.finish();



            //}


          }

        }).setNegativeButton("아니요",
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            // Action for 'NO' Button
            dialog.cancel();
            appcActivity.finish();

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
