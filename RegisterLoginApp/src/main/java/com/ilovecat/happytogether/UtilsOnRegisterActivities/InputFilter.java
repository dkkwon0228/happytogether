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

/**
 * 설명은 여기서 부터
 *
 * Created by dannykwon on 2016 . 6. 20. happytogether
 */

package com.ilovecat.happytogether.UtilsOnRegisterActivities;

import android.text.Spanned;

import java.util.regex.Pattern;

public class InputFilter {

  /**
   * 영문 알파벳과 숫자만 허용.
   */
  public static android.text.InputFilter filterAlphaNum = new android.text.InputFilter() {
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {
      Pattern ps = Pattern.compile("^[a-zA-Z0-9]*$");
      if (!ps.matcher(source).matches()) {
        return "";
      }
      return null;
    }
  };

  /**
   * 알파벳 영문과 숫자, 닷(.)만 허용.
   */
  public static android.text.InputFilter filterAlphaNumDot = new android.text.InputFilter() {
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {
      Pattern ps = Pattern.compile("^[a-zA-Z0-9.]*$");
      if (!ps.matcher(source).matches()) {
        return "";
      }
      return null;
    }
  };


  /**
   * 알파벳 영문과 숫자, 닷(.)만 허용.
   */
  public static android.text.InputFilter filterNum = new android.text.InputFilter() {
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {
      Pattern ps = Pattern.compile("^[0-9.]*$");
      if (!ps.matcher(source).matches()) {
        return "";
      }
      return null;
    }
  };
}
