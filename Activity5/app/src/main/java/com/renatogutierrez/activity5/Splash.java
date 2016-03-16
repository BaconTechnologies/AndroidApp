package com.renatogutierrez.activity5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 2000;
    private Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        mainIntent = new Intent(this, MainActivity.class);

        setTimer();
    }

    public void setTimer(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                startActivity(mainIntent);
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
}
