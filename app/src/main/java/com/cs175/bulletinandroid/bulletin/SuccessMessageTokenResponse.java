package com.cs175.bulletinandroid.bulletin;

/**
 * Created by chenyulong on 11/24/16.
 */
public class SuccessMessageTokenResponse extends SuccessMessageResponse {
    private String token;
    public void setToken(String token){
        this.token = token;
    }
    public String getToken(){
        return token;
    }
}
