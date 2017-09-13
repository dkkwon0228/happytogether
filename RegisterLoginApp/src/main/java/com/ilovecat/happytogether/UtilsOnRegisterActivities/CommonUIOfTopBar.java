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

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ilovecat.happytogether.R;

/**
 * 레지스트 상단바
 */
public class CommonUIOfTopBar extends AppCompatActivity {
  /**
   * @param activity 넘겨받은 액티비티
   */
  public static void setCommonUI(final AppCompatActivity activity) {

    Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
    toolbar.setTitle("");
    activity.setSupportActionBar(toolbar);
    // toolbar의 기본 앱제목이 안보이게 한다.
    //배경색상과 제목은 xml문서에서 정의
    activity.getSupportActionBar().setTitle(null);

  }
}
