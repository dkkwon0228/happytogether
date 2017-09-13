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

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 06. 24. happytogether
 */

import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonConstants;


import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.ilovecat.happytogether.Databasehandler.DatabaseHandler;
import com.ilovecat.happytogether.Databasehandler.JsonHttpUserFunctions;

/**
 * GCMBaseIntentService를 상속받은 클래스를 프로젝트 루트 패키지에 생성한다.
 * 클래스는 반드시 다음 조건을 충족해야 한다.
 * 1.클래스명은 GCMIntentService여야 한다.
 * 2.반드시 루트 패키지 내에 선언되어 있어아 한다.
 *  상기 1,2 조건을 만족해야 하는 이유는 라이브러리 내에서 GCMService를 시작하는 부분이
 *  'GCMIntentService'라는 명칭으로 하드코딩되어 적용되어 있다.
 * @author Leminity
 * http://leminity.tistory.com/27
 *
 */

public class GCMIntentService extends GCMBaseIntentService {

  private String ADMINID = null;

  // GCM Server로부터 발급받은 Project ID를 통해 SuperClass인
  // GCMBaseIntentService를 생성해야한다.
  public GCMIntentService() {
    super(CommonConstants.GCM_SENDER_ID);
  }

  @Override
  protected void onMessage(Context context, Intent intent) {

    // TODO Auto-generated method stub
    /*
     * GCMServer가 전송하는 메시지가 정상 처리 된 경우 구현하는 메소드이다.
     * Notification, 앱 실행 등등 개발자가 하고 싶은 로직을 해당 메소드에서 구현한다.
     * 전달받은 메시지는 Intent.getExtras().getString(key)를 통해 가져올 수 있다.
     */

    String messageContent = intent.getStringExtra("msg");
    String messagePhotoName = intent.getStringExtra("photoname");
    String messageShopName = intent.getStringExtra("shopname");
    String messageShopIcon = intent.getStringExtra("shopicon");

    Log.i(CommonConstants.GCM_LOGTAG, "메시지:" + messageContent);
    Log.i(CommonConstants.GCM_LOGTAG, "messagePhotoName:" + messagePhotoName);
    Log.i(CommonConstants.GCM_LOGTAG, "messageShopName:" + messageShopName);
    Log.i(CommonConstants.GCM_LOGTAG, "messageShopIcon:" + messageShopIcon);

    // String messageFrom = intent.getStringExtra("from");
    // Log.e("getmessage", "getmessage:" + message);

    if (!TextUtils.isEmpty(messageContent)) {

      // String message = messageALL.split("tmp_")[0];
      // Log.e(" message", " message:" + message);
      // String photo_name = messageALL.split("tmp_")[1];
      // String tmp_photo_name = "tmp_" + photo_name;
      // Log.e(" message", " message:" + message);
      // Log.e(" tmp_photo_name", " tmp_photo_name:" + tmp_photo_name);

      // 알림창 설정
      generateNotification(context, messageContent, messagePhotoName, messageShopName, messageShopIcon);


      //푸시메시지가 올 때 팝업창이 뜸
      /*
      임시주석처리 16.6.25
      String MessagePhotoNameAndContent = messagePhotoName + messageContent;
      Intent pushpopintent = new Intent();
      pushpopintent.putExtra("messagePhotoNameAndContent", MessagePhotoNameAndContent);
      Log.e("메시지", "메시지:" + messageContent);


        //16/01/06 Intent가 한개만 넘어감
        //pushpopintent.putExtra("mesagePhotoName", MessagePhotoName);
        //Log.e("MessagePhotoName", "MessagePhotoName:" +MessagePhotoName);
        //pushpopintent.putExtra("mesageShopName", messageShopName);
        //pushpopintent.putExtra("mesageShopIcon", messageShopIcon);

      pushpopintent.setClassName(this, "com.ilovecat.happytogether.gcm.PopupActivity");

      // pushpopintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
      // Intent.FLAG_ACTIVITY_NEW_TASK);
      pushpopintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(pushpopintent);
      */
    }
  }

  /*
   * Receiving push messages
   */
  @Override
  protected void onRegistered(Context context, String registrationId) {

    /*
    * GCMRegistrar.getRegistrationId(context)가 실행되어
    * registrationId를 발급받은 경우 해당 메소드가 콜백된다.
    * 메시지 발송을 위해 regId를 서버로 전송하도록 하자.
    */

    /*
     * 15/12/28 users table의 regId를 등록한다.
     */
    DatabaseHandler mDbOpenHelper = new DatabaseHandler(this);
    ADMINID = mDbOpenHelper.getAdminid();
    mDbOpenHelper.close();
    String SHOP_NAME = mDbOpenHelper.getShopName();
    Log.i(CommonConstants.GCM_LOGTAG, ADMINID);
    JsonHttpUserFunctions userFunction = new JsonHttpUserFunctions();
    userFunction.registerRegIdInUserDB(ADMINID, registrationId);


    //추가 등록아이디(어드민아이디)도 등록한다.
    Log.i(CommonConstants.GCM_LOGTAG, registrationId);

    /*
    임시주석
    userFunction.registerRegIdOnGCMUSER(ADMINID, ZIP_CODE, CITY, SUB_CITY, DONG, SHOP_NAME, registrationId);
    */
    userFunction.registerRegIdOnGCMUserDB(ADMINID, SHOP_NAME, registrationId);

    GCMRegistrar.setRegisteredOnServer(context, true);
  }

