package com.example.fittslaw;

public class TrialDataEntry {
    int _id;
    int trial_no;
    String input_device;

    int start_button_x, start_button_y, start_button_width;
    int target_button_x, target_button_y, target_button_width;
    double target_touch_x, target_touch_y;

    long start_button_click_timestamp, target_button_click_timestamp, movement_time;
    int amplitude;
    double index_of_difficulty;

    int is_missed;


    public TrialDataEntry() {
    }

    public TrialDataEntry(int arg__id, int arg_trial_no, String arg_input_device, int arg_start_button_x, int arg_start_button_y, int arg_start_button_width,
                          int arg_target_button_x, int arg_target_button_y, int arg_target_button_width, double arg_target_touch_x, double arg_target_touch_y,
                          long arg_start_button_click_timestamp, long arg_target_button_click_timestamp, long arg_movement_time, int arg_amplitude,
                          double arg_index_of_difficulty, int arg_is_missed) {
        this._id = arg__id;
        this.trial_no = arg_trial_no;
        this.input_device = arg_input_device;
        this.start_button_x = arg_start_button_x;
        this.start_button_y = arg_start_button_y;
        this.start_button_width = arg_start_button_width;
        this.target_button_x = arg_target_button_x;
        this.target_button_y = arg_target_button_y;
        this.target_button_width = arg_target_button_width;
        this.target_touch_x = arg_target_touch_x;
        this.target_touch_y = arg_target_touch_y;
        this.start_button_click_timestamp = arg_start_button_click_timestamp;
        this.target_button_click_timestamp = arg_target_button_click_timestamp;
        this.movement_time = arg_movement_time;
        this.amplitude = arg_amplitude;
        this.index_of_difficulty = arg_index_of_difficulty;
        this.is_missed = arg_is_missed;
    }

    public TrialDataEntry(int arg_trial_no, String arg_input_device, int arg_start_button_x, int arg_start_button_y, int arg_start_button_width,
                          int arg_target_button_x, int arg_target_button_y, int arg_target_button_width, double arg_target_touch_x, double arg_target_touch_y,
                          long arg_start_button_click_timestamp, long arg_target_button_click_timestamp, long arg_movement_time, int arg_amplitude,
                          double arg_index_of_difficulty, int arg_is_missed) {
        this.trial_no = arg_trial_no;
        this.input_device = arg_input_device;
        this.start_button_x = arg_start_button_x;
        this.start_button_y = arg_start_button_y;
        this.start_button_width = arg_start_button_width;
        this.target_button_x = arg_target_button_x;
        this.target_button_y = arg_target_button_y;
        this.target_button_width = arg_target_button_width;
        this.target_touch_x = arg_target_touch_x;
        this.target_touch_y = arg_target_touch_y;
        this.start_button_click_timestamp = arg_start_button_click_timestamp;
        this.target_button_click_timestamp = arg_target_button_click_timestamp;
        this.movement_time = arg_movement_time;
        this.amplitude = arg_amplitude;
        this.index_of_difficulty = arg_index_of_difficulty;
        this.is_missed = arg_is_missed;
    }

    public int getID() {
        return this._id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public void setTrialDataEntry(int arg_trial_no, String arg_input_device, int arg_start_button_x, int arg_start_button_y, int arg_start_button_width,
                                  int arg_target_button_x, int arg_target_button_y, int arg_target_button_width, double arg_target_touch_x, double arg_target_touch_y,
                                  long arg_start_button_click_timestamp, long arg_target_button_click_timestamp, long arg_movement_time, int arg_amplitude,
                                  double arg_index_of_difficulty, int arg_is_missed) {
        this.trial_no = arg_trial_no;
        this.input_device = arg_input_device;
        this.start_button_x = arg_start_button_x;
        this.start_button_y = arg_start_button_y;
        this.start_button_width = arg_start_button_width;
        this.target_button_x = arg_target_button_x;
        this.target_button_y = arg_target_button_y;
        this.target_button_width = arg_target_button_width;
        this.target_touch_x = arg_target_touch_x;
        this.target_touch_y = arg_target_touch_y;
        this.start_button_click_timestamp = arg_start_button_click_timestamp;
        this.target_button_click_timestamp = arg_target_button_click_timestamp;
        this.movement_time = arg_movement_time;
        this.amplitude = arg_amplitude;
        this.index_of_difficulty = arg_index_of_difficulty;
        this.is_missed = arg_is_missed;
    }
}
