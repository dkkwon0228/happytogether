package com.ilovecat.happytogether.MainShopListingCoupon;

/**
 * 설명은 여기서 부터.
 *
 * Created by dannykwon on 2016. 07. 16. happytogether
 */
public class CouponShop {
  private String name;
  private String numOfSongs;
  //private int thumbnail;
  private String thumbnail;


  public CouponShop() {
  }

  //public CouponShop(String name, String numOfSongs, int thumbnail) {
  public CouponShop(String name, String numOfSongs, String thumbnail) {
    this.name = name;
    this.numOfSongs = numOfSongs;
    this.thumbnail = thumbnail;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNumOfSongs() {
    return numOfSongs;
  }

  public void setNumOfSongs(String numOfSongs) {
    this.numOfSongs = numOfSongs;
  }

  //public int getThumbnail() { return thumbnail; }
  public String getThumbnail() { return thumbnail; }

  //public void setThumbnail(int thumbnail) { this.thumbnail = thumbnail; }
  public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

}