package com.cs175.bulletinandroid.bulletin;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cs175.bulletinandroid.bulletin.Elements.AlertDialogController;

public class RetrieveProfile extends AppCompatActivity implements OnRequestListener{

    BulletinSingleton singleton;
    private AlertDialogController dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_profile);

        dialog = new AlertDialogController();
        singleton.getInstance().getAPI().getMyUserDetails(RetrieveProfile.this);
    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {
        if (response.getResponseCode() == 200) {

            UserResponse info = (UserResponse) response;
            String store_id = info.get_id();
            String displayName = info.getDisplay_name();
            singleton.getInstance().getUserResponse().set_id(store_id);
            singleton.getInstance().getUserResponse().setDisplay_name(displayName);
            runThread(1);
            Intent intent = new Intent(RetrieveProfile.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        } else {
            runThread(2);
        }
    }

    @Override
    public void onResponsesReceived(RequestType type, int resCode, Response[] response) {

    }

    private void runThread(final int flag) {

        new Thread() {
            public void run() {

                try {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (flag == 1) {
                                //login completed

                                Intent intent = new Intent(RetrieveProfile.this, HomePageActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            if (flag == 2) {
                                dialog.showDialog(RetrieveProfile.this, "Something wrong");
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
