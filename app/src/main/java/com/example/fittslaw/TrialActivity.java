package com.example.fittslaw;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TrialActivity extends AppCompatActivity {

    RelativeLayout layout;
    Button paneButton, startButton;
    Intent trialIntent;
    DisplayMetrics displayMetrics;

    int screenWidth, screenHeight, layoutWidth, layoutHeight, layoutDiagonal;

    String inputDevice;
    int[] A = new int[]{0, 0, 0};
    int[] W = new int[]{0, 0, 0};
    int a_pos, w_pos;
    int MAX_TRIALS = 2;
    int currentTrial;

    boolean solvingMissed = false;
    ArrayList<Integer> failedPositions = new ArrayList<Integer>();

    long startButtonClickTime, targetButtonClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        // get or initialise trial parameters
        trialIntent = getIntent();
        inputDevice = trialIntent.getStringExtra("input_type");
        if (inputDevice != null && (inputDevice.equals("Thumb") || inputDevice.equals("Index Finger"))) {
            // TODO we need to do something if this activity is called for practice.
        }

        A = trialIntent.getIntArrayExtra("A");
        W = trialIntent.getIntArrayExtra("W");
        a_pos = trialIntent.getIntExtra("a_pos", 0);
        w_pos = trialIntent.getIntExtra("w_pos", -1);
        currentTrial = trialIntent.getIntExtra("current_trial", 1);
        // we will first increment w_pos only
        // so we will get 0, 0 as in a_pos, w_pos pair. and that is why setting a_pos to 0 is required.

        failedPositions = (ArrayList<Integer>) trialIntent.getSerializableExtra("failed_positions");
        if (failedPositions == null) {
            failedPositions = new ArrayList<Integer>();
        }

        solvingMissed = trialIntent.getBooleanExtra("solving_missed", false);

        layout = (RelativeLayout) findViewById(R.id.rootLayout);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        layoutWidth = layout.getWidth();
        layoutHeight = layout.getHeight();
        layoutDiagonal = (int) Math.sqrt(Math.pow(layoutWidth, 2.0) + Math.pow(layoutHeight, 2.0));
        startMyScreen();
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void startMyScreen() {
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        getOrGenerateAW();
        if (currentTrial > MAX_TRIALS) {
            switchActivity();
        }

        startButton = new Button(this);
        startButton.setText("Text");
        startButton.setBackground(getResources().getDrawable(R.drawable.start_button));
        startButton.setLayoutParams(new RelativeLayout.LayoutParams(500, 500));
        startButton.setX(200.0f);
        startButton.setY(200.0f);
        layout.addView(startButton);

        Object ref = this;

        startButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                getStartButton().setVisibility(View.GONE);

                startButtonClickTime = System.currentTimeMillis();

                Button targetButton = new Button((Context) getReference());
                targetButton.setText("Text");
                targetButton.setBackground(getResources().getDrawable(R.drawable.target_button));
                targetButton.setLayoutParams(new RelativeLayout.LayoutParams(250, 250));
                targetButton.setX(100.0f);
                targetButton.setY(1000.0f);
                getLayout().addView(targetButton);

                paneButton = new Button((Context) getReference());
                paneButton.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                paneButton.setText("");
                paneButton.setVisibility(View.VISIBLE);
                paneButton.setBackground(getResources().getDrawable(R.drawable.invisible_color));
                getLayout().addView(paneButton);

                paneButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            targetButtonClickTime = System.currentTimeMillis();

                            if (!isMiss(event.getRawX(), event.getRawY() - (screenHeight - layoutHeight), 125, 225, 1125)) {
                                Snackbar.make(v, event.getRawX() + "  x  " + event.getRawY(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            } else {
                                setFailedPositions(a_pos);
                                setFailedPositions(w_pos);
                            }
                            switchActivity();
                        }
                        return true;
                    }
                });
            }

            private Object getReference() {
                return ref;
            }

            private RelativeLayout getLayout() {
                return layout;
            }

            private Button getStartButton() {
                return startButton;
            }

            private ArrayList<Integer> getFailedPositions() {
                return failedPositions;
            }

            private void setFailedPositions(Integer arr) {
                failedPositions.add(arr);
            }
        });
    }

    private void switchActivity() {
        if (currentTrial <= MAX_TRIALS) {
            Intent myIntent = new Intent(TrialActivity.this, TrialActivity.class);
            myIntent.putExtra("input_type", inputDevice);
            myIntent.putExtra("A", A);
            myIntent.putExtra("W", W);
            myIntent.putExtra("a_pos", a_pos);
            myIntent.putExtra("w_pos", w_pos);
            myIntent.putExtra("current_trial", currentTrial);

            myIntent.putIntegerArrayListExtra("failed_positions", failedPositions);

            myIntent.putExtra("solving_missed", solvingMissed);
            TrialActivity.this.startActivity(myIntent);
        }
        // TODO go to finish activity
    }

    private void getOrGenerateAW() {

        if (A == null || W == null) {
            A = new int[]{0, 0, 0};
            A[0] = ThreadLocalRandom.current().nextInt(20, 30 + 1) * layoutDiagonal;
            A[1] = ThreadLocalRandom.current().nextInt(35, 45 + 1) * layoutDiagonal;
            A[2] = ThreadLocalRandom.current().nextInt(50, 60 + 1) * layoutDiagonal;

            W = new int[]{0, 0, 0};
            W[0] = ThreadLocalRandom.current().nextInt(10, 15 + 1) * layoutWidth;
            W[1] = ThreadLocalRandom.current().nextInt(25, 30 + 1) * layoutWidth;
            W[2] = ThreadLocalRandom.current().nextInt(35, 40 + 1) * layoutWidth;
        }

        if (solvingMissed) {
            if (failedPositions.isEmpty()) {
                solvingMissed = false;
                currentTrial += 1;
                w_pos = 0;
                a_pos = 0;
                return;
            }
            a_pos = failedPositions.get(0);
            failedPositions.remove(0);

            w_pos = failedPositions.get(0);
            failedPositions.remove(0);

            return;
        }

        w_pos += 1;
        if (w_pos >= 3) {
            w_pos = 0;
            a_pos += 1;
            if (a_pos >= 3) {
                a_pos = 0;
                if (failedPositions.isEmpty()) {
                    currentTrial += 1;
                } else {
                    a_pos = failedPositions.get(0);
                    failedPositions.remove(0);
                    w_pos = failedPositions.get(0);
                    failedPositions.remove(0);
                    solvingMissed = true;
                }
            }
        }
    }

    public boolean isMiss(double rawX, double rawY, int radius, int center_x, int center_y) {

        double d = Math.pow(radius, 2.0) - (Math.pow(center_x - rawX, 2.0) + Math.pow(center_y - rawY, 2.0));

        return d < 0.0;
    }
}
