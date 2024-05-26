package com.cornerstonehospice.android.activities;


import static com.google.gson.internal.$Gson$Types.arrayOf;

import android.Manifest;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.cornerstonehospice.R;
import com.cornerstonehospice.android.manager.SharedPreferenceManager;
import com.cornerstonehospice.android.utils.AppConstants;
import com.we.common.utils.WELogger;

/**
 * Created by Venkatesh on 11/24/2015.
 */
public class MainActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = MainActivity.class.getName();

    private LinearLayout referralButton;
    private LinearLayout criteriaButton;
    private LinearLayout toolsButton;
    private LinearLayout aboutUsButton;
    private ImageButton getContact_call_button;
    private ImageButton getContact_email_button;
    private ImageButton getContact_faceBook_button;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main_dashboard_port;
    }

    @Override
    protected int getToolbarResource() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar();
        initViews();
        getContactInfo();

    }

    private void initViews() {

        referralButton = (LinearLayout) findViewById(R.id.referral_layout_id);
        criteriaButton = (LinearLayout) findViewById(R.id.criteria_layout_id);
        toolsButton = (LinearLayout) findViewById(R.id.tools_layout_id);
        aboutUsButton = (LinearLayout) findViewById(R.id.aboutus_layout_id);
        getContact_call_button = (ImageButton) findViewById(R.id.call_button);
        getContact_email_button = (ImageButton) findViewById(R.id.email_button);
        getContact_faceBook_button = (ImageButton) findViewById(R.id.facebook_button);

        referralButton.setOnClickListener(this);
        criteriaButton.setOnClickListener(this);
        toolsButton.setOnClickListener(this);
        aboutUsButton.setOnClickListener(this);
        getContact_call_button.setOnClickListener(this);
        getContact_email_button.setOnClickListener(this);
        getContact_faceBook_button.setOnClickListener(this);

    }

    private void setUpActionBar() {
        ActionBar bar = getActionBar();
        WELogger.infoLog(TAG, "Action bar :: " + bar);
        ColorDrawable mActionBarColorDrawable = new ColorDrawable(getResources().getColor(R.color.cornerstone_blue4));
        bar.setBackgroundDrawable(mActionBarColorDrawable);
        bar.setIcon(R.color.transparent);
        bar.setDisplayShowHomeEnabled(true);

    }


    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.referral_layout_id) {
            NavigateToMainDashBoardActivity(AppConstants.navigateToReferralFragment);
        } else if (v.getId() == R.id.criteria_layout_id) {
            NavigateToMainDashBoardActivity(AppConstants.navigateToCriteriaFragment);
        } else if (v.getId() == R.id.tools_layout_id) {
            NavigateToMainDashBoardActivity(AppConstants.navigateToToolsFragment);
        } else if (v.getId() == R.id.aboutus_layout_id) {
            NavigateToMainDashBoardActivity(AppConstants.navigateToAboutusFragment);
        } else if (v.getId() == R.id.call_button) {
            call();
        } else if (v.getId() == R.id.email_button) {
            sendEmail();
        } else if (v.getId() == R.id.facebook_button) {
            onFacebookClick();
        } else {
            System.out.println("Nothing");
        }
    }

    private void NavigateToMainDashBoardActivity(int getPositionOfFragmentToLoad) {

        Intent mainDashBoard = new Intent(MainActivity.this, MainDashboardActivity.class);
        mainDashBoard.putExtra("selectFragment", getPositionOfFragmentToLoad);
        startActivity(mainDashBoard);
    }

    public void call() {
        SharedPreferenceManager sharedPref = SharedPreferenceManager.getInstance();
        String phoneNo = sharedPref.getString(AppConstants.KEY_PHONE_NO, AppConstants.DEFAULT_STRING);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, 99);
        }else{
            startActivity(intent);
        }

    }

    public void sendEmail() {
        SharedPreferenceManager sharedPref = SharedPreferenceManager.getInstance();
        String email = sharedPref.getString(AppConstants.KEY_EMAIL, AppConstants.DEFAULT_STRING);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.cornerstone_referral_email_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, getString(R.string.no_email_launcher_defined), Toast.LENGTH_SHORT).show();
        }
    }

    public void onFacebookClick() {
        Uri uri = Uri.parse(AppConstants.FB_LINK); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        showApplicationEndDialog();
    }

    private void showApplicationEndDialog() {
        AlertDialog.Builder endDialog = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        endDialog.setMessage("Do you want to exit?");

        endDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        endDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        endDialog.create().show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main_dashboard_port);
        initViews();
    }

    private void getContactInfo() {
        SharedPreferenceManager sharedPref = SharedPreferenceManager.getInstance();
        sharedPref.saveString(AppConstants.KEY_EMAIL, AppConstants.CORNERSTONE_CONSTANT_EMAIL);
        sharedPref.saveString(AppConstants.KEY_PHONE_NO, AppConstants.CORNERSTONE_CONSTANT_PHONE_NUMBER);
    }

}
