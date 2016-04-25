package com.iyuba.core.me.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.iyuba.core.common.protocol.BaseHttpResponse;
import com.iyuba.core.common.protocol.BaseJSONRequest;

public class UserRankRequest extends BaseJSONRequest {

	public UserRankRequest(String uid) {
		setAbsoluteURI("http://daxue.iyuba.com/ecollege/getPaiming.jsp?format=json&uid="
				+ uid);

	}

	@Override
	protected void fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new UserRankResponse();
	}

}
