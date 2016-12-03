package com.cs175.bulletinandroid.bulletin;

/**
 * Created by Lucky on 11/22/16.
 */

public interface OnRequestListener{
    public enum RequestType{
        CheckEmail,
        Login,
        Register,
        CheckDisplayName
    }
    public void onResponseReceived(RequestType type, Response response);
}
