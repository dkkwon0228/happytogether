package com.ilovecat.happytogether.RegisterShopLocation;

import com.google.android.gms.common.api.GoogleApiClient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.RegisterShopName.RegisterShopNameActivity;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.CommonUIOfTopBar;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.GoBackAction;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 09. happytogether
 */
public class RegisterShopLocationActivity extends AppCompatActivity {


  /**
   * ATTENTION: This was auto-generated to implement the App Indexing API.
   * See https://g.co/AppIndexing/AndroidStudio for more information.
   */
  private GoogleApiClient client;

  static EditText etShopLocation;
  static EditText etShopLocationSiGun;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.register_shoplocation_layout);

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



    etShopLocation = (EditText) findViewById(R.id.etShopLocation);
    /*
     * xml 레이아웃에 추가해야 함.
     * android:focusable="false"
     */
    etShopLocation.setInputType(0);
    // 클릭 리스너
    etShopLocation.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View arg0) {

        etShopLocation.setError(null);

        Intent dashboard = new Intent(getApplicationContext(), RegisterShopLocationSearchActivity.class);
        startActivity(dashboard);
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


      }
    });

    etShopLocationSiGun = (EditText) findViewById(R.id.etShopAddressSiGun);

    Button btnlvRegNext = (Button) findViewById(R.id.reg_next_btn_MyLocation);
    btnlvRegNext.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        etShopLocation.setError(null);


        String myLocation = etShopLocation.getText().toString();
        // 유효 입력 검사.
        if (TextUtils.isEmpty(myLocation)) {
          etShopLocation.setError(getString(R.string.error_field_required));

        } else {


          String strShopLocation = etShopLocation.getText().toString();
          String strShopLocationSiGun = etShopLocationSiGun.getText().toString();
          String[] arrShopLocationSiGun =  strShopLocationSiGun.split(" ");
          String strShopLocationSi = arrShopLocationSiGun[0];
          String strShopLocationGun = arrShopLocationSiGun[1];




          // 엑티비티 이동
          Intent intentMove = new Intent(appCompatActivity, RegisterShopNameActivity.class);
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


          startActivity(intentMove);
          // 액티비비티 에니메이션
          overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


        }


      }
    });

    EditText etMyLocation = (EditText) findViewById(R.id.etMyLocation);
    etMyLocation.setText(strMyLocation);

    EditText etMyLocationSiGun = (EditText) findViewById(R.id.etMyLocationSiGun);
    etMyLocationSiGun.setText(strMyLocationSiGun);

  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
  }


}
