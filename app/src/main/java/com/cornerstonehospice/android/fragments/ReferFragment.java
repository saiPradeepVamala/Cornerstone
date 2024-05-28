package com.cornerstonehospice.android.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.cornerstonehospice.R;
import com.cornerstonehospice.android.activities.MainActivity;
import com.cornerstonehospice.android.activities.PreviewActivity;
import com.cornerstonehospice.android.activities.TakePictureActivity;
import com.cornerstonehospice.android.api.builders.ReferralBuilder;
import com.cornerstonehospice.android.api.requests.ReferalDataRequest;
import com.cornerstonehospice.android.json.ReferralBean;
import com.cornerstonehospice.android.manager.SharedPreferenceManager;
import com.cornerstonehospice.android.utils.AppConstants;
import com.we.common.api.data.results.DataResult;
import com.we.common.async.DataApiAsyncTask;
import com.we.common.utils.WELogger;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReferFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static String TAG = ReferFragment.class.getName();

    private ListView mListView;
    private ArrayList<String> mListItems;
    private ImageView mPreviewThumbnail;
    private String mFinalImagePath;
    private EditText mPatientName;
    private EditText mPatientPhone;
    private EditText mPatientDiagnosis;
    private EditText mReferralName;
    private EditText mReferralPhone;
    private EditText mReferralRelation;
    private RadioGroup mCanCallRadioGroup;
    private EditText mComments;
    private ProgressDialog progressDialog;
    private Button mClearImage;
    private TextView wouldYouLike_take_picture;

    private byte[] imageBytes;

    private int mPosition;

    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    takePicture();
                } else {
                    Log.d("Camera_Permission_Status", "Permission Denied");
                }
            });

    public static Fragment newInstance(int position) {
        ReferFragment f = new ReferFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);

        mListItems = new ArrayList<String>();

        for (int i = 1; i <= 100; i++) {
            mListItems.add(i + ". item - currnet page: " + (mPosition + 1));
        }
        setHasOptionsMenu(true);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.progress_dialog_message));
        progressDialog.setCancelable(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_referral, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_referral, null);
        setUpIdsToViews(v);
        return v;
    }


    private void launchPreviewActivity(byte[] imageBytes) {
        Intent previewActivity = new Intent(getActivity(), PreviewActivity.class);
        previewActivity.putExtra(AppConstants.PREVIEW_IMG_BYTES_KEY, imageBytes);
        startActivity(previewActivity);
    }

    public void takePicture() {
        System.out.println("take picture method is working");
//        Intent takePictureIntent = new Intent(getActivity(), CameraActivity.class);
//        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        Intent takePictureIntent = new Intent(getActivity(), TakePictureActivity.class);
        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_submit_referral) {
            dismissKeyboard();
            WELogger.infoLog(TAG, "onOpetionMenuClick() :: Subitted the refereal");
            //Send TTSL Email
            if (isValidateData()) {
                if (isValidateMobileNumber() && getActivity() != null) {
                    progressDialog.show();
                    new GetImageDataAsynTask().execute();
                } else {
                    Toast.makeText(getActivity(), "Please validate mobile number", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please fill the mandatory data.", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isValidateMobileNumber() {

        String patientPhone = mPatientPhone.getText().toString();
        String referralPhone = mReferralPhone.getText().toString();

        Pattern pattern = Pattern.compile("\\d{9,12}");
        Matcher patientPhoneMatcher = pattern.matcher(patientPhone);
        Matcher referralPhoneMatcher = pattern.matcher(referralPhone);

        if (patientPhoneMatcher.matches() && referralPhoneMatcher.matches()) {
            return true;
        } else
            return false;

        //  boolean numberIsValid = (!(patientPhone.length() < 9 ||  referralPhone.length() < 9 || PhoneNumberUtils.isGlobalPhoneNumber(patientPhone) || PhoneNumberUtils.isGlobalPhoneNumber(referralPhone)) ) ? true : false;

        // return numberIsValid;
    }

    private void SendDataAsEMail(byte[] imageData) {
        ReferralBean referData = getDataFromUI();
        ReferalDataRequest referralRequest = new ReferalDataRequest();
        if (imageBytes != null && imageBytes.length > 0) {
            referralRequest.imageBytes = imageBytes;
        } else {
            referralRequest.imageBytes = null;
        }
        referralRequest.referalRequestBody.referralBean = referData;
        referralRequest.emailRecipient = AppConstants.CORNERSTONE_EMAIL_ID;
        referralRequest.requestDelegate = new ReferralBuilder();
        referralRequest.requestType = ReferralBuilder.RequestType.POST_REFERRAL;
        if (!referralRequest.emailRecipient.isEmpty()) {
            new DataApiAsyncTask(true, getActivity(), referralApiHandler, null).execute(referralRequest);
        } else {
            Toast.makeText(getActivity(), "No email recipient.", Toast.LENGTH_SHORT).show();
        }

    }

    private final Handler referralApiHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(@NonNull android.os.Message msg) {
            DataResult<ReferralBean> dataResult = new DataResult<>();
            if (dataResult.successful) {
                Toast.makeText(getActivity(), "Sent Referral successfully", Toast.LENGTH_LONG).show();
                saveDoctorDetailsToPrefs();
                clearUI();
                getDoctorDetailsFromPrefs();
                mPatientName.requestFocus();
                // Navigating to the main activity.
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Couldn't send Referral", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }

        /* Commented out as msg.obj throws Null point exception
        public void handleMessage(android.os.Message msg) {
            DataResult<ReferralBean> result = (DataResult<ReferralBean>) msg.obj;
            Log.d("ResultStatus", String.valueOf(result.successful));

           if (result.succesesful) {
                Toast.makeText(getActivity(), "Sent Referral successfully", Toast.LENGTH_LONG).show();
                saveDoctorDetailsToPrefs();
                clearUI();
                getDoctorDetailsFromPrefs();
                mPatientName.requestFocus();
            } else {
                Toast.makeText(getActivity(), "Couldn't send Referral", Toast.LENGTH_LONG).show();
            }
                       progressDialog.dismiss();
        }*/
    };

    private void saveDoctorDetailsToPrefs() {
        SharedPreferenceManager sharedPref = SharedPreferenceManager.getInstance();
        sharedPref.saveString(AppConstants.DOCTOR_NAME, mReferralName.getText().toString());
        sharedPref.saveString(AppConstants.DOCTOR_PHONE_NUMBER, mReferralPhone.getText().toString());
        sharedPref.saveString(AppConstants.DOCTOR_RELATION_WITH_PATIENT, mReferralRelation.getText().toString());
    }

    private void getDoctorDetailsFromPrefs() {
        SharedPreferenceManager sharedPref = SharedPreferenceManager.getInstance();
        mReferralName.setText(sharedPref.getString(AppConstants.DOCTOR_NAME, ""));
        mReferralPhone.setText(sharedPref.getString(AppConstants.DOCTOR_PHONE_NUMBER, ""));
        mReferralRelation.setText(sharedPref.getString(AppConstants.DOCTOR_RELATION_WITH_PATIENT, ""));
    }

    public byte[] getImageData() {
        Bitmap bmp = BitmapFactory.decodeFile(mFinalImagePath);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void clearUI() {
        mPatientName.setText("");
        mPatientPhone.setText("");
        mPatientDiagnosis.setText("");
        mReferralName.setText("");
        mReferralPhone.setText("");
        mReferralRelation.setText("");
        mComments.setText("");
        clearCapturedImage();
    }

    private String buildBodyString(ReferralBean referData) {

        String bodyMessage = "";
        bodyMessage = bodyMessage + "PatientName : " + referData.patientName + "\n" +
                "PatientDiagnosis : " + referData.patientDiagnosis + "\n" +
                "PatientPhoneNumber : " + referData.patientPhone + "\n" +
                "Comments : " + referData.additionalNotes + "\n" +
                "Referral Name : " + referData.referralName + "\n" +
                "Referral Number : " + referData.referralPhone + "\n" +
                "RelationShip : " + referData.referralRelationToPatient + "\n";

        return bodyMessage;
    }

    // In your Activity or Fragment




    private void setUpIdsToViews(View view) {

        mPatientName = (EditText) view.findViewById(R.id.makereferral_patient_name);
        mPatientPhone = (EditText) view.findViewById(R.id.makereferral_patient_phone);
        mPatientDiagnosis = (EditText) view.findViewById(R.id.makereferral_patient_diagnosis);
        mReferralName = (EditText) view.findViewById(R.id.makereferral_your_name);
        mReferralPhone = (EditText) view.findViewById(R.id.makereferral_your_phone);
        mReferralRelation = (EditText) view.findViewById(R.id.makereferral_your_relation);
        mCanCallRadioGroup = (RadioGroup) view.findViewById(R.id.referral_radio_group);
        mComments = (EditText) view.findViewById(R.id.makereferral_comments_edit);
        mClearImage = (Button) view.findViewById(R.id.remove_image);
        mClearImage.setVisibility(View.GONE);
        mPreviewThumbnail = (ImageView) view.findViewById(R.id.refferal_thumb_iv);
        wouldYouLike_take_picture = (TextView) view.findViewById(R.id.would_like_take_picture);
        /****** View Holder Object to contain tabitem.xml file elements ******/
        view.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
                    } else {
                        takePicture();
                    }
                }
            }
        });
        setThumbnailImage();
        getDoctorDetailsFromPrefs();
    }

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
        referalBean.canCall = (mCanCallRadioGroup.getCheckedRadioButtonId() == R.id.referral_radioYes);
        return referalBean;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        int RESULT_OK = Activity.RESULT_OK;

        saveDoctorDetailsToPrefs();
        getDoctorDetailsFromPrefs();

        WELogger.infoLog(TAG, "onActivityResult() :: resultCode : " + resultCode + " requestCode : " + requestCode);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            WELogger.infoLog(TAG, "onActivityResult() :: Request code is CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE");
            if (resultCode == RESULT_OK) {
                WELogger.infoLog(TAG, "onActivityResult() :: Result code is RESULT_OK");
                if (data.hasExtra(AppConstants.CAMERA_IMAGE_BYTES_KEY)) {
                    imageBytes = data.getByteArrayExtra(AppConstants.CAMERA_IMAGE_BYTES_KEY);
                    if (mPreviewThumbnail != null) {
                        setThumbnailImage();
                    } else {
                        Toast.makeText(getActivity(), "Error processing image, Retake", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setThumbnailImage() {
        if (imageBytes != null) {
            WELogger.infoLog(TAG, "setImageToTHumbnail() :: Imagebytes size : " + imageBytes.length);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            mPreviewThumbnail.setImageBitmap(bitmap);
            mPreviewThumbnail.setBackgroundResource(R.color.transparent);
            mPreviewThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageBytes != null) {
                        launchPreviewActivity(imageBytes);
                    }
                }
            });
            mPreviewThumbnail.setVisibility(View.VISIBLE);
            mClearImage.setVisibility(View.VISIBLE);
            wouldYouLike_take_picture.setVisibility(View.GONE);
            mClearImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearCapturedImage();
                }
            });
        } else {
        }
    }

    private void clearCapturedImage() {
        imageBytes = null;
        mPreviewThumbnail.setVisibility(View.GONE);
        mClearImage.setVisibility(View.GONE);
        wouldYouLike_take_picture.setVisibility(View.VISIBLE);
        mPreviewThumbnail.setImageBitmap(null);
        mPreviewThumbnail.setBackgroundResource(R.color.light_gray);
        getDoctorDetailsFromPrefs();
    }


    private void setImageThumb(String imageURL) {
        Glide.with(getActivity()).load(imageURL).thumbnail(0.1f).into(mPreviewThumbnail);
    }


    public class GetImageDataAsynTask extends AsyncTask<String, Void, String> {

        public GetImageDataAsynTask() {
        }

        @Override
        protected String doInBackground(String... params) {
//            byte[] imageBytes = getImageData();
            SendDataAsEMail(imageBytes);
            return null;
        }

    }

    public void dismissKeyboard() {

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}