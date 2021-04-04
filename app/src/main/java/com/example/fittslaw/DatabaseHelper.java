package com.example.fittslaw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db_fitts_law.db";
    private static final String TABLE_TRIALS = "tb_trial";

    private static final String KEY_ID = "`id`";
    private static final String KEY_TRIAL_NO = "`trial_no`";
    private static final String KEY_INPUT_DEVICE = "`input_device`";

    private static final String KEY_START_BUTTON_X = "`start_button_x`";
    private static final String KEY_START_BUTTON_Y = "`start_button_y`";
    private static final String KEY_START_BUTTON_WIDTH = "`start_button_width`";
    private static final String KEY_TARGET_BUTTON_X = "`target_button_x`";
    private static final String KEY_TARGET_BUTTON_Y = "`target_button_y`";
    private static final String KEY_TARGET_BUTTON_WIDTH = "`target_button_width`";
    private static final String KEY_TARGET_TOUCH_X = "`target_touch_x`";
    private static final String KEY_TARGET_TOUCH_Y = "`target_touch_y`";

    private static final String KEY_START_BUTTON_CLICK_TIMESTAMP = "`start_button_click_timestamp`";
    private static final String KEY_TARGET_BUTTON_CLICK_TIMESTAMP = "`target_button_click_timestamp`";
    private static final String KEY_MOVEMENT_TIME = "`movement_time`";
    private static final String KEY_AMPLITUDE = "`amplitude`";
    private static final String KEY_INDEX_OF_DIFFICULTY = "`index_of_difficulty`";

    private static final String KEY_IS_MISSED = "`is_missed`";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TRIALS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TRIAL_NO + " INTEGER,"
                + KEY_INPUT_DEVICE + " TEXT,"
                + KEY_START_BUTTON_X + "INTEGER,"
                + KEY_START_BUTTON_Y + "INTEGER,"
                + KEY_START_BUTTON_WIDTH + "INTEGER,"
                + KEY_TARGET_BUTTON_X + "INTEGER,"
                + KEY_TARGET_BUTTON_Y + "INTEGER,"
                + KEY_TARGET_BUTTON_WIDTH + "INTEGER,"
                + KEY_TARGET_TOUCH_X + "REAL,"
                + KEY_TARGET_TOUCH_Y + "REAL,"
                + KEY_START_BUTTON_CLICK_TIMESTAMP + "INTEGER,"
                + KEY_TARGET_BUTTON_CLICK_TIMESTAMP + "INTEGER,"
                + KEY_MOVEMENT_TIME + "INTEGER,"
                + KEY_AMPLITUDE + "INTEGER,"
                + KEY_INDEX_OF_DIFFICULTY + "REAL,"
                + KEY_IS_MISSED + "INTEGER"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIALS);
        // Create tables again
        onCreate(db);
    }

    void addTrialDataEntry(TrialDataEntry trialDataEntry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TRIAL_NO, trialDataEntry.trial_no);
        values.put(KEY_INPUT_DEVICE, trialDataEntry.input_device);
        values.put(KEY_START_BUTTON_X, trialDataEntry.start_button_x);
        values.put(KEY_START_BUTTON_Y, trialDataEntry.start_button_y);
        values.put(KEY_START_BUTTON_WIDTH, trialDataEntry.start_button_width);
        values.put(KEY_TARGET_BUTTON_X, trialDataEntry.target_button_x);
        values.put(KEY_TARGET_BUTTON_Y, trialDataEntry.target_button_y);
        values.put(KEY_TARGET_BUTTON_WIDTH, trialDataEntry.target_button_width);
        values.put(KEY_TARGET_TOUCH_X, trialDataEntry.target_touch_x);
        values.put(KEY_TARGET_TOUCH_Y, trialDataEntry.target_touch_y);
        values.put(KEY_START_BUTTON_CLICK_TIMESTAMP, trialDataEntry.start_button_click_timestamp);
        values.put(KEY_TARGET_BUTTON_CLICK_TIMESTAMP, trialDataEntry.target_button_click_timestamp);
        values.put(KEY_MOVEMENT_TIME, trialDataEntry.movement_time);
        values.put(KEY_AMPLITUDE, trialDataEntry.amplitude);
        values.put(KEY_INDEX_OF_DIFFICULTY, trialDataEntry.index_of_difficulty);
        values.put(KEY_IS_MISSED, trialDataEntry.is_missed);

        // Inserting Row
        db.insert(TABLE_TRIALS, null, values);
        //2nd argument is String containing nullColumnHack

        db.close(); // Closing database connection
    }

    public List<TrialDataEntry> getAllTrialDataEntry() {
        List<TrialDataEntry> contactList = new ArrayList<TrialDataEntry>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRIALS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TrialDataEntry trialDataEntry = new TrialDataEntry(
                        Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        cursor.getString(2),
                        Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)),
                        Integer.parseInt(cursor.getString(6)),
                        Integer.parseInt(cursor.getString(7)),
                        Integer.parseInt(cursor.getString(8)),
                        Double.parseDouble(cursor.getString(9)),
                        Double.parseDouble(cursor.getString(10)),
                        Long.parseLong(cursor.getString(11)),
                        Long.parseLong(cursor.getString(12)),
                        Long.parseLong(cursor.getString(13)),
                        Integer.parseInt(cursor.getString(14)),
                        Double.parseDouble(cursor.getString(15)),
                        Integer.parseInt(cursor.getString(16))
                );
                // Adding contact to list
                contactList.add(trialDataEntry);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return contact list
        return contactList;
    }

    public void deleteAllTrialDataEntry() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TRIALS);
        db.close();
    }
}
