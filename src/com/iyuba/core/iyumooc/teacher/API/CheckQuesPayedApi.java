package com.iyuba.core.iyumooc.teacher.API;

import com.iyuba.core.iyumooc.teacher.bean.QuesPayedRecordBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 17/7/22 13:10
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface CheckQuesPayedApi {
    @GET("pay/apiGetPayQuestionRecord.jsp")
    Call<QuesPayedRecordBean> checkQuestionPayed(
            @Query("userId") String userId,
            @Query("appId") String appId,
            @Query("productId") String productId,
            @Query("packId") String packId,
            @Query("sign")  String sign);
}
