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
            URL imageUrl = new URL(viewHolder.url);
            viewHolder.bitmap = BitmapFactory.decodeStream(imageUrl.openStream());
            viewHolder.bitmap = Bitmap.createScaledBitmap(viewHolder.bitmap, 300, 250, true);
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


    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

}
