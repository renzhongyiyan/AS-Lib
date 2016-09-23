package com.iyuba.core.iyulive.API;


import com.iyuba.core.iyulive.bean.CheckIyubiRspBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/8/15 16:37
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface CheckIyubiApi {
	@GET("pay/checkApi.jsp")
	Call<CheckIyubiRspBean> getIyubiAmount(
			@Query("userId") String userId,
			@Query("format") String format
	);
}
