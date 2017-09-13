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
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;

import com.ilovecat.happytogether.Databasehandler.JsonHttpUserFunctions;
import com.ilovecat.happytogether.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MainMyFragment extends Fragment implements OnScrollListener {
  private static final String ARG_POSITION = "position";


  private int position;

  public static MainMyFragment newInstance(int position) {
    MainMyFragment f = new MainMyFragment();
    Bundle b = new Bundle();
    b.putInt(ARG_POSITION, position);
    f.setArguments(b);
    return f;
  }


  //private static final String rssFeed = "http://mytown.noip.me/getshop.xml";
  //private static final String rssFeed = "http://mytown.noip.me/paging.php?page=";


  private Context thiscontext;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    position = getArguments().getInt(ARG_POSITION);
  }


  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {


    thiscontext = container.getContext();

    // Thread로 웹서버에 접속
    View view = null;


    if (position == 0) {

      view = inflater.inflate(R.layout.main_inner_my_home_layout, container, false);


      TextView tvUserId = (TextView) view.findViewById(R.id.tvUserId);
      tvUserId.setText(MainActivity.userIdFromInnerDB + "님");


      // android.os.NetworkOnMainThreadException 에러 방지
      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
      StrictMode.setThreadPolicy(policy);

      JsonHttpUserFunctions jsonHttpUserFunctions = new JsonHttpUserFunctions();
      JSONObject json = jsonHttpUserFunctions.getMyLocationDetailFromUserDB(MainActivity.userIdFromInnerDB);


      try {
        JSONObject myLocationDetail_JsonObj = json.getJSONObject("myLocationDetail");

        String si = myLocationDetail_JsonObj.getString("MyLocationSi");
        String Gun = myLocationDetail_JsonObj.getString("MyLocationGun");
        String myLocation = myLocationDetail_JsonObj.getString("MyLocation");

        TextView tvFullMyLocation = (TextView) view.findViewById(R.id.tvFullMyLocation);
        tvFullMyLocation.setText(si + " " + Gun + " " + myLocation);

      } catch (JSONException e) {
        e.printStackTrace();
      }


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