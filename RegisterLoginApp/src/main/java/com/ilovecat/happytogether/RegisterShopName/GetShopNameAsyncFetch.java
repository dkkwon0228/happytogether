package com.ilovecat.happytogether.RegisterShopName;

import android.app.ProgressDialog;
import android.os.AsyncTask;

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

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 17. happytogether
 */
public class GetShopNameAsyncFetch extends AsyncTask<String, String, String> {

  ProgressDialog pdLoading = new ProgressDialog(RegisterShopNameSearchActivity.appcActivity);
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
  protected String doInBackground(String... params) {


    try {

      // Enter URL address where your php file resides or your JSON file address
      url = new URL(CommonConstants.search_shopname_url);

    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return e.toString();
    }


    try {

      // Setup HttpURLConnection class to send and receive data from php and mysql
      conn = (HttpURLConnection) url.openConnection();


      // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
      conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
      conn.setRequestMethod("POST");
      conn.setDefaultUseCaches(false);
      conn.setDoInput(true);                         // 서버에서 읽기 모드 지정
      conn.setDoOutput(true);                       // 서버로 쓰기 모드 지정
      conn.setReadTimeout(CommonConstants.READ_TIMEOUT);
      conn.setConnectTimeout(CommonConstants.CONNECTION_TIMEOUT);


      String param = "SHOPLOCATION=" + RegisterShopNameSearchActivity.strShopLocation
          + "&SHOPLOCATIONSI=" + RegisterShopNameSearchActivity.strShopLocationSi
          + "&SHOPLOCATIONGUN=" + RegisterShopNameSearchActivity.strShopLocationGun;
      OutputStream opstrm = conn.getOutputStream();
      opstrm.write(param.getBytes());
      opstrm.flush();
      opstrm.close();

    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
      return e1.toString();
    }

    try {

      int response_code = conn.getResponseCode();

      // Check if successful connection made
      if (response_code == HttpURLConnection.HTTP_OK) {

        // Read data sent from server
        InputStream input = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder result = new StringBuilder();
        String line;



        while (true) {
          line = reader.readLine();
          if (line == null) break;

          result.append(line);
        }

        // Pass data to onPostExecute method
        return (result.toString());

      } else {
        return ("Connection error");
      }

    } catch (IOException e) {
      e.printStackTrace();
      return e.toString();
    } finally {
      conn.disconnect();
    }


  }

  @Override
  protected void onPostExecute(String result) {

    //this method will be running on UI thread
    ArrayList<String> dataListId = new ArrayList<String>();
    ArrayList<String> dataListShopName = new ArrayList<String>();
    ArrayList<String> dataListJibunAddress = new ArrayList<String>();

    pdLoading.dismiss();


    if (result.equals("no rows")) {

      // Do some action if no data from database

    } else {

      try {

        JSONArray jArray = new JSONArray(result);

        // Extract data from json and store into ArrayList
        for (int i = 0; i < jArray.length(); i++) {
          JSONObject json_data = jArray.getJSONObject(i);

          dataListId.add(json_data.getString("Id"));
          dataListShopName.add(json_data.getString("ShopName")
              + " "
              + json_data.getString("ShopSpotName"));
          dataListJibunAddress.add(json_data.getString("JibunAddress"));
        }



        RegisterShopNameSearchActivity.strArrDataId = dataListId.toArray(new String[dataListId.size()]);
        RegisterShopNameSearchActivity.strArrDataShopName = dataListShopName.toArray(new String[dataListShopName.size()]);
        RegisterShopNameSearchActivity.strArrDataJibunAddress = dataListJibunAddress.toArray(new String[dataListJibunAddress.size()]);

      } catch (JSONException e) {
        // You to understand what actually error is and handle it appropriately
        //Toast.makeText(RegisterShopNameSearchActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        //Toast.makeText(RegisterShopNameSearchActivity.this, result.toString(), Toast.LENGTH_LONG).show();
      }

    }

  }

}
