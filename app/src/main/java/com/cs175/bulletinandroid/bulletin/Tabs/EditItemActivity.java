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

    private String title;
    private String description;
    private String itemPicture;
    private String itemPrice;

    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private ImageView itemImageView;

    private boolean processingMessages;

    private AlertDialogController alertDialog;

    private BulletinSingleton singleton = BulletinSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        processingMessages = false;
        alertDialog = new AlertDialogController();

        titleEditText = (EditText) findViewById(R.id.titleEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        itemImageView = (ImageView)findViewById(R.id.editItemImageView);

        itemId = getIntent().getStringExtra("itemId");
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        itemPicture = getIntent().getStringExtra("itemPicture");
        itemPrice = getIntent().getStringExtra("itemPrice");

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
            itemPrice = priceEditText.getText().toString();
            Double price;
            try {
                price = Double.parseDouble(itemPrice);
            } catch (NumberFormatException e) {
                alertDialog.showDialog(EditItemActivity.this, "Please enter valid Price!");
                return;
            }

            title = titleEditText.getText().toString();
            description = descriptionEditText.getText().toString();
            //Need to add update picture = ??

            processingMessages = true;
            //test message
            //alertDialog.showDialog(EditItemActivity.this, "desc:"+ description+"Price:"+price);

            singleton.getAPI().updateItem(this, itemId, title, description, price, itemPicture);
        }
    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {

        if (type == RequestType.UpdateItem){
            processingMessages = false;
            if(response.getResponseCode() == 200) {
                EditItemActivity.this.runOnUiThread(new Runnable(){
                    public void run(){
                        singleton.homePageActivity.tab4Refresh();
                        alertDialog.showDialog(EditItemActivity.this, "You updated this item!");
                    }
                });
            }
        }
        processingMessages = false;
    }

    @Override
    public void onResponsesReceived(RequestType type, int resCode, Response[] response) {

    }
}
