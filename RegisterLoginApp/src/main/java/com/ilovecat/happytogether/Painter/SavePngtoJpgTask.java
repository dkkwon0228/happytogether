package com.ilovecat.happytogether.Painter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonConstants;
import com.ilovecat.happytogether.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 08. 09. happytogether
 */

// PNG를 JPG로

public class SavePngtoJpgTask extends AsyncTask<Bitmap, Void, File> {

  private ProgressDialog mProgressDialog = new ProgressDialog(PainterActivity.appcActivity);

  @Override
  protected void onPreExecute() {

    mProgressDialog.setMessage(PainterActivity.appcActivity.getString(R.string.saving));
    mProgressDialog.setIndeterminate(true);
    mProgressDialog.show();

  }

  @Override
  protected void onPostExecute(File result) {
    mProgressDialog.dismiss();
    if (result != null) {
      Toast.makeText(PainterActivity.appcActivity,
          PainterActivity.appcActivity.getString(R.string.saved_as) + result.getName(), Toast.LENGTH_LONG).show();

      new ProcessGrabCutTask().execute();

    }
  }

  @SuppressLint("SimpleDateFormat")
  @Override
  protected File doInBackground(Bitmap... params) {

    String pngFileName = new SimpleDateFormat("'Painter_'yyyy-MM-dd_HH-mm-ss.S'.png'").format(new Date());
    File result = new File(CommonConstants.INNER_DISK_PATH + "CropImage/", pngFileName);

    PainterActivity.maskPathAndFileName = CommonConstants.INNER_DISK_PATH + "CropImage/" + pngFileName;


    FileOutputStream stream = null;
    try {
      try {
        stream = new FileOutputStream(result);
        if (params[0].compress(Bitmap.CompressFormat.PNG, 75, stream)) {
          PainterActivity.appcActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(result)));
        } else {
          result = null;
        }
      } finally {
        if (stream != null) {
          stream.close();
        }
      }
    } catch (IOException e) {
      result = null;
    }

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      //
    }
    return result;
  }
}
