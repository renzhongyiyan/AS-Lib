package com.iyuba.core.microclass.protocol;

import java.io.IOException;

import android.util.Log;

import com.iyuba.configation.Constant;
import com.iyuba.core.common.network.xml.XmlSerializer;
import com.iyuba.core.common.protocol.BaseHttpResponse;
import com.iyuba.core.common.protocol.BaseXMLRequest;



public class PayRecordRequest extends BaseXMLRequest {
	private String appId;// 213

	/**
	 * 鏍规嵁userId璇锋眰鏄惁宸蹭粯璐?
	 * 
	 * @param userId
	 * @param record
	 */
	public PayRecordRequest(String userId) {

		this.appId = Constant.APPID;		
		setAbsoluteURI("http://app.iyuba.com/pay/recordApi.jsp?userId="
				+ userId + "&appId=" + appId + "&productId=0");
		Log.e("record","http://app.iyuba.com/pay/recordApi.jsp?userId="
			+ userId + "&appId=" + appId + "&productId=0");
		
	}

	@Override
	protected void fillBody(XmlSerializer serializer) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new PayRecordResponse();
	}

}

