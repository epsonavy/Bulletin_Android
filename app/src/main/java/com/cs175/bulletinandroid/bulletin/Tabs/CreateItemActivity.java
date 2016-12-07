package com.cs175.bulletinandroid.bulletin.Tabs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs175.bulletinandroid.bulletin.BulletinSingleton;
import com.cs175.bulletinandroid.bulletin.Elements.AlertDialogController;
import com.cs175.bulletinandroid.bulletin.OnRequestListener;
import com.cs175.bulletinandroid.bulletin.R;
import com.cs175.bulletinandroid.bulletin.Response;

/**
 * Created by Pei Liu on 12/6/16.
 */

public class CreateItemActivity extends AppCompatActivity implements View.OnClickListener, OnRequestListener {

    private String itemId;

    private boolean processingConversation;

    private AlertDialogController alertDialog;

    private BulletinSingleton singleton = BulletinSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        processingConversation = false;

        alertDialog = new AlertDialogController();

        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        TextView userNameTextView = (TextView) findViewById(R.id.userNameTextView);

        ImageView itemImageView = (ImageView)findViewById(R.id.itemImageView);
        ImageView userImageView = (ImageView)findViewById(R.id.userImageView);


        itemId = getIntent().getStringExtra("itemId");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String itemPicture = getIntent().getStringExtra("itemPicture");
        String userPicture = getIntent().getStringExtra("userPicture");
        String userName = getIntent().getStringExtra("userName");

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        userNameTextView.setText(userName);

        HomeItemAdapter.ViewHolder itemViewHolder = new HomeItemAdapter.ViewHolder();
        HomeItemAdapter.ViewHolder userViewHolder = new HomeItemAdapter.ViewHolder();

        itemViewHolder.url = itemPicture;
        userViewHolder.url = userPicture;

        itemImageView.setTag(itemViewHolder);
        userImageView.setTag(userViewHolder);

        itemViewHolder.imageView = itemImageView;
        userViewHolder.imageView = userImageView;

        new DownloadAsyncTask().execute(itemViewHolder);
        new DownloadAsyncTask().execute(userViewHolder);

        Button conversationButton = (Button) findViewById(R.id.makeConversationButton);
        conversationButton.setOnClickListener(this);






    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.makeConversationButton){
            if (processingConversation == false){

                processingConversation = true;
                singleton.getAPI().makeConversation(this, itemId);

            }
        }
    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {
        if(type == RequestType.MakeConversation){
            if(response.getResponseCode() == 200){
                CreateItemActivity.this.runOnUiThread(new Runnable(){
                    public void run(){
                        alertDialog.showDialog(CreateItemActivity.this, "You made a conversation!");
                    }
                });

            }else{
                CreateItemActivity.this.runOnUiThread(new Runnable(){
                    public void run(){
                        alertDialog.showDialog(CreateItemActivity.this, "Conversation has already been made!");
                    }
                });
            }
            processingConversation = false;
        }
    }

    @Override
    public void onResponsesReceived(RequestType type, int resCode, Response[] response) {

    }
}
