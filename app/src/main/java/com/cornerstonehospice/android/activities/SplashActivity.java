package com.cornerstonehospice.android.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cornerstonehospice.R;
import com.cornerstonehospice.android.manager.SharedPreferenceManager;
import com.cornerstonehospice.android.utils.AppConstants;
import com.we.common.utils.WELogger;

/**
 * Starting activity of corner stone, where it will check for the initializations and other module initializations
 */

public class SplashActivity extends BaseActivity {

	private int progress = 0;
	private ProgressBar pb;
	private boolean isBackpressed = false;
	private ImageView splashBgImage;
	private TextView splashText;
	private TextView splashTextTwo;

	@Override
	protected int getLayoutResource() {
		WELogger.infoLog("getLayoutResource() :: ", "getting layout resources ");
		return R.layout.activity_splash;
	}

	@Override
	protected int getToolbarResource() {
		WELogger.infoLog("getToolbarResource() :: ", "getting toolbar resources ");
		return 0;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!isTaskRoot()){
			finish();
			return;
		}
		WELogger.infoLog("onCreate ", "In");
		if(!SharedPreferenceManager.getInstance().getBool(AppConstants.KEY_SHORTCUT_CREATED, false)){
			addHomeScreenShortcut(getApplicationContext());
		}

		setupCrashReportingTool();
		setUpScreen();
		new CountDownTimer(2000, 100) {
			@Override
			public void onFinish() {
				pb.setProgress(100);
				setUpApp();
			}
			@Override
			public void onTick(long millisUntilFinished) {
				pb.setProgress(progress);
				progress +=10;
			}
		}.start();
	}

	public void addHomeScreenShortcut(Context context) {
		Intent shortcutIntent = new Intent(this, SplashActivity.class);
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		Intent addIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_shortcut_name));
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher));
		addIntent.putExtra("duplicate", false);
		addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		context.sendBroadcast(addIntent);
		SharedPreferenceManager.getInstance().saveBool(AppConstants.KEY_SHORTCUT_CREATED, true);
	}

	@Override
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			splashBgImage.setImageResource(R.drawable.cornerstone_bg_land);
		} else {
			splashBgImage.setImageResource(R.drawable.cornerstone_bg);
		}
	}

	private void setupCrashReportingTool() {
		//TODO: Sertup ACRA
	}


	/**
	 * To setup application such as checking the connections and creating DB
	 */
	private void setUpApp() {

//		new SetupAsyncTask(this).execute();
		goToNextScreen();
	}

	/**
	 * TO setup layout and the screen settings 
	 */
	private void setUpScreen() {
		pb =(ProgressBar)findViewById(R.id.splash_pb);
		splashBgImage = (ImageView) findViewById(R.id.isplash_company_mageView);
		splashText = (TextView) findViewById(R.id.splash_screen_title_text);
		splashTextTwo = (TextView) findViewById(R.id.splash_screen_title_text_two);
		Typeface tf = Typeface.createFromAsset(getAssets(),"big-caslon-medium.ttf");
		splashText.setTypeface(tf,Typeface.BOLD);
		splashTextTwo.setTypeface(tf,Typeface.BOLD);
	}

	/**
	 * TO redirect next screen
	 */
	public void goToNextScreen() {
		//startActivity(new Intent(getApplicationContext(), MainDashboardActivity.class));
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
		finish();
	}


	/**
	 * It will close the app if the network is not available
	 */
	public void closeApp() {
		Toast.makeText(getApplicationContext(), getString(R.string.splash_no_internet_shutting_down_txt),
				Toast.LENGTH_SHORT).show();
		new CountDownTimer(3000, 1000) {
			@Override
			public void onFinish() {
				finish();
			}

			@Override
			public void onTick(long millisUntilFinished) {
			}
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_BACK==keyCode) 
			isBackpressed= true;
		return super.onKeyDown(keyCode, event);
	}
}
