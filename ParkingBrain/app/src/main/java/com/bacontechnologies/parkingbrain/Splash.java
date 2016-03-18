package com.bacontechnologies.parkingbrain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    private static final long CHANGE_MAIN_ACTIVITY_DELAY = 300;
    private static final long CHANGE_MOOD_DELAY = 1800;
    private Intent mainIntent;

    private ImageView pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        pet = (ImageView)findViewById(R.id.petPicture);

        mainIntent = new Intent(this, MainActivity.class);

        changePigMood();

    }

    private void changePigMoodOff(){
        pet.setBackground(getDrawable(R.drawable.pig_off));
    }

    public void changePigMood(){
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        changePigMoodOff();

                        TimerTask task2 = new TimerTask() {
                            @Override
                            public void run() {
                                startActivity(mainIntent);
                                finish();
                            }
                        };

                        Timer timer2 = new Timer();
                        timer2.schedule(task2, CHANGE_MAIN_ACTIVITY_DELAY);
                    }
                });
            }
        };

        Timer timer1 = new Timer();
        timer1.schedule(task1, CHANGE_MOOD_DELAY);
    }
}
