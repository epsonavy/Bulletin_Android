package com.cs175.bulletinandroid.bulletin.Tabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs175.bulletinandroid.bulletin.ItemResponse;
import com.cs175.bulletinandroid.bulletin.R;

import com.cs175.bulletinandroid.bulletin.HomeItem;

import java.util.ArrayList;

/**
 * Created by Lucky on 12/6/16.
 */

public class HomeItemAdapter extends BaseAdapter {

    private Typeface font;

    public void changeFont(TextView text){
        text.setTypeface(font);
    }

    public static class ViewHolder {
        ImageView imageView;
        Bitmap bitmap;
        String url;
    }

    private Context context;
    private ItemResponse[] data;

    private LayoutInflater inflater = null;

    public HomeItemAdapter(Context context, ItemResponse[] data){
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        font = Typeface.createFromAsset(context.getAssets(), "Fonts/SF-UI-Display-Light.otf");

    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View v = convertView;
        if(v == null){
            ItemResponse item = data[i];
            v = inflater.inflate(R.layout.listview_home, null);
            TextView titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            TextView priceTextView = (TextView) v.findViewById(R.id.priceTextView);
            TextView descriptionTextView = (TextView) v.findViewById(R.id.descriptionTextView);
            ImageView itemImageView = (ImageView) v.findViewById(R.id.itemImageView);
            ImageView userImageView = (ImageView) v.findViewById(R.id.userImageView);

            titleTextView.setText(item.getTitle());
            descriptionTextView.setText(item.getDescription());
            priceTextView.setText("$"+Double.toString(item.getPrice()));

            ViewHolder itemImageHolder = new ViewHolder();
            ViewHolder userImageHolder = new ViewHolder();
            if(item.getPictures() != null){
                if(item.getPictures().length > 0){
                    String itemPicture = item.getPictures()[0];
                    itemImageHolder.url = itemPicture;
                }
            }
            if(item.getUserPicture() != null){
                userImageHolder.url = item.getUserPicture();
            }

            itemImageHolder.imageView = itemImageView;
            userImageHolder.imageView = userImageView;
            itemImageView.setTag(itemImageHolder);
            userImageView.setTag(userImageHolder);

            changeFont(titleTextView);
            changeFont(priceTextView);
            changeFont(descriptionTextView);


            new DownloadAsyncTask().execute(itemImageHolder);
            new DownloadSmallerItemsAsyncTask().execute(userImageHolder);

        }




        return v;
    }
}


