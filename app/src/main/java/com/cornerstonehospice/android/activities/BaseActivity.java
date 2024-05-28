package com.cornerstonehospice.android.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.cornerstonehospice.R;
import com.we.common.utils.WELogger;


public abstract class BaseActivity extends Activity {

    public static final String LOG_TAG = BaseActivity.class.getSimpleName();
    private Toolbar mToolBar;

    protected abstract int getLayoutResource();                //Toolbar settings

    protected abstract int getToolbarResource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        setToolbar(getToolbarResource());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /* no usages for these method
    public void showShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }*/

    public void showLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private ProgressDialog getProgressDialogInternal() {
        // Inlined variable declaration
        return new ProgressDialog(this);
    }

    /*  no usages for these method
    protected ProgressDialog getProgressDialog() {
        ProgressDialog progressDialog = getProgressDialogInternal();
        progressDialog.setMessage(getString(R.string.please_wait));
        return progressDialog;
    }*/

    protected ProgressDialog getProgressDialog(String message) {
        ProgressDialog progressDialog = getProgressDialogInternal();
        progressDialog.setMessage(message);
        return progressDialog;
    }

    /**
     * Retrieves a dialog with no border or background, containing
     * the given view.
     *
     * @param fragment The view to show inside the dialog.
     */
    /* no usages for these method
    protected Dialog getDialogContainingView(View view) {
        Dialog dlg = new Dialog(this);
        dlg.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(view);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return dlg;
    }*/

    //	/**  Use this method when we are going with the Fragments
//	 * Generic method to launch fragment by replacing instead of adding
//	 * @param fragment
//	 */

    public void launchFragmentByReplacing(Fragment fragment) {
        WELogger.infoLog(LOG_TAG, "BaseActivity :: launchingFragment " + fragment.getClass().getSimpleName());
        FragmentManager fragmentManager = this.getFragmentManager();

        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
            fragmentTransaction
                    .setTransition(FragmentTransaction.TRANSIT_NONE);
            fragmentTransaction.replace(R.id.base_fragment_framelayout_holder,
                    fragment, fragment.getClass().getName());
            fragmentTransaction.commitAllowingStateLoss();
        }
    }


    protected void setToolbar(int id) {
        mToolBar = (Toolbar) findViewById(id);
        WELogger.infoLog(LOG_TAG, " setToolbar  " + mToolBar);
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
        }
    }

    private void setSupportActionBar(Toolbar mToolBar) {
    }


    /**
     * Method to get toolbar for the subclassed activities
     *
     * @return mToolBar
     */
    protected Toolbar getToolBar() {
        return mToolBar;
    }

}
