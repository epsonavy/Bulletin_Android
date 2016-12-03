package com.cs175.bulletinandroid.bulletin;

/**
 * Created by Lucky on 11/22/16.
 */

public abstract class Response {
    public Response(){

    }
    public int responseCode;
    public void setResponseCode(int responseCode){
        this.responseCode = responseCode;
    }
    public int getResponseCode(){
        return this.responseCode;
    }
}
