package com.ilovecat.happytogether.RegisterAuthShopPhoneNumber;

import com.google.android.gcm.GCMRegistrar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ilovecat.happytogether.Main.MainActivity;
import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonConstants;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonUtils;
import com.ilovecat.happytogether.Databasehandler.DatabaseHandler;
import com.ilovecat.happytogether.Databasehandler.JsonHttpUserFunctions;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.CommonUIOfTopBar;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.GoBackAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 09. happytogether
 */
public class RegisterAuthShopPhoneNumberActivity extends AppCompatActivity {


  /**
   * ATTENTION: This was auto-generated to implement the App Indexing API. See
   * https://g.co/AppIndexing/AndroidStudio for more information.
   */
  private String gcmRegId;

  private TextView tvShopName;
  private TextView tvShopPhoneNumber;
  TextView tvConfirmIncommingCallNumber = null;

  private Button btnlvRegNext;

  private String strAdminId;
  private String strAdminPassword;
  private String strAdminEmail;
  private String strMyLocation;
  private String strMyLocationSiGun;
  private String strMyLocationSi;
  private String strMyLocationGun;
  private String strShopLocation;
  private String strShopLocationSiGun;
  private String strShopLocationSi;
  private String strShopLocationGun;
  private String strShopName;
  private String strBizAreaDivisionName;
  private String strBizAreaSectionName;
  private String strShopJibunAddress;
  private String strShopRoadAddress;
  private String strShopPhoneNumber;
  private String strShopLongitude;
  private String strShopLatitude;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.register_auth_shopphonenumber_layout);


    // 상단바 공통 UI
    CommonUIOfTopBar.setCommonUI(this);

    // 상단좌측 뒤로가기
    GoBackAction.goBackAndDoAnimation(this);

    Intent intent = getIntent();
    strAdminId = intent.getExtras().getString("ADMINID");
    strAdminPassword = intent.getExtras().getString("ADMINPASSWORD");
    strAdminEmail = intent.getExtras().getString("ADMINEMAIL");

    strMyLocation = intent.getExtras().getString("MYLOCATION");
    strMyLocationSiGun = intent.getExtras().getString("MYLOCATIONSIGUN");
    strMyLocationSi = intent.getExtras().getString("MYLOCATIONSI");
    strMyLocationGun = intent.getExtras().getString("MYLOCATIONGUN");

    strShopLocation = intent.getExtras().getString("SHOPLOCATION");
    strShopLocationSiGun = intent.getExtras().getString("SHOPLOCATIONSIGUN");
    strShopLocationSi = intent.getExtras().getString("SHOPLOCATIONSI");
    strShopLocationGun = intent.getExtras().getString("SHOPLOCATIONGUN");

    strShopName = intent.getExtras().getString("SHOPNAME");

    strBizAreaDivisionName = intent.getExtras().getString("SHOPBIZAREADIVISIONNAME");
    strBizAreaSectionName = intent.getExtras().getString("SHOPBIZAREASECTIONNAME");

    strShopJibunAddress = intent.getExtras().getString("SHOPJIBUNADDRESS");
    strShopRoadAddress = intent.getExtras().getString("SHOPROADADDRESS");

    strShopPhoneNumber = intent.getExtras().getString("SHOPPHONENUMBER");
    strShopLongitude = intent.getExtras().getString("SHOPLONGITUDE");
    strShopLatitude = intent.getExtras().getString("SHOPLATITUDE");



    Log.i("mylocation", strMyLocation);
    Log.i("mylocationSiGun", strMyLocationSiGun);
    Log.i("mylocationSi", strMyLocationSi);
    Log.i("mylocationGun", strMyLocationGun);


    Log.i("shoplocation", strShopLocation);
    Log.i("shoplocationSiGun", strShopLocationSiGun);
    Log.i("shoplocationSi", strShopLocationSi);
    Log.i("shoplocationGun", strShopLocationGun);

    tvShopName = (TextView) findViewById(R.id.tvShopName);
    tvShopName.setText(strShopName);

    tvShopPhoneNumber = (TextView) findViewById(R.id.tvShopPhoneNumber);
    tvShopPhoneNumber.setText(strShopPhoneNumber);

    tvConfirmIncommingCallNumber = (TextView) findViewById(R.id.tvConfirmIncommingCallNumber);


    btnlvRegNext = (Button) findViewById(R.id.reg_next_btn_MyLocation);

    // 임시로 주석 처리
    //btnlvRegNext.setEnabled(false);


    tvConfirmIncommingCallNumber.setText(R.string.ment_done_auth_phone);
    btnlvRegNext.setEnabled(true);
    btnlvRegNext.setText(R.string.action_done_auth_phone);

    btnlvRegNext.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        String strRandomAdminKey = CommonUtils.getRandomKey(10);
        Log.d("RandomKey", strRandomAdminKey);
        Log.i("sds", strRandomAdminKey);
        Log.i("sds", strAdminId);
        Log.i("sds", strAdminPassword);
        Log.i("sds", strAdminEmail);
        Log.i("sds", strMyLocation);
        Log.i("sds", strShopName);
        Log.i("sds", strBizAreaDivisionName);
        Log.i("sds", strBizAreaSectionName);
        Log.i("sds", strShopLocationSi);
        Log.i("sds", strShopLocationGun);
        Log.i("sds", strShopLocation);


        Log.i("sds", strShopJibunAddress);
        Log.i("sds", strShopRoadAddress);
        Log.i("sds", strShopPhoneNumber);
        Log.i("sds", strShopLongitude);
        Log.i("sds", strShopLatitude);

        JsonHttpUserFunctions jsonHttpUserFunctions = new JsonHttpUserFunctions();
        JSONObject json = jsonHttpUserFunctions.registerUser(
            strRandomAdminKey,
            strAdminId,
            strAdminPassword,
            strAdminEmail,
            strMyLocationSi,
            strMyLocationGun,
            strMyLocation,
            strShopName,
            strBizAreaDivisionName,
            strBizAreaSectionName,
            strShopLocationSi,
            strShopLocationGun,
            strShopLocation,
            strShopJibunAddress,
            strShopRoadAddress,
            strShopPhoneNumber,
            strShopLongitude,
            strShopLatitude
        );


        // check for login response
        try {
          if (json.getString(CommonConstants.JSON_KEY_SUCCESS) != null) {
            // registerErrorMsg.setText("");
            String res = json.getString(CommonConstants.JSON_KEY_SUCCESS);
            if (Integer.parseInt(res) == 1) {

              Toast.makeText(getApplicationContext(), "정상적으로 등록 되었습니다", Toast.LENGTH_LONG).show();

              // user successfully registred
              // Store user details in SQLite Database
              DatabaseHandler db = new DatabaseHandler(getApplicationContext());
              JSONObject json_user = json.getJSONObject("userInfo");

              // Clear all previous data in database
              // 앱을 지우고 다시 설치할 수도 있기 때문에 이전 Adminkey 관련
              // 데이타를 지운다.
              JsonHttpUserFunctions.logoutUser(getApplicationContext());

              String xmlAdminid = json_user.getString(CommonConstants.JSON_KEY_ADMINID);

              // 관리자키 내부디비에 저장
              db.regUser(
                  json_user.getString(CommonConstants.JSON_KEY_ADMINKEY),
                  json_user.getString(CommonConstants.JSON_KEY_ADMINID),
                  json_user.getString(CommonConstants.JSON_KEY_ADMINPASSWD),
                  json_user.getString(CommonConstants.JSON_KEY_ADMINEMAIL),
                  json_user.getString(CommonConstants.JSON_KEY_SHOPNAME),
                  // json.getString(KEY_UID),
                  json_user.getString(CommonConstants.JSON_KEY_CREATED_AT));

              db.addAdminkey(
                  json_user.getString(CommonConstants.JSON_KEY_ADMINKEY),
                  json_user.getString(CommonConstants.JSON_KEY_ADMINID),
                  json_user.getString(CommonConstants.JSON_KEY_ADMINPASSWD),
                  json_user.getString(CommonConstants.JSON_KEY_ADMINEMAIL),
                  CommonConstants.KEY_LOGINSTATE_LOGIN,
                  json_user.getString(CommonConstants.JSON_KEY_SHOPNAME),
                  json_user.getString(CommonConstants.JSON_KEY_CREATED_AT));
              db.close();

              String str = Environment.getExternalStorageState();
              if (str.equals(Environment.MEDIA_MOUNTED)) {

                String dirPath = CommonConstants.INNER_PHOTO_TEMP_PATH;
                File file = new File(dirPath);
                if (!file.exists()) // 원하는 경로에 폴더가 있는지
                  // 확인
                  file.mkdirs(); // 상위폴더까지 생성

                String dirPath2 = CommonConstants.INNER_SHOPIMAGES_PATH;
                File file2 = new File(dirPath2);
                if (!file2.exists()) // 원하는 경로에 폴더가 있는지
                  // 확인
                  file2.mkdirs(); // 상위폴더까지 생성

              } else {
                Toast.makeText(RegisterAuthShopPhoneNumberActivity.this, "SD Card 인식 실패", Toast.LENGTH_SHORT)
                    .show();
              }

              // 디렉토리 생성
              File dirxml = CommonUtils.makeDirectory(CommonConstants.INNER_XML_PATH + xmlAdminid);
              // 파일 생성
              CommonUtils.makeFile(dirxml, (CommonConstants.INNER_XML_PATH + xmlAdminid + "/" + xmlAdminid + ".xml"));

              // 15/12/26
              registerGcm();


              // 엑티비티 이동
              Intent intentMove = new Intent(getApplicationContext(), MainActivity.class);
              // Close all views before launching
              intentMove.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    /*

                    intentMove.putExtra("ADMINID", strAdminId);
                    intentMove.putExtra("ADMINPASSWORD", strAdminPassword);
                    intentMove.putExtra("ADMINEMAIL", strAdminEmail);

                    intentMove.putExtra("MYLOCATION", strMyLocation);
                    intentMove.putExtra("MYLOCATIONSIGUN", strMyLocationSiGun);
                    intentMove.putExtra("MYLOCATIONSI", strMyLocationSi);
                    intentMove.putExtra("MYLOCATIONGUN", strMyLocationGun);

                    intentMove.putExtra("SHOPLOCATION", strShopLocation);
                    intentMove.putExtra("SHOPLOCATIONSIGUN", strShopLocationSiGun);
                    intentMove.putExtra("SHOPLOCATIONSI", strShopLocationSi);
                    intentMove.putExtra("SHOPLOCATIONGUN", strShopLocationGun);

                    intentMove.putExtra("SHOPNAME", strShopName);
                    intentMove.putExtra("SHOPBIZAREADIVISIONNAME", strBizAreaDivisionName);
                    intentMove.putExtra("SHOPBIZAREASECTIONNAME", strBizAreaSectionName);

                    intentMove.putExtra("SHOPJIBUNADDRESS", strShopJibunAddress);
                    intentMove.putExtra("SHOPROADADDRESS", strShopRoadAddress);

                    intentMove.putExtra("SHOPHONENUMBER", strNewShopPhoneNumber);
                    intentMove.putExtra("SHOPLONGITUDE", strShopLongitude);
                    intentMove.putExtra("SHOPLATITUE", strShopLatitude);
                    */
              startActivity(intentMove);
              // 액티비비티 에니메이션
              overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


            } else {
              // Error in registration
              // registerErrorMsg.setText("Error occured
              // in registration");
              Toast.makeText(getApplicationContext(), "이미 이용중인  관리자 ID가 있습니다", Toast.LENGTH_LONG)
                  .show();
            }
          }

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    });



    /*
    개발중 임시로 폰 인증 생략
    PhoneCallListener phoneListener = new PhoneCallListener();
    TelephonyManager telephonyManager = (TelephonyManager) this
        .getSystemService(Context.TELEPHONY_SERVICE);
    telephonyManager.listen(phoneListener,
        PhoneStateListener.LISTEN_CALL_STATE);
    */
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
  }


  private class PhoneCallListener extends PhoneStateListener {

    private boolean isPhoneCalling = false;

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {

      if (TelephonyManager.CALL_STATE_RINGING == state) {
        // phone ringing
        Log.i("LOG_TAG", "RINGING, number: " + incomingNumber);

        // 인텐트로 넘겨받은 전화번호를 분리한다.
        String[] arrShopPhoneNumber = strShopPhoneNumber.split("-");
        final String strNewShopPhoneNumber = arrShopPhoneNumber[0] + arrShopPhoneNumber[1] + arrShopPhoneNumber[2];

        String strTestNumber = "07041001628";
        //if (incommingNumber.equals(strNewShopPhoneNumber)) {
        if (strTestNumber.equals(strNewShopPhoneNumber)) {

          //CreateErrorDialog.createErrorDialog(appCompatActivity,"전화번호 확인","번호 확인");

          tvConfirmIncommingCallNumber.setText(R.string.ment_done_auth_phone);
          btnlvRegNext.setEnabled(true);
          btnlvRegNext.setText(R.string.action_done_auth_phone);

          btnlvRegNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              String strRandomAdminKey = CommonUtils.getRandomKey(10);
              Log.d("RandomKey", strRandomAdminKey);
              Log.i("sds", strRandomAdminKey);
              Log.i("sds", strAdminId);
              Log.i("sds", strAdminPassword);
              Log.i("sds", strAdminEmail);
              Log.i("sds", strMyLocation);
              Log.i("sds", strShopName);
              Log.i("sds", strBizAreaDivisionName);
              Log.i("sds", strBizAreaSectionName);
              Log.i("sds", strShopLocationSi);
              Log.i("sds", strShopLocationGun);
              Log.i("sds", strShopLocation);


              Log.i("sds", strShopJibunAddress);
              Log.i("sds", strShopRoadAddress);
              Log.i("sds", strShopPhoneNumber);
              Log.i("sds", strShopLongitude);
              Log.i("sds", strShopLatitude);

              JsonHttpUserFunctions jsonHttpUserFunctions = new JsonHttpUserFunctions();
              JSONObject json = jsonHttpUserFunctions.registerUser(
                  strRandomAdminKey,
                  strAdminId,
                  strAdminPassword,
                  strAdminEmail,
                  strMyLocationSi,
                  strMyLocationGun,
                  strMyLocation,
                  strShopName,
                  strBizAreaDivisionName,
                  strBizAreaSectionName,
                  strShopLocationSi,
                  strShopLocationGun,
                  strShopLocation,
                  strShopJibunAddress,
                  strShopRoadAddress,
                  strShopPhoneNumber,
                  strShopLongitude,
                  strShopLatitude
              );


              // check for login response
              try {
                if (json.getString(CommonConstants.JSON_KEY_SUCCESS) != null) {
                  // registerErrorMsg.setText("");
                  String res = json.getString(CommonConstants.JSON_KEY_SUCCESS);
                  if (Integer.parseInt(res) == 1) {

                    Toast.makeText(getApplicationContext(), "정상적으로 등록 되었습니다", Toast.LENGTH_LONG).show();

                    // user successfully registred
                    // Store user details in SQLite Database
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    JSONObject json_user = json.getJSONObject("userInfo");

                    // Clear all previous data in database
                    // 앱을 지우고 다시 설치할 수도 있기 때문에 이전 Adminkey 관련
                    // 데이타를 지운다.
                    JsonHttpUserFunctions.logoutUser(getApplicationContext());

                    String xmlAdminid = json_user.getString(CommonConstants.JSON_KEY_ADMINID);

                    // 관리자키 내부디비에 저장
                    db.regUser(
                        json_user.getString(CommonConstants.JSON_KEY_ADMINKEY),
                        json_user.getString(CommonConstants.JSON_KEY_ADMINID),
                        json_user.getString(CommonConstants.JSON_KEY_ADMINPASSWD),
                        json_user.getString(CommonConstants.JSON_KEY_ADMINEMAIL),
                        json_user.getString(CommonConstants.JSON_KEY_SHOPNAME),
                        // json.getString(KEY_UID),
                        json_user.getString(CommonConstants.JSON_KEY_CREATED_AT));

                    db.addAdminkey(
                        json_user.getString(CommonConstants.JSON_KEY_ADMINKEY),
                        json_user.getString(CommonConstants.JSON_KEY_ADMINID),
                        json_user.getString(CommonConstants.JSON_KEY_ADMINPASSWD),
                        json_user.getString(CommonConstants.JSON_KEY_ADMINEMAIL),
                        CommonConstants.KEY_LOGINSTATE_LOGIN,
                        json_user.getString(CommonConstants.JSON_KEY_SHOPNAME),
                        json_user.getString(CommonConstants.JSON_KEY_CREATED_AT));
                    db.close();

                    String str = Environment.getExternalStorageState();
                    if (str.equals(Environment.MEDIA_MOUNTED)) {

                      String dirPath = CommonConstants.INNER_PHOTO_TEMP_PATH;
                      File file = new File(dirPath);
                      if (!file.exists()) // 원하는 경로에 폴더가 있는지
                        // 확인
                        file.mkdirs(); // 상위폴더까지 생성

                      String dirPath2 = CommonConstants.INNER_SHOPIMAGES_PATH;
                      File file2 = new File(dirPath2);
                      if (!file2.exists()) // 원하는 경로에 폴더가 있는지
                        // 확인
                        file2.mkdirs(); // 상위폴더까지 생성

                    } else {
                      Toast.makeText(RegisterAuthShopPhoneNumberActivity.this, "SD Card 인식 실패", Toast.LENGTH_SHORT)
                          .show();
                    }

                    // 디렉토리 생성
                    File dirxml = CommonUtils.makeDirectory(CommonConstants.INNER_XML_PATH + xmlAdminid);
                    // 파일 생성
                    CommonUtils.makeFile(dirxml, (CommonConstants.INNER_XML_PATH + xmlAdminid + "/" + xmlAdminid + ".xml"));

                    // 15/12/26
                    registerGcm();


                    // 엑티비티 이동
                    Intent intentMove = new Intent(getApplicationContext(), MainActivity.class);
                    // Close all views before launching
                    intentMove.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    /*
                    intentMove.putExtra("ADMINID", strAdminId);
                    intentMove.putExtra("ADMINPASSWORD", strAdminPassword);
                    intentMove.putExtra("ADMINEMAIL", strAdminEmail);

                    intentMove.putExtra("MYLOCATION", strMyLocation);
                    intentMove.putExtra("MYLOCATIONSIGUN", strMyLocationSiGun);
                    intentMove.putExtra("MYLOCATIONSI", strMyLocationSi);
                    intentMove.putExtra("MYLOCATIONGUN", strMyLocationGun);

                    intentMove.putExtra("SHOPLOCATION", strShopLocation);
                    intentMove.putExtra("SHOPLOCATIONSIGUN", strShopLocationSiGun);
                    intentMove.putExtra("SHOPLOCATIONSI", strShopLocationSi);
                    intentMove.putExtra("SHOPLOCATIONGUN", strShopLocationGun);

                    intentMove.putExtra("SHOPNAME", strShopName);
                    intentMove.putExtra("SHOPBIZAREADIVISIONNAME", strBizAreaDivisionName);
                    intentMove.putExtra("SHOPBIZAREASECTIONNAME", strBizAreaSectionName);

                    intentMove.putExtra("SHOPJIBUNADDRESS", strShopJibunAddress);
                    intentMove.putExtra("SHOPROADADDRESS", strShopRoadAddress);

                    intentMove.putExtra("SHOPHONENUMBER", strNewShopPhoneNumber);
                    intentMove.putExtra("SHOPLONGITUDE", strShopLongitude);
                    intentMove.putExtra("SHOPLATITUE", strShopLatitude);
                    */

                    startActivity(intentMove);
                    // 액티비비티 에니메이션
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                  } else {
                    // Error in registration
                    // registerErrorMsg.setText("Error occured
                    // in registration");
                    Toast.makeText(getApplicationContext(), "이미 이용중인  관리자 ID가 있습니다", Toast.LENGTH_LONG)
                        .show();
                  }
                }

              } catch (JSONException e) {
                e.printStackTrace();
              }

            }
          });

        } else {

          //CreateErrorDialog.createErrorDialog(appCompatActivity,"전화번호 확인","번호 미확인");

          tvConfirmIncommingCallNumber.setText(R.string.ment_require_auth_phone);
          btnlvRegNext.setEnabled(false);
          btnlvRegNext.setText(R.string.action_require_auth_phone);

        }


        if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
          // active
          Log.i("LOG_TAG", "OFFHOOK");

          isPhoneCalling = true;
        }

        if (TelephonyManager.CALL_STATE_IDLE == state) {
          // run when class initial and phone call ended, need detect flag
          // from CALL_STATE_OFFHOOK
          Log.i("LOG_TAG", "IDLE number");

          if (isPhoneCalling) {

            Handler handler = new Handler();

            //Put in delay because call log is not updated immediately when state changed
            // The dialler takes a little bit of time to write to it 500ms seems to be enough
            handler.postDelayed(new Runnable() {

              @Override
              public void run() {
                // get start of cursor
                Log.i("CallLogDetailsActivity", "Getting Log activity...");
                String[] projection = new String[]{CallLog.Calls.NUMBER};
                Cursor cur = getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, CallLog.Calls.DATE + " desc");
                cur.moveToFirst();
                String lastCallnumber = cur.getString(0);
              }
            }, 500);

            isPhoneCalling = false;
          }

        }
      }
    }


  }

  // 푸시
  public void registerGcm() {

    /*
    * 1.GCM Service가 이용 가능한 Device인지 체크한다. api 8(Android 2.2) 미만인 경우나
    * GCMService를 이용할 수 없는 디바이스의 경우 오류를 발생시키니 반드시 예외처리하도록 한다.
    */
    try {
      GCMRegistrar.checkDevice(this);
      GCMRegistrar.checkManifest(this);
    } catch (Exception e) {
      // TODO: handle exception
      Log.e(CommonConstants.GCM_LOGTAG, "This device can't use GCM");
      return;
    }



    /*
     * 2.SharedPreference에 저장된 RegistrationID가 있는지 확인한다. 없는 경우 null이 아닌 ""이 리턴
     */
    gcmRegId = GCMRegistrar.getRegistrationId(this);
    Log.e("Geted reg_id", "Geted reg_id ok");
    Log.e("reg_id is", gcmRegId);
    if (gcmRegId.equals("")) {

    /*
    * 3.RegstrationId가 없는 경우 GCM Server로 Regsitration ID를 발급 요청한다. 발급
    * 요청이 정상적으로 이루어진 경우 Registration ID는 SharedPreference에 저장되며,
    * GCMIntentService.class의 onRegistered를 콜백한다.
    */
      GCMRegistrar.register(this, CommonConstants.GCM_SENDER_ID);
      Log.e("if reg_id equals null ", gcmRegId);
      //SharedPreference에 저장된 Registration Id가 존재하는 경
    } else {
      Log.e(CommonConstants.GCM_LOGTAG, "if reg_id equals not null " + gcmRegId);
    }

  }

  @Override
  protected void onDestroy() {
    /*
    * 4.앱 종료되기 전이나 종료하기 전에 GCMRegistrar.onDestroy를 반드시 호출한다. 호출하지 않을 경우
    * unRegisterReceiver오류가 발생한다. 해당 함수는 null이나 기타 오류에 대해 내부적으로 예외 처리하고
    * 있으므로, 아무때나 마음껏 호출해도 된다.
    */

    // RegisterActivity에서 DashboardActivuity로 넘어갈 때 에러가 발생하여 주석처리함
    // 2014/10/14
    // GCMRegistrar.onDestroy(this);
    super.onDestroy();
  }

}
