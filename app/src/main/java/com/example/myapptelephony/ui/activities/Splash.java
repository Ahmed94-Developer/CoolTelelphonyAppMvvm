package com.example.myapptelephony.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.myapptelephony.R;

public class Splash extends AppCompatActivity {
    public static int Time_Screen = 3000;
    TextView Title;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Title = (TextView) findViewById(R.id.textView);
        typeface = Typeface.createFromAsset(getAssets(),"Pacifico.ttf");
        Title.setTypeface(typeface);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        },Time_Screen);
    }
}
