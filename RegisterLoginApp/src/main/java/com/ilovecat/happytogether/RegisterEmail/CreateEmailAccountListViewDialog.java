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

package com.ilovecat.happytogether.RegisterEmail;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.ilovecat.happytogether.R;

/**
 * 레지스트 상단바
 */
public class CreateEmailAccountListViewDialog extends AppCompatActivity {

  public static void createEmailAccountListViewDialog(AppCompatActivity activity) {
    final AppCompatActivity actActivity = activity;


    final Dialog dialog = new Dialog(actActivity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
    dialog.setContentView(R.layout.register_email_popupdialog_layout);

    ArrayAdapter<String> strArrayAdapter;
    strArrayAdapter = new ArrayAdapter<String>(actActivity, R.layout.register_email_account_item_layout);
    //이메일 계정 배열을 불러온다.
    String[] arr = actActivity.getResources().getStringArray(R.array.array_email_account);
    for (int i = 0; i < arr.length; i++) {
      strArrayAdapter.add(arr[i]);
    }

    ListView listviewEmailAccounts = (ListView) dialog.findViewById(R.id.lv);
    listviewEmailAccounts.setAdapter(strArrayAdapter);
    listviewEmailAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = (ListView) parent;

        String item = (String) listView.getItemAtPosition(position);
        RegisterEmailActivity.etEmailAccount.setText(item);

        String[] arr = actActivity.getResources().getStringArray(R.array.array_email_account);
        if (position == (arr.length - 1)) {
          RegisterEmailActivity.etCustomEmailAccount.setText(null);
          RegisterEmailActivity.etCustomEmailAccount.setVisibility(View.VISIBLE);
        } else {
          String tempEmailAccount = actActivity.getResources().getString(R.string.temp_email_account);
          RegisterEmailActivity.etCustomEmailAccount.setText(tempEmailAccount);
          RegisterEmailActivity.etCustomEmailAccount.setVisibility(View.GONE);
        }

        dialog.cancel();
      }
    });

    Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
    // if button is clicked, close the register_email_popupdialog_layoutog_layout.xml dialog
    dialogButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dialog.cancel();
      }
    });

    dialog.show();
  }
}
