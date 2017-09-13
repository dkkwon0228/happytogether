package com.ilovecat.happytogether.MainShopListingCoupon;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 08. 13. happytogether
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class ShopNamesParser {

  ShopItem objItem;
  List<ShopItem> listArray;

  /*
  public List<ShopItem> getData(String url,int lastCountOfList, int MAX_COUNT_PER_PAGE) {

          try {

                  listArray = new ArrayList<ShopItem>();

                  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                  DocumentBuilder db = dbf.newDocumentBuilder();
                  Document doc = db.parse(new URL(url).openStream());

                  doc.getDocumentElement().normalize();

                  NodeList nList = doc.getElementsByTagName("item");

                  for (int temp = lastCountOfList; temp < MAX_COUNT_PER_PAGE; temp++) {

                          Node nNode = nList.item(temp);
                          if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                                  Element eElement = (Element) nNode;

                                  objItem = new ShopItem();

                                  objItem.setId(getTagValue("id", eElement));
                                  objItem.setWriterId(getTagValue("writerid", eElement));
                                  objItem.setShopTitle(getTagValue("shoptitle", eElement));
                                  objItem.setShopDesc(getTagValue("shopdesc", eElement));
                                  objItem.setLink(getTagValue("link", eElement));
                                  objItem.setLink_thum(getTagValue("linkthum", eElement));
                                  objItem.setShopPubdate(getTagValue("pubDate", eElement));
                                  objItem.setShopPubdate2(getTagValue("pubDate2", eElement));
                                  listArray.add(objItem);
                          }
                  }

          } catch (Exception e) {
                  e.printStackTrace();
          }

          return listArray;
  }
  */
  public List<ShopItem> getDataAll(String xmlPath) {


		/*
		String requestURL = xmlPath;
		URL Url = null;
		try {
			Url = new URL(requestURL);
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		InputStream is = null;

		try {
			is = Url.openStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/

    FileInputStream fis = null;
    File file = new File(xmlPath);

    try {
      XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      factory.setNamespaceAware(true);
      XmlPullParser parser = factory.newPullParser();

      fis = new FileInputStream(file);

      parser.setInput(fis, "UTF-8");
      //int eventType = parser.getEventType();
      return listArray(parser);

    } catch (XmlPullParserException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      try {
        fis.close(); //inputstream.
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    return null;

    /*
    try {
      listArray = new ArrayList<ShopItem>();

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(new URL(url).openStream());

      doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("item");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					objItem = new ShopItem();

					objItem.setId(getTagValue("id", eElement));
					objItem.setWriterId(getTagValue("writerid", eElement));
					objItem.setShopTitle(getTagValue("shoptitle", eElement));
					objItem.setShopDesc(getTagValue("shopdesc", eElement));
					objItem.setLink(getTagValue("link", eElement));
					objItem.setLink_thum(getTagValue("linkthum", eElement));
					objItem.setShopPubdate(getTagValue("pubDate", eElement));
					objItem.setShopPubdate2(getTagValue("pubDate2", eElement));
					listArray.add(objItem);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listArray;

	}
	*/
  }


  List<ShopItem> listArray(XmlPullParser parser) throws XmlPullParserException, IOException {
    // TODO Auto-generated method stub
    List<ShopItem> parserShop = new ArrayList<ShopItem>();
    String tag = null;
    int parserEvent = parser.getEventType();
    int tagIdentifier = 0;

    while(parserEvent != XmlPullParser.END_DOCUMENT) {

      switch(parserEvent){
        case XmlPullParser.START_DOCUMENT:
          break;
        case XmlPullParser.END_DOCUMENT:
          break;
        case XmlPullParser.END_TAG:
          break;
        case XmlPullParser.START_TAG:

          tag = parser.getName();

          if(tag.equals("id")){
            tagIdentifier = 1;
          }else if(tag.equals("totalPagerNum")){
            tagIdentifier = 2;
          }

          break;

        case XmlPullParser.TEXT:

          if (tagIdentifier == 1) {
            objItem = new ShopItem();
            objItem.tempSaveId = parser.getText().trim();
          } else if(tagIdentifier == 2) {  objItem.tempSaveTotalPagerNum = parser.getText().trim();

            parserShop.add(objItem);
          }
          tagIdentifier = 0;
          break;
      }
      parserEvent = parser.next();
    }

    return parserShop;
  }


  /*
  private static String getTagValue(String sTag, Element eElement) {
  NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();

  Node nValue = (Node) nlList.item(0);
  return nValue.getNodeValue();
  }
  */
}
