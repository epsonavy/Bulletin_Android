package com.cs175.bulletinandroid.bulletin;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.cs175.bulletinandroid.bulletin.animations.*;
import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements OnRequestListener {
    RelativeLayout emailview;
    RelativeLayout passwordview;
    RelativeLayout loadingPanel;
    EditText emailtext;
    EditText passwordtext;
    BulletinSingleton singleton;
    private Context context;
    private RelativeLayout.LayoutParams params;
    private boolean serverResponse;
    private boolean UserInteractions = true;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        WindowManager manager = (WindowManager) getSystemService(Activity.WINDOW_SERVICE);
        int width, height;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Point point = new Point();
        manager.getDefaultDisplay().getSize(point);
        width = point.x;
        height = point.y;
        lp.width = width;
        lp.height = height;
        //getWindow().setAttributes(lp);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);



        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(0);
        getWindow().setBackgroundDrawable(d);
        context = LoginActivity.this;
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);




        emailview = (RelativeLayout)findViewById(R.id.emailview);
        passwordview = (RelativeLayout)findViewById(R.id.passwordview);
        emailtext = (EditText)findViewById(R.id.emailedittext);
        passwordtext = (EditText)findViewById(R.id.passwordedittext);
        loadingPanel = (RelativeLayout)findViewById(R.id.loadingPanel);

        loadingPanel.setVisibility(View.INVISIBLE);
        //getWindow().setBackgroundDrawableResource(R.drawable.background1);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //getWindow().setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL);


        passwordview.setVisibility(View.INVISIBLE);
        params = (RelativeLayout.LayoutParams)(emailview).getLayoutParams();
        serverResponse = false;
        email = "";



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivityBackground.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);



        LoginAnimation loginAnimation = new LoginAnimation(emailview, getApplicationContext());
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        emailview.setLayoutParams(params);
        emailtext.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = size.x;
                    int height = size.y;

                    Log.d("getheight", width+","+height);
                    email = emailtext.getText().toString();
                    if (!email.equals("") || email == null) {
                        singleton.getInstance().getAPI().checkEmail((OnRequestListener) context, email);
                        loadingPanel.setVisibility(View.VISIBLE);
                        //UserInteractions = false;
                        int i = 0;

                        /*while (UserInteractions == false) {

                        }*/
                        loadingPanel.setVisibility(View.GONE);
                        if (serverResponse == true) {
                            passwordview.setVisibility(View.VISIBLE);
                            passwordview.requestFocus();
                            params.addRule(RelativeLayout.ABOVE, R.id.passwordview);
                            params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            emailview.setLayoutParams(params);

                            return true;
                        } else {
                            new AlertDialog.Builder(context)
                                    .setMessage("Let's get you registered")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                            intent.putExtra("key", email);
                                            LoginActivity.this.startActivity(intent);
                                        }
                                    })

                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }

                    }

                }
                return false;
            }

        });
        //loginAnimation.startAnimation();
    }

    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {
            if (UserInteractions) {
                return super.dispatchTouchEvent(ev);
            } else {
                return false;
            }
    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {
        String message = "";
        if(response.getResponseCode() == 418){
            message = "email not found";

        }else if (response.getResponseCode() == 200){
            message = "email found";
            serverResponse = true;
        }else{
            message = " fuck you";
            //something went wrong with the server
        }
        Log.d("god", message);

        UserInteractions = true;
    }
}
