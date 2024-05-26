package com.newsstand;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.SparseArrayCompat;
import androidx.viewpager.widget.ViewPager;

import com.cornerstonehospice.R;
import com.cornerstonehospice.android.activities.CriteriaListActivity;
import com.cornerstonehospice.android.api.builders.ContactBuilder;
import com.cornerstonehospice.android.api.builders.CriteriaBuilder;
import com.cornerstonehospice.android.api.requests.ContactRequest;
import com.cornerstonehospice.android.api.requests.CriteriaDataRequest;
import com.cornerstonehospice.android.api.results.ContactResults;
import com.cornerstonehospice.android.api.results.CriteriaDataResult;
import com.cornerstonehospice.android.manager.SharedPreferenceManager;
import com.cornerstonehospice.android.utils.AppConstants;
import com.nineoldandroids.view.ViewHelper;
import com.we.common.api.data.results.DataResult;
import com.we.common.async.DataApiAsyncTask;
import com.we.common.builders.json.CommonJsonBuilder;
import com.we.common.utils.WELogger;

import java.util.List;
/**
 * @author rameshemandi
 */
public class SlidingViewPagerMainActivity extends AppCompatActivity implements ScrollTabHolder, ViewPager.OnPageChangeListener {

    //private static AccelerateDecelerateInterpolator sSmoothInterpolator = new AccelerateDecelerateInterpolator();
    private static String TAG = SlidingViewPagerMainActivity.class.getName();
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE 		= 100;

    private ImageView mHeaderPicture;
    private View mHeader;

    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private ContentPagerAdapter mPagerAdapter;
    private Gallery mGallery;

    private int mActionBarHeight;
    private int mMinHeaderHeight;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    private ImageView mHeaderLogo;

    private RectF mRect1 = new RectF();
    private RectF mRect2 = new RectF();

