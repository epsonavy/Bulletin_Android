package com.cs175.bulletinandroid.bulletin;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Lucky on 11/22/16.
 */
public class BulletinSingleton {
    public static String API_ADDRESS = "http://54.145.163.155/api/";

    public static String GET_TOKEN = "FETCHTOKEN";

    private static BulletinSingleton ourInstance = new BulletinSingleton();

    public static BulletinSingleton getInstance() {
        return ourInstance;
    }

    private BulletinSingleton() {
        API = new BulletinAPI();
        API.setAPIAddress(API_ADDRESS);
        userResponse = new UserResponse();
        flag = " ";
    }

    private BulletinAPI API;
    private String flag;

    public String itemURL;
    public String getflag() {
        return flag;
    }
    public void setflag(String f) {
        flag = f;
    }
    public BulletinAPI getAPI(){
        return API;
    }

    private Typeface fontRoboto;

    private UserResponse userResponse;

    public UserResponse getUserResponse() {
        return userResponse;
    }
    public Typeface getFont() {
        return fontRoboto;
    }

    public void setFont(Context context) {
        fontRoboto = Typeface.createFromAsset(context.getAssets(), "Fonts/SF-UI-Display-Light.otf");
    }


    public HomePageActivity homePageActivity;


}
