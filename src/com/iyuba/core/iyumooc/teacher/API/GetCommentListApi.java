package com.iyuba.core.iyumooc.teacher.API;

import com.iyuba.core.iyumooc.teacher.bean.CommentListBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 17/7/20 20:35
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface GetCommentListApi {
    @GET("question/getQuestionDetail.jsp?")
    Call<CommentListBean> getCommentList(@Query("format") String format,
                                         @Query("authortype") String authortype,
                                         @Query("questionid") String questionid);
}
