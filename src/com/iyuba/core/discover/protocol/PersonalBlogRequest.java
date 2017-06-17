package com.iyuba.core.discover.protocol;

import android.util.Log;

import com.iyuba.core.common.protocol.BaseHttpResponse;
import com.iyuba.core.common.protocol.BaseJSONRequest;
import com.iyuba.core.common.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

//个人中心中日志内容的获取
public class PersonalBlogRequest extends BaseJSONRequest {

	{
		requestId = 0;
	}


	public PersonalBlogRequest(String blogid) {
		
		MD5 m=new MD5();
		setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol=200065&blogId="+blogid+
				"&sign=" + MD5.getMD5ofStr("20006"+blogid+"iyubaV2")+"&id=242141");
		
		Log.d("BlogRequest","http://http://api.iyuba.com.cn/v2/api.iyuba?protocol=200065&blogId="+blogid+
				"&sign=" + MD5.getMD5ofStr("20006"+blogid+"iyubaV2")+"&id=242141");
		
	}

	@Override
	protected void fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new PersonalBlogResponse();
	}

}

