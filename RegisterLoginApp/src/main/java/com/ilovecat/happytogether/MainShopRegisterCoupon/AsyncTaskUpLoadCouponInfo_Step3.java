package com.ilovecat.happytogether.MainShopRegisterCoupon;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.ilovecat.happytogether.Databasehandler.JsonHttpUserFunctions;
import com.ilovecat.happytogether.R;

import org.json.JSONObject;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 31. happytogether
 */
/*
   * Void => execute, doInBackground의 파라미터 타입
     Void => onProgressUpdate의 파라미터 타입
     String => doInBackground의 리턴값, onPostExecute의 파라미터로 설정됩니다.

     [onPreExecute()] -> [doInBackground()] -> [onPostExecute()]
  */
public class AsyncTaskUpLoadCouponInfo_Step3 extends AsyncTask<Void, Void, String> {

  NotificationManager manager = (NotificationManager) MainRegisterCouponShopStep3.appcActivity.getSystemService(Context.NOTIFICATION_SERVICE);
  NotificationCompat.Builder mBuilder = null;

  private String strCouponNumber;
  private String strShopName;
  private String strCouponImageFileName;
  private String strCouponTitle;
  private String strCouponNotice;
  private String strCouponEndDate;
  private String strCouponNormalPrice;
  private String strCouponSalePrice;
  private String strCouponDescription;
  private String strCouponOpenState;


  public AsyncTaskUpLoadCouponInfo_Step3(
      String strCouponNumber,
      String strShopName,
      String strCouponImageFileName,
      String strCouponTitle,
      String strCouponNotice,
      String strCouponEndDate,
      String strCouponNormalPrice,
      String strCouponSalePrice,
      String strCouponDescription,
      String strCouponOpenState

  ) {
    this.strCouponNumber = strCouponNumber;
    this.strShopName = strShopName;
    this.strCouponImageFileName = strCouponImageFileName;
    this.strCouponTitle = strCouponTitle;
    this.strCouponNotice = strCouponNotice;
    this.strCouponEndDate = strCouponEndDate;
    this.strCouponNormalPrice = strCouponNormalPrice;
    this.strCouponSalePrice = strCouponSalePrice;
    this.strCouponDescription = strCouponDescription;
    this.strCouponOpenState = strCouponOpenState;
  }

  protected String doInBackground(Void... unused) {

    String return_message = "1";

    JsonHttpUserFunctions jsonHttpUserFunctions = new JsonHttpUserFunctions();
    JSONObject json = jsonHttpUserFunctions.registerCouponShop(
        strCouponNumber,
        strShopName,
        strCouponImageFileName,
        strCouponTitle,
        strCouponNotice,
        strCouponEndDate,
        strCouponNormalPrice,
        strCouponSalePrice,
        strCouponDescription,
        strCouponOpenState
    );

    return return_message;
  }

  protected void onPostExecute(String result) {

    mBuilder = new NotificationCompat.Builder(MainRegisterCouponShopStep3.appcActivity.getApplicationContext());
    mBuilder.setSmallIcon(R.drawable.ic_launcher);

    mBuilder.setTicker("쿠폰정보를 업로드 하였습니다.");
    //mBuilder.setTicker(result + "번째 파일이 정상적으로 업로드 되었습니다.");
    mBuilder.setContentText("업로드 알림");
    //mBuilder.setContentTitle(result + "번째 파일이 정상적으로 업로드 되었습니다.");
    mBuilder.setContentTitle("쿠폰정보를 업로드 하였습니다.");


    //mBuilder.setWhen(System.currentTimeMillis());
    //mBuilder.setNumber(10);
    mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
    //mBuilder.setContentIntent(pendingIntent);
    mBuilder.setAutoCancel(true);
    mBuilder.setPriority(Notification.PRIORITY_MAX);

    manager.notify(Integer.parseInt(result), mBuilder.build());
    manager.cancel(Integer.parseInt(result));

    manager = null;
    mBuilder = null;

  }
}
