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

package com.ilovecat.happytogether.RegisterMyLocation;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonUtils;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CreateErrorDialog;
import com.ilovecat.happytogether.RegisterShopLocation.RegisterShopLocationActivity;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 06. 29. happytogether
 */
public class CreateSelectRegisterShopOrDoneDialog {

  public static void createSelectRegisterShopOrDoneDialog(AppCompatActivity activity,
                                                          String mylocation,
                                                          String mylocationSiGun) {
    final AppCompatActivity actActivity = activity;

    if (CommonUtils.isNetworkAvailable(actActivity)) {

      final Dialog dialog = new Dialog(actActivity);
      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
      dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
      dialog.setContentView(R.layout.register_email_select_register_shopname_popupdialog);

      final String strMyLocation = mylocation.trim();
      final String strMyLocationSiGun = mylocationSiGun.trim();

      String[] arraySiGun =  strMyLocationSiGun.split(" ");
      final String strMyLocationSi = arraySiGun[0];
      final String strMyLocationGun = arraySiGun[1];


      Button dialogButtonDone = (Button) dialog.findViewById(R.id.btn_ok);
      // if button is clicked, close the register_email_popupdialog_layout.xmlayout.xml dialog
      dialogButtonDone.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          Intent intent = actActivity.getIntent();
          String strAdminId = intent.getExtras().getString("ADMINID");
          String strAdminPassword = intent.getExtras().getString("ADMINPASSWORD");
          String strAdminEmial = intent.getExtras().getString("ADMINEMAIL");


        /*
        Intent intentMove = new Intent(actActivity, MainSearch.class);
        intentMove.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        actActivity.startActivity(intentMove);
        String strAdminId = intent.getExtras().getString("ADMINID");
        String strAdminPassword = intent.getExtras().getString("ADMINPASSWORD");
        String strAdminEmail = intent.getExtras().getString("ADMINEMAIL");
        actActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        */
          dialog.cancel();
        }
      });

      Button dialogButtonGo = (Button) dialog.findViewById(R.id.btn_go);
      // if button is clicked, close the register_email_popupdialog_layout.xmlayout.xml dialog
      dialogButtonGo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {


          Intent intent = actActivity.getIntent();
          String strAdminId = intent.getExtras().getString("ADMINID");
          String strAdminPassword = intent.getExtras().getString("ADMINPASSWORD");
          String strAdminEmail = intent.getExtras().getString("ADMINEMAIL");

          Intent intentMove = new Intent(actActivity, RegisterShopLocationActivity.class);
          intentMove.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intentMove.putExtra("ADMINID", strAdminId);
          intentMove.putExtra("ADMINPASSWORD", strAdminPassword);
          intentMove.putExtra("ADMINEMAIL", strAdminEmail);

          intentMove.putExtra("MYLOCATION", strMyLocation);
          intentMove.putExtra("MYLOCATIONSIGUN", strMyLocationSiGun);
          intentMove.putExtra("MYLOCATIONSI", strMyLocationSi);
          intentMove.putExtra("MYLOCATIONGUN", strMyLocationGun);
          actActivity.startActivity(intentMove);
          actActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

          dialog.cancel();


        }
      });

      dialog.show();


    } else {

      String eTitle = actActivity.getResources().getString(R.string.mem_regist);
      String eMessage = actActivity.getResources().getString(R.string.error_network_problem);

      CreateErrorDialog.createErrorDialog(actActivity, eTitle, eMessage);

    }
  }
}
