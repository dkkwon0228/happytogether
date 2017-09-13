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

package com.ilovecat.happytogether.Main;

import static com.google.android.gms.common.api.GoogleApiClient.Builder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AddPlaceRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.ilovecat.happytogether.Databasehandler.DatabaseHandler;
import com.ilovecat.happytogether.MainSearch;
import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonConstants;

import java.util.Collections;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 06. 30. happytogether
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

  // Google Place Picker
  int status;

  private GoogleApiClient mGoogleApiClient;

  private static final int PLACE_PICKER_REQUEST = 199;
  private PlacePicker.IntentBuilder builder;

  @SuppressLint("SetJavaScriptEnabled")
  private PagerSlidingTabStrip tabs_shop;
  private ViewPager pager_shop;
  private MyPagerAdapter_shop adapter_shop;

  private PagerSlidingTabStrip tabs_my;
  private ViewPager pager_my;
  private MyPagerAdapter_my adapter_my;

  static AppCompatActivity appcActivity;


  public static String userIdFromInnerDB;
  public static String shopNameFromInnerDB;



  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_layout);

    appcActivity = this;


    /*
      * 내부디비(SQLITE)의 사용자 아이디를 가져온다.
     */
    DatabaseHandler handlerDatabase = new DatabaseHandler(getBaseContext());
    userIdFromInnerDB = handlerDatabase.getAdminid();
    shopNameFromInnerDB = handlerDatabase.getShopName();
    handlerDatabase.close();



    mGoogleApiClient = new Builder(this)
        .addApi(Places.GEO_DATA_API)
        .addApi(Places.PLACE_DETECTION_API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();

    /*
    Intent intent = getIntent();
    String strAdminId = intent.getExtras().getString("ADMINID");
    String strAdminPassword = intent.getExtras().getString("ADMINPASSWORD");
    String strAdminEmail = intent.getExtras().getString("ADMINEMAIL");

    String strMyLocation = intent.getExtras().getString("MYLOCATION");
    String strMyLocationSi = intent.getExtras().getString("MYLOCATIONSI");


    String strShopLocation = intent.getExtras().getString("SHOPLOCATION");
    String strShopLocationSi = intent.getExtras().getString("SHOPLOCATIONSI");


    String strShopName = intent.getExtras().getString("SHOPNAME");
    String strBizAreaDivisionName = intent.getExtras().getString("SHOPBIZAREADIVISIONNAME");
    String strBizAreaSectionName = intent.getExtras().getString("SHOPBIZAREASECTIONNAME");


    String mShopAddress = intent.getExtras().getString("SHOP_ADDRESS");
    String mShopDetailAddress = intent.getExtras().getString("SHOP_DETAIL_ADDRESS");
    String mShopLatLng = intent.getExtras().getString("STR_SHOP_LATLNG");
    String mShopLat = intent.getExtras().getString("SHOP_LAT");
    String mShopLng = intent.getExtras().getString("SHOP_LNG");
    String mShopBusinessNum = intent.getExtras().getString("SHOP_BUSINESS_NUM");
    String mBusinessArea1 = intent.getExtras().getString("SHOP_BUSINESS_AREA1");
    String mBusinessArea2 = intent.getExtras().getString("SHOP_BUSINESS_AREA2");


    LatLng shopLatLng = new LatLng(Double.parseDouble(mShopLat),Double.parseDouble(mShopLng));


    Geocoder geocoder;
    List<Address> addresses = null;
    geocoder = new Geocoder(this, Locale.getDefault());

    try {
      addresses = geocoder.getFromLocation(Double.parseDouble(mShopLat),Double.parseDouble(mShopLng), 1);
      // Here 1 represent max location result to returned, by documents it recommended 1 to 5
    } catch (IOException e) {
      e.printStackTrace();
    }


    String address = addresses.get(0).getAddressLine(0);
    // If any additional address line present than only,
    // check with max available address lines by getMaxAddressLineIndex()
    String city = addresses.get(0).getLocality();
    String state = addresses.get(0).getAdminArea();
    String country = addresses.get(0).getCountryName();
    //String postalCode = addresses.get(0).getPostalCode();
    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL


    Log.i(CommonConstants.GOOGLE_API_LOGTAG, address);
    Log.i(CommonConstants.GOOGLE_API_LOGTAG, city);


    Button mButton = (Button) findViewById(R.id.button);

    mButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addPlaceToGoogleDbAndToTrip();

      }
    });

    */

    // Place Picker
    /*
    status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(appcActivity);
    if (status != ConnectionResult.SUCCESS) {
      if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
        GooglePlayServicesUtil.getErrorDialog(status, appcActivity,
            100).show();
      }
    }


    if (status == ConnectionResult.SUCCESS) {

      builder = new PlacePicker.IntentBuilder();
      //builder.setLatLngBounds(new LatLngBounds(new LatLng(39.916374, -105.112337), new LatLng(39.939552, -105.078779)));
      builder.setLatLngBounds(toBounds(shopLatLng, 200));
      //Context context = this;

      Button mButtonGoMap = (Button) findViewById(R.id.button_viewmap);
      mButtonGoMap.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          try {

            Intent intent = builder.build(CouponShopListingActivity.this);
            // Start the Intent by requesting a result, identified by a request code.
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
            //startActivityForResult(builder.build(appcActivity), PLACE_PICKER_REQUEST);

          } catch (GooglePlayServicesRepairableException e) {
            GooglePlayServicesUtil
                .getErrorDialog(e.getConnectionStatusCode(), CouponShopListingActivity.this, 0);
          } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(CouponShopListingActivity.this, "Google Play Services is not available.",
                Toast.LENGTH_LONG)
                .show();
          }
        }
      });
    }
    */



    Button mButtonSearch = (Button) findViewById(R.id.search);

    mButtonSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        Intent dashboard = new Intent(getApplicationContext(), MainSearch.class);
        startActivity(dashboard);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

      }
    });



    /*
    // AutoComplete
    final TextView placeDetails = (TextView) findViewById(R.id.txtPlaceDetails);

    PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
        getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

    // The following code example shows setting an AutocompleteFilter on a PlaceAutocompleteFragment to
    // set a filter returning only results with a precise address.
    //AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
    //    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
    //    .build();
    //autocompleteFragment.setFilter(typeFilter);

    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
      @Override
      public void onPlaceSelected(Place place) {
        // TODO: Get info about the selected place.
        Log.i(CommonConstants.GCM_LOGTAG, "Place: " + place.getName());

        String placeDetailsStr = place.getName() + "\n"
            + place.getId() + "\n"
            + place.getLatLng().toString() + "\n"
            + place.getAddress() + "\n"
            + place.getAttributions();
        placeDetails.setText(placeDetailsStr);


      }

      @Override
      public void onError(Status status) {
        // TODO: Handle the error.
        Log.i(CommonConstants.GCM_LOGTAG, "An error occurred: " + status);
      }
    });
    */

    tabs_shop = (PagerSlidingTabStrip) findViewById(R.id.tabs_shop);
    pager_shop = (ViewPager) findViewById(R.id.pager_shop);
    adapter_shop = new MyPagerAdapter_shop(getSupportFragmentManager());

    pager_shop.setAdapter(adapter_shop);

    final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
        getResources().getDisplayMetrics());
    pager_shop.setPageMargin(pageMargin);

    tabs_shop.setViewPager(pager_shop);

    // Attach the page change listener to tab strip and **not** the view
    // pager_shop inside the activity
    tabs_shop.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

      // This method will be invoked when a new page becomes selected.
      @Override
      public void onPageSelected(int position) {
        //Toast.makeText(CouponShopListingActivity.this, "Selected page position: " + position, Toast.LENGTH_SHORT).show();
      }

      // This method will be invoked when the current page is scrolled
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Code goes here
      }

      // Called when the scroll state changes:
      // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
      @Override
      public void onPageScrollStateChanged(int state) {
        // Code goes here
      }
    });




    tabs_my = (PagerSlidingTabStrip) findViewById(R.id.tabs_my);
    pager_my = (ViewPager) findViewById(R.id.pager_my);
    adapter_my = new MyPagerAdapter_my(getSupportFragmentManager());

    pager_my.setAdapter(adapter_my);

    final int pageMargin_my = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
        getResources().getDisplayMetrics());
    pager_my.setPageMargin(pageMargin_my);

    tabs_my.setViewPager(pager_my);

    // Attach the page change listener to tab strip and **not** the view
    // pager_shop inside the activity
    tabs_my.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

      // This method will be invoked when a new page becomes selected.
      @Override
      public void onPageSelected(int position) {
        //Toast.makeText(CouponShopListingActivity.this, "Selected page position: " + position, Toast.LENGTH_SHORT).show();
      }

      // This method will be invoked when the current page is scrolled
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Code goes here
      }

      // Called when the scroll state changes:
      // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
      @Override
      public void onPageScrollStateChanged(int state) {
        // Code goes here
      }
    });



  }

  // Place Picker
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case PLACE_PICKER_REQUEST:
          //process Intent......
          Place place = PlacePicker.getPlace(data, this);
          String toastMsg = String.format("Place: %s", place.getAddress());
          String toastMsg2 = String.format("Phone: %s", place.getPhoneNumber());
          String toastMsg3 = String.format("PlaceName: %s", place.getName());
          Toast.makeText(this, toastMsg2, Toast.LENGTH_LONG).show();
          break;

        default:
          status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
          break;

      }


    }
  }

  public LatLngBounds toBounds (LatLng center, double radius) {
    LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
    LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
    return new LatLngBounds(southwest, northeast);
  }



  private void addPlaceToGoogleDbAndToTrip() {
    final Double lat = 35.1978016;
    final Double lng = 129.1154027;
    final String placeName = "돈주까예본점";
    final String address = "부산광역시 해운대구 반여동 1411-18";
    final String website = "";
    final String phoneNumber = "+82 51-521-3434";
    LatLng latlng = new LatLng(lat, lng);

    AddPlaceRequest place =
        new AddPlaceRequest(
            placeName, // Name
            latlng, // Latitude and longitude
            address, // Address
            Collections.singletonList(Place.TYPE_RESTAURANT), // Place types
            phoneNumber, // Phone number
            Uri.parse(website) // Website
        );

    Places.GeoDataApi.addPlace(mGoogleApiClient, place).setResultCallback(new ResultCallback<PlaceBuffer>() {
      @Override
      public void onResult(PlaceBuffer places) {

        if (!places.getStatus().isSuccess()) {

          Log.e(CommonConstants.GCM_LOGTAG, "Place query did not complete. Error: " + places.getStatus().toString());
          places.release();
          return;
        }

        final Place place = places.get(0);
        //newPlaceID = place.getId();
        Log.i(CommonConstants.GCM_LOGTAG, "Place add result Id: " + place.getId());
        Log.i(CommonConstants.GCM_LOGTAG, "Place add result Name: " + place.getName());
        Log.i(CommonConstants.GCM_LOGTAG, "Place add result Address: " + place.getAddress());
        Log.i(CommonConstants.GCM_LOGTAG, "Place add result PhoneNum: " + place.getPhoneNumber());
        places.release();

      }
    });
  }


  @Override
  protected void onStart() {
    super.onStart();
    if (mGoogleApiClient != null)
      mGoogleApiClient.connect();
  }

  @Override
  protected void onStop() {
    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
      mGoogleApiClient.disconnect();
    }
    super.onStop();
  }

  @Override
  public void onConnected(Bundle bundle) {

  }

  @Override
  public void onConnectionSuspended(int i) {

  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {

  }



  /**
   * <pre>
   * 1. 패키지명 : com.ilovecat.happytogether
   * 2. 타입명 :  MyPagerAdapter_shop CouponShopListingActivity.java
   * 3. 작성일 : 2016. 5. 8. 오후 8:36:05
   * 4. 작성자 : dannykwon
   * 5. 설명 :
   * </pre>
   */
  public class MyPagerAdapter_shop extends FragmentPagerAdapter {

    private final String[] TITLES = { "홈", "쿠폰관리", "메시지관리", "내 매장", "설정"};

    public MyPagerAdapter_shop(FragmentManager fm) {
      super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return TITLES[position];
    }

    @Override
    public int getCount() {
      return TITLES.length;
    }

    @Override
    public Fragment getItem(int position) {
      return MainShopFragment.newInstance(position);
    }

  }


  public class MyPagerAdapter_my extends FragmentPagerAdapter {

    private final String[] TITLES = { "홈", "받은 쿠폰함", "단골가게", "관심지역" };

    public MyPagerAdapter_my(FragmentManager fm) {
      super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return TITLES[position];
    }

    @Override
    public int getCount() {
      return TITLES.length;
    }

    @Override
    public Fragment getItem(int position) {
      return MainMyFragment.newInstance(position);
    }

  }



}
