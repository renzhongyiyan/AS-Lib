package com.iyuba.core.teacher.protocol;

import android.util.Log;

import com.iyuba.core.common.protocol.BaseHttpResponse;
import com.iyuba.core.common.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者：renzhy on 17/7/8 17:22
 * 邮箱：renzhongyigoo@gmail.com
 */
public class GetAgreeListRequest extends BaseJSONRequest{
    private String format = "json";

        public GetAgreeListRequest(int qid,int pageNum,int pageCount) {
            Log.e("GetAgreeListRequest", "http://http://www.iyuba.com/question/getAgreeList.jsp?"
                    +"type=questionid"
                    + "&format=" + format
                    + "&typeid=" + qid
                    + "&pageNum=" + pageNum
                    + "&pageCount=" + pageCount);

            setAbsoluteURI("http://http://www.iyuba.com/question/getAgreeList.jsp?"
                    +"type=questionid"
                    + "&format=" + format
                    + "&typeid=" + qid
                    + "&pageNum=" + pageNum
                    + "&pageCount=" + pageCount);
        }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {
        return new GetAgreeListResponse();
    }
}
