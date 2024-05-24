package com.cornerstonehospice.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cornerstonehospice.R;

/**
 * Created by ramesh on 31/08/15.
 */
public class ScalesFragment extends Fragment {

    private WebView mWebview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_scales, null);
        mWebview = (WebView) v.findViewById(R.id.log_web_view);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebview.loadUrl("file:///android_asset/scales.html");
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.getSettings().setBuiltInZoomControls(true);
    }

    public static ScalesFragment newInstance() {

        Bundle args = new Bundle();

        ScalesFragment fragment = new ScalesFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
