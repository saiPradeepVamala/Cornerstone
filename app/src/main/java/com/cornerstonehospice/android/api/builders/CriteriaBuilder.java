package com.cornerstonehospice.android.api.builders;

import com.cornerstonehospice.android.api.requests.CriteriaDataRequest;
import com.cornerstonehospice.android.api.results.CriteriaDataResult;
import com.cornerstonehospice.android.utils.AppUtils;
import com.we.common.api.data.request.BaseDelegatorRequest;
import com.we.common.api.data.request.IRequestType;
import com.we.common.api.data.results.DataResult;
import com.we.common.builders.json.CommonJsonBuilder;

public class CriteriaBuilder extends AppBuilder {
	
	@SuppressWarnings("unchecked")
	public <T> DataResult<T> execute(BaseDelegatorRequest dataRequest) {
	
		switch ((RequestType)dataRequest.requestType) {
		case GET_CRITERIA:
			return (DataResult<T>) getCriteria((CriteriaDataRequest) dataRequest);
		default:
			return null;
		} 
	}
	
	public DataResult<CriteriaDataResult> getCriteria(CriteriaDataRequest request){
//		HttpResult httpResult 				= 		/httpHelper.getString(URLBuilder.getCriteriaURL(), getTokenHeaders());
		DataResult<CriteriaDataResult> result	= 		new DataResult<CriteriaDataResult>();
//		result.successful 					= 		isResultOk(httpResult);
		String str = AppUtils.getJsonFromFile("server_response/criteria_results.json", request.getContext());
		result.entity						= 		CommonJsonBuilder.getEntityForJson(str, CriteriaDataResult.class);//isResultOk(httpResult) ? new CommonJsonBuilder().getListForJson(httpResult.result, contactList) : null;
		return result;
	}
	
	
	
	public enum RequestType implements IRequestType{
		GET_CRITERIA;
	}
}
