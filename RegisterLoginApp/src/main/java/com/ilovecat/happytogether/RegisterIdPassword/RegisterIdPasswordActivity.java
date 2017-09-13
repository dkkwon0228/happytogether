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
 * 설명은 여기서 부터
 *
 * Created by dannykwon on 2016 . 6. 20. happytogether
 */

package com.ilovecat.happytogether.RegisterIdPassword;

import static com.ilovecat.happytogether.CommonConstantsAndUtils.CommonConstants.DB_LOGTAG;
import static com.ilovecat.happytogether.CommonConstantsAndUtils.CommonConstants.JSON_KEY_SUCCESS;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonUtils;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CreateErrorDialog;
import com.ilovecat.happytogether.Databasehandler.DatabaseHandler;
import com.ilovecat.happytogether.Databasehandler.JsonHttpUserFunctions;
import com.ilovecat.happytogether.RegisterEmail.RegisterEmailActivity;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.CommonUIOfTopBar;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.InputFilter;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * <pre>
 * 1. 패키지명 : com.appmaker.gcmtest.adminlogin
 * 2. 타입명 :  RegisterIdPasswordActivity In RegisterIdPasswordActivity.java
 * 3. 작성일 : 2016. 5. 9. 오전 12:02:47
 * 4. 작성자 : dannykwon
 * 5. 설명 :
 * </pre>
 */
public class RegisterIdPasswordActivity extends AppCompatActivity {

  private static String KEY_LOGINSTATE_LOGIN = "1";

  private static final String photo_temp_path = Environment.getExternalStorageDirectory().getAbsolutePath()
      + "/DCIM/MyTown/Temp/";
  private static final String photo_path = Environment.getExternalStorageDirectory().getAbsolutePath()
      + "/DCIM/MyTown/";
  private static final String xml_path = Environment.getExternalStorageDirectory().getAbsolutePath()
      + "/MyTown/";
  private static final String shopimages_path = Environment.getExternalStorageDirectory().getAbsolutePath()
      + "/DCIM/MyTown/ShopImages/";

