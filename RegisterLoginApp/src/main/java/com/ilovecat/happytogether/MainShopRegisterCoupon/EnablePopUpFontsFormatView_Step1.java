package com.ilovecat.happytogether.MainShopRegisterCoupon;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ilovecat.happytogether.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 08. 15. happytogether
 */
public class EnablePopUpFontsFormatView_Step1 {
  public static void EnablePopUpFontsFormatView() {

    ListView listView_fonts = (ListView) MainRegisterCouponShopStep1.popUpFontsFormatView.findViewById(android.R.id.list);
    listView_fonts.setCacheColorHint(0);

    ArrayList<String> arrayOfFontsList = new ArrayList<String>();

    try {

      // custom_list.xml 을 가져와 XmlPullParser 에 넣는다.
      XmlPullParser customList = MainRegisterCouponShopStep1.appcActivity.getResources().getXml(R.xml.fontformat_list);

      // 파싱한 xml 이 END_DOCUMENT(종료 태그)가 나올때 까지 반복한다.
      while (customList.getEventType() != XmlPullParser.END_DOCUMENT) {
        // 태그의 첫번째 속성일 때,
        if (customList.getEventType() == XmlPullParser.START_TAG) {
          // 이름이 "custom" 일때, 첫번째 속성값을 ArrayList에 저장
          if (customList.getName().equals("font")) {
            arrayOfFontsList.add(customList.getAttributeValue(0));
          }
        }
        // 다음 태그로 이동
        customList.next();
      }

    } catch (XmlPullParserException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    FontFormatRowAdapter_Step1 fontAdapter = new FontFormatRowAdapter_Step1(
        MainRegisterCouponShopStep1.appcActivity,
        R.layout.fontformat_row,
        arrayOfFontsList,
        (FontFormatRowAdapter_Step1.KeyFontClickListener) MainRegisterCouponShopStep1.appcActivity
    );

    listView_fonts.setAdapter(fontAdapter);

    // Creating a pop window for emoticons keyboard
    MainRegisterCouponShopStep1.popupWindowFontFormat = new PopupWindow(
        MainRegisterCouponShopStep1.popUpFontsFormatView,
        ViewGroup.LayoutParams.MATCH_PARENT,
        (int) MainRegisterCouponShopStep1.appcActivity.getResources().getDimension(R.dimen.keyboard_height),
        false
    );

    MainRegisterCouponShopStep1.popupWindowFontFormat.setOnDismissListener(new PopupWindow.OnDismissListener() {
      @Override
      public void onDismiss() {
        MainRegisterCouponShopStep1.subMenuFontsFormatLayout.setVisibility(LinearLayout.GONE);
      }
    });
  }
}
