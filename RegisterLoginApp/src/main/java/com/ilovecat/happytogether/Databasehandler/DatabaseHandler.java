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
 * Created by dannykwon on 16. 6. 19.
 */


package com.ilovecat.happytogether.Databasehandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 1. 패키지명 : com.appmaker.gcmtest.adminlogin
 * 2. 타입명 :  DatabaseHandler In DatabaseHandler.java
 * 3. 작성일 : 2016. 5. 9. 오전 12:04:19
 * 4. 작성자 : dannykwon
 * 5. 설명 : 데이타베이스 버전.
 *          데이타베이스 이름과 저장위치.
 *          테이블 이름 각 테이블의 필드(컬럼)이름.
 *          테이타베이스 생성, 테이블 생성.
 *          테이타베이스 업그래이드.
 *          login_tb 테이블에 사용자 정보 등록.
 *          adminkey_tb 테이블에 관련데이타 등록.
 *          adminkey_tb 테이블에 logoutstate 업데이트
 *          adminkey_tb 테이블에 loginstate 업데이트
 */
public class DatabaseHandler extends SQLiteOpenHelper {

  // All Static variables
  // Database Version
  private static final int DATABASE_VERSION = 1;

  // Database Name And Location
  /*
  * 단말기 내부에 DB파일을 저장해야 하나 개발과정중에 DB내용을 수정, 확인해야 할 필요가 있으므로 임시적으로
  * 단말기 외부 저장소에 DB파일을 저장하였다.
  */
  private static String strExternalStorageDirectory =
      Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
  private static final String DATABASE_NAME = strExternalStorageDirectory + "HappyTogether.db";

  // TODO 개발이 완료되어 배포전에 DB파일을 단말기 내부에 저장해야 한다. 2016. 5. 9. 오전 12:10:36
  //private static final String DATABASE_NAME = "GCMTEST.db";


  // Login table name
  private static final String TABLE_LOGIN = "login_tb";
  private static final String TABLE_ADMINKEY = "adminkey_tb";
  private static final String TABLE_IMAGE = "image_tb";

  // Image Table Columns names
  private static final String KEY_ID = "id";
  private static final String KEY_ADMINKEY = "adminkey";
  private static final String KEY_ADMINID = "adminid";
  private static final String KEY_ADMINPASSWD = "adminpasswd";
  private static final String KEY_ADMINEMAIL = "adminemail";
  private static final String KEY_LOGINSTATE = "loginstate";
  private static final String KEY_SHOPNAME = "shopname";
  private static final String KEY_CREATED_AT = "created_at";
  private static final String KEY_UPDATED_AT = "updated_at";
  private static final String KEY_CATE_BIZ = "cate_biz";
  private static final String KEY_ZIPCODE1 = "zipcode1";
  private static final String KEY_ZIPCODE2 = "zipcode2";
  private static final String KEY_ADDRESS1 = "address1";
  private static final String KEY_ADDRESS2 = "address2";
  private static final String KEY_ADDRESS3 = "address3";
  private static final String KEY_ADDRESS4 = "address4";
  private static final String KEY_ADDRESS5 = "address5";
  private static final String KEY_ADDRESS6 = "address6";
  private static final String KEY_ADDRESS_DETAIL = "address_detail";
  private static final String KEY_PHONENUM1 = "phonenum1";
  private static final String KEY_PHONENUM2 = "phonenum2";
  private static final String KEY_PHONENUM3 = "phonenum3";
  private static final String KEY_SHOPICON_URL = "shopicon_url";
  private static final String KEY_OPEN_STATE = "open_state";
  private static final String KEY_DEVICEID = "deviceid";
  private static final String KEY_REGID = "regid";
  private static final String KEY_USERPOINT = "userpoint";
  private static final String KEY_PUSHSTATE = "pushstate";
  private static final String KEY_LoginEXTRA1 = "extra1";
  private static final String KEY_LoginEXTRA2 = "extra2";

