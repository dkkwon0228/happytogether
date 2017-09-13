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

package com.ilovecat.happytogether.CommonConstantsAndUtils;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ilovecat.happytogether.R;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 06. 29. happytogether
 */
public class CreateErrorDialog {

  public static void createErrorDialog(AppCompatActivity activity, String eTitle, String eMessage) {
    final AppCompatActivity actActivity = activity;


    final Dialog dialog = new Dialog(actActivity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
    dialog.setContentView(R.layout.common_error_popupdialog_layout);

    String $errorTitle = eTitle;
    String $errorMessage = eMessage;

    TextView srtErrorTitle = (TextView) dialog.findViewById(R.id.error_title);
    srtErrorTitle.setText($errorTitle);
    TextView srtErrorContent = (TextView) dialog.findViewById(R.id.error_content);
    srtErrorContent.setText($errorMessage);

    dialog.show();


    Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
    // if button is clicked, close the register_email_popupdialog_layout.xmlayout.xml dialog
    dialogButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dialog.cancel();
      }
    });


  }
}
