package com.example.fittslaw;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AfterSubmitActivity extends AppCompatActivity {
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
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void export(View view) throws IOException {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // this will request for permission when user has not granted permission for the app
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            Toast.makeText(this, "in IF", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, "in else", Toast.LENGTH_SHORT).show();
            //generate data
            StringBuilder data = new StringBuilder();
            data.append("Time,Distance");
            for (int i = 0; i < 5; i++) {
                data.append("\n" + String.valueOf(i) + "," + String.valueOf(i * i) + "," + String.valueOf(20) + "," + "shah");
            }
            System.out.println(data.toString().getBytes());
            Intent intent = getIntent();
            String str = intent.getStringExtra("input_type");
            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            System.out.println(date);
            String filename = str.replace(" ", "").toLowerCase() + "_" + date + ".csv";
//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),filename);
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
            System.out.println(file.getAbsolutePath());

            try {
                FileOutputStream out = new FileOutputStream(file);
                out.write((data.toString()).getBytes());
                out.close();
                Toast.makeText(AfterSubmitActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(AfterSubmitActivity.this, ThankYouActivity.class);
                intent1.putExtra("file_location", file.getAbsolutePath());
                startActivity(intent1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AfterSubmitActivity.this, "File not found", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AfterSubmitActivity.this, "Error saving", Toast.LENGTH_SHORT).show();
            }
        }
        }

}


//            ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
//            File directory = contextWrapper.getDir(getFilesDir().getName(), Context.MODE_PRIVATE);
//            File file =  new File(directory,"data.csv");
////            String data = “TEST DATA”;
//            FileOutputStream fos = new FileOutputStream("data.csv", true); // save
//            fos.write(data.toString().getBytes());
//            fos.close();

//saving the file into device
//            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
//            out.write((data.toString()).getBytes());
//            out.close();

//exporting
//            Context context = getApplicationContext();
//            File filelocation = new File(getFilesDir(), "data.csv");
//            Uri path = FileProvider.getUriForFile(context, "com.example.fittslaw.fileprovider", filelocation);
//            System.out.println(path);
//            System.out.println(context);
//            System.out.println(filelocation);
//            Intent fileIntent = new Intent(Intent.ACTION_SEND);
//            fileIntent.setType("text/csv");
//            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
//            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
//            startActivity(Intent.createChooser(fileIntent, "Send mail"));

//            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//
//                // this will request for permission when user has not granted permission for the app
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                Toast.makeText(this,"in IF",Toast.LENGTH_SHORT);
//            }
//Download Script
//                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//                File filelocation = new File(getFilesDir(), "dat    a.csv");
//                Context context = getApplicationContext();
//                Uri path = FileProvider.getUriForFile(context, "com.example.fittslaw.fileprovider", filelocation);
//                DownloadManager.Request request = new DownloadManager.Request(path);
//                request.setVisibleInDownloadsUi(true);
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, path.getLastPathSegment());
//                downloadManager.enqueue(request);