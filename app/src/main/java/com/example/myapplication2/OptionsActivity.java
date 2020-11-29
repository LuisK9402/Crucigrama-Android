package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication2.databinding.ActivityOptionsBinding;

public class OptionsActivity extends AppCompatActivity {

    Switch txt2voiceSwitch;
    TextView colorBtn;
    Button returnBtn;
    RadioGroup radioFontSize;
    RadioGroup radioFontStyle;
    RadioGroup radioColors;

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

        setContentView(R.layout.activity_options);

        FontOptions myFont = new FontOptions(preferencias.getInt("CellFontSize", 11),
                preferencias.getInt("ButtonFontSize", 11),
                preferencias.getInt("HintFontSize", 11),
                preferencias.getString("ChosenFontStyle", "serif"),
                this);

        ActivityOptionsBinding myBinding = DataBindingUtil.setContentView(this,R.layout.activity_options);
        myBinding.setCustomFont(myFont);

        txt2voiceSwitch = findViewById(R.id.txt2voiceSwitch);
        returnBtn = findViewById(R.id.returnOptBtn);
        radioFontSize = findViewById(R.id.radioGroupFontSize);
        radioFontStyle = findViewById(R.id.radioGroupFontStyle);
        radioColors = findViewById(R.id.radioGroupColors);

        setSwitchstate(preferencias.getBoolean("txt2voice",false));

        switch (preferencias.getInt("ChosenFontSize",0)){
            case FontOptions.SMALL_FONT:
                ((RadioButton)findViewById(R.id.radioSmall)).setChecked(true);
                break;
            case FontOptions.MEDIUM_FONT:
                ((RadioButton)findViewById(R.id.radioMedium)).setChecked(true);
                break;
            case FontOptions.LARGE_FONT:
                ((RadioButton)findViewById(R.id.radioLarge)).setChecked(true);
                break;
        }

        switch (preferencias.getInt("ChosenFontStyleNUM",0)){
            case FontOptions.SERIF_FONT:
                ((RadioButton)findViewById(R.id.radioSerif)).setChecked(true);
                break;
            case FontOptions.DISLEXY_FONT:
                ((RadioButton)findViewById(R.id.radioDyslexic)).setChecked(true);
                break;
        }

        if(preferencias.getString("Tema", "Dark").equals("Dark")){
            ((RadioButton)findViewById(R.id.radioDark)).setChecked(true);
        }else{
            ((RadioButton)findViewById(R.id.radioLight)).setChecked(true);
        }

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt2voiceSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt2voiceSwitch.isChecked()){
                    pref_editor.putBoolean("txt2voice",true);
                    txt2voiceSwitch.setText("Activado");
                }
                else {
                    pref_editor.putBoolean("txt2voice", false);
                    txt2voiceSwitch.setText("Desactivado");
                }
                pref_editor.commit();
            }
        });



        radioFontSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onSizeRadioButtonClicked(checkedId);
            }
        });

        radioFontStyle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onStyleRadioButtonClicked(checkedId);
            }
        });

        radioColors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onColorRadioButtonClicked(checkedId);
            }
        });
    }

    private void setSwitchstate(Boolean state){
        if(state){
            txt2voiceSwitch.setChecked(true);
            txt2voiceSwitch.setText("Activado");
        }
        else{
            txt2voiceSwitch.setChecked(false);
            txt2voiceSwitch.setText("Desactivado");
        }
    }

    public void onSizeRadioButtonClicked(int checkedId) {
        // Is the button now checked?
        boolean checked = ((RadioButton) findViewById(checkedId)).isChecked();

        // Check which radio button was clicked
        switch(checkedId) {
            case R.id.radioSmall:
                if(checked) {
                    this.pref_editor.putInt("ChosenFontSize", FontOptions.SMALL_FONT);
                    this.pref_editor.putInt("CellFontSize", 10);
                    this.pref_editor.putInt("ButtonFontSize", 10);
                    this.pref_editor.putInt("HintFontSize", 10);
                }
                break;
            case R.id.radioMedium:
                if(checked) {
                    this.pref_editor.putInt("ChosenFontSize", FontOptions.MEDIUM_FONT);
                    this.pref_editor.putInt("CellFontSize", 15);
                    this.pref_editor.putInt("ButtonFontSize", 15);
                    this.pref_editor.putInt("HintFontSize", 15);
                }
                break;
            case R.id.radioLarge:
                if(checked) {
                    this.pref_editor.putInt("ChosenFontSize", FontOptions.LARGE_FONT);
                    this.pref_editor.putInt("CellFontSize", 20);
                    this.pref_editor.putInt("ButtonFontSize", 15);
                    this.pref_editor.putInt("HintFontSize", 20);
                }
                break;
        }

        pref_editor.commit();
        reload();
    }

    public void onStyleRadioButtonClicked(int checkedId) {
        // Is the button now checked?
        boolean checked = ((RadioButton) findViewById(checkedId)).isChecked();

        // Check which radio button was clicked
        switch(checkedId) {
            case R.id.radioSerif:
                if(checked) {
                    this.pref_editor.putInt("ChosenFontStyleNUM", FontOptions.SERIF_FONT);
                    this.pref_editor.putString("ChosenFontStyle", "serif");
                }
                break;
            case R.id.radioDyslexic:
                if(checked) {
                    this.pref_editor.putInt("ChosenFontStyleNUM", FontOptions.DISLEXY_FONT);
                    this.pref_editor.putString("ChosenFontStyle", "openDyslexic");
                }
                break;
        }

        pref_editor.commit();
        reload();
    }

    private void onColorRadioButtonClicked(int checkedId){
        // Is the button now checked?
        boolean checked = ((RadioButton) findViewById(checkedId)).isChecked();

        // Check which radio button was clicked
        switch(checkedId) {
            case R.id.radioDark:
                if(checked) {
                    this.pref_editor.putString("Tema", "Dark");
                }
                break;
            case R.id.radioLight:
                if(checked) {
                    this.pref_editor.putString("Tema", "Light");
                }
                break;
        }

        pref_editor.commit();
        reload();
    }

    private void reload(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

}