    private TypedValue mTypedValue = new TypedValue();
    ColorDrawable mActionBarColorDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar();
        initConstants();
        setContentView(R.layout.headered_viewpager_activity);
        initUi();
        mPagerAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        setPagerAdapter(mPagerAdapter);
        getContactInfo();

    }

    private void setUpActionBar() {
        ActionBar bar = getSupportActionBar();
        bar.setElevation(0);
        mActionBarColorDrawable = new ColorDrawable(getResources().getColor(R.color.cornerstone_blue4));
        bar.setBackgroundDrawable(mActionBarColorDrawable);
        mActionBarColorDrawable.setAlpha(0);
        bar.setTitle(getResources().getString(R.string.dashboard_dashboard_title_txt));
    }

    private void initUi() {
        mHeaderPicture = (ImageView) findViewById(R.id.header_picture);
        mHeaderLogo = (ImageView) findViewById(R.id.header_logo);
        mHeader = findViewById(R.id.header);

        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(5);

//        mGallery = (Gallery) findViewById(R.id.gallery);
        mGallery.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    private void initConstants() {
        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mMinHeaderHeight + getActionBarHeight();
    }

    private void setPagerAdapter(ContentPagerAdapter adapter) {
        adapter.setTabHolderScrollingContent(this);
        mViewPager.setAdapter(adapter);
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(this);
        mGallery.setAdapter(new HeaderGalleryAdapter(this));
        mViewPager.setCurrentItem(1, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // nothing
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // nothing
    }

    @Override
    public void onPageSelected(int position) {
        SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mPagerAdapter.getScrollTabHolders();
        ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);

        if (mHeader != null && currentHolder != null) {
            currentHolder.adjustScroll((int) (mHeader.getHeight() + ViewHelper.getTranslationY(mHeader)));
        }
        setGalleryPosition(position);
    }

    private void setGalleryPosition(int position) {
        if (position == 0) {
        } else if (position == mGallery.getAdapter().getCount() - 1) {
            mGallery.setSelection(position - 1);
        } else {
            mGallery.setSelection(position);
        }
        ((HeaderGalleryAdapter)mGallery.getAdapter()).setHighlightedItem(position);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
        if (mViewPager.getCurrentItem() == pagePosition) {
            int scrollY = getScrollY(view);
            ViewHelper.setTranslationY(mHeader, Math.max(-scrollY, mMinHeaderTranslation));
            float ratio = clamp(ViewHelper.getTranslationY(mHeader) / mMinHeaderTranslation, 0.0f, 1.0f);
            //	interpolate(mHeaderLogo, getActionBarIconView(), sSmoothInterpolator.getInterpolation(ratio));
            // setActionBarAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
            mActionBarColorDrawable.setAlpha(getAlphaforActionBar(scrollY));
        }
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        // nothing
    }

    private int getAlphaforActionBar(int scrollY) {
        int minDist = 0, maxDist = mHeaderHeight - (2 * getActionBarHeight());
//		mPagerSlidingTabStrip.setTabBackground(R.color.cornerstone_blue4);
//		mPagerSlidingTabStrip.setIndicatorColorResource(R.color.cornerstone_blue2);
        if (scrollY > maxDist) {
//			mPagerSlidingTabStrip.setTabBackground(R.color.cornerstone_blue3);
            return 255;
        } else if (scrollY < minDist) {
            return 0;
        } else {
            int alpha = 0;
            alpha = (int) ((255.0 / maxDist) * scrollY);
            return alpha;
        }
    }

    public int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mHeaderHeight;
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    private void interpolate(View view1, View view2, float interpolation) {
        getOnScreenRect(mRect1, view1);
        getOnScreenRect(mRect2, view2);

        float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
        float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
        float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
        float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));

        ViewHelper.setTranslationX(view1, translationX);
        ViewHelper.setTranslationY(view1, translationY - ViewHelper.getTranslationY(mHeader));
        ViewHelper.setScaleX(view1, scaleX);
        ViewHelper.setScaleY(view1, scaleY);
    }

    private RectF getOnScreenRect(RectF rect, View view) {
        if (view != null && rect != null)
            rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        return rect;
    }

    public int getActionBarHeight() {
        if (mActionBarHeight != 0) {
            return mActionBarHeight;
        }
//        getTheme().resolveAttribute(R.attr.actionBarSize, mTypedValue, true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());
        return mActionBarHeight;
    }


	/*

	Functions related to the activity interaction are here
	 */

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
            Toast.makeText(this, getString(R.string.no_email_launcher_defined), Toast.LENGTH_SHORT).show();
        }
    }

    private void getContactInfo() {
        WELogger.infoLog(TAG, "getContactInfo() :: Getting contact information from Server");
        ContactRequest contactRequest = new ContactRequest();
        contactRequest.requestDelegate = new ContactBuilder();
        contactRequest.requestType = ContactBuilder.WebRequestType.GET_CONTACTS;
        new DataApiAsyncTask(true, this, contactAPIHandler, null).execute(contactRequest);
    }

    private void getCriteria() {
        // TODO: get list of criteria from server
        WELogger.infoLog(TAG, "getCriteriaInfo() :: Getting criteria information from Server");
        CriteriaDataRequest contactRequest 		= 		new CriteriaDataRequest(getApplicationContext());
        contactRequest.requestDelegate 			= 		new CriteriaBuilder();
        contactRequest.requestType 				= 		CriteriaBuilder.RequestType.GET_CRITERIA;
        new DataApiAsyncTask(true, this, criteriaAPIHandler, getProgressDialog()).execute(contactRequest);

    }

    private ProgressDialog getProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        return progressDialog;
    }

    private Handler criteriaAPIHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            DataResult<CriteriaDataResult> criteriaResults	=	(DataResult<CriteriaDataResult>)msg.obj;
            String str = new CommonJsonBuilder().getJsonForEntity(criteriaResults.entity);
            navigateToCriteriaListActivity(str);
        }
    };

    private void navigateToCriteriaListActivity(String payload) {
        Intent i = new Intent (this, CriteriaListActivity.class);
        i.putExtra(AppConstants.CRITERIA_DATA, payload);
        startActivity(i);
    }

    private Handler contactAPIHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            DataResult<List<ContactResults>> contactResults = (DataResult<List<ContactResults>>) msg.obj;
           // Shashi: COmmenting due to access token issue.
//            if (contactResults.successful) {        //Process success results
//                ContactResults contactResultsObj = (ContactResults) contactResults.entities.get(0);
//                WELogger.infoLog(TAG, "contactAPIHandler :: Response from contacts API "+contactResults.entities);
//                WELogger.infoLog(TAG, "contactAPIHandler :: Email and Phone no. recieved from Server \n Email : " + contactResultsObj.email + "\n Phone no : " + contactResultsObj.phone);
//                SharedPreferenceManager sharedPref = SharedPreferenceManager.getInstance();
//                sharedPref.saveString(AppConstants.KEY_EMAIL, contactResultsObj.email);
//                sharedPref.saveString(AppConstants.KEY_PHONE_NO, contactResultsObj.phone);
//            } else {                //Process failure and show the proper message to user
//                WELogger.infoLog(TAG, "contactAPIHandler :: We did not receive the Contact details something went wrong");
//                showLongToast(getString(R.string.cannot_process_requst));
//            }
        }
    };

    private void showLongToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
