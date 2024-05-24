package com.cornerstonehospice.android.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cornerstonehospice.R;
import com.cornerstonehospice.android.api.builders.CriteriaBuilder;
import com.cornerstonehospice.android.api.requests.CriteriaDataRequest;
import com.cornerstonehospice.android.api.results.CriteriaDataResult;
import com.cornerstonehospice.android.fragments.AboutUsFragment;
import com.cornerstonehospice.android.fragments.CriteriaListFragment;
import com.cornerstonehospice.android.fragments.ReferFragment;
import com.cornerstonehospice.android.manager.SharedPreferenceManager;
import com.cornerstonehospice.android.utils.AppConstants;
import com.cornerstonehospice.android.utils.NavItem;
import com.newsstand.SlidingViewPagerFragment;
import com.we.common.api.data.results.DataResult;
import com.we.common.async.DataApiAsyncTask;
import com.we.common.builders.json.CommonJsonBuilder;
import com.we.common.utils.WELogger;

import java.util.ArrayList;

/**
 * Main dashboard where doctor will control navigations from here.
 *
 * @author shashi
 */

public class MainDashboardActivity extends AppCompatActivity {

    private static String TAG = MainDashboardActivity.class.getName();
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private int mCurrentFragment = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WELogger.infoLog(TAG, "onCreate :: inActivity ");
        setContentView(R.layout.activity_dashboard);
        // setContentView(R.layout.activity_main_dashboard_land);
        //  setContentView(R.layout.activity_main_dashboard_port);
        setUpActionBar();

        getExtrasFromMainActivity();
     /*   if (isPhone()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setUpDrawer();

        } */

        getContactInfo();
    }

    private void getExtrasFromMainActivity() {
        Intent fromMainActivity = getIntent();
        selectItemFromDrawer(fromMainActivity.getIntExtra("selectFragment", 0));
    }

    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.target_fragment).getWindowToken(), 0);
    }

    private void selectItemFromDrawer(int position) {
        Fragment frag = new ReferFragment();
        switch (position) {
            case 0:
                frag = ReferFragment.newInstance(0);
                getSupportActionBar().setTitle("REFER");
                navigateToFragment(frag);
                break;
            case 1:
                frag = CriteriaListFragment.newInstance(2);
                getSupportActionBar().setTitle("CRITERIA");
                navigateToFragment(frag);
                break;
            case 2:
                frag = SlidingViewPagerFragment.newInstance(1);
                WELogger.infoLog(TAG, "position " + position + " frag ::" + frag);
                getSupportActionBar().setTitle("TOOLS");
                navigateToFragment(frag);
                break;
            case 3:
                frag = AboutUsFragment.newInstance();
                getSupportActionBar().setTitle("ABOUT US");
                navigateToFragment(frag);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpActionBar() {
        ActionBar bar = getSupportActionBar();
        ColorDrawable mActionBarColorDrawable = new ColorDrawable(getResources().getColor(R.color.cornerstone_blue4));
        assert bar != null;
        bar.setBackgroundDrawable(mActionBarColorDrawable);
        bar.setTitle(getResources().getString(R.string.dashboard_dashboard_title_txt));
        bar.setDisplayHomeAsUpEnabled(true);

    }

    public void navigateToFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.target_fragment, fragment, fragment.getClass().getSimpleName()).commit();
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    public void onDashboardClick(View view) {
        WELogger.infoLog(TAG, "onDashboardClick() :: Dashboard is clicked");
    }

    private void getCriteria() {
        WELogger.infoLog(TAG, "getCriteriaInfo() :: Getting criteria information from Server");
        CriteriaDataRequest contactRequest = new CriteriaDataRequest(getApplicationContext());
        contactRequest.requestDelegate = new CriteriaBuilder();
        contactRequest.requestType = CriteriaBuilder.RequestType.GET_CRITERIA;
        new DataApiAsyncTask(true, this, criteriaAPIHandler, getProgressDialog()).execute(contactRequest);

    }

    public void onFacebookClick(View v) {
        Uri uri = Uri.parse(AppConstants.FB_LINK); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private ProgressDialog getProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        return progressDialog;
    }

    public void call() {
        SharedPreferenceManager sharedPref = SharedPreferenceManager.getInstance();
        String phoneNo = sharedPref.getString(AppConstants.KEY_PHONE_NO, AppConstants.DEFAULT_STRING);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
        startActivity(intent);
    }

    public void sendEmail() {
        SharedPreferenceManager sharedPref = SharedPreferenceManager.getInstance();
        String email = sharedPref.getString(AppConstants.KEY_EMAIL, AppConstants.DEFAULT_STRING);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Conrnerstone referral");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainDashboardActivity.this, getString(R.string.no_email_launcher_defined), Toast.LENGTH_SHORT).show();
        }
    }

    private void getContactInfo() {
        SharedPreferenceManager sharedPref = SharedPreferenceManager.getInstance();
        sharedPref.saveString(AppConstants.KEY_EMAIL, AppConstants.CORNERSTONE_CONSTANT_EMAIL);
        sharedPref.saveString(AppConstants.KEY_PHONE_NO, AppConstants.CORNERSTONE_CONSTANT_PHONE_NUMBER);
    }

    private final Handler criteriaAPIHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            DataResult<CriteriaDataResult> criteriaResults = (DataResult<CriteriaDataResult>) msg.obj;
            String str = CommonJsonBuilder.getJsonForEntity(criteriaResults.entity);
            navigateToCriteriaListActivity(str);
        }
    };

    private void navigateToCriteriaListActivity(String payload) {
        Intent i = new Intent(this, CriteriaListActivity.class);
        i.putExtra(AppConstants.CRITERIA_DATA, payload);
        startActivity(i);
    }

}
