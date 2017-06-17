/**
 * 
 */
package com.iyuba.core.discover.protocol;

import com.iyuba.core.common.protocol.BaseJSONResponse;
import com.iyuba.core.discover.sqlite.mode.BlogContent;
import com.iyuba.core.discover.sqlite.mode.BlogInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author lmy
 *
 */
public class PersonalBlogResponse extends BaseJSONResponse {

	public String result;// 返回代码
	public String message;// 返回信息
	public JSONArray data;



	protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
		// TODO Auto-generated method stub
		JSONObject jsonBody = null;
		try {
			jsonBody = new JSONObject(bodyElement);
		} catch (JSONException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
		try{
			result=jsonBody.getString("result");
		}catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			message = jsonBody.getString("message");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

		return true;
	}




}

