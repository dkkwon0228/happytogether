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

package com.ilovecat.happytogether;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ilovecat.happytogether.Main.MainActivity;
import com.ilovecat.happytogether.Databasehandler.DatabaseHandler;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonUtils;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonConstants;
import com.ilovecat.happytogether.RegisterIdPassword.RegisterIdPasswordActivity;

/**
 * <pre>
 * 1. 패키지명  : com.appmaker.gcmtest
 * 2. 타입명 : EnterApp In EnterApp.java
 * 3. 작성일 : 2016. 5. 8. 오후 10:03:59
 * 4. 작성자 : dannykwon (dkkwon0228@gmail.com)
 * 5. 설명  : 앱 실행시 최초로 런칭되는 클래스.
 *           내부디비(SQLITE)의 adminkey_tb의 row의 갯수를 가져와서 내부변수 count에 할당한다.
 *           count가 0일 경우에는 RegisterIdPasswordActivity 호출한다.
 * 	         count가 1일 경우에는 내부디비의 adminkey_tb의 loginstate 필드의 값을 가져와서 내부변수 loginState에 할당한다.
 *           loginState의 값이 0일 경우, LoginActivity를 호출.
 *           loginState의 값이 1일 경우, MainActivity를 호출.
 * </pre>
 */

public class EnterAppActivity extends AppCompatActivity {

  // GCMIntentService pendingIntent에서 참조한다.
  public static String keyFromNotofication = "FROM_NORMAL";


  private DatabaseHandler handlerDatabase;
  private Integer intAdminCount = null;
  private String strLoginState = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    Handler handler = new Handler();
    handler.postDelayed(new SplashDelayHandler(), 1000);
    startActivity(new Intent(EnterAppActivity.this, SplashActivity.class));

    /*
    내부디비(SQLITE)의 adminkey_tb의 row값을 가져와서 count에 할당.
    어드민키를 획득함
    TABLE_ADMINKEY = "adminkey_tb
    adminkey_tb의 row에  값이 있을 경우에 1, 없을 경우엔 0을 리턴...
    2016/10/04
    */
    handlerDatabase = new DatabaseHandler(this);
    intAdminCount = handlerDatabase.getRowCountAdminkey();
    Log.i(CommonConstants.DB_LOGTAG, "ADMINKEY_COUNT in ENTERAPP = " + intAdminCount);
    handlerDatabase.close();
    CommonUtils.showMemoryStatusLog();
  }

  /**
   * <pre>
   * 1. 패키지명 : com.appmaker.gcmtest
   * 2. 타입명 :  SplashDelayHandler In EnterApp.java
   * 3. 작성일 : 2016. 5. 8. 오후 11:24:01
   * 4. 작성자 : dannykwon (dkkwon0228@gmail.com)
   * 5. 설명 :
   * </pre>
   */
  class SplashDelayHandler implements Runnable {
    public void run() {

      switch (intAdminCount) {
        case 0:
          startActivity(new Intent(EnterAppActivity.this, RegisterIdPasswordActivity.class));
          //startActivity(new Intent(EnterAppActivity.this, CouponShopListingActivity.class));
          finish();
          break;

        case 1:
          /*
           * 내부디비(SQLITE)의 adminkey_tb의 loginstate 필드의 값을 가져와서 strLoginState
           * 변수에 할당.
          */
          strLoginState = handlerDatabase.getLoginState();
          Log.i(CommonConstants.DB_LOGTAG, "strLoginState in ENTERAPP = " + strLoginState);
          handlerDatabase.close();

          switch (Integer.parseInt(strLoginState)) {
            case 0:
              startActivity(new Intent(EnterAppActivity.this, MainSearch.class));
              finish();
              break;

            case 1:

              // keyFromNotofication는  GCMIntentService 액티비티 PendingIntent 에서 할당받는다.
              if(keyFromNotofication.equals("FROM_NOTI")) {
                startActivity(new Intent(EnterAppActivity.this, RegisterIdPasswordActivity.class));
                keyFromNotofication = "FROM_NORMAL"; //원래 key값으로 돌린다.
                finish();

              } else {

                //개발중 편의상 임시로 구현 아래 3줄
                //startActivity(new Intent(EnterAppActivity.this, RegisterIdPasswordActivity.class));
                //keyFromNotofication = "FROM_NORMAL"; //원래 key값으로 돌린다.
                //finish();

                //startActivity(new Intent(EnterAppActivity.this, MainSearch.class));
                startActivity(new Intent(EnterAppActivity.this, MainActivity.class));
                //startActivity(new Intent(EnterAppActivity.this,
                //    com.javapapers.android.googleplacesdetail.GooglePlacesActivity.class));

                finish();
              }

              break;

            default:
              break;
          }
          break;

        default:
          break;
      }
    }
  }

}
