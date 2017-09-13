package com.ilovecat.happytogether.MainShopListingCoupon;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 16. happytogether
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ilovecat.happytogether.MainShopRegisterCoupon.MainRegisterCouponShopStep1;
import com.ilovecat.happytogether.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class CouponShopAdapter extends RecyclerView.Adapter<CouponShopAdapter.MyViewHolder> {

  private Context mContext;
  private List<CouponShop> albumList;

  static String TEMPSAVE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/HappyTogether/TempSave/";


  /*
  * For Parsing XML
  */
  String arrId;
  String id = null;
  String totalPagerNum;
  String stikerTotalNum;
  String textTotalNum;


  ArrayList<String> arrImageUrl;
  ArrayList<String> arrStikerTotalNum;
  ArrayList<String> arrTextTotalNum;

  ArrayList<String[]> arrStikerCenterX;
  ArrayList<String[]> arrStikerCenterY;
  ArrayList<String[]> arrStikerWidth;
  ArrayList<String[]> arrStikerHeight;
  ArrayList<String[]> arrStikerDrawable;


  ArrayList<String[]> arrTextCenterX;
  ArrayList<String[]> arrTextCenterY;
  ArrayList<String[]> arrTextWidth;
  ArrayList<String[]> arrTextHeight;
  ArrayList<String[]> arrTextContent;

  static String photo_path_tempsaveXMLInAdapter = TEMPSAVE_PATH + "tempsave.xml";
  static File XmlFileInAdapter = new File(photo_path_tempsaveXMLInAdapter);
  static Document docRoot;


  public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title, count;
    public ImageView thumbnail, overflow;
    public String tempsaveSetId;

    public MyViewHolder(View view) {
      super(view);
      title = (TextView) view.findViewById(R.id.title);
      count = (TextView) view.findViewById(R.id.count);
      thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
      overflow = (ImageView) view.findViewById(R.id.overflow);
    }
  }


  public CouponShopAdapter(Context mContext, List<CouponShop> albumList) {
    this.mContext = mContext;
    this.albumList = albumList;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.main_inner_shop_coupon_listing_card, parent, false);

    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(final MyViewHolder holder, int position) {
    CouponShop album = albumList.get(position);
    holder.title.setText(album.getName());
    holder.count.setText(album.getNumOfSongs());
    holder.tempsaveSetId = album.getThumbnail();

    // loading album cover using Glide library
    //Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);


    if (holder.tempsaveSetId.substring(0, 1).equals("c")) {
      Glide.with(mContext).load("https://s3-ap-northeast-1.amazonaws.com/happytogether2-s3-images/" + holder.tempsaveSetId).into(holder.thumbnail);
    } else {
      Glide.with(mContext).load(TEMPSAVE_PATH + holder.tempsaveSetId + "/" + "captured_" + holder.tempsaveSetId + ".jpg").into(holder.thumbnail);
    }

    holder.overflow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showPopupMenu(holder.overflow, holder.tempsaveSetId);
      }
    });


    holder.thumbnail.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(mContext, holder.title.getText(), Toast.LENGTH_SHORT).show();


        if (holder.tempsaveSetId.substring(0, 1).equals("c")) {


        } else {

          new ParseTempSaveXMLAsyncTask().execute(holder.tempsaveSetId);
          Log.i("tempsaveSetId", holder.tempsaveSetId);
        }

      }
    });
  }

  /**
   * Showing popup menu when tapping on 3 dots
   */
  private void showPopupMenu(View view, String tsi) {
    // inflate menu
    PopupMenu popup = new PopupMenu(mContext, view);
    MenuInflater inflater = popup.getMenuInflater();
    inflater.inflate(R.menu.menu_album, popup.getMenu());
    popup.setOnMenuItemClickListener(new MyMenuItemClickListener(tsi));
    popup.show();
  }

  /**
   * Click listener for popup menu items
   */
  class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

    String tsi;

    public MyMenuItemClickListener(String tsi) {

    this.tsi = tsi;

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
      switch (menuItem.getItemId()) {
        case R.id.action_open:
          Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
          return true;
        case R.id.action_remove:

          Toast.makeText(mContext, this.tsi, Toast.LENGTH_SHORT).show();

          DialogTempSaveDel(this.tsi);

          return true;
        default:
      }
      return false;
    }
  }

  @Override
  public int getItemCount() {
    return albumList.size();
  }


  /*
  File 	: execute, doInBackground 의 파라미터 타입
  Integer	: onProgressUpdate 의 파라미터 타입
  Int	: doInBackground 의 리턴값, onPostExecute 의 파라미터로 설정됩니다.
  */
  public class ParseTempSaveXMLAsyncTask extends AsyncTask<String, Integer, Integer> {

    ProgressDialog dialog;

    @Override
    protected void onCancelled() {
      super.onCancelled();
    }


    @Override
    protected void onPreExecute() {
      //btn.setText("Thread START!!!!");
      super.onPreExecute();

      //dialog = ProgressDialog.show(SwipeListViewActivity.this, "",
      //   		"로딩중입니다! 잠시 기다려주세요", true);

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      //pb.setProgress(values[0]);
      super.onProgressUpdate(values);
    }

    @Override
    protected Integer doInBackground(String... params) {
      int result = 0;
      //int n = params[0];

      if (PARSETEMPSAVEXML(params[0])) {
        result = 1;
      }
      return result;

    }

    @Override
    protected void onPostExecute(Integer result) {
      //btn.setText("Thread END");
      super.onPostExecute(result);
      if (result == 1) {
        Log.i("copy", "SUCESS");


        final Intent intent = new Intent(mContext, MainRegisterCouponShopStep1.class);
        //Bundle bundle = new Bundle();
        //intent.putExtra("dataListForFilter", selectedDataList);
        // intent.putExtra("dataList", copyDataList);
        // intent.putExtra("dataList_thum", copyDataList_thum);
        // intent.putExtra("dataList_original", arrOriginalImageUrl);

        // intent.putExtra("keyAdminId", ADMINID);
        // intent.putExtra("keyAdminCount", ADMINCOUNT);
        // intent.putExtra("keyShopName", ADMIN_SHOPNAME);



        Log.i("xxxxx", String.valueOf(arrStikerCenterX.get(0)[0]));
        Log.i("yyyyy", String.valueOf(arrStikerCenterY.get(0)[0]));

        intent.putExtra("keyId", id);
        intent.putExtra("keyTotalPagerNum", totalPagerNum);
        intent.putExtra("keyImgUrl", arrImageUrl);
        intent.putExtra("keyStikerTotalNum", arrStikerTotalNum);
        intent.putExtra("keyStikerCenterX", arrStikerCenterX);
        intent.putExtra("keyStikerCenterY", arrStikerCenterY);
        intent.putExtra("keyStikerWidth", arrStikerWidth);
        intent.putExtra("keyStikerHeight", arrStikerHeight);
        intent.putExtra("keyStikerDrawable", arrStikerDrawable);

        intent.putExtra("keyTextTotalNum", arrTextTotalNum);
        intent.putExtra("keyTextCenterX", arrTextCenterX);
        intent.putExtra("keyTextCenterY", arrTextCenterY);
        intent.putExtra("keyTextWidth", arrTextWidth);
        intent.putExtra("keyTextHeight", arrTextHeight);
        intent.putExtra("keyTextContent", arrTextContent);

        intent.putExtra("keyWhere", "TEMPSAVE");
        //setResult(RESULT_OK, intent);
        mContext.startActivity(intent);
      }
    }

  }


  boolean PARSETEMPSAVEXML(String tempsaveSetId) {
  /*
  * Dom XML 파싱
  * TempSave.xml
  */

    try {

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = factory.newDocumentBuilder();
      //InputStream istream = new ByteArrayInputStream(xml.getBytes("utf-8"));

      String photo_path_tempsaveXML = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/HappyTogether/TempSave/tempsave.xml";
      File XmlFile = new File(photo_path_tempsaveXML);

      Document docRoot = docBuilder.parse(XmlFile);

      //루트 엘리먼트 접근
      Element tempsave = docRoot.getDocumentElement();

      //하위 엘리먼트 접근
      NodeList nodeList = tempsave.getElementsByTagName("tempsaveSet");
      Log.i("nodeList", String.valueOf(nodeList));
      int totalTempSaveSetNum = nodeList.getLength();
      Log.i("tempsaveSet 전체갯수", String.valueOf(totalTempSaveSetNum));


      int selectedNodeIndex = 0;

      /*
       * 선택된 노드가 몇번째인지 구한다.
       * selectedNodeIndex = i;
       */
      for (int i = 0; i < totalTempSaveSetNum; i++) {
        Node tempsaveSetNode = nodeList.item(i);
        Element tempsaveSetElement = (Element) tempsaveSetNode;
        arrId = tempsaveSetElement.getAttribute("id");
        //Log.i("tempsaveSet속성Id",arrId);
        if (tempsaveSetId.equals(arrId)) {
          //Log.i("tempsaveSetElement",String.valueOf(tempsaveSetElement));
          //Log.i("i is",String.valueOf(i));
          //xmlArray.add(arrId);
          selectedNodeIndex = i;
        }
      }

      /*
       * selectedNodeIndex 번째 노드만 파싱
       */
      Node tempsaveSetNode = nodeList.item(selectedNodeIndex);
      Element tempsaveSetElement = (Element) tempsaveSetNode;
      arrId = tempsaveSetElement.getAttribute("id");
      Log.i("tempsaveSet Id", arrId);

      id = tempsaveSetElement.getElementsByTagName("id").item(0).getTextContent();
      Log.i("tempsaveSet ID", id);


      totalPagerNum = tempsaveSetElement.getElementsByTagName("totalPagerNum").item(0).getTextContent();
      Log.i("totalPagerNum", totalPagerNum);

      int intTotalPagerNum = Integer.parseInt(totalPagerNum);

      for (int j = 0; j < intTotalPagerNum; j++) {
        NodeList pagerNodeList = tempsaveSetElement.getElementsByTagName("Pager");
        Node pagerNode = pagerNodeList.item(j); //item(i)
        Element pagerNodeElement = (Element) pagerNode;
        arrImageUrl = new ArrayList<String>();
        arrImageUrl.add(pagerNodeElement.getElementsByTagName("BackGroundFilePathAndName").item(0).getTextContent());
        Log.i("imageUrl", String.valueOf(arrImageUrl));

        arrStikerTotalNum = new ArrayList<String>();
        arrStikerTotalNum.add(pagerNodeElement.getElementsByTagName("stikerTotalNum").item(0).getTextContent());
        Log.i("stikerTotalNum", String.valueOf(arrStikerTotalNum));
        stikerTotalNum = pagerNodeElement.getElementsByTagName("stikerTotalNum").item(0).getTextContent();

        arrTextTotalNum = new ArrayList<String>();
        arrTextTotalNum.add(pagerNodeElement.getElementsByTagName("textTotalNum").item(0).getTextContent());
        Log.i("textTotalNum", String.valueOf(arrTextTotalNum));
        textTotalNum = pagerNodeElement.getElementsByTagName("textTotalNum").item(0).getTextContent();


        int intStikerTotalNum = Integer.parseInt(stikerTotalNum);
        String[] stikersCenterX = new String[intStikerTotalNum];
        String[] stikersCenterY = new String[intStikerTotalNum];
        String[] stikersWidth = new String[intStikerTotalNum];
        String[] stikersHeight = new String[intStikerTotalNum];
        String[] stikersDrawable = new String[intStikerTotalNum];

        for (int s = 0; s < intStikerTotalNum; s++) {
          NodeList stikerNodeList = pagerNodeElement.getElementsByTagName("stikerPerPager");
          Node stikerNode = stikerNodeList.item(s); //item(j)
          Element stikerNodeElement = (Element) stikerNode;

          stikersCenterX[s] = stikerNodeElement.getElementsByTagName("stikerCenterX").item(0).getTextContent();
          stikersCenterY[s] = stikerNodeElement.getElementsByTagName("stikerCenterY").item(0).getTextContent();
          stikersWidth[s] = stikerNodeElement.getElementsByTagName("stikerWidth").item(0).getTextContent();
          stikersHeight[s] = stikerNodeElement.getElementsByTagName("stikerHeight").item(0).getTextContent();
          stikersDrawable[s] = stikerNodeElement.getElementsByTagName("stikerDrawable").item(0).getTextContent();

        }
        arrStikerCenterX = new ArrayList<String[]>();
        arrStikerCenterX.add(stikersCenterX);

        arrStikerCenterY = new ArrayList<String[]>();
        arrStikerCenterY.add(stikersCenterY);

        arrStikerWidth = new ArrayList<String[]>();
        arrStikerWidth.add(stikersWidth);

        arrStikerHeight = new ArrayList<String[]>();
        arrStikerHeight.add(stikersHeight);

        arrStikerDrawable = new ArrayList<String[]>();
        arrStikerDrawable.add(stikersDrawable);


        int intTextTotalNum = Integer.parseInt(textTotalNum);
        String[] textsCenterX = new String[intTextTotalNum];
        String[] textsCenterY = new String[intTextTotalNum];
        String[] textsWidth = new String[intTextTotalNum];
        String[] textsHeight = new String[intTextTotalNum];
        String[] textsContent = new String[intTextTotalNum];

        for (int t = 0; t < intTextTotalNum; t++) {
          NodeList textNodeList = pagerNodeElement.getElementsByTagName("textPerPager");
          Node textNode = textNodeList.item(t); //item(j)
          Element textNodeElement = (Element) textNode;

          textsCenterX[t] = textNodeElement.getElementsByTagName("textCenterX").item(0).getTextContent();
          textsCenterY[t] = textNodeElement.getElementsByTagName("textCenterY").item(0).getTextContent();
          textsWidth[t] = textNodeElement.getElementsByTagName("textWidth").item(0).getTextContent();
          textsHeight[t] = textNodeElement.getElementsByTagName("textHeight").item(0).getTextContent();
          textsContent[t] = textNodeElement.getElementsByTagName("textContent").item(0).getTextContent();

        }

        arrTextCenterX = new ArrayList<String[]>();
        arrTextCenterX.add(textsCenterX);

        arrTextCenterY = new ArrayList<String[]>();
        arrTextCenterY.add(textsCenterY);

        arrTextWidth = new ArrayList<String[]>();
        arrTextWidth.add(textsWidth);

        arrTextHeight = new ArrayList<String[]>();
        arrTextHeight.add(textsHeight);

        arrTextContent = new ArrayList<String[]>();
        arrTextContent.add(textsContent);

      }

    } catch (Exception e) {
    }


    return true;
  }

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



  private void DialogTempSaveDel(final String tsi){
    AlertDialog.Builder alt_bld = new AlertDialog.Builder(mContext);
    alt_bld.setMessage(tsi + "삭제하시겠습니까?").setCancelable(
        false).setPositiveButton("예",
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {


            /*
            ShopItem ITEMINROW = getItemCount(position);
            ITEMINROW = shopitems.get(position);

            String tempsaveId = ITEMINROW.getId();
            String fileToDelete = TempSave_path + tempsaveId;

            DeleteDir(fileToDelete);

            delXmlId(tempsaveId);

            try {
              saveXmlFile(docRoot);
            } catch (Exception e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }

            ((SwipeListViewActivity)SwipeListViewActivity.mytaskContext).onResume();
            com.appmaker.gcmtest.tempsave.SwipeListViewActivity.swipeListView.invalidate();
            */


            String fileToDelete = TEMPSAVE_PATH + tsi;
            DeleteDir(fileToDelete);
            delXmlId(tsi);

            try {
              saveXmlFile(docRoot);
            } catch (Exception e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }

            //((MainCouponShopFragment)MainCouponShopFragment.thiscontext).onResume();
            //com.ilovecat.happytogether.MainShopListingCoupon.MainCouponShopFragment.recyclerView_tempsave.invalidate();
            //com.ilovecat.happytogether.MainShopListingCoupon.MainCouponShopFragment.couponShopAdapter_tempsave.notifyDataSetChanged();



          }
        }).setNegativeButton("아니요",
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            // Action for 'NO' Button
            dialog.cancel();
          }
        });
    AlertDialog alert = alt_bld.create();
    // Title for AlertDialog
    //alert.setTitle("Title");
    // Icon for AlertDialog
    //alert.setIcon(R.drawable.icon);
    alert.show();
  }

  private void DeleteDir(String path)
  {
    File file = new File(path);
    File[] childFileList = file.listFiles();
    for(File childFile : childFileList)
    {
      if(childFile.isDirectory()) {
        DeleteDir(childFile.getAbsolutePath());     //하위 디렉토리 루프
      }
      else {
        childFile.delete();    //하위 파일삭제
      }
    }
    file.delete();    //root 삭제
  }

  private void delXmlId(String id) {
    try {

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = factory.newDocumentBuilder();
      //InputStream istream = new ByteArrayInputStream(xml.getBytes("utf-8"));

      String photo_path_tempsaveXML = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/HappyTogether/TempSave/tempsave.xml";
      File XmlFile = new File(photo_path_tempsaveXML);

      docRoot = docBuilder.parse(XmlFile);

      //루트 엘리먼트 접근
      Element tempsave = docRoot.getDocumentElement();

      //하위 엘리먼트 접근
      NodeList nodeList = tempsave.getElementsByTagName("tempsaveSet");
      Log.i("nodeList",String.valueOf(nodeList));
      int totalTempSaveSetNum = nodeList.getLength();
      Log.i("tempsaveSet전체갯수",String.valueOf(totalTempSaveSetNum));

      String selectedId = id;
      Log.i("id is",id);


      Node tempsaveSetToDel = null;

      for(int i =0; i < totalTempSaveSetNum; i++) {
        Node tempsaveSetNode = nodeList.item(i);
        Element tempsaveSetElement = (Element)tempsaveSetNode;
        String arrId = tempsaveSetElement.getAttribute("id");
        //Log.i("tempsaveSet속성Id",arrId);
        if(arrId.equals(selectedId)) {
          tempsaveSetToDel = tempsaveSetNode;
          Log.i("tempsaveSetNode is",String.valueOf(tempsaveSetNode));
        }
      }

      Log.i("tempsaveSetToDe is",String.valueOf(tempsaveSetToDel));
      tempsaveSetToDel.getParentNode().removeChild(tempsaveSetToDel);
      docRoot.normalize();

    } catch (Exception e){  }

  }

  public static final void saveXmlFile(Document xml) throws Exception {

    // XML 파일로 쓰기
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();

    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    DOMSource source = new DOMSource(docRoot);
    File file = new File(TEMPSAVE_PATH);
    if( !file.exists() ) { // 원하는 경로에 폴더가 있는지 확인
      file.mkdirs();
    }

    XmlFileInAdapter.delete();

    if(XmlFileInAdapter.exists()==true) {
      StreamResult result2 = new StreamResult(photo_path_tempsaveXMLInAdapter);
      transformer.transform(source, result2);
    } else {
      StreamResult result2 = new StreamResult(new FileOutputStream(new File(photo_path_tempsaveXMLInAdapter)));
      transformer.transform(source, result2);
    }

    System.out.println("File saved!");
  }

}
