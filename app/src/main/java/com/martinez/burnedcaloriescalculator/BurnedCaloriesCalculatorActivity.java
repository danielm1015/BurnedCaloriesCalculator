package com.martinez.burnedcaloriescalculator;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class BurnedCaloriesCalculatorActivity extends AppCompatActivity
implements AdapterView.OnItemSelectedListener{

    //define widget variables
    private EditText weightET, nameET;
    private TextView milesRanTV, burnedCaloriesTV, bmiTV;
    private SeekBar milesRanSeekBar;
    private Spinner feetSpinner, inchesSpinner;

    // define instance variables
    private String weightString = "";
    private float milesRanFloat, caloriesBurned, bmi = 0;
    private int feetNum,
            inchesNum = 0;

    private SharedPreferences savedValues;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burned_calories_calculator);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

        //reference variables
        weightET = (EditText) findViewById(R.id.weightET);
        nameET = (EditText) findViewById(R.id.nameET);
        milesRanTV = (TextView) findViewById(R.id.mileRanTV);
        burnedCaloriesTV = (TextView) findViewById(R.id.caloriesBurnedTV);
        bmiTV = (TextView) findViewById(R.id.bmiTV);
        milesRanSeekBar = (SeekBar) findViewById(R.id.milesRanSeekBar);
        feetSpinner = (Spinner) findViewById(R.id.spinnerFeet);
        inchesSpinner = (Spinner) findViewById(R.id.spinnerInches);

        //array drop down on spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.feet_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        feetSpinner.setAdapter(adapter);

         adapter = ArrayAdapter.createFromResource(
                this, R.array.inches_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        inchesSpinner.setAdapter(adapter);

        feetSpinner.setOnItemSelectedListener(this);
        inchesSpinner.setOnItemSelectedListener(this);

        //anonymous inner class as the listener
        milesRanSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                milesRanTV.setText(progress+"mi");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                calculateAndDisplay();
            }
        })
        ;
    }

    @Override
    public void onPause() {
        // save the instance variables
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("weight", weightString);
        editor.putString("mileRan", milesRanTV.getText().toString());
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // get the instance variables
        weightString = savedValues.getString("weight", "");
        String mileRan = savedValues.getString("mileRan", "");
        milesRanTV.setText(mileRan);
    }


    //event handler for EditText Action
    private EditText.OnEditorActionListener onEditTextListener = new EditText.OnEditorActionListener(){

        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                calculateAndDisplay();
            }

            // hide soft keyboard
            return false;
        }
    };


    private void calculateAndDisplay() {

        weightString = weightET.getText().toString();
        float weightFloat;
        if(weightString.equals(""))
        {
            weightFloat = 0;
        }
        else {
            weightFloat = Float.parseFloat(weightString);
        }

        int progress = milesRanSeekBar.getProgress();
        milesRanFloat = (float) progress;

        caloriesBurned = (float) (0.75* weightFloat* milesRanFloat);
        bmi = ((weightFloat* 703)/ ((12*feetNum +inchesNum)*(12 * feetNum +inchesNum)));

        burnedCaloriesTV.setText(String.valueOf(caloriesBurned));
        bmiTV.setText(String.valueOf(bmi));

        }


    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id) {
        feetNum = position + 1;
        inchesNum = position + 1;

        Toast.makeText(getApplicationContext(), "Position: " + position +
                " Value: " +feetNum, Toast.LENGTH_LONG).show();
        calculateAndDisplay();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }


}
