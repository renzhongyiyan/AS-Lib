/**
 * 
 */
package com.iyuba.core.common.protocol.message;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iyuba.core.common.protocol.BaseJSONResponse;
import com.iyuba.core.me.sqlite.mode.SearchItem;

/**
 * @author
 * 
 */
public class ResponseSearchList extends BaseJSONResponse {

	public String result;// 返回代码
	public String message;// 返回信息
	public JSONArray data;
	public ArrayList<SearchItem> list;
	public int firstPage;
	public int prevPage;
	public int nextPage;
	public int lastPage;
	public int total;

	@Override
	protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
		// TODO Auto-generated method stub
		list = new ArrayList<SearchItem>();
		try {
			JSONObject jsonObjectRootRoot = new JSONObject(bodyElement);
			result = jsonObjectRootRoot.getString("result");
			if (result.equals("591")) {
				// JSONObject jsonObjectRootRoot = new JSONObject(bodyElement);
				// result = jsonObjectRootRoot.getString("result");
				total = jsonObjectRootRoot.getInt("total");
				prevPage = jsonObjectRootRoot.getInt("prevPage");
				nextPage = jsonObjectRootRoot.getInt("nextPage");
				lastPage = jsonObjectRootRoot.getInt("lastPage");
				data = jsonObjectRootRoot.getJSONArray("data");
				if (data != null && data.length() != 0) {
					int size = data.length();
					SearchItem item;
					JSONObject jsonObject;
					for (int i = 0; i < size; i++) {
						item = new SearchItem();
						jsonObject = ((JSONObject) data.opt(i));
						item.uid = jsonObject.getString("uid");
						item.username = jsonObject.getString("username");
						item.similar = jsonObject.getString("similar");
						list.add(item);
					}
				}
			} else if (result.endsWith("590")) {
				return false;

			}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return true;
	}
}
