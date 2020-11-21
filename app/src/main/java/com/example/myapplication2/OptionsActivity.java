package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class OptionsActivity extends AppCompatActivity {

    Switch txt2voiceSwitch;
    TextView colorBtn;
    SharedPreferences preferencias;
    SharedPreferences.Editor pref_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        txt2voiceSwitch = findViewById(R.id.txt2voiceSwitch);
        colorBtn = findViewById(R.id.colorMenu);

        preferencias = getSharedPreferences("cw_preferences", Context.MODE_PRIVATE);
        pref_editor = preferencias.edit();

        if(preferencias.getBoolean("txt2voice",true)){
            txt2voiceSwitch.setChecked(true);
        }

        txt2voiceSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt2voiceSwitch.isChecked()){
                    pref_editor.putBoolean("txt2voice",true);
                }
                else {
                    pref_editor.putBoolean("txt2voice", false);
                }
                pref_editor.commit();
            }
        });
    }


}
