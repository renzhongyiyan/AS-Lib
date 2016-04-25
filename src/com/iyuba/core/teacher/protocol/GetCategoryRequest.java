package com.iyuba.core.teacher.protocol;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.iyuba.core.common.protocol.BaseHttpResponse;
import com.iyuba.core.common.protocol.BaseJSONRequest;

public class GetCategoryRequest  extends BaseJSONRequest {


	public GetCategoryRequest( ) {
		setAbsoluteURI("http://www.iyuba.com/question/getCategoryList.jsp");
		Log.e("iyuba", "http://www.iyuba.com/question/getCategoryList.jsp");
	}

	@Override
	protected void fillBody(JSONObject jsonObject) throws JSONException {
	}

	@Override
	public BaseHttpResponse createResponse() {
		return new GetCategoryResponse();
	}

}
