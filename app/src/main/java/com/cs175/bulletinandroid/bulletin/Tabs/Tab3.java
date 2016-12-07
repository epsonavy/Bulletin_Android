package com.cs175.bulletinandroid.bulletin.Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs175.bulletinandroid.bulletin.BulletinSingleton;
import com.cs175.bulletinandroid.bulletin.Elements.AlertDialogController;
import com.cs175.bulletinandroid.bulletin.HomePageActivity;
import com.cs175.bulletinandroid.bulletin.OnRequestListener;
import com.cs175.bulletinandroid.bulletin.R;
import com.cs175.bulletinandroid.bulletin.Response;

/**
 * Created by Pei Liu on 12/7/16.
 */
public class Tab3 extends Fragment implements View.OnClickListener, OnRequestListener {

    private AlertDialogController alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab3, container, false);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.createItemButton) {
            alertDialog.showDialog(getActivity(), "You Post an item!");
        }
    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {

    }

    @Override
    public void onResponsesReceived(RequestType type, int resCode, Response[] response) {
        if(type == RequestType.PostItem) {



            /*
            if(response.getResponseCode() == 200){
                Tab3.this.runOnUiThread(new Runnable(){
                    public void run(){
                        alertDialog.showDialog(EditItemActivity.this, "You made a conversation!");
                    }
                });

            }else{
                Tab3.this.runOnUiThread(new Runnable(){
                    public void run(){
                        alertDialog.showDialog(EditItemActivity.this, "Conversation has already been made!");
                    }
                });
            }*/
        }
    }
}
