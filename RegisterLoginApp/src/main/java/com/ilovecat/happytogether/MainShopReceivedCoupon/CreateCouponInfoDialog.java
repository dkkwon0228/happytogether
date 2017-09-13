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

package com.ilovecat.happytogether.MainShopReceivedCoupon;

import static android.view.View.INVISIBLE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ilovecat.happytogether.R;

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

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 06. 29. happytogether
 */
public class CreateCouponInfoDialog implements AbsListView.OnScrollListener {


  // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
  private static final int CONNECTION_TIMEOUT = 10000;
  private static final int READ_TIMEOUT = 15000;
  private static AppCompatActivity actActivity;


  private static ArrayAdapter<String> strArrayAdapter;
  private static Dialog dialog;


  private static ListView listview;

  private static boolean mLockListView;

  private static ProgressBar mProgressBarLoadMore;

  private static FrameLayout footer_fi;

  // Flag for current page
  private static int stepCount; //0, 20, 40, 60....증가
  private static int visibleListViewItemCount = 20; //20개씩 보여준다

  private static final String rssFeed = "http://mytown.noip.me/search/getUser.php";


  public static void createCouponInfoDialog(AppCompatActivity activity) {

    actActivity = activity;


    dialog = new Dialog(actActivity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
    dialog.setContentView(R.layout.main_inner_shop_coupon_receive_popupdialog);


    strArrayAdapter = new ArrayAdapter<String>(actActivity, R.layout.main_inner_shop_coupon_recieve_item);


    listview = (ListView) dialog.findViewById(R.id.lv);

    LayoutInflater inflater = LayoutInflater.from(dialog.getContext());
    footer_fi = (FrameLayout) inflater.inflate(R.layout.main_inner_shop_coupon_recieve_footer_more, null);
    listview.addFooterView(footer_fi);

    mProgressBarLoadMore = (ProgressBar) footer_fi.findViewById(R.id.load_more_progressBar);


    final Handler mHandler = new Handler();
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {

        // Fetch data from mysql table using AsyncTask
        new AsyncFetch().execute(rssFeed);

        mHandler.sendEmptyMessage(0);
      }
    }, 500);

    /*
    listviewEmailAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = (ListView) parent;

        String item = (String) listView.getItemAtPosition(position);
        RegisterEmailActivity.etEmailAccount.setText(item);

        String[] arr = actActivity.getResources().getStringArray(R.array.array_email_account);
        if (position == (arr.length - 1)) {
          RegisterEmailActivity.etCustomEmailAccount.setText(null);
          RegisterEmailActivity.etCustomEmailAccount.setVisibility(View.VISIBLE);
        } else {
          String tempEmailAccount = actActivity.getResources().getString(R.string.temp_email_account);
          RegisterEmailActivity.etCustomEmailAccount.setText(tempEmailAccount);
          RegisterEmailActivity.etCustomEmailAccount.setVisibility(View.GONE);
        }

        dialog.cancel();
      }
    });
    */

    Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
    // if button is clicked, close the register_email_popupdialog_layout.xmlayout.xml dialog
    dialogButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {

        //stepCount = 0;
        dialog.cancel();


      }
    });

    dialog.show();

  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {

  }

  @Override
  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

  }


  // Create class AsyncFetch
  private static class AsyncFetch extends AsyncTask<String, String, String> {

    ProgressDialog pdLoading = new ProgressDialog(dialog.getContext());
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

        String xmlPagingURL = params[0];

        // Enter URL address where your php file resides or your JSON file address
        url = new URL(xmlPagingURL);

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
        conn.setReadTimeout(READ_TIMEOUT);
        conn.setConnectTimeout(CONNECTION_TIMEOUT);


        //######################
        //매우 중요 0으로 반드시 초기화 해주어야 한다.
        stepCount = 0;
        //#####################


        int initLimit = stepCount;
        int endLimit = visibleListViewItemCount;


        String param = "initLIMIT=" + String.valueOf(initLimit) + "&endLIMIT=" + String.valueOf(endLimit);
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

          /*
          while ((line = reader.readLine()) != null) {
            result.append(line);
          }
          */

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


      pdLoading.dismiss();


      if (result.equals("no rows")) {

        // Do some action if no data from database

      } else {

        try {

          JSONArray jArray = new JSONArray(result);


          int total_xmldata_inx = jArray.length();


          if (total_xmldata_inx >= visibleListViewItemCount) {

            footer_fi.setVisibility(VISIBLE);
            mProgressBarLoadMore.setVisibility(VISIBLE);


          } else {

            footer_fi.setVisibility(INVISIBLE);
            mProgressBarLoadMore.setVisibility(INVISIBLE);


          }

          // Extract data from json and store into ArrayList
          for (int i = 0; i < jArray.length(); i++) {
            JSONObject json_data = jArray.getJSONObject(i);

            strArrayAdapter.add(json_data.getString("AdminId"));

          }


          listview.setOnScrollListener(scrollListener);


          listview.setAdapter(strArrayAdapter);



        } catch (JSONException e) {
          // You to understand what actually error is and handle it appropriately
        }

      }

    }

  }


  private static AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

      int count = totalItemCount - visibleItemCount;

      if (firstVisibleItem >= count && totalItemCount != 0
          && !mLockListView) {
        Log.i("MAIN in onScroll", "Loading next items");

        stepCount = stepCount + visibleListViewItemCount;

        // 매우중요
        // 데이타 통신 전에 락을 걸어야 함
        mLockListView = true;

        Runnable run = new Runnable()
        {
          @Override
          public void run() {

            new loadMoreListView().execute(rssFeed);

            //락을 해제함
            mLockListView = false;
          }
        };

        // 속도의 딜레이를 구현하기 위한 꼼수
        Handler handler = new Handler();
        handler.postDelayed(run, 250);

      }
    }
  };


  private static class loadMoreListView extends AsyncTask<String, String, String> {


    HttpURLConnection conn;
    URL url = null;

    @Override
    protected void onPreExecute() {
      super.onPreExecute();

    }


    @Override
    protected String doInBackground(final String... params) {

      try {

        //current_page += 1;

        String xmlPagingURL = params[0];

        // Enter URL address where your php file resides or your JSON file address
        url = new URL(xmlPagingURL);

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
        conn.setReadTimeout(READ_TIMEOUT);
        conn.setConnectTimeout(CONNECTION_TIMEOUT);


        int initLimit = stepCount;
        int endLimit = visibleListViewItemCount;


        String param = "initLIMIT=" + String.valueOf(initLimit) + "&endLIMIT=" + String.valueOf(endLimit);
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

          /*
          while ((line = reader.readLine()) != null) {
            result.append(line);
          }
          */

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


      if (result.equals("no rows")) {



      } else {

        try {

          JSONArray jArray = new JSONArray(result);

          int total_xmldata_inx = jArray.length();

          if (total_xmldata_inx >= visibleListViewItemCount) {

            footer_fi.setVisibility(VISIBLE);
            mProgressBarLoadMore.setVisibility(VISIBLE);

          } else {

            footer_fi.setVisibility(INVISIBLE);
            mProgressBarLoadMore.setVisibility(INVISIBLE);

          }


          // Extract data from json and store into ArrayList
          for (int i = 0; i < jArray.length(); i++) {
            JSONObject json_data = jArray.getJSONObject(i);

            strArrayAdapter.add(json_data.getString("AdminId"));

          }


          strArrayAdapter.notifyDataSetChanged();


        } catch (JSONException e) {
          // You to understand what actually error is and handle it appropriately
        }

      }

    }
  }


}
