package com.cornerstonehospice.android.fragments;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.cornerstonehospice.R;
import com.cornerstonehospice.android.adapters.ExpandableListAdapter;
import com.cornerstonehospice.android.api.results.Criteria;
import com.cornerstonehospice.android.api.results.CriteriaDataResult;
import com.cornerstonehospice.android.application.CornerStoneApplication;
import com.newsstand.ScrollTabHolderFragment;
import com.we.common.builders.json.CommonJsonBuilder;
import com.we.common.utils.WELogger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CriteriaListFragment extends ScrollTabHolderFragment implements OnScrollListener {

    private static final String ARG_POSITION = "position";

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private String TAG = CriteriaListFragment.class.getName();

    private int mPosition;
    private String payload;

    public static Fragment newInstance(int position) {
        CriteriaListFragment f = new CriteriaListFragment();
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

        View v = inflater.inflate(R.layout.activity_criteria_list, null);
        // get the listview
        expListView = (ExpandableListView) v.findViewById(R.id.lvExp);
        View placeHolderView = inflater.inflate(R.layout.headered_viewpager_header_placeholder, expListView, false);
        expListView.addHeaderView(placeHolderView);
        prepareExpandList();

        return v;
    }

    public void prepareExpandList() {
        prepareListData();
        if (listDataHeader!=null && !listDataHeader.isEmpty()) {
            listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);
//            expListView.getLi
            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    ImageView imageView = (ImageView) v.findViewById(R.id.arrow_img);
                    if (parent.isGroupExpanded(groupPosition)) {
                        Matrix matrix = new Matrix();
                        imageView.setScaleType(ImageView.ScaleType.MATRIX);   //required
                        matrix.postRotate((float) 0f, imageView.getDrawable().getBounds().width() / 2, imageView.getDrawable().getBounds().height() / 2);
                        imageView.setImageMatrix(matrix);
                        parent.collapseGroup(groupPosition);
                    } else {
                        Matrix matrix = new Matrix();
                        imageView.setScaleType(ImageView.ScaleType.MATRIX);   //required
                        matrix.postRotate((float) 90f, imageView.getDrawable().getBounds().width() / 2, imageView.getDrawable().getBounds().height() / 2);
                        imageView.setImageMatrix(matrix);
                        parent.expandGroup(groupPosition);
                    }
                    return true;
                }
            });
        }
    }

    private void prepareListData() {
        payload = loadJSONFromAsset(requireActivity());
//        payload = ((CornerStoneApplication) requireActivity().getApplication()).getParsedCriteria();
        WELogger.infoLog(TAG, "onCreateView() :: Parsed criteria String ; " + payload);
        CriteriaDataResult criteriaList = (CommonJsonBuilder.getEntityForJson(payload, CriteriaDataResult.class));
        if (criteriaList != null) {
            List<Criteria> criteriaGroup = criteriaList.criteria_results;
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
            for (Criteria criteria : criteriaGroup) {
                listDataHeader.add(criteria.criterionTitle);
                listDataChild.put(criteria.criterionTitle, criteria.criterion.get(0).indication);
            }
        }else{
            Log.d("LISTYI","NULL");
        }
    }


    /**
     * Function to parse a local json file from assets
     */
    private String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("criteria_results.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        expListView.setOnScrollListener(this);
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        if (scrollHeight == 0 && expListView.getFirstVisiblePosition() >= 1) {
            if (scrollHeight == 0 && expListView.getFirstVisiblePosition() >= 1) {
                return;
            }
            expListView.setSelectionFromTop(1, scrollHeight);
        }
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

}
