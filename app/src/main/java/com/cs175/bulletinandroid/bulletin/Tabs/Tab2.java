package com.cs175.bulletinandroid.bulletin.Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cs175.bulletinandroid.bulletin.ConversationResponse;
import com.cs175.bulletinandroid.bulletin.ItemResponse;
import com.cs175.bulletinandroid.bulletin.OnRequestListener;
import com.cs175.bulletinandroid.bulletin.R;
import com.cs175.bulletinandroid.bulletin.Response;

/**
 * Created by chenyulong on 12/4/16.
 */
public class Tab2 extends Fragment implements OnRequestListener {
    private boolean processingMessages;
    private ListView contentListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.tab2, container, false);
        processingMessages = false;

        contentListView = (ListView) view.findViewById(R.id.contentListView);

        refreshMessages();
        return view;
    }



    public void refreshMessages(){
        if (processingMessages == false){
            processingMessages = true;
        }
    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {

    }

    @Override
    public void onResponsesReceived(RequestType type, int resCode, Response[] response) {
        if (resCode == 200){
            final MessageItemAdapter adapter = new MessageItemAdapter(getContext(), (ConversationResponse[]) response);
            getActivity().runOnUiThread(new Runnable(){
                public void run(){
                    contentListView.setAdapter(adapter);
                }
            });
        }
    }
}
