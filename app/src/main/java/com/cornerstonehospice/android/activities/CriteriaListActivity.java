package com.cornerstonehospice.android.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.app.Fragment;

import com.cornerstonehospice.R;
import com.cornerstonehospice.android.adapters.CriteriaListAdapter;
import com.cornerstonehospice.android.api.results.Criteria;
import com.cornerstonehospice.android.api.results.CriteriaDataResult;
import com.cornerstonehospice.android.utils.AppConstants;
import com.we.common.builders.json.CommonJsonBuilder;
import com.we.common.utils.WELogger;

public class CriteriaListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mCriteriaListView;
    private CriteriaListAdapter mAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_criteria_list;
    }

    @Override
    protected int getToolbarResource() {
        //return R.id.toolbar;
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpUI();
        initListView();
    }

    private void initListView() {
        String payload = getIntent().getStringExtra(AppConstants.CRITERIA_DATA);
        CriteriaDataResult criteriaList = (CommonJsonBuilder.getEntityForJson(payload, CriteriaDataResult.class));
        mAdapter = new CriteriaListAdapter(this);
        mAdapter.addAll(criteriaList.criteria_results);
        WELogger.infoLog("Criteria List", "Number of items: " + mAdapter.getCount());
        mCriteriaListView.setAdapter(mAdapter);
        mCriteriaListView.setOnItemClickListener(this);
    }

    private void setUpUI() {
        getToolBar().setTitle(getString(R.string.criteria_screen_title));
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        navigateToCriteriaDetailsFragment(mAdapter.getItem(position));
    }

    private void navigateToCriteriaDetailsFragment(Criteria item) {
//        CriteriaDetailsFragment fr = new CriteriaDetailsFragment();
//        Commented out and added as fragment
        Fragment fr = new Fragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Bundle b = new Bundle();
        b.putString("ITEM", CommonJsonBuilder.getJsonForEntity(item));
        fr.setArguments(b);
        ft.addToBackStack(fr.getTag());
        ft.replace(R.id.details_fragment_frame, fr, fr.getTag());
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
