package com.ilovecat.happytogether.StickerClass;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.text.SpannableString;
import android.text.style.ReplacementSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilovecat.happytogether.MainShopRegisterCoupon.MainRegisterCouponShopStep1;
import com.ilovecat.happytogether.R;


public abstract class StickerMessageView extends FrameLayout {

  public static final String TAG = "com.knef.stickerView";
  private BorderView iv_border;
  private ImageView iv_scale;
  private ImageView iv_delete;
  private ImageView iv_flip;
  private ImageView iv_fontscaleY;



  // 사용하지 않음
  //private ImageView iv_fontscaleX;

  //public ImageView iv_fontformat;
  //public ImageView iv_fontsize;
  //public ImageView iv_fontcolor;

  // For scalling
  private float this_orgX = -1, this_orgY = -1;
  private float scale_orgX = -1, scale_orgY = -1;
  private double scale_orgWidth = -1, scale_orgHeight = -1;
  // For rotating
  private float rotate_orgX = -1, rotate_orgY = -1, rotate_newX = -1, rotate_newY = -1;
  // For moving
  private float move_orgX = -1, move_orgY = -1;
  //private float objWidth_ForTempSave = -1, objHeight_ForTempSave = -1;
  private float objWidth_ForTempSave = 270, objHeight_ForTempSave = 120;

  private static double centerX, centerY;

  private final static int BUTTON_SIZE_DP = 30;
  private final static int SELF_SIZE_DP = 100;


  //private int SELF_SIZE_DP_WIDTH = MainRegisterCouponShopStep1.MessageWidth;
  //private int SELF_SIZE_DP_HEIGHT = MainRegisterCouponShopStep1.MessageHeight;
  private static int SELF_SIZE_DP_WIDTH = 270;
  private static int SELF_SIZE_DP_HEIGHT = 120;



  public static int w;
  public static int h;


  public StickerMessageView(Context context) {
    super(context);

    init(context, w, h);
  }

