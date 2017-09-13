package com.ilovecat.happytogether.RegisterMyLocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.CommonUIOfTopBar;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.GoBackAction;

import java.util.ArrayList;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 09. happytogether
 */
public class RegisterMyLocationActivity extends AppCompatActivity {

  //static AppCompatActivity appCompatActivity;

  static ArrayList<String> arraybuild = new ArrayList<String>();

  static EditText etMyLocation;

  static EditText etMyLocationAdress;
  static EditText etMyLocationAdressSiGun;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.register_mylocation_layout);

    final AppCompatActivity appCompatActivity = this;

    // 상단바 공통 UI
    CommonUIOfTopBar.setCommonUI(this);

    // 상단좌측 뒤로가기
    GoBackAction.goBackAndDoAnimation(this);





    etMyLocationAdress = (EditText) findViewById(R.id.etMyLocation);
    /*
     * xml 레이아웃에 추가해야 함.
     * android:focusable="false"
     */
    etMyLocationAdress.setInputType(0);
    // 클릭 리스너
    etMyLocationAdress.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View arg0) {



        etMyLocationAdress.setError(null);

        Intent dashboard = new Intent(getApplicationContext(), RegisterMyLocationSearchActivity.class);
        startActivity(dashboard);
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


      }
    });

    etMyLocationAdressSiGun = (EditText) findViewById(R.id.etMyLocationSiGun);



    Button btnlvRegNext = (Button) findViewById(R.id.reg_next_btn_MyLocation);

    btnlvRegNext.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        etMyLocationAdress.setError(null);


        String myLocation = etMyLocationAdress.getText().toString();
        String myLocationSiGun = etMyLocationAdressSiGun.getText().toString();

        // 유효 입력 검사.
        if (TextUtils.isEmpty(myLocation)) {
          etMyLocationAdress.setError(getString(R.string.error_field_required));
        } else {


          CreateSelectRegisterShopOrDoneDialog.createSelectRegisterShopOrDoneDialog(appCompatActivity, myLocation, myLocationSiGun);

          /*
          // 엑티비티 이동
          Intent intent = new Intent(getApplicationContext(), RegisterEmailActivity.class);
          // Close all views before launching
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(intent);
          // 액티비비티 에니메이션
          overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
          */

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
