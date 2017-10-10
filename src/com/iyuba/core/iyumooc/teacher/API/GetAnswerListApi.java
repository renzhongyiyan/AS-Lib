package com.iyuba.core.iyumooc.teacher.API;

import com.iyuba.core.iyumooc.teacher.bean.AgreeListBean;
import com.iyuba.core.iyumooc.teacher.bean.AnswerListBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 17/7/8 17:49
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface GetAnswerListApi {
    @GET("question/getQuestionDetail.jsp")
    Call<AnswerListBean> getAnswerList(@Query("format") String format,
                                       @Query("questionid") String questionid);
}
