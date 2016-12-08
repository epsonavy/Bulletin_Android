package com.cs175.bulletinandroid.bulletin.Tabs;

/**
 * Modified by Pei Liu on 12/6/16.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.cs175.bulletinandroid.bulletin.ImageCache;

import java.net.URL;


public class DownloadItemsAsyncTask extends AsyncTask<MyListingItemAdapter.ViewHolder, Void, MyListingItemAdapter.ViewHolder> {
    protected MyListingItemAdapter.ViewHolder doInBackground(MyListingItemAdapter.ViewHolder... params){
        MyListingItemAdapter.ViewHolder viewHolder = params[0];
        try{
            if (ImageCache.doesImageExist(viewHolder.url)){
                Log.d("Bulletin", "Found it?");
                viewHolder.bitmap = ImageCache.getImage(viewHolder.url);
            }else {
                URL imageUrl = new URL(viewHolder.url);
                viewHolder.bitmap = BitmapFactory.decodeStream(imageUrl.openStream());
                ImageCache.saveImage(viewHolder.url, viewHolder.bitmap);
            }
        }catch(Exception e){
            viewHolder.bitmap = null;
        }
        return viewHolder;
    }
    @Override
    protected void onPostExecute(MyListingItemAdapter.ViewHolder result){
        super.onPostExecute(result);
        Log.d("MEOW", "DONE EXECUTION");
        if(result.bitmap != null){
            result.imageView.setImageBitmap(result.bitmap);
        }else{

        }
    }

}
