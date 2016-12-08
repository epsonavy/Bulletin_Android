package com.cs175.bulletinandroid.bulletin.Tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by chenyulong on 12/4/16.
 */
public class TabPager extends FragmentStatePagerAdapter {

    int tabCount;


    private Tab1 tab1 = new Tab1();
    private Tab2 tab2 = new Tab2();
    private Tab3 tab3 = new Tab3();
    private Tab4 tab4 = new Tab4();
    private Tab5 tab5 = new Tab5();


    public TabPager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }




    @Override
    public Fragment getItem(int position) {


        switch(position){
            case 0:
                return tab1;
            case 1:
                return tab2;
            case 2:
                return tab3;
            case 3:
                return tab4;
            case 4:
                return tab5;
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
