package com.ilovecat.happytogether.MainShopListingCoupon;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 17. happytogether
 */
public class ListingCouponShopAsyncFetch_tempsave extends AsyncTask<String, String,  List<ShopItem>> {

  ProgressDialog pdLoading = new ProgressDialog(MainCouponShopFragment.thiscontext);
  HttpURLConnection conn;
  URL url = null;

  @Override
  protected void onPreExecute() {
    super.onPreExecute();

    //this method will be running on UI thread
    pdLoading.setMessage("\tLoading...");
    pdLoading.setCancelable(false);
    pdLoading.show();

  }

  @Override
  protected  List<ShopItem> doInBackground(String... params) {

    List<ShopItem> arrayOfData = new ShopNamesParser().getDataAll(params[0]);
    // 배열을 뒤집어 리스트뷰를 역순으로 정렬
    Collections.reverse(arrayOfData);
    return arrayOfData;

  }

  @Override
  protected void onPostExecute(List<ShopItem> result) {


    //this method will be running on UI thread
    ArrayList<String> dataListCouponImageUrl = new ArrayList<String>();
    ArrayList<String> dataListCouponTitle = new ArrayList<String>();
    ArrayList<String> dataListCouponNotice1 = new ArrayList<String>();


    pdLoading.dismiss();


    if (null == result || result.size() == 0) {


    } else {

      for (int i = 0; i <  result.size(); i++) {
        dataListCouponImageUrl.add(result.get(i).getId());
        dataListCouponTitle.add("111aasa11sdsaasassasdsa");
        dataListCouponNotice1.add("111adsad");
      }

      MainCouponShopFragment.strArrDataCouponImageUrl_tempsave = dataListCouponImageUrl.toArray(new String[dataListCouponImageUrl.size()]);
      MainCouponShopFragment.strArrDataCouponTitle_tempsave = dataListCouponTitle.toArray(new String[dataListCouponTitle.size()]);
      MainCouponShopFragment.strArrDataCouponNotice1_tempsave = dataListCouponNotice1.toArray(new String[dataListCouponNotice1.size()]);

      MainCouponShopFragment.prepareAlbums_tempsave();

    }

  }



}
