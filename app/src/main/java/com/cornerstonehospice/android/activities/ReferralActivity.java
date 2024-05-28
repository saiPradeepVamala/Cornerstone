package com.cornerstonehospice.android.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.cornerstonehospice.R;
import com.cornerstonehospice.android.api.builders.ReferralBuilder;
import com.cornerstonehospice.android.api.requests.ReferalDataRequest;
import com.cornerstonehospice.android.json.ReferralBean;
import com.we.common.api.data.results.DataResult;
import com.we.common.async.DataApiAsyncTask;
import com.we.common.utils.WELogger;


import java.util.Objects;

/**
 * Main dashboard where doctor will control navigations from here.
 *
 * @author shashi
 */

public class ReferralActivity extends BaseActivity {

    private static String TAG = ReferralActivity.class.getName();

    private EditText mPatientName;
    private EditText mPatientPhone;
    private EditText mPatientDiagnosis;
    private EditText mReferralName;
    private EditText mReferralPhone;
    private EditText mReferralRelation;
    private Switch mCanCallSwitch;
    private EditText mComments;
    private ProgressDialog progressDialog;
    private boolean mIsCalledToCorner;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_referral;
    }

    @Override
    protected int getToolbarResource() {
        //  return R.id.toolbar;
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WELogger.infoLog(TAG, "onCreate :: inActivity ");
        setUpIdsToViews();
        setUpUI();
        setListnersToViews();
    }

    private void setListnersToViews() {
        mCanCallSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                WELogger.infoLog(TAG, "Switch check changed " + isChecked);
                mIsCalledToCorner = isChecked;
            }
        });
    }

    private void setUpUI() {
        Toolbar toolBar = getToolBar();
        toolBar.setTitle(getString(R.string.dashboard_make_a_referal_txt));

        Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);
    }


    private void setUpIdsToViews() {

        mPatientName = (EditText) findViewById(R.id.makereferral_patient_name);
        mPatientPhone = (EditText) findViewById(R.id.makereferral_patient_phone);
        mPatientDiagnosis = (EditText) findViewById(R.id.makereferral_patient_diagnosis);
        mReferralName = (EditText) findViewById(R.id.makereferral_your_name);
        mReferralPhone = (EditText) findViewById(R.id.makereferral_your_phone);
        mReferralRelation = (EditText) findViewById(R.id.makereferral_your_relation);

//        mCanCallSwitch = (Switch) findViewById(R.id.make_referal_call_switch);
        mComments = (EditText) findViewById(R.id.makereferral_comments_edit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_referral, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.menu_submit_referral) {
            if (isValidateData()) {
                showSubmitDialog();
                Log.d("ValidData","Data is valid");
            } else {
                showLongToast(getString(R.string.makereferral_please_check_the_mandatory_fileds));
            }
//            referralSubmission();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showSubmitDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.make_referral_submit_title)
                .setMessage(R.string.make_referral_submit_message)
                .setPositiveButton(R.string.yes_button, (arg0, arg1) -> {
                    checkMandatoryFields();
                    referralSubmission();
                })
                .setNegativeButton(R.string.no_button, (dialog1, which) -> {
                    /*DO_NOTHING*/
                }).show();
    }

    private boolean checkMandatoryFields() {
        return false;
    }

    private void referralSubmission() {

        progressDialog = getProgressDialog(getString(R.string.makereferral_submitting_referral));
        ReferralBean referData = getDataFromUI();
        progressDialog.show();
//            Will send data via Retrofit API
            ReferalDataRequest request = new ReferalDataRequest();
            request.requestDelegate = new ReferralBuilder();
//            request.requestType = ReferralBuilder.RequestType.postReferal;
//            request.referral = referData;
//            new DataApiAsyncTask(true, this, submissionHandler, null).execute(request);


    }

    private final Handler submissionHandler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {
            DataResult<ReferralBean> referralResult = (DataResult<ReferralBean>) msg.obj;
            progressDialog.dismiss();
            if (referralResult.successful) {        //Process success
                WELogger.infoLog(TAG, "submissionHandler :: Referal results Patinet Name - " + referralResult.entity.patientName);
                Toast.makeText(ReferralActivity.this, getString(R.string.make_referral_success), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                //Process failure and show the proper message to user
                showLongToast(getString(R.string.cannot_process_requst));
            }
        }
    };

//    Redundant
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//    }

    private boolean isValidateData() {
        String patientName = mPatientName.getText().toString();
        String patientPhone = mPatientPhone.getText().toString();
        String referralName = mReferralName.getText().toString();
        String referralPhone = mReferralPhone.getText().toString();
        String referralRelation = mReferralRelation.getText().toString();
        boolean isMandatoryFiledsFilled = !TextUtils.isEmpty(patientName) && !TextUtils.isEmpty(patientPhone) && !TextUtils.isEmpty(referralName) && !TextUtils.isEmpty(referralPhone) && !TextUtils.isEmpty(referralRelation);
        WELogger.infoLog(TAG, "getDataFromUI() :: Mandatory fileds check : " + isMandatoryFiledsFilled);
        return isMandatoryFiledsFilled;
    }

    private ReferralBean getDataFromUI() {
        ReferralBean referalBean = null;
        referalBean = new ReferralBean();
        referalBean.id = "5583dfb4d8f4a21a71ea1087";             //Fixme : Why is this static?
        referalBean.patientName = mPatientName.getText().toString();
        referalBean.patientPhone = mPatientPhone.getText().toString();
        referalBean.patientDiagnosis = mPatientDiagnosis.getText().toString();
        referalBean.referralName = mReferralName.getText().toString();
        referalBean.referralPhone = mReferralPhone.getText().toString();
        referalBean.referralRelationToPatient = mReferralRelation.getText().toString();
        referalBean.additionalNotes = mComments.getText().toString();
        referalBean.canCall = mIsCalledToCorner;

        return referalBean;
    }
}



