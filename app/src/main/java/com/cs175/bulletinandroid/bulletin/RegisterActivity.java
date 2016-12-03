package com.cs175.bulletinandroid.bulletin;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Normalizer;

public class RegisterActivity extends AppCompatActivity  implements OnRequestListener{

    String userEmail;
    EditText emailText;
    EditText usernameText;
    EditText passwordText;
    EditText confirmationText;

    RelativeLayout emailView;
    RelativeLayout usernameView;
    RelativeLayout passwordView;
    RelativeLayout confirmationView;

    Button nextButton;
    Instrumentation inst;

    private int flag;
    private BulletinSingleton singleton;
    private Context context;


    private String email;
    private String password;
    private String username;
    private int responseCode;

    private FormatValidator validator;

    private boolean UserInteractions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();


        userEmail = "deafult";
        userEmail = getIntent().getStringExtra("key");
        if (!userEmail.equals("")) {
            emailText.setText(userEmail);
            emailText.setSelection(emailText.getText().length());
        }

        nextButton = (Button)findViewById(R.id.nextButton);
        inst = new Instrumentation();
        flag = 0;
        responseCode = 0;

        validator = new FormatValidator();

        setFocusLisenters();
        context = RegisterActivity.this;
        UserInteractions = true;

        emailText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(validator.validateEmail(emailText.getText().toString())) {

                    usernameView.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)emailView.getLayoutParams();
                    layoutSwitch(params, R.id.usernamelayout);
                    emailView.setLayoutParams(params);

                    usernameView.requestFocus();
                    return true;
                }
                return false;
            }
        });

        usernameText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (true) {
                    passwordView.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)usernameView.getLayoutParams();
                    layoutSwitch(params, R.id.passwordlayout);
                    usernameView.setLayoutParams(params);

                    passwordView.requestFocus();
                    return true;
                }
                return false;
            }
        });

        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (true) {

                    confirmationView.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)passwordView.getLayoutParams();
                    layoutSwitch(params, R.id.conformationlayout);
                    passwordView.setLayoutParams(params);
                    confirmationView.requestFocus();
                    return true;
                }
                return false;
            }
        });

        confirmationText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               if (true) {

               }
               return false;
           }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    usernameView.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)emailView.getLayoutParams();
                    layoutSwitch(params, R.id.usernamelayout);
                    emailView.setLayoutParams(params);

                    usernameView.requestFocus();
                } else if (flag == 1) {
                    passwordView.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)usernameView.getLayoutParams();
                    layoutSwitch(params, R.id.passwordlayout);
                    usernameView.setLayoutParams(params);

                    passwordView.requestFocus();
                } else if (flag == 2) {
                    confirmationView.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)passwordView.getLayoutParams();
                    layoutSwitch(params, R.id.conformationlayout);
                    passwordView.setLayoutParams(params);
                    confirmationView.requestFocus();
                } else {
                    //Done
                    password = confirmationText.getText().toString();
                    if (password.equals(passwordText.getText().toString())) {
                        email = emailText.getText().toString();
                        username = usernameText.getText().toString();
                        UserInteractions = false;
                        singleton.getInstance().getAPI().register((OnRequestListener) context, email, password, username);
                        String title = nextButton.getText().toString();
                        nextButton.setText("Verifying");
                        while (UserInteractions == false) {

                        }
                        nextButton.setText(title);
                        if (responseCode == 200) {
                            Intent intent = new Intent(RegisterActivity.this, RegistrationCompletedActivity.class);
                            startActivity(intent);
                            finish();

                        }

                    } else {
                        //password doesn't match
                    }
                }
            }
        });

    }

    private void initViews () {
        emailText = (EditText) findViewById(R.id.registerEmail);
        usernameText = (EditText) findViewById(R.id.registerUserName);
        passwordText = (EditText) findViewById(R.id.registerPassword);
        confirmationText = (EditText) findViewById(R.id.registerConfirmation);

        emailView = (RelativeLayout)findViewById(R.id.emaillayout);
        usernameView = (RelativeLayout)findViewById(R.id.usernamelayout);
        passwordView = (RelativeLayout)findViewById(R.id.passwordlayout);
        confirmationView = (RelativeLayout)findViewById(R.id.conformationlayout);

        usernameView.setVisibility(View.INVISIBLE);
        passwordView.setVisibility(View.INVISIBLE);
        confirmationView.setVisibility(View.INVISIBLE);

    }

    private void layoutSwitch(RelativeLayout.LayoutParams lp, int id){

        lp.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.removeRule(RelativeLayout.ABOVE);
        lp.addRule(RelativeLayout.ABOVE, id);

    }

    private void setFocusLisenters() {
        emailText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    flag = 0;
                    usernameView.setVisibility(View.GONE);
                    passwordView.setVisibility(View.GONE);
                    confirmationView.setVisibility(View.GONE);
                }
            }
        });

        usernameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    flag = 1;
                    passwordView.setVisibility(View.GONE);
                    confirmationView.setVisibility(View.GONE);
                }
            }
        });

        passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    flag = 2;
                    confirmationView.setVisibility(View.GONE);
                }
            }
        });

        confirmationText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    flag = 3;
                    nextButton.setText("Done");
                } else {
                    nextButton.setText("Next");
                }
            }
        });
    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {
        String message = "";
        if(response.getResponseCode() == 400){
            message = "register not work";
            responseCode = 400;

        }else if (response.getResponseCode() == 200){
            message = "register works";
            responseCode = 200;
        }else{
            message = " fuck you";
            //something went wrong with the server
        }
        Log.d("god", message);
        UserInteractions = true;
    }
    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {
        if (UserInteractions) {
            return super.dispatchTouchEvent(ev);
        } else {
            return false;
        }
    }

}
