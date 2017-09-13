package com.ilovecat.happytogether.MainShopRegisterCoupon;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ilovecat.happytogether.CommonConstantsAndUtils.CommonUtils;
import com.ilovecat.happytogether.MainShopListingCoupon.MainCouponShopFragment;
import com.ilovecat.happytogether.R;
import com.ilovecat.happytogether.StickerClass.StickerMessageTextView;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 08. 14. happytogether
 */
/*
File: execute, doInBackground 의 파라미터 타입
Integer: onProgressUpdate 의 파라미터 타입
Int: doInBackground 의 리턴값, onPostExecute 의 파라미터로 설정됩니다.
*/
public class AsyncTaskTempSave_Step1 extends AsyncTask<Integer, Integer, Integer> {

  ProgressDialog dialog;

  @Override
  protected void onCancelled() {
    super.onCancelled();
  }

  @Override
  protected void onPostExecute(Integer result) {
    //btn.setText("Thread END");
    super.onPostExecute(result);
    if (result == 1) {
      Log.i("copy", "SUCESS");

     /*
      * 14/11/01
      * 화면을 캡처하여 이미지 사이즈대로 자른 후
      * 쿠폰보관함용으로
      * TempSave 폴더로 저장한다.
      */
      FrameLayout container;
      container = (FrameLayout) MainRegisterCouponShopStep1.appcActivity.findViewById(R.id.StcikerFrameLayout);
      container.setDrawingCacheEnabled(true);
      container.buildDrawingCache();
      //container.invalidate(); // 뷰를 갱신해 주어야 함

      int sizeOfArray = MainRegisterCouponShopStep1.arrayListStickerMessageView.size();
      for (int i = 0; i < sizeOfArray; i++) {
        MainRegisterCouponShopStep1.arrayListStickerMessageView.get(i).setControlItemsHidden(true);
      }

      int sizeOfArray2 = MainRegisterCouponShopStep1.arrayListStickerShopLogoView.size();
      for (int i = 0; i < sizeOfArray2; i++) {
        MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(i).setControlItemsHidden(true);
      }
      //tv_ShopNameSticker.setControlItemsHidden(true);

      MainRegisterCouponShopStep1.captureBitmap = container.getDrawingCache();


      FileOutputStream fos;
      try {
        fos = new FileOutputStream(MainRegisterCouponShopStep1.TEMPSAVE_PATH_OF_SET
            + "captured_"
            + MainRegisterCouponShopStep1.SAVE_TIME + ".jpg");

        // 100일 경우 500K 정도 파일크기가 되므로 50으로 수정. 50일 경우 80k 정도 차지
        MainRegisterCouponShopStep1.captureBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }

      Toast.makeText(MainRegisterCouponShopStep1.appcActivity, "쿠폰 임시보관함에 보관되었습니다",
          Toast.LENGTH_SHORT).show();


    }

    if (null != dialog && dialog.isShowing()) {
      dialog.dismiss();
    }

  }

  @Override
  protected void onPreExecute() {
    //btn.setText("Thread START!!!!");
    dialog = ProgressDialog.show(MainRegisterCouponShopStep1.appcActivity, "",
        "임시보관중입니다. 잠시 기다려주세요", true);
    super.onPreExecute();

  }

  @Override
  protected void onProgressUpdate(Integer... values) {
    //pb.setProg0ress(values[0]);
    super.onProgressUpdate(values);
  }

  @Override
  protected Integer doInBackground(Integer... params) {
    int result = 0;
    int n = params[0];

    String onPressState = "NEXT";

      /*
      if(TEMPSAVE(TEMPSAVE_ID, onPressState)) {
        result = 1;
      }
      */
    if (TEMPSAVE()) {
      result = 1;
    }
    return result;

  }


  /******************************************************
   * TEMPSAVE(String TEMPSAVE_ID, String stateOfOnPress)
   *
   * XML 파싱하여 메모리에 할당한 후 XML를 저장, Temp 파일을 TempSave로 넘긴다. 다이얼로그 윈도우 띄움
   ******************************************************/

