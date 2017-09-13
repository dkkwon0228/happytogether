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

package com.ilovecat.happytogether.CommonConstantsAndUtils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 06. 25. happytogether
 */
public class CommonUtils {
  /**
   * 디바이스 메모리 현황.
   */
  public static void showMemoryStatusLog() {
    double maxMemory = Runtime.getRuntime().maxMemory() / ( 1024.0f );
    double allocateMemory = Debug.getNativeHeapAllocatedSize() / ( 1024.0f );
    Log.i( CommonConstants.MEMORY_LOGTAG, "전체 메모리 : " + maxMemory + "KB " );
    Log.i( CommonConstants.MEMORY_LOGTAG, "할당된 메모리 : " + allocateMemory + "KB " );
  }

  /**
   * 네트워크 연결 체크
   * @param activity
   * @return
   */
  public static boolean isNetworkAvailable(Activity activity) {
    ConnectivityManager connectivity = (ConnectivityManager) activity
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivity == null) {
      return false;
    } else {
      NetworkInfo[] info = connectivity.getAllNetworkInfo();
      if (info != null) {
        for (int i = 0; i < info.length; i++) {
          if (info[i].getState() == NetworkInfo.State.CONNECTED) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * 파일 생성.
   *
   * @return file
   */
  public static File makeFile(File fileDir, String strFilePath) {
    File file = null;
    boolean isSuccess = false;
    if (fileDir.isDirectory()) {
      file = new File(strFilePath);
      if (file != null && !file.exists()) {
        Log.i(CommonConstants.FILEIO_LOGTAG, "!file.exists");
        try {
          isSuccess = file.createNewFile();
        } catch (IOException error) {
          error.printStackTrace();
        } finally {
          Log.i(CommonConstants.FILEIO_LOGTAG, "파일생성 여부 = " + isSuccess);
        }
      } else {
        Log.i(CommonConstants.FILEIO_LOGTAG, "file.exists");
      }
    }
    return file;
  }

  /**
   * 파일에 내용 쓰기.
   */
  public static boolean writeFile(File file, byte[] byteArrayFileContent) {
    boolean result;
    FileOutputStream fos;
    if (file != null && file.exists() && byteArrayFileContent != null) {
      try {
        fos = new FileOutputStream(file);
        try {
          fos.write(byteArrayFileContent);
          fos.flush();
          fos.close();
        } catch (IOException error) {
          error.printStackTrace();
        }
      } catch (FileNotFoundException error) {
        error.printStackTrace();
      }
      result = true;
    } else {
      result = false;
    }
    return result;
  }

  /**
   * 랜덤키를 생성하는 메서드.
   *
   * @param len 문자열 키의 길이
   * @return 생성된 랜덤키
   */
  public static String getRandomKey(int len) {
    int intSeed = 0;
    int intSeedSize = 61;
    String strSrc = "ABCDEFGHIJKLMNOPQRSTUWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    String strKey = "";

    for (int i = 0; i < len; i++) {
      intSeed = (int) ((Math.random() * intSeedSize) + 1);
      strKey += String.valueOf(strSrc.charAt(intSeed - 1));
    }

    return strKey;
  }

  /**
   * 디렉토리 생성.
   *
   * @return dir
   */
  public static File makeDirectory(String strDirPath) {
    File fileDir = new File(strDirPath);
    if (!fileDir.exists()) {
      fileDir.mkdirs();
      Log.i(CommonConstants.FILEIO_LOGTAG, "생성");
    } else {
      Log.i(CommonConstants.FILEIO_LOGTAG, "이미존재함");
    }

    return fileDir;
  }



  /**
   * 사업자 번호 유효성 검사
   * @param str
   * @return
   */
  public static boolean isBizNum(String str){
    if(str == null) return false;

    str = str.trim();
    if(str.equals("") || str.length() != 10) return false;

    int suma = 0;
    int sumb = 0;
    int sumc = 0;
    int sumf = 0;
    int osub = 0;
    int s = 0;
    int lastval = 0;

    for(int i = 0; i<8; i++){
      if(i%3 == 1) s = 3;
      else if(i%3 == 2) s = 7;
      else if(i%3 == 0) s = 1;

      suma = suma + (Integer.parseInt(str.substring(i,i+1))*s);
      sumf = sumf + Integer.parseInt(str.substring(i,i+1));
    }
    if(sumf == 0) return false;

    osub = Integer.parseInt(str.substring(9));
    sumb = (Integer.parseInt(str.substring(8,9))*5) % 10;
    sumc = (int)(Integer.parseInt(str.substring(8,9))*5 / 10);
    lastval = (10 - ((suma + sumb + sumc) % 10)) % 10;

    if(osub == lastval) return true;
    else return false;
  }


  // Using In TempSave
  // Using In Painter
  public static void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {

    File fileCacheItem = new File(strFilePath);
    OutputStream out = null;

    try {
      fileCacheItem.createNewFile();
      out = new FileOutputStream(fileCacheItem);

      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  // Using TempSave
  public static Bitmap cropCenterBitmap(Bitmap src, int w, int h) {
    if(src == null)
      return null;

    int width = src.getWidth();
    int height = src.getHeight();

    if(width < w && height < h)
      return src;

    int x = 0;
    int y = 0;

    if(width > w)
      x = (width - w)/2;

    if(height > h)
      y = (height - h)/2;

    int cw = w; // crop width
    int ch = h; // crop height

    if(w > width)
      cw = width;

    if(h > height)
      ch = height;

    Bitmap returnBitmap = Bitmap.createBitmap(src, x, y, cw, ch);

    return returnBitmap;
  }



  public static int convertDpToPixel(float dp, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float px = dp * (metrics.densityDpi / 160f);
    return (int) px;
  }

  public static int convertPixelToDp(float pixel, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float dp = pixel / (metrics.densityDpi / 160f);
    return (int) dp;
  }

}
