package com.example.fittslaw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class TrialActivity extends AppCompatActivity {
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trialacivity);


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(TrialActivity.this, AfterSubmitActivity.class);


                Intent intent1 = getIntent();
                String str = intent1.getStringExtra("input_type");
                intent.putExtra("input_type",str.toString());

                startActivity(intent);

            }
        },3000);
    }
}