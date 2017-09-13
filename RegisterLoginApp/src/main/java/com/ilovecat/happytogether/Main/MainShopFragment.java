package com.ilovecat.happytogether.Main;

/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.TextView;

import com.ilovecat.happytogether.Databasehandler.JsonHttpUserFunctions;
import com.ilovecat.happytogether.MainShopListingCoupon.CouponShopListingActivity;
import com.ilovecat.happytogether.MainShopReceivedCoupon.CreateCouponInfoDialog;
import com.ilovecat.happytogether.MainShopRegisterCoupon.MainRegisterCouponShopStep1;
import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonUtils;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CreateErrorDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class MainShopFragment extends Fragment implements OnScrollListener {
  private static final String ARG_POSITION = "position";


  private int position;

  public static MainShopFragment newInstance(int position) {
    MainShopFragment f = new MainShopFragment();
    Bundle b = new Bundle();
    b.putInt(ARG_POSITION, position);
    f.setArguments(b);
    return f;
  }


  //private static final String rssFeed = "http://mytown.noip.me/getshop.xml";
  private static final String rssFeed = "http://mytown.ddns.net/paging.php?page=";


  private Context thiscontext;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    position = getArguments().getInt(ARG_POSITION);
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


    thiscontext = container.getContext();

    View view = null;

    if (position == 0) {
      view = inflater.inflate(R.layout.main_inner_shop_home_layout, container, false);

      TextView tvShopName = (TextView) view.findViewById(R.id.tvshopName);
      tvShopName.setText(MainActivity.shopNameFromInnerDB);

      // android.os.NetworkOnMainThreadException 에러 방지
      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
      StrictMode.setThreadPolicy(policy);

      JsonHttpUserFunctions jsonHttpUserFunctions = new JsonHttpUserFunctions();
      JSONObject json = jsonHttpUserFunctions.getShopLocationDetailFromUserDB(MainActivity.userIdFromInnerDB);

      try {
        JSONObject shopLocationDetail_JsonObj = json.getJSONObject("shopLocationDetail");

        String si = shopLocationDetail_JsonObj.getString("ShopLocationSi");
        String Gun = shopLocationDetail_JsonObj.getString("ShopLocationGun");
        String shopLocation = shopLocationDetail_JsonObj.getString("ShopLocation");

        TextView tvFullShopLocation = (TextView) view.findViewById(R.id.tvFullShopAddress);
        tvFullShopLocation.setText(si + " " + Gun + " " + shopLocation);

      } catch (JSONException e) {
        e.printStackTrace();
      }


    }
    if (position == 1) {
      view = inflater.inflate(R.layout.main_inner_shop_coupon_layout, container, false);

      // 쿠폰을 받은 사람 (EX. 54명이 쿠폰을 받았습니다)
      TextView btnTodayCouponCountShop = (TextView) view.findViewById(R.id.tvTodayCouponCountShop);

      btnTodayCouponCountShop.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          if (v.isClickable()) {

            if (CommonUtils.isNetworkAvailable(MainActivity.appcActivity)) {

              CreateCouponInfoDialog.createCouponInfoDialog(MainActivity.appcActivity);

            } else {

              CreateErrorDialog.createErrorDialog(MainActivity.appcActivity, "ssd", "sds");

            }


          }
        }

      });



      TextView btnTotalCouponCountShop = (TextView) view.findViewById(R.id.tvTotalCouponCountShop);

      btnTotalCouponCountShop.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {

          if (v.isClickable()) {

            if (CommonUtils.isNetworkAvailable(MainActivity.appcActivity)) {

              Intent intent = new Intent(MainActivity.appcActivity, CouponShopListingActivity.class);
              // Close all views before launching
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              intent.putExtra("CURRENTITEM", "0");
              startActivity(intent);
              // 액티비비티 에니메이션
              //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            } else {

              CreateErrorDialog.createErrorDialog(MainActivity.appcActivity, "ssd", "sds");

            }


          }
        }

      });



      Button btnRegCoupon = (Button) view.findViewById(R.id.btnRegCouponShop);
      btnRegCoupon.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          if (v.isClickable()) {

            if (CommonUtils.isNetworkAvailable(MainActivity.appcActivity)) {

              Intent intent = new Intent(MainActivity.appcActivity, MainRegisterCouponShopStep1.class);
              // Close all views before launching
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              intent.putExtra("keyWhere", "COUPONMAIN");
              //intent.putExtra("ADMINPASSWORD", pw);
              startActivity(intent);
              // 액티비비티 에니메이션
              //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            } else {

              CreateErrorDialog.createErrorDialog(MainActivity.appcActivity, "ssd", "sds");

            }

          }
        }

      });

      Button btnRepoCoupon = (Button) view.findViewById(R.id.btnRepoCoupon);
      btnRepoCoupon.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          if (v.isClickable()) {

            if (CommonUtils.isNetworkAvailable(MainActivity.appcActivity)) {

              Intent intent = new Intent(MainActivity.appcActivity, CouponShopListingActivity.class);
              // Close all views before launching
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              intent.putExtra("CURRENTITEM", "0");
              startActivity(intent);
              // 액티비비티 에니메이션
              //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            } else {

              CreateErrorDialog.createErrorDialog(MainActivity.appcActivity, "ssd", "sds");

            }


          }
        }

      });

    }
    if (position == 2) {
      view = inflater.inflate(R.layout.main_inner_shop_message_layout, container, false);
    }
    if (position == 3) {
      view = inflater.inflate(R.layout.main_inner_shop_myshop_layout, container, false);
    }
    if (position == 4) {
      view = inflater.inflate(R.layout.main_inner_shop_setting_layout, container, false);
    }


    return view;
  }


  @Override
  public void onResume() {
    super.onResume();
  }


  @Override
  public void onScroll(AbsListView view, int firstVisibleItem,
                       int visibleItemCount, int totalItemCount) {
  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {

  }


}