  public StickerMessageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, w, h);
  }

  public StickerMessageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, w, h);
  }

  private void init(final Context context, int width, int height) {


    this.iv_border = new BorderView(context);
    this.iv_scale = new ImageView(context);
    this.iv_delete = new ImageView(context);
    this.iv_flip = new ImageView(context);
    //this.iv_fontscaleX = new ImageView(context);
    this.iv_fontscaleY = new ImageView(context);



    //this.iv_fontformat = new ImageView(context);
    //this.iv_fontsize = new ImageView(context);
    //this.iv_fontcolor = new ImageView(context);

    this.iv_scale.setImageResource(R.drawable.sticker_zoominout);
    this.iv_delete.setImageResource(R.drawable.sticker_remove);
    this.iv_flip.setImageResource(R.drawable.sticker_flip);
    //this.iv_fontscaleX.setImageResource(R.drawable.sticker_zoominout_x);
    this.iv_fontscaleY.setImageResource(R.drawable.sticker_zoominout_y);

    //this.iv_fontformat.setImageResource(R.drawable.sticker_fontformat);
    //this.iv_fontsize.setImageResource(R.drawable.sticker_fontsize);
    //this.iv_fontcolor.setImageResource(R.drawable.sticker_fontcolor);


    this.setTag("DraggableViewGroup");
    this.iv_border.setTag("iv_border");
    this.iv_scale.setTag("iv_scale");
    this.iv_delete.setTag("iv_delete");
    this.iv_flip.setTag("iv_flip");
    //this.iv_fontscaleX.setTag("iv_fontscaleX");
    this.iv_fontscaleY.setTag("iv_fontscaleY");


    //this.iv_fontformat.setTag("iv_fontformat");
    //this.iv_fontsize.setTag("iv_fontsize");
    //this.iv_fontcolor.setTag("iv_fontcolor");


    int margin = convertDpToPixel(BUTTON_SIZE_DP, getContext()) / 2;
    int size = convertDpToPixel(SELF_SIZE_DP, getContext());



    DisplayMetrics metrics = this.getResources().getDisplayMetrics();
    int widthD = metrics.widthPixels;


    int size_width =  convertDpToPixel(SELF_SIZE_DP_WIDTH, getContext());
    int size_height = convertDpToPixel(SELF_SIZE_DP_HEIGHT, getContext());

    LayoutParams this_params =
        new LayoutParams(
            size_width,
            size_height
        );
    this_params.gravity = Gravity.TOP | Gravity.LEFT;
    this_params.setMargins(convertDpToPixel(MainRegisterCouponShopStep1.MessageBoxLeftMargin,context),margin,margin,margin);


    LayoutParams iv_main_params =
        new LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );
    iv_main_params.setMargins(margin, margin, margin, margin);


    LayoutParams iv_border_params =
        new LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );
    iv_border_params.setMargins(margin, margin, margin, margin);


    LayoutParams iv_scale_params =
        new LayoutParams(
            convertDpToPixel(BUTTON_SIZE_DP, getContext()),
            convertDpToPixel(BUTTON_SIZE_DP, getContext())
        );
    iv_scale_params.gravity = Gravity.BOTTOM | Gravity.RIGHT;

    LayoutParams iv_delete_params =
        new LayoutParams(
            convertDpToPixel(BUTTON_SIZE_DP, getContext()),
            convertDpToPixel(BUTTON_SIZE_DP, getContext())
        );
    iv_delete_params.gravity = Gravity.TOP | Gravity.RIGHT;

    LayoutParams iv_flip_params =
        new LayoutParams(
            convertDpToPixel(BUTTON_SIZE_DP, getContext()),
            convertDpToPixel(BUTTON_SIZE_DP, getContext())
        );
    iv_flip_params.gravity = Gravity.TOP | Gravity.LEFT;

    LayoutParams iv_fontscaleX_params =
        new LayoutParams(
            convertDpToPixel(BUTTON_SIZE_DP, getContext()),
            convertDpToPixel(BUTTON_SIZE_DP, getContext())
        );

    iv_fontscaleX_params.gravity = Gravity.LEFT | Gravity.CENTER;

    LayoutParams iv_fontscaleY_params =
        new LayoutParams(
            convertDpToPixel(BUTTON_SIZE_DP, getContext()),
            convertDpToPixel(BUTTON_SIZE_DP, getContext())
        );

    iv_fontscaleY_params.gravity = Gravity.BOTTOM | Gravity.CENTER;


    /*
    LayoutParams iv_fontformat_params =
        new LayoutParams(
            convertDpToPixel(BUTTON_SIZE_DP, getContext()),
            convertDpToPixel(BUTTON_SIZE_DP, getContext())
        );

    iv_fontformat_params.gravity = Gravity.BOTTOM | Gravity.CENTER;
    iv_fontformat_params.setMargins(-convertDpToPixel(35, context), 0, 0, 0);


    LayoutParams iv_fontsize_params =
        new LayoutParams(
            convertDpToPixel(BUTTON_SIZE_DP, getContext()),
            convertDpToPixel(BUTTON_SIZE_DP, getContext())
        );

    iv_fontsize_params.gravity = Gravity.BOTTOM | Gravity.CENTER;

    LayoutParams iv_fontcolor_params =
        new LayoutParams(
            convertDpToPixel(BUTTON_SIZE_DP, getContext()),
            convertDpToPixel(BUTTON_SIZE_DP, getContext())
        );

    iv_fontcolor_params.gravity = Gravity.BOTTOM | Gravity.CENTER;
    iv_fontcolor_params.setMargins(convertDpToPixel(35, context), 0, 0, 0);

    */

    this.setLayoutParams(this_params);
    this.addView(getMainView_Shadow(), iv_main_params);
    this.addView(getMainView(), iv_main_params);


    this.addView(iv_border, iv_border_params);
    this.addView(iv_scale, iv_scale_params);
    this.addView(iv_delete, iv_delete_params);
    this.addView(iv_flip, iv_flip_params);

    //this.addView(iv_fontscaleX, iv_fontscaleX_params);
    this.addView(iv_fontscaleY, iv_fontscaleY_params);

    //this.addView(iv_fontformat, iv_fontformat_params);
    //this.addView(iv_fontsize, iv_fontsize_params);
    //this.addView(iv_fontcolor, iv_fontcolor_params);

    //this.setControlItemsHidden(true);
    this.setOnTouchListener(mTouchListener);
    this.iv_scale.setOnTouchListener(mTouchListener);

    //this.iv_fontscaleX.setOnTouchListener(mTouchListener);
    this.iv_fontscaleY.setOnTouchListener(mTouchListener);

    this.iv_delete.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (StickerMessageView.this.getParent() != null) {

          // Remove Instance In Message ArrayList
          //StickerMessageView clickedStickerMessageView = (StickerMessageView) view;
          int clickedIndex = MainRegisterCouponShopStep1.arrayListStickerMessageView.indexOf(StickerMessageView.this);
          Log.i("clickedIndex: ", String.valueOf(clickedIndex));
          MainRegisterCouponShopStep1.arrayListStickerMessageView.remove(clickedIndex);


          ViewGroup myCanvas = ((ViewGroup) StickerMessageView.this.getParent());
          myCanvas.removeView(StickerMessageView.this);

        }
      }
    });
    this.iv_flip.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View view) {
        Log.v(TAG, "flip the view");

        View mainView = getMainView();
        mainView.setRotationY(mainView.getRotationY() == -180f ? 0f : -180f);
        mainView.invalidate();
        requestLayout();

        View mainView_Shadow = getMainView_Shadow();
        mainView_Shadow.setRotationY(mainView.getRotationY() == -180f ? 0f : -180f);
        mainView_Shadow.invalidate();
        requestLayout();
      }
    });



    /*
    this.iv_fontsize.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View view) {
        Log.v(TAG, "flip the view");

        ((MainRegisterCouponShopStep1)mContext).ShowFontSizeDialog();

      }
    });


  this.iv_fontcolor.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View view) {
        Log.v(TAG, "flip the view");

        ((MainRegisterCouponShopStep1)mContext).getColor(view);

      }
    });
  */

  }







  public boolean isFlip() {
    return getMainView().getRotationY() == -180f;
  }
  public boolean isFlip_Shadow() {
    return getMainView_Shadow().getRotationY() == -180f;
  }

  protected abstract View getMainView();
  protected abstract View getMainView_Shadow();

  private OnTouchListener mTouchListener = new OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent event) {

      if (view.getTag().equals("DraggableViewGroup")) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:


            //##################################################################################
            // 메시지 어래이 배열에서 터치된 객체를 맨 마미막으로 셋하고
            // 기존 마지막 객체를 테치된 객체의 자리에 셋한다.
            int sizeOfArray = MainRegisterCouponShopStep1.arrayListStickerMessageView.size();
            Log.i("sizeOfArray: ", String.valueOf(sizeOfArray));


            StickerMessageView clickedStickerMessageView = (StickerMessageView) view;
            int clickedIndex = MainRegisterCouponShopStep1.arrayListStickerMessageView.indexOf(clickedStickerMessageView);
            Log.i("clickedIndex: ", String.valueOf(clickedIndex));


            StickerMessageView clickedIndexOBJ = MainRegisterCouponShopStep1.arrayListStickerMessageView.get(clickedIndex);
            StickerMessageView lastIndexOBJ = MainRegisterCouponShopStep1.arrayListStickerMessageView.get(sizeOfArray - 1);


            MainRegisterCouponShopStep1.arrayListStickerMessageView.set(sizeOfArray - 1, clickedIndexOBJ);
            MainRegisterCouponShopStep1.arrayListStickerMessageView.set(clickedIndex, lastIndexOBJ);
            //##################################################################################




            // 메시지 어래이의 맨 마지막을 제외한 나머지의 컨트롤뷰를 안보이게 한다.
            int sizeOfArray2 = MainRegisterCouponShopStep1.arrayListStickerMessageView.size();
            for(int i = 0; i < sizeOfArray2 -1; i++) {

              MainRegisterCouponShopStep1.arrayListStickerMessageView.get(i).setControlItemsHidden(true);

            }

            //##################################################################################
            // 메시지 어래이의 맨 마지막 객체의 텍스트 값을 오토컴피리터텍스트뷰에 할당한다.
            StickerMessageTextView lastOBJ = (StickerMessageTextView) MainRegisterCouponShopStep1.arrayListStickerMessageView.get(sizeOfArray -1);
            String textMessage =  lastOBJ.tv_main.getText().toString();
            MainRegisterCouponShopStep1.actvCouponTitle.setText(textMessage);
            MainRegisterCouponShopStep1.actvCouponTitle.invalidate();
            requestLayout();
            //##################################################################################




            int l = MainRegisterCouponShopStep1.arrayListStickerShopLogoView.size();
            for(int i=0; i< l; i++) {
              MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(i).setControlItemsHidden(true);
            }

            StickerMessageView.this.setControlItemsHidden(false);
            StickerMessageView.this.bringToFront();
            //StickerViewShopLogo.bringToFront();
            StickerMessageView.this.invalidate();
            requestLayout();




            Log.v(TAG, "sticker view action down");
            move_orgX = event.getRawX();
            move_orgY = event.getRawY();
            break;

          case MotionEvent.ACTION_MOVE:
            Log.v(TAG, "sticker view action move");
            float offsetX = event.getRawX() - move_orgX;
            float offsetY = event.getRawY() - move_orgY;
            StickerMessageView.this.setX(StickerMessageView.this.getX() + offsetX);
            StickerMessageView.this.setY(StickerMessageView.this.getY() + offsetY);
            move_orgX = event.getRawX();
            move_orgY = event.getRawY();


            Log.i("X", String.valueOf(StickerMessageView.this.getX()));
            Log.i("Y", String.valueOf(StickerMessageView.this.getY()));

            Log.i("W", String.valueOf(objWidth_ForTempSave));
            Log.i("D", String.valueOf(objHeight_ForTempSave));

            getRelativePos(StickerMessageView.this.getX(), StickerMessageView.this.getY());


            // Info
            MainRegisterCouponShopStep1.x.setText(String.valueOf(StickerMessageView.this.getX()));
            MainRegisterCouponShopStep1.y.setText(String.valueOf(StickerMessageView.this.getY()));
            //MainRegisterCouponShopStep1.drawablePath.setText(fileName);

            MainRegisterCouponShopStep1.w.setText(String.valueOf(objWidth_ForTempSave));
            MainRegisterCouponShopStep1.h.setText(String.valueOf(objHeight_ForTempSave));


            int sizeOfArray3 = MainRegisterCouponShopStep1.arrayListStickerMessageView.size();
            StickerMessageTextView clickedView = (StickerMessageTextView) MainRegisterCouponShopStep1.arrayListStickerMessageView.get(sizeOfArray3-1);
            MainRegisterCouponShopStep1.shadowColor.setText(String.valueOf(clickedView.tv_main_shadow.getShadowColor()));
            MainRegisterCouponShopStep1.shadowColorSize.setText(String.valueOf(clickedView.tv_main_shadow.getTextSize()));
            MainRegisterCouponShopStep1.shadowCenter.setText(String.valueOf(clickedView.tv_main_shadow.getCenterState()));

            MainRegisterCouponShopStep1.textColor.setText(String.valueOf(clickedView.tv_main.getCurrentTextColor()));
            MainRegisterCouponShopStep1.textColorSize.setText(String.valueOf(clickedView.tv_main.getTextSize()));
            MainRegisterCouponShopStep1.textFormat.setText(String.valueOf(clickedView.tv_main.getTypefacePath()));
            MainRegisterCouponShopStep1.textPattern.setText(String.valueOf(clickedView.tv_main.getPatternPath()));





            break;

          case MotionEvent.ACTION_UP:


            Log.v(TAG, "sticker view action up");


            break;


        }
      } else if (view.getTag().equals("iv_scale")) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            Log.v(TAG, "iv_scale action down");

            this_orgX = StickerMessageView.this.getX();
            this_orgY = StickerMessageView.this.getY();

            scale_orgX = event.getRawX();
            scale_orgY = event.getRawY();
            scale_orgWidth = StickerMessageView.this.getLayoutParams().width;
            scale_orgHeight = StickerMessageView.this.getLayoutParams().height;

            rotate_orgX = event.getRawX();
            rotate_orgY = event.getRawY();

            centerX = StickerMessageView.this.getX() +
                ((View) StickerMessageView.this.getParent()).getX() +
                (float) StickerMessageView.this.getWidth() / 2;


            //double statusBarHeight = Math.ceil(25 * getContext().getResources().getDisplayMetrics().density);
            int result = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
              result = getResources().getDimensionPixelSize(resourceId);
            }
            double statusBarHeight = result;
            centerY = StickerMessageView.this.getY() +
                ((View) StickerMessageView.this.getParent()).getY() +
                statusBarHeight +
                (float) StickerMessageView.this.getHeight() / 2;

            break;
          case MotionEvent.ACTION_MOVE:
            Log.v(TAG, "iv_scale action move");

            rotate_newX = event.getRawX();
            rotate_newY = event.getRawY();

            double angle_diff = Math.abs(
                Math.atan2(event.getRawY() - scale_orgY, event.getRawX() - scale_orgX)
                    - Math.atan2(scale_orgY - centerY, scale_orgX - centerX)) * 180 / Math.PI;

            Log.v(TAG, "angle_diff: " + angle_diff);

            double length1 = getLength(centerX, centerY, scale_orgX, scale_orgY);
            double length2 = getLength(centerX, centerY, event.getRawX(), event.getRawY());

            int size = convertDpToPixel(SELF_SIZE_DP, getContext());
            if (length2 > length1
                && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                ) {
              //scale up
               /*
                            double offsetX = Math.abs(event.getRawX() - scale_orgX);
                            double offsetY = Math.abs(event.getRawY() - scale_orgY);
                            double offset = Math.max(offsetX, offsetY);
                            offset = Math.round(offset);
                            */
              double ratio = Math.abs(event.getRawX() / scale_orgX);
              double ratioWidth = StickerMessageView.this.getLayoutParams().width * ratio;
              ratioWidth = Math.round(ratioWidth);
              double ratioHeight = StickerMessageView.this.getLayoutParams().height * ratio;
              ratioHeight = Math.round(ratioHeight);

              StickerMessageView.this.getLayoutParams().width = (int) ratioWidth;
              StickerMessageView.this.getLayoutParams().height = (int) ratioHeight;


              objWidth_ForTempSave = (float)convertPixelToDp((float)ratioWidth,getContext());
              objHeight_ForTempSave = (float)convertPixelToDp((float)ratioHeight,getContext());

              onScaling(true);
              //DraggableViewGroup.this.setX((float) (getX() - offset / 2));
              //DraggableViewGroup.this.setY((float) (getY() - offset / 2));
            } else if (length2 < length1
                && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                && StickerMessageView.this.getLayoutParams().width > size / 2
                && StickerMessageView.this.getLayoutParams().height > size / 2) {
              //scale down
              /*
                            double offsetX = Math.abs(event.getRawX() - scale_orgX);
                            double offsetY = Math.abs(event.getRawY() - scale_orgY);
                            double offset = Math.max(offsetX, offsetY);
                            offset = Math.round(offset);
                            */

              double ratio = Math.abs(event.getRawX() / scale_orgX);
              double ratioWidth = StickerMessageView.this.getLayoutParams().width * ratio;
              ratioWidth = Math.round(ratioWidth);
              double ratioHeight = StickerMessageView.this.getLayoutParams().height * ratio;
              ratioHeight = Math.round(ratioHeight);


              StickerMessageView.this.getLayoutParams().width = (int) ratioWidth;
              StickerMessageView.this.getLayoutParams().height = (int) ratioHeight;


              objWidth_ForTempSave = (float)convertPixelToDp((float)ratioWidth,getContext());
              objHeight_ForTempSave = (float)convertPixelToDp((float)ratioHeight,getContext());

              onScaling(false);
            }

            //rotate
                        /*
                        double angle = Math.atan2(event.getRawY() - centerY, event.getRawX() - centerX) * 180 / Math.PI;
                        Log.v(TAG, "log angle: " + angle);

                        //setRotation((float) angle - 45);
                        setRotation((float) angle - 45);
                        Log.v(TAG, "getRotation(): " + getRotation());

                        onRotating();

                        rotate_orgX = rotate_newX;
                        rotate_orgY = rotate_newY;
                        */
            scale_orgX = event.getRawX();
            scale_orgY = event.getRawY();


            // Info
            //MainRegisterCouponShopStep1.w.setText(String.valueOf(StickerMessageView.this.getLayoutParams().width));
            //MainRegisterCouponShopStep1.h.setText(String.valueOf(StickerMessageView.this.getLayoutParams().height));

            MainRegisterCouponShopStep1.w.setText(String.valueOf(objWidth_ForTempSave));
            MainRegisterCouponShopStep1.h.setText(String.valueOf(objHeight_ForTempSave));




            postInvalidate();
            requestLayout();
            break;

          case MotionEvent.ACTION_UP:
            Log.v(TAG, "iv_scale action up");
            break;


        }
      } else if (view.getTag().equals("iv_fontscaleY")) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            Log.v(TAG, "iv_scaleY action down");

            this_orgX = StickerMessageView.this.getX();
            this_orgY = StickerMessageView.this.getY();

            scale_orgX = event.getRawX();
            scale_orgY = event.getRawY();
            scale_orgWidth = StickerMessageView.this.getLayoutParams().width;
            scale_orgHeight = StickerMessageView.this.getLayoutParams().height;

            rotate_orgX = event.getRawX();
            rotate_orgY = event.getRawY();

            centerX = StickerMessageView.this.getX() +
                ((View) StickerMessageView.this.getParent()).getX() +
                (float) StickerMessageView.this.getWidth() / 2;


            //double statusBarHeight = Math.ceil(25 * getContext().getResources().getDisplayMetrics().density);
            int result = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
              result = getResources().getDimensionPixelSize(resourceId);
            }
            double statusBarHeight = result;
            centerY = StickerMessageView.this.getY() +
                ((View) StickerMessageView.this.getParent()).getY() +
                statusBarHeight +
                (float) StickerMessageView.this.getHeight() / 2;

            break;
          case MotionEvent.ACTION_MOVE:
            Log.v(TAG, "iv_scaleY action move");

            rotate_newX = event.getRawX();
            rotate_newY = event.getRawY();

            double angle_diff = Math.abs(
                Math.atan2(event.getRawY() - scale_orgY, event.getRawX() - scale_orgX)
                    - Math.atan2(scale_orgY - centerY, scale_orgX - centerX)) * 180 / Math.PI;

            Log.v(TAG, "angle_diff: " + angle_diff);

            double length1 = getLength(centerX, centerY, scale_orgX, scale_orgY);
            double length2 = getLength(centerX, centerY, event.getRawX(), event.getRawY());

            int size = convertDpToPixel(SELF_SIZE_DP, getContext());
            if (length2 > length1
                && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                ) {
              //scale up
                            double offsetX = Math.abs(event.getRawX() - scale_orgX);
                            double offsetY = Math.abs(event.getRawY() - scale_orgY);
                            double offset = Math.max(offsetX, offsetY);
                            offset = Math.round(offset);

              double ratio = Math.abs(event.getRawX() / scale_orgX);
              double ratioWidth = StickerMessageView.this.getLayoutParams().width * ratio;
              ratioWidth = Math.round(ratioWidth);
              double ratioHeight = StickerMessageView.this.getLayoutParams().height * ratio;
              ratioHeight = Math.round(ratioHeight);

              //StickerViewMessage.this.getLayoutParams().width = (int) ratioWidth;
              StickerMessageView.this.getLayoutParams().height += offsetY;


              objWidth_ForTempSave = (float)convertPixelToDp((float)ratioWidth,getContext());
              objHeight_ForTempSave = (float)convertPixelToDp((float)ratioHeight,getContext());

              onScaling(true);
              //DraggableViewGroup.this.setX((float) (getX() - offset / 2));
              //DraggableViewGroup.this.setY((float) (getY() - offset / 2));
            } else if (length2 < length1
                && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                && StickerMessageView.this.getLayoutParams().width > size / 2
                && StickerMessageView.this.getLayoutParams().height > size / 2) {
              //scale down
                            double offsetX = Math.abs(event.getRawX() - scale_orgX);
                            double offsetY = Math.abs(event.getRawY() - scale_orgY);
                            double offset = Math.max(offsetX, offsetY);
                            offset = Math.round(offset);

              double ratio = Math.abs(event.getRawX() / scale_orgX);
              double ratioWidth = StickerMessageView.this.getLayoutParams().width * ratio;
              ratioWidth = Math.round(ratioWidth);
              double ratioHeight = StickerMessageView.this.getLayoutParams().height * ratio;
              ratioHeight = Math.round(ratioHeight);


             //StickerViewMessage.this.getLayoutParams().width = (int) ratioWidth;
              StickerMessageView.this.getLayoutParams().height -= offsetY;


              objWidth_ForTempSave = (float)convertPixelToDp((float)ratioWidth,getContext());
              objHeight_ForTempSave = (float)convertPixelToDp((float)ratioHeight,getContext());

              onScaling(false);
            }

            //rotate
                        /*
                        double angle = Math.atan2(event.getRawY() - centerY, event.getRawX() - centerX) * 180 / Math.PI;
                        Log.v(TAG, "log angle: " + angle);

                        //setRotation((float) angle - 45);
                        setRotation((float) angle - 45);
                        Log.v(TAG, "getRotation(): " + getRotation());

                        onRotating();

                        rotate_orgX = rotate_newX;
                        rotate_orgY = rotate_newY;
                        */
            scale_orgX = event.getRawX();
            scale_orgY = event.getRawY();

            MainRegisterCouponShopStep1.w.setText(String.valueOf(objWidth_ForTempSave));
            MainRegisterCouponShopStep1.h.setText(String.valueOf(objHeight_ForTempSave));

            postInvalidate();
            requestLayout();
            break;

          case MotionEvent.ACTION_UP:
            Log.v(TAG, "iv_scaleY action up");
            break;

        }

      }  else if (view.getTag().equals("iv_fontscaleX")) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            Log.v(TAG, "iv_scaleX action down");

            this_orgX = StickerMessageView.this.getX();
            this_orgY = StickerMessageView.this.getY();

            scale_orgX = event.getRawX();
            scale_orgY = event.getRawY();
            scale_orgWidth = StickerMessageView.this.getLayoutParams().width;
            scale_orgHeight = StickerMessageView.this.getLayoutParams().height;

            rotate_orgX = event.getRawX();
            rotate_orgY = event.getRawY();

            centerX = StickerMessageView.this.getX() +
                ((View) StickerMessageView.this.getParent()).getX() +
                (float) StickerMessageView.this.getWidth() / 2;


            //double statusBarHeight = Math.ceil(25 * getContext().getResources().getDisplayMetrics().density);
            int result = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
              result = getResources().getDimensionPixelSize(resourceId);
            }
            double statusBarHeight = result;
            centerY = StickerMessageView.this.getY() +
                ((View) StickerMessageView.this.getParent()).getY() +
                statusBarHeight +
                (float) StickerMessageView.this.getHeight() / 2;

            break;
          case MotionEvent.ACTION_MOVE:
            Log.v(TAG, "iv_scaleX action move");

            rotate_newX = event.getRawX();
            rotate_newY = event.getRawY();

            double angle_diff = Math.abs(
                Math.atan2(event.getRawY() - scale_orgY, event.getRawX() - scale_orgX)
                    - Math.atan2(scale_orgY - centerY, scale_orgX - centerX)) * 180 / Math.PI;

            Log.v(TAG, "angle_diff: " + angle_diff);

            double length1 = getLength(centerX, centerY, scale_orgX, scale_orgY);
            double length2 = getLength(centerX, centerY, event.getRawX(), event.getRawY());

            int size = convertDpToPixel(SELF_SIZE_DP, getContext());
            if (length2 > length1
                && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                ) {
              //scale up
              double offsetX = Math.abs(event.getRawX() - scale_orgX);
              double offsetY = Math.abs(event.getRawY() - scale_orgY);
              double offset = Math.max(offsetX, offsetY);
              offset = Math.round(offset);

              double ratio = Math.abs(event.getRawX() / scale_orgX);
              double ratioWidth = StickerMessageView.this.getLayoutParams().width * ratio;
              ratioWidth = Math.round(ratioWidth);
              double ratioHeight = StickerMessageView.this.getLayoutParams().height * ratio;
              ratioHeight = Math.round(ratioHeight);

              StickerMessageView.this.getLayoutParams().width += offsetX;
              //StickerViewMessage.this.getLayoutParams().height += offsetY;
              onScaling(true);
              //DraggableViewGroup.this.setX((float) (getX() - offset / 2));
              //DraggableViewGroup.this.setY((float) (getY() - offset / 2));
            } else if (length2 < length1
                && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                && StickerMessageView.this.getLayoutParams().width > size / 2
                && StickerMessageView.this.getLayoutParams().height > size / 2) {
              //scale down
              double offsetX = Math.abs(event.getRawX() - scale_orgX);
              double offsetY = Math.abs(event.getRawY() - scale_orgY);
              double offset = Math.max(offsetX, offsetY);
              offset = Math.round(offset);

              double ratio = Math.abs(event.getRawX() / scale_orgX);
              double ratioWidth = StickerMessageView.this.getLayoutParams().width * ratio;
              ratioWidth = Math.round(ratioWidth);
              double ratioHeight = StickerMessageView.this.getLayoutParams().height * ratio;
              ratioHeight = Math.round(ratioHeight);


              StickerMessageView.this.getLayoutParams().width -= offsetX;
              StickerMessageView.this.getLayoutParams().height -= offsetY;


              onScaling(false);
            }

            //rotate
                        /*
                        double angle = Math.atan2(event.getRawY() - centerY, event.getRawX() - centerX) * 180 / Math.PI;
                        Log.v(TAG, "log angle: " + angle);

                        //setRotation((float) angle - 45);
                        setRotation((float) angle - 45);
                        Log.v(TAG, "getRotation(): " + getRotation());

                        onRotating();

                        rotate_orgX = rotate_newX;
                        rotate_orgY = rotate_newY;
                        */
            scale_orgX = event.getRawX();
            scale_orgY = event.getRawY();


            postInvalidate();
            requestLayout();
            break;

          case MotionEvent.ACTION_UP:
            Log.v(TAG, "iv_scaleX action up");
            break;

        }
      }
      return true;
    }

  };





  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
  }

  private double getLength(double x1, double y1, double x2, double y2) {
    return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
  }

  private float[] getRelativePos(float absX, float absY) {
    Log.v("ken", "getRelativePos getX:" + ((View) this.getParent()).getX());
    Log.v("ken", "getRelativePos getY:" + ((View) this.getParent()).getY());
    float[] pos = new float[]{
        absX - ((View) this.getParent()).getX(),
        absY - ((View) this.getParent()).getY()
    };

    Log.v(TAG, "getRelativePos absX:" + absX);
    Log.v(TAG, "getRelativePos relativeX:" + pos[0]);



    Log.v(TAG, "getRelativePos absY:" + absY);
    Log.v(TAG, "getRelativePos relativeY:" + pos[1]);
    return pos;
  }

  public void setControlItemsHidden(boolean isHidden) {
    if (isHidden) {
      iv_border.setVisibility(View.INVISIBLE);
      iv_scale.setVisibility(View.INVISIBLE);
      iv_delete.setVisibility(View.INVISIBLE);
      iv_flip.setVisibility(View.INVISIBLE);
      //iv_fontscaleX.setVisibility(View.INVISIBLE);
      iv_fontscaleY.setVisibility(View.INVISIBLE);

      //iv_fontcolor.setVisibility(View.INVISIBLE);
      //iv_fontsize.setVisibility(View.INVISIBLE);
      //iv_fontformat.setVisibility(View.INVISIBLE);

    } else {
      iv_border.setVisibility(View.VISIBLE);
      iv_scale.setVisibility(View.VISIBLE);

      //iv_fontscaleX.setVisibility(View.VISIBLE);
      iv_fontscaleY.setVisibility(View.VISIBLE);
      iv_delete.setVisibility(View.VISIBLE);

      //iv_fontcolor.setVisibility(View.VISIBLE);
      //iv_fontsize.setVisibility(View.VISIBLE);
      //iv_fontformat.setVisibility(View.VISIBLE);
    }
  }


  public void setControlItemsMoveLock(boolean isMoveLock) {
    if (isMoveLock) {


      this.setTag("NonDraggableViewGroup");

    } else {

      this.setTag("DraggableViewGroup");
      }
  }


  protected View getImageViewFlip() {
    return iv_flip;
  }

  protected void onScaling(boolean scaleUp) {
  }

  protected void onRotating() {
  }




  private class BorderView extends View {
    public BorderView(Context context) {
      super(context);
    }

    public BorderView(Context context, AttributeSet attrs) {
      super(context, attrs);
    }

    public BorderView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
      super.onDraw(canvas);
      // Draw sticker border

      LayoutParams params = (LayoutParams) this.getLayoutParams();

      Log.v(TAG, "params.leftMargin: " + params.leftMargin);

      Rect border = new Rect();
      border.left = (int) this.getLeft() - params.leftMargin;
      border.top = (int) this.getTop() - params.topMargin;
      border.right = (int) this.getRight() - params.rightMargin;
      border.bottom = (int) this.getBottom() - params.bottomMargin;
      Paint borderPaint = new Paint();
      borderPaint.setColor(Color.LTGRAY);
      borderPaint.setStyle(Paint.Style.STROKE);

      borderPaint.setAntiAlias(true);
      borderPaint.setDither(true);
      borderPaint.setPathEffect(new DashPathEffect(new float[]{ 5, 5, }, 0));
      borderPaint.setStrokeWidth(6);


      canvas.drawRect(border, borderPaint);

    }
  }


  private static int convertDpToPixel(float dp, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float px = dp * (metrics.densityDpi / 160f);
    return (int) px;
  }


  /**
   * px을 dp로 변환
   * @param con
   * @param px
   * @return dp
   */
  public static int getPxToDp(Context con, int px) {
    float density = 0.0f;
    density  = con.getResources().getDisplayMetrics().density;
    Log.d("Desity", "density = " + density);
    return (int)(px / density);
  }

  // TempSave
  public static double getCenterX() {
    return centerX;
  }

  public static double getCenterY() {
    return centerY;

  }



  public float getObjWidth() {
    return objWidth_ForTempSave;
  }
  public void setObjWidth(float w) {
    this.objWidth_ForTempSave = w;
  }


  public float getObjHeight() {
    return objHeight_ForTempSave;
  }
  public void setObjHeight(float h) {
    this.objHeight_ForTempSave = h;
  }

  private static int convertPixelToDp(float pixel, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float dp = pixel / (metrics.densityDpi / 160f);
    return (int) dp;
  }

}
