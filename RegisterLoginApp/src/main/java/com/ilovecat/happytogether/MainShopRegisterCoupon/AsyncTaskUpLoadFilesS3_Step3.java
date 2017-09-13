package com.ilovecat.happytogether.MainShopRegisterCoupon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.ilovecat.happytogether.MainShopListingCoupon.MainCouponShopFragment;
import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.RegisterEmail.RegisterEmailActivity;

import java.io.File;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 31. happytogether
 */
/*
   * File : execute, doInBackground 의 파라미터 타입
   * Integer : onProgressUpdate 의 파라미터 타입
   * String : doInBackground 의 리턴값, onPostExecute 의 파라미터로 설정됩니다.
   */

public class AsyncTaskUpLoadFilesS3_Step3 extends AsyncTask<Void, Integer, String>
    implements DialogInterface.OnCancelListener {

  ProgressDialog pdLoading = new ProgressDialog(MainRegisterCouponShopStep3.appcActivity);

  NotificationManager manager = (NotificationManager) MainRegisterCouponShopStep3.appcActivity.getSystemService(Context.NOTIFICATION_SERVICE);
  NotificationCompat.Builder mBuilder = null;

  // AWS S3
  CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
      MainRegisterCouponShopStep3.appcActivity.getApplicationContext(),
      "ap-northeast-2:00031770-06b1-4b93-a597-3b06f99ecea0",
      Regions.AP_NORTHEAST_2
      //"ap-northeast-2:52adf683-5415-42aa-bdcc-10c444adc2cc",
      //Regions.AP_NORTHEAST_2
  );

  String BUCKET_NAME = "happytogether2-s3-images";

  AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
  TransferUtility transferUtility = new TransferUtility(s3, MainRegisterCouponShopStep3.appcActivity.getApplicationContext());

  private File file;
  private String openState;

  public AsyncTaskUpLoadFilesS3_Step3(File file, String openState) {

    this.file = file;
    this.openState = openState;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    //this method will be running on UI thread
    pdLoading.setMessage("\tLoading...");
    pdLoading.setCancelable(false);
    pdLoading.show();
  }

  @Override
  protected String doInBackground(Void... unused) {

    String return_message = "2";

    // String content = executeClient(sourceFileUri[0]);
    // return content;
    //final File fileName = sourceFileUri[0];
    //fileNameSize = (int) fileName.length();
    //Log.w("dsd", "fileName is " + fileName);

    TransferObserver observer = transferUtility.upload(BUCKET_NAME, file.getName(), file);

    return return_message;
  }

  @Override
  protected void onProgressUpdate(Integer... progress) {

  }

  @Override
  public void onCancel(DialogInterface dialog) {
    dialog.dismiss();
  }

  @Override
  protected void onPostExecute(final String result) {

    super.onPostExecute(result);

    mBuilder = new NotificationCompat.Builder(MainRegisterCouponShopStep3.appcActivity.getApplicationContext());
    mBuilder.setSmallIcon(R.drawable.ic_launcher);

    if (openState == "Y") {
      mBuilder.setTicker("즉시공개로 업로드 되었습니다.");
      //mBuilder.setTicker(result + "번째 파일이 정상적으로 업로드 되었습니다.");
      mBuilder.setContentText("업로드 알림");
      //mBuilder.setContentTitle(result + "번째 파일이 정상적으로 업로드 되었습니다.");
      mBuilder.setContentTitle("즉시공개로  업로드 되었습니다.");

    } else {

      mBuilder.setTicker("비공개로 업로드 되었습니다.");
      //mBuilder.setTicker(result + "번째 파일이 정상적으로 업로드 되었습니다.");
      mBuilder.setContentText("업로드 알림");
      //mBuilder.setContentTitle(result + "번째 파일이 정상적으로 업로드 되었습니다.");
      mBuilder.setContentTitle("비공개로 업로드 되었습니다.");

    }

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


    pdLoading.dismiss();

  }
}