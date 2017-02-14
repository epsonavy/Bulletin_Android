package com.cs175.bulletinandroid.bulletin.Tabs;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.cs175.bulletinandroid.bulletin.BulletinSingleton;
import com.cs175.bulletinandroid.bulletin.HomeItem;
import com.cs175.bulletinandroid.bulletin.ItemResponse;
import com.cs175.bulletinandroid.bulletin.OnRequestListener;
import com.cs175.bulletinandroid.bulletin.R;
import com.cs175.bulletinandroid.bulletin.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * Created by chenyulong on 12/4/16.
 */
public class Tab1 extends Fragment implements OnRequestListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{

    private BulletinSingleton singleton = BulletinSingleton.getInstance();

    private Typeface font;

    private TextView mainHeaderTextView;
    private TextView universityTextView;
    private EditText searchEditText;
    private ListView contentListView;

    private SwipeRefreshLayout swipeRefresh;

    private boolean processingItemRefresh;


    public void changeFont(TextView text){
        text.setTypeface(font);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab1, container, false);

        mainHeaderTextView = (TextView) view.findViewById(R.id.mainHeaderTextView);
        universityTextView = (TextView) view.findViewById(R.id.universityTextView);
        searchEditText = (EditText) view.findViewById(R.id.searchEditText);
        contentListView = (ListView) view.findViewById(R.id.contentListView);

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.scrollControl);

        swipeRefresh.setOnRefreshListener(this);

        font = Typeface.createFromAsset(getActivity().getAssets(), "Fonts/SF-UI-Display-Light.otf");

        changeFont(mainHeaderTextView);
        changeFont(universityTextView);
        contentListView.setOnItemClickListener(this);
        Log.d("Bulletin Test", singleton.getUserResponse().get_id());

        refreshItems();

        return view;

    }
    public void refreshItems(){
        if(processingItemRefresh == false){
            processingItemRefresh = true;
            singleton.getAPI().getItems(this);
        }


    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {
        processingItemRefresh = false;
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResponsesReceived(RequestType type, int resCode,  Response[] response) {
        if(type == RequestType.GetItems){
            if (resCode == 200){
                Arrays.sort((ItemResponse[]) response, new Comparator<ItemResponse>() {
                    @Override
                    public int compare(ItemResponse t1, ItemResponse t2) {
                        if(t1.getExpiration() > t2.getExpiration()){
                            return -1;
                        }
                        return 1;
                    }
                });

                String userId = BulletinSingleton.getInstance().getUserResponse().get_id();
                int count = 0;
                for(int i=0; i<response.length; i++){
                    if (((ItemResponse)response[i]).getUserId().equals(userId) == false) count++;
                }

                final ItemResponse[] filteredResponses = new ItemResponse[count];
                int filterCount = 0;
                for(int i=0; i<response.length; i++){
                    if (((ItemResponse)response[i]).getUserId().equals(userId) == false){
                        ItemResponse r = (ItemResponse) response[i];
                        filteredResponses[filterCount] = new ItemResponse();
                        filteredResponses[filterCount].setUserId(r.getUserId());
                        filteredResponses[filterCount].setTitle(r.getTitle());
                        filteredResponses[filterCount].setPrice(r.getPrice());
                        filteredResponses[filterCount].setPictures(new String[] { r.getPictures()[0]});
                        filteredResponses[filterCount].setUserName(r.getUserName());
                        filteredResponses[filterCount].setUserPicture(r.getUserPicture());
                        filteredResponses[filterCount].set_id(r.get_id());
                        filteredResponses[filterCount++].setDescription(r.getDescription());
                    }
                }

                final HomeItemAdapter adapter = new HomeItemAdapter(getContext(), filteredResponses);
                getActivity().runOnUiThread(new Runnable(){
                    public void run(){
                        contentListView.setAdapter(adapter);
                    }
                });

            }
        }
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                swipeRefresh.setRefreshing(false);
            }
        });
        processingItemRefresh = false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent viewItemIntent = new Intent(getActivity(), ViewItemActivity.class);

        HomeItemAdapter adapter = (HomeItemAdapter) adapterView.getAdapter();
        ItemResponse item = (ItemResponse) adapter.getItem(i);

        viewItemIntent.putExtra("description", item.getDescription());
        viewItemIntent.putExtra("title", item.getTitle());
        viewItemIntent.putExtra("userName", item.getUserName());
        viewItemIntent.putExtra("itemPicture", item.getPictures()[0]);
        viewItemIntent.putExtra("userPicture", item.getUserPicture());
        viewItemIntent.putExtra("itemId", item.get_id());
        startActivity(viewItemIntent);
    }

    @Override
    public void onRefresh() {
        if(processingItemRefresh == true) swipeRefresh.setRefreshing(false);
        refreshItems();
    }
}
