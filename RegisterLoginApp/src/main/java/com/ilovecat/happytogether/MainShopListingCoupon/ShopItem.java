package com.ilovecat.happytogether.MainShopListingCoupon;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 08. 13. happytogether
 */
public class ShopItem  {
  public String tempSaveId;
  public String tempSaveTotalPagerNum;

  public String getId() {
    return tempSaveId;
  }

  public void setId(String id) {
    this.tempSaveId = id;
  }

  public String getTotalPagerNum() {
    return tempSaveTotalPagerNum;
  }

  public String setTotalPagerNum(String totalPagerNum) {
    return tempSaveTotalPagerNum = totalPagerNum;
  }

}