  @Override
  protected void onUnregistered(Context arg0, String arg1) {
    // TODO Auto-generated method stub
    /**
     * GCMRegistrar.unregister(context) 호출로 해당 디바이스의
     * registrationId를 해지요청한 경우 해당 메소드가 콜백된다.
     */
    Log.e("키를 제거합니다.(GCM)", "제거되었습니다.");
  }

  /**
   * <pre>
   * 1. 메소드명 : generateNotification
   * 2. 작성일 : 2016. 5. 8. 오후 9:35:32
   * 3. 작성자 : dannykwon
   * 4. 설명 :
   * </pre>
   */
  private static void generateNotification(Context context, String message, String photoname, String shopname,
                                           String shopicon) {

    int icon = R.drawable.ic_launcher;

    long when = System.currentTimeMillis();
    String title = "♡MYTOWN♡ " + shopname + "에서 ♡새로운 알림이 왔네요♡";


    /*
    얼마전에 안드로이드 스튜디오를 업데이트 했더니 GCMService에서 Notification.setLatestEventInfo 부분에서
    오류가 발생하더군요. 구글링 하여 문서를 찾아본 결과 CompleSdkVersion이 23(android 버전 4.0)이상일 경우
    setLatestEventInfo 부분이 삭제되어서 사용할 수 없게 되었다는 소식이 있었습니다.
    그래서 해당 코드를 사용하지 않고 Notification Builder를 사용하여
    동일한 Notification이 띄워지도록 하겠습니다.
    http://boxfoxs.tistory.com/266
    16.6.24

    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    Notification notification = new Notification(icon, title, System.currentTimeMillis());

    // 기본으로 지정된 소리를 내기 위해
    notification.defaults = (Notification.DEFAULT_SOUND);

    // 알림 소리를 한번만 내도록
    notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;

    // 확인하면 자동으로 알림이 제거 되도록
    notification.flags = Notification.FLAG_AUTO_CANCEL;

    // 사용자가 알람을 확인하고 클릭했을때 새로운 액티비티를 시작할 인텐트 객체
    Intent intentGCM = new Intent(context, com.appmaker.gcmtest.replys.moreReplysActivity.class);
    com.appmaker.gcmtest.replys.moreReplysActivity.popPHOTO_NAME = photoname;
    intentGCM.putExtra("keyPopPhotoName", photoname);
    intentGCM.putExtra("keyWriterId", ADMINID);

    // re_message=message;
    // 새로운 태스크(Task) 상에서 실행되도록(보통은 태스크1에 쌓이 태스크2를 만들어서 전혀 다른 실행으로 관리한다)
    intentGCM.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
        | Intent.FLAG_ACTIVITY_CLEAR_TOP
        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    // intentGCM.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

    // 인텐트 객체를 포장해서 전달할 인텐트 전달자 객체
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentGCM,
        PendingIntent.FLAG_UPDATE_CURRENT);
    // 상단바를 드래그 했을때 보여질 내용 정의하기
    notification.flags |= Notification.FLAG_AUTO_CANCEL;
    notification.setLatestEventInfo(context, title, message, pendingIntent);
    // 알림창 띄우기(알림이 여러개일수도 있으니 알림을 구별할 상수값, 여러개라면 상수값을 달리 줘야 한다.
    notificationManager.notify(0, notification);
    */


    // http://itmir.tistory.com/457 참고해야함
    Intent intentGCM = new Intent(context, EnterAppActivity.class);
    String key = "FROM_NOTI";
    EnterAppActivity.keyFromNotofication = key;
    //intentGCM.putExtra("keyPopPhotoName", photoname);

    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    PendingIntent pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intentGCM,
        //new Intent(context, EnterAppActivity.class),
        PendingIntent.FLAG_UPDATE_CURRENT);

    Notification.Builder builder = new Notification.Builder(context)
        .setContentIntent(pendingIntent)
        .setSmallIcon(icon)
        .setContentTitle(title)
        .setContentText(message)
        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
        .setAutoCancel(true)
        .setNumber(10)
        .setTicker("소식 왔어요!");

    Notification notification = builder.build(); // API Level 16+ 에서만 적용.
    nm.notify(1234, notification);

  }

  @Override
  protected void onError(Context arg0, String arg1) {
    // TODO Auto-generated method stub
    // GCM 오류 발생 시 처리해야 할 코드를 작성한다.
    // ErrorCode에 대해선 GCM 홈페이지와 GCMConstants 내 static variable 참조한다.
  }
}
