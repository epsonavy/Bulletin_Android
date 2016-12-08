package com.cs175.bulletinandroid.bulletin.Tabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.cs175.bulletinandroid.bulletin.ImageCache;

import java.net.URL;

/**
 * Created by chenyulong on 12/8/16.
 */

public class DownloadAsyncTaskProfilePicture extends AsyncTask<HomeItemAdapter.ViewHolder, Void, HomeItemAdapter.ViewHolder> {
    protected HomeItemAdapter.ViewHolder doInBackground(HomeItemAdapter.ViewHolder... params){
        HomeItemAdapter.ViewHolder viewHolder = params[0];
        try{
            if (ImageCache.doesImageExist(viewHolder.url)){
                Log.d("Bulletin", "Found it?");
                viewHolder.bitmap = Bitmap.createScaledBitmap(ImageCache.getImage(viewHolder.url), 1300, 1300, true);

            }else {
                URL imageUrl = new URL(viewHolder.url);
                viewHolder.bitmap = BitmapFactory.decodeStream(imageUrl.openStream());
                viewHolder.bitmap = Bitmap.createScaledBitmap(viewHolder.bitmap, 1300, 1300, true);
                ImageCache.saveImage(viewHolder.url, viewHolder.bitmap);
            }
        }catch(Exception e){

            viewHolder.bitmap = null;
        }
        return viewHolder;
    }
    @Override
    protected void onPostExecute(HomeItemAdapter.ViewHolder result){
        super.onPostExecute(result);
        Log.d("MEOW", "DONE EXECUTION");
        if(result.bitmap != null){
            result.imageView.setImageBitmap(result.bitmap);
        }else{

        }
    }



}

