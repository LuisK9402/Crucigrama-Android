package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.SharedPreferences;
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

    SharedPreferences preferencias;
    SharedPreferences.Editor pref_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        preferencias = getSharedPreferences("cw_preferences", Context.MODE_PRIVATE);
        pref_editor = preferencias.edit();

        FontOptions myFont = new FontOptions(preferencias.getInt("CellFontSize", 11),
                                            preferencias.getInt("ButtonFontSize", 11),
                                            preferencias.getInt("HintFontSize", 11),
                                            preferencias.getString("ChosenFontStyle", "serif"),
                                            this);

        ActivityOptionsBinding myBinding = DataBindingUtil.setContentView(this,R.layout.activity_options);
        myBinding.setCustomFont(myFont);
        setTheme(R.style.CustomTheme);

        txt2voiceSwitch = findViewById(R.id.txt2voiceSwitch);
        colorBtn = findViewById(R.id.colorMenu);
        returnBtn = findViewById(R.id.returnOptBtn);
        radioFontSize = findViewById(R.id.radioGroupFontSize);
        radioFontStyle = findViewById(R.id.radioGroupFontStyle);

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

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    Toast.makeText(this, "Small", Toast.LENGTH_SHORT).show();
                    this.pref_editor.putInt("ChosenFontSize", FontOptions.SMALL_FONT);
                    this.pref_editor.putInt("CellFontSize", 10);
                    this.pref_editor.putInt("ButtonFontSize", 10);
                    this.pref_editor.putInt("HintFontSize", 10);
                }
                break;
            case R.id.radioMedium:
                if(checked) {
                    Toast.makeText(this, "Medio", Toast.LENGTH_SHORT).show();
                    this.pref_editor.putInt("ChosenFontSize", FontOptions.MEDIUM_FONT);
                    this.pref_editor.putInt("CellFontSize", 15);
                    this.pref_editor.putInt("ButtonFontSize", 15);
                    this.pref_editor.putInt("HintFontSize", 15);
                }
                break;
            case R.id.radioLarge:
                if(checked) {
                    Toast.makeText(this, "Grande", Toast.LENGTH_SHORT).show();
                    this.pref_editor.putInt("ChosenFontSize", FontOptions.LARGE_FONT);
                    this.pref_editor.putInt("CellFontSize", 20);
                    this.pref_editor.putInt("ButtonFontSize", 15);
                    this.pref_editor.putInt("HintFontSize", 20);
                }
                break;
        }

        pref_editor.commit();
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
    }

}
