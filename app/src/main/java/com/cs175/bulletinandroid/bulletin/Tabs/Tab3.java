package com.cs175.bulletinandroid.bulletin.Tabs;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs175.bulletinandroid.bulletin.BulletinSingleton;
import com.cs175.bulletinandroid.bulletin.Elements.AlertDialogController;
import com.cs175.bulletinandroid.bulletin.HomePageActivity;
import com.cs175.bulletinandroid.bulletin.OnRequestListener;
import com.cs175.bulletinandroid.bulletin.R;
import com.cs175.bulletinandroid.bulletin.Response;
import com.cs175.bulletinandroid.bulletin.UploadResponse;

import java.io.FileDescriptor;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Pei Liu on 12/7/16.
 */
public class Tab3 extends Fragment implements View.OnClickListener, OnRequestListener {

    private AlertDialogController alertDialog;
    private boolean processingItemRefresh;
    private BulletinSingleton singleton = BulletinSingleton.getInstance();

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

    private Typeface font;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab3, container, false);
        processingItemRefresh = false;

        TextView mainHeaderTextView = (TextView) view.findViewById(R.id.createItemTitleTextView);
        titleEditText = (EditText) view.findViewById(R.id.inputCreateItemTitle);
        descriptionEditText = (EditText) view.findViewById(R.id.inputCreateDescription);
        priceEditText = (EditText) view.findViewById(R.id.inputCreateItemPrice);
        itemImageView = (ImageView) view.findViewById(R.id.createItemImageView);
        itemPicture = "";

        itemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence colors[] = new CharSequence[] {"Take a picture", "Choose from gallery"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        font = Typeface.createFromAsset(getActivity().getAssets(), "Fonts/SF-UI-Display-Light.otf");
        changeFont(mainHeaderTextView);

        Button createButton = (Button) view.findViewById(R.id.createItemButton);
        createButton.setOnClickListener(this);

        return view;
    }

    public void changeFont(TextView text){
        text.setTypeface(font);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.createItemButton) {
            itemPrice = priceEditText.getText().toString();
            Double price;
            try {
                price = Double.parseDouble(itemPrice);
            } catch (NumberFormatException e) {
                getActivity().runOnUiThread(new Runnable(){
                    public void run(){
                        Toast.makeText(getActivity(), "Please enter valid Price!",
                                Toast.LENGTH_LONG).show();
                        //alertDialog.showDialog(getActivity(), "Please enter valid Price!");
                    }
                });
                return;
            }

            title = titleEditText.getText().toString();
            description = descriptionEditText.getText().toString();

            processingItemRefresh = true;
            singleton.getAPI().postItem(this, title, description, itemPicture, price);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            itemImageView.setImageBitmap(photo);
            singleton.getAPI().uploadImage((OnRequestListener)getActivity(), photo);

        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
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

            singleton.getAPI().uploadImage((OnRequestListener)getActivity(), bmp);

        }

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {
        if (type == RequestType.UploadImage) {
            UploadResponse itemResponse = (UploadResponse) response;
            itemPicture = itemResponse.getUrl();
            runThread(1);
        } else {

            if(type == RequestType.PostItem) {
                processingItemRefresh = false;
                if(response.getResponseCode() == 200){
                    getActivity().runOnUiThread(new Runnable(){
                        public void run(){
                            singleton.homePageActivity.tab4Refresh();
                            Toast.makeText(getActivity(), "You Post an item!",
                                    Toast.LENGTH_LONG).show();
                            //alertDialog.showDialog(getActivity(), "You Post an item!");
                        }
                    });

                }else{
                    getActivity().runOnUiThread(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(), "The server is down!",
                                    Toast.LENGTH_LONG).show();
                            //alertDialog.showDialog(getActivity(), "The server is down!");
                        }
                    });
                }
            }
            processingItemRefresh = false;
        }
    }

    @Override
    public void onResponsesReceived(RequestType type, int resCode, Response[] response) {

    }

    private void runThread(final int flag) {

        new Thread() {
            public void run() {

                try {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //nextButton.setText(title);
                            if (flag == 1) {
                                alertDialog.showDialog(getActivity(), "Upload image succeeded in fragment!");
                            }
                            if (flag == 2) {


                            }
                            if (flag == 3) {
                                alertDialog.showDialog(getActivity(), "Server error, please try agai");
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