  private static final String KEY_MAINTITLE = "maintitle";
  private static final String KEY_SUBTITLE = "subtitle";
  private static final String KEY_BIZAREACATEGORY = "bizareacategory";
  private static final String KEY_CATEGORY = "category";
  private static final String KEY_EVENTTYPE1 = "eventtype1";
  private static final String KEY_EVENTTYPE2 = "eventtype2";
  private static final String KEY_EVENTTYPE3 = "eventtype3";
  private static final String KEY_EVENTTYPE4 = "eventtype4";
  private static final String KEY_CREATEDATE = "createdate";
  private static final String KEY_MODIFYDATE = "modifydate";
  private static final String KEY_TOTALIMAGESNUM = "totalimages_num";
  private static final String KEY_FILENAME1 = "filename1";
  private static final String KEY_FILENAME_THUM1 = "filename_thum1";
  private static final String KEY_FILENAME2 = "filename2";
  private static final String KEY_FILENAME_THUM2 = "filename_thum2";
  private static final String KEY_FILENAME3 = "filename3";
  private static final String KEY_FILENAME_THUM3 = "filename_thum3";
  private static final String KEY_FILENAME4 = "filename4";
  private static final String KEY_FILENAME_THUM4 = "filename_thum4";
  private static final String KEY_FILENAME5 = "filename5";
  private static final String KEY_FILENAME_THUM5 = "filename_thum5";
  private static final String KEY_FILEPATH = "filepath";
  private static final String KEY_STATESALE = "statesale";
  private static final String KEY_SALEPRICE = "saleprice";
  private static final String KEY_NORMALPRICE = "normalprice";
  private static final String KEY_STATEMAIN = "statemain";
  private static final String KEY_SALEPERCENT = "salepercent";
  private static final String KEY_STATECOUFON = "statecoufon";
  private static final String KEY_STATEAUCTION1000 = "stateauction1000";
  private static final String KEY_COUFONTOTALNUM = "coufontotalnum";
  private static final String KEY_COUFONSTARTDATE = "coufonstartdate";
  private static final String KEY_COUFONENDDATE = "coufonenddate";
  private static final String KEY_AUCTION1000STARTPRICE = "auction1000startprice";
  private static final String KEY_AUCTION1000STARTDATE = "auction1000startdate";
  private static final String KEY_AUCTION1000ENDDATE = "auction1000enddate";
  private static final String KEY_AUCTION1000MAXPRICE = "auction1000maxprice";
  private static final String KEY_EXTRA1 = "extra1";
  private static final String KEY_EXTRA2 = "extra2";
  private static final String KEY_EXTRA3 = "extra3";
  private static final String KEY_EXTRA4 = "extra4";

  /*
   * TODO 아래 주석된 처리된 이유를 정확하게 파악해야 함
  */
  // 2016. 5. 9. 오전 12:28:37
  // private static final String KEY_UID = "uid";

