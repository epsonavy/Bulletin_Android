package com.cs175.bulletinandroid.bulletin.Tabs;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
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
import com.cs175.bulletinandroid.bulletin.UploadResponse;

import java.io.FileDescriptor;
import java.io.IOException;

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
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
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

        itemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence colors[] = new CharSequence[] {"Take a picture", "Choose from gallery"};

                AlertDialog.Builder builder = new AlertDialog.Builder(EditItemActivity.this);
                builder.setTitle("Upload a picture");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        } else {
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(i, RESULT_LOAD_IMAGE);
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            itemImageView.setImageBitmap(photo);
            singleton.getAPI().uploadImage((OnRequestListener)EditItemActivity.this, photo);

        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = EditItemActivity.this.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            itemImageView.setImageBitmap(bmp);

            singleton.getAPI().uploadImage((OnRequestListener)EditItemActivity.this, bmp);

        }

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                EditItemActivity.this.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
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
            // Need to add update picture here <<<================

            processingMessages = true;

            singleton.getAPI().updateItem(this, itemId, title, description, price, itemPicture);
        }
    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {
        if (type == RequestType.UploadImage) {
            UploadResponse itemResponse = (UploadResponse) response;
            itemPicture = itemResponse.getUrl();
            runThread(1);
        } else {
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
    }

    @Override
    public void onResponsesReceived(RequestType type, int resCode, Response[] response) {

    }

    private void runThread(final int flag) {

        new Thread() {
            public void run() {

                try {
                    EditItemActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (flag == 1) {
                                alertDialog.showDialog(EditItemActivity.this, "Upload image succeeded!");
                            }
                            if (flag == 2) {
                                alertDialog.showDialog(EditItemActivity.this, "Server error, please try again");
                            }
                            if (flag == 3) {
                                alertDialog.showDialog(EditItemActivity.this, "Server error, please try agai");
                            }
                        }
                    });
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }
}
