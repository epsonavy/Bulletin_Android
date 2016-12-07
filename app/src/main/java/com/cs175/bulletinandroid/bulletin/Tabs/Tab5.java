package com.cs175.bulletinandroid.bulletin.Tabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cs175.bulletinandroid.bulletin.LoginActivityBackground;
import com.cs175.bulletinandroid.bulletin.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by chenyulong on 12/4/16.
 */
public class Tab5 extends Fragment {

    private Button settingButton;
    private static String GET_TOKEN = "FETCHTOKEN";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.tab5, container, false);
        settingButton = (Button)view.findViewById(R.id.SettingButton);

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(GET_TOKEN, MODE_PRIVATE).edit();
                editor.clear();
                Intent intent = new Intent(getActivity(), LoginActivityBackground.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;

    }
}
