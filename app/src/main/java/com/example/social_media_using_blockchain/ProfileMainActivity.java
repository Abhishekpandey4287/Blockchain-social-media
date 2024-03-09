package com.example.social_media_using_blockchain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class ProfileMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to hide the status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}