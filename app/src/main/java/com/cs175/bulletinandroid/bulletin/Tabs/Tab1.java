package com.cs175.bulletinandroid.bulletin.Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.cs175.bulletinandroid.bulletin.BulletinSingleton;
import com.cs175.bulletinandroid.bulletin.R;

/**
 * Created by chenyulong on 12/4/16.
 */
public class Tab1 extends Fragment {
    private Button button;
    private TextView textView;
    private BulletinSingleton singleton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab1, container, false);
        button = (Button)view.findViewById(R.id.testButton);
        textView = (TextView)view.findViewById(R.id.textView1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(singleton.getAPI().getToken());
            }
        });

        return view;

    }
}
