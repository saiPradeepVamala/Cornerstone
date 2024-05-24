package com.cornerstonehospice.android.api.requests;

import android.content.Context;

import com.we.common.api.data.request.BaseDelegatorRequest;


public class CriteriaDataRequest extends BaseDelegatorRequest {

    Context mContext;
    public CriteriaDataRequest(Context ctx){
        mContext = ctx;
    }

    public Context getContext(){
        return mContext;
    }

}
