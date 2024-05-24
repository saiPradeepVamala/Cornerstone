package com.cornerstonehospice.android.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cornerstonehospice.R;
import com.cornerstonehospice.android.api.results.Criteria;
import com.cornerstonehospice.android.api.results.Criterion;
import com.we.common.builders.json.CommonJsonBuilder;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class CriteriaDetailsFragment extends Fragment {

    private Criteria mCriteria;
    private TextView mTitleTV;
    private TextView mCriterionTV;
    private LinearLayout linearLayout;


    public CriteriaDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String itemModel = getArguments().getString("ITEM", null);
        mCriteria = CommonJsonBuilder.getEntityForJson(itemModel, Criteria.class);
        View v = inflater.inflate(R.layout.fragment_criteria_list, container, false);
        initUI(v);
        return v;
    }

    @Override
    public void onStart() {
        setTextToUI();
        super.onStart();
    }



    private void setTextToUI() {
        if (!(mCriteria.criterion.size() > 1)) {
            mTitleTV.setText(mCriteria.criterionTitle);
            mCriterionTV.setText(getIndicationsListAsString(mCriteria.criterion.get(0).indication));
        } else {
            mTitleTV.setText(mCriteria.criterionTitle);
            mCriterionTV.setVisibility(View.GONE);
            if(linearLayout.getChildCount()>1) {
                linearLayout.removeViews(1,linearLayout.getChildCount()-1);
            }
            int viewIndex = 1;
            for (Criterion crit : mCriteria.criterion) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.criteria_cell, null);
                TextView titleTV = (TextView) v.findViewById(R.id.criteria_heading);
                TextView indicatorTV = ((TextView) v.findViewById(R.id.criterion));

                titleTV.setTextSize((getResources().getDimensionPixelSize(R.dimen.textSizeMicro))/2);
                titleTV.setText(crit.indicators_subtitle);
                indicatorTV.setText(getIndicationsListAsString(crit.indication));
                linearLayout.addView(v, viewIndex++);
            }
        }
    }

    private String getIndicationsListAsString(List<String> indications) {
        String sb = "";
        for (String str : indications) {
            sb = sb.concat(str + System.getProperty("line.separator") + System.getProperty("line.separator"));
        }
        return sb;
    }

    private void initUI(View fragmentView) {
        mTitleTV = (TextView) fragmentView.findViewById(R.id.criteria_heading);
        mCriterionTV = (TextView) fragmentView.findViewById(R.id.criterion);
        linearLayout = (LinearLayout) fragmentView.findViewById(R.id.rootView);
    }
}
