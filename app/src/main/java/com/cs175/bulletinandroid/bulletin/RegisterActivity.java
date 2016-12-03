package com.cs175.bulletinandroid.bulletin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    String userEmail;
    EditText emailText;
    EditText usernameText;
    EditText passwordText;
    EditText confirmationText;

    RelativeLayout emailView;
    RelativeLayout usernameView;
    RelativeLayout passwordView;
    RelativeLayout confirmationView;



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




        emailText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(true) {
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
}
