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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TrialActivity extends AppCompatActivity {

    RelativeLayout layout;
    Button paneButton, startButton;
    Intent trialIntent;
    DisplayMetrics displayMetrics;

    int screenWidth, screenHeight, layoutWidth, layoutHeight, layoutDiagonal;
    int START_BUTTON_WIDTH = 170;

    String inputDevice;
    int[] A = new int[]{0, 0, 0};
    int[] W = new int[]{0, 0, 0};
    int a_pos, w_pos;
    int MAX_TRIALS = 10;
    int currentTrial;

    double target_touch_x, target_touch_y;

    boolean solvingMissed = false;
    ArrayList<Integer> failedPositions = new ArrayList<Integer>();

    long startButtonClickTime, targetButtonClickTime;

    // for randomness
    List<Integer> thetaList;

    {
        thetaList = new ArrayList<Integer>();
        for (int i = 0; i < 180; i = i + 15) {
            thetaList.add(i);
        }
    }

    int startButtonX, startButtonY, targetButtonX, targetButtonY, wBound;
    int target_button_center_X, target_button_center_Y;
    int firstX = 0, firstY = 0, secondX = 0, secondY = 0;

    boolean isTouchEventHandled = false;

    DatabaseHelper db_helper;
    boolean isActualTrial = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_trial);

        // get or initialise trial parameters
        trialIntent = getIntent();
        inputDevice = trialIntent.getStringExtra("input_type");
        if (inputDevice != null && (inputDevice.equals("Thumb") || inputDevice.equals("Index finger"))) {
            isActualTrial = true;
        }

        if (isActualTrial) {
            db_helper = new DatabaseHelper(this);
        }
        else {
            MAX_TRIALS = 1;
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

        // random positions
        loadRandomPositions();

        startButton = new Button(this);
        startButton.setText("");
        startButton.setBackground(getResources().getDrawable(R.drawable.start_button));
        startButton.setLayoutParams(new RelativeLayout.LayoutParams(START_BUTTON_WIDTH, START_BUTTON_WIDTH));
        startButton.setX(startButtonX);
        startButton.setY(startButtonY);
        layout.addView(startButton);

        Object ref = this;

        startButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                getStartButton().setVisibility(View.GONE);
//                layout.removeView(startButton);

                startButtonClickTime = System.currentTimeMillis();

                Button targetButton = new Button((Context) getReference());
                targetButton.setText("");
                targetButton.setBackground(getResources().getDrawable(R.drawable.target_button));
                targetButton.setLayoutParams(new RelativeLayout.LayoutParams(W[w_pos], W[w_pos]));
                targetButton.setX(targetButtonX);
                targetButton.setY(targetButtonY);
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
                        if (isTouchEventHandled) {
                            return false;
                        }
                        isTouchEventHandled = true;

                        targetButtonClickTime = System.currentTimeMillis();
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            target_touch_x = event.getRawX();
//                            target_touch_y = event.getRawY() - (screenHeight - layoutHeight);
                            target_touch_y = event.getRawY(); // since we are using full-screen
                            if (!isMiss(target_touch_x, target_touch_y, W[w_pos] / 2, target_button_center_X, target_button_center_Y)) {
                                if (isActualTrial) {     // do not write to DB in practice mode
                                    writeDataToDB(0);
                                }
                            } else {
                                setFailedPositions(a_pos);
                                setFailedPositions(w_pos);
                                if (isActualTrial) {     // do not write to DB in practice mode
                                    writeDataToDB(1);
                                }
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
        } else {
            Intent intent = new Intent(TrialActivity.this, AfterSubmitActivity.class);
            intent.putExtra("input_type", inputDevice);
            intent.putExtra("is_actual_trial", isActualTrial);
            startActivity(intent);
        }
    }

    private void getOrGenerateAW() {

        if (A == null || W == null) {
            if (isActualTrial) {
                db_helper.deleteAllTrialDataEntry();
            }

            A = new int[]{0, 0, 0};
            A[0] = (int) ((ThreadLocalRandom.current().nextInt(20, 30 + 1) * layoutDiagonal) * 0.01);
            A[1] = (int) ((ThreadLocalRandom.current().nextInt(35, 45 + 1) * layoutDiagonal) * 0.01);
            A[2] = (int) ((ThreadLocalRandom.current().nextInt(50, 60 + 1) * layoutDiagonal) * 0.01);

            W = new int[]{0, 0, 0};
            W[0] = (int) ((ThreadLocalRandom.current().nextInt(10, 15 + 1) * layoutWidth) * 0.01);
            W[1] = (int) ((ThreadLocalRandom.current().nextInt(20, 27 + 1) * layoutWidth) * 0.01);
            W[2] = (int) ((ThreadLocalRandom.current().nextInt(28, 35 + 1) * layoutWidth) * 0.01);
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

    private void loadRandomPositions() {
        int mainCircleX, mainCircleY, mainCircleR, thetaListIndex;
        double theta = 0.0;
        boolean tempMadeIt = false;

        mainCircleX = layoutWidth / 2;
        if (a_pos == 2) {
            mainCircleY = (int) (0.5 * layoutHeight);
        } else if (a_pos == 1) {
            mainCircleY = (int) ((new double[]{0.4, 0.5, 0.6})[(new Random()).nextInt(3)] * layoutHeight);
        } else {
            mainCircleY = (int) ((new double[]{0.3, 0.5, 0.7})[(new Random()).nextInt(3)] * layoutHeight);
        }

        mainCircleR = A[a_pos] / 2;

        wBound = (int) (0.5 * W[w_pos] * Math.sqrt(2.0));

        while (!tempMadeIt) {
            thetaListIndex = new Random().nextInt(thetaList.size());
            theta = Math.PI * thetaList.get(thetaListIndex) / 180.0;

            firstX = mainCircleX + ((int) (mainCircleR * Math.cos(theta)));
            firstY = mainCircleY - ((int) (mainCircleR * Math.sin(theta)));

            if (10 < (firstX + wBound) && (firstX + wBound) < (layoutWidth - 25)) {
                if (10 < (firstX - wBound) && (firstX - wBound) < (layoutWidth - 25)) {
                    if (10 < (firstY + wBound) && (firstY + wBound) < (layoutHeight - 25)) {
                        if (10 < (firstY - wBound) && (firstY - wBound) < (layoutHeight - 25)) {
                            tempMadeIt = true;
                            continue;
                        }
                    }
                }
            }

            thetaList.remove(thetaListIndex);
        }

        secondX = mainCircleX - ((int) (mainCircleR * Math.cos(theta)));
        secondY = mainCircleY + ((int) (mainCircleR * Math.sin(theta)));

        if (new Random().nextInt(2) == 0) {
            startButtonX = firstX - wBound;
            startButtonY = firstY - wBound;
            targetButtonX = secondX - wBound;
            targetButtonY = secondY - wBound;
            target_button_center_X = secondX;
            target_button_center_Y = secondY;
        } else {
            startButtonX = secondX - wBound;
            startButtonY = secondY - wBound;
            targetButtonX = firstX - wBound;
            targetButtonY = firstY - wBound;
            target_button_center_X = firstX;
            target_button_center_Y = firstY;
        }
    }

    public void writeDataToDB(int is_missed) {
        if (isActualTrial) {
            double index_of_difficulty;
            DecimalFormat df = new DecimalFormat("#.####");
            df.setRoundingMode(RoundingMode.CEILING);

            index_of_difficulty = Math.log(((1.0 * A[a_pos]) / W[w_pos]) + 1.0) / Math.log(2);

            TrialDataEntry trialDataEntry = new TrialDataEntry(
                    currentTrial, inputDevice, startButtonX + wBound, startButtonY + wBound,
                    START_BUTTON_WIDTH, target_button_center_X, target_button_center_Y,
                    W[w_pos], target_touch_x, target_touch_y, startButtonClickTime,
                    targetButtonClickTime, targetButtonClickTime - startButtonClickTime,
                    A[a_pos], Double.parseDouble(df.format(index_of_difficulty)), is_missed
            );

            db_helper.addTrialDataEntry(trialDataEntry);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
