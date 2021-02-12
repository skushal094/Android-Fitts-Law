package com.example.fittslaw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class aftersubmit extends AppCompatActivity {
    TextView receiver_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftersubmit);

        receiver_msg = (TextView)findViewById(R.id.text_input_type);
        Intent intent = getIntent();
        String str = intent.getStringExtra("input_type");
        System.out.println(str);
        receiver_msg.setText(str);
    }

}