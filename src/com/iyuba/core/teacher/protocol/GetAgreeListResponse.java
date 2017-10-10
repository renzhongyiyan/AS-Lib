package com.iyuba.core.teacher.protocol;

import com.iyuba.core.common.protocol.BaseJSONResponse;

import org.json.JSONObject;

/**
 * 作者：renzhy on 17/7/8 17:47
 * 邮箱：renzhongyigoo@gmail.com
 */
public class GetAgreeListResponse extends BaseJSONResponse {
    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        return false;
    }
}
