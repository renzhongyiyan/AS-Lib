package com.iyuba.core.teacher.protocol;
import org.json.JSONException;
import org.json.JSONObject;

import com.iyuba.core.common.protocol.BaseJSONResponse;

public class CommentResponse extends BaseJSONResponse {

	public String result;
	public String message;
	
	@Override
	protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
		JSONObject jsonBody = null;
		try {
			jsonBody = new JSONObject(bodyElement);
			result = jsonBody.getString("result");
			message = jsonBody.getString("message");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return true;
	}

}
