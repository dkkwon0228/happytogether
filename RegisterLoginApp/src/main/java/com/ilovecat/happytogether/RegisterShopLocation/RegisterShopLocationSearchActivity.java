package com.ilovecat.happytogether.RegisterShopLocation;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonConstants;
import com.ilovecat.happytogether.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 09. happytogether
 */
public class RegisterShopLocationSearchActivity extends AppCompatActivity {

  // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
  private SimpleCursorAdapter myAdapter;

  SearchView searchView = null;
  private String[] strArrDataSiGu = {"No Suggestions"};
  private String[] strArrDataDong = {""};

  Context context;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.register_shoplocation_search_layout);

    final String[] from = new String[]{"Dong", "Si"};
    final int[] to = new int[]{android.R.id.text1, android.R.id.text2};

    context = this;



    // setup SimpleCursorAdapter
    myAdapter = new SimpleCursorAdapter(
        RegisterShopLocationSearchActivity.this,
        R.layout.register_mylocation_suggestion_layout,
        null,
        from,
        to,
        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);


    // Fetch data from mysql table using AsyncTask
    new AsyncFetch().execute();
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // adds item to action bar
    getMenuInflater().inflate(R.menu.search_menu, menu);

    // Get Search item from action bar and Get Search service
    MenuItem searchItem = menu.findItem(R.id.action_search);
    SearchManager searchManager = (SearchManager) RegisterShopLocationSearchActivity.this.getSystemService(Context.SEARCH_SERVICE);

    final Handler mHandler = new Handler();

    if (searchItem != null) {
      searchView = (SearchView) searchItem.getActionView();

    }
    if (searchView != null) {

      // Cusor Color White
      final EditText editTextView = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

      try {
        Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
        mCursorDrawableRes.setAccessible(true);
        mCursorDrawableRes.set(editTextView, 0); // 0: @null (automatic), or any drawable of yours.
      } catch (Exception e) {
        e.printStackTrace();
      }

      searchView.setSearchableInfo(searchManager.getSearchableInfo(RegisterShopLocationSearchActivity.this.getComponentName()));
      searchView.setFocusable(true);
      searchView.setIconified(false);
      searchView.requestFocusFromTouch();

      searchView.setSuggestionsAdapter(myAdapter);
      // Getting selected (clicked) item suggestion
      searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
        @Override
        public boolean onSuggestionClick(int position) {

          // Add clicked text to search box
          CursorAdapter ca = searchView.getSuggestionsAdapter();
          Cursor cursor = ca.getCursor();
          cursor.moveToPosition(position);
          searchView.setQuery(cursor.getString(cursor.getColumnIndex("Dong")), false);
          RegisterShopLocationActivity.etShopLocation.setText(cursor.getString(cursor.getColumnIndex("Dong")));
          RegisterShopLocationActivity.etShopLocationSiGun.setText(cursor.getString(cursor.getColumnIndex("Si")));


          //
          InputMethodManager imm= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
          imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

              finish();
              overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

              mHandler.sendEmptyMessage(0);
            }
          }, 700);

          return true;
        }

        @Override
        public boolean onSuggestionSelect(int position) {
          return true;
        }
      });
      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String s) {

          return false;
        }

        @Override
        public boolean onQueryTextChange(final String s) {

          // Filter data
          final MatrixCursor mc = new MatrixCursor(new String[]{BaseColumns._ID, "Si", "Dong"});
          for (int i = 0; i < strArrDataDong.length; i++) {


            if (strArrDataDong[i].toLowerCase().startsWith(s.toLowerCase())) {

              mc.addRow(new Object[]{i, strArrDataSiGu[i], strArrDataDong[i]});

            } else if (strArrDataDong[i].toLowerCase().endsWith(s.toLowerCase())) {

              mc.addRow(new Object[]{i, strArrDataSiGu[i], strArrDataDong[i]});


            } else if (strArrDataDong[i].toLowerCase().contains(s.toLowerCase())) {

              mc.addRow(new Object[]{i, strArrDataSiGu[i], strArrDataDong[i]});

            }

          }

          myAdapter.changeCursor(mc);
          return false;


        }
      });



    }

    return true;
  }





  public static CharSequence highlightText(String search, String originalText) {
    if (search != null && !search.equalsIgnoreCase("")) {
      String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
      int start = normalizedText.indexOf(search);
      if (start < 0) {
        return originalText;
      } else {
        Spannable highlighted = new SpannableString(originalText);
        while (start >= 0) {
          int spanStart = Math.min(start, originalText.length());
          int spanEnd = Math.min(start + search.length(), originalText.length());
          highlighted.setSpan(new ForegroundColorSpan(Color.BLUE), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
          start = normalizedText.indexOf(search, spanEnd);
        }
        return highlighted;
      }
    }
    return originalText;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    return super.onOptionsItemSelected(item);
  }


  // Every time when you press search button on keypad an Activity is recreated which in turn calls this function
  @Override
  protected void onNewIntent(Intent intent) {

    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      String query = intent.getStringExtra(SearchManager.QUERY);
      if (searchView != null) {
        searchView.clearFocus();
      }

      // User entered text and pressed search button. Perform task ex: fetching data from database and display

    }
  }

  // Create class AsyncFetch
  private class AsyncFetch extends AsyncTask<String, String, String> {

    ProgressDialog pdLoading = new ProgressDialog(RegisterShopLocationSearchActivity.this);
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
        url = new URL(CommonConstants.search_location_url);

      } catch (MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return e.toString();
      }
      try {

        // Setup HttpURLConnection class to send and receive data from php and mysql
        conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(CommonConstants.READ_TIMEOUT);
        conn.setConnectTimeout(CommonConstants.CONNECTION_TIMEOUT);
        conn.setRequestMethod("GET");

        // setDoOutput to true as we receive data
        conn.setDoOutput(true);
        conn.connect();

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

          while ((line = reader.readLine()) != null) {
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
      ArrayList<String> dataListSiGu = new ArrayList<String>();
      ArrayList<String> dataListDong = new ArrayList<String>();

      pdLoading.dismiss();


      if (result.equals("no rows")) {

        // Do some action if no data from database

      } else {

        try {

          JSONArray jArray = new JSONArray(result);

          // Extract data from json and store into ArrayList
          for (int i = 0; i < jArray.length(); i++) {
            JSONObject json_data = jArray.getJSONObject(i);

            dataListSiGu.add(json_data.getString("Si") + " " + json_data.getString("Gu"));
            dataListDong.add(json_data.getString("Dong"));
          }

          strArrDataSiGu = dataListSiGu.toArray(new String[dataListSiGu.size()]);
          strArrDataDong = dataListDong.toArray(new String[dataListDong.size()]);

        } catch (JSONException e) {
          // You to understand what actually error is and handle it appropriately
          Toast.makeText(RegisterShopLocationSearchActivity.this, e.toString(), Toast.LENGTH_LONG).show();
          Toast.makeText(RegisterShopLocationSearchActivity.this, result.toString(), Toast.LENGTH_LONG).show();
        }

      }

    }

  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    //
    InputMethodManager imm= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

    final Handler mHandler = new Handler();
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {

        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        mHandler.sendEmptyMessage(0);
      }
    }, 700);
  }
}
