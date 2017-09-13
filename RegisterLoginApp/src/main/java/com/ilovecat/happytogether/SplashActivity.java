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

package com.ilovecat.happytogether;

/**
 * ${PACKAGE_NAME} 설명은 여기서 부터. *
 *
 *
 * Created by dannykwon on 2016. 06. 24. happytogether
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class SplashActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash_layout);

    // 2014.10-10
    // 앱 종료시 스플래시화면을 finish.
    // 2016.6.24
    // 핸들러 클래스 사용시 종종 볼 수 있었던 이 경고가 메모리누수를 일으킨다는 군요.
    // 해결책은 핸들러의 하위 클래스를 만들어서 약참조 시킴으로써 해결된다
    // 참고: http://blog.hansune.com/465

    SplashExitHandler handlerExitSplash = new SplashExitHandler(this);
    handlerExitSplash.sendEmptyMessageDelayed(0, 1000);
  }

  // 핸들러 객체 만들기.
  private static class SplashExitHandler extends Handler {

    private final WeakReference<SplashActivity> currActivity;

    public SplashExitHandler(SplashActivity activity) {
      currActivity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
      SplashActivity activity = currActivity.get();
      if (activity != null) {
        activity.handleMessage(msg);
      }
    }

  }

  // Handler 에서 호출하는 함수
  private void handleMessage(Message msg) {
    finish(); // Acitity 종료
  }

}

