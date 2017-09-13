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

package com.ilovecat.happytogether.RegisterEmail;

import java.util.regex.Pattern;


/**
 * 이메일 유효성 검사
 *
 * @author dannykwon
 * @version 1.0
 * @since 6.23
 */
public class CheckEmail {


  /**
   * 이메일을 조합한다.
   *
   * @param arr                이메일계정 스트링배열
   * @param emailId            이메일아이디
   * @param emailAccount       이메일계정 배열에서 선택한 이메일
   * @param customEmailAccount 직접입력한 이메일계정
   * @return String 조합된 이메일 아이디@이메일계정
   */

  public static String combineCompleteEmail(String[] arr, String emailId,
                                            String emailAccount, String customEmailAccount) {

    String fullEmail = null;
    if (emailAccount.equals(arr[arr.length - 1])) {
      fullEmail = emailId + "@" + customEmailAccount;
    } else {
      fullEmail = emailId + "@" + emailAccount;
    }

    return fullEmail;

  }

  /**
   * 넘겨받은 이메일의 유효성을 검사한다.
   *
   * @param email 검사할 이메일
   * @return boolean 정상인지 여부를 리턴한다.
   */
  public static boolean checkEmail(String email) {
    /*String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(email);
    boolean isNormal = m.matches();
    return isNormal;*/
    if (email == null) {
      return false;
    }
    boolean isNormal = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+",
        email.trim());
    return isNormal;
  }


}
