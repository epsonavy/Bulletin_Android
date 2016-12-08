package com.cs175.bulletinandroid.bulletin.Tabs;

/**
 * Created by Lucky on 12/6/16.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cs175.bulletinandroid.bulletin.ImageCache;
import com.cs175.bulletinandroid.bulletin.Tabs.HomeItemAdapter;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Lucky on 10/8/2016.
 */

public class DownloadAsyncTask extends AsyncTask<HomeItemAdapter.ViewHolder, Void, HomeItemAdapter.ViewHolder> {
    protected HomeItemAdapter.ViewHolder doInBackground(HomeItemAdapter.ViewHolder... params){
        HomeItemAdapter.ViewHolder viewHolder = params[0];
        try{
            if (ImageCache.doesImageExist(viewHolder.url)){
                Log.d("Bulletin", "Found it?");
                viewHolder.bitmap = ImageCache.getImage(viewHolder.url);
                //viewHolder.bitmap = Bitmap.createScaledBitmap(ImageCache.getImage(viewHolder.url), 500, 400, true);
            }else {
                URL imageUrl = new URL(viewHolder.url);
                viewHolder.bitmap = BitmapFactory.decodeStream(imageUrl.openStream());
                //viewHolder.bitmap = Bitmap.createScaledBitmap(viewHolder.bitmap, 500, 400, true);
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
