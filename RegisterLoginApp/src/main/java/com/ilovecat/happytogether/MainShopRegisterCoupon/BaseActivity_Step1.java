package com.ilovecat.happytogether.MainShopRegisterCoupon;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 08. 01. happytogether
 */
import android.support.v7.app.AppCompatActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public abstract class BaseActivity_Step1 extends AppCompatActivity {

  public static ImageLoader imageLoader = ImageLoader.getInstance();

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.item_clear_memory_cache:
				imageLoader.clearMemoryCache();
				return true;
			case R.id.item_clear_disc_cache:
				imageLoader.clearDiscCache();
				return true;
			default:
				return false;
		}
	}
	*/
}
