package com.ilovecat.happytogether.MainShopRegisterCoupon;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 08. 01. happytogether
 */
import android.os.Bundle;
import android.widget.AbsListView;

import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

//import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 *
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class AbsListViewBaseActivity_Step1 extends BaseActivity_Step1 {

  protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
  protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";

  public static AbsListView listView;

  protected boolean pauseOnScroll = false;
  protected boolean pauseOnFling = true;

  @Override
  public void onRestoreInstanceState(Bundle savedInstanceState) {
    pauseOnScroll = savedInstanceState.getBoolean(STATE_PAUSE_ON_SCROLL, false);
    pauseOnFling = savedInstanceState.getBoolean(STATE_PAUSE_ON_FLING, true);
  }

  @Override
  public void onResume() {
    super.onResume();
    applyScrollListener();
  }

  private void applyScrollListener() {
    listView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
    outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
  }

}
