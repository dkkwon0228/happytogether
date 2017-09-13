package com.ilovecat.happytogether.MainShopRegisterCoupon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ilovecat.happytogether.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 08. 14. happytogether
 */
/*
  File 	: execute, doInBackground 의 파라미터 타입
  Integer: onProgressUpdate 의 파라미터 타입
  Int	: doInBackground 의 리턴값, onPostExecute 의 파라미터로 설정됩니다.
  */
public class AsyncTaskCaptureImage_Step1 extends AsyncTask<Integer, Integer, Integer> {

  ProgressDialog dialog;

  @Override
  protected void onPreExecute() {
    //btn.setText("Thread START!!!!");
    dialog = ProgressDialog.show(MainRegisterCouponShopStep1.appcActivity, "",
        "임시보관중입니다. 잠시 기다려주세요", true);
    super.onPreExecute();


    FrameLayout container;
    container = (FrameLayout) MainRegisterCouponShopStep1.appcActivity.findViewById(R.id.StcikerFrameLayout);
    container.setDrawingCacheEnabled(true);
    container.buildDrawingCache();
    container.invalidate(); // 뷰를 갱신해 주어야 함

    int sizeOfArray = MainRegisterCouponShopStep1.arrayListStickerMessageView.size();
    for (int i = 0; i < sizeOfArray; i++) {
      MainRegisterCouponShopStep1.arrayListStickerMessageView.get(i).setControlItemsHidden(true);
    }

    int sizeOfArray2 = MainRegisterCouponShopStep1.arrayListStickerShopLogoView.size();
    for (int i = 0; i < sizeOfArray2; i++) {
      MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(i).setControlItemsHidden(true);
    }
    //tv_ShopNameSticker.setControlItemsHidden(true);

    MainRegisterCouponShopStep1.captureBitmap = container.getDrawingCache();

  }

  @Override
  protected void onProgressUpdate(Integer... values) {
    //pb.setProgress(values[0]);
    super.onProgressUpdate(values);
  }

  @Override
  protected Integer doInBackground(Integer... params) {
    int result = 0;

    String arrfileName = MainRegisterCouponShopStep1.FOR_CROP_IMAGE_FILENAME.split("_")[2];
    String FILE_SAVE_TIME = arrfileName.substring(0, 13);

    FileOutputStream fos;
    try {
      fos = new FileOutputStream(MainRegisterCouponShopStep1.cropedImageDir_path
          + "captured_"
          + FILE_SAVE_TIME
          + ".jpg");

      // 100일 경우 500K 정도 파일크기가 되므로 50으로 수정. 50일 경우 80k 정도 차지
      MainRegisterCouponShopStep1.captureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }


    File files = new File(MainRegisterCouponShopStep1.cropedImageDir_path
        + "captured_"
        + FILE_SAVE_TIME
        + ".jpg");

    if (files.exists()) {
      result = 1;
    }

    return result;
  }

  @Override
  protected void onCancelled() {
    super.onCancelled();
  }

  @Override
  protected void onPostExecute(Integer result) {
    //btn.setText("Thread END");
    super.onPostExecute(result);
    if (result == 1) {
      Log.i("copy", "SUCESS");

	/*
         * 14/11/01
	 * 화면을 캡처하여 이미지 사이즈대로 자른 후
	 * Temp 폴더로 저장한다.
	 */

      Toast.makeText(MainRegisterCouponShopStep1.context, "Captured!", Toast.LENGTH_LONG).show();

      String arrfileName = MainRegisterCouponShopStep1.FOR_CROP_IMAGE_FILENAME.split("_")[2];
      String FILE_SAVE_TIME = arrfileName.substring(0, 13);
      String fullPathCapturedImage = MainRegisterCouponShopStep1.cropedImageDir_path
          + "captured_"
          + FILE_SAVE_TIME
          + ".jpg";

      Intent intent = new Intent(MainRegisterCouponShopStep1.context, MainRegisterCouponShopStep2.class);
      // Close all views before launching
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      intent.putExtra("COUPONTITLE", MainRegisterCouponShopStep1.title);
      intent.putExtra("COUPONENDDATE", MainRegisterCouponShopStep1.endDate);
      intent.putExtra("COUPONNOTICE", MainRegisterCouponShopStep1.couponNotice);
      intent.putExtra("FULL_PATH_CAPTURED_IMAGE", fullPathCapturedImage);

      MainRegisterCouponShopStep1.appcActivity.startActivity(intent);
      // 액티비비티 에니메이션
      MainRegisterCouponShopStep1.appcActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
      //System.exit(0); //매우중요

    }

    if (null != dialog && dialog.isShowing()) {
      dialog.dismiss();
    }

  }
}