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
 * Created by dannykwon on 16. 6. 20.
 */

package com.ilovecat.happytogether.Databasehandler;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

public class JsonHttpUserFunctions {
  //JSSONParser Class는 JsonHttpUserFunctions 클래스와 같은 패키지,public으로 선언된 클래스이다.
  private JSONParserHttpURLConnect jsonParserHttpURLConnect;
  //private JSONParser jsonParser;

  private static String updateURL = "http://mytown.ddns.net/android_api/index.php";
  private static String sendReplyPushURL = "http://mytown.ddns.net/android_api/index.php";

  // 회원 상점 등록
  private static String registerUserURL = "http://mytown.ddns.net/android_api/registerUser.php";
  // 아이디 중복 체크
  private static String checkIfExistIdURL = "http://mytown.ddns.net/android_api/checkIfExistId.php";
  // 상점디비 Id로 상점 정보 가져오기
  private static String getShopInfoFromShopAddressDBURL = "http://mytown.ddns.net/android_api/getShopInfoFromShopAddressDB.php";
  // RegId user2 디비에 등록
  private static String registerRegIdInUserDBURL = "http://mytown.ddns.net/android_api/registerRegIdInUserDB.php";
  // RegId gcm_user2 디비에 등록
  private static String registerRegIdOnGCMUserDBURL = "http://mytown.ddns.net/android_api/registerRegIdOnGCMUserDB.php";


  private static String getUserURL = "http://mytown.ddns.net/android_api/getUser.php";

  private static String registerCouponShopURL = "http://mytown.ddns.net/android_api/registerCouponShop.php";

  // User2디비로 부터 사용자 시,구.군동리 주소를 가져온다.
  private static String getMyLocationDetailFromUserDBURL = "http://mytown.ddns.net/android_api/getMyLocationDetailFromUserDB.php";

  // User2디비로 부터 상점 시,구.군동리 주소를 가져온다.
  private static String getShopLocationDetailFromUserDBURL = "http://mytown.ddns.net/android_api/getShopLocationDetailFromUserDB.php";

  private static String API_URL = "http://mytown.ddns.net/android_api/index.php";

  private static String getShopInfoFromShopAddressDB_tag = "getShopInfoFromShopAddressDB";


  private static String login_tag = "login";
  private static String updateAddress_tag = "updateAddress";
  private static String updateShopName_tag = "updateShopName";
  private static String updateBizArea_tag = "updateBizArea";
  private static String updatePhoneNum_tag = "updatePhoneNum";
  private static String registerRegIdInUserDB_tag = "registerGcmRegId";
  private static String updateRegIdOnGCMUserDB_tag = "updateRegIdOnGCMUserDB";
  private static String sendReplyPush_tag = "sendreplypush";



  // constructor
  public JsonHttpUserFunctions() {

    jsonParserHttpURLConnect = new JSONParserHttpURLConnect();
    //jsonParser = new JSONParser();
  }

  /*
   * function make Login Request
   * @param email
   * @param password
   */
  public JSONObject loginUser(String adminId, String adminPasswd) {

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("tag", login_tag);
    params.put("AdminId", adminId);
    params.put("AdminPasswd", adminPasswd);

    JSONObject json = jsonParserHttpURLConnect.makeHttpRequest(API_URL, "POST", params);
    // return json
    // Log.e("JSON", json.toString());
    return json;
  }


  public JSONObject checkIfExistId(String adminId) {

    HashMap<String, String> params = new HashMap<String, String>();
    //params.put("tag", checkIfExistId_tag);
    params.put("AdminId", adminId);

    // checkIfExistIdURL = "http://mytown.ddns.net/android_api/checkIfExistId.php";
    JSONObject jsonObject = jsonParserHttpURLConnect.makeHttpRequest(checkIfExistIdURL, "POST", params);
    return jsonObject;
  }

