package com.cs175.bulletinandroid.bulletin.Tabs;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
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

    private Typeface font;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab3, container, false);

        TextView mainHeaderTextView = (TextView) view.findViewById(R.id.createItemTitleTextView);
        titleEditText = (EditText) view.findViewById(R.id.inputCreateItemTitle);
        descriptionEditText = (EditText) view.findViewById(R.id.inputCreateDescription);
        priceEditText = (EditText) view.findViewById(R.id.inputCreateItemPrice);

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
            // Need to add update picture here <<<================
            itemPicture = "";

            processingItemRefresh = true;
            singleton.getAPI().postItem(this, title, description, itemPicture, price);
        }
    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {
        Toast.makeText(getActivity(), "received Response",
                Toast.LENGTH_LONG).show();
        if(type == RequestType.PostItem) {
            Toast.makeText(getActivity(), ""+response.getResponseCode(),
                    Toast.LENGTH_LONG).show();
            processingItemRefresh = false;
            if(response.getResponseCode() == 200){
                getActivity().runOnUiThread(new Runnable(){
                    public void run(){
                        singleton.homePageActivity.tab1Refresh();
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

    @Override
    public void onResponsesReceived(RequestType type, int resCode, Response[] response) {

    }
}
