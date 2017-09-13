package com.ilovecat.happytogether.CommonConstantsAndUtils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.ilovecat.happytogether.MainShopRegisterCoupon.MainRegisterCouponShopStep1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 24. happytogether
 */
public class UtilsForAlbumAndCamera {
  public static File getImageFile(Uri uri, Context context) {

    //BitmapFactory.Options options = new BitmapFactory.Options();
    //options.inJustDecodeBounds = true;
    String[] projection = {MediaStore.Images.Media.DATA};

    if (uri == null) {
      uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    Cursor mCursor = context.getContentResolver().query(
        uri,
        projection,        //Which columns to return
        null,                        // WHERE clause; which rows to return (all rows)
        null,                        // WHERE clause selection arguments (none)
        MediaStore.Images.Media.DATE_MODIFIED + " desc" // Order-by clause (ascending by name)
    );
    if (mCursor == null || mCursor.getCount() < 1) {
      return null;
    } // no cursor or no record

    int column_index = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    mCursor.moveToFirst();
    //uri = Uri.parse(mCursor.getString(column_index));
    String path = mCursor.getString(column_index);
    //Bitmap displayBitmap = BitmapFactory.decodeFile(path, options);
    //options = getBitmapSize(options);
    //Bitmap displayBitmap = BitmapFactory.decodeFile(path, options);
    //uploadImage.setImageBitmap(displayBitmap);

    if (mCursor != null) {
      mCursor.close();
      mCursor = null;
    }
    return new File(path);
  }



  public static boolean copyFile(File srcFile, File destFile) {

    FileInputStream inputStream = null;
    FileOutputStream outputStream = null;
    FileChannel fcin = null;
    FileChannel fcout = null;

    try {
      inputStream = new FileInputStream(srcFile);
      outputStream = new FileOutputStream(destFile);
      fcin = inputStream.getChannel();
      fcout = outputStream.getChannel();

      long size = fcin.size();
      fcin.transferTo(0, size, fcout);
      //fcout = null;

      return true;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {

      try {
        fcout.close();
      } catch (IOException ioe) {
        return false;
      }
      try {
        fcin.close();
      } catch (IOException ioe) {
        return false;
      }
      try {
        outputStream.close();
      } catch (IOException ioe) {
        return false;
      }
      try {
        inputStream.close();
      } catch (IOException ioe) {
        return false;
      }
    }

  }




  public static Uri saveFileForCropImage() {
    Uri uri;


    File file = new File(MainRegisterCouponShopStep1.tempDir_path);
    if (!file.exists())  // 원하는 경로에 폴더가 있는지 확인
      file.mkdirs();

    MainRegisterCouponShopStep1.FOR_CROP_IMAGE_FILENAME = "tmp_stikercrop_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
    uri = Uri.fromFile(new File(MainRegisterCouponShopStep1.tempDir_path,
        MainRegisterCouponShopStep1.FOR_CROP_IMAGE_FILENAME));
    return uri;
  }



}
