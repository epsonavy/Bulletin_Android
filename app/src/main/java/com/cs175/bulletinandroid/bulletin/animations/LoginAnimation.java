package com.cs175.bulletinandroid.bulletin.animations;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

/**
 * Created by chenyulong on 11/23/16.
 */
public class LoginAnimation {
    private RelativeLayout layoutview;
    private Context context;
    public LoginAnimation(RelativeLayout layoutview, Context context) {
        this.layoutview = layoutview;
        this.context = context;
    }

    public void startAnimation(){
        final int [] position = new int[2];
        layoutview.getLocationInWindow(position);
        Log.d("damn", position[0]+","+position[1]);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        final int height = size.y;
        TranslateAnimation animation = new TranslateAnimation(0, 0, height-position[1], 0);

        animation.setDuration(2000);
        animation.setFillAfter(true);
        //animation.setRepeatCount(Animation.INFINITE);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                layoutview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layoutview.clearAnimation();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        layoutview.startAnimation(animation);
    }
}