  //boolean TEMPSAVE(String TEMPSAVE_ID, String stateOfOnPress) {
  boolean TEMPSAVE() {

    try {

      // TempSave 폴더 생성
      String str = Environment.getExternalStorageState();
      if (str.equals(Environment.MEDIA_MOUNTED)) {

        String strTempSavePath = MainRegisterCouponShopStep1.TEMPSAVE_PATH;
        File file = new File(strTempSavePath);
        if (!file.exists()) // 원하는 경로에 폴더가 있는지
          // 확인
          file.mkdirs(); // 상위폴더까지 생성

      } else {
        Toast.makeText(MainRegisterCouponShopStep1.appcActivity, "SD Card 인식 실패", Toast.LENGTH_SHORT)
            .show();
      }



      /*
      if(stateOfOnPress.equals("FILTER") || stateOfOnPress.equals("ROTATE")
          || stateOfOnPress.equals("REMOVE_PAGE")) {
        SAVE_TIME =  TEMPSAVE_ID;
      } else {
        SAVE_TIME =  String.valueOf(System.currentTimeMillis());
      }
      */

      /*
       * TempSave 폴더 아래 저장시간 폴더를 생성.
       */

      MainRegisterCouponShopStep1.SAVE_TIME = String.valueOf(System.currentTimeMillis());

      //"/DCIM/HappyTogether/TempSave/" + 저장시간/
      MainRegisterCouponShopStep1.TEMPSAVE_PATH_OF_SET =
          MainRegisterCouponShopStep1.TEMPSAVE_PATH
              + MainRegisterCouponShopStep1.SAVE_TIME
              + "/";

      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      docFactory.setIgnoringElementContentWhitespace(true);
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();


      // 루트 엘리먼트
      // "/DCIM/HappyTogether/TempSave/" + tempsave.xml
      File XmlFile = new File(MainRegisterCouponShopStep1.TEMPSAVE_XML_PATH);
      Document doc;
      Element rootElement;


      if (XmlFile.exists()) {
        doc = docBuilder.parse(XmlFile);
        rootElement = doc.getDocumentElement();

      } else {
        doc = docBuilder.newDocument();
        rootElement = doc.createElement("tempsave");
        doc.appendChild(rootElement);
      }

      Collection<TempSaveSet> tempsaveset = new ArrayList<TempSaveSet>();
      tempsaveset.add(new TempSaveSet());

      for (TempSaveSet k : tempsaveset) {

        //######################################################
        //XML <tempsaveSet id="1471007744853">
        // tempsaveSet 엘리먼트
        Element tempsaveSet = doc.createElement("tempsaveSet");
        rootElement.appendChild(tempsaveSet);
        // 속성값 정의
        Attr attr = doc.createAttribute("id");
        attr.setValue(MainRegisterCouponShopStep1.SAVE_TIME);
        tempsaveSet.setAttributeNode(attr);
        //######################################################

        //######################################################
        //XML <id>1471007744853</id>
        // id(생성일) 엘리먼트
        Element idDate = doc.createElement("id");
        idDate.appendChild(doc.createTextNode(MainRegisterCouponShopStep1.SAVE_TIME));
        tempsaveSet.appendChild(idDate);
        //######################################################


        // totalPagerNum 엘리먼트
        Element totalPagerNum = doc.createElement("totalPagerNum");
        //totalPagerNum.appendChild(doc.createTextNode(String.valueOf(TOTAL_PAGER_NUM)));
        totalPagerNum.appendChild(doc.createTextNode(String.valueOf("1")));
        tempsaveSet.appendChild(totalPagerNum);


        //for(int i = 0; i < TOTAL_PAGER_NUM; i++) {
        for (int i = 0; i < 1; i++) {

          //######################################################
          String pagerIndexTag = "Pager";
          //Element pagerIndex[] = new Element[TOTAL_PAGER_NUM];
          Element pagerIndex[] = new Element[1];

          // XML <Pager index="0">
          pagerIndex[i] = doc.createElement(pagerIndexTag);
          // 속성값 정의
          Attr attrPager = doc.createAttribute("index");
          attrPager.setValue(String.valueOf(i));
          pagerIndex[i].setAttributeNode(attrPager);
          //######################################################


          //######################################################
          // <ImageUrl>/storage/emulated/0/DCIM/MyTown/TempSave/1471007744853/temp_2016080209320233944_1494091357_1.jpg</ImageUrl>
          //IMAGE URL
          String pagerBackGroundImageTag = "BackGroundFilePathAndName";
          Element pagerImage = doc.createElement(pagerBackGroundImageTag);

          /*
          String arrImageName[]  =  imageUrls.get(i).split("/");
          String lastImageName = arrImageName[arrImageName.length - 1];
          String FullPath = TEMPSAVE_PATH_OF_SET + lastImageName;
          */
          pagerImage.appendChild(doc.createTextNode(MainRegisterCouponShopStep1.BackGroundFilePathAndName));
          pagerIndex[i].appendChild(pagerImage);
          Log.i("BGFilePathAndName", String.valueOf(MainRegisterCouponShopStep1.BackGroundFilePathAndName));
          //######################################################


          //######################################################
          // XML <stikerTotalNum>2</stikerTotalNum>
          //페이저당 스티커 갯수
          String stikerTotalNumPerPagerTag = "stikerTotalNum";
          Element stikerTotalNumPerPager = doc.createElement(stikerTotalNumPerPagerTag);

          int countOfStikerNum = 0;

          if (i == 0) {
            countOfStikerNum = MainRegisterCouponShopStep1.arrayListStickerShopLogoView.size();
            Log.i(" countOfStikerNum", String.valueOf(countOfStikerNum));
          }
          stikerTotalNumPerPager.appendChild(doc.createTextNode(String.valueOf(countOfStikerNum)));
          pagerIndex[i].appendChild(stikerTotalNumPerPager);
          //######################################################

          //######################################################
          // XML <textTotalNum>1</textTotalNum>
          //페이저당 텍스트 갯수
          String textTotalNumPerPagerTag = "textTotalNum";
          Element textTotalNumPerPager = doc.createElement(textTotalNumPerPagerTag);

          int countOfTextNum = 0;

          if (i == 0) {
            countOfTextNum = MainRegisterCouponShopStep1.arrayListStickerMessageView.size();
          }

          textTotalNumPerPager.appendChild(doc.createTextNode(String.valueOf(countOfTextNum)));
          pagerIndex[i].appendChild(textTotalNumPerPager);
          //######################################################


          //페이저당 스티커 엘리먼트
          int countOfStikerView = 0;

          if (i == 0) {
            countOfStikerView = MainRegisterCouponShopStep1.arrayListStickerShopLogoView.size();
          }

          if (countOfStikerView != 0) {

            String[] stikersCenterX = new String[countOfStikerView];
            String[] stikersCenterY = new String[countOfStikerView];
            String[] stikersWidth = new String[countOfStikerView];
            String[] stikersHeight = new String[countOfStikerView];
            String[] stikersDrawable = new String[countOfStikerView];

            int actionBarHeight_px = 0;
            int actionBarHeight_dp = 0;
            // Calculate ActionBar height
            TypedValue tv = new TypedValue();
            if (MainRegisterCouponShopStep1.appcActivity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
              actionBarHeight_px = TypedValue.complexToDimensionPixelSize(tv.data,
                  MainRegisterCouponShopStep1.appcActivity.getResources().getDisplayMetrics());
              actionBarHeight_dp = CommonUtils.convertPixelToDp(actionBarHeight_px,
                  MainRegisterCouponShopStep1.context);

              Log.i("dsd", String.valueOf(actionBarHeight_px));
              Log.i("sdsds", String.valueOf(actionBarHeight_dp));

            }


            for (int j = 0; j < countOfStikerView; j++) {

              Log.i("j is", String.valueOf(j));

              // XML<stikerPerPager index="0">
              String stikerPerPagerTag = "stikerPerPager";
              Element stikerPerPager[] = new Element[countOfStikerView];
              stikerPerPager[j] = doc.createElement(stikerPerPagerTag);
              // 속성값 정의
              Attr attrStiker = doc.createAttribute("index");
              attrStiker.setValue(String.valueOf(j));
              stikerPerPager[j].setAttributeNode(attrStiker);

              stikersCenterX[j] = "0";
              stikersCenterY[j] = "0";
              stikersWidth[j] = "0";
              stikersHeight[j] = "0";
              stikersDrawable[j] = "";



              stikersWidth[j] = String.valueOf(MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(j).getObjWidth());
              stikersHeight[j] = String.valueOf(MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(j).getObjHeight());


              /**
               * 스티커 이미지의 Y좌표 값 보정을 위한 값
               * 130은 스티커 이미지의 기본 가로/세로 길이
               */
              int scaleSpread = (int)MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(j).getObjWidth() - 130;


              stikersCenterX[j] = String.valueOf(MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(j).getX());
              stikersCenterY[j] = String.valueOf(MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(j).getY() - actionBarHeight_dp);
              stikersDrawable[j] = String.valueOf(MainRegisterCouponShopStep1.arrayListStickerShopLogoView.get(j).getPathAndFileName());

              Element stikerCenterX = doc.createElement("stikerCenterX");
              stikerCenterX.appendChild(doc.createTextNode(stikersCenterX[j]));
              stikerPerPager[j].appendChild(stikerCenterX);
              Log.i("XXXXXXXX", String.valueOf(stikersCenterX[j]));

              Element stikerCenterY = doc.createElement("stikerCenterY");
              stikerCenterY.appendChild(doc.createTextNode(stikersCenterY[j]));
              stikerPerPager[j].appendChild(stikerCenterY);
              Log.i("YYYYYYYY", String.valueOf(stikersCenterY[j]));

              Element stikerWidth = doc.createElement("stikerWidth");
              stikerWidth.appendChild(doc.createTextNode(stikersWidth[j]));
              stikerPerPager[j].appendChild(stikerWidth);
              Log.i("WIDTH", String.valueOf(stikersWidth[j]));

              Element stikerHeight = doc.createElement("stikerHeight");
              stikerHeight.appendChild(doc.createTextNode(stikersHeight[j]));
              stikerPerPager[j].appendChild(stikerHeight);
              Log.i("HEIGHT", String.valueOf(stikersHeight[j]));

              Element stikerDrawable = doc.createElement("stikerDrawable");
              stikerDrawable.appendChild(doc.createTextNode(stikersDrawable[j]));
              stikerPerPager[j].appendChild(stikerDrawable);
              Log.i("Drawable", String.valueOf(stikersDrawable[j]));


              pagerIndex[i].appendChild(stikerPerPager[j]);

            }
          }

          //페이저당 텍스트 엘리먼트
          int countOfTextView = 0;

          if (i == 0) {
            countOfTextView = MainRegisterCouponShopStep1.arrayListStickerMessageView.size();
          }


          if (countOfTextView != 0) {

            String[] textsCenterX = new String[countOfTextView];
            String[] textsCenterY = new String[countOfTextView];
            String[] textsWidth = new String[countOfTextView];
            String[] textsHeight = new String[countOfTextView];
            String[] textsContent = new String[countOfTextView];

            int actionBarHeight_px = 0;
            int actionBarHeight_dp = 0;
            // Calculate ActionBar height
            TypedValue tv = new TypedValue();
            if (MainRegisterCouponShopStep1.appcActivity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
              actionBarHeight_px = TypedValue.complexToDimensionPixelSize(tv.data,
                  MainRegisterCouponShopStep1.appcActivity.getResources().getDisplayMetrics());
              actionBarHeight_dp = CommonUtils.convertPixelToDp(actionBarHeight_px,
                  MainRegisterCouponShopStep1.context);

              Log.i("dsd", String.valueOf(actionBarHeight_px));
              Log.i("sdsds", String.valueOf(actionBarHeight_dp));

            }



            for (int j = 0; j < countOfTextView; j++) {


              Log.i("j is", String.valueOf(j));


              // XML <textPerPager index="0">
              String textPerPagerTag = "textPerPager";
              Element textPerPager[] = new Element[countOfTextView];
              textPerPager[j] = doc.createElement(textPerPagerTag);
              // 속성값 정의
              Attr attrText = doc.createAttribute("index");
              attrText.setValue(String.valueOf(j));
              textPerPager[j].setAttributeNode(attrText);

              textsCenterX[j] = "0";
              textsCenterY[j] = "0";
              textsWidth[j] = "0";
              textsHeight[j] = "0";
              textsContent[j] = "";

              textsWidth[j] = String.valueOf(MainRegisterCouponShopStep1.arrayListStickerMessageView.get(j).getObjWidth());
              textsHeight[j] = String.valueOf(MainRegisterCouponShopStep1.arrayListStickerMessageView.get(j).getObjHeight());


              /**
               * 스티커 이미지의 Y좌표 값 보정을 위한 값
               * 130은 스티커 이미지의 기본 가로/세로 길이
               */
              int scaleSpread = (int)MainRegisterCouponShopStep1.arrayListStickerMessageView.get(j).getObjWidth();


              //textsCenterX[j] = String.valueOf(MainRegisterCouponShopStep1.arrayListStickerMessageView.get(j).getCenterX());
              //textsCenterY[j] = String.valueOf(MainRegisterCouponShopStep1.arrayListStickerMessageView.get(j).getCenterY());
              textsCenterX[j] = String.valueOf(MainRegisterCouponShopStep1.arrayListStickerMessageView.get(j).getX());
              textsCenterY[j] = String.valueOf(MainRegisterCouponShopStep1.arrayListStickerMessageView.get(j).getY() - actionBarHeight_dp);


              StickerMessageTextView lastOBJ = (StickerMessageTextView) MainRegisterCouponShopStep1.arrayListStickerMessageView.get(j);
              String textMessage =  lastOBJ.tv_main.getText().toString();
              textsContent[j] = textMessage;


              /*
              if (i == 0) {
                textsCenterX[j] = String.valueOf(MainRegisterCouponShopStep1.arrayListStickerMessageView.get(j).getCenterX());
              }
              */

              Element textCenterX = doc.createElement("textCenterX");
              textCenterX.appendChild(doc.createTextNode(textsCenterX[j]));
              textPerPager[j].appendChild(textCenterX);

              Element textCenterY = doc.createElement("textCenterY");
              textCenterY.appendChild(doc.createTextNode(textsCenterY[j]));
              textPerPager[j].appendChild(textCenterY);


              Element textWidth = doc.createElement("textWidth");
              textWidth.appendChild(doc.createTextNode(textsWidth[j]));
              textPerPager[j].appendChild(textWidth);
              Log.i("WIDTH", String.valueOf(textsWidth[j]));

              Element textHeight = doc.createElement("textHeight");
              textHeight.appendChild(doc.createTextNode(textsHeight[j]));
              textPerPager[j].appendChild(textHeight);
              Log.i("HEIGHT", String.valueOf(textsHeight[j]));


              Element textContent = doc.createElement("textContent");
              textContent.appendChild(doc.createTextNode(textsContent[j]));
              textPerPager[j].appendChild(textContent);
              Log.i("textContent", String.valueOf(textsContent[j]));




              pagerIndex[i].appendChild(textPerPager[j]);

            }
          }

          tempsaveSet.appendChild(pagerIndex[i]);

        }
      }


      // XML 파일로 쓰기
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();

      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource source = new DOMSource(doc);


      File file = new File(MainRegisterCouponShopStep1.TEMPSAVE_PATH_OF_SET);
      if (!file.exists()) { // 원하는 경로에 폴더가 있는지 확인
        file.mkdirs();
      }


      if (XmlFile.exists()) {
        StreamResult result2 = new StreamResult(MainRegisterCouponShopStep1.TEMPSAVE_XML_PATH);
        transformer.transform(source, result2);
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
      } else {
        StreamResult result2 = new StreamResult(new FileOutputStream(new File(MainRegisterCouponShopStep1.TEMPSAVE_XML_PATH)));
        transformer.transform(source, result2);
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
      }

      System.out.println("File saved!");
      //Toast.makeText(selectedImagesActivity.this, "임시 보관소에 저장되었습니다.", Toast.LENGTH_SHORT).show();

    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return true;
  }

  public static class TempSaveSet {
    public String getName() {
      return "foo";
    }

    public Integer getPort() {
      return 12345;
    }
  }
}
