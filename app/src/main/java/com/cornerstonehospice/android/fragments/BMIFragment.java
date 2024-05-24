package com.cornerstonehospice.android.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cornerstonehospice.R;
import com.newsstand.ScrollTabHolderFragment;
import com.we.common.utils.WELogger;

import java.text.DecimalFormat;

public class BMIFragment extends ScrollTabHolderFragment implements OnScrollListener {

    private static final String ARG_POSITION = "position";

    private ListView mListView;
    private int mPosition;

    private static String TAG = BMIFragment.class.getName();

    private double mSelectedHeight;
    private double mSelectedWeight;
    private SeekBar mHeightSeekBar;
    private SeekBar mWeightSeekbar;
    private TextView mHeightTV;
    private TextView mWeightTV;
    private TextView mBMIResultsTV;
    private TextView mBMIResultTextTV;

    public static Fragment newInstance(int position) {
        BMIFragment f = new BMIFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.headered_viewpager_fragment_list, null);

        mListView = (ListView) v.findViewById(R.id.listView);

        View placeHolderView = inflater.inflate(R.layout.headered_viewpager_header_placeholder, mListView, false);
        mListView.addHeaderView(placeHolderView);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView.setOnScrollListener(this);
//	 	mListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.headered_viewpager_list_item, android.R.id.text1, mListItems));
        mListView.setAdapter(new listAdapter(getActivity()));
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
            return;
        }

        mListView.setSelectionFromTop(1, scrollHeight);

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mScrollTabHolder != null)
            mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // nothing
    }

    public class listAdapter extends BaseAdapter {

        LayoutInflater inflater = null;

        public listAdapter(Activity a) {
            inflater = (LayoutInflater) a.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;

            if (convertView == null) {
                vi = inflater.inflate(R.layout.activity_bmi, null);
                setUpIdsToViews(vi);
                setUpUI();
                setListenersToViews();
            }
            return vi;
        }
    }

	/*
    BMI Fragment related implementation
	 */

    private void setUpIdsToViews(View v) {
        mHeightSeekBar = (SeekBar) v.findViewById(R.id.bmi_height_seekbar);
        mWeightSeekbar = (SeekBar) v.findViewById(R.id.bmi_weighet_seekbar);
        mWeightTV = (TextView) v.findViewById(R.id.bmi_weight_pounds_tv);
        mHeightTV = (TextView) v.findViewById(R.id.bmi_height_in_feets_tv);
        mBMIResultsTV = (TextView) v.findViewById(R.id.bmi_result_tv);
        mBMIResultTextTV = (TextView) v.findViewById(R.id.bmi_result_text);
        v.findViewById(R.id.bmi_calcluate_bmi_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCalculateBMI();
            }
        });
    }

    private void setUpUI() {
//		getToolBar().setTitle(getString(R.string.bmi_screen_title));
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //   mHeightTV.setText(mHeightSeekBar.getProgress());
        mHeightSeekBar.setMax(100);
        mWeightSeekbar.setMax(600);
        mHeightSeekBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        mWeightSeekbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

        //     mWeightTV.setText(mWeightSeekbar.getProgress());
    }

    private void setListenersToViews() {

        mHeightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
              //  mSelectedHeight = getConvertedValue(progress, .1f);
                mSelectedHeight = (progress *  0.0833333);
                WELogger.infoLog(TAG, "setListenersToViews() :: mSelectedHeight " + mSelectedHeight);
                String feet = String.valueOf(progress / 12);
                String inch = String.valueOf(progress % 12);
                mHeightTV.setText(feet + " ' " + inch + " ''");
                //mHeightTV.setText(String.format("%.1f", mSelectedHeight));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHeightTV.setText("" + progress);
               // mSelectedHeight = getConvertedValue(progress, .1f);
                mSelectedHeight = (progress *  0.0833333);
                WELogger.infoLog(TAG,"setListenersToViews() :: mSelectedHeight "+mSelectedHeight);
                String feet = String.valueOf(progress / 12);
                String inch = String.valueOf(progress % 12);
                mHeightTV.setText(feet + " ' " + inch + " ''");
                //mHeightTV.setText(String.format("%.1f", mSelectedHeight));
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
        mHeightSeekBar.setProgress(50);
        mWeightSeekbar.setProgress(300);
    }

    public double getConvertedValue(int intVal, float factor) {
        double floatVal = 0.0;
        floatVal = factor * intVal;
        return floatVal;
    }

    public void onCalculateBMI() {
        double bmiValue = (mSelectedWeight * 0.453592) / ((mSelectedHeight * 30.38 / 100) * (mSelectedHeight * 30.38 / 100));
        mBMIResultsTV.setText(String.format((new DecimalFormat("#.##").format(bmiValue))));
        mBMIResultTextTV.setText(setBMIText(bmiValue));
    }

    private String setBMIText(double BMIValue) {
        if (BMIValue < 18.5) {
            mBMIResultsTV.setTextColor(getResources().getColor(R.color.Orange));
            return "Underweight";
        } else if (BMIValue < 25.0) {
            mBMIResultsTV.setTextColor(getResources().getColor(R.color.LimeGreen));
            return "Normal";
        } else if (BMIValue < 30.0) {
            mBMIResultsTV.setTextColor(getResources().getColor(R.color.Orange));
            return "Overweight";
        } else {
            mBMIResultsTV.setTextColor(getResources().getColor(R.color.Red));
            return "Obese";
        }
    }

}