  public JSONObject getShopInfoFromShopAddressDB(String id) {
    // Building Parameters
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("tag", getShopInfoFromShopAddressDB_tag);
    params.put("Id", id);

    // getting JSON Object
    //getShopInfoFromShopAddressDBURL = "http://mytown.ddns.net/android_api/getShopInfoFromShopAddressDB.php";
    JSONObject jsonObject = jsonParserHttpURLConnect.makeHttpRequest(getShopInfoFromShopAddressDBURL, "POST", params);
    // return json
    return jsonObject;
  }



  public JSONObject getMyLocationDetailFromUserDB(String userId) {
    // Building Parameters
    HashMap<String, String> params = new HashMap<String, String>();
    //params.put("tag", getShopInfoFromShopAddressDB_tag);
    params.put("UserId", userId);

    // getting JSON Object
    JSONObject jsonObject = jsonParserHttpURLConnect.makeHttpRequest(getMyLocationDetailFromUserDBURL, "POST", params);
    // return json
    return jsonObject;
  }

  public JSONObject getShopLocationDetailFromUserDB(String userId) {
    // Building Parameters
    HashMap<String, String> params = new HashMap<String, String>();
    //params.put("tag", getShopInfoFromShopAddressDB_tag);
    params.put("UserId", userId);

    // getting JSON Object
    JSONObject jsonObject = jsonParserHttpURLConnect.makeHttpRequest(getShopLocationDetailFromUserDBURL, "POST", params);
    // return json
    return jsonObject;
  }


  public JSONObject registerUser(String adminKey, String adminId, String adminPassword,
                                  String adminEmail,
                                  String myLocationSi, String myLocationGun, String myLocation,
                                  String shopName,
                                  String bizAreaDivisionName, String bizAreaSectionName,
                                  String shopLocationSi, String shopLocationGun, String shopLocation,
                                  String shopJibunAddress,
                                  String shopRoadAddress,
                                  String shopPhoneNumber,
                                  String shopLongitude, String shopLatitude) {
    // Building Parameters
    HashMap<String, String> params = new HashMap<String, String>();
    //params.put("tag", "registerUser");
    params.put("AdminKey", adminKey);
    params.put("AdminId", adminId);
    params.put("AdminPassword", adminPassword);
    params.put("AdminEmail", adminEmail);
    params.put("ShopName", shopName);
    params.put("MyLocationSi", myLocationSi);
    params.put("MyLocationGun", myLocationGun);
    params.put("MyLocation", myLocation);
    params.put("BizAreaDivisionName", bizAreaDivisionName);
    params.put("BizAreaSectionName", bizAreaSectionName);
    params.put("ShopLocationSi", shopLocationSi);
    params.put("ShopLocationGun", shopLocationGun);
    params.put("ShopLocation", shopLocation);
    params.put("ShopJibunAddress", shopJibunAddress);
    params.put("ShopRoadAddress", shopRoadAddress);
    params.put("ShopPhoneNumber", shopPhoneNumber);
    params.put("ShopLongitude", shopLongitude);
    params.put("ShopLatitude", shopLatitude);
    params.put("UserPoint", "1000"); // 15/12/29  userpoint를 1000으로 초기화

    // getting JSON Object
    JSONObject jsonObject = jsonParserHttpURLConnect.makeHttpRequest(registerUserURL, "POST", params);
    // return json
    return jsonObject;
  }



  public JSONObject registerRegIdInUserDB(String adminId, String regId) {
    // Building Parameters
    HashMap<String, String> params = new HashMap<String, String>();
    //params.put("tag", registerRegIdInUserDB_tag);
    params.put("AdminId", adminId);
    params.put("RegId", regId);
    // getting JSON Object
    JSONObject json = jsonParserHttpURLConnect.makeHttpRequest(registerRegIdInUserDBURL, "POST", params);
    // return json
    return json;
  }

  public JSONObject registerRegIdOnGCMUserDB(String adminId, String shopName, String regId) {
    // Building Parameters
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("AdminId", adminId);
    params.put("ShopName", shopName);
    params.put("RegId", regId);
    // getting JSON Object
    JSONObject json = jsonParserHttpURLConnect.makeHttpRequest(registerRegIdOnGCMUserDBURL, "POST", params);
    // return json
    return json;
  }


