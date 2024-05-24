package com.cornerstonehospice.android.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cornerstonehospice.R;
import com.newsstand.ScrollTabHolderFragment;
import com.we.common.utils.WELogger;

import java.text.DecimalFormat;

public class MELDFragment extends ScrollTabHolderFragment implements OnScrollListener {

    private static final String ARG_POSITION = "position";

    private ListView mListView;
    private int mPosition;

    private static String TAG = MELDFragment.class.getName();

    private EditText mBilirubinET;
    private EditText mCreatinineET;
    private EditText mInrET;
    private RadioGroup mDialysisRadioGroup;
    private RadioButton yesButton;
    private RadioButton noButton;
    private TextView meldText;
    private TextView meldValue;


    public static Fragment newInstance(int position) {
        MELDFragment f = new MELDFragment();
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
                vi = inflater.inflate(R.layout.activity_meld, null);
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
        mBilirubinET = (EditText) v.findViewById(R.id.meld_bilirubin);
        mCreatinineET = (EditText) v.findViewById(R.id.meld_serum_creatinine);
        mInrET = (EditText) v.findViewById(R.id.meld_INR);
        mDialysisRadioGroup = (RadioGroup) v.findViewById(R.id.meld_radio_dialysis);
        yesButton = (RadioButton) v.findViewById(R.id.radioYes);
        noButton = (RadioButton) v.findViewById(R.id.radioNo);
        meldValue = (TextView) v.findViewById(R.id.meld_result_tv);
        meldText = (TextView) v.findViewById(R.id.meld_result_text);

        v.findViewById(R.id.meld_calcluate_meld_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCalculateMELD();
            }
        });

        v.findViewById(R.id.meld_reset_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUI();
            }
        });

    }

    private void clearUI() {
        mBilirubinET.setText("");
        mCreatinineET.setText("");
        mInrET.setText("");
        mCreatinineET.requestFocus();
        meldValue.setText("");
        meldText.setText("");
    }

    private void setUpUI() {
//		getToolBar().setTitle(getString(R.string.bmi_screen_title));
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //   mHeightTV.setText(mHeightSeekBar.getProgress());

        //     mWeightTV.setText(mWeightSeekbar.getProgress());
    }

    private void setListenersToViews() {


    }

    public double getConvertedValue(int intVal, float factor) {
        double floatVal = 0.0;
        floatVal = factor * intVal;
        return floatVal;
    }

    public void onCalculateMELD() {
        double creatinine, bilirubin, inr;
        double MELD;
        if (mCreatinineET.getText().length() != 0 && mInrET.getText().length() != 0  && mBilirubinET.getText().length() != 0  && mInrET.getText().length() != 0) {

            if (mDialysisRadioGroup.getCheckedRadioButtonId() == yesButton.getId()) {
                creatinine = 4.0f;
            } else {
                creatinine = Double.parseDouble(mCreatinineET.getText().toString());
            }
            bilirubin = Double.parseDouble(mBilirubinET.getText().toString());
            inr = Double.parseDouble(mInrET.getText().toString());
            // MELD Score = 10 * ((0.957 * ln(Creatinine)) + (0.378 * ln(Bilirubin)) + (1.12 * ln(INR))) + 6.43
            MELD = (9.57 * Math.log(creatinine) + 3.78 * Math.log(bilirubin) + 11.2 * Math.log((inr)) + 6.43);
            WELogger.infoLog(TAG, "  9.57 * " + Math.log(creatinine)+" 3.78 * "+ Math.log(bilirubin)+" 11.2  * "+ Math.log(inr));
            WELogger.infoLog(TAG, "  9.57 * Math.log(creatinine)  = " + 9.57 * Math.log(creatinine));
            WELogger.infoLog(TAG, " OLD MELD "+ MELD);
            WELogger.infoLog(TAG, " MELD "+ new DecimalFormat("#.#").format(Math.ceil(MELD)));
                    meldValue.setText("MELD Score: " + (new DecimalFormat("#.#").format(Math.ceil(MELD))));
            setMELDText(MELD);
        }else{
            Toast.makeText(getActivity(), "Please fill the required fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void setMELDText(double valueMELD) {
		/*
		40 or more — 71.3% mortality
		30–39 — 52.6% mortality
		20–29 — 19.6% mortality
		10–19 — 6.0% mortality
		<9 — 1.9% mortality
		 */

//		mBMIResultTextTV.setText(setBMIText(valueMELD));

		if (valueMELD > 40) {
            meldText.setTextColor(getResources().getColor(R.color.Red));
            meldText.setText("71.3% ");
		}
		else if (valueMELD > 30) {
            meldText.setTextColor(getResources().getColor(R.color.Red));
            meldText.setText("52.6% ");
		}
		else if (valueMELD >20) {
            meldText.setTextColor(getResources().getColor(R.color.Orange));
            meldText.setText("19.6% ");
		}
		else if (valueMELD >10) {
            meldText.setTextColor(getResources().getColor(R.color.LimeGreen));
            meldText.setText("6.0% ");
		}
		else {
            meldText.setTextColor(getResources().getColor(R.color.LimeGreen));
            meldText.setText("1.9% ");
		}
    }

}