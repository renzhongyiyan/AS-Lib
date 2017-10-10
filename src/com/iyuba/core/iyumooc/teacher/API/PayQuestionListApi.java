package com.iyuba.core.iyumooc.teacher.API;

import com.iyuba.core.iyumooc.teacher.bean.PayQuestionListBean;
import com.iyuba.core.iyumooc.teacher.bean.QuestionListBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 17/6/18 17:24
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface PayQuestionListApi {
    //type=?
    //category1=quesAbilityType
    //category2=quesAppType
    //sort=0、1按时间或者按精华
    //price=0、1免费或收费
    @GET("question/getQuestionList.jsp")
    Call<PayQuestionListBean> getQuesList(@Query("format") String format, @Query("type") int type,
                                          @Query("category1") int category1,@Query("category2") int category2,
                                          @Query("pageNum") int pageNum, @Query("pageCount") int pageCount,
                                          @Query("sort") int sort, @Query("price") int price);
}
