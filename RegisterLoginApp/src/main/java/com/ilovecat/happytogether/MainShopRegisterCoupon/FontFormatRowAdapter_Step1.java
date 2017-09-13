package com.ilovecat.happytogether.MainShopRegisterCoupon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ilovecat.happytogether.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 08. 02. happytogether
 */
//public class FontRowAdapter extends ArrayAdapter<ShopItem> {
public class FontFormatRowAdapter_Step1 extends BaseAdapter {

  private Context fontactivity;
  //private Activity shopactivity;

  private ArrayList<String> fontitems;
  //private FontItem objBean;
  private int fontrow;
  private DisplayImageOptions options;
  static ImageLoader imageLoader;

  KeyFontClickListener mListener;

  String[] FONT_PATH = new String[] {
      "NBGothic.ttf",
      "lotte_happy_bold.ttf",
      "yalolja_OTF_Regular.otf",
      "BMDOHYEON_otf.otf",
      "BMHANNA_11yrs_otf.otf",
      "BMJUA_otf.otf",
      "Typo_DecoSolidSlash.ttf"
  };


  static HashMap<String, Typeface> typefaceMap0 = new HashMap<String, Typeface>();

  static ViewHolder holder;
  static View convertView;



  public static Typeface getTypeface0(Context context, String path) {
    if ("DEFAULT".equals(path)) { return Typeface.DEFAULT; }
    if (typefaceMap0.get(path) != null) { return typefaceMap0.get(path); }
    Typeface typeface = Typeface.createFromAsset(context.getAssets(), path);
    typefaceMap0.put(path, typeface);
    return typeface;
  }

  public FontFormatRowAdapter_Step1(Context act, int resource, ArrayList<String> arrayList, KeyFontClickListener listener) {
    //public FontRowAdapter(Context act, List<ShopItem> arrayList, String adminid) {
    //super(act,shoprow,arrayList);
    this.fontactivity = act;
    this.fontrow = resource;
    this.fontitems = arrayList;
    this.mListener = listener;
    //this.ADMINID = adminid;

    options = new DisplayImageOptions.Builder()
        //.showStubImage(R.drawable.profile_org)
        //.showImageForEmptyUrl(R.drawable.profile_org)
        .showImageOnLoading(R.drawable.ic_stub)
        .showImageForEmptyUri(R.drawable.ic_empty)
        .showImageOnFail(R.drawable.ic_error)
        //.cacheInMemory(true)
        //.cacheOnDisc(true)
        .considerExifParams(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
        //.cacheInMemory()
        //.cacheOnDisc()
        .build();

    imageLoader = ImageLoader.getInstance();
    //imageLoader = initImageLoader(shopactivity);

  }


  @Override
  public int getCount() {
    return fontitems.size();
  }


  @Override
  public String getItem(int position) {
    return fontitems.get(position);
  }



  @Override
  public long getItemId(int position) {

    return position;
  }


  @Override
  public View getView(final int position, View convertViewOrg, ViewGroup parent) {


    //View view = convertView;
    //ViewHolder holder;
    //java.util.Date date = null;

    convertView = convertViewOrg;

    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) fontactivity
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(fontrow, null);
      holder = new ViewHolder();

      if ((fontitems == null) || ((position + 1) > fontitems.size())) return convertView;
      holder.fontName = (TextView) convertView.findViewById(R.id.fontName);

      holder.typeface0 =  getTypeface0(fontactivity, FONT_PATH[0]);
      holder.typeface1 = getTypeface0(fontactivity, FONT_PATH[1]);
      holder.typeface2 = getTypeface0(fontactivity, FONT_PATH[2]);
      holder.typeface3 = getTypeface0(fontactivity, FONT_PATH[3]);
      holder.typeface4 = getTypeface0(fontactivity, FONT_PATH[4]);
      holder.typeface5 = getTypeface0(fontactivity, FONT_PATH[5]);
      holder.typeface6 = getTypeface0(fontactivity, FONT_PATH[6]);

      convertView.setTag(holder);

    } else {
      holder = (ViewHolder) convertView.getTag();
    }



    holder.fontName.setText(fontitems.get(position));
    //holder.fontName.setTypeface(holder.typeface2);

    if(position == 0) {
      holder.fontName.setTypeface(holder.typeface0);
      convertView.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          mListener.keyFontClickedIndex(position, holder.typeface0);
        }
      });

    }
    if(position == 1) {
      holder.fontName.setTypeface(holder.typeface1);
      convertView.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          mListener.keyFontClickedIndex(position, holder.typeface1);
        }
      });
    }
    if(position == 2) {
      holder.fontName.setTypeface(holder.typeface2);
      convertView.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          mListener.keyFontClickedIndex(position, holder.typeface2);
        }
      });
    }

    if(position == 3) {
      holder.fontName.setTypeface(holder.typeface3);
      convertView.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          mListener.keyFontClickedIndex(position, holder.typeface3);
        }
      });
    }
    if(position == 4) {
      holder.fontName.setTypeface(holder.typeface4);
      convertView.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          mListener.keyFontClickedIndex(position, holder.typeface4);
        }
      });
    }

    if(position == 5) {
      holder.fontName.setTypeface(holder.typeface5);
      convertView.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          mListener.keyFontClickedIndex(position, holder.typeface5);
        }
      });
    }

    if(position == 6) {
      holder.fontName.setTypeface(holder.typeface6);
      convertView.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          mListener.keyFontClickedIndex(position, holder.typeface6);
        }
      });
    }


    return convertView;
  }

  public class ViewHolder {
    private TextView fontName;
    /*
    private Typeface typeface0, typeface1, typeface2, typeface3, typeface4, typeface5
    , typeface6, typeface7, typeface8, typeface9, typeface10, typeface11;
    */
    private Typeface typeface0, typeface1, typeface2, typeface3, typeface4,
        typeface5, typeface6;
  }

	/*
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	*/
	/*
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}
	 */

  public interface KeyFontClickListener {
    public void keyFontClickedIndex(int position, Typeface typeface);
  }

}
