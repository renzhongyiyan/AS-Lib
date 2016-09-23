package com.iyuba.core.iyulive.API;

import com.iyuba.core.iyulive.bean.LivePackListBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/7/19 17:59
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface MyLivePackApi {
	@GET("getClass.iyuba")
	Call<LivePackListBean> getMyLivePackList(@Query("protocol") String protocol,
											 @Query("uid") String uid,
											 @Query("pageNumber") int pageNumber,
											 @Query("pageCounts") int pageCounts,
											 @Query("sign") String sign,
											 @Query("zhibo") String zhibo);
}
