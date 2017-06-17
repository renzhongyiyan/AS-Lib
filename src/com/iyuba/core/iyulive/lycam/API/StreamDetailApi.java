package com.iyuba.core.iyulive.lycam.API;

import android.support.annotation.NonNull;

import com.iyuba.core.iyulive.lycam.bean.StreamInfoModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * 作者：renzhy on 16/7/30 11:50
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface StreamDetailApi {
	@GET("/streams/{streamId}")
	Call<StreamInfoModel> getStreamInfo(
			@NonNull @Header("Authorization") String authorization,
			@Path("streamId") String streamId
	);
}
