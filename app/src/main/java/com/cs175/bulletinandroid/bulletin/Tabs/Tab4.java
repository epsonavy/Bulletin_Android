package com.cs175.bulletinandroid.bulletin.Tabs;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cs175.bulletinandroid.bulletin.BulletinSingleton;
import com.cs175.bulletinandroid.bulletin.ItemResponse;
import com.cs175.bulletinandroid.bulletin.OnRequestListener;
import com.cs175.bulletinandroid.bulletin.R;
import com.cs175.bulletinandroid.bulletin.Response;

/**
 * Modified by Pei Liu on 12/6/16.
 */

public class Tab4 extends Fragment implements OnRequestListener, AdapterView.OnItemClickListener {

    private BulletinSingleton singleton = BulletinSingleton.getInstance();

    private Typeface font;

    private TextView headerTextView;
    private TextView existNoItemTextView;

    private ListView itemListView;

    private boolean processingItemRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab4, container, false);

        headerTextView = (TextView) view.findViewById(R.id.myListingTitleTextView);
        existNoItemTextView = (TextView) view.findViewById(R.id.myListingSubTitleTextView);
        itemListView = (ListView) view.findViewById(R.id.itemListView);
        itemListView.setOnItemClickListener(this);

        font = Typeface.createFromAsset(getActivity().getAssets(), "Fonts/SF-UI-Display-Light.otf");

        changeFont(headerTextView);
        changeFont(existNoItemTextView);

        refreshItems();


        return view;
    }

    public void changeFont(TextView text){
        text.setTypeface(font);
    }

    public void refreshItems(){
        if(processingItemRefresh == false){
            processingItemRefresh = true;
            singleton.getAPI().getMyItems(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent viewItemIntent = new Intent(getActivity(), EditItemActivity.class);

        MyListingItemAdapter adapter = (MyListingItemAdapter) adapterView.getAdapter();
        ItemResponse item = (ItemResponse) adapter.getItem(i);

        viewItemIntent.putExtra("description", item.getDescription());
        viewItemIntent.putExtra("title", item.getTitle());
        viewItemIntent.putExtra("itemPicture", item.getPictures()[0]);
        viewItemIntent.putExtra("itemPrice", ""+item.getPrice());
        viewItemIntent.putExtra("itemId", item.get_id());
        startActivity(viewItemIntent);
    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {

    }

    @Override
    public void onResponsesReceived(RequestType type, int resCode, Response[] response) {
        if(type == RequestType.GetItems){

            if (resCode == 200){
                existNoItemTextView.setText("Your items are below.");
                final MyListingItemAdapter adapter = new MyListingItemAdapter(getContext(), (ItemResponse[]) response);
                getActivity().runOnUiThread(new Runnable(){
                    public void run(){
                        itemListView.setAdapter(adapter);
                    }
                });

            }
        }
    }
}
