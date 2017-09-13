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

package com.ilovecat.happytogether.UtilsOnRegisterActivities;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilovecat.happytogether.R;

/**
 * 상단메뉴바 왼쪽의 뒤로가기 버튼과 텍스트 객체 생성 및 뒤로이동,애니메이션 구현.
 */
public class GoBackAction extends Activity {
  /**
   * 넘겨받은 액티비티의 뒤로가기 아이콘과 텍스트를 할당.
   * 액티비티 종료와 애니메이션 수행.
   * @param activity 넘겨받은 액티비티
   */
  public static void goBackAndDoAnimation(final Activity activity) {

    final ImageView mBtnBackIcon = (ImageView) activity.findViewById(R.id.toolbar_backIcon);
    mBtnBackIcon.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
      }
    });

    final TextView mBtnBackTxt = (TextView) activity.findViewById(R.id.toolbar_backString);
    mBtnBackTxt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
      }
    });

  }
}
