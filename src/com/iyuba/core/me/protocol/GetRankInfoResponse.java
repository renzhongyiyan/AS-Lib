package com.iyuba.core.me.protocol;

import android.util.Log;

import com.iyuba.core.common.util.GsonUtils;
import com.iyuba.core.me.sqlite.mode.RankUser;
import com.iyuba.http.toolbox.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */

public class GetRankInfoResponse extends BaseJSONResponse {

    public String myWpm = "";
    public String myImgSrc = "";
    public String myId = "";
    public String myRanking = "";
    public String myCnt = "";
    public String result = "";
    public String message = "";
    public String myName = "";
    public String myWords = "";
    public List<RankUser> rankUsers = new ArrayList<RankUser>();

    @Override
    protected boolean extractBody(JSONObject headerElement, String bodyElement) {
        Log.e("GetRank","====================");
        try {
            JSONObject jsonRoot = new JSONObject(bodyElement);
            message = jsonRoot.getString("message");

            if (message.equals("Success")) {
                myWpm = jsonRoot.getString("mywpm");
                myImgSrc = jsonRoot.getString("myimgSrc");
                myId = jsonRoot.getString("myid");
                myRanking = jsonRoot.getString("myranking");
                myCnt = jsonRoot.getString("mycnt");
                result = jsonRoot.getString("result");
                myName = jsonRoot.getString("myname");
                myWords = jsonRoot.getString("mywords");

                rankUsers = GsonUtils.toObjectList(jsonRoot.getString("data"), RankUser.class);
//                for (Iterator itr = comments.iterator(); itr.hasNext(); ) {
//                    Comment ct = (Comment) itr.next();
//                    ct.produceBackList();
//                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