  public JSONObject registerCouponShop(
      String couponNumber,
      String shopName,
      String couponImageFileName,
      String couponTitle,
      String couponNotice,
      String couponEndDate,
      String couponNormalPrice,
      String couponSalePrice,
      String couponDescription,
      String couponOpenState) {
    // Building Parameters
    HashMap<String, String> params = new HashMap<String, String>();
    //params.put("tag", "registerUser");
    params.put("CouponNumber", couponNumber);
    params.put("ShopName", shopName);
    params.put("CouponImageFileName", couponImageFileName);
    params.put("CouponTitle", couponTitle);
    params.put("CouponNotice", couponNotice);
    params.put("CouponEndDate", couponEndDate);
    params.put("CouponNormalPrice", couponNormalPrice);
    params.put("CouponSalePrice", couponSalePrice);
    params.put("CouponDescription", couponDescription);
    params.put("CouponOpenState", couponOpenState);

    // getting JSON Object
    JSONObject jsonObject = jsonParserHttpURLConnect.makeHttpRequest(registerCouponShopURL, "POST", params);
    // return json
    return jsonObject;
  }



  public JSONObject getUser() {
    // Building Parameters
    HashMap<String, String> params = new HashMap<String, String>();
    // getting JSON Object
    JSONObject json = jsonParserHttpURLConnect.makeHttpRequest(getUserURL, "POST", params);
    // return json
    return json;
  }

  public JSONObject updateAddress(String adminId
      , String shopAddress, String shopAddressDetail) {
    // Building Parameters

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("tag", updateAddress_tag);
    params.put("AdminId", adminId);
    params.put("ShopAddress", shopAddress);
    params.put("ShopAddressDetail", shopAddressDetail);


    // getting JSON Object
    JSONObject json = jsonParserHttpURLConnect.makeHttpRequest(updateURL, "POST", params);
    // return json
    return json;
  }

  public JSONObject updateShopName(String adminId, String shopName) {
    // Building Parameters

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("tag", updateShopName_tag);
    params.put("AdminId", adminId);
    params.put("ShopName", shopName);
    // getting JSON Object
    JSONObject json = jsonParserHttpURLConnect.makeHttpRequest(updateURL, "POST", params);
    // return json
    return json;
  }

  public JSONObject updateBizArea(String adminId, String bizArea) {
    // Building Parameters

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("tag", updateBizArea_tag);
    params.put("AdminId", adminId);
    params.put("BizArea", bizArea);
    // getting JSON Object
    JSONObject json = jsonParserHttpURLConnect.makeHttpRequest(updateURL, "POST", params);
    // return json
    return json;
  }

  public JSONObject sendReplyPush(String sendId, String recieveId, String reply, String photoName) {
    // Building Parameters

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("tag", sendReplyPush_tag);
    params.put("SendId", sendId);
    params.put("RecieveId", recieveId);
    params.put("Reply", reply);
    params.put("PhotoName", photoName);
    // getting JSON Object
    JSONObject json = jsonParserHttpURLConnect.makeHttpRequest(sendReplyPushURL, "POST", params);
    // return json
    return json;
  }

  public JSONObject updatePhoneNum(String adminId, String PhoneNum1, String PhoneNum2, String PhoneNum3) {
    // Building Parameters

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("tag", updatePhoneNum_tag);
    params.put("AdminId", adminId);
    params.put("PhoneNum1", PhoneNum1);
    params.put("PhoneNum2", PhoneNum2);
    params.put("PhoneNum3", PhoneNum3);
    // getting JSON Object
    JSONObject json = jsonParserHttpURLConnect.makeHttpRequest(updateURL, "POST", params);
    // return json
    return json;
  }


  /*
   * Function get Login status
   */
  public boolean isUserLoggedIn(Context context) {
    DatabaseHandler db = new DatabaseHandler(context);
    int count = db.getRowCount();
    if (count > 0) {
      // user logged in
      return true;
    }
    return false;
  }

  /*
   * Function to logout user
   * Reset Database
   */
  public static boolean logoutUser(Context context) {
    DatabaseHandler db = new DatabaseHandler(context);
    db.resetTables();
    return true;
  }
}