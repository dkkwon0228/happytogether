package com.ilovecat.happytogether.MainShopRegisterCoupon;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ilovecat.happytogether.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BackGroundMainActivity_Step1 extends AbsListViewBaseActivity_Step1 {

  String[] menuImageUrls;
  String[] background00ImageUrls;
  String[] background01ImageUrls;
  String[] background02ImageUrls;
  String[] background03ImageUrls;
  String[] background04ImageUrls;
  String[] background05ImageUrls;
  String[] background06ImageUrls;

  DisplayImageOptions options;

  //public static Context mContext;

  int menuPosition = 0;

  static ImageView imageViewForBackground = null;
  static Integer PAGERIMAGEPOSITION;
  String PAGERIMAGESPATH;
  Integer PAGERTOTALNUM;

  static Context mContext;

  Canvas canvas;

  int displayWidth;
  int displayHeight;
  //int stikerResId = 0;

  boolean isOpen = true;
  //public static int stikerNum;

  int imageViewWidth;
  int imageViewHeight;


  Animation animBounce1;

  String external_download_path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
  String temp_path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/MyTown/Temp/";
  String mytown_path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/MyTown/";
  static String photo_path_tempsave = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/MyTown/TempSave/";
  static String photo_path_cropstiker = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/MyTown/CropStiker/";
  static String photo_path_tempsaveXMLInAdapter = photo_path_tempsave + "tempsave.xml";
  static File XmlFileInAdapter = new File(photo_path_tempsaveXMLInAdapter);
  String photo_path_temp = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/MyTown/Temp/";
  String shopimages_path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/MyTown/ShopImages/";


  int STIKER_NUM_LIMIT_PER_PAGER = 10;

  private static final int PICK_FROM_CAMERA_OR_ALBUM = 0;
  private static final int PICK_FROM_ALBUM = 1;
  private static final int CROP_FROM_CAMERA_AND_ALBUM = 2;

  private static final int REQUEST_CODE_SETTINGS = 0;
  private static final String TAG = "afterClickStikerCrop";
  String CROPSTIKER_FILENAME;
  File original_file_forCrop;
  File copy_file_forCrop;

  String ISFROM;
  String CATENAME = null;
  String tempSaveId = null;
  static Document docRoot;
  String TEMPSAVE_PATH_OF_SET;

  File copy_file[];
  File copy_Thumfile[];
  File copy_SmallThumfile[];
  File original_file[];
  File original_Thumfile[];
  File originalTexImages_file[];
  File copyTextImages_file[];

  int TotalNumInPager = 0;
  int pagerIndexOfToChange = 0;
  String IS_PAGER_WHERE_FROM = null;

  String backgroundPath = null;

  String SELECTED_PATH_PHOTO_NAME = null;

  File copy_file_thum[];

  Bitmap bitmapBackground = null;

  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.backgroundmain);


    mContext = getApplicationContext();
    Resources mRes = mContext.getResources();
    Log.i("mRes In OnCreate is ", "" + mRes);

    initImageLoader(mContext);

    final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View rootView = inflater.inflate(R.layout.backgroundmain, null);

    animBounce1 = AnimationUtils.loadAnimation(this, R.anim.stickerscaleinout);

    Intent intent = getIntent();
    //ISFROM = intent.getExtras().getString("keyIsFrom");
    //tempSaveId = intent.getExtras().getString("keyTempSaveId");
    //TotalNumInPager = intent.getExtras().getInt("keyTotalPageNum");
    //pagerIndexOfToChange = intent.getExtras().getInt("keyPageNum");
    //IS_PAGER_WHERE_FROM = intent.getExtras().getString("keyWherePagerFrom");


    //Constants constants = new Constants();
    //Bundle bundle = getIntent().getExtras();
    //menuImageUrls = constants.getStringArray(STIKER_Extra.STIKERMENU_IMAGES);
    menuImageUrls = new String[]{
        "http://1.bp.blogspot.com/-y-HQwQ4Kuu0/TdD9_iKIY7I/AAAAAAAAE88/3G4xiclDZD0/s1600/Twitter_Android.png",
        "http://3.bp.blogspot.com/-nAf4IMJGpc8/TdD9OGNUHHI/AAAAAAAAE8E/VM9yU_lIgZ4/s1600/Adobe%2BReader_Android.png",
        "http://cdn.geekwire.com/wp-content/uploads/2011/05/oovoo-android.png?7794fe",
        "http://icons.iconarchive.com/icons/kocco/ndroid/128/android-market-2-icon.png",
        "http://thecustomizewindows.com/wp-content/uploads/2011/11/Nicest-Android-Live-Wallpapers.png",
        "http://c.wrzuta.pl/wm16596/a32f1a47002ab3a949afeb4f",
        "http://macprovid.vo.llnwd.net/o43/hub/media/1090/6882/01_headline_Muse.jpg",
        "assets://Living Things @#&=+-_.,!()~'%20.jpg", // Image from assets
        //"drawable://" + R.drawable.ic_launcher, // Image from drawables
        "http://upload.wikimedia.org/wikipedia/ru/b/b6/Как_кот_с_мышами_воевал.png", // Link with UTF-8
        "https://www.eff.org/sites/default/files/chrome150_0.jpg", // Image from HTTPS
        "http://bit.ly/soBiXr", // Redirect link
        "http://img001.us.expono.com/100001/100001-1bc30-2d736f_m.jpg", // EXIF
        "", // Empty link
        "http://wrong.site.com/corruptedLink" // Wrong link
    };

    background00ImageUrls = new String[]{
        "assets://coupon_templatebg01.jpg",
        "assets://coupon_templatebg02.jpg",
        "assets://coupon_templatebg03.jpg",
        "assets://coupon_templatebg04.jpg",
        "assets://coupon_templatebg05.jpg",
        "assets://coupon_templatebg06.jpg",
        "assets://coupon_templatebg07.jpg",
        "assets://coupon_templatebg08.jpg",
        "assets://coupon_templatebg09.jpg",
        "assets://coupon_templatebg10.jpg",
        "assets://coupon_templatebg11.jpg",
        "assets://coupon_templatebg12.jpg",
        "assets://coupon_templatebg13.jpg",
        "assets://coupon_templatebg14.jpg",
        "assets://coupon_templatebg15.jpg",
        "assets://coupon_absbg01.jpg",
        "assets://coupon_absbg02.jpg",
        "assets://coupon_absbg03.jpg",
        "assets://coupon_absbg04.jpg",
        "assets://coupon_absbg05.jpg",
        "assets://coupon_absbg07.jpg",
        "assets://coupon_absbg08.jpg",
        "assets://coupon_patternbg01.jpg",
        "assets://coupon_patternbg02.jpg",
        "assets://coupon_patternbg03.jpg",
        "assets://coupon_patternbg04.jpg",
        "assets://coupon_patternbg05.jpg",
        "assets://coupon_patternbg07.jpg",
        "assets://coupon_patternbg08.jpg",
        "assets://coupon_patternbg09.jpg",
        "assets://coupon_patternbg10.jpg",
        "assets://coupon_patternbg11.jpg"

    };


    options = new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.ic_stub)
        .showImageForEmptyUri(R.drawable.ic_empty)
        .showImageOnFail(R.drawable.ic_error)
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .considerExifParams(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();

    //HorizontalListView listviewMenu = (HorizontalListView) findViewById(R.id.listview);
    //listviewMenu.setAdapter(mAdapter);

    listView = (GridView) findViewById(R.id.gridview);
    ((GridView) listView).setAdapter(new GridImageAdapter00());

    listView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //startImagePagerActivity(position);

        Log.i("menuPosition is ", "" + menuPosition);
        Log.i("position is ", "" + position);

        toggle();

        RelativeLayout imageViewForBackgroundLayout = (RelativeLayout) findViewById(R.id.ImageViewForBackgroundLayout);
        imageViewForBackgroundLayout.setVisibility(View.VISIBLE);


        imageViewForBackground = (ImageView) findViewById(R.id.imageviewForBackground);

        backgroundPath = background00ImageUrls[position];
        MainRegisterCouponShopStep1.BackGroundFilePathAndName = background00ImageUrls[position];


        Log.i("backgroundPath is ", backgroundPath);

        String[] arr = backgroundPath.split("//");
        String backgroundNewPath = arr[1];
        String[] arrNewPath = backgroundNewPath.split("/");
        int arrNewPathSize = arrNewPath.length;
        String backgroundname = arrNewPath[arrNewPathSize - 1];


        try {
          InputStream is = getAssets().open(backgroundname);

          //BitmapFactory.Options options = new BitmapFactory.Options();
          //options.inSampleSize = 1;
          bitmapBackground = BitmapFactory.decodeStream(is);
          imageViewForBackground.setImageBitmap(bitmapBackground);
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }

      }
    });


    /*
     * 모두삭제 버튼
     */
    Button btStikerShow = (Button) findViewById(R.id.background_show);
    btStikerShow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        toggle();
      }
    });

    Button btStikerSave = (Button) findViewById(R.id.backgroundsave);
    btStikerSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        //SELECTED_PATH_PHOTO_NAME = backgroundPath;
        //Log.i("asasa", SELECTED_PATH_PHOTO_NAME);
        //Bitmap bitmap = loadBitmapFromAssets(SELECTED_PATH_PHOTO_NAME);
        MainRegisterCouponShopStep1.ivBgCoupon.setImageBitmap(bitmapBackground);
        //MainRegisterCouponShopStep1.ivBgCoupon.setAlpha(100);


        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            finish();
          }
        }, 500);
      }

    });

  }

  public static void initImageLoader(Context context) {
    // This configuration tuning is custom. You can tune every option, you may tune some of them,
    // or you can create default configuration by
    //  ImageLoaderConfiguration.createDefault(this);
    // method.
    ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
    config.threadPriority(Thread.NORM_PRIORITY - 2);
    config.denyCacheImageMultipleSizesInMemory();
    config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
    config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
    config.tasksProcessingOrder(QueueProcessingType.LIFO);
    config.writeDebugLogs(); // Remove for release app

    // Initialize ImageLoader with configuration.
    ImageLoader.getInstance().init(config.build());
  }


  public class GridImageAdapter00 extends BaseAdapter {
    @Override
    public int getCount() {
      return background00ImageUrls.length;
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
        view = getLayoutInflater().inflate(R.layout.item_backgroundgrid_image, parent, false);
        holder = new ViewHolder();
        assert view != null;
        holder.imageView = (ImageView) view.findViewById(R.id.image);
        holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
        view.setTag(holder);
      } else {
        holder = (ViewHolder) view.getTag();
      }

      imageLoader.displayImage(background00ImageUrls[position], holder.imageView, options, new SimpleImageLoadingListener() {
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

  public class GridImageAdapter01 extends BaseAdapter {
    @Override
    public int getCount() {
      return background01ImageUrls.length;
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
        view = getLayoutInflater().inflate(R.layout.item_backgroundgrid_image, parent, false);
        holder = new ViewHolder();
        assert view != null;
        holder.imageView = (ImageView) view.findViewById(R.id.image);
        holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
        view.setTag(holder);
      } else {
        holder = (ViewHolder) view.getTag();
      }

      imageLoader.displayImage(background01ImageUrls[position], holder.imageView, options, new SimpleImageLoadingListener() {
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

  public class GridImageAdapter02 extends BaseAdapter {
    @Override
    public int getCount() {
      return background02ImageUrls.length;
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
        view = getLayoutInflater().inflate(R.layout.item_backgroundgrid_image, parent, false);
        holder = new ViewHolder();
        assert view != null;
        holder.imageView = (ImageView) view.findViewById(R.id.image);
        holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
        view.setTag(holder);
      } else {
        holder = (ViewHolder) view.getTag();
      }

      imageLoader.displayImage(background02ImageUrls[position], holder.imageView, options, new SimpleImageLoadingListener() {
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


  @Override
  public void onResume() {
    super.onResume();

  }


  public void toggle() {
    TranslateAnimation anim = null;
    TranslateAnimation anim2 = null;

    isOpen = !isOpen;

    RelativeLayout stikeGridLayout = (RelativeLayout) findViewById(R.id.backgroundGridLayout);
    //RelativeLayout stikerMenuGalleryLayout = (RelativeLayout) findViewById(R.id.backgroundMenuGalleryLayout);

    if (isOpen) {
      stikeGridLayout.setVisibility(View.VISIBLE);
      anim = new TranslateAnimation(0.0f, 0.0f, stikeGridLayout.getHeight(), 0.0f);


      //stikerMenuGalleryLayout.setVisibility(View.VISIBLE);
      //anim2 = new TranslateAnimation(0.0f, 0.0f, -(stikerMenuGalleryLayout.getHeight()), 0.0f);

    } else {
      anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, stikeGridLayout.getHeight());
      anim.setAnimationListener(collapseListener);

      //anim2 = new TranslateAnimation(0.0f, 0.0f, 0.0f, -(stikerMenuGalleryLayout.getHeight()));
      //anim2.setAnimationListener(collapseListener);
    }

    anim.setDuration(300);
    anim.setInterpolator(new AccelerateInterpolator(1.0f));
    stikeGridLayout.startAnimation(anim);

    //anim2.setDuration(300);
    //anim2.setInterpolator(new AccelerateInterpolator(1.0f));
    //stikerMenuGalleryLayout.startAnimation(anim2);
  }

  Animation.AnimationListener collapseListener = new Animation.AnimationListener() {
    public void onAnimationEnd(Animation animation) {
      RelativeLayout stikeGridLayout = (RelativeLayout) findViewById(R.id.backgroundGridLayout);
      stikeGridLayout.setVisibility(View.INVISIBLE);

      //RelativeLayout stikerMenuGalleryLayout = (RelativeLayout) findViewById(R.id.backgroundMenuGalleryLayout);
      //stikerMenuGalleryLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }
  };

  /**
   * Get Bitmap's Width
   **/
  public static int getBitmapOfWidth(String fileName) {
    try {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(fileName, options);
      return options.outWidth;
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * Get Bitmap's height
   **/
  public static int getBitmapOfHeight(String fileName) {

    try {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(fileName, options);

      return options.outHeight;
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  public void onBackPressed() {

    super.onBackPressed();


  }


  /******************************************************
   * getTitleList()
   *
   * SD카드 안에 있는 파일 리스트를 읽어 오는 메소드
   ******************************************************/
  private ArrayList<String> getFileList(String Path) //알아 보기 쉽게 메소드 부터 시작합니다.
  {
    try {
      FilenameFilter fileFilter = new FilenameFilter()  //이부분은 특정 확장자만 가지고 오고 싶을 경우 사용하시면 됩니다.
      {
        public boolean accept(File dir, String name) {
          return name.startsWith("Text_"); //이 부분에 사용하고 싶은 확장자를 넣으시면 됩니다.
        } //end accept
      };
      File file = new File(Path); //경로를 SD카드로 잡은거고 그 안에 있는 A폴더 입니다. 입맛에 따라 바꾸세요.
      File[] files = file.listFiles(fileFilter);//위에 만들어 두신 필터를 넣으세요. 만약 필요치 않으시면 fileFilter를 지우세요.
      ArrayList<String> fileList = new ArrayList<String>(); //파일이 있는 만큼 어레이 생성했구요

      for (int i = 0; i < files.length; i++) {
        fileList.add(files[i].getName());        //루프로 돌면서 어레이에 하나씩 집어 넣습니다.
      }//end for
      return fileList;
    } catch (Exception e) {
      return null;
    }//end catch()
  }//end getTitleList

  /**
   * Bitmap 이미지를 가운데를 기준으로 w, h 크기 만큼 crop한다.
   *
   * @param src 원본
   * @param w   넓이
   * @param h   높이
   */
  public static Bitmap cropCenterBitmap(Bitmap src, int w, int h) {
    if (src == null)
      return null;

    int width = src.getWidth();
    int height = src.getHeight();

    if (width < w && height < h)
      return src;

    int x = 0;
    int y = 0;

    if (width > w)
      x = (width - w) / 2;

    if (height > h)
      y = (height - h) / 2;

    int cw = w; // crop width
    int ch = h; // crop height

    if (w > width)
      cw = width;

    if (h > height)
      ch = height;

    Bitmap returnBitmap = Bitmap.createBitmap(src, x, y, cw, ch);

    return returnBitmap;
  }


}