package com.iyuba.core.iyulive.API;


import com.iyuba.core.iyulive.bean.RegistResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/8/8 16:00
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface RegistByPhoneApi {
	@GET("v2/api.iyuba")
	Call<RegistResponse> registByPhone(
			@Query("protocol") String protocol,
			@Query("platform") String platform,
			@Query("username") String username,
			@Query("password") String password,
			@Query("mobile") String email,
			@Query("app") String appName,
			@Query("format") String format,
			@Query("sign") String sign
	);
}