  private static final String DB_TAG = "SQLDataBase";
  private static final String GCM_TAG = "GCM_JOB";
  private static final String FILE_IO_TAG = "FILE_IO";


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.register_id_password_layout);

    final AppCompatActivity appcActivity = this;


    // 상단바 공통 UI
    CommonUIOfTopBar.setCommonUI(this);

    // BUTTON assets
    final Button btnRegister;
    final Button btnLinkToLogin;
    Button btnlvRegNext = (Button) findViewById(R.id.reg_next_button);
    // AutoCompleteTextView
    final AutoCompleteTextView actvAdminid = (AutoCompleteTextView) findViewById(R.id.etRegisterAdminid);
    final AutoCompleteTextView actvAdminpasswd = (AutoCompleteTextView) findViewById(R.id.etRegisterAdminpasswd);
    final AutoCompleteTextView actvAdminpasswd2 = (AutoCompleteTextView) findViewById(R.id.et_Re_RegisterAdminpasswd);

    // DatabaseHandle In databasehandler package
    DatabaseHandler handlerDatabase = new DatabaseHandler(this);
    int count = handlerDatabase.getRowCountAdminkey();
    Log.i(DB_LOGTAG, "ADMINKEY_COUNT in REGISTER = " + count);
    handlerDatabase.close();

    if (android.os.Build.VERSION.SDK_INT > 9) {
      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
      StrictMode.setThreadPolicy(policy);
    }

    // 내부 DB의 adminkey_tb 테이블의 row 값이 0일 경우 (앱 관리자 등록이 안되어 있을 경우)
    if (count == 0) {

      /*
      아이디, 패스워드, 패스워드재입력 필드 입력모드 설정
      2016/10/04
      */

      // 아이디 - 초기입력은 알파벳
      actvAdminid.setPrivateImeOptions("defaultInputmode=english;");
      // 아이디 - 알파벳과 숫자만 입력받게.
      actvAdminid.setFilters(new android.text.InputFilter[]{InputFilter.filterAlphaNum});

      // 입력되는 값을 비밀번호 입력같이 * 표시로 바꿀수 있다.
      PasswordTransformationMethod ptmPassword = new PasswordTransformationMethod();
      actvAdminpasswd.setTransformationMethod(ptmPassword);
      // 패스워드 - 초기 입력은 알파벳
      actvAdminpasswd.setPrivateImeOptions("defaultInputmode=english;");
      // 패스워드 - 영문~숫자만 입력받으며, 특수문자는 제한.
      actvAdminpasswd.setFilters(new android.text.InputFilter[]{InputFilter.filterAlphaNum});

      // 패스워드 재입력 - 입력되는 값을 비밀번호 입력같이 * 표시로 바꿀수 있다.
      actvAdminpasswd2.setTransformationMethod(ptmPassword);
      // 패스워드 재입력 - 초기 입력은 알파벳
      actvAdminpasswd2.setPrivateImeOptions("defaultInputmode=english;");
      // 패스워드 재입력 - 영문~숫자만 입력받으며, 특수문자는 제한.
      actvAdminpasswd2.setFilters(new android.text.InputFilter[]{InputFilter.filterAlphaNum});


      btnlvRegNext.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          actvAdminid.setError(null);
          actvAdminpasswd.setError(null);
          actvAdminpasswd2.setError(null);

          //String id = "TEST";
          String pw = "12345678";
          String pw2 = "12345678";


          String id = actvAdminid.getText().toString();
          //String pw = actvAdminpasswd.getText().toString();
          //String pw2 = actvAdminpasswd2.getText().toString().trim();
          // 유효 입력 검사.
          if (TextUtils.isEmpty(id)) {
            actvAdminid.setError(getString(R.string.error_field_required));
          } else if (TextUtils.isEmpty(pw)) {
            actvAdminpasswd.setError(getString(R.string.error_field_required));
          } else if (TextUtils.isEmpty(pw2)) {
            actvAdminpasswd2.setError(getString(R.string.error_field_required));
          } else if (id.length() < 3) {
            actvAdminid.setError(getString(R.string.error_short_length));
          } else if (pw.length() < 8) {
            actvAdminpasswd.setError(getString(R.string.error_invalid_password));
          } else if (pw2.length() < 8) {
            actvAdminpasswd2.setError(getString(R.string.error_invalid_password));
          } else if (!pw.equals(pw2)) {
            actvAdminpasswd2.setError(getString(R.string.error_incorrect_password));
          } else if (id.length() > 20) {
            actvAdminid.setError(getString(R.string.error_long_length));
          } else if (pw.length() > 20) {
            actvAdminpasswd.setError(getString(R.string.error_long_length));
          } else if (pw2.length() > 20) {
            actvAdminpasswd2.setError(getString(R.string.error_long_length));
          } else {


            /*
            네트워크연결을 확인 후 아이디 중복체크를 수행한다.
            중복된 아이디가 없을 경우 다음 엑티비티로 이동
            */
            if (CommonUtils.isNetworkAvailable(appcActivity)) {

              JsonHttpUserFunctions jsonHttpUserFunctions = new JsonHttpUserFunctions();
              Log.i("id", id);
              JSONObject jsonObjectCheckedIfExistId = jsonHttpUserFunctions.checkIfExistId(id);

              // 아이디 중복 체크
              try {
                if (jsonObjectCheckedIfExistId.getString(JSON_KEY_SUCCESS) != null) {

                  String res = jsonObjectCheckedIfExistId.getString(JSON_KEY_SUCCESS);

                  switch (Integer.parseInt(res)) {
                    case 0:
                      // 엑티비티 이동
                      Intent intent = new Intent(getApplicationContext(), RegisterEmailActivity.class);
                      // Close all views before launching
                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      intent.putExtra("ADMINID", id);
                      intent.putExtra("ADMINPASSWORD", pw);
                      startActivity(intent);
                      // 액티비비티 에니메이션
                      overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                      break;

                    default:
                      String eTitle = appcActivity.getResources().getString(R.string.mem_regist);
                      String eMessage = appcActivity.getResources().getString(R.string.error_id_exist);

                      CreateErrorDialog.createErrorDialog(appcActivity, eTitle, eMessage);
                      break;
                  }

                }

              } catch (JSONException error) {
                error.printStackTrace();
              }

            } else {

              String eTitle = appcActivity.getResources().getString(R.string.mem_regist);
              String eMessage = appcActivity.getResources().getString(R.string.error_network_problem);
              CreateErrorDialog.createErrorDialog(appcActivity, eTitle, eMessage);
            }

          }


        }
      });


    } else {
      /*
      Intent intent = new Intent(RegisterIdPasswordActivity.this,
                    com.appmaker.gcmtest.adminlogin.DashboardRegActivity.class);
      startActivity(intent);
      finish();
      */
    }


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

}
