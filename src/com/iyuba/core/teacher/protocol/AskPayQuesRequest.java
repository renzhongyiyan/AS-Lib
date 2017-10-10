package com.iyuba.core.teacher.protocol;

import android.util.Log;

import com.iyuba.core.common.protocol.BaseHttpResponse;
import com.iyuba.core.common.protocol.BaseJSONRequest;
import com.iyuba.core.common.util.TextAttr;

import org.json.JSONException;
import org.json.JSONObject;

public class AskPayQuesRequest extends BaseJSONRequest {
	private String format = "json";

	public AskPayQuesRequest(String uid, String username, String desc, int type, String askuid, int price) {
		String uri;
		uri = "http://www.iyuba.com/question/askQuestion.jsp?"
				+ "&format=" + format
				+ "&uid=" + uid
				+ "&username=" + TextAttr.encode(TextAttr.encode(TextAttr.encode(username)))
				+ "&question=" + desc
				+ "&category1=" + type
				+ "&price=" + price;

		if(!askuid.equals(""))
			uri = uri +"&tuid="+askuid;

		Log.e("iyuba", uri);
		setAbsoluteURI(uri);
	}

	public AskPayQuesRequest(String uid, String username, String desc, int type, int appType, String askuid,int price) {
		String uri = null;
		uri = "http://www.iyuba.com/question/askQuestion.jsp?"
				+ "&format=" + format
				+ "&uid=" + uid
				+ "&username=" + TextAttr.encode(TextAttr.encode(TextAttr.encode(username)))
				+ "&question=" + desc
				+ "&category1=" + type
				+ "&category2=" + appType
				+ "&price=" + price;
			
		if(!askuid.equals("")) 
			uri = uri +"&tuid="+askuid;
		 
	
		Log.e("iyuba", uri);
		setAbsoluteURI(uri);
	}
	
	@Override
	public BaseHttpResponse createResponse() {
		return new AskPayQuesResponse ();
	}

	@Override
	protected void fillBody(JSONObject jsonObject) throws JSONException {
		
	}

}
