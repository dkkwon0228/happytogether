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

package com.ilovecat.happytogether.MainShopRegisterCoupon;

import static com.ilovecat.happytogether.CommonConstantsAndUtils.CommonConstants.DB_LOGTAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ilovecat.happytogether.MainShopListingCoupon.CouponShopListingActivity;
import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.Databasehandler.DatabaseHandler;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.CommonUIOfTopBar;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.GoBackAction;

import java.io.File;


/**
 * <pre>
 * 1. 패키지명 : com.appmaker.gcmtest.adminlogin
 * 2. 타입명 :  RegisterIdPasswordActivity In RegisterIdPasswordActivity.java
 * 3. 작성일 : 2016. 5. 9. 오전 12:02:47
 * 4. 작성자 : dannykwon
 * 5. 설명 :
 * </pre>
 */
public class MainRegisterCouponShopStep3 extends AppCompatActivity {

  public static AppCompatActivity appcActivity;
  public static Context appcContext;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_register_coupon_shop_step3_layout);

    appcActivity = this;
    appcContext = getApplicationContext();

    // 상단바 공통 UI
    CommonUIOfTopBar.setCommonUI(this);
    GoBackAction.goBackAndDoAnimation(this);


    Intent getedIntent = getIntent();

    final String strFullPathCapturedImage = getedIntent.getExtras().getString("FULL_PATH_CAPTURED_IMAGE");

    final String strCouponTitle = getedIntent.getExtras().getString("COUPONTITLE");
    final String strCouponEndDate = getedIntent.getExtras().getString("COUPONENDDATE");
    final String strCouponNotice = getedIntent.getExtras().getString("COUPONNOTICE");

    final String strCouponNormalPrice = getedIntent.getExtras().getString("COUPONNORMALPRICE");
    final String strCouponSalePrice = getedIntent.getExtras().getString("COUPONSALEPRICE");
    final String strCouponDescription = getedIntent.getExtras().getString("COUPONDESCRIPTION");



    String capturedFileName[] = strFullPathCapturedImage.split("/");
    int size = capturedFileName.length;
    final String strCouponImageFileName = capturedFileName[size -1];


    File imgFile = new  File(strFullPathCapturedImage);

    if(imgFile.exists()){

      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inSampleSize = 1;
      Bitmap bitmapInTemp = BitmapFactory.decodeFile(strFullPathCapturedImage, options);
      //bitmapInTemp = ImageUtil.GetRotatedBitmap(bitmapInTemp, degree);

      ImageView ivCapturedImg  = (ImageView) findViewById(R.id.ivCouponImg);
      ivCapturedImg.setImageBitmap(bitmapInTemp);

    }





    final TextView tvCouponOpen = (TextView) findViewById(R.id.tvCouponOpen);
    tvCouponOpen.setText("비공개");

    final Button btnlvRegDone = (Button) findViewById(R.id.reg_done_button);

    ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButtonCouponOpen);

    toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {

        if(isChecked) {

          tvCouponOpen.setText(getString(R.string.register_coupon_nowopen));

        } else {

          tvCouponOpen.setText(getString(R.string.register_coupon_private));
        }

      }
    });

    final String strCouponNumber = "3213-2311-212343444";

    // DatabaseHandle In databasehandler package
    DatabaseHandler handlerDatabase = new DatabaseHandler(this);
    int count = handlerDatabase.getRowCountAdminkey();
    Log.i(DB_LOGTAG, "ADMINKEY_COUNT in REGISTER = " + count);

    final String innerdbShopName = handlerDatabase.getShopName();
    handlerDatabase.close();

    if (android.os.Build.VERSION.SDK_INT > 9) {
      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
      StrictMode.setThreadPolicy(policy);
    }

    // 내부 DB의 adminkey_tb 테이블의 row 값이 0일 경우 (앱 관리자 등록이 안되어 있을 경우)
    if (count == 0) {


    } else {
      btnlvRegDone.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(final View view) {

          btnlvRegDone.setEnabled(false);


          final String strCouponOpenState;

          if( tvCouponOpen.getText().toString().equals(getString(R.string.register_coupon_nowopen))) {
            strCouponOpenState = "Y";
          } else {
            strCouponOpenState = "N";
          }

          Log.i("123", strCouponNumber);
          Log.i("123", innerdbShopName);

          Log.i("123", strCouponTitle);
          Log.i("123", strCouponEndDate);
          Log.i("123", strCouponNotice);
          Log.i("123", strCouponNormalPrice);
          Log.i("123", strCouponSalePrice);
          Log.i("123", strCouponDescription);
          Log.i("123", strCouponOpenState);


          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

              new AsyncTaskUpLoadCouponInfo_Step3(
                  strCouponNumber,
                  innerdbShopName,
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

              File file = new File(strFullPathCapturedImage);
              new AsyncTaskUpLoadFilesS3_Step3(file, strCouponOpenState).execute();

            }
          }, 3000);

          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

              Intent intent = new Intent(getApplicationContext(), CouponShopListingActivity.class);
              // Close all views before launching
              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

              if( tvCouponOpen.getText().toString().equals(getString(R.string.register_coupon_nowopen))) {
                intent.putExtra("CURRENTITEM", "0");
                } else {
                intent.putExtra("CURRENTITEM", "1");
                }

              startActivity(intent);
              // 액티비비티 에니메이션
              overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
              finish();

            }
          }, 5000);


        }
      });

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

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if(keyCode==KeyEvent.KEYCODE_BACK)
      Toast.makeText(getApplicationContext(), "업로드 중입니다.",
          Toast.LENGTH_LONG).show();

    return false;
    // Disable back button..............
  }

}
