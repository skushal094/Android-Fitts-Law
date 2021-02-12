package com.example.fittslaw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class mainscreen extends AppCompatActivity {
    ListView listview;
    String mlabel[] = {"Practice Test","Trial Test"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        listview = findViewById(R.id.listview);
        Myadapter adapter = new Myadapter(this, mlabel);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Toast.makeText(mainscreen.this, "First List Item", Toast.LENGTH_SHORT).show();
                }
                if (position == 1){
//                    Toast.makeText(mainscreen.this, "Second List Item", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mainscreen.this,startscreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    class Myadapter extends ArrayAdapter<String>{
        Context context;
        String rlabel[];
        Myadapter(Context c, String label[])
        {
            super(c, R.layout.list_design,R.id.textView2, label);
            this.context = c;
            this.rlabel=label;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_design,parent, false);
            TextView mylabel = row.findViewById(R.id.textView2);

            mylabel.setText(rlabel[position]);
            return row;
        }
    }
}