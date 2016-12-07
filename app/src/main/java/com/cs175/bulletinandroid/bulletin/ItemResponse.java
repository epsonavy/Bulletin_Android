package com.cs175.bulletinandroid.bulletin;

/**
 * Created by Lucky on 12/6/16.
 */

public class ItemResponse extends Response{

    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getExpiration() {
        return expiration;
    }

    public void setExpiration(Double expiration) {
        this.expiration = expiration;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }

    private String userId;
    private String title;
    private String description;
    private Double expiration;
    private Double price;
    private String[] pictures;

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    private String userPicture;
    private String userName;

    public ItemResponse(){

    }
}
