/**
 * 
 */
package com.iyuba.core.common.protocol.message;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iyuba.core.common.protocol.VOABaseJsonResponse;
import com.iyuba.core.me.sqlite.mode.DoingsInfo;

/**
 * @author yao
 * 
 */
public class ResponseDoingsInfo extends VOABaseJsonResponse {
	public String result;// 返回代码
	public String message;// 返回信息
	public String uid;// 用户id
	public String username;// 用户名
	public String counts;// 当前页总共的评论数
	public JSONArray data;
	public ArrayList<DoingsInfo> doingslist;

	@Override
	protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
		// TODO Auto-generated method stub
		JSONObject jsonBody = null;
		try {
			jsonBody = new JSONObject(bodyElement);
		} catch (JSONException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		doingslist = new ArrayList<DoingsInfo>();
		// blogContent=new BlogContent();
		try {
			result = jsonBody.getString("result");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			message = jsonBody.getString("message");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (result.equals("301")) {
			try {
				System.out.println("blogInfo.blogCounts"
						+ jsonBody.getString("counts"));
				counts = jsonBody.getString("counts");

			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try {
				uid = jsonBody.getString("uid");
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try {
				username = jsonBody.getString("username");
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			try {
				data = jsonBody.getJSONArray("data");
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (data != null && data.length() != 0) {
				int size = data.length();
				DoingsInfo doingsInfo;
				JSONObject jsonObject;
				for (int i = 0; i < size; i++) {
					try {
						doingsInfo = new DoingsInfo();
						jsonObject = ((JSONObject) data.opt(i));
						doingsInfo.message = jsonObject.getString("message");
						doingsInfo.doid = jsonObject.getString("doid");
						doingsInfo.dateline = jsonObject.getString("dateline");
						doingsInfo.from = jsonObject.getString("from");
						doingsInfo.ip = jsonObject.getString("ip");
						doingsInfo.replynum = jsonObject.getString("replynum");
						doingsInfo.username = username;
						doingsInfo.uid = uid;

						doingslist.add(doingsInfo);
					} catch (JSONException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}

			}

		}
		return true;
	}
}
