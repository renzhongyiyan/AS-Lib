package com.iyuba.core.me.protocol;

import android.util.Log;

import com.iyuba.core.common.util.MD5;
import com.iyuba.http.BaseHttpResponse;
import com.iyuba.http.toolbox.BaseJSONRequest;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/3.
 */

public class GetRankInfoRequest extends BaseJSONRequest {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private String date = sdf.format(new Date());

    public GetRankInfoRequest(String uid, String type, String start, String total) {
//        String sign = URLEncoder.encode(uid + type + start + total + date);
        String sign = MD5.getMD5ofStr(uid + type + start + total + date);
        setAbsoluteURI("http://cms.iyuba.com/newsApi/getNewsRanking.jsp?uid=" +
                uid +
                "&type=" +
                type +
                "&start=" +
                start +
                "&total=" +
                total +
                "&sign=" +
                sign);
        setMethod(Method.GET);
        Log.e("GetRankInfoRequest", "http://cms.iyuba.com/newsApi/getNewsRanking.jsp?uid=" +
                uid +
                "&type=" +
                type +
                "&start=" +
                start +
                "&total=" +
                total +
                "&sign=" +
                sign);

    }

    @Override
    public BaseHttpResponse createResponse() {
        return new GetRankInfoResponse();
    }
}
