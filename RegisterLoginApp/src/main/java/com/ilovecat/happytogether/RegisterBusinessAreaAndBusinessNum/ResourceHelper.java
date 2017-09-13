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
import android.content.res.TypedArray;

import com.ilovecat.happytogether.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 06. 26. happytogether
 */
public class ResourceHelper {

  public static List<TypedArray> getMultiTypedArray(Context context, String key) {
    List<TypedArray> array = new ArrayList<>();

    try {
      Class<R.array> res = R.array.class;
      Field field;
      int counter = 0;

      do {
        field = res.getField(key + "_" + counter);
        array.add(context.getResources().obtainTypedArray(field.getInt(null)));
        counter++;
      } while (field != null);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      return array;
    }
  }
}
