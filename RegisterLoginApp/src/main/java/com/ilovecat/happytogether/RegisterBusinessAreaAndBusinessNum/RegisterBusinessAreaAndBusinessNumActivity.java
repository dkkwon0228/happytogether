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

package com.ilovecat.happytogether.RegisterBusinessAreaAndBusinessNum;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ilovecat.happytogether.Main.MainActivity;
import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonUtils;
import com.ilovecat.happytogether.CommonConstantsAndUtils.CreateErrorDialog;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.CommonUIOfTopBar;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.GoBackAction;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.InputFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RegisterBusinessAreaAndBusinessNumActivity extends AppCompatActivity {

  private List<String> listDataHeader;
  private HashMap<String, List<String>> listDataChild;
  private Dialog dialogMarketList;

  private ExpandableListView listviewBusinessArea;
  private EditText inputBusinessArea;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.register_businessarea_layout);

    final Context context = this;

    final AppCompatActivity appcActivity = this;

    // 상단바 공통 UI
    CommonUIOfTopBar.setCommonUI(this);
    //뒤로가기와 에니메이션
    GoBackAction.goBackAndDoAnimation(this);

    final EditText inputBusinessLicenseNum;
    inputBusinessLicenseNum = (EditText) findViewById(R.id.etRegisterBusinessLicenseNum);
    inputBusinessLicenseNum.setFilters(new android.text.InputFilter[]{InputFilter.filterNum});
    inputBusinessLicenseNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_NEXT) {

          // http://www.iteye.com/topic/1010285 참고

          if(CommonUtils.isBizNum(inputBusinessLicenseNum.getText().toString())) {
            createBusinessAreaExpandableListViewDialog();

          } else {

            String a = "asas";
            String b = "asas";

            CreateErrorDialog.createErrorDialog(appcActivity,a,b);

          }




          return true;
        } else {
          return false;
        }
      }
    });



    inputBusinessArea = (EditText) findViewById(R.id.etRegisterBusinessArea);
    /*
     * xml 레이아웃에 추가해야 함.
     * android:focusable="false"
     */
    inputBusinessArea.setInputType(0);

    // 클릭 리스너
    inputBusinessArea.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View arg0) {

        // http://www.iteye.com/topic/1010285 참고
        createBusinessAreaExpandableListViewDialog();
      }

    });


    Button mRegNextButton = (Button) findViewById(R.id.reg_next_button);
    mRegNextButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {


        inputBusinessLicenseNum.setError(null);
        inputBusinessArea.setError(null);
        String businessNum = inputBusinessLicenseNum.getText().toString();
        String bizarea = inputBusinessArea.getText().toString();

        if(CommonUtils.isBizNum(businessNum)) {

          // Check for a valid id.
          if (TextUtils.isEmpty(businessNum)) {
            inputBusinessLicenseNum.setError(getString(R.string.error_field_required));
          } else if (businessNum.length() > 11) {
            inputBusinessLicenseNum.setError(getString(R.string.error_long_length));
          } else if (TextUtils.isEmpty(bizarea)) {
            inputBusinessArea.setError(getString(R.string.error_field_business_area_required));
          } else {


          // split을 이용한 문자열 분리
          String[] arrBizarasaea = bizarea.split("-") ;
          String mBusinessArea1 = arrBizarasaea[0].trim();
          String mBusinessArea2 = arrBizarasaea[1].trim();


          Intent intent = getIntent();
          String mAdminId = intent.getExtras().getString("ADMINID");
          String mAdminPassword = intent.getExtras().getString("ADMINPASSWORD");
          String mAdminEmail = intent.getExtras().getString("ADMINEMAIL");
          String mShopName = intent.getExtras().getString("SHOPNAME");
          String mShopAddress = intent.getExtras().getString("SHOP_ADDRESS");
          String mShopDetailAddress = intent.getExtras().getString("SHOP_DETAIL_ADDRESS");
          String mShopLatLng = intent.getExtras().getString("STR_SHOP_LATLNG");
          String mShopLat = intent.getExtras().getString("SHOP_LAT");
          String mShopLng = intent.getExtras().getString("SHOP_LNG");


          // Launch Dashboard Screen
          Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
          dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


          dashboard.putExtra("ADMINID", mAdminId);
          dashboard.putExtra("ADMINPASSWORD", mAdminPassword);
          dashboard.putExtra("ADMINEMAIL", mAdminEmail);
          dashboard.putExtra("SHOPNAME", mShopName);
          dashboard.putExtra("SHOP_ADDRESS", mShopAddress);
          dashboard.putExtra("SHOP_DETAIL_ADDRESS", mShopDetailAddress);
          dashboard.putExtra("STR_SHOP_LATLNG", mShopLatLng);
          dashboard.putExtra("SHOP_LAT", mShopLat);
          dashboard.putExtra("SHOP_LNG", mShopLng);
          dashboard.putExtra("SHOP_BUSINESS_NUM", businessNum);

          dashboard.putExtra("SHOP_BUSINESS_AREA1", mBusinessArea1);
          dashboard.putExtra("SHOP_BUSINESS_AREA2", mBusinessArea2);


          startActivity(dashboard);
          overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


          }


        } else {
          CreateErrorDialog.createErrorDialog(appcActivity,"sds","sdsd");
        }


      }
    });

  }

  private void createBusinessAreaExpandableListViewDialog() {
    View viewList = this.getLayoutInflater().inflate(R.layout.register_businessarea_popupdialog, null);
    dialogMarketList = new Dialog(this);
    dialogMarketList.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
    dialogMarketList.setContentView(viewList);

    listviewBusinessArea = (ExpandableListView) dialogMarketList.findViewById(R.id.lvExp);

    // preparing list data
    prepareListData();

    BusinessAreaListAdapter listAdapter = new BusinessAreaListAdapter(this,
        listDataHeader, listDataChild);
    listviewBusinessArea.setAdapter(listAdapter);

    // Listview Group click listener
    listviewBusinessArea.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

      @Override
      public boolean onGroupClick(ExpandableListView parent, View v,
                                  int groupPosition, long id) {
        Toast.makeText(getApplicationContext(),
            "Group Clicked " + listDataHeader.get(groupPosition),
            Toast.LENGTH_SHORT).show();
        return false;
      }
    });

    // Listview Group expanded listener
    listviewBusinessArea.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

      @Override
      public void onGroupExpand(int groupPosition) {
        Toast.makeText(getApplicationContext(),
            listDataHeader.get(groupPosition) + " Expanded",
            Toast.LENGTH_SHORT).show();
      }
    });

    // Listview Group collasped listener
    listviewBusinessArea.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

      @Override
      public void onGroupCollapse(int groupPosition) {
        Toast.makeText(getApplicationContext(),
            listDataHeader.get(groupPosition) + " Collapsed",
            Toast.LENGTH_SHORT).show();

      }
    });

    // Listview on child click listener
    listviewBusinessArea.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

      @Override
      public boolean onChildClick(ExpandableListView parent, View v,
                                  int groupPosition, int childPosition, long id) {
        // TODO Auto-generated method stub
        Toast.makeText(
            getApplicationContext(),
            listDataHeader.get(groupPosition)
                + " : "
                + listDataChild.get(
                listDataHeader.get(groupPosition)).get(
                childPosition), Toast.LENGTH_SHORT)
            .show();


        String strBizArea_1 = listDataHeader.get(groupPosition).toString();
        String strBizArea_2 = listDataChild.get(
            listDataHeader.get(groupPosition)).get(
            childPosition).toString();
        String strBizArea = strBizArea_1 + "-" + strBizArea_2;

        inputBusinessArea.setText(strBizArea);
        dialogMarketList.cancel();

        return false;
      }
    });


    Button dialogButton = (Button) dialogMarketList.findViewById(R.id.btn_ok);
    // if button is clicked, close the register_email_popupdialog_layout.xmlayout.xml dialog
    dialogButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dialogMarketList.cancel();
      }
    });

    dialogMarketList.show();
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

  /*
   * Preparing the list data
   */
  private void prepareListData() {
    listDataHeader = new ArrayList<String>();
    listDataChild = new HashMap<String, List<String>>();

    Resources res = getResources();
    TypedArray typedArray = res.obtainTypedArray(R.array.array_business_area0);
    int alength = typedArray.length();
    String[][] array = new String[alength][];

    for (int i = 0; i < alength; ++i) {
      int id = typedArray.getResourceId(i, 0);
      if (id > 0) {
        array[i] = res.getStringArray(id);
      } else {
        System.out.println("something wrong with the XML");
        // something wrong with the XML
      }
    }
    typedArray.recycle(); // Important!

    for (int i = 0; i < alength; i++) {
      listDataHeader.add(array[i][0]);

      List<String> arrChild = new ArrayList<String>();

      // j = 1 인 이유는 자식 첫번째 아이템이 그룹명이기 때문이다.
      for(int j = 1; j < array[i].length; j++) {
        arrChild.add(array[i][j]);
      }
      listDataChild.put(listDataHeader.get(i), arrChild);
    }
  }



}


