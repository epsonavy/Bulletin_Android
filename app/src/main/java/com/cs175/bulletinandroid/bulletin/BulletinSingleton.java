package com.cs175.bulletinandroid.bulletin;

/**
 * Created by Lucky on 11/22/16.
 */
public class BulletinSingleton {
    public static String API_ADDRESS = "http://54.145.163.155/api/";


    private static BulletinSingleton ourInstance = new BulletinSingleton();

    public static BulletinSingleton getInstance() {
        return ourInstance;
    }

    private BulletinSingleton() {
        API = new BulletinAPI();
        API.setAPIAddress(API_ADDRESS);
    }

    private BulletinAPI API;

    public BulletinAPI getAPI(){
        return API;
    }
}
