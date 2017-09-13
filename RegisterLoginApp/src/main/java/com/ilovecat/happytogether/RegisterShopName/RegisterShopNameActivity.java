package com.ilovecat.happytogether.RegisterShopName;

import com.google.android.gms.common.api.GoogleApiClient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CreateErrorDialog;
import com.ilovecat.happytogether.RegisterAuthShopPhoneNumber.RegisterAuthShopPhoneNumberActivity;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.CommonUIOfTopBar;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.GoBackAction;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 09. happytogether
 */
public class RegisterShopNameActivity extends AppCompatActivity {


  /**
   * ATTENTION: This was auto-generated to implement the App Indexing API.
   * See https://g.co/AppIndexing/AndroidStudio for more information.
   */
  private GoogleApiClient client;

  static EditText etShopName;
  static EditText etShopJibunAddress;
  static EditText etShopPhoneNumber;
  static EditText etShopBizAreaSectionName;

  static String strShopBizAreaDivisionName;
  static String strShopRoadAddress;
  static String strLongitude;
  static String strLatitude;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.register_shopname_layout);

    final AppCompatActivity appCompatActivity = this;

    // 상단바 공통 UI
    CommonUIOfTopBar.setCommonUI(this);

    // 상단좌측 뒤로가기
    GoBackAction.goBackAndDoAnimation(this);



    Intent intent = getIntent();
    final String strAdminId = intent.getExtras().getString("ADMINID");
    final String strAdminPassword = intent.getExtras().getString("ADMINPASSWORD");
    final String strAdminEmail = intent.getExtras().getString("ADMINEMAIL");

    final String strMyLocation = intent.getExtras().getString("MYLOCATION");
    final String strMyLocationSiGun = intent.getExtras().getString("MYLOCATIONSIGUN");
    final String strMyLocationSi = intent.getExtras().getString("MYLOCATIONSI");
    final String strMyLocationGun = intent.getExtras().getString("MYLOCATIONGUN");

    final String strShopLocation = intent.getExtras().getString("SHOPLOCATION");
    final String strShopLocationSiGun = intent.getExtras().getString("SHOPLOCATIONSIGUN");
    final String strShopLocationSi = intent.getExtras().getString("SHOPLOCATIONSI");
    final String strShopLocationGun = intent.getExtras().getString("SHOPLOCATIONGUN");


    etShopName = (EditText) findViewById(R.id.etShopName);
    /*
     * xml 레이아웃에 추가해야 함.
     * android:focusable="false"
     */
    etShopName.setInputType(0);
    // 클릭 리스너
    etShopName.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View arg0) {

        etShopName.setError(null);
        etShopPhoneNumber.setError(null);


        Intent dashboard = new Intent(getApplicationContext(), RegisterShopNameSearchActivity.class);
        dashboard.putExtra("SHOPLOCATION", strShopLocation);
        dashboard.putExtra("SHOPLOCATIONSI", strShopLocationSi);
        dashboard.putExtra("SHOPLOCATIONGUN", strShopLocationGun);
        startActivity(dashboard);

      }
    });

    etShopJibunAddress = (EditText) findViewById(R.id.etShopAddress);
    etShopPhoneNumber = (EditText) findViewById(R.id.etShopPhoneNumber);
    etShopBizAreaSectionName = (EditText) findViewById(R.id.etShopBizArea);

    Button btnlvRegNext = (Button) findViewById(R.id.reg_next_btn_MyLocation);

    btnlvRegNext.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        etShopName.setError(null);
        etShopPhoneNumber.setError(null);


        String shopName = etShopName.getText().toString();
        String shopPhoneNumber = etShopPhoneNumber.getText().toString();
        String errorString = getString(R.string.error_empty_shop_phonenumber);

        // 유효 입력 검사.
        if (TextUtils.isEmpty(shopName)) {
          etShopName.setError(getString(R.string.error_field_required));

        } else if (shopPhoneNumber.equals(errorString)) {



          String titleEmptyShopPhoneNumberPopUpDialog = getResources().getString(R.string.title_empty_shopphonenumber);
          String ment = getResources().getString(R.string.ment_empty_shopphonenumber);
          CreateErrorDialog.createErrorDialog(appCompatActivity,
              titleEmptyShopPhoneNumberPopUpDialog,
              ment);

          etShopPhoneNumber.setError(getString(R.string.error_empty_shop_phonenumber));


        } else {


          String strShopName = etShopName.getText().toString();
          String strShopJibunAddress = etShopJibunAddress.getText().toString();
          String strShopPhoneNumber = etShopPhoneNumber.getText().toString();
          String strShopBizAreaSectionName = etShopBizAreaSectionName.getText().toString();

          Log.i("sds", strAdminId);
          Log.i("sds", strAdminPassword);
          Log.i("sds", strAdminEmail);
          Log.i("sds", strMyLocation);


          Log.i("sds", strShopName);
          Log.i("sds", strShopBizAreaDivisionName);
          Log.i("sds", strShopBizAreaSectionName);

          Log.i("sds", strShopLocationSi);
          Log.i("sds", strShopLocationGun);
          Log.i("sds", strShopLocation);


          Log.i("sds", strShopJibunAddress);
          Log.i("sds", strShopRoadAddress);
          Log.i("sds", strShopPhoneNumber);
          Log.i("sds", strLongitude);
          Log.i("sds", strLatitude);






          // 엑티비티 이동
          Intent intentMove = new Intent(appCompatActivity, RegisterAuthShopPhoneNumberActivity.class);
          // Close all views before launching
          intentMove.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

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

          intentMove.putExtra("SHOPBIZAREADIVISIONNAME", strShopBizAreaDivisionName);
          intentMove.putExtra("SHOPBIZAREASECTIONNAME", strShopBizAreaSectionName);


          intentMove.putExtra("SHOPJIBUNADDRESS", strShopJibunAddress);
          intentMove.putExtra("SHOPROADADDRESS", strShopRoadAddress);

          intentMove.putExtra("SHOPPHONENUMBER", strShopPhoneNumber);

          intentMove.putExtra("SHOPLONGITUDE", strLongitude);
          intentMove.putExtra("SHOPLATITUDE", strLatitude);


          startActivity(intentMove);
          // 액티비비티 에니메이션
          overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


        }


      }
    });

  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
  }



}
