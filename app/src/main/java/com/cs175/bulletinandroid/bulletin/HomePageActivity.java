package com.cs175.bulletinandroid.bulletin;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;


import com.cs175.bulletinandroid.bulletin.Elements.AlertDialogController;
import com.cs175.bulletinandroid.bulletin.Tabs.*;

public class HomePageActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, OnRequestListener {

    private TabLayout tabLayout;

    private TabLayout.Tab tab1;
    private TabLayout.Tab tab2;
    private TabLayout.Tab tab3;
    private TabLayout.Tab tab4;
    private TabLayout.Tab tab5;
    private AlertDialogController alertDialog;

    private BulletinSingleton singleton;
    private TabPager adapter;

    private ViewPager viewPager;
    private String pictureURL;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        alertDialog = new AlertDialogController();
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        initTabIcons();

        viewPager = (ViewPager)findViewById(R.id.pager);

        adapter = new TabPager(getSupportFragmentManager(), tabLayout.getTabCount());



        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);
        tabLayout.addOnTabSelectedListener(this);

        Log.d("tabCount", ""+adapter.getCount());

        BulletinSingleton.getInstance().homePageActivity = this;


    }

    public void initTabIcons(){
        tab1 = tabLayout.newTab();
        tab2 = tabLayout.newTab();
        tab3 = tabLayout.newTab();
        tab4 = tabLayout.newTab();
        tab5 = tabLayout.newTab();

        tab1.setIcon(R.drawable.home_filled);
        tab2.setIcon(R.drawable.message);
        tab3.setIcon(R.drawable.user);
        tab4.setIcon(R.drawable.talk);
        tab5.setIcon(R.drawable.settings);

        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.addTab(tab3);
        tabLayout.addTab(tab4);
        tabLayout.addTab(tab5);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.d("tabCount", "count is: "+tab.getPosition());
        tab1.setIcon(R.drawable.home);
        tab2.setIcon(R.drawable.message);
        tab3.setIcon(R.drawable.user);
        tab4.setIcon(R.drawable.talk);
        tab5.setIcon(R.drawable.settings);

        int index = tab.getPosition();
        if (index == 0) {
            tab1.setIcon(R.drawable.home_filled);
        } else if (index == 1) {
            tab2.setIcon(R.drawable.message_filled);
        } else if (index == 2) {
            tab3.setIcon(R.drawable.user_filled);
        } else if (index == 3) {
            tab4.setIcon(R.drawable.talk_filled);
        } else if (index == 4) {
            tab5.setIcon(R.drawable.settings_filled);
        }
        viewPager.setCurrentItem(tab.getPosition());

    }

    public void tab1Refresh(){
        ((Tab1)adapter.getItem(0)).refreshItems();
    }

    public void tab2Refresh(){
        ((Tab2)adapter.getItem(1)).refreshMessages();
    }

    public void tab4Refresh(){
        ((Tab4)adapter.getItem(3)).refreshItems();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onResponseReceived(RequestType type, Response response) {


        if (response.getResponseCode() == 200) {
            if (type == RequestType.UploadImage) {
                UploadResponse itemResponse = (UploadResponse) response;
                pictureURL = itemResponse.getUrl();
                runThread(1);
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
                                if (pictureURL!=null) {
                                    singleton.getInstance().getAPI().updatePicture((OnRequestListener) HomePageActivity.this, pictureURL);
                                }

                            }
                            if (flag == 2) {
                                alertDialog.showDialog(HomePageActivity.this, "Server error, please try again");
                            }
                            if (flag == 3) {
                                alertDialog.showDialog(HomePageActivity.this, "Server error, please try agai");
                            }
                            if (flag == 4) {
                                alertDialog.showDialog(HomePageActivity.this, "Upload image succeeded!");
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
