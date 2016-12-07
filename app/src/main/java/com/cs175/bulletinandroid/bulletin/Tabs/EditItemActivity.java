package com.cs175.bulletinandroid.bulletin.Tabs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class EditItemActivity extends AppCompatActivity implements View.OnClickListener, OnRequestListener {

    private String itemId;

    private boolean processingConversation;

    private AlertDialogController alertDialog;

    private BulletinSingleton singleton = BulletinSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        processingConversation = false;

        alertDialog = new AlertDialogController();

        EditText titleEditText = (EditText) findViewById(R.id.titleEditText);
        EditText descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        EditText priceEditText = (EditText) findViewById(R.id.priceEditText);
        ImageView itemImageView = (ImageView)findViewById(R.id.editItemImageView);

        itemId = getIntent().getStringExtra("itemId");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String itemPicture = getIntent().getStringExtra("itemPicture");
        String itemPrice = getIntent().getStringExtra("itemPrice");


        titleEditText.setText(title);
        descriptionEditText.setText(description);
        priceEditText.setText(itemPrice);

        MyListingItemAdapter.ViewHolder itemViewHolder = new MyListingItemAdapter.ViewHolder();

        itemViewHolder.url = itemPicture;

        itemImageView.setTag(itemViewHolder);

        itemViewHolder.imageView = itemImageView;

        new DownloadItemsAsyncTask().execute(itemViewHolder);

        Button updateButton = (Button) findViewById(R.id.updateItemButton);
        updateButton.setOnClickListener(this);






    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.updateItemButton){
            if (processingConversation == false){

                processingConversation = true;
                //singleton.getAPI().makeConversation(this, itemId);
                //updating

            }
        }
    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {

        /*
        if(type == RequestType.MakeConversation){
            if(response.getResponseCode() == 200){
                EditItemActivity.this.runOnUiThread(new Runnable(){
                    public void run(){
                        alertDialog.showDialog(EditItemActivity.this, "You made a conversation!");
                    }
                });

            }else{
                EditItemActivity.this.runOnUiThread(new Runnable(){
                    public void run(){
                        alertDialog.showDialog(EditItemActivity.this, "Conversation has already been made!");
                    }
                });
            }
            processingConversation = false;
        }*/
    }

    @Override
    public void onResponsesReceived(RequestType type, int resCode, Response[] response) {

    }
}
