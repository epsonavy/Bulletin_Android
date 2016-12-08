package com.cs175.bulletinandroid.bulletin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
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
            String profile_picture = info.getProfile_picture();
            singleton.getInstance().getUserResponse().set_id(store_id);
            singleton.getInstance().getUserResponse().setDisplay_name(displayName);
            singleton.getInstance().getUserResponse().setProfile_picture(profile_picture);
            runThread(1);
        } else {
            runThread(2);
        }
    }

    @Override
    public void onResponsesReceived(RequestType type, int resCode, Response[] response) {

    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1
            );
        }else{
            Intent intent = new Intent(RetrieveProfile.this, HomePageActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("Bulletin", "You're set to go!");
                    Intent intent = new Intent(RetrieveProfile.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Intent goHome = new Intent(this, LoginActivity.class);
                    startActivity(goHome);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
                                verifyStoragePermissions(RetrieveProfile.this);

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
