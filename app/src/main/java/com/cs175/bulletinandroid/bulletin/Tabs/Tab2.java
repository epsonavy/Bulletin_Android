package com.cs175.bulletinandroid.bulletin.Tabs;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cs175.bulletinandroid.bulletin.BulletinSingleton;
import com.cs175.bulletinandroid.bulletin.ConversationResponse;
import com.cs175.bulletinandroid.bulletin.ItemResponse;
import com.cs175.bulletinandroid.bulletin.OnRequestListener;
import com.cs175.bulletinandroid.bulletin.R;
import com.cs175.bulletinandroid.bulletin.Response;

/**
 * Created by chenyulong on 12/4/16.
 */
public class Tab2 extends Fragment implements OnRequestListener, AdapterView.OnItemClickListener {
    private boolean processingMessages;
    private ListView contentListView;

    private Typeface font;

    private TextView mainHeaderTextView;
    private TextView subHeaderTextView;

    private BulletinSingleton singleton = BulletinSingleton.getInstance();

    public void changeFont(TextView text){
        text.setTypeface(font);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.tab2, container, false);

        font = Typeface.createFromAsset(getActivity().getAssets(), "Fonts/SF-UI-Display-Light.otf");

        mainHeaderTextView = (TextView) view.findViewById(R.id.mainHeaderTextView);
        subHeaderTextView = (TextView) view.findViewById(R.id.subHeaderTextView);

        changeFont(mainHeaderTextView);
        changeFont(subHeaderTextView);

        processingMessages = false;

        contentListView = (ListView) view.findViewById(R.id.contentListView);
        contentListView.setOnItemClickListener(this);

        refreshMessages();
        return view;
    }



    public void refreshMessages(){
        if (processingMessages == false){
            processingMessages = true;
            singleton.getAPI().getConverations(this);
        }
    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {

    }

    @Override
    public void onResponsesReceived(RequestType type, int resCode, Response[] response) {
        if(type == RequestType.GetConversations) {
            if (resCode == 200) {
                final MessageItemAdapter adapter = new MessageItemAdapter(getContext(), (ConversationResponse[]) response);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        contentListView.setAdapter(adapter);
                    }
                });
            }
        }
        processingMessages = false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent conversationIntent = new Intent(getActivity(), ConversationActivity.class);
        MessageItemAdapter adapter = (MessageItemAdapter) adapterView.getAdapter();
        ConversationResponse conversation = (ConversationResponse) adapter.getItem(i);

        String userId = singleton.getUserResponse().get_id();
        if(userId.equals(conversation.getUserWith())){
            conversationIntent.putExtra("userName", conversation.getUserStartName());
        }else{
            conversationIntent.putExtra("userName", conversation.getUserWithName());
        }

        conversationIntent.putExtra("conversationId", conversation.get_id());


        startActivity(conversationIntent);
        Log.d("Bulletin", "Chnaged");
    }
}
