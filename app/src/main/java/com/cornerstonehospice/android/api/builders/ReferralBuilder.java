package com.cornerstonehospice.android.api.builders;

import android.util.Log;

import com.cornerstonehospice.android.api.requests.ReferalDataRequest;
import com.cornerstonehospice.android.json.ReferralBean;
import com.we.common.api.data.request.BaseDelegatorRequest;
import com.we.common.api.data.request.IRequestType;
import com.we.common.api.data.results.DataResult;
import com.we.common.api.http.results.HttpResult;
import com.we.common.builders.json.CommonJsonBuilder;
import com.we.common.utils.WELogger;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;

import java.io.ByteArrayInputStream;
import java.util.Objects;

public class ReferralBuilder extends AppBuilder {
	public static final String TAG = "ReferralBuilder";
	
	@SuppressWarnings("unchecked")
	public <T> DataResult<T> execute(BaseDelegatorRequest dataRequest) {

        if (Objects.requireNonNull((RequestType) dataRequest.requestType) == RequestType.POST_REFERRAL)
		{
			Log.d("If_Else","If" + dataRequest);
            return (DataResult<T>) postReferral((ReferalDataRequest) dataRequest);
        }
		Log.d("If_Else","Else");
        return null;
    }
	
	public DataResult<ReferralBean> postReferral(ReferalDataRequest request){

		MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		ContentBody bodyStr = null;
		ContentBody cbFile = null;
		if (request.imageBytes != null) {
			cbFile = new InputStreamBody(new ByteArrayInputStream(request.imageBytes), "image/jpeg");
			multipartEntity.addPart("referral_pic", cbFile);
		}
		bodyStr = new InputStreamBody(new ByteArrayInputStream(Objects.requireNonNull(CommonJsonBuilder.getJsonForEntity(request.referalRequestBody.referralBean)).getBytes()), "multipart/form-data");
		multipartEntity.addPart("referral_email_body", bodyStr);

		HttpResult httpResult 				= 	httpHelper.postString(URLBuilder.getPostReferralDataUrl(request.emailRecipient), multipartEntity.toString(), null);
		DataResult<ReferralBean> result	= 	new DataResult<ReferralBean>();
		result.successful					= 	isResultOk(httpResult);
		WELogger.infoLog(TAG, "createMultipartAndPost :: Reponse on posting : " + result.successful);

		return result;
	}

	public static enum RequestType implements IRequestType{
		POST_REFERRAL;
	}
}
