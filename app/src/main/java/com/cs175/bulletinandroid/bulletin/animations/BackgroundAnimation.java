package com.cs175.bulletinandroid.bulletin.animations;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cs175.bulletinandroid.bulletin.LoginActivity;
import com.cs175.bulletinandroid.bulletin.R;

/**
 * Created by chenyulong on 11/22/16.
 */
public class BackgroundAnimation {
    private View view;
    private Context context;
    private Activity loginActivity;
    int flag;
    TransitionDrawable transition;
    RelativeLayout layout;
    public BackgroundAnimation(Activity loginActiviy, View view, Context context) {
        this.loginActivity = loginActiviy;
        this.view = view;
        this.context = context;
        flag = 0;
        transition = (TransitionDrawable) ContextCompat.getDrawable(context, R.drawable.transition);
    }

    public void startAnimation(){
        layout = (RelativeLayout)view.findViewById(R.id.mainLayout);
        layout.setBackground(transition);
        hanldechange();

    }
    public void hanldechange() {
        Handler hand = new Handler();

        hand.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (flag == 0) {

                    transition = (TransitionDrawable) ContextCompat.getDrawable(context, R.drawable.transition);
                    layout.setBackground(transition);
                    transition.startTransition(3000);
                    flag = 1;
                } else if (flag == 1) {
                    transition = (TransitionDrawable) ContextCompat.getDrawable(context, R.drawable.transition2);
                    layout.setBackground(transition);
                    transition.startTransition(3000);
                    flag = 2;
                } else {
                    transition = (TransitionDrawable) ContextCompat.getDrawable(context, R.drawable.transition3);
                    layout.setBackground(transition);
                    transition.startTransition(3000);
                    flag = 0;
                }
                hanldechange();
            }
        }, 3000);
    }

}
