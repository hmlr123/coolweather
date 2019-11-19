package com.hmlr123.coolweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.amitshekhar.DebugDB;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DebugDB.getAddressLog();
    }
}
