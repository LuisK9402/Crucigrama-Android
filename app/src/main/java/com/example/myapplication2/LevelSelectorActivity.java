package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LevelSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selector);
    }


    public void selectLevel(View v){
        Intent i = new Intent(getApplicationContext(),CrosswordActivity.class);
        String buttonText = ((Button) v).getText().toString();
        String buttonTextAux = buttonText.replaceAll("Nivel ", "");
        int level = Integer.parseInt(buttonText.replaceAll("Nivel ", ""));
        i.putExtra("level", level);
        startActivity(i);
    }
}