  public DatabaseHandler(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  // Creating Tables
  @Override
  public void onCreate(SQLiteDatabase db) {
    String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
        + KEY_ID + " INTEGER PRIMARY KEY,"
        + KEY_ADMINKEY + " TEXT,"
        + KEY_ADMINID + " TEXT UNIQUE,"
        + KEY_ADMINPASSWD + " TEXT,"
        + KEY_ADMINEMAIL + " TEXT,"
        + KEY_CREATED_AT + " TEXT,"
        + KEY_UPDATED_AT + " TEXT,"
        + KEY_CATE_BIZ + " TEXT,"
        + KEY_SHOPNAME + " TEXT,"
        + KEY_ZIPCODE1 + " TEXT,"
        + KEY_ZIPCODE2 + " TEXT,"
        + KEY_ADDRESS1 + " TEXT,"
        + KEY_ADDRESS2 + " TEXT,"
        + KEY_ADDRESS3 + " TEXT,"
        + KEY_ADDRESS4 + " TEXT,"
        + KEY_ADDRESS5 + " TEXT,"
        + KEY_ADDRESS6 + " TEXT,"
        + KEY_ADDRESS_DETAIL + " TEXT,"
        + KEY_PHONENUM1 + " TEXT,"
        + KEY_PHONENUM2 + " TEXT,"
        + KEY_PHONENUM3 + " TEXT,"
        + KEY_SHOPICON_URL + " TEXT,"
        + KEY_OPEN_STATE + " TEXT,"
        + KEY_DEVICEID + " TEXT,"
        + KEY_REGID + " TEXT,"
        + KEY_USERPOINT + " TEXT,"
        + KEY_PUSHSTATE + " TEXT,"
        + KEY_LoginEXTRA1 + " TEXT,"
        + KEY_LoginEXTRA2 + " TEXT"
        + ")";
    db.execSQL(CREATE_LOGIN_TABLE);

    String CREATE_ADMINKEY_TABLE = "CREATE TABLE " + TABLE_ADMINKEY + "("
        + KEY_ID + " INTEGER PRIMARY KEY,"
        + KEY_ADMINKEY + " TEXT,"
        + KEY_ADMINID + " TEXT UNIQUE,"
        + KEY_ADMINPASSWD + " TEXT,"
        + KEY_ADMINEMAIL + " TEXT,"
        + KEY_LOGINSTATE + " TEXT,"
        + KEY_SHOPNAME + " TEXT,"
        + KEY_CREATED_AT + " TEXT"
        + ")";
    db.execSQL(CREATE_ADMINKEY_TABLE);

    String CREATE_TABLE_IMAGE = "CREATE TABLE " + TABLE_IMAGE + "("
        + KEY_ID + " integer primary key autoincrement,"
        + KEY_ADMINID + " TEXT,"
        + KEY_SHOPNAME + " TEXT,"
        + KEY_MAINTITLE + " TEXT,"
        + KEY_SUBTITLE + " TEXT,"
        + KEY_BIZAREACATEGORY + " TEXT,"
        + KEY_CATEGORY + " TEXT,"
        + KEY_EVENTTYPE1 + " TEXT,"
        + KEY_EVENTTYPE2 + " TEXT,"
        + KEY_EVENTTYPE3 + " TEXT,"
        + KEY_EVENTTYPE4 + " TEXT,"
        + KEY_CREATEDATE + " NUMERIC,"
        + KEY_MODIFYDATE + " NUMERIC,"
        + KEY_TOTALIMAGESNUM + " TEXT,"
        + KEY_FILENAME1 + " TEXT UNIQUE,"
        + KEY_FILENAME_THUM1 + " TEXT,"
        + KEY_FILENAME2 + " TEXT,"
        + KEY_FILENAME_THUM2 + " TEXT,"
        + KEY_FILENAME3 + " TEXT,"
        + KEY_FILENAME_THUM3 + " TEXT,"
        + KEY_FILENAME4 + " TEXT,"
        + KEY_FILENAME_THUM4 + " TEXT,"
        + KEY_FILENAME5 + " TEXT,"
        + KEY_FILENAME_THUM5 + " TEXT,"
        + KEY_FILEPATH + " TEXT,"
        + KEY_STATESALE + " TEXT,"
        + KEY_SALEPRICE + " TEXT,"
        + KEY_NORMALPRICE + " TEXT,"
        + KEY_STATEMAIN + " TEXT,"
        + KEY_SALEPERCENT + " TEXT,"
        + KEY_STATECOUFON + " TEXT,"
        + KEY_STATEAUCTION1000 + " TEXT,"
        + KEY_COUFONTOTALNUM + " TEXT,"
        + KEY_COUFONSTARTDATE + " TEXT,"
        + KEY_COUFONENDDATE + " TEXT,"
        + KEY_AUCTION1000STARTPRICE + " TEXT,"
        + KEY_AUCTION1000STARTDATE + " TEXT,"
        + KEY_AUCTION1000ENDDATE + " TEXT,"
        + KEY_AUCTION1000MAXPRICE + " TEXT,"
        + KEY_EXTRA1 + " TEXT,"
        + KEY_EXTRA2 + " TEXT,"
        + KEY_EXTRA3 + " TEXT,"
        + KEY_EXTRA4 + " TEXT"
        + ")";
    db.execSQL(CREATE_TABLE_IMAGE);

  }

  /*
   * Upgrading database 데이타베이스를 업그래이드한다..
   */
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

    // Create tables again
    onCreate(db);
  }

