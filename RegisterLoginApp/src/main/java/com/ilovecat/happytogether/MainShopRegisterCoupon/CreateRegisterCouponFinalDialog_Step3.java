/*
 * Copyright (c) 2016.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ilovecat.happytogether.MainShopRegisterCoupon;

import android.app.Dialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ilovecat.happytogether.R;

import java.io.File;


/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 06. 29. happytogether
 */
public class CreateRegisterCouponFinalDialog_Step3 extends AppCompatActivity {

  public static void createRegisterCouponFinalDialog(
      AppCompatActivity activity,
      final String eMessage,
      final String strCouponNumber,
      final String strShopName,
      final String strCouponImageFileName,
      final String strCouponTitle,
      final String strCouponNotice,
      final String strCouponEndDate,
      final String strCouponNormalPrice,
      final String strCouponSalePrice,
      final String strCouponDescription,
      final String strCouponOpenState,
      final String strFullCouponImageFileName

  ) {
    final AppCompatActivity actActivity = activity;


    final Dialog dialog = new Dialog(actActivity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
    dialog.setContentView(R.layout.main_register_coupon_shop_final_dialog);

    String $errorMessage = eMessage;

    TextView srtErrorContent = (TextView) dialog.findViewById(R.id.error_content);
    srtErrorContent.setText($errorMessage);

    dialog.show();


    Button dialogButtonOpen = (Button) dialog.findViewById(R.id.btn_open);
    // if button is clicked, close the register_email_popupdialog_layout.xmlayout.xml dialog
    dialogButtonOpen.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {

            new AsyncTaskUpLoadCouponInfo_Step3(
                strCouponNumber,
                strShopName,
                strCouponImageFileName,
                strCouponTitle,
                strCouponNotice,
                strCouponEndDate,
                strCouponNormalPrice,
                strCouponSalePrice,
                strCouponDescription,
                strCouponOpenState

            ).execute();

          }
        }, 0);

        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {

            File file = new File(strFullCouponImageFileName);
            new AsyncTaskUpLoadFilesS3_Step3(file, strCouponOpenState).execute();

          }
        }, 3000);






        dialog.cancel();

      }
    });

    Button dialogButtonPrivate = (Button) dialog.findViewById(R.id.btn_private);
    // if button is clicked, close the register_email_popupdialog_layout.xmlayout.xml dialog
    dialogButtonPrivate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {

            new AsyncTaskUpLoadCouponInfo_Step3(
                strCouponNumber,
                strShopName,
                strCouponImageFileName,
                strCouponTitle,
                strCouponNotice,
                strCouponEndDate,
                strCouponNormalPrice,
                strCouponSalePrice,
                strCouponDescription,
                strCouponOpenState
            ).execute();
          }
        }, 0);

        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            File file = new File(strFullCouponImageFileName);
            new AsyncTaskUpLoadFilesS3_Step3(file, strCouponOpenState).execute();
          }
        }, 3000);


        dialog.cancel();
      }
    });


  }

}

