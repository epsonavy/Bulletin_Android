package com.cs175.bulletinandroid.bulletin.Tabs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs175.bulletinandroid.bulletin.BulletinSingleton;
import com.cs175.bulletinandroid.bulletin.Elements.AlertDialogController;
import com.cs175.bulletinandroid.bulletin.Elements.RoundedImageView;
import com.cs175.bulletinandroid.bulletin.FormatValidator;
import com.cs175.bulletinandroid.bulletin.LoginActivityBackground;
import com.cs175.bulletinandroid.bulletin.OnRequestListener;
import com.cs175.bulletinandroid.bulletin.R;
import com.cs175.bulletinandroid.bulletin.RegisterActivity;
import com.cs175.bulletinandroid.bulletin.RegistrationCompletedActivity;
import com.cs175.bulletinandroid.bulletin.Response;

import org.w3c.dom.Text;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by chenyulong on 12/4/16.
 */
public class Tab5 extends Fragment implements OnRequestListener {

    private Button settingButton;
    private BulletinSingleton singleton;
    private static String GET_TOKEN = "FETCHTOKEN";
    private RoundedImageView settingPicture;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private AlertDialogController alertDialog;
    private TextView emailText;
    private EditText passwordText;
    private EditText confirmText;
    private EditText hiddenText;
    private FormatValidator validator;
    private TextView mainTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.tab5, container, false);
        settingButton = (Button)view.findViewById(R.id.SettingButton);
        settingPicture = (RoundedImageView)view.findViewById(R.id.SettingPicture);
        alertDialog = new AlertDialogController();

        emailText = (TextView)view.findViewById(R.id.SettingEmailText);
        passwordText = (EditText)view.findViewById(R.id.SettingPasswordText);
        confirmText = (EditText)view.findViewById(R.id.SettingConfirmText);
        hiddenText = (EditText)view.findViewById(R.id.SettingHiddenText);
        mainTitle = (TextView)view.findViewById(R.id.SettingMainTitle);

        mainTitle.setTypeface(singleton.getInstance().getFont());

        hiddenText.requestFocus();
        passwordText.setTransformationMethod(new PasswordTransformationMethod());
        confirmText.setTransformationMethod(new PasswordTransformationMethod());
        validator = new FormatValidator();

        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                int index = validator.validatePassword(passwordText.getText().toString());

                if (index == 0) {
                    confirmText.requestFocus();
                    return true;
                } else if (index == 1) {
                    alertDialog.showDialog(getActivity(), "Password is too short!");
                    return false;
                } else if (index == 2) {
                    alertDialog.showDialog(getActivity(), "Password is too long!");
                    return false;
                }

                return false;
            }
        });

        hiddenText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    settingButton.setText("Logout");
                }
            }
        });




        passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    settingButton.setText("Next");
                }
            }
        });

        confirmText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String password = confirmText.getText().toString();
                if (password.equals(passwordText.getText().toString())) {

                    singleton.getInstance().getAPI().updatePassword((OnRequestListener) getActivity(), password);
                    hiddenText.requestFocus();
                    return true;
                } else {
                    alertDialog.showDialog(getActivity(), "Passwords do not match!");
                }
                return false;
            }
        });

        confirmText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    settingButton.setText("Confirm");
                }
            }
        });




        emailText.setText(singleton.getInstance().getUserResponse().getEmail());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        HomeItemAdapter.ViewHolder viewHolder = new HomeItemAdapter.ViewHolder();
        viewHolder.imageView = settingPicture;

        //alertDialog.showDialog(getActivity(), singleton.getInstance().getUserResponse().getProfile_picture());
        viewHolder.url = singleton.getInstance().getUserResponse().getProfile_picture();
        settingPicture.setTag(viewHolder);
        new DownloadAsyncTaskProfilePicture().execute(viewHolder);



        settingPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CharSequence colors[] = new CharSequence[] {"Take a picture", "Choose from gallery"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = settingButton.getText().toString();
                if (title.equals("Logout")) {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(GET_TOKEN, MODE_PRIVATE).edit();
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(getActivity(), LoginActivityBackground.class);
                    startActivity(intent);
                    getActivity().finish();

                } else if (title.equals("Next")) {

                    int index = validator.validatePassword(passwordText.getText().toString());

                    if (index == 0) {

                        confirmText.requestFocus();
                    } else if (index == 1) {
                        alertDialog.showDialog(getActivity(), "Password is too short!");

                    } else if (index == 2) {
                        alertDialog.showDialog(getActivity(), "Password is too long!");

                    }

                } else if (title.equals("Confirm")) {

                    String password = confirmText.getText().toString();
                    if (password.equals(passwordText.getText().toString())) {

                        singleton.getInstance().getAPI().updatePassword((OnRequestListener) getActivity(), password);
                        hiddenText.requestFocus();
                    } else {
                        alertDialog.showDialog(getActivity(), "Passwords do not match!");
                    }
                }
            }
        });


        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            settingPicture.setImageBitmap(photo);
            singleton.getInstance().setflag("tab5");
            singleton.getInstance().getAPI().uploadImage((OnRequestListener)getActivity(), photo);

        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
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
            settingPicture.setImageBitmap(bmp);
            singleton.getInstance().setflag("tab5");
            singleton.getInstance().getAPI().uploadImage((OnRequestListener)getActivity(), bmp);


        }

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {
        if (type == RequestType.UploadImage) {
            runThread(1);
        }
    }

    @Override
    public void onResponsesReceived(RequestType type, int resCode, Response[] response) {

    }

    private void runThread(final int flag) {

        new Thread() {
            public void run() {

                try {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //nextButton.setText(title);
                            if (flag == 1) {
                                alertDialog.showDialog(getActivity(), "Upload image succeeded in fragment!");
                            }
                            if (flag == 2) {


                            }
                            if (flag == 3) {
                                alertDialog.showDialog(getActivity(), "Server error, please try agai");
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
