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

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
public class MainRegisterCouponShopStep2 extends AppCompatActivity {


  private String strFullPathCapturedImage;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_register_coupon_shop_step2_layout);

    final AppCompatActivity appcActivity = this;


    // 상단바 공통 UI
    CommonUIOfTopBar.setCommonUI(this);
    GoBackAction.goBackAndDoAnimation(this);

    Intent getedIntent = getIntent();

    final String strCouponTitle = getedIntent.getExtras().getString("COUPONTITLE");
    final String strCouponEndDate = getedIntent.getExtras().getString("COUPONENDDATE");
    final String strCouponNotice = getedIntent.getExtras().getString("COUPONNOTICE");
    strFullPathCapturedImage = getedIntent.getExtras().getString("FULL_PATH_CAPTURED_IMAGE");

    Log.i("111", strFullPathCapturedImage);


    File imgFile = new  File(strFullPathCapturedImage);

    if(imgFile.exists()){


      Log.i("222", strFullPathCapturedImage);



      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inSampleSize = 1;
      Bitmap bitmapInTemp = BitmapFactory.decodeFile(strFullPathCapturedImage, options);
      //bitmapInTemp = ImageUtil.GetRotatedBitmap(bitmapInTemp, degree);

      ImageView ivCapturedImg  = (ImageView) findViewById(R.id.ivCouponImg);
      ivCapturedImg.setImageBitmap(bitmapInTemp);


      /*

      Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

      ImageView ivCapturedImg  = (ImageView) findViewById(R.id.ivCouponImg);
      ivCapturedImg.setImageBitmap(myBitmap);
      */
    }


    Button btnlvRegNext = (Button) findViewById(R.id.reg_next_button);

    final EditText etCouponNormalPrice = (EditText) findViewById(R.id.etRegisterCouponNormalPrice);
    etCouponNormalPrice.addTextChangedListener(new CommaWonTextWatcher_Step2(etCouponNormalPrice));

    final EditText etCouponSalePrice = (EditText) findViewById(R.id.etRegisterCouponSalePrice);
    etCouponSalePrice.addTextChangedListener(new CommaWonTextWatcher_Step2(etCouponSalePrice));

    final EditText etCouponDescription = (EditText) findViewById(R.id.etRegisterCouponDescription);
    // 초기입력은 국문으로
    etCouponDescription.setPrivateImeOptions("defaultInputmode=korea;");

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


    } else {
      btnlvRegNext.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {


          String strCoponNormalPrice = etCouponNormalPrice.getText().toString().trim();
          String strCoponSalePrice = etCouponSalePrice.getText().toString().trim();
          String strCoponDescription = etCouponDescription.getText().toString().trim();



          // 엑티비티 이동
          Intent intent = new Intent(getApplicationContext(), MainRegisterCouponShopStep3.class);
          // Close all views before launching
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


          intent.putExtra("FULL_PATH_CAPTURED_IMAGE", strFullPathCapturedImage);

          intent.putExtra("COUPONTITLE", strCouponTitle);
          intent.putExtra("COUPONENDDATE", strCouponEndDate);
          intent.putExtra("COUPONNOTICE", strCouponNotice);

          intent.putExtra("COUPONNORMALPRICE", strCoponNormalPrice);
          intent.putExtra("COUPONSALEPRICE", strCoponSalePrice);
          intent.putExtra("COUPONDESCRIPTION", strCoponDescription);



          startActivity(intent);
          // 액티비비티 에니메이션
          overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);



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
  public void onBackPressed() {
    super.onBackPressed();

    File imgFile = new  File(strFullPathCapturedImage);

    if(imgFile.exists()) {

      imgFile.delete();
    }


    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
  }

}
