package com.cornerstonehospice.android.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cornerstonehospice.R;
import com.cornerstonehospice.android.utils.AppConstants;

/**
 * @author Shashi
 */

public class PreviewActivity extends AppCompatActivity {

    private static final String TAG = PreviewActivity.class.getName() ;
    private ImageView mPreviewImage;
    private byte[] mImageBytes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar();
        setContentView(R.layout.activity_preview);
        init();
        setupIDsViews();
        setUpUI();
    }

    private void init() {
        Intent intent = getIntent();
        if(intent != null){
            mImageBytes =  intent.getByteArrayExtra(AppConstants.PREVIEW_IMG_BYTES_KEY);
        }
    }

    private void setUpUI() {
        Glide.with(this).load(mImageBytes).into(mPreviewImage);
    }

    private void setUpActionBar(){
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setElevation(0);
        Drawable mActionBarColorDrawable = new ColorDrawable(getResources().getColor(R.color.cornerstone_blue4));
        bar.setBackgroundDrawable(mActionBarColorDrawable);
//        mActionBarColorDrawable.setAlpha(0);
        bar.setTitle(getResources().getString(R.string.preview_title));
        bar.setDisplayHomeAsUpEnabled(true);
    }


    private void setupIDsViews() {
        mPreviewImage = (ImageView) findViewById(R.id.preview_image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        }

        return super.onOptionsItemSelected(item);
    }
}
