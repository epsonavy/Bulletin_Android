package com.cs175.bulletinandroid.bulletin.Tabs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cs175.bulletinandroid.bulletin.R;

/**
 * Created by Lucky on 12/6/16.
 */

public class ViewItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        TextView userNameTextView = (TextView) findViewById(R.id.userNameTextView);


        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String itemPicture = getIntent().getStringExtra("itemPicture");
        String userPicture = getIntent().getStringExtra("userPicture");
        String userName = getIntent().getStringExtra("userName");

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        userNameTextView.setText(userName);




    }
}
