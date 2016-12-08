package com.cs175.bulletinandroid.bulletin;

/**
 * Created by Lucky on 12/7/16.
 */

public class UploadResponse extends Response{
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String url;
    private String userId;
}
