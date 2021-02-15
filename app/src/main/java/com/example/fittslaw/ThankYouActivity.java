package com.example.fittslaw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThankYouActivity extends AppCompatActivity {
    TextView receiver_msg;
    Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);

        receiver_msg = (TextView) findViewById(R.id.textView11);
        Intent intent = getIntent();
        String str = intent.getStringExtra("file_location");
        System.out.println(str);
        receiver_msg.setText(str);

        homeButton = findViewById(R.id.button2);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThankYouActivity.this, MainScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
