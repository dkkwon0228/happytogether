package com.ilovecat.happytogether.RegisterShopName;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonConstants;
import com.ilovecat.happytogether.Databasehandler.JsonHttpUserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.Normalizer;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 09. happytogether
 */
public class RegisterShopNameSearchActivity extends AppCompatActivity {

  // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
  private static final int CONNECTION_TIMEOUT = 10000;
  private static final int READ_TIMEOUT = 15000;
  private SimpleCursorAdapter myAdapter;

  SearchView searchView = null;
  public static String[] strArrDataId = {""};
  public static String[] strArrDataShopName = {"No Suggestions"};
  public static String[] strArrDataJibunAddress = {""};

  Context context;

  public static String strShopLocation;
  public static String strShopLocationSi;
  public static String strShopLocationGun;


  public static AppCompatActivity appcActivity;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.register_shopname_search_layout);

    appcActivity = this;

    final String[] from = new String[]{"ShopName", "JibunAddress"};
    final int[] to = new int[]{android.R.id.text1, android.R.id.text2};

    context = this;

    Intent intent = getIntent();
    strShopLocation = intent.getExtras().getString("SHOPLOCATION");
    strShopLocationSi = intent.getExtras().getString("SHOPLOCATIONSI");
    strShopLocationGun = intent.getExtras().getString("SHOPLOCATIONGUN");

    // setup SimpleCursorAdapter
    myAdapter = new SimpleCursorAdapter(
        RegisterShopNameSearchActivity.this,
        R.layout.register_shopname_suggestion_layout,
        null,
        from,
        to,
        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);


    // Fetch data from mysql table using AsyncTask
    new GetShopNameAsyncFetch().execute();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // adds item to action bar
    getMenuInflater().inflate(R.menu.search_menu, menu);

    // Get Search item from action bar and Get Search service
    MenuItem searchItem = menu.findItem(R.id.action_search);
    SearchManager searchManager = (SearchManager) RegisterShopNameSearchActivity.this.getSystemService(Context.SEARCH_SERVICE);

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

      searchView.setSearchableInfo(searchManager.getSearchableInfo(RegisterShopNameSearchActivity.this.getComponentName()));
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

          String shopIndexId = cursor.getString(cursor.getColumnIndex("Id"));
          Log.i("sds", shopIndexId);

          JsonHttpUserFunctions jsonHttpUserFunctions = new JsonHttpUserFunctions();
          JSONObject json = jsonHttpUserFunctions.getShopInfoFromShopAddressDB(shopIndexId);


          try {
            if (json.getString(CommonConstants.JSON_KEY_SUCCESS) != null) {
              String res = json.getString(CommonConstants.JSON_KEY_SUCCESS);
              if (Integer.parseInt(res) == 1) {

                JSONObject shopinfos = json.getJSONObject("shopinfos");


                // 상점 전화번호
                String strPhoneNumber = shopinfos.getString("PhoneNumber");
                // 상점 주소 디비에 폰번호가 없는 경우가 있다.
                if (strPhoneNumber == null || strPhoneNumber.length() == 0) {

                  RegisterShopNameActivity.etShopPhoneNumber.setText(R.string.error_empty_shop_phonenumber);
                  RegisterShopNameActivity.etShopPhoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);

                } else {
                  RegisterShopNameActivity.etShopPhoneNumber.setText(strPhoneNumber);

                }

                // 상점 중간 분류
                String strBizAreaDivisionName = shopinfos.getString("BizAreaDivisionName");
                RegisterShopNameActivity.strShopBizAreaDivisionName = strBizAreaDivisionName;


                // 상점 세부업종
                String strBizareaSectionName = shopinfos.getString("BizAreaSectionName");
                RegisterShopNameActivity.etShopBizAreaSectionName.setText(strBizareaSectionName);

                // 도로명 주소
                String strRoadAddress = shopinfos.getString("RoadNameAddress");
                RegisterShopNameActivity.strShopRoadAddress = strRoadAddress;


                // 경위도
                String strLongitude = shopinfos.getString("Longitude");
                String strLatitude = shopinfos.getString("Latitude");
                RegisterShopNameActivity.strLongitude = strLongitude;
                RegisterShopNameActivity.strLatitude = strLatitude;


              }

            }
          } catch (JSONException e) {
            e.printStackTrace();
          }


          searchView.setQuery(cursor.getString(cursor.getColumnIndex("ShopName")), false);
          // 상점 이름 - 지점명 포함
          RegisterShopNameActivity.etShopName.setText(cursor.getString(cursor.getColumnIndex("ShopName")));
          // 상점 지번주소
          RegisterShopNameActivity.etShopJibunAddress.setText(cursor.getString(cursor.getColumnIndex("JibunAddress")));


          // 키보드 내리기
          InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
          final MatrixCursor mc = new MatrixCursor(new String[]{BaseColumns._ID,
              "Id",
              "ShopName",
              "JibunAddress"});
          for (int i = 0; i < strArrDataShopName.length; i++) {

            if (strArrDataShopName[i].toLowerCase().contains(s.toLowerCase())) {

              mc.addRow(new Object[]{i,
                  strArrDataId[i],
                  strArrDataShopName[i],
                  strArrDataJibunAddress[i]});
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



  @Override
  public void onBackPressed() {
    super.onBackPressed();


    //
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
