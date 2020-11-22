package com.js.kakatooth_sms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainA extends AppCompatActivity {
    Handler handler =new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
    }

    @Override
    protected void onResume(){
        super.onResume();
        handler.postDelayed(r,2000);
    }

    @Override
    protected void onPause(){
        super.onPause();
        handler.removeCallbacks(r);
    }
}
