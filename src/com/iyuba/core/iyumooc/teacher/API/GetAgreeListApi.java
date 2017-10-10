package com.iyuba.core.iyumooc.teacher.API;

import com.iyuba.core.iyumooc.teacher.bean.AgreeListBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 17/7/8 17:49
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface GetAgreeListApi  {
    @GET("question/getAgreeList.jsp")
    Call<AgreeListBean> getAgreeList(@Query("type") String type,
                                     @Query("typeid") String typeid,
                                     @Query("format") String format,
                                     @Query("pageNum") int pageNum,
                                     @Query("pageCount") int pageCount);
}
