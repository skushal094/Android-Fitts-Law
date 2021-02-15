package com.example.fittslaw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class StartScreenActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscreen);
        addListenerButton();
    }

    private void addListenerButton() {
        radioGroup = findViewById(R.id.radioGender);
        button = findViewById(R.id.btnDisplay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedID = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedID);
//                Toast.makeText(startscreen.this, radioButton.getText(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StartScreenActivity.this, TrialActivity.class);
                intent.putExtra("input_type", radioButton.getText().toString());
                startActivity(intent);
//                finish();
            }
        });
    }
}
