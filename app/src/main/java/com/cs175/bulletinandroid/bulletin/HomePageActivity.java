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


import com.cs175.bulletinandroid.bulletin.Tabs.*;

public class HomePageActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener  {

    private TabLayout tabLayout;

    private TabLayout.Tab tab1;
    private TabLayout.Tab tab2;
    private TabLayout.Tab tab3;
    private TabLayout.Tab tab4;
    private TabLayout.Tab tab5;

    private TabPager adapter;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

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

        tab1.setIcon(R.drawable.home);
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
}
