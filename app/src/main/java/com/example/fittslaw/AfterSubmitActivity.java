package com.example.fittslaw;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AfterSubmitActivity extends AppCompatActivity {
    TextView receiver_msg;
    boolean isActualTrial = false;
    DatabaseHelper db_helper;
    List<TrialDataEntry> dataEntryList;
    Button shareButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftersubmit);

        receiver_msg = (TextView) findViewById(R.id.text_input_type);
        Intent intent = getIntent();
        String str = intent.getStringExtra("input_type");
        isActualTrial = intent.getBooleanExtra("is_actual_trial", false);
        if (!isActualTrial) {
            shareButton = findViewById(R.id.button);
            shareButton.setText("GO TO HOME");
        }
        db_helper = new DatabaseHelper(this);
        receiver_msg.setText(str);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void export(View view) throws IOException {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // this will request for permission when user has not granted permission for the app
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//            Toast.makeText(this, "in IF", Toast.LENGTH_SHORT).show();
        } else if (isActualTrial) {

//            Toast.makeText(this, "in else", Toast.LENGTH_SHORT).show();
            //generate data
            dataEntryList = db_helper.getAllTrialDataEntry();

            StringBuilder data = new StringBuilder();
            data.append("id,trial_no,input_device,start_button_x,start_button_y,start_button_width,target_button_x," +
                    "target_button_y,target_button_width,target_touch_x,target_touch_y,start_button_click_timestamp," +
                    "target_button_click_timestamp,movement_time,amplitude,index_of_difficulty,is_missed");
            for (TrialDataEntry trialDataEntry : dataEntryList) {
                data.append("\n")
                        .append(trialDataEntry._id).append(",")
                        .append(trialDataEntry.trial_no).append(",")
                        .append(trialDataEntry.input_device).append(",")
                        .append(trialDataEntry.start_button_x).append(",")
                        .append(trialDataEntry.start_button_y).append(",")
                        .append(trialDataEntry.start_button_width).append(",")
                        .append(trialDataEntry.target_button_x).append(",")
                        .append(trialDataEntry.target_button_y).append(",")
                        .append(trialDataEntry.target_button_width).append(",")
                        .append(trialDataEntry.target_touch_x).append(",")
                        .append(trialDataEntry.target_touch_y).append(",")
                        .append(trialDataEntry.start_button_click_timestamp).append(",")
                        .append(trialDataEntry.target_button_click_timestamp).append(",")
                        .append(trialDataEntry.movement_time).append(",")
                        .append(trialDataEntry.amplitude).append(",")
                        .append(trialDataEntry.index_of_difficulty).append(",")
                        .append(trialDataEntry.is_missed);
            }
            Intent intent = getIntent();
            String str = intent.getStringExtra("input_type");
            String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ssZ", Locale.getDefault()).format(new Date());
            String filename = str.replace(" ", "").toLowerCase() + "_" + date + ".csv";
//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),filename);
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);

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
        } else {
            Intent intent = new Intent(AfterSubmitActivity.this, MainScreenActivity.class);
            startActivity(intent);
            finish();
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
