package com.cs175.bulletinandroid.bulletin.Tabs;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by chenyulong on 12/4/16.
 */
public class Tab1 extends Fragment implements OnRequestListener {

    private BulletinSingleton singleton = BulletinSingleton.getInstance();

    private Typeface font;

    private TextView mainHeaderTextView;
    private TextView universityTextView;
    private EditText searchEditText;
    private ListView contentListView;

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


        font = Typeface.createFromAsset(getActivity().getAssets(), "Fonts/SF-UI-Display-Light.otf");

        changeFont(mainHeaderTextView);
        changeFont(universityTextView);

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

    }

    @Override
    public void onResponsesReceived(RequestType type, int resCode,  Response[] response) {
        if(type == RequestType.GetItems){
            if (resCode == 200){
                final HomeItemAdapter adapter = new HomeItemAdapter(getContext(), (ItemResponse[]) response);
                getActivity().runOnUiThread(new Runnable(){
                    public void run(){
                        contentListView.setAdapter(adapter);
                    }
                });

            }
        }
    }
}
