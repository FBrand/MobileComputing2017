package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("APP", "onCreate start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("APP", "onCreate ende");
    }
}
