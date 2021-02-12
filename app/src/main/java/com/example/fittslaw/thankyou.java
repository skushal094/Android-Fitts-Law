package com.example.fittslaw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class thankyou extends AppCompatActivity {
    TextView receiver_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);

        receiver_msg = (TextView)findViewById(R.id.textView11);
        Intent intent = getIntent();
        String str = intent.getStringExtra("file_location");
        System.out.println(str);
        receiver_msg.setText(str);
    }
}