package com.example.fittslaw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainScreenActivity extends AppCompatActivity {
    ListView listview;
    String mlabel[] = {"Practice Test", "Trial Test"};
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        listview = findViewById(R.id.listview);
        MyAdapter adapter = new MyAdapter(this, mlabel);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
//                    Toast.makeText(MainScreenActivity.this, "First List Item", Toast.LENGTH_SHORT).show();
                    intent = new Intent(MainScreenActivity.this, TrialActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (position == 1) {
//                    Toast.makeText(mainscreen.this, "Second List Item", Toast.LENGTH_SHORT).show();
                    intent = new Intent(MainScreenActivity.this, StartScreenActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rlabel[];

        MyAdapter(Context c, String label[]) {
            super(c, R.layout.list_design, R.id.textView2, label);
            this.context = c;
            this.rlabel = label;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View row = layoutInflater.inflate(R.layout.list_design, parent, false);
            TextView mylabel = row.findViewById(R.id.textView2);

            mylabel.setText(rlabel[position]);
            return row;
        }
    }
}
