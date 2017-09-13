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

import android.os.Environment;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 06. 24. happytogether
 */
public final class CommonConstants {

  // JSON Response node names In RegisterActivity.
  public static final String JSON_KEY_SUCCESS = "success";
  public static final String JSON_KEY_ERROR = "error";
  public static final String JSON_KEY_ERROR_MSG = "error_msg";
  public static final String JSON_KEY_UID = "Uid";
  public static final String JSON_KEY_ADMINKEY = "AdminKey";
  public static final String JSON_KEY_ADMINID = "AdminId";
  public static final String JSON_KEY_ADMINPASSWD = "EncryptedPassword";
  public static final String JSON_KEY_ADMINEMAIL = "AdminEmail";
  public static final String JSON_KEY_SHOPNAME = "ShopName";
  public static final String JSON_KEY_CREATED_AT = "CreatedAt";
  public static final String JSON_KEY_DEVICEID = "DeviceId";
  public static final String JSON_KEY_REGID = "RegId";
  public static final String KEY_LOGINSTATE_LOGIN = "1";


  // Log LogTag
  public static final String DB_LOGTAG = "SQLDataBase";
  public static final String GCM_LOGTAG = "GCM_JOB";
  public static final String FILEIO_LOGTAG = "FILE_IO";
  public static final String MEMORY_LOGTAG = "MEMORY";
  public static final String GOOGLE_API_LOGTAG = "GOOGLE_API";

  // Google GCM
  public static final String GCM_SENDER_ID = "372246090993";

  //
  public static final String INNER_PHOTO_TEMP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
      + "/DCIM/MyTown/Temp/";
  public static final String INNER_PHOTO_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
      + "/DCIM/MyTown/";
  public static final String INNER_XML_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
      + "/MyTown/";
  public static final String INNER_SHOPIMAGES_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
      + "/DCIM/MyTown/ShopImages/";
  public static final String INNER_DISK_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
      + "/DCIM/HappyTogether/";


  //################################################################################################
  // RegisterLocationSearchActivity
  public static final String search_location_url = "http://mytown.ddns.net/search/search_location.php";

  // RegisterShopLocationSearchActivity
  public static final String search_shopname_url = "http://mytown.ddns.net/search/search_shopname.php";

  // ASYNC FETCH SETTING
  public static final int CONNECTION_TIMEOUT = 10000;
  public static final int READ_TIMEOUT = 15000;
  //################################################################################################
}
