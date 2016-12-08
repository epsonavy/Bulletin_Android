package com.cs175.bulletinandroid.bulletin;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cs175.bulletinandroid.bulletin.Elements.AlertDialogController;
import com.cs175.bulletinandroid.bulletin.Elements.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.R.attr.bitmap;

public class RegistrationCompletedActivity extends AppCompatActivity implements  OnRequestListener {

    private RelativeLayout layout;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private String selectedImagePath;
    private RoundedImageView imageView;
    private Button completeButton;
    private String email;
    private String password;
    private boolean loginEnable;
    private BulletinSingleton singleton;
    private Context context;
    private AlertDialogController alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_completed);

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        context = RegistrationCompletedActivity.this;

        loginEnable = false;
        alertDialog = new AlertDialogController();
        if (email!=null && password!=null) {
            singleton.getInstance().getAPI().login((OnRequestListener) RegistrationCompletedActivity.this, email, password);
        }

        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int dp = (int) (150 * scale + 0.5f);
        int dpTop = (int) (30*scale + 0.5f);
        initViews();

        imageView = new RoundedImageView(getApplicationContext());

        imageView.setImageResource(R.drawable.tuzki);
        imageView.setAdjustViewBounds(true);
        imageView.setBackgroundResource(R.drawable.profile_circle_shape);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dp, dp);

        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.BELOW, R.id.confirmationSubTitle);
        params.setMargins(0, dpTop, 0, 0);
        imageView.setLayoutParams(params);
        layout.addView(imageView);





        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.tuzki);
        File f = new File(context.getCacheDir(), "profile_picture");
        try {
            f.createNewFile();

//Convert bitmap to byte array

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        singleton.getInstance().getAPI().uploadImage((OnRequestListener)context, bmp);









        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginEnable) {
                    Intent intent = new Intent(RegistrationCompletedActivity.this, RetrieveProfile.class);
                    startActivity(intent);
                    finish();
                } else {
                    alertDialog.showDialog(RegistrationCompletedActivity.this, "Login failed, try again!");
                }
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence colors[] = new CharSequence[] {"Take a picture", "Choose from gallery"};

                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationCompletedActivity.this);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
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
            imageView.setImageBitmap(bmp);




            //create a file to write bitmap data
            File f = new File(context.getCacheDir(), "profile_picture");
            try {
                f.createNewFile();

//Convert bitmap to byte array

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            singleton.getInstance().getAPI().uploadImage((OnRequestListener)context, bmp);



        }


    }



    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    public void initViews() {
        layout = (RelativeLayout)findViewById(R.id.confirmationPageLayout);
        completeButton = (Button)findViewById(R.id.completeButton);
    }



    @Override
    public void onResponseReceived(RequestType type, Response response) {
        if (response.getResponseCode() == 200){
            //message = "register works";

            if (type == RequestType.UploadImage) {
                Log.d("dddd", "upload image works");
                runThread(1);
            }
            if (type == RequestType.Login) {
                loginEnable = true;
                SuccessMessageTokenResponse token = (SuccessMessageTokenResponse) response;
                String tokenInfo = token.getToken();
                Log.d("token", tokenInfo);
                singleton.getInstance().getAPI().setToken(tokenInfo);
                SharedPreferences.Editor editor = getSharedPreferences(singleton.getInstance().GET_TOKEN, MODE_PRIVATE).edit();
                editor.putString("token", tokenInfo);
                editor.apply();
            }
        } else if (response.getResponseCode() == 400) {
            if (type == RequestType.UploadImage) {
                runThread(2);
            }
        } else {

                runThread(3);

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
                            //nextButton.setText(title);
                            if (flag == 1) {
                                alertDialog.showDialog(RegistrationCompletedActivity.this, "works!");
                            }
                            if (flag == 2) {
                                alertDialog.showDialog(RegistrationCompletedActivity.this, "400 error");
                            }
                            if (flag == 3) {
                                alertDialog.showDialog(RegistrationCompletedActivity.this, "other errors");
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

    /*public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 50;
        int targetHeight = 50;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }*/
}
