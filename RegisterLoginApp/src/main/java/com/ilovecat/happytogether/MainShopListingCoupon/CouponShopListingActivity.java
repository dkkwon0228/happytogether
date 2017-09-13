package com.ilovecat.happytogether.MainShopListingCoupon;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 16. happytogether
 */
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.CommonUIOfTopBar;
import com.ilovecat.happytogether.UtilsOnRegisterActivities.GoBackAction;

public class CouponShopListingActivity extends AppCompatActivity {

  private ImageView ivBackArrow;
  private TextView tvBack;
  private TextView tvTitle;


  public static AppCompatActivity appcActivity;

  @SuppressLint("SetJavaScriptEnabled")
  private PagerSlidingTabStrip tabs_openOrprivate;
  public static ViewPager pager_openOrprivate;
  private CouponShopAdapter_openOrprivate adapter_openOrprivate;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_inner_shop_coupon_listing);

    appcActivity = this;


    Intent getedIntent = getIntent();
    String strCurrentItem = getedIntent.getExtras().getString("CURRENTITEM");


    // 상단바 공통 UI
    CommonUIOfTopBar.setCommonUI(this);

    // 상단좌측 뒤로가기
    GoBackAction.goBackAndDoAnimation(this);

    initCollapsingToolbar();


    try {
      Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
    } catch (Exception e) {
      e.printStackTrace();
    }



    tabs_openOrprivate = (PagerSlidingTabStrip) findViewById(R.id.tabs_openOrprivate);
    pager_openOrprivate = (ViewPager) findViewById(R.id.pager_openOrprivate);
    adapter_openOrprivate = new CouponShopAdapter_openOrprivate(getSupportFragmentManager());

    pager_openOrprivate.setAdapter(adapter_openOrprivate);

    final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
        getResources().getDisplayMetrics());
    pager_openOrprivate.setPageMargin(pageMargin);
    pager_openOrprivate.setCurrentItem(Integer.parseInt(strCurrentItem));

    tabs_openOrprivate.setViewPager(pager_openOrprivate);

    // Attach the page change listener to tab strip and **not** the view
    // pager_shop inside the activity
    tabs_openOrprivate.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

      // This method will be invoked when a new page becomes selected.
      @Override
      public void onPageSelected(int position) {
        //Toast.makeText(CouponShopListingActivity.this, "Selected page position: " + position, Toast.LENGTH_SHORT).show();
      }

      // This method will be invoked when the current page is scrolled
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Code goes here
      }

      // Called when the scroll state changes:
      // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
      @Override
      public void onPageScrollStateChanged(int state) {
        // Code goes here
      }
    });
  }

  /**
   * Initializing collapsing toolbar
   * Will show and hide the toolbar title on scroll
   */

  private void initCollapsingToolbar() {
    final CollapsingToolbarLayout collapsingToolbar =
        (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

    collapsingToolbar.setTitle("");

    ivBackArrow = (ImageView) findViewById(R.id.toolbar_backIcon);
    ivBackArrow.setVisibility(View.INVISIBLE);


    tvBack = (TextView) findViewById(R.id.toolbar_backString);
    tvBack.setVisibility(View.INVISIBLE);

    tvTitle = (TextView) findViewById(R.id.toolbar_titleString);
    tvTitle.setVisibility(View.INVISIBLE);



    AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
    appBarLayout.setExpanded(true);

    // hiding & showing the title when toolbar expanded & collapsed
    appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      boolean isShow = false;
      int scrollRange = -1;

      @Override
      public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (scrollRange == -1) {
          scrollRange = appBarLayout.getTotalScrollRange();
        }

        if (scrollRange + verticalOffset == 0) {
          //collapsingToolbar.setTitle(getString(R.string.app_name));
          ivBackArrow.setVisibility(View.VISIBLE);
          tvBack.setVisibility(View.VISIBLE);
          tvTitle.setVisibility(View.VISIBLE);


          isShow = true;
        } else if (isShow) {
          //collapsingToolbar.setTitle(" ");
          ivBackArrow.setVisibility(View.INVISIBLE);
          tvBack.setVisibility(View.INVISIBLE);
          tvTitle.setVisibility(View.INVISIBLE);


          isShow = false;
        }

      }
    });
  }



  /**
   * <pre>
   * 1. 패키지명 : com.appmaker.gcmtest
   * 2. 타입명 :  MyPagerAdapter_shop CouponShopListingActivity.java
   * 3. 작성일 : 2016. 5. 8. 오후 8:36:05
   * 4. 작성자 : dannykwon
   * 5. 설명 :
   * </pre>
   */
  public class CouponShopAdapter_openOrprivate extends FragmentPagerAdapter {

    private final String[] TITLES = { "공개된 쿠폰", "비공개 쿠폰", "임시보관함 쿠폰"};

    public CouponShopAdapter_openOrprivate(FragmentManager fm) {
      super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return TITLES[position];
    }

    @Override
    public int getCount() {
      return TITLES.length;
    }

    @Override
    public Fragment getItem(int position) {
      return MainCouponShopFragment.newInstance(position);
    }

  }



}