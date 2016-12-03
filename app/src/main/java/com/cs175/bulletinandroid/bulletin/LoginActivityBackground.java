package com.cs175.bulletinandroid.bulletin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cs175.bulletinandroid.bulletin.animations.BackgroundAnimation;

public class LoginActivityBackground extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_background);
        BackgroundAnimation backgroundAnimation = new BackgroundAnimation(this, (View)findViewById(android.R.id.content), getApplicationContext());
        backgroundAnimation.startAnimation();

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        else {
            Intent StartApp = new Intent(this, LoginActivity.class);
            startActivity(StartApp);
        }

    }

}
