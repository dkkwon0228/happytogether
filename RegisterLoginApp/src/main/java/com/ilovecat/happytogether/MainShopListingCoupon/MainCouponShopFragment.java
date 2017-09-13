package com.ilovecat.happytogether.MainShopListingCoupon;

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
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.ilovecat.happytogether.R;

import java.util.ArrayList;
import java.util.List;

public class MainCouponShopFragment extends Fragment implements OnScrollListener {

  private static final String ARG_POSITION = "position";
  private int position;

  String xml_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/HappyTogether/TempSave/";
  String xmlRssFeed = xml_path + "tempsave.xml";

  public static MainCouponShopFragment newInstance(int position) {
    MainCouponShopFragment f = new MainCouponShopFragment();
    Bundle b = new Bundle();
    b.putInt(ARG_POSITION, position);
    f.setArguments(b);
    return f;
  }


  public static Context thiscontext;

  // 공개된 쿠폰
  private RecyclerView recyclerView;
  private static CouponShopAdapter couponShopAdapter;
  private static List<CouponShop> couponShopList;

  public static String[] strArrDataCouponImageUrl = {""};
  public static String[] strArrDataCouponTitle = {""};
  public static String[] strArrDataCouponNotice1 = {""};


  // 비공개 쿠폰
  private RecyclerView recyclerView_private;
  private static CouponShopAdapter couponShopAdapter_private;
  private static List<CouponShop> couponShopList_private;

  public static String[] strArrDataCouponImageUrl_private = {""};
  public static String[] strArrDataCouponTitle_private = {""};
  public static String[] strArrDataCouponNotice1_private = {""};

  // 임시보관 쿠폰
  public static RecyclerView recyclerView_tempsave;
  public static CouponShopAdapter couponShopAdapter_tempsave;
  public static List<CouponShop> couponShopList_tempsave;


  public static String[] strArrDataCouponImageUrl_tempsave = {""};
  public static String[] strArrDataCouponTitle_tempsave = {""};
  public static String[] strArrDataCouponNotice1_tempsave = {""};


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

      view = inflater.inflate(R.layout.main_inner_coupon_shop_open_layout, container, false);

      recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

      couponShopList = new ArrayList<>();
      couponShopAdapter = new CouponShopAdapter(thiscontext, couponShopList);

      RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(thiscontext, 1);
      recyclerView.setLayoutManager(mLayoutManager);
      recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(4), true));
      recyclerView.setItemAnimator(new DefaultItemAnimator());
      recyclerView.setAdapter(couponShopAdapter);

      new ListingCouponShopAsyncFetch().execute();

    }
    if (position == 1) {
      view = inflater.inflate(R.layout.main_inner_coupon_shop_private_layout, container, false);

      recyclerView_private = (RecyclerView) view.findViewById(R.id.recycler_view);

      couponShopList_private = new ArrayList<>();
      couponShopAdapter_private = new CouponShopAdapter(thiscontext, couponShopList_private);

      RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(thiscontext, 1);
      recyclerView_private.setLayoutManager(mLayoutManager);
      recyclerView_private.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(4), true));
      recyclerView_private.setItemAnimator(new DefaultItemAnimator());
      recyclerView_private.setAdapter(couponShopAdapter_private);

      new ListingCouponShopAsyncFetch_private().execute();

    }



    if (position == 2) {
      view = inflater.inflate(R.layout.main_inner_coupon_shop_tempsave_layout, container, false);


      recyclerView_tempsave = (RecyclerView) view.findViewById(R.id.recycler_view);

      couponShopList_tempsave = new ArrayList<>();
      couponShopAdapter_tempsave = new CouponShopAdapter(thiscontext, couponShopList_tempsave);

      RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(thiscontext, 1);
      recyclerView_tempsave.setLayoutManager(mLayoutManager);
      recyclerView_tempsave.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(4), true));
      recyclerView_tempsave.setItemAnimator(new DefaultItemAnimator());
      recyclerView_tempsave.setAdapter(couponShopAdapter_tempsave);

      new ListingCouponShopAsyncFetch_tempsave().execute(xmlRssFeed);


    }

    return view;
  }



  @Override
  public void onResume() {

    super.onResume();
    /*
    // 리싸이클뷰 갱신
    if (position == 2) {
      couponShopList_tempsave = new ArrayList<>();
      couponShopAdapter_tempsave = new CouponShopAdapter(thiscontext, couponShopList_tempsave);
      recyclerView_tempsave.setAdapter(couponShopAdapter_tempsave);
      new ListingCouponShopAsyncFetch_tempsave().execute(xmlRssFeed);
      couponShopAdapter_tempsave.notifyDataSetChanged();
    }
    */

  }


  @Override
  public void onScroll(AbsListView view, int firstVisibleItem,
                       int visibleItemCount, int totalItemCount) {
  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {

  }

  /**
   * RecyclerView item decoration - give equal margin around grid item
   */
  public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
      this.spanCount = spanCount;
      this.spacing = spacing;
      this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
      int position = parent.getChildAdapterPosition(view); // item position
      int column = position % spanCount; // item column

      if (includeEdge) {
        outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
        outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

        if (position < spanCount) { // top edge
          outRect.top = spacing;
        }
        outRect.bottom = spacing; // item bottom
      } else {
        outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
        outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
        if (position >= spanCount) {
          outRect.top = spacing; // item top
        }
      }
    }
  }

  /**
   * Converting dp to pixel
   */
  private int dpToPx(int dp) {
    Resources r = getResources();
    return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
  }


  /**
   * Adding few albums for testing
   */
  public static void prepareAlbums() {

    CouponShop a;

    for (int i = 0; i < strArrDataCouponTitle.length; i++) {

      a = new CouponShop(strArrDataCouponTitle[i], strArrDataCouponNotice1[i], strArrDataCouponImageUrl[i]);
      couponShopList.add(a);

    }

    couponShopAdapter.notifyDataSetChanged();

  }


  /**
   * Adding few albums for testing
   */
  public static void prepareAlbums_private() {

    CouponShop a;

    for (int i = 0; i < strArrDataCouponTitle_private.length; i++) {

      a = new CouponShop(strArrDataCouponTitle_private[i], strArrDataCouponNotice1_private[i], strArrDataCouponImageUrl_private[i]);
      couponShopList_private.add(a);

    }

    couponShopAdapter_private.notifyDataSetChanged();

  }



  /**
   * Adding few albums for testing
   */
  public static void prepareAlbums_tempsave() {

    CouponShop a;

    for (int i = 0; i < strArrDataCouponTitle_tempsave.length; i++) {

      a = new CouponShop(strArrDataCouponTitle_tempsave[i], strArrDataCouponNotice1_tempsave[i], strArrDataCouponImageUrl_tempsave[i]);
      couponShopList_tempsave.add(a);

    }

    couponShopAdapter_tempsave.notifyDataSetChanged();

  }



}
