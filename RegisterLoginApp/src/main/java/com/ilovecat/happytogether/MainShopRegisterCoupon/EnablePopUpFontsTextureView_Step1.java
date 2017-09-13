package com.ilovecat.happytogether.MainShopRegisterCoupon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.StickerClass.StickerMessageTextView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.IOException;
import java.io.InputStream;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 08. 15. happytogether
 */
public class EnablePopUpFontsTextureView_Step1 extends AbsListViewBaseActivity_Step1 {

  public static void EnablePopUpFontsTextureView() {

    AbsListViewBaseActivity_Step1.listView = (GridView) MainRegisterCouponShopStep1.popUpFontsTextureView.findViewById(R.id.gridview);

    ((GridView) listView).setAdapter(new GridImageAdapter00());


    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        int sizeOfArray = MainRegisterCouponShopStep1.arrayListStickerMessageView.size();
        final StickerMessageTextView clickedView = (StickerMessageTextView) MainRegisterCouponShopStep1.arrayListStickerMessageView.get(sizeOfArray - 1);


        if (position == 0) {
          clickedView.tv_main.getPaint().setShader(null);
          clickedView.tv_main.invalidate();

        } else {

          String backgroundPath = MainRegisterCouponShopStep1.fontTextureImageUrls[position];
          Log.i("backgroundPath is ", backgroundPath);

          String[] arr = backgroundPath.split("//");
          String backgroundNewPath = arr[1];
          String[] arrNewPath = backgroundNewPath.split("/");
          int arrNewPathSize = arrNewPath.length;
          String backgroundname = arrNewPath[arrNewPathSize - 1];


          try {
            InputStream is = MainRegisterCouponShopStep1.appcActivity.getAssets().open(backgroundname);

            //BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = 1;
            Bitmap bitmapBackground = BitmapFactory.decodeStream(is);

            Shader shader = new BitmapShader(bitmapBackground,
                Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            clickedView.tv_main.getPaint().setShader(shader);
            clickedView.tv_main.setPatternPath(backgroundPath);


            clickedView.tv_main.invalidate();


          } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }

        }


      }
    });

    // Creating a pop window for emoticons keyboard
    MainRegisterCouponShopStep1.popupWindowFontTexture = new PopupWindow(
        MainRegisterCouponShopStep1.popUpFontsTextureView,
        ViewGroup.LayoutParams.MATCH_PARENT,
        (int) MainRegisterCouponShopStep1.appcActivity.getResources().getDimension(R.dimen.keyboard_height),
        false
    );

    MainRegisterCouponShopStep1.popupWindowFontTexture.setOnDismissListener(new PopupWindow.OnDismissListener() {
      @Override
      public void onDismiss() {
        MainRegisterCouponShopStep1.subMenuFontsFormatLayout.setVisibility(LinearLayout.GONE);
      }
    });
  }

  private static class GridImageAdapter00 extends BaseAdapter {
    @Override
    public int getCount() {
      return MainRegisterCouponShopStep1.fontTextureImageUrls.length;
    }

    @Override
    public Object getItem(int position) {
      return null;
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      final ViewHolder holder;
      View view = convertView;
      if (view == null) {
        view = MainRegisterCouponShopStep1.appcActivity.getLayoutInflater().inflate(R.layout.item_fonttexturegrid_image, parent, false);
        holder = new ViewHolder();
        assert view != null;
        holder.imageView = (ImageView) view.findViewById(R.id.image);
        holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
        view.setTag(holder);
      } else {
        holder = (ViewHolder) view.getTag();
      }

      BaseActivity_Step1.imageLoader.displayImage(
          MainRegisterCouponShopStep1.fontTextureImageUrls[position],
          holder.imageView,
          MainRegisterCouponShopStep1.options,
          new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
              holder.progressBar.setProgress(0);
              holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {
              holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
              holder.progressBar.setVisibility(View.GONE);
            }
          }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current,
                                         int total) {
              holder.progressBar.setProgress(Math.round(100.0f * current / total));
            }
          }
      );

      return view;
    }

    class ViewHolder {
      ImageView imageView;
      ProgressBar progressBar;
    }
  }

}
