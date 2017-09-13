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

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 06. 25. happytogether
 */
public class CheckableRelativeLayout extends RelativeLayout implements Checkable {
  final String NS = "http://schemas.android.com/apk/res/com.ilovecat.happytogether";
  final String ATTR = "checkable";

  int checkableId;
  Checkable checkable;

  public CheckableRelativeLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    checkableId = attrs.getAttributeResourceValue(NS, ATTR, 0);
  }

  @Override
  public boolean isChecked() {
    checkable = (Checkable) findViewById(checkableId);
    if(checkable == null)
      return false;
    return checkable.isChecked();
  }

  @Override
  public void  setChecked(boolean checked) {
    checkable = (Checkable) findViewById(checkableId);
    if(checkable == null)
      return;
    checkable.setChecked(checked);
  }

  @Override
  public void toggle() {
    checkable = (Checkable) findViewById(checkableId);
    if(checkable == null)
      return;
    checkable.toggle();
  }
}
