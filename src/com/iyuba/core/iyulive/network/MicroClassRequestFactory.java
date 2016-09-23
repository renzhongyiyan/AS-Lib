package com.iyuba.core.iyulive.network;


import com.iyuba.core.iyulive.API.CoursePackApi;
import com.iyuba.core.iyulive.API.CourseTypeApi;
import com.iyuba.core.iyulive.API.SlidePicApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * 作者：renzhy on 16/6/22 16:45
 * 邮箱：renzhongyigoo@gmail.com
 */
public class MicroClassRequestFactory {

	private static CoursePackApi coursePackApi;
	private static CourseTypeApi courseTypeApi;
	private static SlidePicApi slidePicApi;

	private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
	private static OkHttpClient okHttpClient = new OkHttpClient();
	private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();

	public static void initOkHttpClient(){
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(10, TimeUnit.SECONDS)
				.readTimeout(10, TimeUnit.SECONDS)
				.addInterceptor(interceptor)
				.build();
	}

	public static CoursePackApi getCoursePackApi(){
		if(coursePackApi == null){
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://class.iyuba.com/")
					.addConverterFactory(gsonConverterFactory)
					.build();

			coursePackApi = retrofit.create(CoursePackApi.class);
		}
		return coursePackApi;
	}

	public static CourseTypeApi getCourseTypeApi(){
		if(coursePackApi == null){
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://class.iyuba.com/")
					.addConverterFactory(gsonConverterFactory)
					.build();

			courseTypeApi = retrofit.create(CourseTypeApi.class);
		}
		return courseTypeApi;
	}

	public static SlidePicApi getSlidePicApi(){
		if(slidePicApi == null){
			initOkHttpClient();
			Retrofit retrofit = new Retrofit.Builder()
					.client(okHttpClient)
					.baseUrl("http://app.iyuba.com/")
					.addConverterFactory(gsonConverterFactory)
					.build();

			slidePicApi = retrofit.create(SlidePicApi.class);
		}
		return slidePicApi;
	}

}
