package com.cornerstonehospice.android.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.cornerstonehospice.R;
import com.we.common.utils.WELogger;

import java.text.DecimalFormat;

/**
 * This is screen is responsible to calculate GMI
 *
 * @author shashi
 */

public class BMIActivity extends BaseActivity implements OnClickListener {

    // Made TAG field final to prevent it from being changed
    private static final String TAG = BMIActivity.class.getName();

    private double mSelectedHeight;
    private double mSelectedWeight;
    private SeekBar mHeightSeekBar;
    private SeekBar mWeightSeekbar;
    private TextView mHeightTV;
    private TextView mWeightTV;
    private TextView mBMIResultsTV;
    private TextView mBMIResultTextTV;

    // commented out unused fields
//    private int inchesString;
//    private int weightInInches;

    private Button bmiCalcluateBtn;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_bmi;
    }

    @Override
    protected int getToolbarResource() {
        // commented out as no resource called toolbar
//         return R.id.toolbar;
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WELogger.infoLog(TAG, "onCreate :: inActivity ");
        setUpIdsToViews();
        setUpUI();
        setListenersToViews();
    }

    private void setListenersToViews() {

        mHeightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {

                progress = progresValue;
                mHeightTV.setText("" + progress);
                mSelectedHeight = getConvertedValue(progress, .1f);
                String feet = String.valueOf(progress / 10);
                String inch = String.valueOf(progress % 10);
                mHeightTV.setText(feet + " ' " + inch + " '' ");


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mHeightTV.setText("" + progress);
                mSelectedHeight = getConvertedValue(progress, .1f);
                String feet = String.valueOf(progress / 10);
                String inch = String.valueOf(progress % 10);
                mHeightTV.setText(feet + " ' " + inch + " '' ");


            }
        });


        mWeightSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                mSelectedWeight = getConvertedValue(progress, .5f);
                mWeightTV.setText("" + mSelectedWeight);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mWeightTV.setText("" + progress);
                mSelectedWeight = getConvertedValue(progress, .5f);
                mWeightTV.setText("" + mSelectedWeight);
            }
        });

    }

    public void onCalculateBMI(View view) {
//        Changing to if statement as we have a single case
        if (view.getId() == R.id.bmi_calcluate_bmi_btn) {
            double bmiValue = (mSelectedWeight * 0.453592) / ((mSelectedHeight * 30.38 / 100) * (mSelectedHeight * 30.38 / 100));
            mBMIResultsTV.setText(String.format((new DecimalFormat("#.##").format(bmiValue))));
            mBMIResultTextTV.setText(setBMIText(bmiValue));
        }
//        switch (view.getId()) {
//            case R.id.bmi_calcluate_bmi_btn:
//                double bmiValue = (mSelectedWeight * 0.453592) / ((mSelectedHeight * 30.38 / 100) * (mSelectedHeight * 30.38 / 100));
//                mBMIResultsTV.setText(String.format((new DecimalFormat("#.##").format(bmiValue))));
//                mBMIResultTextTV.setText(setBMIText(bmiValue));
//                break;
//        }
    }

    private String setBMIText(double BMIValue) {
        if (BMIValue < 18.5) {
//            Replaced getResources().getColor(R.color.Orange) with ContextCompat.getColor(this,R.color.Orange) as it is deprecated.
            mBMIResultTextTV.setTextColor(ContextCompat.getColor(this,R.color.Orange));
//                    getResources().getColor(R.color.Orange));
            return "UNDERWEIGHT";
        } else if (BMIValue < 25.0) {
            mBMIResultTextTV.setTextColor(ContextCompat.getColor(this,R.color.LimeGreen));
            return "NORMAL";
        } else if (BMIValue < 30.0) {
            mBMIResultTextTV.setTextColor(ContextCompat.getColor(this,R.color.Orange));
            return "OVERWEIGHT";
        } else {
            mBMIResultTextTV.setTextColor(ContextCompat.getColor(this,R.color.Red));
            return "OBESE";
        }
    }

    private void setUpIdsToViews() {
        // removed redundant cast
        mHeightSeekBar = findViewById(R.id.bmi_height_seekbar);
        mWeightSeekbar = findViewById(R.id.bmi_weighet_seekbar);
        mWeightTV = findViewById(R.id.bmi_weight_pounds_tv);
        mHeightTV = findViewById(R.id.bmi_height_in_feets_tv);
        mBMIResultsTV = findViewById(R.id.bmi_result_tv);
        mBMIResultTextTV = findViewById(R.id.bmi_result_text);
        bmiCalcluateBtn = findViewById(R.id.bmi_calcluate_bmi_btn);
        bmiCalcluateBtn.setOnClickListener(this);
    }

    private void setUpUI() {
         getToolBar().setTitle(getString(R.string.bmi_screen_title));
//          No specific method getSupportActionBar() in BMIActivity.java class
//         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           mHeightTV.setText(mHeightSeekBar.getProgress());
        mHeightSeekBar.setMax(102);
        mWeightSeekbar.setMax(600);
             mWeightTV.setText(mWeightSeekbar.getProgress());
    }

    public double getConvertedValue(int intVal, float factor) {
        double floatVal = 0.0;
        floatVal = factor * intVal;
        return floatVal;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // Replaced switch with if statement
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
//      Replaced switch with if statement
        if (v.getId() == R.id.bmi_calcluate_bmi_btn){
            double bmiValue = (mSelectedWeight * 0.453592) / ((mSelectedHeight * 30.38 / 100) * (mSelectedHeight * 30.38 / 100));
                mBMIResultsTV.setText(String.format((new DecimalFormat("#.##").format(bmiValue))));
                mBMIResultTextTV.setText(setBMIText(bmiValue));
        }
        if (v.getId() == R.id.bmi_calcluate_bmi_btn) {
            double bmiValue = (mSelectedWeight * 0.453592) / ((mSelectedHeight * 30.38 / 100) * (mSelectedHeight * 30.38 / 100));
            mBMIResultsTV.setText(String.format((new DecimalFormat("#.##").format(bmiValue))));
            mBMIResultTextTV.setText(setBMIText(bmiValue));
        }
    }
}
