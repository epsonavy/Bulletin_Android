package com.cs175.bulletinandroid.bulletin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * Created by Lucky on 12/8/16.
 */

public class ImageCache {

    public static String externalStoragePath = Environment.getExternalStorageDirectory().toString();


    public static boolean doesImageExist(String url){
        return new File(externalStoragePath, removeStuff(url)).exists();

    }

    public static String removeStuff(String url){
        return url.replace(":","").replace("/", "");
    }
    public static Bitmap getImage(String url){
        File f = new File(externalStoragePath, url);
        return BitmapFactory.decodeFile(externalStoragePath+"/"+removeStuff(url));
    }
    public static void saveImage(String url, Bitmap bitmap){
        try {
            Log.d("Bulletin", removeStuff(url));
            FileOutputStream fileOut = new FileOutputStream(new File(externalStoragePath, removeStuff(url)));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
            fileOut.flush();
            fileOut.close();
        }catch(Exception e){
            Log.d("Bulletin", e.getMessage());
        }
    }
}
