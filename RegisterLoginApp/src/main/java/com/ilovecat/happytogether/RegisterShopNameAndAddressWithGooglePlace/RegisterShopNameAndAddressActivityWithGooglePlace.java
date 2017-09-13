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

package com.ilovecat.happytogether.RegisterShopNameAndAddressWithGooglePlace;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonConstants;
import com.ilovecat.happytogether.RegisterBusinessAreaAndBusinessNum.RegisterBusinessAreaAndBusinessNumActivity;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.CommonUIOfTopBar;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.GoBackAction;


public class RegisterShopNameAndAddressActivityWithGooglePlace extends AppCompatActivity
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

  // Google Place Picker
  int status;
  private GoogleApiClient mGoogleApiClient;
  private static final int PLACE_PICKER_REQUEST = 199;
  private PlacePicker.IntentBuilder builder;

  private String strShopLatLng;
  private LatLng shopLatLng;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    setContentView(R.layout.register_shopname_google_place_layout);

    /*
    mGoogleApiClient = new GoogleApiClient
        .Builder(this)
        .addApi(Places.GEO_DATA_API)
        .addApi(Places.PLACE_DETECTION_API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();
    */

    final Context context = this;
    final AppCompatActivity appcActivity = this;

    // 상단바 공통 UI
    CommonUIOfTopBar.setCommonUI(this);
    //뒤로가기와 에니메이션
    GoBackAction.goBackAndDoAnimation(this);


    Intent intent = getIntent();
    final String strAdminId = intent.getExtras().getString("ADMINID");
    final String strAdminPassword = intent.getExtras().getString("ADMINPASSWORD");
    final String strAdminEmail = intent.getExtras().getString("ADMINEMAIL");
    final String strMyLocation = intent.getExtras().getString("MYLOCATION");
    final String strMyLocationSiGun = intent.getExtras().getString("MYLOCATIONSIGUN");


    // GPS 트랙커
    //Intent intentGPS = new Intent(this, GPSTrackerActivity.class);
    //startActivityForResult(intentGPS,1);

    /*
    chkGpsService();

    LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    if(location==null){
      location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

      double longitude = location.getLongitude();
      double latitude = location.getLatitude();

      Log.i(CommonConstants.GOOGLE_API_LOGTAG,Double.toString(latitude));
      Log.i(CommonConstants.GOOGLE_API_LOGTAG,Double.toString(longitude));

    }



    PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
        .getCurrentPlace(mGoogleApiClient, null);
    result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
      @Override
      public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
          Log.i("PLACE API", String.format("Place '%s' has likelihood: %g",
              placeLikelihood.getPlace().getName(),
              placeLikelihood.getLikelihood()));
        }
        likelyPlaces.release();
      }
    });


*/




    final EditText inputShopname;
    inputShopname = (EditText) findViewById(R.id.etRegisterShopname);


    inputShopname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_NEXT) {

          // http://www.iteye.com/topic/1010285 참고
          //createBusinessAreaExpandableListViewDialog();

          return true;
        } else {
          return false;
        }
      }
    });


    final EditText inputShopAddress;
    inputShopAddress = (EditText) findViewById(R.id.etAddressGooglePlace);

    final EditText inputShopDetailAddress;
    inputShopDetailAddress = (EditText) findViewById(R.id.etDetailAddressGooglePlace);

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


        //String placeDetailsStr = place.getName() + "\n"
        //    + place.getId() + "\n"
        //    + place.getLatLng().toString() + "\n"
        //    + place.getAddress() + "\n"
        //    + place.getAttributions() + "\n"
        //    + place.getPlaceTypes() + "\n"
        //    + place.getLocale() + "\n"
        //    + place.getPriceLevel() + "\n"
        //    + place.getWebsiteUri() + "\n"
        //    + place.getRating();

        //placeDetails.setText(placeDetailsStr);


        strShopLatLng = place.getLatLng().toString();
        shopLatLng = place.getLatLng();

        inputShopname.setText(place.getName());
        inputShopAddress.setText(place.getAddress());

      }

      @Override
      public void onError(Status status) {
        // TODO: Handle the error.
        Log.i(CommonConstants.GCM_LOGTAG, "An error occurred: " + status);
      }
    });
    */


    Button mRegNextButton = (Button) findViewById(R.id.reg_next_button);
    mRegNextButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {


        inputShopname.setError(null);
        inputShopAddress.setError(null);

        String shopname = inputShopname.getText().toString();
        String shopaddress = inputShopAddress.getText().toString();
        String shopdetailaddress = inputShopDetailAddress.getText().toString();


        // Check for a valid id.
        if (TextUtils.isEmpty(shopname)) {
          inputShopname.setError(getString(R.string.error_field_required));
        } else if (shopname.length() > 20) {
          inputShopname.setError(getString(R.string.error_long_length));
        } else if (TextUtils.isEmpty(shopaddress)) {
          inputShopAddress.setError(getString(R.string.error_field_required));
        } else {

          Intent intent = getIntent();
          String mAdminId = intent.getExtras().getString("ADMINID");
          String mAdminPassword = intent.getExtras().getString("ADMINPASSWORD");
          String mAdminEmail = intent.getExtras().getString("ADMINEMAIL");


          // Shop LATLNG to String
          Double shopLatD = shopLatLng.latitude;
          Double shopLngD = shopLatLng.longitude;
          String strShopLat = shopLatD.toString();
          String strShopLng = shopLngD.toString();
          //shopLat = Double.parseDouble(coordl1);
          //shopLng = Double.parseDouble(coordl2);

          // Launch Dashboard Screen
          Intent dashboard = new Intent(getApplicationContext(), RegisterBusinessAreaAndBusinessNumActivity.class);
          dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


          dashboard.putExtra("ADMINID", mAdminId);
          dashboard.putExtra("ADMINPASSWORD", mAdminPassword);
          dashboard.putExtra("ADMINEMAIL", mAdminEmail);

          dashboard.putExtra("SHOPNAME", shopname);
          dashboard.putExtra("SHOP_ADDRESS", shopaddress);
          dashboard.putExtra("SHOP_DETAIL_ADDRESS", shopdetailaddress);
          dashboard.putExtra("STR_SHOP_LATLNG", strShopLatLng);
          dashboard.putExtra("SHOP_LAT", strShopLat);
          dashboard.putExtra("SHOP_LNG", strShopLng);

          startActivity(dashboard);
          overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


        }

      }
    });

  }

  //GPS 트랙커
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == 1){
      Bundle extras = data.getExtras();
      Double longitude = extras.getDouble("Longitude");
      Double latitude = extras.getDouble("Latitude");
      Log.i(CommonConstants.GOOGLE_API_LOGTAG,Double.toString(latitude));
      Log.i(CommonConstants.GOOGLE_API_LOGTAG,Double.toString(longitude));


    }
  }


  //GPS 설정 체크
  private boolean chkGpsService() {

    String gps = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

    if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {

      // GPS OFF 일때 Dialog 표시
      AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
      gsDialog.setTitle("위치 서비스 설정");
      gsDialog.setMessage("무선 네트워크 사용, GPS 위성 사용을 모두 체크하셔야 정확한 위치 서비스가 가능합니다.\n위치 서비스 기능을 설정하시겠습니까?");
      gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          // GPS설정 화면으로 이동
          Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
          intent.addCategory(Intent.CATEGORY_DEFAULT);
          startActivity(intent);
        }
      })
          .setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {







              return;
            }
          }).create().show();
      return false;

    } else {








      return true;
    }
  }





  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

}


