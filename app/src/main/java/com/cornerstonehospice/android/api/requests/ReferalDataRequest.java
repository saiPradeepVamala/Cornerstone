package com.cornerstonehospice.android.api.requests;

import com.we.common.api.data.request.BaseDelegatorRequest;


public class ReferalDataRequest extends BaseDelegatorRequest {

	public ReferralRequestBody referalRequestBody = new ReferralRequestBody();

	public byte[] imageBytes;

	public String emailRecipient;

}
