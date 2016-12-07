package com.cs175.bulletinandroid.bulletin;

/**
 * Created by Lucky on 12/6/16.
 */

public class HomeItem {

    private String title;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public Double getExpiration() {
        return expiration;
    }

    public void setExpiration(Double expiration) {
        this.expiration = expiration;
    }

    private Double price;
    private String userImage;
    private String itemImage;
    private Double expiration;

    public HomeItem(String title, String description, Double price, String userImage, String itemImage, Double expiration){
        this.title = title;
        this.description = description;
        this.price = price;
        this.userImage = userImage;
        this.itemImage = itemImage;
        this.expiration = expiration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