  /*
   * Storing user details in database 사용자 정보를 데이타베이스에 저장한다.
   */
  public void regUser(String adminKey, String adminId, String adminPasswd, String adminEmail,
                      String shopName, String created_at) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_ADMINKEY, adminKey); // Name
    values.put(KEY_ADMINID, adminId); // Email
    values.put(KEY_ADMINPASSWD, adminPasswd);
    values.put(KEY_ADMINEMAIL, adminEmail);
    values.put(KEY_SHOPNAME, shopName);
    values.put(KEY_CREATED_AT, created_at); // Created At

    // Inserting Row
    db.insert(TABLE_LOGIN, null, values);
    db.close(); // Closing database connection
  }

  public void addAdminkey(String adminKey, String adminId, String adminPasswd, String adminEmail,
                          String loginstate, String shopName, String created_at) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_ADMINKEY, adminKey); // Name
    values.put(KEY_ADMINID, adminId);
    values.put(KEY_ADMINPASSWD, adminPasswd);
    values.put(KEY_ADMINEMAIL, adminEmail);
    values.put(KEY_LOGINSTATE, loginstate);
    values.put(KEY_SHOPNAME, shopName);
    values.put(KEY_CREATED_AT, created_at); // Created At

    // Inserting Row
    db.insert(TABLE_ADMINKEY, null, values);
    db.close(); // Closing database connection
  }

  public void updateLogoutAdminkey(String logoutstate) {

    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("update " + TABLE_ADMINKEY + " set loginstate = '" + logoutstate + "'");
    db.close(); // Closing database connection
  }

  public void updateLoginAdminkey(String loginstate) {

    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("update " + TABLE_ADMINKEY + " set loginstate = '" + loginstate + "'");
    db.close(); // Closing database connection
  }

  public void updateShopName(String shopName) {

    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("update " + TABLE_LOGIN + " set shopname = '" + shopName + "'");
    db.close(); // Closing database connection
  }

  public void updateBizArea(String bizArea) {

    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("update " + TABLE_LOGIN + " set cate_biz = '" + bizArea + "'");
    db.close(); // Closing database connection
  }

  public void updateShopAddress(String column, String address) {

    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("update " + TABLE_LOGIN + " set " + column + " = '" + address + "'");
    db.close(); // Closing database connection
  }

  /*
   * public void updateShopAddress1(String shopAddress1) {
   *
   * SQLiteDatabase db = this.getWritableDatabase(); db.execSQL("update "+
   * TABLE_LOGIN + " set address1 = '" + shopAddress1 + "'"); db.close(); //
   * Closing database connection } public void updateShopAddress2(String
   * shopAddress2) {
   *
   * SQLiteDatabase db = this.getWritableDatabase(); db.execSQL("update "+
   * TABLE_LOGIN + " set address2 = '" + shopAddress2 + "'"); db.close(); //
   * Closing database connection } public void updateShopAddress3(String
   * shopAddress3) {
   *
   * SQLiteDatabase db = this.getWritableDatabase(); db.execSQL("update "+
   * TABLE_LOGIN + " set address3 = '" + shopAddress3 + "'"); db.close(); //
   * Closing database connection } public void updateShopAddress4(String
   * shopAddress4) {
   *
   * SQLiteDatabase db = this.getWritableDatabase(); db.execSQL("update "+
   * TABLE_LOGIN + " set address4 = '" + shopAddress4 + "'"); db.close(); //
   * Closing database connection } public void updateShopAddress5(String
   * shopAddress5) {
   *
   * SQLiteDatabase db = this.getWritableDatabase(); db.execSQL("update "+
   * TABLE_LOGIN + " set address5 = '" + shopAddress5 + "'"); db.close(); //
   * Closing database connection } public void updateShopAddress6(String
   * shopAddress6) {
   *
   * SQLiteDatabase db = this.getWritableDatabase(); db.execSQL("update "+
   * TABLE_LOGIN + " set address6 = '" + shopAddress6 + "'"); db.close(); //
   * Closing database connection }
  */

  public void updateShopAddressDetail(String shopAddressDetail) {

    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("update " + TABLE_LOGIN + " set address_detail = '" + shopAddressDetail + "'");
    db.close(); // Closing database connection
  }

  public void updateShopIcon(String shopIcon) {
    SQLiteDatabase db = this.getWritableDatabase();

    db.execSQL("update " + TABLE_LOGIN + " set shopicon_url = '" + shopIcon + "' ");
    db.close(); // Closing database connection

  }

  public void updatePhoneNum1(String phoneNum1) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("update " + TABLE_LOGIN + " set phonenum1 = '" + phoneNum1 + "' ");
    db.close(); // Closing database connection
  }

  public void updatePhoneNum2(String phoneNum2) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("update " + TABLE_LOGIN + " set phonenum2 = '" + phoneNum2 + "' ");
    db.close(); // Closing database connection
  }

  public void updatePhoneNum3(String phoneNum3) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("update " + TABLE_LOGIN + " set phonenum3 = '" + phoneNum3 + "' ");
    db.close(); // Closing database connection
  }

  /**
   * Getting user data from database
   */
  public HashMap<String, String> getUserDetails() {
    HashMap<String, String> user = new HashMap<String, String>();
    String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    // Move to first row
    cursor.moveToFirst();
    if (cursor.getCount() > 0) {
      user.put("adminkey", cursor.getString(1));
      user.put("adminid", cursor.getString(2));
      user.put("adminpasswd", cursor.getString(3));
      user.put("uid", cursor.getString(4));
      user.put("created_at", cursor.getString(5));
    }
    cursor.close();
    db.close();
    // return user
    return user;
  }

  /*
   * Getting user login status return true if rows are there in table
   */
  public int getRowCount() {
    String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    int rowCount = cursor.getCount();
    db.close();
    cursor.close();

    return rowCount;
  }

  /*
    내부디비(SQLITE)의 adminkey_tb의 row값을 가져와서 count에 할당.
    어드민키를 획득함
    TABLE_ADMINKEY = "adminkey_tb
    adminkey_tb의 row에  값이 있을 경우에 1, 없을 경우엔 0을 리턴
    2016/10/04
  */
  public int getRowCountAdminkey() {
    String countQuery = "SELECT  * FROM " + TABLE_ADMINKEY;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    int rowCount = cursor.getCount();
    db.close();
    cursor.close();

    return rowCount;
  }

  // 로그인상태를 가져옴
  // TABLE_ADMINKEY = "adminkey_tb
  public String getLoginState() {
    String countQuery = "SELECT loginstate FROM " + TABLE_ADMINKEY;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);

    if (cursor != null) {
      cursor.moveToFirst();
      String loginstate = cursor.getString(0);
      db.close();
      cursor.close();
      return loginstate;

    } else {
      return null;
    }

  }

  public String getAdminid() {
    String countQuery = "SELECT adminid FROM " + TABLE_LOGIN;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);

    if (cursor != null) {
      cursor.moveToFirst();
      String id = cursor.getString(0);
      db.close();
      cursor.close();
      return id;

    } else {
      return null;
    }

  }

  public String getAdminPasswd() {
    String countQuery = "SELECT adminpasswd FROM " + TABLE_LOGIN;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);

    if (cursor != null) {
      cursor.moveToFirst();
      String passwd = cursor.getString(0);
      db.close();
      cursor.close();
      return passwd;

    } else {
      return null;
    }

  }

  public String getShopIcon() {
    String countQuery = "SELECT shopicon_url FROM " + TABLE_LOGIN;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);

    if (cursor != null) {
      cursor.moveToFirst();
      String shopicon = cursor.getString(0);
      db.close();
      cursor.close();
      return shopicon;

    } else {
      return null;
    }

  }

  public String getShopName() {
    String countQuery = "SELECT  shopname FROM " + TABLE_LOGIN;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);

    if (cursor != null) {
      cursor.moveToFirst();
      String shopname = cursor.getString(0);
      db.close();
      cursor.close();
      return shopname;

    } else {
      return null;
    }

  }

  public String getBizArea() {
    String countQuery = "SELECT  cate_biz FROM " + TABLE_LOGIN;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);

    if (cursor != null) {
      cursor.moveToFirst();
      String cate_biz = cursor.getString(0);
      db.close();
      cursor.close();
      return cate_biz;

    } else {
      return null;
    }

  }

  public ArrayList<String> getShopAddress() {
    String selectQuery = "SELECT address1,address2,address3,address4,address5,address6,address_detail FROM "
        + TABLE_LOGIN;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    if (cursor != null) {
      cursor.moveToFirst();
      ArrayList<String> shopaddress = new ArrayList<String>();
      for (int i = 0; i < 7; i++) {
        shopaddress.add(cursor.getString(i));
      }
      db.close();
      cursor.close();
      return shopaddress;

    } else {
      return null;
    }
  }

  public ArrayList<String> getPhoneNum() {
    String selectQuery = "SELECT phonenum1,phonenum2,phonenum3 FROM " + TABLE_LOGIN;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    if (cursor != null) {
      cursor.moveToFirst();
      ArrayList<String> phonenum = new ArrayList<String>();
      for (int i = 0; i < 3; i++) {
        phonenum.add(cursor.getString(i));
      }
      db.close();
      cursor.close();
      return phonenum;

    } else {
      return null;
    }
  }

  /*
   * Re create database Delete all tables and create them again
   */
  public void resetTables() {
    SQLiteDatabase db = this.getWritableDatabase();
    // Delete All Rows
    db.delete(TABLE_LOGIN, null, null);
    db.delete(TABLE_ADMINKEY, null, null);
    db.delete(TABLE_IMAGE, null, null);
    db.close();
  }

  /*
 * Function to logout user
 * Reset Database
 */
  public boolean logoutUser(Context context) {
    DatabaseHandler db = new DatabaseHandler(context);
    db.resetTables();
    return true;
  }


}
