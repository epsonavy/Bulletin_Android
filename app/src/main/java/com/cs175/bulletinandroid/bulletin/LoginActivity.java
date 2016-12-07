package com.cs175.bulletinandroid.bulletin;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.icu.text.LocaleDisplayNames;
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


import com.cs175.bulletinandroid.bulletin.Elements.AlertDialogController;
import com.cs175.bulletinandroid.bulletin.animations.*;
import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements OnRequestListener {
    RelativeLayout emailview;
    RelativeLayout passwordview;

    private static String GET_TOKEN = "FETCHTOKEN";
    EditText emailtext;
    EditText passwordtext;
    BulletinSingleton singleton;
    private Context context;
    private RelativeLayout.LayoutParams params;

    private boolean UserInteractions = true;
    private String email;
    private String password;
    private FormatValidator validator;
    private AlertDialogController alertDialog;
    private SuccessMessageTokenResponse token;
    private String getStoredToken;

    private TextView mainTitle;
    private TextView subTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        singleton.getInstance().setFont(context);

        SharedPreferences prefs = getSharedPreferences(GET_TOKEN, MODE_PRIVATE);

        getStoredToken = "x";

        getStoredToken = prefs.getString("token", "error");//"No name defined" is the default value.

        Log.d("checktokensave", getStoredToken);
        if (!getStoredToken.equals("error")) {
            Log.d("checktokensave", getStoredToken);
            singleton.getInstance().getAPI().checkToken((OnRequestListener) context, getStoredToken);
        }

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(0);
        getWindow().setBackgroundDrawable(d);


        validator = new FormatValidator();
        alertDialog = new AlertDialogController();





        mainTitle = (TextView)findViewById(R.id.LoginMainTitle);
        subTitle = (TextView)findViewById(R.id.LoginSubTitle);
        emailview = (RelativeLayout)findViewById(R.id.emailview);
        passwordview = (RelativeLayout)findViewById(R.id.passwordview);
        emailtext = (EditText)findViewById(R.id.emailedittext);
        passwordtext = (EditText)findViewById(R.id.passwordedittext);

        mainTitle.setTypeface(singleton.getInstance().getFont());
        subTitle.setTypeface(singleton.getInstance().getFont());


        passwordview.setVisibility(View.INVISIBLE);
        params = (RelativeLayout.LayoutParams)(emailview).getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        emailview.setLayoutParams(params);

        email = "";
        password = "";


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

        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        emailview.setLayoutParams(params);
        emailtext.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_NEXT) {


                    email = emailtext.getText().toString();

                    if (!validator.validateEmail(email)) {

                        alertDialog.showDialog(LoginActivity.this, "Please use an .edu email!");
                        return false;
                    }

                    if (!email.equals("") && email != null) {
                        singleton.getInstance().getAPI().checkEmail((OnRequestListener) context, email);
                        UserInteractions = false;

                    }

                }
                return false;
            }

        });

        passwordtext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    email = emailtext.getText().toString();
                    if (!validator.validateEmail(email)) {
                        alertDialog.showDialog(LoginActivity.this, "Please use an .edu email!");
                        return false;
                    }

                    password = passwordtext.getText().toString();
                    int index = validator.validatePassword(password);

                    if (index == 0) {
                        singleton.getInstance().getAPI().login((OnRequestListener) context, email, password);

                        UserInteractions = false;


                    } else if (index == 1) {
                        alertDialog.showDialog(LoginActivity.this, "Password is too short!");

                    } else if (index == 2) {
                        alertDialog.showDialog(LoginActivity.this, "Password is too long!");

                    }




                }
                return false;
            }
        });

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

        if(response.getResponseCode() == 418){
            //message = "email not found";

            if (type == RequestType.CheckEmail) {
                runThread(0);
                //alertDialog.startAnotherActivity(LoginActivity.this, "Let's get you registered", email);
            }


        }else if (response.getResponseCode() == 200){
            //message = "email found";
            Log.d("run" ,"x");
            if (type == RequestType.CheckEmail) {

                runThread(1);
            }
            else if (type == RequestType.Login) {

                token = (SuccessMessageTokenResponse) response;
                String tokenInfo = token.getToken();
                Log.d("token", tokenInfo);
                singleton.getInstance().getAPI().setToken(tokenInfo);
                SharedPreferences.Editor editor = getSharedPreferences(GET_TOKEN, MODE_PRIVATE).edit();
                editor.putString("token", tokenInfo);
                editor.apply();
                runThread(2);

            } else if (type == RequestType.CheckToken) {
                Log.d("run" ,getStoredToken);
                singleton.getInstance().getAPI().setToken(getStoredToken);

                runThread(2);
            }
        } else if (response.getResponseCode() == 400) {
            //password not found

            if (type == RequestType.Login) {
                alertDialog.showDialog(LoginActivity.this, "Password not recognized");
            }

        }
        else{

            alertDialog.showDialog(LoginActivity.this, "Unable to connect to server");
            //something went wrong with the server
        }

        UserInteractions = true;
    }

    private void runThread(final int flag) {

        new Thread() {
            public void run() {

                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //go to register page
                                if (flag == 0)
                                    alertDialog.startAnotherActivity(LoginActivity.this, "Let's get you registered", email);
                                //move to password view
                                if (flag == 1) {
                                    passwordview.setVisibility(View.VISIBLE);
                                    params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                                    emailview.setLayoutParams(params);
                                    passwordview.requestFocus();
                                }
                                //login in
                                if (flag == 2) {
                                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                    startActivity(intent);
                                    finish();
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
