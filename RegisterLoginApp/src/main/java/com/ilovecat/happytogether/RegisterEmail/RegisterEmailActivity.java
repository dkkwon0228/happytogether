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

/**
 * ${PAKAGE_NAME} 설명은 여기서 부터 *
 *
 *
 * Created by dannykwon on 2016 16. 6. 21. happytogether
 */

package com.ilovecat.happytogether.RegisterEmail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.RegisterMyLocation.RegisterMyLocationActivity;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.CommonUIOfTopBar;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.GoBackAction;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.InputFilter;


public class RegisterEmailActivity extends AppCompatActivity {

  public static EditText etEmailAccount;
  public static EditText etCustomEmailAccount;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.register_email_layout);

    final AppCompatActivity appcActivity = this;

    // 상단바 공통 UI
    CommonUIOfTopBar.setCommonUI(this);

    // 상단좌측 뒤로 가기
    GoBackAction.goBackAndDoAnimation(this);


    final EditText etEmailId;

    etEmailId = (EditText) findViewById(R.id.etRegisterEmail);
    // 숫자와 알파벳 허용
    etEmailId.setFilters(new android.text.InputFilter[]{InputFilter.filterAlphaNum});
    // 초기입력은 영문으로
    etEmailId.setPrivateImeOptions("defaultInputmode=english;");
    etEmailId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_NEXT) {

          CreateEmailAccountListViewDialog.createEmailAccountListViewDialog(appcActivity);

          return true;
        } else {
          return false;
        }
      }
    });


    etCustomEmailAccount = (EditText) findViewById(R.id.etRegisterCustomEmailAccount);
    // 숫자 영문 닷(.) 허용
    etCustomEmailAccount.setFilters(new android.text.InputFilter[]{InputFilter.filterAlphaNumDot});
    // 초기입력은 영문으로
    etCustomEmailAccount.setPrivateImeOptions("defaultInputmode=english;");
    etCustomEmailAccount.setVisibility(View.GONE);

    etEmailAccount = (EditText) findViewById(R.id.etRegisterEmailAccount);
    /*
     * xml 레이아웃에 추가해야 함.
     * android:focusable="false"
     */
    etEmailAccount.setInputType(0);
    // 클릭 리스너
    etEmailAccount.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View arg0) {

        CreateEmailAccountListViewDialog.createEmailAccountListViewDialog(appcActivity);

      }
    });


    Button btnRegNext = (Button) findViewById(R.id.reg_next_button);
    btnRegNext.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        etEmailId.setError(null);
        etEmailAccount.setError(null);
        etCustomEmailAccount.setError(null);


        String lvEmailId =  "TEST";
        String lvEmailAccount =  "google.com";
        String lvCustomEmailAccount =  "google2.com";

        //String lvEmailId = etEmailId.getText().toString().trim();
        //String lvEmailAccount = etEmailAccount.getText().toString().trim();
        //String lvCustomEmailAccount = etCustomEmailAccount.getText().toString().trim();

        if (TextUtils.isEmpty(lvEmailId)) {
          etEmailId.setError(getString(R.string.error_field_required));
        } else if (lvEmailId.length() > 20) {
          etEmailId.setError(getString(R.string.error_long_length));
        } else if (lvEmailId.length() < 3) {
          etEmailId.setError(getString(R.string.error_short_length));
        } else if (TextUtils.isEmpty(lvEmailAccount)) {
          etEmailAccount.setError(getString(R.string.error_field_required));
        } else if (lvCustomEmailAccount.length() < 3) {
          etCustomEmailAccount.setError(getString(R.string.error_short_length));
        } else if (lvCustomEmailAccount.length() > 20) {
          etCustomEmailAccount.setError(getString(R.string.error_long_length));
        } else if (TextUtils.isEmpty(lvCustomEmailAccount)) {
          etCustomEmailAccount.setError(getString(R.string.error_field_required));
        } else {

          String[] arr = getResources().getStringArray(R.array.array_email_account);
          String strCompletelyEmail = CheckEmail.combineCompleteEmail(arr, lvEmailId,
              lvEmailAccount, lvCustomEmailAccount).trim();

          if (CheckEmail.checkEmail(strCompletelyEmail)) {

            Intent intent = getIntent();
            String strAdminId = intent.getExtras().getString("ADMINID");
            String strAdminPassword = intent.getExtras().getString("ADMINPASSWORD");


            // Launch Dashboard Screen
            Intent dashboard = new Intent(getApplicationContext(), RegisterMyLocationActivity.class);
            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            dashboard.putExtra("ADMINID", strAdminId);
            dashboard.putExtra("ADMINPASSWORD", strAdminPassword);
            dashboard.putExtra("ADMINEMAIL", strCompletelyEmail);

            startActivity(dashboard);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            //CreateSelectRegisterShopOrDoneDialog.createSelectRegisterShopNameDialog(appcActivity, strCompletelyEmail);

          } else {
            //etCustomEmailAccount.setError(getString(R.string.error_invalid_emailtype));
          }

        }
      }
    });
  }





  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
  }

}

