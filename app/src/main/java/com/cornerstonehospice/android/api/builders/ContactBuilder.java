package com.cornerstonehospice.android.api.builders;

import com.cornerstonehospice.android.api.requests.ContactRequest;
import com.cornerstonehospice.android.api.results.ContactResults;
import com.google.gson.reflect.TypeToken;
import com.we.common.api.data.request.BaseDelegatorRequest;
import com.we.common.api.data.request.IRequestType;
import com.we.common.api.data.results.DataResult;
import com.we.common.api.http.results.HttpResult;
import com.we.common.builders.json.CommonJsonBuilder;

import java.lang.reflect.Type;
import java.util.List;

public class ContactBuilder extends AppBuilder {

    public DataResult<ContactResults> getContacts(ContactRequest request){

        HttpResult httpResult 				= 		httpHelper.getString(URLBuilder.getContactURL(), getTokenHeaders());
        DataResult<ContactResults> result	= 		new DataResult<ContactResults>();
        result.successful 					= 		isResultOk(httpResult);
        Type contactList				 	= 		new TypeToken<List<ContactResults>>() {}.getType();
        result.entities						= 		isResultOk(httpResult) ? CommonJsonBuilder.getListForJson(httpResult.result, contactList) : null;
        return result;
    }

    public <T> DataResult<T> execute(BaseDelegatorRequest dataRequest) {

        switch ((WebRequestType)dataRequest.requestType) {
            case GET_CONTACTS:
                return (DataResult<T>) getContacts((ContactRequest)dataRequest);
            default:
                return null;
        }
    }

    public static enum WebRequestType implements IRequestType{
        GET_CONTACTS;
    }
}
