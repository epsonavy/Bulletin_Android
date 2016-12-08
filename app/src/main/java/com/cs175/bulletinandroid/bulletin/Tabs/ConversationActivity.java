package com.cs175.bulletinandroid.bulletin.Tabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cs175.bulletinandroid.bulletin.BulletinSingleton;
import com.cs175.bulletinandroid.bulletin.ConversationResponse;
import com.cs175.bulletinandroid.bulletin.LoginActivity;
import com.cs175.bulletinandroid.bulletin.MessageResponse;
import com.cs175.bulletinandroid.bulletin.OnRequestListener;
import com.cs175.bulletinandroid.bulletin.R;
import com.cs175.bulletinandroid.bulletin.Response;


/**
 * Created by Lucky on 12/7/16.
 */

public class ConversationActivity extends AppCompatActivity implements OnRequestListener, TextView.OnEditorActionListener, SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefresh;

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_NEXT) {


            String message = sendMessageEditText.getText().toString();
            if(message.length() > 0){
                processingMessages = true;
                singleton.getAPI().sendMessage(this, conversationId, message);
                sendMessageEditText.setText("");
            }

        }
        return false;
    }

    private TextView mainHeaderTextView;
    private TextView subHeaderTextView;
    private String conversationId;
    private String userName;

    private EditText sendMessageEditText;

    private ListView contentListView;


    private Context context;

    private boolean processingMessages;

    private BulletinSingleton singleton = BulletinSingleton.getInstance();

    private Typeface font;

    private void changeFont(TextView text){
        text.setTypeface(font);
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        context = this;

        processingMessages = false;

        mainHeaderTextView = (TextView) findViewById(R.id.mainHeaderTextView);
        subHeaderTextView = (TextView) findViewById(R.id.subHeaderTextView);

        sendMessageEditText = (EditText) findViewById(R.id.sendMessageEditText);

        font = Typeface.createFromAsset(getAssets(), "Fonts/SF-UI-Display-Light.otf");

        changeFont(mainHeaderTextView);
        changeFont(subHeaderTextView);


        Intent conversationIntent = getIntent();
        userName = conversationIntent.getStringExtra("userName");
        conversationId = conversationIntent.getStringExtra("conversationId");

        subHeaderTextView.setText("with " + userName);

        contentListView = (ListView) findViewById(R.id.contentListView);
        sendMessageEditText.setOnEditorActionListener(this);

        refreshMessages();

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.scrollControl);
        swipeRefresh.setOnRefreshListener(this);




    }
    protected void onResume(){
        super.onResume();

    }



    public void refreshMessages(){

        if (processingMessages == false){
            processingMessages = true;
            singleton.getAPI().getAllMessages(this, conversationId);
        }

    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {
        if (type == RequestType.SendMessage){
            processingMessages = false;
            if(response.getResponseCode() == 200) {
                refreshMessages();
            }
        }
        ConversationActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                swipeRefresh.setRefreshing(false);
            }
        });
        processingMessages = false;
    }

    @Override
    public void onResponsesReceived(RequestType type, int resCode, Response[] response) {
        if(type == RequestType.GetAllMessages){
            if (resCode == 200){

                final MessageEntityItemAdapter adapter = new MessageEntityItemAdapter(this, (MessageResponse[]) response, userName);
                ConversationActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        contentListView.setAdapter(adapter);

                    }
                });
            }
        }
        ConversationActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                swipeRefresh.setRefreshing(false);
            }
        });
        processingMessages = false;
    }

    public void onBackPressed(){
        BulletinSingleton.getInstance().homePageActivity.tab2Refresh();
        super.onBackPressed();
    }

    @Override
    public void onRefresh() {
        if (processingMessages == true) swipeRefresh.setRefreshing(false);
        refreshMessages();
    }
}
