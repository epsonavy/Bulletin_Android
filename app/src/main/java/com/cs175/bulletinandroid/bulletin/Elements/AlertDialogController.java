package com.cs175.bulletinandroid.bulletin.Elements;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cs175.bulletinandroid.bulletin.LoginActivity;
import com.cs175.bulletinandroid.bulletin.R;
import com.cs175.bulletinandroid.bulletin.RegisterActivity;

/**
 * Created by chenyulong on 12/3/16.
 */
public class AlertDialogController {

    public void showDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_alert_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.alertText);
        text.setText(msg);
        Button dialogButton = (Button)dialog.findViewById(R.id.alertButton);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void startAnotherActivity(final Activity activity, String msg, final String info) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_alert_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.alertText);
        text.setText(msg);

        Button dialogButton = (Button)dialog.findViewById(R.id.alertButton);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(activity, RegisterActivity.class);
                intent.putExtra("key", info);
                activity.startActivity(intent);
            }
        });

        dialog.show();
    }
}
