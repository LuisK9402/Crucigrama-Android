package com.proyecto.cruzando;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.proyecto.cruzando.databinding.ActivityLevelSelectorBinding;

public class LevelSelectorActivity extends AppCompatActivity {

    SharedPreferences preferencias;
    SharedPreferences.Editor pref_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencias = getSharedPreferences("cw_preferences", Context.MODE_PRIVATE);
        pref_editor = preferencias.edit();

        Resources.Theme tema = getTheme();
        if(preferencias.getString("Tema", "Dark").equals("Dark")){
            tema.applyStyle(R.style.DarkTheme,true);
        }else{
            tema.applyStyle(R.style.LightTheme,true);
        }

        setContentView(R.layout.activity_level_selector);

        FontOptions myFont = new FontOptions(preferencias.getInt("CellFontSize", 11),
                preferencias.getInt("ButtonFontSize", 11),
                preferencias.getInt("HintFontSize", 11),
                preferencias.getString("ChosenFontStyle", "serif"),
                this);

        ActivityLevelSelectorBinding myBinding = DataBindingUtil.setContentView(this,R.layout.activity_level_selector);
        myBinding.setCustomFont(myFont);

        Button returnButton = findViewById(R.id.returnLvlBtn);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void selectLevel(View v){
        Intent i = new Intent(getApplicationContext(),CrosswordActivity.class);
        String buttonText = ((Button) v).getText().toString();
//        String buttonTextAux = buttonText.replaceAll("Nivel ", "");
//        setTheme(R.style.CustomTheme);
        int level = Integer.parseInt(buttonText.replaceAll("Nivel ", ""));
        i.putExtra("level", level);
        startActivity(i);
    }
}